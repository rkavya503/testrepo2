package com.akuacom.pss2.customer.report;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.jsf.model.TableContentProvider;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.history.vo.ReportEvent;
import com.akuacom.pss2.history.vo.ReportEventParticipant;
import com.akuacom.pss2.history.vo.ReportSummaryVo;
import com.akuacom.utils.lang.DateUtil;

public class JSFUsage extends ReportTab {

	private static final long serialVersionUID = 6723944720774241357L;
	
	private static final Logger log = Logger
				.getLogger(JSFUsage.class.getName());
	
	
	public JSFUsage(CustomerReports report) {
		super(report,CustomerReports.Tab.usageTab,
				SearchCriteria.FILTER.participant,
				SearchCriteria.FILTER.event,
				SearchCriteria.FILTER.startDate
			);
	}
	
	private boolean aggregator;
	private List<String> allPaticipantNames;
	private List<String> contributedPaticipantNames;
	private List<String> nonContributedPaticipantNames;
	
	private String allPaticipantNamesString;
	private String contributedPaticipantNamesString;
	
	private String contributedInfo;
	
	public List<String> getNonContributedPaticipantNames() {
		return nonContributedPaticipantNames;
	}

	public void setNonContributedPaticipantNames(
			List<String> nonContributedPaticipantNames) {
		this.nonContributedPaticipantNames = nonContributedPaticipantNames;
	}

	public boolean isAggregator() {
		return aggregator;
	}

	public void setAggregator(boolean aggregator) {
		this.aggregator = aggregator;
	}

	public List<String> getAllPaticipantNames() {
		return allPaticipantNames;
	}

	public void setAllPaticipantNames(List<String> allPaticipantNames) {
		this.allPaticipantNames = allPaticipantNames;
	}

	public List<String> getContributedPaticipantNames() {
		return contributedPaticipantNames;
	}

	public void setContributedPaticipantNames(
			List<String> contributedPaticipantNames) {
		this.contributedPaticipantNames = contributedPaticipantNames;
	}

	public String getAllPaticipantNamesString() {
		if(allPaticipantNames==null||allPaticipantNames.isEmpty()) return "";
		
		StringBuilder temp = new StringBuilder();
		String comma = ",";
		boolean firstItem = true;
		for(String str:allPaticipantNames){
			if(!firstItem){
				temp.append(comma);
			}
			temp.append(str);
			firstItem = false;
		}
		allPaticipantNamesString = temp.toString();
		
		return allPaticipantNamesString;
	}
	
	public String getNonPaticipantNamesString() {
		if(nonContributedPaticipantNames==null||nonContributedPaticipantNames.isEmpty()) return "";
		
		StringBuilder temp = new StringBuilder();
		String comma = ",";
		boolean firstItem = true;
		for(String str:nonContributedPaticipantNames){
			if(!firstItem){
				temp.append(comma);
			}
			temp.append(str);
			firstItem = false;
		}
		return temp.toString();
	}

	public String getContributedInfo() {
		return contributedInfo;
	}

	public void setContributedInfo(String contributedInfo) {
		this.contributedInfo = contributedInfo;
	}

	public String getContributedPaticipantNamesString() {
		if(contributedPaticipantNames==null||contributedPaticipantNames.isEmpty()) return "";
		
		StringBuilder temp = new StringBuilder();
		String comma = ",";
		boolean firstItem = true;
		for(String str:contributedPaticipantNames){
			if(!firstItem){
				temp.append(comma);
			}
			temp.append(str);
			firstItem = false;
		}
		contributedPaticipantNamesString = temp.toString();
		
		return contributedPaticipantNamesString;
	}

	private TableContentProvider<ReportSummaryVo> reports = 
		new	AbstractTableContentProvider<ReportSummaryVo>(){
	
		private List<ReportSummaryVo> contents  = Collections.emptyList();
		private int totalCount = -1; //-1 indicates no search executed 
		
		@Override
		public void updateModel(){
			try{
				if(!getReportTab().equals(getReport().getActiveTab()))
					return;
				if(!isValid()) return;
				
				boolean scchanged = isSearchCriteriaChange();
				if(scchanged){
					///sync search criteria
					setSearchCriteria(getReport().getWorkingSearchCriteria());
				}
				
				if(isRangeChanged() || isSortColumnChanged() || scchanged){
					contents = getUsageByDate();
				}
				totalCount = contents.size();
				
			}
			catch (Exception e) {
				FDUtils.addMsgError("Internal error");
				log.error("Internal error. ", e);
			}
		}
		
		@Override
		public List<ReportSummaryVo> getContents() {
			if(contents.isEmpty()){
				return initContents();
			}
			return contents;
		}
	
		@Override
		public int getTotalRowCount() {
			return totalCount;
		}
	};
	
	@SuppressWarnings("unused")
	private Date date;
	@SuppressWarnings("unused")
	private String participants;// String split by comma
	@SuppressWarnings("unused")
	private String eventName;
	private boolean exportDisable = true;
	public void exportAction() throws Exception {
		String name = "USAGE_REPORT";
		this.getUsageByDate();
		String body = getExportContent();
		String filename = name + ".csv";

		FDUtils.export(filename, body);
	}

	public Date getDate() {
		return this.getSearchCriteria().getStartDate();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public TableContentProvider<ReportSummaryVo> getReports() {
		return reports;
	}

	public void setReports(TableContentProvider<ReportSummaryVo> reports) {
		this.reports = reports;
	}

	public boolean isExportDisable() {
		return exportDisable;
	}

	public void setExportDisable(boolean exportDisable) {
		this.exportDisable = exportDisable;
	}
//TODO: change this part ,just pass the root participant to flex ,and obtain all the related participants in server side.
	// in this case(customer report), don't pass any eventname to flex
	public String getParticipants() {
		List<String> participantNames = getSearchCriteria().getParticipantNames();
		
		if(participantNames==null||participantNames.isEmpty()){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i< participantNames.size(); i++){
			if(i!=0){
				sb.append(",");
			}
			sb.append(participantNames.get(i));
		}
		
		return "null".equalsIgnoreCase(sb.toString())?"":sb.toString();
	}

	public String getEventName() {
		//TODO
		return "EVENT NAME";
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	private List<ReportSummaryVo> getUsageByDate() {
		HistoryReportManager dataManager = getReportManager();

		Date date = this.getSearchCriteria().getStartDate();
		List<ParticipantVO> participants = this.getSearchCriteria()
				.getParticipants();
		if (date == null) {
			date = DateUtil.getPreviousDay(new Date());
		}
		ReportEvent event = null;
		
		List<ReportEvent> allEvents = null;
		
		ArrayList<String> participantNames = new ArrayList<String>();
		for(ParticipantVO vo: participants){
			List<String> names = dataManager.findAggParticipantNames(vo.getParticipantName());
			participantNames.addAll(names);
		}
		try {
			allEvents = dataManager
					.getRelatedEventsForparticipant(participantNames, getDate());
		} catch (Exception e) {
			FDUtils.addMsgError("Internal Error");
			log.error(e);
		}
		// assume there is one event per day
		if (allEvents != null && !allEvents.isEmpty())
			event = allEvents.get(0);
		
		if(participantNames.size()>1){
			setAggregator(true);
			// set all participants names
			setAllPaticipantNames(participantNames);
			//set all contributed participant
			setContributedPaticipantNames(dataManager.findContributedParticipantNames(getSearchCriteria().getStartDate(), participantNames));
			//set all non contributed
			List<String> nonContributed = (List<String>) participantNames.clone();
			nonContributed.removeAll(getContributedPaticipantNames());
			setNonContributedPaticipantNames(nonContributed);
			int contributedCounts = this.getContributedPaticipantNames()==null?0:this.getContributedPaticipantNames().size();
			this.setContributedInfo("contributing participants "+contributedCounts+"/"+participantNames.size());
			if(event!=null){
				List<String> contributed = new ArrayList<String>();
		        List<ReportEventParticipant> eps =  event.getParticipants();
		        if(eps!=null){
		        	for(ReportEventParticipant ep : eps){
		        		contributed.add(ep.getParticipantName());
		    		}
		        }
				
				List<String> eventContributed = (List<String>) participantNames.clone();//event contributed
				eventContributed.retainAll(contributed);//exclude those participants who doesn't attend this activity
				
				List<String> allContributed = null;
				if(eventContributed!=null&&contributedPaticipantNames!=null){
					allContributed = new ArrayList<String>();
					for(String value :contributedPaticipantNames){
						if(!eventContributed.contains(value)){
							value = value+" *";
						}
						allContributed.add(value);
					}
					
					this.setContributedPaticipantNames(allContributed);
				}
				
			}
		}else{
			setAggregator(false);
			setAllPaticipantNames(Collections.EMPTY_LIST);
			setContributedPaticipantNames(Collections.EMPTY_LIST);
		}
		
		List<ReportSummaryVo> temp = getUsageForEvent(event, dataManager,participantNames);
			
		return convert4Display(temp);
	}

	private List<ReportSummaryVo> getUsageForEvent(ReportEvent event, HistoryReportManager dataManager,List<String> participantNames) {
		// 1. get participants that participant in this event
		
		List<ReportEvent> events = Arrays.asList(event);
		// 2. get usage info for the given participants
		try {
			return dataManager.getReportSummary(participantNames, getDate(), events);
		} catch (Exception e) {
			FDUtils.addMsgError("Internal Error");
			log.error(e);
		}
		
		return null;
	}
	
	private List<ReportSummaryVo> initContents() {
		ReportSummaryVo daySum = new ReportSummaryVo();
		daySum.setCatalog("Entire Day");
		
		ReportSummaryVo eveSum = new ReportSummaryVo();
		eveSum.setCatalog("During Event");
		
		return Arrays.asList(daySum, eveSum);
	}

	private String getExportContent() throws Exception{
		int maxiFractionDigits = 2;
	    NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(maxiFractionDigits);
		
		List<ReportSummaryVo> contents  = getUsageByDate();
		if(contents.isEmpty()){
			contents = initContents();
		}
 		StringBuffer exportContent = new StringBuffer();
 		
 		exportContent.append("Report Summary for "+DateUtil.format(this.getSearchCriteria().getStartDate(), "MM/dd/yyyy")+"\n"); 		
 		// append title
 		exportContent.append(", Baseline-Avg(KW),Baseline-Total(KWH),Actual-Avg(KW),Actual-Total(KWH),Shed-Avg(KW),Shed-Total(KWH)"+"\n");
 		//append the first row 
 		exportContent.append("Entire Day," + Double.valueOf(nf.format(contents.get(0).getBaseAvg())) +","+ Double.valueOf(nf.format(contents.get(0).getBaseTotal())) +","+ Double.valueOf(nf.format(contents.get(0).getActualAvg())) +","+ Double.valueOf(nf.format(contents.get(0).getActualTotal())) +","+ Double.valueOf(nf.format(contents.get(0).getShedAvg())) +","+ Double.valueOf(nf.format(contents.get(0).getShedTotal())) +"\n");
 		//append the second row
 		exportContent.append("During Event," + Double.valueOf(nf.format(contents.get(1).getBaseAvg())) +","+ Double.valueOf(nf.format(contents.get(1).getBaseTotal())) +","+ Double.valueOf(nf.format(contents.get(1).getActualAvg())) +","+ Double.valueOf(nf.format(contents.get(1).getActualTotal())) +","+ Double.valueOf(nf.format(contents.get(1).getShedAvg())) +","+ Double.valueOf(nf.format(contents.get(1).getShedTotal())) +"\n");

		List<ParticipantVO> participants = this.getReport().getParticipant();
		List<String> participantNames = new ArrayList<String>();
			for(ParticipantVO vo: participants){
				List<String> names = getReportManager().findAggParticipantNames(vo.getParticipantName());
				participantNames.addAll(names);
			}
			
 	    List<PDataEntry> baseline = this.getReportManager().findForecastUsageDataEntryList(participantNames, this.getSearchCriteria().getStartDate());
 	    List<PDataEntry> usageline =this.getReportManager().findRealTimeUsageDataEntryList(participantNames, this.getSearchCriteria().getStartDate());
		
 	    Date date = DateUtil.stripTime(new Date());
	    Date nextDay = DateUtil.stripTime(DateUtil.getNextDay(date));
	    
	    HistoryReportManager dataManager = getReportManager();	   
	    PDataSet set = dataManager.getDataSetByName("usage");

	    int offset = (int) set.getPeriod();
		
	    exportContent.append("Baseline:,,,Usage:");
	    exportContent.append("\n");
	    exportContent.append("Time,Value,,Time,Value");
	    
	    List<String> baselinetitles = new ArrayList<String>();
	    while(date.before(nextDay)){
	    	baselinetitles.add(DateUtil.format(date, "HH:mm"));
	    	date = DateUtil.add(date, Calendar.SECOND, offset);
	    }

	 	Date startTime = null;
	 	Double[] baseEntrys = new Double[24*60*60/offset];
	 	date = DateUtil.stripTime(new Date());
	 	
	 	for(int i=0; i<baseline.size();i++){
	 		if(startTime == null) startTime = DateUtil.stripTime(baseline.get(i).getTime());
	 		int index = (int) ((baseline.get(i).getTime().getTime()-startTime.getTime())/(offset*1000));
	 	 	baseEntrys[index] = Double.valueOf(nf.format(baseline.get(i).getValue()));
		}
		nf.setMaximumFractionDigits(3);
		
//		exportContent = appendTitles(exportContent, limit, baselinetitles, baseEntrys);

//		exportContent.append("Usage:");
	 	//append usage line row title
		
	 	Double[] usageEntrys = new Double[24*60*60/offset];
	 	
	 	for(int i=0; i<usageline.size();i++){
	 		if(startTime == null) startTime = DateUtil.stripTime(usageline.get(i).getTime());
	 		int index = (int) ((usageline.get(i).getTime().getTime()-startTime.getTime())/(offset*1000));
	 		usageEntrys[index] = Double.valueOf(nf.format(usageline.get(i).getValue()));
		}
	 	exportContent = appendTitles(exportContent, baselinetitles, baseEntrys, usageEntrys);
	 
		exportContent.append("\n");
	  
		return exportContent.toString();
	}

	private StringBuffer appendTitles(StringBuffer exportContent,
			List<String> baselinetitles, Double[] baseEntrys, Double[] usageEntrys) {
		for(int i=0; i<baselinetitles.size();i++){
			exportContent.append("\n");
			exportContent.append(baselinetitles.get(i));
			exportContent.append(",");
			if(baseEntrys[i]!=null)
				exportContent.append(baseEntrys[i]);	
			exportContent.append(",");
			exportContent.append(",");
			exportContent.append(baselinetitles.get(i));
			exportContent.append(",");
			if(usageEntrys[i]!=null)
				exportContent.append(usageEntrys[i]);	
		}
		
		return exportContent;
	}
	@Override
	public void resetStatus() {
		
	}
	
	private List<ReportSummaryVo> convert4Display(
			List<ReportSummaryVo> temp) {
		if(temp == null){
			temp = new ArrayList<ReportSummaryVo>();
		}
		boolean existEnireVo = false;
		boolean existEnventVo = false;
		exportDisable = true;
		for(ReportSummaryVo vo: temp){
			if("Entire Day".equals(vo.getCatalog())){
				existEnireVo = true;
				exportDisable = false; //make export button enable when exists proper data
			}else{
				existEnventVo = true;
			}
		}
		
		if(!existEnireVo){
			ReportSummaryVo daySum = new ReportSummaryVo();
			daySum.setCatalog("Entire Day");
			temp.add(daySum);
		}
		if(!existEnventVo){
			ReportSummaryVo eveSum = new ReportSummaryVo();
			eveSum.setCatalog("During Event");
			temp.add(eveSum);
		}
		return temp;
	}
	
}
