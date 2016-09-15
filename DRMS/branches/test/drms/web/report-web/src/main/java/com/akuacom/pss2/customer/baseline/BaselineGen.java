/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.report.ReportClientOfflineStatisticsModel.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.customer.baseline;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.EJB;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.customer.report.FDUtils;
import com.akuacom.pss2.customer.report.ReportContext;
import com.akuacom.pss2.customer.report.SearchCriteria;
import com.akuacom.pss2.customer.report.ServiceLocator;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.history.HistoryDataManager;
import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.utils.DateUtil;

public class BaselineGen implements Serializable{
	
	private static final long serialVersionUID = -3053285882291299833L;
	
//	@EJB(mappedName="operator-report/HistoryReportManager/local")
	private HistoryReportManager reportManager;
	
	private HistoryDataManager dataManager;
	
	//searchCtieria to collect user input
	private SearchCriteria sc;
		
	//SearchCriteria which is taking effect
	//searchCtiteria takes effect when user click "search Button";
	private SearchCriteria workingsc;
	
	private boolean buttonRendered =true;
	
	private boolean enabled=false;
	
	private long progress = 0;
	
	private BParticipantSelectionDlg participantSelection;
	
	private ReportContext context;
	
	private long totalCount = 1;
	
	private String errorLog ="Failed to generate baseline for ";
	
	private int threadPoolSize = 5;
	
	private DefaultProgressUpdater progressUpdater;
	
   	private String dateFormat;

	public BaselineGen(){
        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        dateFormat=systemManager.getPss2Features().getDateFormat();
	}
	
	public void generationAction(){
		if(getSearchCriteria().getParticipantNames()==null || getSearchCriteria().getParticipantNames().isEmpty()){
			FDUtils.addMsgError("Pelease select participants");
			return;
		}
		
		if(getSearchCriteria().getStartDate().after(getSearchCriteria().getEndDate())){
			FDUtils.addMsgError("Start Date must be no later than end date");
			return;
		}
		
		workingsc =  getSearchCriteria().copy();
		enabled = true;
		buttonRendered=false;
		
		Date start = DateUtil.stripTime(workingsc.getStartDate());
		Date end = DateUtil.stripTime(workingsc.getEndDate());
		
		totalCount = workingsc.getParticipantNames().size();
		totalCount= totalCount* ( (end.getTime()-start.getTime())/DateUtil.MSEC_IN_DAY +1);
		
		progressUpdater  = new DefaultProgressUpdater(totalCount);
		ExecutorService threadExecutor = Executors.newFixedThreadPool(threadPoolSize);
		
		for(String p:workingsc.getParticipantNames()){
			Date sDate  = start;
			while(!sDate.after(end)){
				//create a task
				BaseLineGenerator task = new BaseLineGenerator(progressUpdater,sDate,p,getDataManager());
				threadExecutor.execute(task);
				sDate=DateUtil.add(sDate, Calendar.DATE, 1);
			}
		}
		threadExecutor.shutdown();
		//just return response to client, the generation is still in progress now
		//the client will use periodical poll request to get the progress
	}
	
	public void clearDirtyData(){
		if(getSearchCriteria().getParticipantNames()==null || getSearchCriteria().getParticipantNames().isEmpty()){
			FDUtils.addMsgError("Pelease select participants");
			return;
		}
		
		if(getSearchCriteria().getStartDate().after(getSearchCriteria().getEndDate())){
			FDUtils.addMsgError("Start Date must be no later than end date");
			return;
		}
		
		workingsc =  getSearchCriteria().copy();
		
		Date start = DateUtil.getStartOfDay(workingsc.getStartDate());
		Date end = DateUtil.getEndOfDay(workingsc.getEndDate());
		
		DataManager dataManager=ServiceLocator.findHandler(DataManager.class, "pss2/DataManagerBean/remote");
		dataManager.clearUsageData(workingsc.getParticipantNames(), start, end);
		
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
	
	public BParticipantSelectionDlg getPartSelection() {
		if(participantSelection==null){
			participantSelection = new BParticipantSelectionDlg(this);
		}
		return participantSelection;
	}
	
	public HistoryReportManager getReportManager(){
		if(reportManager==null)
			reportManager =EJBFactory.getBean(HistoryReportManager.class);
		return reportManager;
	}
	
	public ReportContext getContext(){
		return this.context;
	}
	
	public List<ParticipantVO> getParticipant(){
		SearchCriteria criteria = this.getSearchCriteria();
		return criteria.getParticipants();
	}
	
	public boolean isTimeNeeded(){
		return false;
	}
	
	public boolean isEndDateNeeded(){
		return true;
	}
	
	public boolean isProgramNeeded(){
		return false;
	}
	
	public HistoryDataManager getDataManager() {
		if(dataManager==null)
			dataManager =EJBFactory.getBean(HistoryDataManager.class);
		return dataManager;
	}

	public boolean isButtonRendered() {
		return buttonRendered;
	}
	
	public long getCurrentValue() {
		if(this.isEnabled()){
			double p=  progressUpdater.getDonePercentage();
			long lp = (new Double(p)).longValue();
			if(lp<=0) lp =1;
			if(p>=100){
				buttonRendered=true;
				lp = 101;
			}
			return lp;
		}
		return -1;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getErrorLog() {
		return errorLog;
	}
	
	public boolean isScopeChange(){
		SearchCriteria sc = this.getSearchCriteria();
		SearchCriteria workingsc = this.workingsc;
		if(workingsc==null ){
			if(sc==null){
				return false;
			}else{
				return true;
			}
		}else{
			if(sc==null)
				return false;
			else{
				return 
				 !sc.getStartDate().equals(workingsc.getStartDate())
				 || !sc.getEndDate().equals(workingsc.getEndDate())
				 || !sc.getParticipantNames().equals(sc.getParticipantNames());
			}
		}
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
}