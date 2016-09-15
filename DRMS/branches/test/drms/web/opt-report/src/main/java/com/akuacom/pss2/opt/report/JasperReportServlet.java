package com.akuacom.pss2.opt.report;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.history.HistoryEvent;
import com.akuacom.pss2.history.HistoryEventParticipant;
import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.pss2.history.vo.ReportEvent;
import com.akuacom.pss2.history.vo.ReportEventParticipant;
import com.akuacom.pss2.history.vo.ReportSummaryVo;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.utils.lang.DateUtil;

import javax.ejb.EJB;
public class JasperReportServlet extends HttpServlet {
	private static final long serialVersionUID = 5799219560638883610L;
	private static final Logger log = Logger.getLogger(JasperReportServlet.class);
	
	private HistoryReportManager hrm;
	
	public HistoryReportManager getReportManager(){
		if(hrm==null)
			hrm =EJBFactory.getBean(HistoryReportManager.class);
		return hrm;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String report = request.getParameter("reportName");

		if ("scorecard".equals(report)) {
			String eventName = request.getParameter("eventName");
//			String startTime = request.getParameter("startTime");
//			String endTime = request.getParameter("endTime");
			
			byte[] bytes = null;
			ServletOutputStream servletOutputStream = response.getOutputStream();
			
			try {
				// get data
				if (eventName == null) {
					throw new Exception("Event Name is empty");
				}
				
//				Date startDate = new Date(Long.parseLong(startTime));
//				Date endDate = new Date(Long.parseLong(endTime));
				
		        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
		        String dateformat=systemManager.getPss2Features().getDateFormat();
				
				if (dateformat==null)
					dateformat="MM/dd/yyyy";
				DateFormat format = new SimpleDateFormat(dateformat);
				
				HistoryEvent he = getReportManager().getEventByNameAsHistoryEvent(eventName);
				if (he == null) {
					throw new Exception("Did not retrieve any historical event");
				}
				
				String advNotification = "0 Hours";
				int notDiff = 0;
				if(he.getIssueTime()!=null && he.getStartTime()!=null)
					notDiff = DateUtil.minuteOffset(he.getIssueTime(), he.getStartTime());
				if (notDiff > 0) {
					int hours = notDiff / 60;
					int min = notDiff % 60;
					StringBuilder sb = new StringBuilder();
					sb.append(hours);
					if (hours == 1) {
						sb.append(" Hour and ");
					} else {
						sb.append(" Hours and ");
					}
					sb.append(min);
					if (min == 1) {
						sb.append(" Minute");
					} else {
						sb.append(" Minutes");
					}
					advNotification = sb.toString();
				}
				
				String reportDuration = "0 Hours";
				int durDiff = 0;
				if(he.getStartTime()!=null && he.getEndTime()!=null)
					durDiff = DateUtil.minuteOffset(he.getStartTime(), he.getEndTime());
				if (durDiff > 0) {
					int hours = durDiff / 60;
					int min = durDiff % 60;
					StringBuilder sb = new StringBuilder();
					sb.append(hours);
					if (hours == 1) {
						sb.append(" Hour and ");
					} else {
						sb.append(" Hours and ");
					}
					sb.append(min);
					if (min == 1) {
						sb.append(" Minute");
					} else {
						sb.append(" Minutes");
					}
					reportDuration = sb.toString();
				}
				
				List<HistoryEventParticipant> heps = getReportManager().getParticipantsForEventAsHistoryEventParticipant(eventName);
				
				if (heps == null || heps.size() < 1) {
					throw new Exception("Did not retrieve any historical event participants");
				}
				
				double rteRegShed = 0.0;
				double rteAveActShed = 0.0;

				// check clients
				ArrayList<String> tmpParts = new ArrayList<String>();
				for (HistoryEventParticipant hep : heps) {
					if (hep.getClient().booleanValue()) {
//						if (hep.getParticipation() != null && hep.getParticipation().intValue() == 0) {
								if ( hep.getPercentage() != null && hep.getPercentage().doubleValue() > 0.0) {
//							parts.add(hep.getParticipantName());
							if (!tmpParts.contains(hep.getParent())) {
								tmpParts.add(hep.getParent());
							}
						}
					}
				}
				
				// check participants
				ArrayList<String> parts = new ArrayList<String>();
				for (HistoryEventParticipant hep : heps) {
					if (!hep.getClient().booleanValue()) {
						if (hep.getParticipation() != null && hep.getParticipation().intValue() == 0) {
							if (tmpParts.contains(hep.getParticipantName())) {
								parts.add(hep.getParticipantName());
								
								if (hep.getRegisteredShed() != null) {
									rteRegShed += hep.getRegisteredShed().doubleValue();
								}
							}
						}
					}
				}
				
				List<PDataEntry> usage = getReportManager().findRealTimeUsageDataEntryList(parts, he.getEndTime());
				List<PDataEntry> baseline = getReportManager().findForecastUsageDataEntryList(parts, he.getEndTime());
				
//				if ((usage == null || usage.size() < 1) && (baseline == null || baseline.size() < 1)) {
//					throw new Exception("No usage related data is available for this event");
//				}
				
				ArrayList<HashMap<String, Object>> reportRows = new ArrayList<HashMap<String, Object>>();
				
				double maxValue = 0.0;
				for (PDataEntry de : usage) {
					HashMap<String, Object> row = new HashMap<String, Object>();
					row.put("time", de.getTime());
					row.put("actualValue", de.getValue());
					if (de.getValue() != null && de.getValue().doubleValue() > maxValue) {
						maxValue = de.getValue().doubleValue();
					}
					
					row.put("baselineValue", null);
					row.put("startLine", null);
					row.put("endLine", null);
					
					reportRows.add(row);
				}
				
				for (PDataEntry de : baseline) {
					HashMap<String, Object> row = getMapForTime(de.getTime(), reportRows);
					if (row == null) {
						row = new HashMap<String, Object>();
						row.put("time", de.getTime());
						row.put("actualValue", null);
						row.put("startLine", null);
						row.put("endLine", null);
						reportRows.add(row);
					}
					row.put("baselineValue", de.getValue());
					if (de.getValue() != null && de.getValue().doubleValue() > maxValue) {
						maxValue = de.getValue().doubleValue();
					}
				}
				
				// event start lines /////////////////////////////////////////
				//truncate seconds
				Date startTime = DateUtils.truncate(he.getStartTime(), Calendar.MINUTE);
				
				Date targetDate1 = new Date(startTime.getTime() - 9000);
				Date targetDate2 = new Date(startTime.getTime() - 6000);
				Date targetDate3 = new Date(startTime.getTime() - 3000);
				Date targetDate4 = new Date(startTime.getTime());
				Date targetDate5 = new Date(startTime.getTime() + 3000);
				Date targetDate6 = new Date(startTime.getTime() + 6000);
				Date targetDate7 = new Date(startTime.getTime() + 9000);
				
				HashMap<String, Object> row = getMapForTime(targetDate1, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate1.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("endLine", null);
					reportRows.add(row);
				}
				row.put("startLine", new Double(0.0));
				
				row = getMapForTime(targetDate2, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate2.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("endLine", null);
					reportRows.add(row);
				}
				row.put("startLine", new Double(0.0));
				
				row = getMapForTime(targetDate3, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate3.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("endLine", null);
					reportRows.add(row);
				}
				row.put("startLine", new Double(maxValue));
				
				row = getMapForTime(targetDate4, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate4.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("endLine", null);
					reportRows.add(row);
				}
				row.put("startLine", new Double(maxValue));
				
				row = getMapForTime(targetDate5, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate5.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("endLine", null);
					reportRows.add(row);
				}
				row.put("startLine", new Double(maxValue));
				
				row = getMapForTime(targetDate6, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate6.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("endLine", null);
					reportRows.add(row);
				}
				row.put("startLine", new Double(0.0));
				
				row = getMapForTime(targetDate7, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate7.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("endLine", null);
					reportRows.add(row);
				}
				row.put("startLine", new Double(0.0));
				
				// event end lines //////////////////////////////////////
				//truncate seconds
				Date endTime = DateUtils.truncate(he.getEndTime(), Calendar.MINUTE);
				
				targetDate1 = new Date(endTime.getTime() - 9000);
				targetDate2 = new Date(endTime.getTime() - 6000);
				targetDate3 = new Date(endTime.getTime() - 3000);
				targetDate4 = new Date(endTime.getTime());
				targetDate5 = new Date(endTime.getTime() + 3000);
				targetDate6 = new Date(endTime.getTime() + 6000);
				targetDate7 = new Date(endTime.getTime() + 9000);
				
				row = getMapForTime(targetDate1, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate1.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("startLine", null);
					reportRows.add(row);
				}
				row.put("endLine", new Double(0.0));
				
				row = getMapForTime(targetDate2, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate2.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("startLine", null);
					reportRows.add(row);
				}
				row.put("endLine", new Double(0.0));
				
				row = getMapForTime(targetDate3, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate3.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("startLine", null);
					reportRows.add(row);
				}
				row.put("endLine", new Double(maxValue));
				
				row = getMapForTime(targetDate4, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate4.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("startLine", null);
					reportRows.add(row);
				}
				row.put("endLine", new Double(maxValue));
				
				row = getMapForTime(targetDate5, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate5.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("startLine", null);
					reportRows.add(row);
				}
				row.put("endLine", new Double(maxValue));
				
				row = getMapForTime(targetDate6, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate6.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("startLine", null);
					reportRows.add(row);
				}
				row.put("endLine", new Double(0.0));
				
				row = getMapForTime(targetDate7, reportRows);
				if (row == null) {
					row = new HashMap<String, Object>();
					row.put("time", new Timestamp(targetDate7.getTime()));
					row.put("actualValue", null);
					row.put("baselineValue", null);
					row.put("startLine", null);
					reportRows.add(row);
				}
				row.put("endLine", new Double(0.0));
				///// end of start/end event lines
				
				JRDataSource dataSource = new JRMapCollectionDataSource(reportRows);
				
				ReportEvent re = new ReportEvent();
				re.setEventName(eventName);
				re.setProgramName(he.getProgramName());
				re.setStartTime(he.getStartTime());
				re.setEndTime(he.getEndTime());
				List<ReportEventParticipant> eps =  new ArrayList<ReportEventParticipant>();
				for(String part:parts){
					ReportEventParticipant rp = new ReportEventParticipant();
					rp.setParticipation(0);
					rp.setParticipantName(part);
					eps.add(rp);
				}
				re.setParticipants(eps);
				ArrayList<ReportEvent> reList = new ArrayList<ReportEvent>();
				reList.add(re);
				
				List<ReportSummaryVo> summary = getReportManager().getReportSummary(parts, he.getStartTime(), reList);
				for (ReportSummaryVo vo : summary) {
					if ("During Event".equals(vo.getCatalog())) {
						rteAveActShed = vo.getShedAvg();
						break;
					}
				}
				
				String totProgramRegShed = (Math.round(he.getRegisteredShed() * 100.0) / 100.0) + " kW";
				String availableRegShed = (Math.round(rteRegShed * 100.0) / 100.0) + " kW";
				String rteAverageActualShed = (Math.round(rteAveActShed * 100.0) / 100.0) + " kW";
				
				format = new SimpleDateFormat(dateformat+" HH:mm"); 
				String eventStart = format.format(he.getStartTime());
				String eventEnd = format.format(he.getEndTime());
				String numProgPart = "0";
				if (he.getNumProgramParticipants() != null) {
					numProgPart = he.getNumProgramParticipants().toString();
				}
				
				HashMap<String, Object> params = new HashMap<String,Object>();
				params.put("programName", he.getProgramName());
				params.put("eventName", eventName);
				params.put("eventStartDate", eventStart);
				params.put("eventEndDate", eventEnd);
				params.put("advNotification", advNotification);
				params.put("reportDuration", reportDuration);
				params.put("numProgPart", numProgPart);
				params.put("avProgPart", Integer.toString(parts.size()));
				params.put("totProgramRegShed", totProgramRegShed);
				params.put("availableRegShed", availableRegShed);
				params.put("rteAverageActualShed", rteAverageActualShed);
				params.put("rteRegShed", new Double(Math.round(rteRegShed * 100.0) / 100.0));
				params.put("rteAveActShed", new Double(Math.round(rteAveActShed * 100.0) / 100.0));
				
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				String startOfDay = format.format(DateUtil.getStartOfDay(he.getStartTime()));
				String endOfDay = format.format(DateUtil.getEndOfDay(he.getEndTime()));
				
				params.put("startOfDay", startOfDay);
				params.put("endOfDay", endOfDay);
				
				String fn = getServletConfig().getServletContext().getRealPath("/reports/scorecard.jrxml");
				
				JasperReport jasperReport = JasperCompileManager.compileReport(fn);
				bytes = JasperRunManager.runReportToPdf(jasperReport, params, dataSource); 
				
				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				response.setHeader("Pragma", "public");
				response.setHeader("Cache-Control", "public");
	
				servletOutputStream.write(bytes, 0, bytes.length);
				servletOutputStream.flush();
				servletOutputStream.close();
			} catch (JRException e) {
				// display stack trace in the browser
				log.error("JasperReportServlet unexpected error from Jasper", e);
				response.setContentType("text/html");
				response.getOutputStream().print(getErrorMessage("JasperReportServlet unexpected error from Jasper: " + e.getMessage()));
			} catch (Exception e) {
				// display stack trace in the browser
				log.error("JasperReportServlet unexpected error", e);
				response.setContentType("text/html");
				response.getOutputStream().print(getErrorMessage(e.getMessage()));
			}
		} else {
			log.error("JasperReportServlet invalid report request");
			response.setContentType("text/html");
			response.getOutputStream().print("<H1>Invalid report type</H1>");
		}
	}
	
	private HashMap<String, Object> getMapForTime(Date target, ArrayList<HashMap<String, Object>> rows) {
		HashMap<String, Object> res = null;
		
		for (HashMap<String, Object> row : rows) {
			if (target.equals(row.get("time"))){
				return row;
			}
		}
		
		return res;
	}
	
	private String getErrorMessage(String error) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">");
		sb.append("<html lang=\"en-US\" xml:lang=\"en-US\">");
		sb.append("<head><title>DRAS Participant</title></head>");
		sb.append("<body>");
		sb.append("<style>");
		sb.append("#header {");
		sb.append("	float: left;");
		sb.append("	width: 100%;");
		sb.append("	font-size: .8em;");
		sb.append("	line-height: normal;");
		sb.append("	text-align: left;");
		sb.append("	overflow: auto;");
		sb.append("	margin: 0 0 8px 0;");
		sb.append("	background: #DAE0D2 url(\"../images/layout/header_background-wide.jpg\")");
		sb.append("		repeat-x center bottom;");
		sb.append("}");
		sb.append("body {");
		sb.append("	color: #000000;");
		sb.append("	text-align: left;");
		sb.append("	margin: 0px;");
		sb.append("	font-family: verdana, arial, sans-serif;");
		sb.append("	font-size: small;");
		sb.append("	background-color: #FFFFFF;");
		sb.append("}");
		sb.append("#page_footer {");
		sb.append("	background-color: #fff;");
		sb.append("	clear: both;");
		sb.append("	border-top: solid #5d819d 1px;");
		sb.append("	margin: 10px 10px 20px 10px;");
		sb.append("}");
		sb.append("#page_footer .akua {");
		sb.append("	padding-top: 5px;");
		sb.append("	padding-bottom: 10px;");
		sb.append("	margin-bottom: 0px;");
		sb.append("	text-align: right;");
		sb.append("	font-size: 0.9em;");
		sb.append("	color: #93abbe;");
		sb.append("	letter-spacing: 1px;");
		sb.append("}");
		sb.append("</style>");
		sb.append("<div id=\"header\"><h1>DRAS Customer Interface</h1></div>");
		sb.append("<div id=\"error-body\"><strong>An occurred generating the Scorecard.</strong>");
		sb.append("<br><br>");
		sb.append(error);
		sb.append("<br><br>");
		sb.append("Please <a href=\"/pss2.utility/jsp/reports/scoreCard.jsf\">try again</a></div>");
		sb.append("<div id=\"page_footer\">");
		sb.append("<div class=\"akua\">");
		sb.append("<p>Powered by: <a href=\"http://www.akuacom.com\" target=\"_blank\">");
		sb.append("<img src=\"/facdash/images/layout/Akuacom-logo.gif\" alt=\"Akuacom\" width=\"100\" height=\"44\" longdesc=\"http://www.akuacom.com\" /></a></p>");
		sb.append("<p>&copy; 2007-2011 <a href=\"http://www.akuacom.com\">Akuacom</a> All Rights Reserved.</p>");
	    sb.append("</div></body></html>");
		
		return sb.toString();
	}
}