package com.akuacom.pss2.customer.report;

/**
 * The Class JSFClientStatus.
 */
public class JSFClientStatus extends ReportTab {
	
	private static final long serialVersionUID = 172268884193900782L;
    
    private String selectedTab ="clientOfflineStatisticsTab";
    //two sub tabs
    private JSFClientStatusStatistics summary;
	private JSFClientStatusInstance   instance;
    
	public enum Tab {
		clientOfflineStatisticsTab,
		clientOfflineInstanceTab
	}
	
	public JSFClientStatus(CustomerReports report){
		super(report,CustomerReports.Tab.clientStatusTab,
			SearchCriteria.FILTER.participant,
			SearchCriteria.FILTER.event,
			SearchCriteria.FILTER.startDate,
			SearchCriteria.FILTER.startTime,
		    SearchCriteria.FILTER.endDate,
		    SearchCriteria.FILTER.endTime,
			SearchCriteria.FILTER.program
		);
		this.summary = new JSFClientStatusStatistics(this);
		this.instance = new JSFClientStatusInstance(this);
	}
	
	protected boolean needValidation(){
		return false;
	}
	
	public Tab getActiveTab(){
		return  Enum.valueOf(Tab.class, selectedTab);
	}
	
	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public JSFClientStatusStatistics getSummary() {
		return summary;
	}

	public JSFClientStatusInstance getInstance() {
		return instance;
	}
	@Override
	public void resetStatus() {
		getSummary().resetStatus();
		getInstance().resetStatus();
	}
	
}