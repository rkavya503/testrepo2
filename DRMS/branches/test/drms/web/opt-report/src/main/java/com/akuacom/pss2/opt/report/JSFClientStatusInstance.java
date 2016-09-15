package com.akuacom.pss2.opt.report;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.jsf.model.TableContentProvider;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.history.vo.ClientOfflineInstanceData;

public class JSFClientStatusInstance extends ReportTab {

	private static final long serialVersionUID = -8222617916950212698L;

	private static final Logger log = Logger
			.getLogger(JSFClientStatusInstance.class.getName());

	private JSFClientStatus parent;
	
	private JSFClientStatus.Tab subTab = JSFClientStatus.Tab.clientOfflineInstanceTab;
	
	private boolean toResetStatus = false;

	public JSFClientStatusInstance(JSFClientStatus parent) {
		super(parent.getReport(), OperatorReports.Tab.clientStatusTab,
				SearchCriteria.FILTER.participant, SearchCriteria.FILTER.event,
				SearchCriteria.FILTER.startDate,
				SearchCriteria.FILTER.startTime, SearchCriteria.FILTER.endDate,
				SearchCriteria.FILTER.endTime, SearchCriteria.FILTER.program);
		this.parent = parent;
	}
	
	protected boolean needValidation(){
		return super.needValidation() && subTab.equals(parent.getActiveTab());
	}
	
	private TableContentProvider<ClientOfflineInstanceData> offlineInstance =
				new AbstractTableContentProvider<ClientOfflineInstanceData>() {

		private List<ClientOfflineInstanceData> contents = Collections
				.emptyList();
		private int totalCount = -1; //-1 indicates no search executed before
		
		@Override
		public void updateModel() {
			try {
				if(!getReportTab().equals(getReport().getActiveTab()))
					return;
				
				if(!subTab.equals(parent.getActiveTab()))
					return;
				
				if(!isValid()) return;
				
				SearchConstraint searchConstraint = WebUtil
						.getSearchConstraint(this);
				boolean scchanged = isSearchCriteriaChange();
				if (scchanged) {
					// /sync search criteria
					setSearchCriteria(getReport().getWorkingSearchCriteria());
					// allow other tab to get updated when activated
				}
				SearchCriteria sc = getSearchCriteria();

				List<String> participantNames = sc.getParticipantNames();
				String program = sc.getProgram();
				if (SearchCriteria.PROGRAM_ALL.equals(program))
					program = null;
				DateRange timeRange = sc.getDateTimeRange();

				if (scchanged || isRangeChanged()
						|| isSortColumnChanged()) {
					if (scchanged ) {
						if (sc.isSearchByParticipant()) {
							totalCount = getReportManager()
									.getClientOfflineInstanceCount(
											participantNames, program,
											timeRange);
						} else if (sc.isSearchByEvent()) {
							totalCount = getReportManager()
									.getEventClientOfflineInstanceCount(
											sc.getEvent());
						} else {
							totalCount = 0;
						}
					}
					if (sc.isSearchByParticipant()) {
						contents = getReportManager()
								.findClientOfflineInstance(participantNames,
										program, timeRange, searchConstraint);
					} else if (sc.isSearchByEvent()) {
						contents = getReportManager()
								.findEventClientOfflineInstance(sc.getEvent(),
										searchConstraint);
					} else {
						contents = Collections.emptyList();
					}
				}
			} catch (Exception e) {
				FDUtils.addMsgError("Internal Error!");
				log.error("Internal Error", e);
			}
		}

		@Override
		public List<ClientOfflineInstanceData> getContents() {
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
	
	public TableContentProvider<ClientOfflineInstanceData> getOfflineInstance() {
		return offlineInstance;
	}
	
	public void exportAction(){
		try{
			String filename = "CLIENT_OFFLINE_INSTANCE.csv";
			String body = getInstanceExportContent();
			FDUtils.export(filename, body);
		}catch(Exception e){
			FDUtils.addMsgError("Internal error");
			log.error("Internal error ", e);
		}
	}
	
	public String getInstanceExportContent() throws Exception{
		SearchCriteria                  sc = getSearchCriteria();
		List<String> participantNames = sc.getParticipantNames();
		String program = sc.getProgram();
		if(SearchCriteria.PROGRAM_ALL.equals(program)) program = null;
		DateRange timeRange = sc.getDateTimeRange();
		List<ClientOfflineInstanceData> contents = Collections.emptyList();
		SearchConstraint  constraint = WebUtil.getSearchConstraint(offlineInstance);
		constraint.setRowCount(-1);
		
		if(sc.isSearchByEvent()){
			 contents=getReportManager().findEventClientOfflineInstance(
				sc.getEvent(),constraint);
		}else if(sc.isSearchByParticipant()){
			contents =getReportManager().findClientOfflineInstance(
					participantNames,program,timeRange,constraint);
		}
		
		String exportContent  = "CLIENT, DATE/TIME OFFLINE, DURATION(MIN) \n";
		for (ClientOfflineInstanceData c : contents) {
			exportContent = exportContent + c.getParticipantName() + ", ";
			exportContent = exportContent + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(c.getStartTime()) + ", ";
			exportContent = exportContent + c.getDuration() + ", ";
			exportContent = exportContent + " \n";				
		}
		return exportContent;
	}

	@Override
	public void resetStatus() {
		toResetStatus = true;
	}
	
}
