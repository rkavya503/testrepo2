package com.akuacom.pss2.richsite.report;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.akuacom.pss2.report.entities.ClientOfflineReport;
import com.akuacom.pss2.report.entities.ClientOfflineReportEntity;

public class ClientOfflineReportBackingBean implements Serializable {
	private static final long serialVersionUID = 1196474488208480688L;
	/** The log */
	private static final Logger log = Logger.getLogger(ClientOfflineReportBackingBean.class);	
	private List<ClientOfflineReport> summary = new ArrayList<ClientOfflineReport>();
	private List<ClientOfflineReportEntity> detail = new ArrayList<ClientOfflineReportEntity>();
	private Date searchDate;
	// option list for time & hour selection
	private static List<SelectItem> hourList;
	private static List<SelectItem> minList;
    private int searchHour;
    private int searchMin;
    private Date lastSearchDate;
	private ClientOfflineReportAdaptor adaptor = new ClientOfflineReportAdaptor();
	private ClientOfflineReport report;
	
	public ClientOfflineReportBackingBean(){
		super();
		try{
			
			 String reportID = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("reportID");
			if(reportID==null||reportID.equalsIgnoreCase("")){
				// summary page
				summary = adaptor.getReportSummary();
			}else{
				//detail page
				report = adaptor.getReport(reportID);
				detail.clear();
				List<ClientOfflineReportEntity> list = new ArrayList<ClientOfflineReportEntity>();
				list.addAll(report.getDetails());
				detail = adaptor.sortDetails(list);
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
	
//	public void searchAction(){
//		if(searchDate!=null){
//			lastSearchDate = getSearchDate();
//			detail = adaptor.getReportDetail(lastSearchDate);
//		}
//	}
	public void generateReportAction(){
		if(searchDate!=null){
			lastSearchDate = getSearchDate();
			adaptor.generateReport(lastSearchDate);
		}
	}	
	public void generateNotificationAction(){
		adaptor.generateNotification();
	}
	public void exportToExcel() throws IOException{
		if(lastSearchDate!=null){
			adaptor.exportToExcel(detail, lastSearchDate);	
		}else{
			if(detail.size()>0){
				Date date = detail.get(0).getGenerateTime();
				adaptor.exportToExcel(detail, date);	
			}else{
				adaptor.exportToExcel(detail, new Date());	
			}
		}
	}

	/**
	 * @return the detail
	 */
	public List<ClientOfflineReportEntity> getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(List<ClientOfflineReportEntity> detail) {
		this.detail = detail;
	}

	/**
	 * @return the searchDate
	 */
	public Date getSearchDate() {
		return construct(searchDate,this.searchHour,this.searchMin,0);
	}

	/**
	 * @param searchDate the searchDate to set
	 */
	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	/**
	 * @return the adaptor
	 */
	public ClientOfflineReportAdaptor getAdaptor() {
		return adaptor;
	}

	/**
	 * @param adaptor the adaptor to set
	 */
	public void setAdaptor(ClientOfflineReportAdaptor adaptor) {
		this.adaptor = adaptor;
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
	 * @return the searchHour
	 */
	public int getSearchHour() {
		return searchHour;
	}

	/**
	 * @param searchHour the searchHour to set
	 */
	public void setSearchHour(int searchHour) {
		this.searchHour = searchHour;
	}

	/**
	 * @return the searchMin
	 */
	public int getSearchMin() {
		return searchMin;
	}

	/**
	 * @param searchMin the searchMin to set
	 */
	public void setSearchMin(int searchMin) {
		this.searchMin = searchMin;
	}

	/**
	 * @return the summary
	 */
	public List<ClientOfflineReport> getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(List<ClientOfflineReport> summary) {
		this.summary = summary;
	}

	/**
	 * @return the report
	 */
	public ClientOfflineReport getReport() {
		return report;
	}

	/**
	 * @param report the report to set
	 */
	public void setReport(ClientOfflineReport report) {
		this.report = report;
	}
	public String cancel(){
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec=context.getExternalContext();
		try{
			ec.redirect("/pss2.utility/jsp/reports/clientOfflineReport.jsf");
		}catch(IOException e){
			return "failure";
		}
		context.responseComplete();
		return "success";
	}
}
