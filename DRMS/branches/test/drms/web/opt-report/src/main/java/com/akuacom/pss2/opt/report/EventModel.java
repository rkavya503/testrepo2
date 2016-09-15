package com.akuacom.pss2.opt.report;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.pss2.history.vo.ReportEvent;

public class EventModel extends AbstractTreeContentProvider<ReportEvent>{

	private int totalCount = 0;
	private List<ReportEvent> contents;
	private HistoryReportManager reportManager;
	private boolean searchBtnClicked = false;
	
	private String participantName = "";
	
	private OperatorReports report;
	private SearchCriteria workingsc;
	
	public EventModel(OperatorReports report){
		this.report = report;
		this.reportManager = report.getReportManager();
	}
	
	@Override
	public int getTotalRowCount() {
		return totalCount;
	}
	
	@Override
	public List<ReportEvent> getContents() {
		if(contents==null)
			return Collections.emptyList();
		else
			return contents;
	}
	
	
	@Override
	public void updateModel() {
		if(isSortColumnChanged() || isRangeChanged() || searchBtnClicked){
			try {
				SearchCriteria searchCriteria = report.getEventSelection().getSearchCriteria();
				SearchConstraint searchConstraint = WebUtil.getSearchConstraint(this);
				String program = searchCriteria.getProgram();
				if(program.equals(SearchCriteria.PROGRAM_ALL)){
					program = null;
				}
				DateRange range =new DateRange();
				range.setStartTime(searchCriteria.getStartDateTime());
				range.setEndTime(searchCriteria.getEndDateTime());
				
				if(workingsc==null || !workingsc.equals(searchCriteria))
					totalCount= getEventCount(program, range, searchConstraint);
				
				clearTreeNodeCache(null);
				contents=searchEvent(program, range, searchConstraint);
				
				workingsc = (SearchCriteria) BeanUtils.cloneBean(searchCriteria);
				searchBtnClicked = false;
			} catch (Exception e) {
				//TODO
				e.printStackTrace();
			}
		}
	}
	
	protected int getEventCount(String program,DateRange range,
			SearchConstraint searchConstraint) throws Exception{
		return reportManager.getEventCount(program, range, searchConstraint);
	}
	
	protected List<ReportEvent> searchEvent(String program,DateRange range,
			SearchConstraint searchConstraint) throws Exception{
		return reportManager.findEvents(program, range, searchConstraint);
	}
	
	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	
	public void searchAction(){
		searchBtnClicked = true;
	}

	@Override
	public List<ReportEvent> getChildren(ReportEvent event) {
		return Collections.emptyList();
	}
	
	@Override
	public boolean hasChildren(ReportEvent event) {
		return false;
	}
	
	public OperatorReports getReport() {
		return report;
	}
}
