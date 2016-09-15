package com.akuacom.pss2.event;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akuacom.pss2.core.EmailResourceUtil;
import com.akuacom.pss2.program.scertp.SCERTPEvent;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.utils.lang.DateUtil;

public class SceEventEmailFormatter extends EventEmailFormatter {
	
	public SceEventEmailFormatter(String emailResourceUtilClassName){
		super(emailResourceUtilClassName);
	}
	
	protected String getEmailBody(Event event, Date nearTime, List<Long> rowKeys,
            List<String> columnNames, Map<String, Map<Long,
            SignalEntry>> signalMap, String serverHost, boolean isHTML, boolean isRevision, Boolean isOptout)
    {
        StringBuilder rv = new StringBuilder("");
        String resourceSuffix = "_PLAIN";
        String newLine = "\n";
        if (isHTML) {
            resourceSuffix = "_HTML";
            newLine = "<br>";
        }
        String programType = "";
        boolean isSCERTP = false;
        if(event instanceof SCERTPEvent) {
        	programType = "SCERTP";
            isSCERTP = true;
        }

        boolean skipSignalBlock = false;

        if(isSCERTP) {
            // SCE RTP gets a more involved header
            SCERTPEvent sceEvent = (SCERTPEvent)event;
            List<String> var =new ArrayList<String>();
            String footerKey = "EVENT_PARTICIPANT_EMAIL_HEADER_SCERTP_NORMAL";
            if (isRevision) {
            	
                footerKey = "EVENT_PARTICIPANT_EMAIL_HEADER_SCERTP_REVISION";
                var.add(DateUtil.formatDate(event.getStartTime())); // Event date
                var.add(DateUtil.formatDate(event.getStartTime())); // Event date
                var.add(sceEvent.getTemperatureRange());
            } else if (sceEvent.getReportingWeatherStation() == null) {
            	skipSignalBlock = true;
                footerKey = "EVENT_PARTICIPANT_EMAIL_HEADER_SCERTP_UNAVAILABLE";
                Calendar eventCal = Calendar.getInstance();
                eventCal.setTime(event.getStartTime());
                eventCal.add(Calendar.DATE,-1); // no temperature for the day before the event
            	var.add(DateUtil.formatDate(eventCal.getTime()));   // day before event
                var.add(DateUtil.formatDate(event.getStartTime())); // Event date
            } else {
            
                // normal initial-issue
                var.add(DateUtil.formatDate(event.getStartTime())); // Event date
                var.add(DateFormat.getDateTimeInstance().format(new Date())); // time right now
                var.add(DateUtil.formatDate(event.getStartTime())); // Event date
                var.add(sceEvent.getTemperatureRange());
            }
            
            if(isOptout!=null&&isOptout){
            	footerKey = "EVENT_PARTICIPANT_EMAIL_HEADER_SCERTP_OPT_OUT";           	
            }
            String descrip = getLocalizedString(footerKey,var);
            descrip = descrip.replace("\n", newLine);  // fix up newlines for plain / html
            rv.append(newLine).append(descrip).append(newLine);
        } else {
            // Not SCERTP
            String headerKey = "EVENT_PARTICIPANT_EMAIL_HEADER" + resourceSuffix + (programType.isEmpty() ? "" : "_" + programType);
            List<String> variables = new ArrayList<String>();
            variables.add(event.getProgramName());
            variables.add(DateUtil.format(event.getIssuedTime()));
            variables.add(DateUtil.format(event.getIssuedTime()));
            if(nearTime == null)
            {
            	variables.add(DateUtil.format(event.getIssuedTime()));
            } 
            else
            {
            	variables.add(DateUtil.format(nearTime));
            }
            variables.add(DateUtil.format(event.getStartTime()));
            variables.add(DateUtil.format(event.getEndTime()));
            rv.append(getLocalizedString(headerKey, variables));
        }

        if( !skipSignalBlock) {
            if(columnNames == null || columnNames.size() <= 0) {
                return rv.toString();
            }

            String headerKey = "EVENT_PARTICIPANT_EMAIL_THEADER" + resourceSuffix + (programType.isEmpty() ? "" : "_" + programType);
            List<String> variables = new ArrayList<String>();
            variables.add(DateUtil.formatDate(event.getStartTime()));

            rv.append(getLocalizedString(headerKey, variables));

            if (isHTML) {
                rv.append("<br><table><tr><td width='80'>Hour</td>");
            }
            else {
                rv.append("\nHour");
            }


            Collections.sort(rowKeys);

            for(String column : columnNames)
            {
                // don't make a column for pending signals
                if(column.equalsIgnoreCase("pending")) { continue; }
                
             // don't make a column for price signals
             //   if(column.equalsIgnoreCase("price")) { continue; }
                // capitalize the name of the column
                String capitalizedColumn;
                if (isSCERTP) {
                    //SCERTP has custom display column titles, more than just the signal name
                    String colKey = column.toUpperCase()+"_COLUMN" + (programType.isEmpty() ? "" : "_" + programType);
                    capitalizedColumn = EmailResourceUtil.getLocalizedString(colKey , new ArrayList<String>());
                } else {
                    capitalizedColumn = column.replaceFirst(column.substring(0,1), (column.substring(0,1)).toUpperCase());
                }
                if (isHTML) {
                    rv.append("<td width='80'>").append(capitalizedColumn).append("</td>");
                } else {
                    rv.append("\t\t").append(capitalizedColumn);
                }

                Map<Long, SignalEntry> oneColumn = signalMap.get(column);  // get the signal for this column
                SignalEntry preEntry = null;
                for(Long rowDate : rowKeys)
                {
                    if(!oneColumn.containsKey(rowDate) && preEntry != null)
                    {
                        oneColumn.put(rowDate, preEntry);
                    }
                    preEntry = oneColumn.get(rowDate);
                }
            }
            if (isHTML) { rv.append("</tr>"); }
            else        { rv.append("\n");  }
            Date now = new Date();
            //for(Long rowDate : rowKeys)
            for (int row = 0; row < rowKeys.size(); ++row)
            {
                Long rowDate = rowKeys.get(row);
                Date rowTime = new Date(rowDate);
                if (row < rowKeys.size()-1) {
                    Long nextRowDate = rowKeys.get(row+1);
                    Date nextRowTime = new Date(nextRowDate);
                    if (nextRowTime.before(now)) { continue; }
                }
                // Start a row
                if (isHTML) { rv.append("<tr><td>"); }
                rv.append(DateUtil.format(rowTime,"HH:mm"));
                if (isHTML) { rv.append("</td>"); }
                for(String columnName : columnNames)
                {
                    if(columnName.equalsIgnoreCase("pending")) { continue; }

                    int l = columnName.length() / TAB_SIZE;
                    int k;
                    Map<Long, SignalEntry> oneColumn = signalMap.get(columnName);
                    if(oneColumn.containsKey(rowDate))
                    {
                        // There is a signal entry for this row
                        SignalEntry entry = oneColumn.get(rowDate);
                        String showValue = entry.getValueAsString();
                        if (isHTML) { rv.append("<td>").append(showValue).append("</td>"); }
                        else        { rv.append("\t\t").append(showValue); }
                        k = l - showValue.length() / TAB_SIZE;
                    }
                    else if(columnName.equalsIgnoreCase("mode"))
                    {
                        // it's the mode column, but there's no mode signal entry for this row
                        if (isHTML) { rv.append("<td>default</td>" ); }
                        else        { rv.append("\t\tdefault" ); }
                        k = l - "default".length() / TAB_SIZE;
                    }
                    else
                    {
                        // It's not a mode column AND there's no signal entry
                        if (isHTML) { rv.append("<td>0</td>" ); }
                        else        { rv.append("\t\t0" ); }
                        k = l;
                    }
                    if (!isHTML) {
                        if (k > 0) {
                            for (int i = 0; i < k; i++) {
                                rv.append("\t");
                            }
                        }
                    }
                }
                // End of the row
                if (isHTML) { rv.append("</tr>"); }
                else        { rv.append("\n"); }
            }
            if (isHTML) { rv.append("</table>"); }
        }

        List<String> variables2 = new ArrayList<String>();
        if( isSCERTP ){
            SCERTPEvent sceEvent = (SCERTPEvent)event;
            if (sceEvent.getReportingWeatherStation() != null) {
                String footerKey = "EVENT_PARTICIPANT_EMAIL_FOOTER_SCERTP";
                if (isRevision) { footerKey += "_REVISION"; }
                String footr = getLocalizedString(footerKey,variables2);
                rv.append(newLine).append(newLine).append(footr.replace("\n", newLine));
            }
        } else {
            if(nearTime != null)
        	variables2.add(DateUtil.formatDate(nearTime));
            String footerKey = "EVENT_PARTICIPANT_EMAIL_FOOTER" + resourceSuffix + (programType.isEmpty() ? "" : "_" + programType);
            rv.append(getLocalizedString(footerKey,variables2));
            variables2 = new ArrayList<String>();
            variables2.add(serverHost);
            rv.append(getLocalizedString("EMAIL_FOOTER" + resourceSuffix, variables2));
        }

        return rv.toString();
    }

	
}
