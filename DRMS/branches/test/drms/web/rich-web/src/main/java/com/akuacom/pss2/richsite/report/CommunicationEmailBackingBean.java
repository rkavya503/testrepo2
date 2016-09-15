package com.akuacom.pss2.richsite.report;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.report.CommunicationEmailManager;
import com.akuacom.pss2.report.entities.CommunicationEmail;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;

public class CommunicationEmailBackingBean implements Serializable {
	private static final long serialVersionUID = 1196474488208480688L;
	/** The log */
	private static final Logger log = Logger.getLogger(CommunicationEmailBackingBean.class);	
	
	private static List<SelectItem> hourList;
	private static List<SelectItem> minList;
	private Date searchFromDate;
	private int searchFromHour;
    private int searchFromMin;
	private Date searchToDate;
	private int searchToHour;
    private int searchToMin;
    private List<CommunicationEmail> emails = new ArrayList<CommunicationEmail>();
    private String displayNothing="";
	public CommunicationEmailBackingBean(){
		super();
		searchFromDate = construct(new Date(),0,0,0);
		searchFromHour =0;
		searchFromMin = 0;
		searchToDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(searchToDate);
		searchToHour = calendar.get(Calendar.HOUR_OF_DAY);
		searchToMin = calendar.get(Calendar.MINUTE);
		List<CommunicationEmail> list = getManager().find(searchFromDate, searchToDate);
		setEmails(list);
	}
	
	public void search(){
		Date from = getSearchFromDate();
		Date to = getSearchToDate();
		if(from!=null&&to!=null){
			List<CommunicationEmail> list = getManager().find(from, to);
			setEmails(list);
		}
	}	
	public void exportToExcel() throws IOException{
		String filename = "communicationEmail.csv";
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
	private static String timeFormatType = "MM/dd/yyyy HH:mm:ss";
    public static synchronized String getDateString(Date date) {
        return new SimpleDateFormat(timeFormatType).format(date);
    }
	private String generateContent(){
    	String result ="";
    	StringBuffer sb = new StringBuffer();
    	sb.append("From,");
    	if( getSearchFromDate()!=null){
    		AkuacomJSFUtil.writeNext(sb, getDateString( getSearchFromDate())) ;
    		sb.append("\n");
    	}else{
    		sb.append(",\n");
    	}
    	sb.append("To,");
    	if( getSearchToDate()!=null){
    		AkuacomJSFUtil.writeNext(sb, getDateString( getSearchToDate())) ;
    		sb.append("\n");
    	}else{
    		sb.append(",\n");
    	}
    	sb.append("Creation Time,Contact Name,Address,Subject,Participant Name,Client Name,Status,Sent Time,\n");
 
    	for(CommunicationEmail email : emails){
    		Date creationTime = email.getCreationTime();
    		if(creationTime==null){
    			sb.append(",");	
    		}else{
    			sb.append(getDateString(creationTime)+",");
    		}
    		AkuacomJSFUtil.writeNext(sb, email.getContactName());
    		AkuacomJSFUtil.writeNext(sb, email.getEmailAddress()) ;
    		AkuacomJSFUtil.writeNext(sb, email.getSubject()) ;
    		AkuacomJSFUtil.writeNext(sb, email.getParticipantName()) ;
    		AkuacomJSFUtil.writeNext(sb, email.getClientName()) ;
    		AkuacomJSFUtil.writeNext(sb, email.getStatusExpress()) ;
    		Date sendTime = email.getSendTime();
    		if(sendTime==null){
    			sb.append(",");	
    		}else{
    			sb.append(getDateString(sendTime)+",");
    		}

    		sb.append("\n");
    	}
    	
    	
    	result = sb.toString();
    	return result;
    }
	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}
	public List<SelectItem> getHourList() {
		return hourList;
	}

	public List<SelectItem> getMinList() {
		return minList;
	}
	
	static {
		hourList = new ArrayList<SelectItem>();
        for (int i = 0; i < 24; i++) {
        	hourList.add(new SelectItem(i,formatTime(i)));
        }
		
        minList = new ArrayList<SelectItem>();
        for (int i = 0; i < 60; i++) {
        	minList.add(new SelectItem(i,formatTime(i)));
        }
	}
	private static String formatTime(int i){
		if(i<10)
			return "0"+i;
		else
			return i+"";
	}
	private static Date construct(Date date,int hour,int min, int sec){
		if(date==null) return null;
		final Calendar calendar =  Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @return the searchFromDate
	 */
	public Date getSearchFromDate() {
		searchFromDate = construct(searchFromDate,searchFromHour,searchFromMin,0);
		return searchFromDate;
	}

	/**
	 * @param searchFromDate the searchFromDate to set
	 */
	public void setSearchFromDate(Date searchFromDate) {
		this.searchFromDate = searchFromDate;
	}

	/**
	 * @return the searchFromHour
	 */
	public int getSearchFromHour() {
		return searchFromHour;
	}

	/**
	 * @param searchFromHour the searchFromHour to set
	 */
	public void setSearchFromHour(int searchFromHour) {
		this.searchFromHour = searchFromHour;
	}

	/**
	 * @return the searchFromMin
	 */
	public int getSearchFromMin() {
		return searchFromMin;
	}

	/**
	 * @param searchFromMin the searchFromMin to set
	 */
	public void setSearchFromMin(int searchFromMin) {
		this.searchFromMin = searchFromMin;
	}

	/**
	 * @return the searchToDate
	 */
	public Date getSearchToDate() {
		searchToDate = construct(searchToDate,searchToHour,searchToMin,0);
		return searchToDate;
	}

	/**
	 * @param searchToDate the searchToDate to set
	 */
	public void setSearchToDate(Date searchToDate) {
		this.searchToDate = searchToDate;
	}

	/**
	 * @return the searchToHour
	 */
	public int getSearchToHour() {
		return searchToHour;
	}

	/**
	 * @param searchToHour the searchToHour to set
	 */
	public void setSearchToHour(int searchToHour) {
		this.searchToHour = searchToHour;
	}

	/**
	 * @return the searchToMin
	 */
	public int getSearchToMin() {
		return searchToMin;
	}

	/**
	 * @param searchToMin the searchToMin to set
	 */
	public void setSearchToMin(int searchToMin) {
		this.searchToMin = searchToMin;
	}

	/**
	 * @param hourList the hourList to set
	 */
	public static void setHourList(List<SelectItem> hourList) {
		CommunicationEmailBackingBean.hourList = hourList;
	}

	/**
	 * @param minList the minList to set
	 */
	public static void setMinList(List<SelectItem> minList) {
		CommunicationEmailBackingBean.minList = minList;
	}
	private CommunicationEmailManager manager;
	private CommunicationEmailManager getManager() {
		if(manager==null){
			manager = EJBFactory.getBean(CommunicationEmailManager.class);	
		}
		return manager;
	}

	/**
	 * @return the emails
	 */
	public List<CommunicationEmail> getEmails() {
		return emails;
	}

	/**
	 * @param emails the emails to set
	 */
	public void setEmails(List<CommunicationEmail> emails) {
		this.emails = emails;
	}

	/**
	 * @return the displayNothing
	 */
	public String getDisplayNothing() {
		if(emails.size()>0){
			
			displayNothing="Total found "+emails.size()+ " record(s).";
		}else{
			displayNothing="Nothing found to display.";
		}
		return displayNothing;
	}

	/**
	 * @param displayNothing the displayNothing to set
	 */
	public void setDisplayNothing(String displayNothing) {
		this.displayNothing = displayNothing;
	}


	
}
