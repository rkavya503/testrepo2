/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.report.ReportClientOfflineStatisticsModel.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.opt.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.ejb.EJB;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.history.vo.ReportEvent;
import com.akuacom.pss2.price.australia.LocationPriceManager;
import com.akuacom.pss2.price.australia.LocationPriceManagerBean;
import com.akuacom.pss2.price.australia.PriceRecord;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.HeaderStyle;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.utils.lang.DateUtil;

public class OperatorReports implements Serializable{
	
	private static final long serialVersionUID = -3053285882291299833L;
	
	public static final String TAB_USAGE         = "usageTab";
	public static final String TAB_CLIENT_STATUS = "clientStatusTab";
	public static final String TAB_EVENTS        = "eventsTab";
	public static final String TAB_PARTICIPATION = "participationTab";
	
	public enum  Tab{
		usageTab,
		clientStatusTab,
		eventsTab,
		participationTab
	}
	
	private String selectedTab;
	
	private Boolean usageEnable = true;
	
//	@EJB(mappedName="operator-report/HistoryReportManager/local")
	private HistoryReportManager reportManager;
	public HistoryReportManager getReportManager(){
		if(reportManager==null)
			reportManager =EJBFactory.getBean(HistoryReportManager.class);
		return reportManager;
	}
	
	private JSFClientStatus clientStatus;

	private JSFEventParticipation participation;
	
	private JSFUsage usage;
	
	private JSFEvents events;
	
	//searchCtieria to collect user input
	private SearchCriteria sc;
		
	//SearchCriteria which is taking effect
	//searchCtiteria takes effect when user click "search Button";
	private SearchCriteria workingsc;
	
	private ParticipantSelectionDlg participantSelection;
	
	private EventSelectionDlg eventSelection;
	
	//private List<Program> partProgramOptions;
	
	private List<Program> programOptions;
	
	private List<ParticipantVO> participants;
	
	private ReportEvent lastEvent;
	
	//private Object oldFilterBy;
	
	private OptReportContext context;
   	private String dateFormat;
   	private String dateTimeFormat;
   	private String fullDateTimeFormat;
   	private String headerStyle;
	public String getHeaderStyle(){
		Boolean isProductionServer = getSystemManager().getPss2Features().isProductionServer();
    	if(isProductionServer){
    	    headerStyle = HeaderStyle.PRODUCTION_SERVER_UTILITY_OPERATOR.getStyleName();
    	}else{
    	    headerStyle = HeaderStyle.TEST_SERVER_UTILITY_OPERATOR.getStyleName();
    	}
		return this.headerStyle;
	}

	public OperatorReports(){
		if(isUsageEnable())
			selectedTab =  TAB_USAGE;
		else
			selectedTab = TAB_CLIENT_STATUS;
		clientStatus = new JSFClientStatus(this);
		
		participation = new JSFEventParticipation(this);
		events = new JSFEvents(this);
		usage = new JSFUsage(this);
		context = new OptReportContext();
		
        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        
        PSS2Features features = systemManager.getPss2Features();
        dateFormat=features.getDateFormat();
        dateTimeFormat=features.getDateFormat()+" HH:mm";
        fullDateTimeFormat=features.getDateFormat()+" HH:mm:ss";
	}
	
	
	public boolean isUsageEnable() {
		//TODO
		return usageEnable;
	}

	public void setUsageEnable(boolean usageEnable) {
		this.usageEnable = usageEnable;
	}
	
	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}
	
	public Tab getActiveTab(){
		return  Enum.valueOf(Tab.class, selectedTab);
	}
	
	public boolean isTimeNeeded(){
		return TAB_CLIENT_STATUS.equals(selectedTab);
	}
	
	public boolean isEndDateNeeded(){
		return !TAB_USAGE.equals(selectedTab);
	}
	
	public boolean isProgramNeeded(){
		return !TAB_USAGE.equals(selectedTab);
	}
	
	public boolean isByEvent(){
		return this.getSearchCriteria().getEvent()!=null;
	}
	
	public boolean isByParticipant(){
		return this.getSearchCriteria().getParticipants()!=null;
	}
	
	public void searchAction(){
		workingsc =  getSearchCriteria().copy();
		//reset all table status,e.g., sort columns, current pagination 
		for(ReportTab tab: this.getAllReports()){
			tab.resetStatus();
		}
		
	}
	
	public ReportTab getClientStatus(){
		return clientStatus;
	}
	
	public ReportTab getParticipation() {
		return participation;
	}

	public JSFUsage getUsage() {
		return usage;
	}

	public JSFEvents getEvents() {
		return events;
	}
	
	public ReportTab[] getAllReports(){
		return new ReportTab[]{
			getUsage(),
			getClientStatus(),
			getEvents(),
			getParticipation()
		};
	}
	
	public SearchCriteria getSearchCriteria(){
		if(this.sc==null){
			sc= new SearchCriteria();
		}
		return sc;
	}
	
	public SearchCriteria getWorkingSearchCriteria(){
		return this.workingsc;
	}
	
	public ParticipantSelectionDlg getPartSelection() {
		if(participantSelection==null){
			participantSelection = new ParticipantSelectionDlg(this);
		}
		return participantSelection;
	}
	
	public EventSelectionDlg getEventSelection() {
		if(eventSelection==null){
			eventSelection = new EventSelectionDlg(this);
		}
		return eventSelection;
	}
	

	
	public OptReportContext getContext(){
		return this.context;
	}
	
	public List<Program> getProgramOptions() throws Exception{
		//SearchCriteria criteria = this.getSearchCriteria();
		/*if(criteria.isSearchByParticipant()){
			if(partProgramOptions==null 
					|| (oldFilterBy==null || !oldFilterBy.equals(criteria.getFilterByObject()))){
				
				List<String> ids = new ArrayList<String>();
				for(Participant part:getPartSelection().getSelection()){
					ids.add(part.getUUID());
				}
				partProgramOptions = getReportManager().findPrograms(ids);
			}
			return partProgramOptions;
		}else{*/
			if(programOptions==null){
				programOptions = getReportManager().findAllPrograms();
			}
			return programOptions;
		//}
	}
	
	public List<ParticipantVO> getParticipant(){
		SearchCriteria criteria = this.getSearchCriteria();
		if(criteria.isSearchByParticipant()){
			return criteria.getParticipants();
		}else if(criteria.isSearchByEvent()){
			if(lastEvent==null || !lastEvent.equals(sc.getEvent())){
				try {
					participants = getReportManager().getParticipantsForEvent(sc.getEvent());
				} catch (Exception e) {
					FDUtils.addMsgError("Error happened while action getParticipant method"+e);
				}
			}
			lastEvent = sc.getEvent();
			return participants; 
		}
		return new ArrayList<ParticipantVO>();
	}
	
	public List<SelectItem> getProgramOptionItems() throws Exception{
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem item=new SelectItem(SearchCriteria.PROGRAM_ALL, SearchCriteria.PROGRAM_ALL_LABEL);
		items.add(item);
		
		for(Program program:getProgramOptions()){
			item=new SelectItem(program.getProgramName(),program.getProgramName());
			items.add(item);
		}
		return items;
	}
	
	public List<SelectItem> getAllProgramOptionItems() throws Exception{
		if(programOptions==null){
			programOptions = getReportManager().findAllPrograms();
		}
		
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem item=new SelectItem(SearchCriteria.PROGRAM_ALL, SearchCriteria.PROGRAM_ALL_LABEL);
		items.add(item);
		
		for(Program program:programOptions){
			item=new SelectItem(program.getProgramName(),program.getProgramName());
			items.add(item);
		}
		return items;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public String getFullDateTimeFormat() {
		return fullDateTimeFormat;
	}

    public String getHeaderDateTimeFormat() {
        return dateTimeFormat + " z";
    }
    
	private SystemManager systemManager;
	public SystemManager getSystemManager() {
		if(systemManager==null)
			systemManager =EJBFactory.getBean(SystemManager.class);
		return systemManager;
	}
	
	
	public String logoutAndCleanSession(){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpSession session = request.getSession();
        session.invalidate();
        
		return "success";
	}
	
	public boolean isDisplayPrice() {
		// this has session scope
		return getFeatures().isFeatureAustraliaPriceEnabled();
	}
	
	private PSS2Features getFeatures() {
		if (features == null) {
			features = getSystemManager().getPss2Features();
		}
		return features;
	}
	
	public String getAustrialiaPrice(){
		PriceRecord pr=getLocationPriceManager().getPrice();
		if(pr==null|| pr.getPrice()==null) return "N/A";
		return pr.getPrice()+"$/MW";
	}
	
	 public LocationPriceManager getLocationPriceManager(){
	    	if(locationPriceManager==null){
	    		locationPriceManager =  EJBFactory.getBean(LocationPriceManagerBean.class);
	    	}
	    	return locationPriceManager;
	 }
	 
	public String getPriceInformation(){
		PriceRecord pr=getLocationPriceManager().getPrice();
		if(pr==null|| pr.getPrice()==null) return "No price available";
		
		String format=(String) FacesContext.getCurrentInstance().
			getExternalContext().getApplicationMap().get("headerDateTimeFormat");
		
		return "Price for "+pr.getLocation()+",updated to "+ 
			DateUtil.format(pr.getTime(), format, TimeZone.getTimeZone("GMT+10"));
	}
	
	public boolean isScorecardEnabled() {
		return getFeatures().isScorecardEnabled().booleanValue();
	}
	 
	 PSS2Features features = null;
	 private LocationPriceManager locationPriceManager;
}