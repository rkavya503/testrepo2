package com.akuacom.pss2.richsite.participant;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.akuacom.common.Condition;
import com.akuacom.common.Condition.OPERATOR;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jdbc.SearchConstraint.ORDER;
import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.query.ParticipantSearchCriteria;
import com.akuacom.pss2.query.ParticipantSummary;
import com.akuacom.pss2.richsite.FDUtils;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.ParticipantViewLayout;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.PSS2Util;
import com.akuacom.pss2.web.WebUtils;
import com.akuacom.utils.lang.DateUtil;

public class ParticipantDataProvider extends
		AbstractTableContentProvider<JSFParticipant> implements ParticipantViewLayout {
	
	SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
    private String dateFormat;

	private static final long serialVersionUID = 3672418744321856056L;
	
	private int ALL_ROWS = Integer.MAX_VALUE; //big enough to fetch all rows

	private static final Logger log = Logger.getLogger(ParticipantDataProvider.class
			.getName());

	private SearchHistory searchHistory;

	private boolean searchInResult;

	private NativeQueryManager nativeQuery;
	 
	private List<JSFParticipant> participants =Collections.emptyList();
	
	private int totalCount = 0;
	private FilterCondition condition;
	
	//resetTable is true indicates that table status will be reset, 
	//page No. will be set to first page,etc
	private boolean resetTable = false;
	
	private static enum FilterOption {
		ParticipantName("participantName", "Name"), Account("Account",
				"Account#"), Program("Program", "Program");
		
		private String param;
		private String label;

		FilterOption(String name, String label) {
			this.param = name;
			this.label = label;
		}
		
		public String toString() {
			return this.label;
		}
	}

	public ParticipantDataProvider() {
		searchHistory = new SearchHistory();
		buildViewLayout();
	}
	
	@Override
	public void updateModel() {
		
		String loginFlag = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("loginFlag");
		
		
		if(loginFlag!=null&&loginFlag.equalsIgnoreCase("true")){
			searchHistory = new SearchHistory();
			ClientDataProvider clientDataProvider = (ClientDataProvider) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientDataModel");
			if(clientDataProvider!=null){
				clientDataProvider.getSearchHistory().clear();
			}
		}
		String msg = this.validate();
		if(msg!=null){
			FDUtils.addMsgError(msg);
			//do nothing
			return ;
		}
		SearchConstraint sc=getSearchConstraint();
		try{
			QueryResult result = executeQuery(sc);
			this.totalCount =result.totalCount;
			this.participants =convertPartListToJSFPartList(result.participants);
		}catch(CriteriaContradictionException e){
			this.totalCount = 0;
			this.participants = Collections.emptyList();
		}catch(Exception e){
			log.error(e.getMessage(),e);
			FDUtils.addMsgError("internal error");
		}
	}
	
	public QueryResult executeQuery(SearchConstraint sc) throws Exception{
		ParticipantSearchCriteria psc = this.getParticipantSearchCritera();
		QueryResult result = new QueryResult();
		 if(sc.getRowCount()!=ALL_ROWS){
			 result.totalCount =getQueryManager().getParticipantSummaryCount(psc);
		 }
		 if(result.totalCount>0 || sc.getRowCount()==ALL_ROWS){	
			 result.participants =getQueryManager().getParticipantSummary(psc,sc);
			 if(isEnableCBPConsolidation()){
				 for(ParticipantSummary pp:result.participants){
					 pp.setCbpConsolidation(true);
				 }
			}
		 }
		return result;
	}
	
	public ParticipantSearchCriteria getParticipantSearchCritera() throws CriteriaContradictionException{
		//if request is initialized by link in program's tab
		//a new search should be triggered
		String program =getFilterByExactProgram();
		if(program!=null) {
			condition = new FilterCondition(FilterOption.Program.name(),program,OPERATOR.EQUAL);
			resetTable = true;
			searchHistory.clear();
			getSearchHistory().appendToHistory(condition);
		}
		
		//DRMS-7899
		ParticipantSearchCriteria psc = new ParticipantSearchCriteria();
		
		List<String> participantNameList = searchHistory
		.getCandiateParameterValues(FilterOption.ParticipantName.name(), OPERATOR.START_WITH);
		List<String> accountList = searchHistory
		.getCandiateParameterValues(FilterOption.Account.name(), OPERATOR.START_WITH);
		
		if(!(participantNameList!=null&&participantNameList.size()>0)){
			String	name     =    searchHistory.getStrParamValue(FilterOption.ParticipantName.name(),OPERATOR.START_WITH);
			psc.setNameLeadingStr(name);
		}else{
			psc.setNameList(participantNameList);
		}
		if(!(accountList!=null&&accountList.size()>0)){
			String  account     =    searchHistory.getStrParamValue(FilterOption.Account.name(),OPERATOR.START_WITH);
			psc.setAccountLeadingStr(account);
		}else{
			psc.setAccountList(accountList);
		}
		
		
		List<String> exactPrograms = searchHistory
					.getCandiateParameterValues(FilterOption.Program.name(), OPERATOR.EQUAL);
		List<String> programleadingStr = searchHistory
					.getCandiateParameterValues(FilterOption.Program.name(), OPERATOR.START_WITH);
		

		psc.setProgramLeadingStr(programleadingStr);
		psc.setExactProgramNames(exactPrograms);
		
		return psc;
	}
	
	public void historySearchAction() {
		resetTable = true;
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		int index = Integer.parseInt(params.get("linkSequence"));
		searchHistory.setMaxWorkingIndex(index);
	}
	
	@Override
	public int getTotalRowCount() {
		return totalCount;
	}

	@Override
	public void resetSortingOrpagination() {
		 if(resetTable){
			 getSequenceRange().setFirstRow(0);
			 resetTable =false;
		 }
	}
	
	protected String validate(){
		// When get participants by click search button
		FilterCondition condition = this.getSearchCondition();
		FilterOption option = Enum.valueOf(FilterOption.class, condition.getParam());
		//DRMS-7899
		String conditionValue ="";
		try{
			conditionValue = (String)condition.getValue();
			if(conditionValue.startsWith("*")){
				conditionValue=conditionValue.replaceFirst("\\*","");
			}
			if(conditionValue.endsWith("*")){
				conditionValue=conditionValue.substring(0, conditionValue.length()-1);
			}	
		}catch(Exception e){
			
		}
		switch(option){
			case ParticipantName:
				if(!PSS2Util.isLegalParticipantName(conditionValue))
					return "Illegal Participant Name";
				return null;
			case Account:
				if(!PSS2Util.isLegalAccountNo(conditionValue))
					return "Illegal Participant Name";
				return null;
			case Program:
				if(!PSS2Util.isLegalProgramName(conditionValue))
					return "Illegal Program name";
				return null;
			default:
				return null;
		}
	}
	
	public void filterAction() {
		FilterCondition condition = getSearchCondition();
		if(this.validate()==null){
			resetTable = true;
			searchHistory.resetMaxWorkingIndex();
			//TODO validation
			if(this.isSearchInResult()){
				//append to history
				searchHistory.appendToHistory(condition);
			}else{
				//replace it
				searchHistory.clear();
				searchHistory.appendToHistory(condition);
			}
		}
	}
	
	public void participantDeleteAction() {
		List<JSFParticipant> temp = new ArrayList<JSFParticipant>();
		if (this.participants != null){
            for (JSFParticipant p: participants){
                if (p.isSelected()) temp.add(p);
            }
		}
	   if(!temp.isEmpty()){
		    ParticipantManager pm = (ParticipantManager) EJBFactory.getBean(ParticipantManager.class);
		    for (JSFParticipant p: temp){
	               pm.removeParticipant(p.getName());
	        }
	   }
	}

    public void participantCancelAction() {
    	
    }
    
	public void clearAction() {
		resetTable = true;
		searchHistory.clear();
		this.searchInResult =false;
		condition = FilterCondition.DefaultFilter();
		searchHistory.resetMaxWorkingIndex();
	}
	
	public void searchAllAction() {
		resetTable = true;
		searchHistory.searchAll();
	}
	
	@Override
	public List<? extends JSFParticipant> getContents() {
		if(this.participants==null)
			return Collections.emptyList();
		else
			return participants;
	}
	
	protected FilterOption getFilterOption(){
		return Enum.valueOf(FilterOption.class,getSearchCondition().getParam());
	}
	
	protected NativeQueryManager getQueryManager(){
		if(this.nativeQuery==null){
			nativeQuery = EJBFactory.getBean(NativeQueryManager.class);
		}
		return nativeQuery;
	}
	
	protected String getFilterByExactProgram(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		String program = request.getParameter("program");
		return program;
	}
	
	public FilterCondition getSearchCondition() {
		if (condition == null) {
			condition =  FilterCondition.DefaultFilter();
		}
		condition.setOpterator(OPERATOR.START_WITH);
		
		String program =getFilterByExactProgram();
		if(program!=null) {
			condition = new FilterCondition(FilterOption.Program.name(),program,OPERATOR.EQUAL);
		}
		return condition;
	}
	
	public void filterTypeChanged(ValueChangeEvent event) {
		//FilterCondition condition = getSearchCondition();
		//FilterOption option = Enum.valueOf(FilterOption.class,(String)event.getNewValue());
		//condition.setOpterator(OPERATOR.START_WITH);
	}
	
	public boolean isSearchInResult() {
		return searchInResult;
	}
	
	public void setSearchInResult(boolean searchInResult) {
		this.searchInResult = searchInResult;
	}

	public SearchHistory getSearchHistory() {
		if (this.searchHistory == null)
			searchHistory = new SearchHistory();
		return searchHistory;
	}
	
	public String getSelectedParticipant() {
		 String str ="";
		 for (JSFParticipant p: participants){
			 if(p.isSelected())
					 str+=p.getName()+",";
         }
		 if(str.length()>1) str=str.substring(0,str.length()-1);
		 return str;
	}
	
	public List<SelectItem> getFilterOptions() {
		return WebUtils.enumToSelectItemsByNameLabelPair(FilterOption.class);
	}
	
	public List<SelectItem> getEventStatusOptions() {
		return WebUtils.enumToSelectItems(EventStatus.class);
	}

	public static class FilterCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		public static FilterCondition  DefaultFilter(){
			return new FilterCondition(FilterOption.ParticipantName.name(),"",OPERATOR.START_WITH);
		}
		
		public FilterCondition(String name, String value, OPERATOR opt) {
			super(name, value, opt);
		}
		
		public String getLabel() {
			switch(getOpterator()){
				case EQUAL:
					return this.getParam() + " = " + this.getValue();
				case START_WITH:
					//DRMS-7899
					String value = (String)this.getValue();
					String result ="";
					if(value.startsWith("*")){
						 result = this.getParam() +" : " +this.getValue();
					}else{
						 result = this.getParam() +" : " +this.getValue()+"*";	
					}
					if(result.endsWith("**")){
						result=result.substring(0, result.length()-1);
					}
					return result;
				case END_WITH:
					return this.getParam()+" : " +"*"+this.getValue();
				default:
					//TODO
					return this.getParam()+" : "+this.getValue();
			}
		}
	}
	
	private List<JSFParticipant> convertPartListToJSFPartList(List<ParticipantSummary> summary){
		List<JSFParticipant> jsfParticipants = new ArrayList<JSFParticipant>();
		for (ParticipantSummary pp : summary) {
			JSFParticipant item = new JSFParticipant();
			item.setName(pp.getName());
			item.setAccountNumber(pp.getAccountNumber());
			item.setUUID(pp.getUUID());
			item.setBcdRepName(pp.getBcdRepName());
			item.setAutoDrProfileStartDate(pp.getAutoDrProfileStartDate());
			item.setInstaller(pp.isInstaller());
			item.setUserType(pp.getUserType().toString());
			item.setHasNotes((pp.getNotes() != null)
					&& (!pp.getNotes().isEmpty()));
			item.setRequestId(pp.getRequestId());
			
			if(isEnableCBPConsolidation()){
				pp.setCbpConsolidation(true);
			}
			
			item.setAggPartCount(pp.getAggPartCount());
			
			Collections.sort(pp.getProgramParticipation());
			item.setProgramsParticipation(pp.getProgramParticipation());
			item.setDisabled(pp.getActiveEvents().size()>0);			
			item.setType(pp.getTypeStr());
			item.setNotes(pp.getNotes());
			
			jsfParticipants.add(item);
			
		}
		return jsfParticipants;
	}
	
	//exports
	public void exportHtmlTableToExcel() throws IOException {
		try{
			 String filename = "participantList.csv";
		     FacesContext fc = FacesContext.getCurrentInstance();
		     HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
		     response.reset();
		     response.addHeader("cache-control", "must-revalidate");
		     response.setContentType("application/octet_stream");
		     response.setCharacterEncoding("UTF-8");
		     response.setHeader("Content-Disposition", "attachment; filename=\""
		                    + filename + "\"");
		     
		     String table = getExportContents();
		     //table = new String(table.getBytes(),"utf-8");
		     response.getWriter().print(table);
		     fc.responseComplete();
		}catch(Exception e){
			FDUtils.addMsgInfo("Internal Error");
			log.error(e.getMessage(),e);
		}
	}
	
	public String getExportContents(){
		boolean showparticipantInfo = isParticipantInfoEnable();
		StringBuffer buffer = null;
		if(showparticipantInfo){
			buffer = new StringBuffer("Name,Account#,Customer Name,BCD Rep Name,Street Address,City,Abank,Start Date,Deactivate Date,Enrollment Date,AutoDR Profile date," +
					"Programs,Installer,Aggregator,Enable Client Data,Participant Type,Participant Notes");
		}else{
			 buffer = new StringBuffer("Name,Account#,Customer Name,Street Address,City,Abank,Start Date,Deactivate Date,Enrollment Date," +
						"Programs,Installer,Aggregator,Enable Client Data,Participant Type,Participant Notes");
		}
		
		List<ParticipantSummary> summary=getExportParticipantSummary();
		for (ParticipantSummary part : summary) {
			
			buffer.append("\n");
			writeNext(buffer,part.getName());
			writeNext(buffer,part.getAccountNumber());
			writeNext(buffer,part.getCustomerName());
			if(showparticipantInfo){
				writeNext(buffer,part.getBcdRepName());
			}
			
			writeNext(buffer,part.getAddress());
			writeNext(buffer,part.getServiceCityName());
			writeNext(buffer,part.getAbank());
			if(part.getStartDate()!=null){
				writeNext(buffer,DateUtil.format(part.getStartDate(), getDateFormat()));
			}else{
				buffer.append(",");
			}
			
			if(part.getEndDate()!=null){
				writeNext(buffer,DateUtil.format(part.getEndDate(), getDateFormat()));
			}else{
				buffer.append(",");
			}
			if(part.getEnrollmentDate()!=null){
				writeNext(buffer,DateUtil.format(part.getEnrollmentDate(), getDateFormat()));
			}else{
				buffer.append(",");
			}
			if(showparticipantInfo){
				if(part.getAutoDrProfileStartDate()!=null){
					writeNext(buffer,DateUtil.format(part.getAutoDrProfileStartDate(), getDateFormat()));
				}else{
					buffer.append(",");
				}
			}
			if(isEnableCBPConsolidation()){
				writeNext(buffer,part.getAllProgramNamesWithConsolidation());
			}else{
				writeNext(buffer,part.getAllProgramNames());	
			}
			writeNext(buffer,String.valueOf(part.isInstaller()));
			writeNext(buffer,String.valueOf(part.isAggregator()));
			writeNext(buffer,String.valueOf(part.isDataEnabler()));
			writeNext(buffer,part.getParticipantType());
	
			String notes = part.getNotes();
			if(notes!=null){
				
				notes = StringEscapeUtils.unescapeHtml(notes);
				//remove all html tag
				notes=notes.replaceAll("<\\/?\\w+>", "");
				notes=notes.replaceAll("[\\n\\r\\t]", "");
				//in case there is comma in the notes
				//comma is used as column separator
				notes=notes.replaceAll(",",";");
				notes=notes.replaceAll("\\s{2,}", "");
				
				notes = notes.trim();
				if(notes.length()>100){
					notes = notes.substring(0, 100);	
				}
				
			}else{
				notes ="";
			}
			
			buffer.append(notes);
		}
		return buffer.toString();
	}
	
	protected SearchConstraint getSearchConstraint(){
		SearchConstraint sc = WebUtils.getSearchConstraint(this);
		if(sc.getOrderColumns().size()==0)
			sc.addSortColumn("participantName",ORDER.ASC);
		return sc;
	}
	
	protected  List<ParticipantSummary> getExportParticipantSummary(){
		SearchConstraint sc=  getSearchConstraint();
		sc.setStartRow(0);
		sc.setRowCount(ALL_ROWS); //big enough
		QueryResult result;
		try {
			result = this.executeQuery(sc);
			return result.participants;
		} catch (Exception e) {
			log.error(e);
			FDUtils.addMsgError("Internal Error");
		}
		return Collections.emptyList();
	}
	
	public static class QueryResult implements Serializable{
		private static final long serialVersionUID = -5053307918175993425L;
		public int totalCount =0;
		public List<ParticipantSummary> participants =Collections.emptyList();
		
	}

	public String getDateFormat() {
		if(dateFormat==null){
			dateFormat=systemManager.getPss2Features().getDateFormat();
		}
		
		return dateFormat;
	}
	
	private static final char QUOTECHAR ='"';
	private static final char ESCAPECHAR ='"';
	private static final char SEPARATOR =',';
	private static final String NULLCHAR ="null";
	private static final int INITIAL_STRING_SIZE = 128;
	
    private StringBuffer writeNext(StringBuffer sb, String nextElement) {
    	if(nextElement==null||NULLCHAR.equalsIgnoreCase(nextElement.trim())){
    		sb.append(QUOTECHAR);
    		sb.append(QUOTECHAR);
    	    sb.append(SEPARATOR);
    	    
    	    return sb;
    	}
    	sb.append(QUOTECHAR);
        sb.append(stringContainsSpecialCharacters(nextElement) ? processLine(nextElement) : nextElement);
        sb.append(QUOTECHAR);
        sb.append(SEPARATOR);
        
        return sb;
    }

	private boolean stringContainsSpecialCharacters(String line) {
	    return line.indexOf(QUOTECHAR) != -1;
	}
	
	protected StringBuilder processLine(String nextElement)
	{
		StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
	    for (int j = 0; j < nextElement.length(); j++) {
	        char nextChar = nextElement.charAt(j);
	        if (nextChar == QUOTECHAR) {
	        	sb.append(ESCAPECHAR).append(nextChar);
	        } else {
	            sb.append(nextChar);
	        }
	    }
	    
	    return sb;
	}
	
	protected SystemManager getSystemManager(){
		if(this.systemManager==null){
			systemManager = EJBFactory.getBean(SystemManager.class);
		}
		return systemManager;
	}
	public boolean isParticipantInfoEnable(){
		return getSystemManager().getPss2Features().isParticipantInfoEnabled();
	}
    private static ProgramManager pm;

	public static ProgramManager getPm() {
		if(pm==null){
			pm = EJB3Factory.getBean(ProgramManager.class);
		}
		return pm;
	}
	public boolean isEnableCBPConsolidation(){
		Boolean result = CBPUtil.isEnableCBPConsolidation();
		if(result==null){
			result = CBPUtil.isEnableCBPConsolidation(getPm().getAllPrograms());
		}
		return result;
	}
	
	private boolean canCreateParticipant;
	private boolean canDeleteParticipant;
	private boolean canSearchPaticipants;
	private boolean canExportParticipants;
	private boolean canClearParticipants;
	private boolean canDeleteChecked;
	private boolean aggregationEnabled;
	private boolean editParticipant;
	private boolean viewParticipant;
	private boolean canUserAddClient;
	private boolean sce;
	
	public boolean isCanCreateParticipant() {
		return canCreateParticipant;
	}
	
	public boolean isSce()
	{
		String utilityName = systemManager.getPss2Properties().getUtilityName();
		if((utilityName).equalsIgnoreCase("sce"))
			sce=true;
		else
			sce=false;
		return sce;
	}
	

	public void setCanCreateParticipant(boolean canCreateParticipant) {
		this.canCreateParticipant = canCreateParticipant;
	}

	public boolean isCanDeleteParticipant() {
		return canDeleteParticipant;
	}

	public void setCanDeleteParticipant(boolean canDeleteParticipant) {
		this.canDeleteParticipant = canDeleteParticipant;
	}
	
	public boolean isCanSearchPaticipants() {
		return canSearchPaticipants;
	}

	public void setCanSearchPaticipants(boolean canSearchPaticipants) {
		this.canSearchPaticipants = canSearchPaticipants;
	}

	public boolean isCanExportParticipants() {
		return canExportParticipants;
	}

	public void setCanExportParticipants(boolean canExportParticipants) {
		this.canExportParticipants = canExportParticipants;
	}

	public boolean isCanClearParticipants() {
		return canClearParticipants;
	}

	public void setCanClearParticipants(boolean canClearParticipants) {
		this.canClearParticipants = canClearParticipants;
	}

	public boolean isCanDeleteChecked() {
		return canDeleteChecked;
	}

	public void setCanDeleteChecked(boolean canDeleteChecked) {
		this.canDeleteChecked = canDeleteChecked;
	}

	public boolean isAggregationEnabled() {
		return aggregationEnabled;
	}

	public void setAggregationEnabled(boolean aggregationEnabled) {
		this.aggregationEnabled = aggregationEnabled;
	}	

	public boolean isEditParticipant() {
		return editParticipant;
	}

	public void setEditParticipant(boolean editParticipant) {
		this.editParticipant = editParticipant;
	}

	public boolean isViewParticipant() {
		return viewParticipant;
	}

	public void setViewParticipant(boolean viewParticipant) {
		this.viewParticipant = viewParticipant;
	}	

	public boolean isCanUserAddClient() {
		return canUserAddClient;
	}

	public void setCanUserAddClient(boolean canUserAddClient) {
		this.canUserAddClient = canUserAddClient;
	}

	private void buildViewLayout(){
        try {
        	getViewBuilderManager().buildParticipantViewLayout(this);
        } catch (NamingException e) {                // log exception

        }
	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{
       return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}	
}
