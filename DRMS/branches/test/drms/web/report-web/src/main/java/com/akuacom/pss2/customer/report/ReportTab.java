package com.akuacom.pss2.customer.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.utils.DateUtil;

public abstract class ReportTab implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private SearchCriteria.FILTER[] searchFilters;
	
	private CustomerReports report;
	
	private CustomerReports.Tab reportTab;
	
	private SearchCriteria searchCriteria;
	
	private boolean valid = false;
	
	public  abstract void resetStatus();
	
	protected static String formatPercent(BigDecimal decimal){
		if(decimal==null) return ""; 
		decimal= decimal.multiply(new BigDecimal(100));
		decimal=decimal.setScale(1, BigDecimal.ROUND_HALF_UP);
		return decimal.toString();
	}
	
	public ReportTab(CustomerReports report,CustomerReports.Tab tab,SearchCriteria.FILTER... searchFilters){
		this.report = report;
		this.searchFilters = searchFilters;
		this.reportTab = tab;
		addPhaseListener();
	}
	
	public SearchCriteria.FILTER[] getSearchFilters() {
		return searchFilters;
	}
	
	public HistoryReportManager getReportManager(){
		return this.report.getReportManager();
	}
	
	public SearchCriteria getSearchCriteria() {
		if(searchCriteria==null)
			searchCriteria = new SearchCriteria();
		return searchCriteria;
	}
	
	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	protected boolean isSearchCriteriaChange(){
		SearchCriteria sc = report.getWorkingSearchCriteria();
		SearchCriteria workingsc = getSearchCriteria();
		
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
				boolean b = SearchCriteria.isSame(workingsc, sc, getSearchFilters());
				return !b;				
			}
		}
	}
	
	protected boolean needValidation(){
		return getReportTab().equals(getReport().getActiveTab());
	}
	
	protected void addPhaseListener(){
		FacesContext.getCurrentInstance().getViewRoot().addPhaseListener(new PhaseListener(){
			private static final long serialVersionUID = 1L;
			
			public void beforePhase(PhaseEvent event){
				
			}
			public void afterPhase(PhaseEvent event){
				if(needValidation() &&
						event.getPhaseId() == PhaseId.INVOKE_APPLICATION){
							String msg= validate();
							FDUtils.addMsgError(msg);
							valid=( msg==null);
				}
			}
			public PhaseId getPhaseId(){
				return PhaseId.INVOKE_APPLICATION;
			}
		}); 
	}
	
	public boolean isValid(){
		return valid;
	}
	
	public String validate(){
		SearchCriteria sc=report.getWorkingSearchCriteria();
		if(sc==null) return null;
		
		List<SearchCriteria.FILTER> filters =Arrays.asList(getSearchFilters());
		if(sc.getParticipantName()==null){
			return "must select a participant";  
		}
		
		//contains end date
		if(filters.indexOf(SearchCriteria.FILTER.endDate)>=0){
			//contains start time and end time
			if(sc.getEndDate().after(new Date()))
				return "end date can not be a date in the future";  
			if(filters.indexOf(SearchCriteria.FILTER.startTime)>=0
				&& filters.indexOf(SearchCriteria.FILTER.endTime)>=0){
					if(sc.getStartDateTime().after(sc.getEndDateTime()))
						return "start date and time must be earlier than end date and time";
				}
				else{
					if(sc.getStartDate().after(sc.getEndDate()))
						return  "start date must be earlier than end date";
			}
		}else{
			if(sc.getStartDate().after(DateUtil.getEndOfDay(new Date())))
				return "A future date was entered"; 
		}
		return null;
	}
	
	public CustomerReports getReport() {
		return report;
	}

	public void setReport(CustomerReports report) {
		this.report = report;
	}

	public CustomerReports.Tab getReportTab() {
		return reportTab;
	}
}
