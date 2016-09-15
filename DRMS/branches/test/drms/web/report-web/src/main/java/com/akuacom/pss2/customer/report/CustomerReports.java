/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.report.ReportClientOfflineStatisticsModel.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.customer.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;

import javax.ejb.EJB;
public class CustomerReports implements Serializable{
	
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
	
	private AggregationTree aggTree;
	
	private HistoryReportManager reportManager;

	private JSFClientStatus clientStatus;

	private JSFEventParticipation participation;
	
	private JSFUsage usage;
	
	private JSFEvents events;
	
	//searchCtieria to collect user input
	private SearchCriteria sc;
		
	//SearchCriteria which is taking effect
	//searchCtiteria takes effect when user click "search Button";
	private SearchCriteria workingsc;
	
	private List<String> programOptions;
	
	private ReportContext context;

    private boolean aggParticipation = false;

    private ParticipantManager partMan;

   	private ProgramParticipantAggregationManager aggMan = null;
	
   	private String dateFormat;
   	private String dateTimeFormat;
   	
	public CustomerReports(){
		
		clientStatus = new JSFClientStatus(this);
		
		participation = new JSFEventParticipation(this);
		events = new JSFEvents(this);
		usage = new JSFUsage(this);
		context = new ReportContext(getSystemManager());
		aggTree = new AggregationTree();
		init();
		if(isUsageEnable())
			selectedTab =  TAB_USAGE;
		else
			selectedTab = TAB_CLIENT_STATUS;
		
        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        PSS2Features features = systemManager.getPss2Features();
        dateFormat=features.getDateFormat();
        dateTimeFormat=features.getDateFormat()+" HH:mm";
	}
	
	
	protected void init(){
		try{
			String participantName = getContext().getParticipantName();
			List<ParticipantVO>  parts=getReportManager().getParticipants(Arrays.asList(participantName));
			getSearchCriteria().setParticipants(parts);
			getSearchCriteria().setParticipantName(participantName);

            Participant loggedUser = this.getPartMan().getParticipant(participantName);
            setAggParticipation(this.inAggregationTree(loggedUser));
		}catch(Exception e){
			
		}
	}
	
	public ReportTab[] getAllReports() {
        if (this.isUsageEnable()) {
            return new ReportTab[]{
                    getUsage(),
                    getClientStatus(),
                    getEvents(),
                    getParticipation()
            };
        } else {
            return new ReportTab[]{
                    getClientStatus(),
                    getEvents(),
                    getParticipation()
            };
        }
    }
	
	public boolean isUsageEnable() {
		usageEnable = getSystemManager().getPss2Features().isUsageEnabled();
		if(usageEnable){
			String participantName = getContext().getParticipantName();
			boolean enableDataFlag = getParticipantManager().getParticipant(participantName).getDataEnabler();	
			usageEnable=enableDataFlag;
		}
		
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
	
	public void searchAction(){
		//click search
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

	public SearchCriteria getSearchCriteria(){
		if(this.sc==null){
			sc= new SearchCriteria();
		}
		return sc;
	}
	
	public SearchCriteria getWorkingSearchCriteria(){
		return this.workingsc;
	}
	
	public HistoryReportManager getReportManager(){
		if(reportManager==null){
			reportManager =EJBFactory.getBean(HistoryReportManager.class);
		}
		return reportManager;
	}
	
	public ReportContext getContext(){
		return this.context;
	}
	
	public List<String> getProgramOptions() throws Exception{
		if(context.getParentProgram()!= null && !AggregationTree.NO_PROGRAM.equals(context.getParentProgram())){
			//program participant is selected by participant selection in aggregation tree 
			return Arrays.asList(context.getParentProgram());
		}else{
			return getAllProgramOptions();
		}
	}
	
	public List<String> getAllProgramOptions() throws Exception{
		if(programOptions==null){
			programOptions = new ArrayList<String>();
			List<Program> programs = getReportManager()
					.getProgramsForParticipant(getSearchCriteria().getParticipantName());
				for (Program program : programs) {
					programOptions.add(program.getProgramName());
			}
		}
		return programOptions;
	}
	
	public List<ParticipantVO> getParticipant(){
		SearchCriteria criteria = this.getSearchCriteria();
		return criteria.getParticipants();
	}
	
	public List<SelectItem> getProgramOptionItems() throws Exception{
		List<SelectItem> items = new ArrayList<SelectItem>();
		if(context.getParentProgram()== null || AggregationTree.NO_PROGRAM.equals(context.getParentProgram())){
			SelectItem item=new SelectItem(SearchCriteria.PROGRAM_ALL, SearchCriteria.PROGRAM_ALL_LABEL);
			items.add(item);
		}
		for(String program:getProgramOptions()){
			SelectItem item=new SelectItem(program,program);
			items.add(item);
		}
		return items;
	}
	
	public AggregationTree getAggTree() {
		if(aggTree==null){
			aggTree = new AggregationTree();
		}
		return aggTree;
	}

	public void setAggTree(AggregationTree aggTree) {
		this.aggTree = aggTree;
	}
	
	public void switchParticipantAction() throws Exception{
		this.getContext().setSwtichingProgram(getAggTree().getParentProgram());
		String participantName = getAggTree().getSelectedParticipantName();
		List<ParticipantVO>  parts=getReportManager().getParticipants(Arrays.asList(participantName));
		getSearchCriteria().setParticipants(parts);
		getSearchCriteria().setParticipantName(participantName);
	}
	
	private SystemManager systemManager;
	public SystemManager getSystemManager() {
		if(systemManager==null)
			systemManager =EJBFactory.getBean(SystemManager.class);
		return systemManager;
	}

    public ParticipantManager getPartMan() {
    	if(partMan==null)
    		partMan =EJBFactory.getBean(ParticipantManager.class);

		return partMan;
	}

    public ProgramParticipantAggregationManager getAggMan() {
    	if(aggMan==null)
    		aggMan =EJBFactory.getBean(ProgramParticipantAggregationManager.class);

		return aggMan;
	}

      public boolean inAggregationTree(Participant p){
           int aggCount = 0;
           for (ProgramParticipant ppp : p.getProgramParticipants()) {
                aggCount += this.getAggMan().getDescendants(ppp).size();
			}
           if (aggCount == 0) return false;
           else return true;
    }

    public boolean isAggParticipation() {
        return aggParticipation;
    }

    public void setAggParticipation(boolean aggParticipation) {
        this.aggParticipation = aggParticipation;
    }

    
	private ParticipantManager participantManager;
	public ParticipantManager getParticipantManager() {
		if(participantManager==null)
		participantManager =EJBFactory.getBean(ParticipantManager.class);
		return participantManager;
	}

	public String getDateFormat() {
		return dateFormat;
	}
	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

    public String getHeaderDateTimeFormat() {
        return dateTimeFormat + " z";
    }
}