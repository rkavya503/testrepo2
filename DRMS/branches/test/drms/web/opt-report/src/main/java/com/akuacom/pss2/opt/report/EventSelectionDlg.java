package com.akuacom.pss2.opt.report;

import java.io.Serializable;

public class EventSelectionDlg implements Serializable{
	
	private static final long serialVersionUID = -5322083050730129386L;
	
	private OperatorReports report;
	
	private SearchCriteria searchCriteria;
	
	public EventSelectionDlg(OperatorReports report){
		this.report = report;
	}
	
	private EventModel event;
	
	public Object getSelection(){
		return getEvent().getFirstSelected();
	}
	
	public EventModel getEvent() {
		if( event==null)
			event = new EventModel(report);
		return event;
	}
	
	public void okAction(){
		Object obj = this.getSelection();
		report.getSearchCriteria().setFilterByObject(obj);
	}
	
	public OperatorReports getReport() {
		return report;
	}

	public SearchCriteria getSearchCriteria() {
		if(searchCriteria==null){
			searchCriteria = new SearchCriteria();
		}
		return searchCriteria;
	}
	
}
