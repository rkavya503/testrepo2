package com.akuacom.pss2.richsite.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;
import com.akuacom.pss2.task.TimerConfig;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.JSFClientOfflineConfigLayout;

public class JSFClientOfflineConfigBackingBean implements Serializable, JSFClientOfflineConfigLayout {

	private static final long serialVersionUID = 8155573046623766786L;
	private static final Logger log = Logger.getLogger(JSFClientOfflineConfigBackingBean.class);
	private TimerConfig reportConfig;
	private TimerConfig summerConfig;
	private TimerConfig winterConfig;
	private String updateResult="";
	private ClientOfflineTimerAdaptor adaptor = new ClientOfflineTimerAdaptor();
	private String summerThreshold;
	private String winterThreshold;
	private boolean clientReportOfflineConfigurationEnabled;
	private boolean clientOfflineSummerNotificationEnabled;
	private boolean clientOfflineWinterNotificationEnabled;
	
	
	public JSFClientOfflineConfigBackingBean(){
		super();
		
		reportConfig = adaptor.getReportConfig();
		summerConfig = adaptor.getSummerConfig();
		winterConfig = adaptor.getWinterConfig();
		summerThreshold = String.valueOf(summerConfig.getThreshold());
		winterThreshold = String.valueOf(winterConfig.getThreshold());
		initialize();
		buildViewLayout();
	}

	public void updateReportAction(){
		updateResult="";
		if(reportConfig!=null){
			
			adaptor.updateReportConfig(reportConfig);
			adaptor.retrieveData();
			reportConfig = adaptor.getReportConfig();
			summerConfig = adaptor.getSummerConfig();
			winterConfig = adaptor.getWinterConfig();
			updateResult = "Save Success.";
			adaptor.getClientOfflineReportManager().scheduleTimer();
			log.info("Invoke client offline report manager to re-schedule timer");
		}
	}
	public void updateNotificationAction(){
		updateResult="";
		if(summerConfig!=null&&winterConfig!=null){
			if(validate()){
				summerConfig.setThreshold(Integer.valueOf(summerThreshold));
				winterConfig.setThreshold(Integer.valueOf(winterThreshold));
				summerConfig = adaptor.updateSummerConfig(summerConfig);
				winterConfig = adaptor.updateWinterConfig(winterConfig);
				adaptor.retrieveData();
				reportConfig = adaptor.getReportConfig();
				summerConfig = adaptor.getSummerConfig();
				winterConfig = adaptor.getWinterConfig();
				updateResult = "Save Success.";
				adaptor.getClientOfflineNotificationManager().scheduleTimer();
				log.info("Invoke client offline notification manager to re-schedule timer");
			}
		}
	}
	
	private boolean validate(){
		boolean result = true;
		try{
			int st = Integer.valueOf(summerThreshold);
			if(st<0){
				throw new Exception();
			}
		}catch(Exception e){
			AkuacomJSFUtil.addMsgError("Summer threshold should be a valid positive number.");
			result = false;
		}
		try{
			int st = Integer.valueOf(winterThreshold);
			if(st<0){
				throw new Exception();
			}
		}catch(Exception e){
			AkuacomJSFUtil.addMsgError("Winter threshold should be a valid positive number.");
			result = false;
		}
		return result;
	}
	
	public TimerConfig getReportConfig() {
		return reportConfig;
	}
	public void setReportConfig(TimerConfig reportConfig) {
		this.reportConfig = reportConfig;
	}
	public TimerConfig getSummerConfig() {
		return summerConfig;
	}
	public void setSummerConfig(TimerConfig summerConfig) {
		this.summerConfig = summerConfig;
	}
	public TimerConfig getWinterConfig() {
		return winterConfig;
	}
	public void setWinterConfig(TimerConfig winterConfig) {
		this.winterConfig = winterConfig;
	}
	public ClientOfflineTimerAdaptor getAdaptor() {
		return adaptor;
	}
	public void setAdaptor(ClientOfflineTimerAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	// option list for time & hour selection
	private static List<SelectItem> hourList;
	private static List<SelectItem> minList;
	private static List<SelectItem> dayList;
    public List<SelectItem> getHourList() {
		return hourList;
	}

	public List<SelectItem> getMinList() {
		return minList;
	}
	public List<SelectItem> getDayList() {
		return dayList;
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
        dayList = new ArrayList<SelectItem>();
        dayList.add(new SelectItem(0,"Sunday"));
        dayList.add(new SelectItem(1,"Monday"));
        dayList.add(new SelectItem(2,"Tuesday"));
        dayList.add(new SelectItem(3,"Wednesday"));
        dayList.add(new SelectItem(4,"Thursday"));
        dayList.add(new SelectItem(5,"Friday"));
        dayList.add(new SelectItem(6,"Saturday"));
	}
	private static String formatTime(int i){
		if(i<10)
			return "0"+i;
		else
			return i+"";
	}

	/**
	 * @return the updateResult
	 */
	public String getUpdateResult() {
		return updateResult;
	}

	/**
	 * @param updateResult the updateResult to set
	 */
	public void setUpdateResult(String updateResult) {
		this.updateResult = updateResult;
	}

	/**
	 * @param hourList the hourList to set
	 */
	public static void setHourList(List<SelectItem> hourList) {
		JSFClientOfflineConfigBackingBean.hourList = hourList;
	}

	/**
	 * @param minList the minList to set
	 */
	public static void setMinList(List<SelectItem> minList) {
		JSFClientOfflineConfigBackingBean.minList = minList;
	}

	/**
	 * @param dayList the dayList to set
	 */
	public static void setDayList(List<SelectItem> dayList) {
		JSFClientOfflineConfigBackingBean.dayList = dayList;
	}

	/**
	 * @return the summerThreshold
	 */
	public String getSummerThreshold() {
		return summerThreshold;
	}

	/**
	 * @param summerThreshold the summerThreshold to set
	 */
	public void setSummerThreshold(String summerThreshold) {
		this.summerThreshold = summerThreshold;
	}

	/**
	 * @return the winterThreshold
	 */
	public String getWinterThreshold() {
		return winterThreshold;
	}

	/**
	 * @param winterThreshold the winterThreshold to set
	 */
	public void setWinterThreshold(String winterThreshold) {
		this.winterThreshold = winterThreshold;
	}
	
	private List<SelectItem> startMonthItems;
	private List<SelectItem> startDayItems;
	private List<SelectItem> endMonthItems;
	private List<SelectItem> endDayItems;	
	
	public void initialize() {
		startMonthItems = new ArrayList<SelectItem>();
		endMonthItems = new ArrayList<SelectItem>();
		startDayItems= new ArrayList<SelectItem>();
		endDayItems= new ArrayList<SelectItem>();
		for(int i =0;i<12;i++){
			startMonthItems.add(new SelectItem(String.valueOf(i),String.valueOf(i+1)));
			endMonthItems.add(new SelectItem(String.valueOf(i),String.valueOf(i+1)));
		}
		
		int startMonth = this.getSummerConfig().getStartMonth();
		int endMonth = this.getSummerConfig().getEndMonth();
		int startDays = monthDay(startMonth);
		int endDays = monthDay(endMonth);
		for(int i =1;i<startDays+1;i++){
			startDayItems.add(new SelectItem(String.valueOf(i),String.valueOf(i)));
		}
		for(int i =1;i<endDays+1;i++){
			endDayItems.add(new SelectItem(String.valueOf(i),String.valueOf(i)));
		}
	}
	public void startMonthChange(ValueChangeEvent event) {
		if ((event.getOldValue() == null) || (!event.getOldValue().equals(event.getNewValue()))) {
			String monthValue = event.getNewValue().toString();
			startDayItems = new ArrayList<SelectItem>();
			int month = Integer.valueOf(monthValue);
			int days = monthDay(month);
			for(int i =1;i<days+1;i++){
				startDayItems.add(new SelectItem(String.valueOf(i),String.valueOf(i)));
			}
		}
	}
	public void endMonthChange(ValueChangeEvent event) {
		if ((event.getOldValue() == null) || (!event.getOldValue().equals(event.getNewValue()))) {
			String monthValue = event.getNewValue().toString();
			endDayItems = new ArrayList<SelectItem>();
			int month = Integer.valueOf(monthValue);
			int days = monthDay(month);
			for(int i =1;i<days+1;i++){
				endDayItems.add(new SelectItem(String.valueOf(i),String.valueOf(i)));
			}
		}
	}
	
	
	private int monthDay(int month){
		if(month==1){
			return 28;
		}else if(month==0||month==2||month==4||month==6||month==7||month==9||month==11){
			return 31;
		}else{
			return 30;
		}
	}
	/**
	 * @return the startMonthItems
	 */
	public List<SelectItem> getStartMonthItems() {
		return startMonthItems;
	}

	/**
	 * @param startMonthItems the startMonthItems to set
	 */
	public void setStartMonthItems(List<SelectItem> startMonthItems) {
		this.startMonthItems = startMonthItems;
	}

	/**
	 * @return the startDayItems
	 */
	public List<SelectItem> getStartDayItems() {
		return startDayItems;
	}

	/**
	 * @param startDayItems the startDayItems to set
	 */
	public void setStartDayItems(List<SelectItem> startDayItems) {
		this.startDayItems = startDayItems;
	}

	/**
	 * @return the endMonthItems
	 */
	public List<SelectItem> getEndMonthItems() {
		return endMonthItems;
	}

	/**
	 * @param endMonthItems the endMonthItems to set
	 */
	public void setEndMonthItems(List<SelectItem> endMonthItems) {
		this.endMonthItems = endMonthItems;
	}

	/**
	 * @return the endDayItems
	 */
	public List<SelectItem> getEndDayItems() {
		return endDayItems;
	}

	/**
	 * @param endDayItems the endDayItems to set
	 */
	public void setEndDayItems(List<SelectItem> endDayItems) {
		this.endDayItems = endDayItems;
	}
	
	private void buildViewLayout(){

		try 
		{
			getViewBuilderManager().buildJSFClientOfflineConfigBackingBeanLayout(this);

		} catch (NamingException e) 
		{
			// log exception
		}

	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{

		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");

	}

	public boolean isClientReportOfflineConfigurationEnabled() {
		return clientReportOfflineConfigurationEnabled;
	}

	public void setClientReportOfflineConfigurationEnabled(
			boolean clientReportOfflineConfigurationEnabled) {
		this.clientReportOfflineConfigurationEnabled = clientReportOfflineConfigurationEnabled;
	}

	public boolean isClientOfflineSummerNotificationEnabled() {
		return clientOfflineSummerNotificationEnabled;
	}

	public void setClientOfflineSummerNotificationEnabled(
			boolean clientOfflineSummerNotificationEnabled) {
		this.clientOfflineSummerNotificationEnabled = clientOfflineSummerNotificationEnabled;
	}

	public boolean isClientOfflineWinterNotificationEnabled() {
		return clientOfflineWinterNotificationEnabled;
	}

	public void setClientOfflineWinterNotificationEnabled(
			boolean clientOfflineWinterNotificationEnabled) {
		this.clientOfflineWinterNotificationEnabled = clientOfflineWinterNotificationEnabled;
	}

	
	
	
}
