package com.akuacom.pss2.customer.report;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.jsf.model.TableContentProvider;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.history.vo.ClientOfflineStatisticsData;

public class JSFClientStatusStatistics extends ReportTab {
	private static final long serialVersionUID = -8222617916950212698L;
	
	private static final Logger log = Logger.getLogger(JSFClientStatusStatistics.class.getName());
	
	private JSFClientStatus parent;
	
	private JSFClientStatus.Tab subTab =  JSFClientStatus.Tab.clientOfflineStatisticsTab;
	
	private boolean toResetStatus = false;
	public JSFClientStatusStatistics(JSFClientStatus parent){
		super(parent.getReport(),CustomerReports.Tab.clientStatusTab,
			SearchCriteria.FILTER.participant,
			SearchCriteria.FILTER.event,
			SearchCriteria.FILTER.startDate,
			SearchCriteria.FILTER.startTime,
		    SearchCriteria.FILTER.endDate,
		    SearchCriteria.FILTER.endTime,
			SearchCriteria.FILTER.program
		);
		this.parent = parent;
	}
	
	protected boolean needValidation(){
		return super.needValidation() && subTab.equals(parent.getActiveTab());
	}
	
	private TableContentProvider<ClientOfflineStatisticsData> offlineStatistics = 
				new	AbstractTableContentProvider<ClientOfflineStatisticsData>(){
	
		private List<ClientOfflineStatisticsData> contents  = Collections.emptyList();
		private int totalCount = -1; // -1 indicates no search executed before
		
		@Override
		public void updateModel(){
			try{
				if(!getReportTab().equals(getReport().getActiveTab()))
					return;
				
				if(!subTab.equals(parent.getActiveTab()))
					return;
				
				if(!isValid()) return;
				
				SearchConstraint  searchConstraint = WebUtil.getSearchConstraint(this);
				boolean scchanged = isSearchCriteriaChange();
				if(scchanged){
					///sync search criteria
					setSearchCriteria(getReport().getWorkingSearchCriteria());
					//make sure another sub tab(s) to get updated when activated
				}
				SearchCriteria                  sc = getSearchCriteria();
				List<String> participantNames = sc.getParticipantNames();
				String program = sc.getProgram();
					if(SearchCriteria.PROGRAM_ALL.equals(program)) program = null;
				DateRange timeRange = sc.getDateTimeRange();
				
				if( scchanged || isRangeChanged() || isSortColumnChanged() ){
					if(scchanged){
						totalCount=getReportManager().getClientOfflineStatisticsCount(
								participantNames,program, timeRange);
					}
					if(totalCount>0){
						contents =getReportManager().findClientOfflineStatistics(
							participantNames,program,timeRange,searchConstraint);
					}else{
						contents= Collections.emptyList();
					}
				}
			}
			catch (Exception e) {
				FDUtils.addMsgError("Internal error");
				log.error("Internal error ", e);
			}
		}
		
		@Override
		public List<ClientOfflineStatisticsData> getContents() {
			return contents;
		}
		
		@Override
		public int getTotalRowCount() {
			return totalCount;
		}
		
		@Override
		public void resetSortingOrpagination() {
			super.resetSortingOrpagination();
			if(toResetStatus){
				getSequenceRange().setFirstRow(0);
				setSortColumn(null);
				toResetStatus =false;
			}
		}
	};

	public TableContentProvider<ClientOfflineStatisticsData> getOfflineStatistics() {
		return offlineStatistics;
	}
	
	public void exportAction(){
		try{
			String filename = "CLIENT_STATUS_STATISTICS.csv";
			String body = getSummaryExportContent();
			FDUtils.export(filename, body);
		}catch(Exception e){
			FDUtils.addMsgError("Internal error");
			log.error("Internal error ", e);
		}
	}

	public String getSummaryExportContent() throws Exception{
		SearchCriteria                  sc = getSearchCriteria();
		List<String> participantNames = sc.getParticipantNames();
		String program = sc.getProgram();
		if(SearchCriteria.PROGRAM_ALL.equals(program)) program = null;
		DateRange timeRange = sc.getDateTimeRange();
		SearchConstraint  constraint = WebUtil.getSearchConstraint(offlineStatistics);
		constraint.setRowCount(-1);
		
		List<ClientOfflineStatisticsData> contents = Collections.emptyList();
		
		contents=getReportManager().findClientOfflineStatistics(
					participantNames,program,timeRange,constraint);
		String exportContent =  "CLIENT, # TIMES, TOTAL TIME(MIN), %TIME, %DURING EVENTS \n";
		for (ClientOfflineStatisticsData c : contents) {
			 exportContent += c.getParticipantName() + ", ";
			 exportContent += c.getTimes() + ", ";
			 exportContent += c.getTotalTime() + ", ";
			 exportContent += formatPercent(c.getTimePercent()) + ", ";
			if(c.getDuringEvent() != null){
				exportContent += formatPercent(c.getDuringEvent()) + ", ";					
			}
			else{
				exportContent += "No Event, ";
			}
			exportContent += " \n";				
		}
		return exportContent;
	}
	@Override
	public void resetStatus() {
		toResetStatus = true;
	}
}
