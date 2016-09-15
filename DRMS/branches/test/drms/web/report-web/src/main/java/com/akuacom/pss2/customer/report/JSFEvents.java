package com.akuacom.pss2.customer.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.jsf.model.SortColumn;
import com.akuacom.jsf.model.TableContentProvider;
import com.akuacom.jsf.model.TreeContentProvider;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.history.vo.ReportEvent;
import com.akuacom.pss2.history.vo.ReportEventPerformance;
import com.akuacom.pss2.history.vo.ReportSignalMaster;
import com.akuacom.pss2.history.vo.SignalNameConstants;

public class JSFEvents extends ReportTab {
	/**
	 * Constructor
	 * @param report
	 */
	public JSFEvents(CustomerReports report) {
		super(report, CustomerReports.Tab.eventsTab,
				SearchCriteria.FILTER.participant, SearchCriteria.FILTER.event,
				SearchCriteria.FILTER.startDate, SearchCriteria.FILTER.endDate,
				SearchCriteria.FILTER.program);
	}
	//------------------------------------------------Attributes-----------------------------------------------------------
	/** serial version*/
	private static final long serialVersionUID = -2980615526144560104L;
	
	/** logger*/
	private static final Logger log = Logger.getLogger(JSFEvents.class.getName());
	
//	/** program all flag*/
//	private static final String PROGRAM_ALL="ALL";
	
	/** display signal page flag*/
	private boolean displaySignalDetail = false;
	
	/** export disable flag for events*/
	boolean exportDisable=true;
	
	/** export disable flag for events performance*/
	boolean exportPerformanceDisable=true;
	
	/** export disable flag for events signal*/
	boolean exportSignalDisable=true;

	/** selected event name*/
	private String selectedEventName;
	
	/** selected program name*/
	private String selectedProgramName;
	
	/** selected client name*/
	private String selectedClientName;

	/** event signal display flag -- CPP price*/
	private boolean cppPriceColDisplay = false;
    
	/** event signal display flag -- mode*/
	private boolean modeColDisplay = false;
    
	/** event signal display flag -- price*/
	private boolean priceColDisplay = false;
    
	/** event signal display flag -- pending*/
	private boolean pendingColDisplay = false;
    
	/** event signal display flag -- bid*/
	private boolean bidColDisplay = false;
	
	/** event performance action flag*/
	private boolean eventPerformanceActionFlag = false;
	
	/** event signal action flag*/
	private boolean eventSignalActionFlag = false;
	
	public static final String performanceSummaryTab = "performanceSummaryTab";
	public static final String signalDetailTab = "signalDetailTab";	
    private String selectedTab = performanceSummaryTab;
    
    private boolean toResetEventTable = false;
    
	/** event table content provider*/
	private TableContentProvider<ReportEvent> events = new AbstractTableContentProvider<ReportEvent>() {

		private List<ReportEvent> contents = Collections.emptyList();
		private int totalCount =  -1; //-1 indicates no search executed

		@Override
		public void updateModel() {
			
			try {
							
				if (!getReportTab().equals(getReport().getActiveTab())){
					return;
				}
				
				if(!isValid()) return;
				
				SearchConstraint  searchConstraint = WebUtil.getSearchConstraint(this);
				boolean scchanged = isSearchCriteriaChange();
				if(scchanged){
					///sync search criteria
					setSearchCriteria(getReport().getWorkingSearchCriteria());
				}
				SearchCriteria sc = getSearchCriteria();
				if(isRangeChanged() || isSortColumnChanged() || scchanged){
					contents = getReportEvent(searchConstraint,sc);
					totalCount = getReportEventCount(searchConstraint,sc);	
					if(contents.size()>0){
						exportDisable = false;
					}else{
						exportDisable = true;
					}
					//DRMS-5644
					//Clean the reportPerformanceTree
					//Clean the selectedEventName
					//Set the exportPerformanceDisable to false
					if(reportPerformanceTree!=null){
						if(reportPerformanceTree.getContents()!=null){
							reportPerformanceTree.getContents().clear();
						}
					}
					exportPerformanceDisable=true;
				}
			} catch (Exception e) {
				FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
				log.error(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL, e);
			}
		}

		@Override
		public List<ReportEvent> getContents() {
			return contents;
		}

		@Override
		public int getTotalRowCount() {
			return totalCount;
		}
		
		@Override
		public void resetSortingOrpagination() {
			super.resetSortingOrpagination();
			if(toResetEventTable){
				getSequenceRange().setFirstRow(0);
				setSortColumn(null);
				toResetEventTable =false;
			}
		}
	};
	
	/** signal column names*/
	private List<String> signalDetailColumnNames = Collections.emptyList();
	
	/** event signal table content provider*/
	private TableContentProvider<ReportSignalMaster> eventSignal 
			= new AbstractTableContentProvider<ReportSignalMaster>() {

		private List<ReportSignalMaster> contents = Collections.emptyList();
		private int totalCount = -1; //indicates no search executed
		
		
		
		@Override
		public void updateModel() {
			try {
				
				if (!getReportTab().equals(getReport().getActiveTab())){
					return;
				}
				SearchConstraint  searchConstraint = WebUtil.getSearchConstraint(this);
				boolean scchanged = isSearchCriteriaChange();
				if(scchanged){
					///sync search criteria
					setSearchCriteria(getReport().getWorkingSearchCriteria());
				}
				SearchCriteria sc = getSearchCriteria();
				if(eventSignalActionFlag){
					eventSignalActionFlag = false;
					signalDetailColumnNames = Collections.emptyList();
					signalDetailColumnNames = getReportManager()
						.getSignalNames(getSelectedEventName(), getSelectedClientName());
					
					if(signalDetailColumnNames != null){
						resetSignalColumnDisplayFlag();
						for (String signalDetailColName : signalDetailColumnNames) {
							if(signalDetailColName.equals(SignalNameConstants.CPP_PRICE_SIGNAL_NAME)){
								setCppPriceColDisplay(true);
							}
							if(signalDetailColName.equals(SignalNameConstants.BID_SIGNAL_NAME)){
								setBidColDisplay(true);
							}
							if(signalDetailColName.equals(SignalNameConstants.MODE_SIGNAL_NAME)){
								setModeColDisplay(true);
							}
							if(signalDetailColName.equals(SignalNameConstants.PENDING_SIGNAL_NAME)){
								setPendingColDisplay(true);
							}
							if(signalDetailColName.equals(SignalNameConstants.PRICE_SIGNAL_NAME)){
								setPriceColDisplay(true);
							}
						}
						contents = getReportEventSignal(searchConstraint,sc);
						totalCount= contents.size();
						reportSignalMasterContents = contents;
						if(contents.size()>0){
							exportSignalDisable = false;
						}else{
							exportSignalDisable = true;
						}
					}	
				}else if(isSortColumnChanged()){
					
					signalDetailColumnNames = Collections.emptyList();
					signalDetailColumnNames = getReportManager()
						.getSignalNames(getSelectedEventName(), getSelectedClientName());
					
					if(signalDetailColumnNames != null){
						resetSignalColumnDisplayFlag();
						for (String signalDetailColName : signalDetailColumnNames) {
							if(signalDetailColName.equals(SignalNameConstants.CPP_PRICE_SIGNAL_NAME)){
								setCppPriceColDisplay(true);
							}
							if(signalDetailColName.equals(SignalNameConstants.BID_SIGNAL_NAME)){
								setBidColDisplay(true);
							}
							if(signalDetailColName.equals(SignalNameConstants.MODE_SIGNAL_NAME)){
								setModeColDisplay(true);
							}
							if(signalDetailColName.equals(SignalNameConstants.PENDING_SIGNAL_NAME)){
								setPendingColDisplay(true);
							}
							if(signalDetailColName.equals(SignalNameConstants.PRICE_SIGNAL_NAME)){
								setPriceColDisplay(true);
							}
						}
						contents = getReportEventSignal(searchConstraint,sc);
						if(contents.size()>0){
							exportSignalDisable = false;
						}else{
							exportSignalDisable = true;
						}
						totalCount = contents.size();
					}
					
					
					List<ReportSignalMaster> allSignals = contents;
					List<ReportSignalMaster> sortedSignals = new ArrayList<ReportSignalMaster>();
					if(allSignals != null){
						
						TreeMap signalMap = new TreeMap();
						NavigableMap navigableSignalMap = new TreeMap();
//						searchConstraint=null;						
						SortColumn sortColumn = eventSignal.getSortColumn();
						if(sortColumn.getName().equals("signalTime")){
							for (ReportSignalMaster reportSignalMaster : allSignals) {
								signalMap.put(reportSignalMaster.getSignalTime(), reportSignalMaster);
							}
						}
						else if(sortColumn.getName().equals("pending")){
							Integer i = 0;
							String key = "";
							for (ReportSignalMaster reportSignalMaster : allSignals) {
								i++;
								if(reportSignalMaster.getPending() != null){
									key = reportSignalMaster.getPending();
								}

								if(signalMap.containsKey(key)){
									signalMap.put(key + i, reportSignalMaster);
								}
								else{
									signalMap.put(key, reportSignalMaster);
								}
								key = "";
							}									
						}
						else if(sortColumn.getName().equals("mode")){
							Integer i = 0;
							String key = "";

							for (ReportSignalMaster reportSignalMaster : allSignals) {
								i++;
								
								if(reportSignalMaster.getMode() != null){
									key = reportSignalMaster.getMode();
								}

								if(signalMap.containsKey(key)){
									signalMap.put(key + i, reportSignalMaster);
								}
								else{
									signalMap.put(key, reportSignalMaster);
								}
								key = "";
							}
						}
						else if(sortColumn.getName().equals("price")){
							for (ReportSignalMaster reportSignalMaster : allSignals) {
								double numberValue=-1;
								try{
									numberValue= Double.valueOf(reportSignalMaster.getPrice());
								}catch(Exception e){
									//The value from report signal master get is null
									//and use the default value -1 to set the object location
								}
								signalMap = putNumberSortObject(signalMap,numberValue,reportSignalMaster);
							}
						}
						else if(sortColumn.getName().equals("CPPPrice")){

							for (ReportSignalMaster reportSignalMaster : allSignals) {
								double numberValue=-1;
								try{
									numberValue= Double.valueOf(reportSignalMaster.getCPPPrice());
								}catch(Exception e){
									//The value from report signal master get is null
									//and use the default value -1 to set the object location
								}
								signalMap = putNumberSortObject(signalMap,numberValue,reportSignalMaster);
							}
						}
						else if(sortColumn.getName().equals("bid")){
							for (ReportSignalMaster reportSignalMaster : allSignals) {
								double numberValue=-1;
								try{
									numberValue= Double.valueOf(reportSignalMaster.getBid());
								}catch(Exception e){
									//The value from report signal master get is null
									//and use the default value -1 to set the object location
								}
								signalMap = putNumberSortObject(signalMap,numberValue,reportSignalMaster);
							}
						}
						Collection signalCollection;
						if(sortColumn.isAscendent()){
							signalCollection = signalMap.values();
						}
						else{
							navigableSignalMap = signalMap.descendingMap();
							signalCollection = navigableSignalMap.values();
						}

						if(signalCollection != null){
							for (Object object : signalCollection) {
								sortedSignals.add((ReportSignalMaster)object);
							}
						}
					}
					contents = sortedSignals;
					totalCount = contents.size();
					reportSignalMasterContents = contents;
				}
			} catch (Exception e) {
				FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
				log.error(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL, e);
			}
		}

		@Override
		public List<ReportSignalMaster> getContents() {
			return contents;
		}

		@Override
		public int getTotalRowCount() {
			return totalCount;
		}
	};
	
	private List<ReportSignalMaster> reportSignalMasterContents = Collections.emptyList(); 
	
	/** event performance tree table content provider*/
	private TreeContentProvider<ReportEventPerformance> reportPerformanceTree = 
		new AbstractTreeContentProvider<ReportEventPerformance>(){

		private List<ReportEventPerformance> contents;
		private int totalRowCount = -1; //-1 indicates no search executed
		@Override
		public List<ReportEventPerformance> getChildren(ReportEventPerformance parent) {
			List<ReportEventPerformance> result = new ArrayList<ReportEventPerformance>();
			try {
				result = getReportManager().getCustomerChildrenEventPerformance(
						parent.getParticipantName(),
						parent.getProgramName(),
						parent.getEventName(),
						null);
			} catch (Exception e) {
				FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
				log.error(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL, e);
			}
			
			return result;
		}

		@Override
		public boolean hasChildren(ReportEventPerformance parent) {
			return !parent.isClient();
		}
		
		@Override
		public List<ReportEventPerformance> getContents() {
			return contents;
		}
		
		@Override
		public int getTotalRowCount() {
			return totalRowCount;
		}

		@Override
		public void updateModel() {
			
			try {
						
				if (!getReportTab().equals(getReport().getActiveTab())){
					return;
				}
				boolean scchanged = isSearchCriteriaChange();
				if(scchanged){
					///sync search criteria
					setSearchCriteria(getReport().getWorkingSearchCriteria());
				}
				SearchCriteria sc = getSearchCriteria();
				
				//TODO Refactoring 
				if(getSelectedEventName()==null){
					clearTreeNodeCache(null);
					contents =Collections.emptyList();
					totalRowCount = -1;
					exportPerformanceDisable= true;
				}
				else if(eventPerformanceActionFlag){
					clearTreeNodeCache(null);
					eventPerformanceActionFlag = false;
					contents = getReportEventParticipantPerformance(null,sc);
					if(contents.size()>0){
						exportPerformanceDisable = false;
					}else{
						exportPerformanceDisable = true;
					}
					totalRowCount = contents.size();
				}
				if(eventSignalActionFlag){
					eventSignal.updateModel();
				}
			} catch (Exception e) {
				FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
				log.error(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL, e);
			}
		}
	};
	
	
	
	//------------------------------------------------Functions------------------------------------------------------------
	
	/**
	 * Private function for retrieve event signal
	 */
	private List<ReportSignalMaster> getReportEventSignal(
			SearchConstraint  searchConstraint , SearchCriteria sc)throws Exception{
		List<ReportSignalMaster> contents = Collections.emptyList();
		contents = getReportManager().getEventSignal(getSelectedEventName(), getSelectedClientName(),null);
		
		return contents;
	
	}
	
	/**
	 * Private function for retrieve event participation performance
	 * @param searchConstraint
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	private List<ReportEventPerformance> getReportEventParticipantPerformance(
			SearchConstraint  searchConstraint , SearchCriteria sc)throws Exception{
		List<ReportEventPerformance> contents = Collections.emptyList();
//		contents = getReportManager().getCustomerEventPerformance(getSelectedProgramName(),
//				getSelectedEventName(), searchConstraint);
		//DRMS-7048
		String participantName = sc.getParticipantName();
		contents = getReportManager().getCustomerEventPerformance(getSelectedProgramName(),
				getSelectedEventName(),participantName, searchConstraint);
		return contents;
	}
	private List<ReportEventPerformance> getReportAllEventPerformance()throws Exception{
		List<ReportEventPerformance> contents = Collections.emptyList();
//		contents = getReportManager().getCustomerAllEventPerformance(getSelectedProgramName(), getSelectedEventName());
		//DRMS-7048
		String participantName = getSearchCriteria().getParticipantName();
		contents = getReportManager().getCustomerAllEventPerformance(getSelectedProgramName(), getSelectedEventName(),participantName);
		//DRMS-7402
		for(ReportEventPerformance content:contents){
			String participant = content.getParticipantName();
			if(participant.indexOf(".")==-1){
				//participant
				List<ReportEventPerformance> participantPerformanceList = Collections.emptyList();
				participantPerformanceList = getReportManager().getCustomerEventPerformance(getSelectedProgramName(),
						getSelectedEventName(),participant, null);
				if(participantPerformanceList.size()>0){
					ReportEventPerformance bean = participantPerformanceList.get(0);
					content.setAvgShed(bean.getAvgShed());
					content.setTotalShed(bean.getTotalShed());
				}
			}
		}
		
		return contents;
	}
	/**
	 * Private function for retrieve report event
	 * @param searchConstraint
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	private List<ReportEvent> getReportEvent(SearchConstraint  searchConstraint , SearchCriteria sc)throws Exception{
		List<ReportEvent> contents = Collections.emptyList();
		List<String> selectPrograms=new ArrayList<String>();
		if (sc.getProgram().equals(ReportConstants.PROGRAM_ALL)){
			List<String> programs = getReport().getAllProgramOptions();
			for(String program:programs){
					selectPrograms.add(program);
			}
		}else{
			selectPrograms.add(sc.getProgram());
		}
		
		if(sc.getParticipants()!=null){
			contents = getReportManager().getReportEvent(sc.getParticipantNames(), 
					selectPrograms, sc.getStartOfStartDate(),
					sc.getEndOfEndDate(), searchConstraint);
		}
		return contents;
	}
	
	/**
	 * Private function for retrieve event count
	 * @param searchConstraint
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	private int getReportEventCount(SearchConstraint  searchConstraint , SearchCriteria sc) throws Exception{
		int totalCount=0;
		List<String> selectPrograms=new ArrayList<String>();
		if (sc.getProgram().equals(ReportConstants.PROGRAM_ALL)){
			List<String> programs = getReport().getAllProgramOptions();
			for(String program:programs){
					selectPrograms.add(program);
			}
		}else{
			selectPrograms.add(sc.getProgram());
		}
		
		if(sc.getParticipants()!=null){
			totalCount = getReportManager().getReportEventCount(
					sc.getParticipantNames(),selectPrograms,
					sc.getStartOfStartDate(), sc.getEndOfEndDate());
		}
		return totalCount;
	}
	
	@Override
	public void resetStatus() {
		toResetEventTable = true;
		setSelectedTab(performanceSummaryTab);
		displaySignalDetail = false;
		selectedEventName = null;
	}
	
	/**
	 * Function for invoke event performance
	 */
	public void eventPerformanceAction() {
		eventPerformanceActionFlag = true;
		setSelectedTab(performanceSummaryTab);
		displaySignalDetail = false;
		FacesContext ctx = FacesContext.getCurrentInstance();  
		selectedEventName = (String)ctx.getExternalContext().getRequestParameterMap().get("selectedEventName");
		setSelectedProgramName((String)ctx.getExternalContext().getRequestParameterMap().get("selectedProgramName"));
	}
	
	/**
	 * Function for invoke event signal
	 */
	public void eventSignalAction(){
		setEventSignalActionFlag(true);
		setSelectedTab(signalDetailTab);
		FacesContext ctx = FacesContext.getCurrentInstance();  
		setSelectedClientName((String)ctx.getExternalContext().getRequestParameterMap().get("selectedClientName"));
		displaySignalDetail = true;
	}
	
	/**
	 * Function for invoke export event data
	 */
	public void exportEventsAction(){
		String filename = "EVENTS_REPORT" + ".csv";
        
        try {
        	FDUtils.export(filename, getExportEventsContent());
        }catch(Exception e) {
        	FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_EXPORT+ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
        	log.error(ReportConstants.WARNING_MESSAGE_ERROR_EXPORT+e.getMessage());
        }
	}	
	/**
	 * Function for invoke export event data
	 */
	public void exportEventPerformanceAction(){
		String filename = "EVENTS_PERFORMANCE_SUMMARY_REPORT" + ".csv";
        
        try {
        	FDUtils.export(filename, getExportEventPerformanceContent());
        }catch(Exception e) {
        	FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_EXPORT+ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
        	log.error(ReportConstants.WARNING_MESSAGE_ERROR_EXPORT+e.getMessage());
        }
        
	}
	/**
	 * Function for invoke export event data
	 */
	public void exportEventSignalAction(){
		String filename = "SIGNAL_DETAIL_REPORT" + ".csv";
        
        try {
        	FDUtils.export(filename, getExportEventSignalContent());
        }catch(Exception e) {
        	FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_EXPORT+ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
        	log.error(ReportConstants.WARNING_MESSAGE_ERROR_EXPORT+e.getMessage());
        }
	}
	/**
	 * Private function for retrieve event data content
	 * @return
	 * @throws Exception
	 */
	private String getExportEventsContent() throws Exception{
		
		String exportContent = "";
		List<ReportEvent> contents;
		
		boolean scchanged = isSearchCriteriaChange();
		if(scchanged){
			///sync search criteria
			setSearchCriteria(getReport().getWorkingSearchCriteria());
		}
		SearchConstraint  constraint = WebUtil.getSearchConstraint(events);
		constraint.setRowCount(-1);
		SearchCriteria sc = getSearchCriteria();
		contents = getReportEvent(constraint,sc);
		exportContent = exportContent + "EVENT ID, PROGRAM, START DATE/TIME, END DATE/TIME \n";
		
		for (ReportEvent c : contents) {
			exportContent = exportContent + c.getEventName() + ", ";
			exportContent = exportContent + c.getProgramName() + ", ";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            if(c.getStartTime()!=null){
				exportContent = exportContent + simpleDateFormat.format(c.getStartTime()) + ", ";
			}else{
				exportContent = exportContent + ", ";
			}
			if(c.getEndTime()!=null){
				exportContent = exportContent + simpleDateFormat.format(c.getEndTime()) + ", ";
			}else{
				exportContent = exportContent + ", ";
			}
			
			exportContent = exportContent + " \n";				
		}
		return exportContent;		
	}

	/**
	 * Private function for retrieve event data content
	 * @return
	 * @throws Exception
	 */
	private String getExportEventPerformanceContent() throws Exception{
		
		String exportContent = "";
		List<ReportEventPerformance> contents;
		
		boolean scchanged = isSearchCriteriaChange();
		if(scchanged){
			///sync search criteria
			setSearchCriteria(getReport().getWorkingSearchCriteria());
		}
		SearchCriteria sc = getSearchCriteria();
//		contents = getReportEventPerformance(null,sc);
		contents = getReportAllEventPerformance();
		
		exportContent = exportContent + "ENTITY, PROGRAM, EVENT, START, END, AVG SHED, TOTAL SHED \n";
		
		for (ReportEventPerformance c : contents) {
			exportContent = exportContent + c.getParticipantName() + ", ";
			exportContent = exportContent + c.getProgramName() + ", ";
			exportContent = exportContent + c.getEventName() + ", ";
//			exportContent = exportContent + dateFormat.format(c.getStartTime()) + ", ";
//			exportContent = exportContent + dateFormat.format(c.getEndTime()) + ", ";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            if(c.getStartTime()!=null){
				exportContent = exportContent + simpleDateFormat.format(c.getStartTime()) + ", ";
			}else{
				exportContent = exportContent + ", ";
			}
			if(c.getEndTime()!=null){
				exportContent = exportContent + simpleDateFormat.format(c.getEndTime()) + ", ";
			}else{
				exportContent = exportContent + ", ";
			}
			
			if(c.isClient()){
				exportContent = exportContent +  ", ";
				exportContent = exportContent +  ", ";
			}else{
				if(c.getAvgShed()!=null){
					exportContent = exportContent + c.getAvgShed() + ", ";
				}else{
					exportContent = exportContent +  ", ";
				}
				if(c.getTotalShed()!=null){
					exportContent = exportContent + c.getTotalShed() + ", ";
				}else{
					exportContent = exportContent +  ", ";
				}
			}

			exportContent = exportContent + " \n";				
		}
		return exportContent;		
	}
	
	private List<String> sortHeader(List<String> columnHeaders){
		List<String> result = new ArrayList<String>();
		for (String header : columnHeaders) {
			if(header.equals(SignalNameConstants.BID_SIGNAL_NAME)){
				result.add(header);
				columnHeaders.remove(header);
				break;
			}
		}
		for (String header : columnHeaders) {
			if(header.equals(SignalNameConstants.MODE_SIGNAL_NAME)){
				result.add(header);
				columnHeaders.remove(header);
				break;
			}
		}
		for (String header : columnHeaders) {
			if(header.equals(SignalNameConstants.PRICE_SIGNAL_NAME)){
				result.add(header);
				columnHeaders.remove(header);
				break;
			}
		}
		for (String header : columnHeaders) {
			if(header.equals("pending")){
				result.add(header);
				columnHeaders.remove(header);
				break;
			}
		}
		for (String header : columnHeaders) {
			result.add(header);
		}
		return result;
	}
	
	/**
	 * Private function for retrieve event data content
	 * @return
	 * @throws Exception
	 */
	private String getExportEventSignalContent() throws Exception{
		SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		String exportContent = "";
		List<ReportSignalMaster> contents;
		
		boolean scchanged = isSearchCriteriaChange();
		if(scchanged){
			///sync search criteria
			setSearchCriteria(getReport().getWorkingSearchCriteria());
		}
		SearchCriteria sc = getSearchCriteria();
//		contents = getReportEventSignal(null,sc);
		if(reportSignalMasterContents.size()>0){
			contents = reportSignalMasterContents;
		}else{
			contents = getReportEventSignal(null,sc);
		}
		//reportSignalMasterContents;
		signalDetailColumnNames = sortHeader(signalDetailColumnNames);
		List<String> columnHeaders = signalDetailColumnNames;
		
		exportContent = exportContent + "TIME , ";
		for (String header : columnHeaders) {
			if(header.equals("pending")){
				header = "Status";
			}
			if(header.equals(SignalNameConstants.BID_SIGNAL_NAME)){
				header = "Bid";
			}
			if(header.equals(SignalNameConstants.MODE_SIGNAL_NAME)){
				header = "Mode";
			}
			if(header.equals(SignalNameConstants.PRICE_SIGNAL_NAME)){
				header = "Price";
			}
			exportContent = exportContent + header + ", ";	
		}
		exportContent = exportContent + " \n";
		
		for (ReportSignalMaster reportSignalMaster : contents) {
			
			exportContent = exportContent + timeFormat.format(reportSignalMaster.getSignalTime()) + ", ";
			
			for (String header : columnHeaders) {
				if(header.equals(SignalNameConstants.CPP_PRICE_SIGNAL_NAME)){
					if(reportSignalMaster.getCPPPrice() == null){
						exportContent = exportContent + ", ";
					}
					else{
						exportContent = exportContent + reportSignalMaster.getCPPPrice()  + ", ";
					}
				}
				if (header.equals(SignalNameConstants.BID_SIGNAL_NAME)) {
					if(reportSignalMaster.getBid() == null){
						exportContent = exportContent + ", ";
					}
					else{
						exportContent = exportContent + reportSignalMaster.getBid() + ", ";						
					}
				}
				if (header.equals(SignalNameConstants.MODE_SIGNAL_NAME)) {
					if(reportSignalMaster.getMode() == null){
						exportContent = exportContent + ", ";
					}
					else{
						exportContent = exportContent + reportSignalMaster.getMode() + ", ";	
					}
				}
				if (header.equals(SignalNameConstants.PENDING_SIGNAL_NAME)) {
					if(reportSignalMaster.getPending() == null){
						exportContent = exportContent + ", ";
					}
					else{
						exportContent = exportContent + reportSignalMaster.getPending() + ", ";	
					}
				}
				if (header.equals(SignalNameConstants.PRICE_SIGNAL_NAME)) {
					if(reportSignalMaster.getPrice() == null){
						exportContent = exportContent + ", ";
					}
					else{
						exportContent = exportContent + reportSignalMaster.getPrice() + ", ";	
					}
				}
			}
			
			exportContent = exportContent + " \n";
		}
		return exportContent;		
	}
	//------------------------------------------------Setter&Getter--------------------------------------------------------
	public boolean isExportPerformanceDisable() {
		return exportPerformanceDisable;
	}
	public void setExportPerformanceDisable(boolean exportPerformanceDisable) {
		this.exportPerformanceDisable = exportPerformanceDisable;
	}
	public boolean isExportSignalDisable() {
		return exportSignalDisable;
	}
	public void setExportSignalDisable(boolean exportSignalDisable) {
		this.exportSignalDisable = exportSignalDisable;
	}
    public boolean isCppPriceColDisplay() {
		return cppPriceColDisplay;
	}
	public void setCppPriceColDisplay(boolean cppPriceColDisplay) {
		this.cppPriceColDisplay = cppPriceColDisplay;
	}
	public boolean isModeColDisplay() {
		return modeColDisplay;
	}
	public void setModeColDisplay(boolean modeColDisplay) {
		this.modeColDisplay = modeColDisplay;
	}
	public boolean isPriceColDisplay() {
		return priceColDisplay;
	}
	public void setPriceColDisplay(boolean priceColDisplay) {
		this.priceColDisplay = priceColDisplay;
	}
	public boolean isPendingColDisplay() {
		return pendingColDisplay;
	}
	public void setPendingColDisplay(boolean pendingColDisplay) {
		this.pendingColDisplay = pendingColDisplay;
	}
	public boolean isBidColDisplay() {
		return bidColDisplay;
	}
	public void setBidColDisplay(boolean bidColDisplay) {
		this.bidColDisplay = bidColDisplay;
	}
	public TreeContentProvider<ReportEventPerformance> getReportPerformanceTree() {
		return reportPerformanceTree;
	}
	public void setReportPerformanceTree(
			TreeContentProvider<ReportEventPerformance> reportPerformanceTree) {
		this.reportPerformanceTree = reportPerformanceTree;
	}	
	public TableContentProvider<ReportSignalMaster> getEventSignal() {
		return eventSignal;
	}
	public void setEventSignal(TableContentProvider<ReportSignalMaster> eventSignal) {
		this.eventSignal = eventSignal;
	}
	public TableContentProvider<ReportEvent> getEvents() {
		return events;
	}
	public boolean isExportDisable() {
		return exportDisable;
	}
	public void setExportDisable(boolean exportDisable) {
		this.exportDisable = exportDisable;
	}	
	public void setSelectedEventName(String selectedEventName) {
		this.selectedEventName = selectedEventName;
	}
	public String getSelectedEventName() {
		return selectedEventName;
	}
	public void setEventPerformanceActionFlag(boolean eventPerformanceActionFlag) {
		this.eventPerformanceActionFlag = eventPerformanceActionFlag;
	}
	public boolean isEventPerformanceActionFlag() {
		return eventPerformanceActionFlag;
	}
	public void setSelectedClientName(String selectedClientName) {
		this.selectedClientName = selectedClientName;
	}
	public String getSelectedClientName() {
		return selectedClientName;
	}
	public void setEventSignalActionFlag(boolean eventSignalActionFlag) {
		this.eventSignalActionFlag = eventSignalActionFlag;
	}
	public boolean isEventSignalActionFlag() {
		return eventSignalActionFlag;
	}
	public void setSelectedProgramName(String selectedProgramName) {
		this.selectedProgramName = selectedProgramName;
	}
	public String getSelectedProgramName() {
		return selectedProgramName;
	}
	public void setSignalDetailColumnNames(
			List<String> signalDetailColumnNames) {
		this.signalDetailColumnNames = signalDetailColumnNames;
	}
	public List<String> getSignalDetailColumnNames() {
		return signalDetailColumnNames;
	}
	public void setDisplaySignalDetail(boolean displaySignalDetail) {
		this.displaySignalDetail = displaySignalDetail;
	}
	public boolean isDisplaySignalDetail() {
		return displaySignalDetail;
	}


	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}


	public String getSelectedTab() {
		return selectedTab;
	}


	public void setReportSignalMasterContents(
			List<ReportSignalMaster> reportSignalMasterContents) {
		this.reportSignalMasterContents = reportSignalMasterContents;
	}


	public List<ReportSignalMaster> getReportSignalMasterContents() {
		return reportSignalMasterContents;
	}
	private void resetSignalColumnDisplayFlag(){
	    cppPriceColDisplay = false;
	    modeColDisplay = false;
	    priceColDisplay = false;
	    pendingColDisplay = false;
	    bidColDisplay = false;
	}
	private TreeMap putNumberSortObject(TreeMap treeMap,double a,Object obj){
		double keyNumber = generateKeyNumber(treeMap,a);
		treeMap.put(keyNumber, obj);
		return treeMap;
	}
	
	private double generateKeyNumber(Map treeMap,double a){
		boolean isContainFlag = treeMap.containsKey(a);
		double keyNumber=a;
		if(!isContainFlag){
			return keyNumber;
		}else{
			double randomNumber = Math.random()*0.0001;
			keyNumber=generateKeyNumber(treeMap,a+randomNumber);
		}
		return keyNumber;
	}

}
