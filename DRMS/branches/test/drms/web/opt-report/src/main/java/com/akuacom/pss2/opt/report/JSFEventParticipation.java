package com.akuacom.pss2.opt.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.jsf.model.TableContentProvider;
import com.akuacom.pss2.history.vo.ReportEventParticipation;
import com.akuacom.pss2.program.Program;

public class JSFEventParticipation extends ReportTab{
	
	/**
	 * Constructor
	 * @param report
	 */
	public JSFEventParticipation(OperatorReports report) {
		super(report,OperatorReports.Tab.participationTab,
				SearchCriteria.FILTER.participant,
				SearchCriteria.FILTER.event,
				SearchCriteria.FILTER.startDate,
			    SearchCriteria.FILTER.endDate,
				SearchCriteria.FILTER.program
			);
	}
	
	//------------------------------------------------Attributes-----------------------------------------------------------
	/** serial version*/
	private static final long serialVersionUID = -2982615526144560104L;
	
	/** logger*/
	private static final Logger log = Logger.getLogger(JSFEventParticipation.class.getName());
	
	/** program all flag*/
	private static final String PROGRAM_ALL="ALL";

	/** export disable flag*/
	boolean exportDisable=true;

	protected boolean toResetStatus;
	
	/** event participation table content provider*/
	private TableContentProvider<ReportEventParticipation> eventParticipations 
				= new AbstractTableContentProvider<ReportEventParticipation>() {

		private List<ReportEventParticipation> contents = Collections.emptyList();
		private int totalCount = -1; //-1 indicates no search executed 
		
		@Override
		public void updateModel(){
			try{
				if(!getReportTab().equals(getReport().getActiveTab()))
					return;
				
				if(!isValid()) return;
				
				SearchConstraint  searchConstraint = WebUtil.getSearchConstraint(this);
				boolean scchanged = isSearchCriteriaChange();
				if(scchanged){
					///sync search criteria
					setSearchCriteria(getReport().getWorkingSearchCriteria());
				}
				SearchCriteria sc = getSearchCriteria();
				
				if(isRangeChanged() || isSortColumnChanged() || scchanged){
					contents = getReportEventParticipation(searchConstraint,sc);
					if(scchanged)
						totalCount = getReportEventParticipationCount(searchConstraint,sc);						
					if(contents.size()>0){
						exportDisable = false;
					}else{
						exportDisable = true;
					}
				}
			}
			catch (Exception e) {
				FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
				log.error(ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL, e);
			}
		}
		
		@Override
		public List<ReportEventParticipation> getContents() {
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
	
	//------------------------------------------------Functions------------------------------------------------------------
	/**
	 * Function for invoke export data
	 */
	public void exportAction(){
        String filename = "EVENT_PARTICIPATION_REPORT.csv";       
        try {
        	FDUtils.export(filename, getExportContent());
        }catch(Exception e) {
        	FDUtils.addMsgError(ReportConstants.WARNING_MESSAGE_ERROR_EXPORT+ReportConstants.WARNING_MESSAGE_ERROR_INTERNAL);
        	log.error(ReportConstants.WARNING_MESSAGE_ERROR_EXPORT+e.getMessage());
        }
	}
	/**
	 * Private function for get export data contents
	 * @return Export data contents
	 * @throws Exception
	 */
	private String getExportContent() throws Exception{
		String exportContent = "";
		List<ReportEventParticipation> contents;	
		boolean scchanged = isSearchCriteriaChange();
		if(scchanged){
			///sync search criteria
			setSearchCriteria(getReport().getWorkingSearchCriteria());
		}
		SearchCriteria sc = getSearchCriteria();		
		SearchConstraint  constraint = WebUtil.getSearchConstraint(eventParticipations);
		constraint.setRowCount(-1);
		
		contents = getReportEventParticipation(constraint,sc);
		exportContent = exportContent
				+ "CLIENT, PROGRAM, EVENT, DATE, PARTICIPATION \n";
		if(contents!=null){
			for (ReportEventParticipation c : contents) {
				exportContent = exportContent + c.getClientName() + ", ";
				exportContent = exportContent + c.getProgramName() + ", ";
				exportContent = exportContent + c.getEventName() + ", ";
				if(c.getEventDate()!=null){
					exportContent = exportContent + c.getEventDate() + ", ";
				}else{
					exportContent = exportContent + ", ";
				}

//				exportContent = exportContent + c.getEventDate() + ", ";
				exportContent = exportContent + c.getStatus() + ", ";
				exportContent = exportContent + " \n";
			}
		}
		return exportContent;
	}
	/**
	 * Private function for retrieve event participation data 
	 * @param searchConstraint
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	private List<ReportEventParticipation> getReportEventParticipation(SearchConstraint  searchConstraint , SearchCriteria sc)throws Exception{
		List<ReportEventParticipation> contents = Collections.emptyList();
		List<String> selectPrograms=new ArrayList<String>();
		
		if (sc.getProgram().equals(PROGRAM_ALL)){
			List<Program> programs = getReport().getProgramOptions();
			for(Program program:programs){
				if((program.getProgramName()!=null)&&(!program.getProgramName().equalsIgnoreCase(""))){
					selectPrograms.add(program.getProgramName());
				}
			}
		}else{
			selectPrograms.add(sc.getProgram());
		}
		
		if(sc.getParticipants()!=null){
			List<String> participantNames= sc.getParticipantNames();
			contents = getReportManager().getEventParticipation(participantNames, selectPrograms,
										sc.getStartOfStartDate(), sc.getEndOfEndDate(), searchConstraint);
		}else if(sc.getEvent()!=null&&(!sc.getEvent().getEventName().equalsIgnoreCase(""))){
			contents = getReportManager().getEventParticipation(sc.getEvent().getEventName(), 
						selectPrograms, sc.getStartOfStartDate(), sc.getEndOfEndDate(), searchConstraint);
		}
		return contents;
	}
	/**
	 * Private function for retrieve event participation count
	 * @param searchConstraint
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	private int getReportEventParticipationCount(SearchConstraint  searchConstraint , SearchCriteria sc) throws Exception{
		int totalCount=0;
		List<String> selectPrograms=new ArrayList<String>();
		if (sc.getProgram().equals(PROGRAM_ALL)){
			List<Program> programs = getReport().getProgramOptions();
			for(Program program:programs){
				if((program.getProgramName()!=null)&&(!program.getProgramName().equalsIgnoreCase(""))){
					selectPrograms.add(program.getProgramName());
				}
			}
		}else{
			selectPrograms.add(sc.getProgram());
		}

		if(sc.getParticipants()!=null){
			List<String> participantNames= sc.getParticipantNames();
			totalCount = getReportManager().getEventParticipationCount(participantNames, 
						selectPrograms,sc.getStartOfStartDate(), sc.getEndOfEndDate());
		}else if(sc.getEvent()!=null&&(!sc.getEvent().getEventName().equalsIgnoreCase(""))){
			totalCount = getReportManager().getEventParticipationCount(sc.getEvent().getEventName(), 
					selectPrograms,sc.getStartOfStartDate(), sc.getEndOfEndDate());
		}
		return totalCount;
	}
	
	
	//------------------------------------------------Setter&Getter--------------------------------------------------------
	
	public TableContentProvider<ReportEventParticipation> getEventParticipations() {
		return eventParticipations;
	}


	public void setEventParticipations(
			TableContentProvider<ReportEventParticipation> eventParticipations) {
		this.eventParticipations = eventParticipations;
	}
	
	public boolean isExportDisable() {
		return exportDisable;
	}


	public void setExportDisable(boolean exportDisable) {
		this.exportDisable = exportDisable;
	}
	@Override
	public void resetStatus() {
		toResetStatus= true;		
	}
}

	

