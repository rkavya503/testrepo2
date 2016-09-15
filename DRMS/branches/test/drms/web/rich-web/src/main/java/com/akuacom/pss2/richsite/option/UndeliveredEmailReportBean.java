package com.akuacom.pss2.richsite.option;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.email.EmailReportEntity;
import com.akuacom.pss2.email.UndeliveredEmailManager;
import com.akuacom.utils.DateUtil;

public class UndeliveredEmailReportBean implements Serializable{
	//TODO: startDate endDate also need time
	private static final long serialVersionUID = 1196674488204480649L;
	private static UndeliveredEmailManager emailManager = EJB3Factory.getBean(UndeliveredEmailManager.class);
	private List<EmailReportEntity> entities = new ArrayList<EmailReportEntity>();
	
	public List<EmailReportEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<EmailReportEntity> entities) {
		this.entities = entities;
	}

	
	private EventTiming eventTime;
	public EventTiming getEventTiming() {
		if(eventTime==null){
			eventTime = new EventTiming();
			//initialize with default value in program constraint
			eventTime.setStartHour(0);
			eventTime.setStartMin(0);
			eventTime.setStartSec(0);
			
			Calendar now = Calendar.getInstance();			
			eventTime.setEndHour(now.get(Calendar.HOUR_OF_DAY));
			eventTime.setEndMin(now.get(Calendar.MINUTE));
			eventTime.setEndSec(now.get(Calendar.SECOND));
			eventTime.setStartDate(now.getTime());
			eventTime.setEndDate(now.getTime());
		}
		return eventTime;
	}
	
	public UndeliveredEmailReportBean(){
		
	}
	
	public void testEmail(){
		emailManager.sendReportEmail();
	}

	public void search(){
		if(eventTime.getStartTime()!=null&&eventTime.getEndTime()!=null){
			try {
				entities = emailManager.getUndeliveredEmails(eventTime.getStartTime(), eventTime.getEndTime());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void export() throws IOException{
		String filename = "undeliveredEmailReport.csv";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
        response.reset();
        response.addHeader("cache-control", "must-revalidate");
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""+ filename + "\"");
        String result =generateContent();
        response.getWriter().print(result);
        fc.responseComplete();
	}
	private String generateContent(){
    	String result ="";
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append("Time of Email,Subject Line,Contact Name,Email Address,Participant Name,Client Name,\n");
    	for(EmailReportEntity entity : entities){
    		
    		writeNext(sb, DateUtil.format(entity.getCreateTime()));
    		writeNext(sb,entity.getSubject());
    		writeNext(sb,entity.getContactName());
    		writeNext(sb,entity.getContactAddress());
    		writeNext(sb,entity.getParticipantName());
    		writeNext(sb,entity.getClientName());
    		
    		sb.append("\n");
    	}
    	
    	
    	result = sb.toString();
    	return result;
    }

	
	//************************************************
	private static final char QUOTECHAR ='"';
	private static final char ESCAPECHAR ='"';
	private static final char SEPARATOR =',';
	private static final String NULLCHAR ="null";
	private static final int INITIAL_STRING_SIZE = 128;
	
    private StringBuffer writeNext(StringBuffer sb, String nextElement) {
    	if(nextElement==null||NULLCHAR.equalsIgnoreCase(nextElement.trim())){
    		sb.append(QUOTECHAR);
    		sb.append(QUOTECHAR);
    	    sb.append(SEPARATOR);
    	    
    	    return sb;
    	}
    	sb.append(QUOTECHAR);
        sb.append(stringContainsSpecialCharacters(nextElement) ? processLine(nextElement) : nextElement);
        sb.append(QUOTECHAR);
        sb.append(SEPARATOR);
        
        return sb;
    }

	private boolean stringContainsSpecialCharacters(String line) {
	    return line.indexOf(QUOTECHAR) != -1;
	}
	
	protected StringBuilder processLine(String nextElement)
	{
		StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
	    for (int j = 0; j < nextElement.length(); j++) {
	        char nextChar = nextElement.charAt(j);
	        if (nextChar == QUOTECHAR) {
	        	sb.append(ESCAPECHAR).append(nextChar);
	        } else {
	            sb.append(nextChar);
	        }
	    }
	    
	    return sb;
	}
	
}
