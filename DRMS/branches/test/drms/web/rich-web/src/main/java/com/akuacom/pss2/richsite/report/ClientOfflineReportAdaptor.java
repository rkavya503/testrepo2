package com.akuacom.pss2.richsite.report;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.email.ClientOfflineNotificationManager;
import com.akuacom.pss2.report.ClientOfflineReportManager;
import com.akuacom.pss2.report.entities.ClientOfflineReport;
import com.akuacom.pss2.report.entities.ClientOfflineReportEntity;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;
import com.akuacom.pss2.richsite.util.ClientOfflineReportEntityComparator;
import com.akuacom.utils.DateUtil;

public class ClientOfflineReportAdaptor implements Serializable {
	private static final long serialVersionUID = 1196474488204480688L;
	
	public List<ClientOfflineReport> getReportSummary(){
		List<ClientOfflineReport> result = new ArrayList<ClientOfflineReport>();
		result = getReport().getReportSummary();
		return result;
	}
	public ClientOfflineReport getReport(String uuid){
		ClientOfflineReport result = getReport().getReport(uuid);
		return result;
	}
//	public List<ClientOfflineReportEntity> getReportDetail(Date date){
//		List<ClientOfflineReportEntity> result = new ArrayList<ClientOfflineReportEntity>();
//		result = getReport().getReportDetail(date);
//		return result;
//	}
	public void generateReport(Date date){
		getReport().generateReport(date);
	}
	private ClientOfflineReportManager report;
	private ClientOfflineReportManager getReport() {
		if(report==null){
			report = EJBFactory.getBean(ClientOfflineReportManager.class);	
		}
		return report;
	}
	private ClientOfflineNotificationManager notificator;
	
	
	/**
	 * @return the notificator
	 */
	public ClientOfflineNotificationManager getNotificator() {
		if(notificator==null){
			notificator = EJBFactory.getBean(ClientOfflineNotificationManager.class);	
		}
		return notificator;
	}
	
	public void generateNotification(){
		getNotificator().businessLogic();
	}
	public void exportToExcel(List<ClientOfflineReportEntity> detail,Date searchDate) throws IOException{
	   	 String filename = "clientOfflineReport.csv";
	        FacesContext fc = FacesContext.getCurrentInstance();
	        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
	        response.reset();
	        response.addHeader("cache-control", "must-revalidate");
	        response.setContentType("application/octet_stream");
	        response.setHeader("Content-Disposition", "attachment; filename=\""+ filename + "\"");
	        String result =generateContent(detail,searchDate);
	        response.getWriter().print(result);
	        fc.responseComplete();
	 }
	    /**
	     * Private function for generate the excel file content.
	     * @return
	     */
	    private String generateContent(List<ClientOfflineReportEntity> detail,Date searchDate){
	    	String result ="";
	    	StringBuffer sb = new StringBuffer();
	    	sb.append("Generate Date,");
	    	if(searchDate!=null){
	    		AkuacomJSFUtil.writeNext(sb, DateUtil.format(searchDate)) ;
	    		sb.append("\n");
	    	}else{
	    		sb.append(",\n");
	    	}
	    	//sb.append("Client Name,Participant Name,Account Number,Start Time,End Time,Last Contact,Offline Duration,\n");
	    	sb.append("Client Name,Participant Name,Account Number,Last Contact,Offline Day(s),Offline Hour(s),Offline Minute(s),Offline Total Minute(s),\n");
	    	for(ClientOfflineReportEntity client : detail){
	    		AkuacomJSFUtil.writeNext(sb, client.getClientName()) ;
	    		AkuacomJSFUtil.writeNext(sb, client.getParticipantName());
	    		AkuacomJSFUtil.writeNext(sb, client.getAccountNumber()) ;
	    		Date dateStart = client.getStartTime();
//	    		if(dateStart==null){
//	    			sb.append(",");	
//	    		}else{
//	    			sb.append(getDateString(dateStart)+",");
//	    		}
//	    		Date dateEnd = client.getEndTime();
//	    		if(dateEnd==null){
//	    			sb.append(",");	
//	    		}else{
//	    			sb.append(getDateString(dateEnd)+",");
//	    		}
	    		Date date = client.getLastContact();
	    		if(date==null){
	    			sb.append(",");	
	    		}else{
	    			sb.append(getDateString(date)+",");
	    		}
	    		buildEntity(client);
	    		AkuacomJSFUtil.writeNext(sb, client.getOfflineDays()) ;
	    		AkuacomJSFUtil.writeNext(sb, client.getOfflineHours()) ;
	    		AkuacomJSFUtil.writeNext(sb, client.getOfflineMins()) ;
	    		AkuacomJSFUtil.writeNext(sb, client.getOfflineTotalMins()) ;
	    		sb.append("\n");
	    	}
	    	
	    	
	    	result = sb.toString();
	    	return result;
	    }
	    
	    private void buildEntity(ClientOfflineReportEntity e){
	    	if(e!=null){
	    		String offline = e.getOffline();
	    		if(offline.contains("Day")&&offline.contains("Hour")&&offline.contains("Minute")){
	    			String[] t1= offline.split(",");
	    			long dayInterval=Long.valueOf(t1[0].substring(0,t1[0].length()-6).trim());
	    			long hourInterval=Long.valueOf(t1[1].substring(0,t1[1].length()-7).trim());
	    			long minInterval=Long.valueOf(t1[2].substring(0,t1[2].length()-9).trim());
	    			long totalMins = dayInterval*24*60+hourInterval*60+minInterval;
	    			e.setOfflineDays(String.valueOf(dayInterval));
	    			e.setOfflineHours(String.valueOf(hourInterval));
	    			e.setOfflineMins(String.valueOf(minInterval));
	    			e.setOfflineTotalMins(String.valueOf(totalMins));
	    		}
	    	}
	    }
	    
	    private static String timeFormatType = "MM/dd/yyyy HH:mm:ss";
	    public static synchronized String getDateString(Date date) {
	        return new SimpleDateFormat(timeFormatType).format(date);
	    }
	    public List<ClientOfflineReportEntity> sortDetails(List<ClientOfflineReportEntity> detail){
	    	Map<String,List<ClientOfflineReportEntity>> result = new TreeMap<String,List<ClientOfflineReportEntity>>();
	    	for(ClientOfflineReportEntity instance:detail){
	    		String clientName = instance.getClientName();
	    		if(result.containsKey(clientName)){
	    			result.get(clientName).add(instance);
	    		}else{
	    			result.put(clientName, new ArrayList<ClientOfflineReportEntity>());
	    			result.get(clientName).add(instance);
	    		}
	    	}
	    	Set<String> keySet = result.keySet();
	    	List<ClientOfflineReportEntity> list = new ArrayList<ClientOfflineReportEntity>();
	    	for(String key:keySet){
	    		list.addAll(sortList(result.get(key)));
	    	}
	    	return list;
	    }
	    
	    public static List<ClientOfflineReportEntity> sortList(List<ClientOfflineReportEntity> aItems) {
			if(aItems!=null){
				ClientOfflineReportEntityComparator comparator = new ClientOfflineReportEntityComparator();
				
				Collections.sort(aItems, comparator);
			}
			return aItems;
		}
}
