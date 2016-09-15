/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.event;

import com.akuacom.pss2.core.EmailResourceUtil;
import com.akuacom.pss2.program.scertp.SCERTPEvent;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.utils.lang.DateUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author spierson
 */
public class EventEmailFormatter {
	
	private String emailResourceUtilClassName = null;
	
	
	
	public String getEmailResourceUtilClassName() {
		return emailResourceUtilClassName;
	}

	public void setEmailResourceUtilClassName(String emailResourceUtilClassName) {
		this.emailResourceUtilClassName = emailResourceUtilClassName;
	}

	public EventEmailFormatter(){
		emailResourceUtilClassName = "com.akuacom.pss2.core.EmailResourceUtil";
	}

	public EventEmailFormatter(String emailResourceUtilClassName){
		this.emailResourceUtilClassName = emailResourceUtilClassName;
	}

    protected static final int TAB_SIZE = 6;

    /**
	 * To participant string.
	 *
	 * @return the string
	 */
	public String generateEmailContent(Event event, List<Signal> programSignals, 
		String serverHost, String contentType, boolean isRevision, Date nearTime, Boolean isOptout)
	{
		
		
		List<Long> rowKeys = new ArrayList<Long>();
        List<String> colKeys = new ArrayList<String>();
        Map<String, Map<Long, SignalEntry>> signalMap = new HashMap<String, Map<Long, SignalEntry>>();
        for (Signal programSignal : programSignals) {
        	if(programSignal.getSignalDef().getSignalName().equalsIgnoreCase("pending"))
    		{
        		for(SignalEntry pse : programSignal.getSignalEntries())
        		{
        			if(pse.getParentSignal().getSignalDef().isLevelSignal()
        					&& pse.getParentSignal().getSignalDef().getSignalName().equalsIgnoreCase("pending")
        					&& pse.getLevelValue().equalsIgnoreCase("on"))
        			{
        				nearTime = pse.getTime();
        			}
        		}
    			continue;
    		}
        	Map<Long, SignalEntry> entryMap = new HashMap<Long, SignalEntry>();
            SignalEntry[] signalEntries =
            	programSignal.getSignalEntries().toArray(new SignalEntry[0]);

            signalMap.put(programSignal.getSignalDef().getSignalName(), entryMap);
            for (int j = 0; signalEntries != null && j < signalEntries.length; j++)
            {
                SignalEntry signalEntry = signalEntries[j];
                Long time = signalEntry.getTime().getTime();
                if(!rowKeys.contains(time))
                {
                	rowKeys.add(time);
                }
                entryMap.put(time, signalEntry);

                if(!colKeys.contains(programSignal.getSignalDef().getSignalName()))
                {
                	colKeys.add(programSignal.getSignalDef().getSignalName());
                }
            }

        }

        boolean isHTML = (contentType!=null && contentType.equalsIgnoreCase("text/html"));
        return getEmailBody(event, nearTime, rowKeys, colKeys, signalMap, serverHost, isHTML, isRevision,isOptout);

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
	
	
	
	protected String  getLocalizedString(String key, List<String> variables){
		Class c=null;
		try {
			c = Class.forName(this.getEmailResourceUtilClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Method m= null;
		try {
			m = c.getDeclaredMethod("getLocalizedString", String.class, List.class);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		m.setAccessible(true); //if security settings allow this
		Object o = null;
		try {
			o = m.invoke(null, key, variables);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} //use null if the method is static
		return o==null?null:o.toString() ;
		
	}

}
