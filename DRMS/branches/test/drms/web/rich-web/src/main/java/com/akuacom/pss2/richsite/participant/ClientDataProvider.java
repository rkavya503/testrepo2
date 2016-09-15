package com.akuacom.pss2.richsite.participant;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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

import org.apache.log4j.Logger;

import com.akuacom.common.Condition;
import com.akuacom.common.Condition.FilterParam;
import com.akuacom.common.Condition.OPERATOR;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jdbc.SearchConstraint.ORDER;
import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.query.ClientSearchCriteria;
import com.akuacom.pss2.query.ClientSummary;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.query.PartContact;
import com.akuacom.pss2.richsite.FDUtils;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.ClientViewLayout;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.PSS2Util;
import com.akuacom.pss2.web.WebUtils;

public class ClientDataProvider extends
		AbstractTableContentProvider<JSFClient> implements ClientViewLayout {

	private static final long serialVersionUID = 3672418744321856056L;
	
	private int ALL_ROWS = Integer.MAX_VALUE; //big enough to fetch all rows

	private static final Logger log = Logger.getLogger(ClientDataProvider.class
			.getName());

	private SearchHistory searchHistory;

	private boolean searchInResult;

	private NativeQueryManager nativeQuery;
	 
	private List<JSFClient> clients =Collections.emptyList();
	
	private int totalCount = 0;
	private FilterCondition condition;
	
	
	//resetTable is true indicates that table status will be reset, 
	//page No. will be set to first page,etc
	private boolean resetTable = false;
	
	private static final int TYPE_AUTO = 0;
	private static final int TYPE_MANUAL = 2;

	private static final int COMMS_STATUS_ONLINE = 0;
	private static final int COMMS_STATUS_OFFLINE = 2;
	

	private static enum FilterOption {
		ClientName("clientName", "Client Name"), Participant("Participant",
				"Participant"), Program("Program", "Program"), ClientType(
				"ClientType", "Client Type"), CommStatus("Comm Status",
				"Comm Status"), EventStatus("EventStatus", "Event Status");

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

	private static enum CommsStatus implements FilterParam {
		ONLINE(COMMS_STATUS_ONLINE), OFFLINE(COMMS_STATUS_OFFLINE);
		int intType;
		CommsStatus(int intType){
			this.intType =intType;
		}
		@Override
		public Object getValue() {
			return intType;
		}
	}

	private static enum ClientType implements FilterParam{
		AUTO(TYPE_AUTO), MANUAL(TYPE_MANUAL);
		int intType;
		ClientType(int integerType){
			this.intType = integerType;
		}
		@Override
		public Object getValue() {
			return intType;
		}
	}

	public ClientDataProvider() {
		searchHistory = new SearchHistory();
		buildViewLayout();
	}
	
	@Override
	public void updateModel() {
		String msg = this.validate();
		if(msg!=null){
			FDUtils.addMsgError(msg);
			//do nothing
			return ;
		}
		SearchConstraint sc=getSearchConstraint();
		try{
			QueryResult result = executeQuery(sc,false);
			this.totalCount =result.totalCount;
			this.clients =convertPartListToJSFPartList(result.clients);
		}catch(CriteriaContradictionException e){
			this.totalCount = 0;
			this.clients = Collections.emptyList();
		}catch(Exception e){
			log.error("Interal Error", e);
			FDUtils.addMsgError("internal error");
		}
	}
	
	public QueryResult executeQuery(SearchConstraint sc,boolean withContacts) throws Exception{
		ClientSearchCriteria csc = this.getClientSearchCritera();
		if(withContacts)
			csc.setFetchContacts(true);
		
		QueryResult result = new QueryResult();
		 if(sc.getRowCount()!=ALL_ROWS){
			 result.totalCount =getQueryManager().getClientSummaryCount(csc);
		 }
		 if(result.totalCount>0 || sc.getRowCount()==ALL_ROWS){	
			 result.clients =getQueryManager().getClientSummary(csc,sc);
		 }
		return result;
	}
	
	public ClientSearchCriteria getClientSearchCritera() throws CriteriaContradictionException{
		
		//if request is initialized by link in program's tab
		//a new search should be triggered
		String program =getFilterByExactProgram();
		if(program!=null) {
			condition = new FilterCondition(FilterOption.Program.name(),program,OPERATOR.EQUAL);
			resetTable = true;
			searchHistory.clear();
			getSearchHistory().appendToHistory(condition);
		}
		
		ClientSearchCriteria csc = new ClientSearchCriteria();
		List<String> participantNameList = searchHistory
		.getCandiateParameterValues(FilterOption.Participant.name(), OPERATOR.START_WITH);
		List<String> clientNameList = searchHistory
		.getCandiateParameterValues(FilterOption.ClientName.name(), OPERATOR.START_WITH);
		
		if(!(participantNameList!=null&&participantNameList.size()>0)){
			String	name     =    searchHistory.getStrParamValue(FilterOption.Participant.name(),OPERATOR.START_WITH);
			csc.setParenLeadingStr(name);
		}else{
			csc.setParticipantNameList(participantNameList);
		}
		if(!(clientNameList!=null&&clientNameList.size()>0)){
			String	name     =    searchHistory.getStrParamValue(FilterOption.ClientName.name(),OPERATOR.START_WITH);
			csc.setClientNameLeadingStr(name);
		}else{
			csc.setClientNameList(clientNameList);
		}
		
		//String	parent     =    searchHistory.getStrParamValue(FilterOption.Participant.name(),OPERATOR.START_WITH);
		//String  client     =    searchHistory.getStrParamValue(FilterOption.ClientName.name(),OPERATOR.START_WITH);
		Integer type       =    searchHistory.getIntegerParamValue(FilterOption.ClientType.name(),OPERATOR.EQUAL);
		Integer sts        =    searchHistory.getIntegerParamValue(FilterOption.CommStatus.name(),OPERATOR.EQUAL);
		Object  evtStatus  =    searchHistory.getParamValue(FilterOption.EventStatus.name(),OPERATOR.EQUAL);
		
		List<String> exactPrograms = searchHistory
					.getCandiateParameterValues(FilterOption.Program.name(), OPERATOR.EQUAL);
		List<String> programleadingStr = searchHistory
					.getCandiateParameterValues(FilterOption.Program.name(),OPERATOR.START_WITH);
		
		
		
		csc.setClientType(type);
		csc.setCommsStatus(sts);
		csc.setEventStatus((EventStatus) evtStatus);
		csc.setProgramLeadingStr(programleadingStr);
		csc.setExactProgramName(exactPrograms);
		return csc;
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
			case Participant:
				if(!PSS2Util.isLegalParticipantName(conditionValue))
					return "Illegal Participant Name";
				return null;
			case ClientName:
				if(!PSS2Util.isLegalClientName((conditionValue)))
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
		List<JSFClient> temp = new ArrayList<JSFClient>();
		if (this.clients != null){
            for (JSFClient c: clients){
                if (c.isSelected()) temp.add(c);
            }
		}
	   if(!temp.isEmpty()){
		    ClientManager cm = (ClientManager) EJBFactory.getBean(ClientManager.class);
		    for (JSFClient p: temp){
	               cm.removeClient(p.getName());
	        }
	   }
	}

    public void participantCancelAction() {
    	
    }
    
	public void clearAction() {
		resetTable = true;
		condition = FilterCondition.DefaultFilter();
		this.searchInResult =false;
		searchHistory.clear();
		searchHistory.resetMaxWorkingIndex();
	}

	public void searchAllAction() {
		resetTable = true;
		searchHistory.searchAll();
	}
	
	@Override
	public List<? extends JSFClient> getContents() {
		if(this.clients==null)
			return Collections.emptyList();
		else
			return clients;
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
		FilterOption option = Enum.valueOf(FilterOption.class,condition.getParam());
		switch (option) {
			case ClientName:
			case Participant:
			case Program:
				condition.setOpterator(OPERATOR.START_WITH);
				break;
			case ClientType:
				condition.setOpterator(OPERATOR.EQUAL);
				break;
			case CommStatus:
				condition.setOpterator(OPERATOR.EQUAL);
				break;
			case EventStatus:
				condition.setOpterator(OPERATOR.EQUAL);
				break;
		}
		
		String program =getFilterByExactProgram();
		if(program!=null) {
			condition = new FilterCondition(FilterOption.Program.name(),program,OPERATOR.EQUAL);
		}
		return condition;
	}
	
	private FilterOption lastOption = FilterOption.ClientName;
	
	public void filterTypeChanged(ValueChangeEvent event) {
		FilterCondition condition = getSearchCondition();
		FilterOption option = Enum.valueOf(FilterOption.class,(String)event.getNewValue());
		switch (option) {
			case ClientName:
			case Participant:
			case Program:
				if(lastOption == FilterOption.ClientName||lastOption == FilterOption.Participant||lastOption == FilterOption.Program){
					
				}else{
					condition.setValue("");	
				}
				condition.setOpterator(OPERATOR.START_WITH);
				lastOption = option;
				break;
			case ClientType:
				condition.setValue(ClientType.AUTO);
				condition.setOpterator(OPERATOR.EQUAL);
				lastOption = option;
				break;
			case CommStatus:
				condition.setValue(CommsStatus.ONLINE);
				condition.setOpterator(OPERATOR.EQUAL);
				lastOption = option;
				break;
			case EventStatus:
				condition.setValue(EventStatus.ACTIVE);
				condition.setOpterator(OPERATOR.EQUAL);
				lastOption = option;
				break;
		}
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
		 for (JSFClient c: clients){
			 if(c.isSelected())
					 str+=c.getName()+",";
         }
		 if(str.length()>1) str=str.substring(0,str.length()-1);
		 return str;
	}

	public boolean isFilterByClientType() {
		return FilterOption.ClientType == getFilterOption();
	}

	public boolean isFiltedByEventStatus() {
		return FilterOption.EventStatus ==  getFilterOption();
	}

	public boolean isFiltedByCommsStatus() {
		return FilterOption.CommStatus ==  getFilterOption();
	}

	public boolean isFiltedByTextInput() {
		return isTextInput( getFilterOption());
	}
	
	private static boolean isTextInput(FilterOption option){
		return FilterOption.ClientName  == option
			|| FilterOption.Participant == option
			|| FilterOption.Program     == option;
	}
	
	public List<SelectItem> getFilterOptions() {
		return WebUtils.enumToSelectItemsByNameLabelPair(FilterOption.class);
	}

	public List<SelectItem> getClientTypes() {
		return WebUtils.enumToSelectItems(ClientType.class);
	}
	
	public List<SelectItem> getCommsStatusOptions() {
		return WebUtils.enumToSelectItems(CommsStatus.class);
	}
	
	public List<SelectItem> getEventStatusOptions() {
		return WebUtils.enumToSelectItems(EventStatus.class);
	}

	public static class FilterCondition extends Condition {
		private static final long serialVersionUID = 1L;
		
		public static FilterCondition  DefaultFilter(){
			return new FilterCondition(FilterOption.ClientName.name(),"",OPERATOR.START_WITH);
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
		
		public ClientType getClientType() {
			return (ClientType) this.getValue();
		}

		public void setClientType(ClientType clientType) {
			this.setValue(clientType);
		}
		
		public CommsStatus getCommsStatus() {
			return (CommsStatus) this.getValue();
		}

		public void setCommsStatus(CommsStatus commsStatus) {
			this.setValue(commsStatus);
		}
		
		public EventStatus getEventStatus() {
			return (EventStatus) this.getValue();
		}
		
		public void setEventStatus(EventStatus evtStatus) {
			this.setValue(evtStatus);
		}
	}
	
	private List<JSFClient> convertPartListToJSFPartList(List<ClientSummary> summary){
		List<JSFClient> jsfParticipants = new ArrayList<JSFClient>();
		for (ClientSummary part : summary) {
			JSFClient item = new JSFClient();
			item.setName(part.getName());
			item.setType(part.getTypeString());
			item.setStatus(part.getCommsStatus().toString());
			item.setParticipantName(part.getParent());
			item.setManualControl(part.isManualControl());
			
			Collections.sort(part.getPrograms());
			item.setProgramsParticipation(part.getPrograms());
			//DRMS-7895
			String currentEventStatus = part.getCurrentStatus().name();
			if(("OPT_OUT").equalsIgnoreCase(currentEventStatus)){
				currentEventStatus="OPT OUT";
			}
            item.setCurrentEventStatus(currentEventStatus);
            item.setLastEventStatus(part.getLastStatus().name());
            
            item.setLastMode(part.getLastMode().name());
            item.setCurrentMode(part.getCurrentMode().name());
            
			// last contact
			item.setLastContact(part.getLastContact());
			item.setDisabled(part.getEvents().size()>0); 
            item.setDeviceType(part.getDeviceType());
            
            //email address , for clients export
            item.setEmailAddress(part.getEmailAddress());
        
            item.setABank(part.getABank());
            item.setLeadAccount(part.isLeadAccount());
            item.setParticipantType(part.getParticipantType());
		
            jsfParticipants.add(item);
		}
		return jsfParticipants;
	}
	
	//exports
	public void exportHtmlTableToExcel() throws IOException {
		 String filename = "clientList.csv";
	     FacesContext fc = FacesContext.getCurrentInstance();
	     HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
	     response.reset();
	     response.addHeader("cache-control", "must-revalidate");
	     response.setContentType("application/octet_stream");
	     response.setHeader("Content-Disposition", "attachment; filename=\""
	                    + filename + "\"");
	     
	     String table = getClientsExportContent() ;
	     response.getWriter().print(table);
	     fc.responseComplete();
	}
	
	protected SearchConstraint getSearchConstraint(){
		SearchConstraint sc = WebUtils.getSearchConstraint(this);
		if(sc.getOrderColumns().size()==0)
			sc.addSortColumn("participantName",ORDER.ASC);
		return sc;
	}
	
	protected  List<ClientSummary> getExportClientSummary(){
		SearchConstraint sc=  getSearchConstraint();
		sc.setStartRow(0);
		sc.setRowCount(ALL_ROWS); //big enough
		QueryResult result;
		try {
			result = this.executeQuery(sc,true);
			return result.clients;
		} catch (Exception e) {
			log.error(e);
			FDUtils.addMsgError("Internal Error");
		}
		return Collections.emptyList();
	}
	

	public  String getClientsExportContent() {
		SystemManager systemManager =EJBFactory.getBean(SystemManager.class);
		String dateFormat=systemManager.getPss2Features().getDateFormat()+ " HH:mm:ss";
        StringBuilder str = new StringBuilder();
        if(isSceFlag()){
        	str.append("Client,Participant,Type,Programs,Event Status,Mode,Last Contact,Comm Status,Device Type,Participant Type,ABank,Lead Accounts,Contact1,Contact2,Contact3,Contact4");	
        }else{
        	str.append("Client,Participant,Type,Programs,Event Status,Mode,Last Contact,Comm Status,Device Type,Contact1,Contact2,Contact3,Contact4");
        }
        
		List<ClientSummary> summary=getExportClientSummary();
		List<JSFClient> clients= this.convertPartListToJSFPartList(summary);
		
        for(JSFClient part : clients){
        	str.append("\n");
        	str.append(part.getName()).append(",");
        	str.append(part.getParticipantName()).append(",");
        	str.append(part.getType()).append(",");
        	if(isEnableCBPConsolidation()){
        		str.append("\"").append(part.getProgramNamesWithConsolidation()).append("\",");
			}else{
				str.append("\"").append(part.getProgramNames()).append("\",");
			}        	 
        	str.append(part.getEventStatus()+",");
        	str.append(part.getMode()+",");
            str.append(part.getLastContact()==null ? "" :
            		new SimpleDateFormat(dateFormat).format(part.getLastContact())).append(",");
            
            str.append(part.getStatus()+",");
            //DRMS-6121
            String deviceType = part.getDeviceType();
            if(deviceType==null){
            	deviceType="";
            }
            str.append("\"").append(deviceType).append("\",");
           if(isSceFlag()){
        	   String participantType = part.getParticipantType();
               if(participantType==null){
               	participantType="";
               }
               str.append("\"").append(participantType).append("\",");
               String abank = part.getABank();
               if(abank==null){
               	abank="";
               }
               str.append("\"").append(abank).append("\",");
               String leadAccountString = part.getLeadAccountString();
               if(leadAccountString==null){
               	leadAccountString="";
               }
               str.append("\"").append(leadAccountString).append("\",");  
           }
           
            List<String> emailAdd = part.getEmailAddress();
            str.append(emailAdd.size()>0?emailAdd.get(0):"").append(",");
            str.append(emailAdd.size()>1?emailAdd.get(1):"").append(",");
            str.append(emailAdd.size()>2?emailAdd.get(2):"").append(",");
            str.append(emailAdd.size()>3?emailAdd.get(3):"");
        }
        return str.toString();
    }


	private String generateContactsContent() {
		SystemManager systemManager =EJBFactory.getBean(SystemManager.class);
		boolean dlenabled=systemManager.getPss2Features().isDemandLimitingEnabled();
		StringBuffer sb = new StringBuffer(
				"Client,Contact Name,Email Address,Event Notification,Comms Notification");
		
		if(dlenabled)
			sb.append(",Demand Limiting Notification");
		
		SearchConstraint sc = this.getSearchConstraint();
		//export all rows, no pagination
		sc.setRowCount(ALL_ROWS);
		List<PartContact> contacts = Collections.emptyList();
	
		try{
			ClientSearchCriteria csc = this.getClientSearchCritera();
			contacts=getQueryManager().getClientContacts(csc, sc);
			for (PartContact contact : contacts) {
				sb.append("\n"+contact.getName()+",");
				sb.append(contact.getDescription()+ ",");
				sb.append(contact.getAddress()+ ",");
				sb.append((contact.getEventNotification()==null?"":contact.getEventNotification().description())+",");
				sb.append((contact.getCommNotification()==null?false:contact.getCommNotification()));
				if(dlenabled)
					sb.append(","+contact.getDemandLimitingNotifications());
			}
		}catch(CriteriaContradictionException e){
			
		}catch(Exception e){
			FDUtils.addMsgError("Internal Error");
			log.error(e.getMessage(),e);
		}
		return sb.toString();
	}
	
	public void exportContactsAction() throws IOException {
		String filename = "contactList.csv";
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) fc
				.getExternalContext().getResponse();
		response.reset();
		response.addHeader("cache-control", "must-revalidate");
		response.setContentType("application/octet_stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ filename + "\"");
		String result = generateContactsContent();
		
		BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(response.getOutputStream() ) );
		bw.write(result);
		bw.flush();
		bw.close();
		fc.responseComplete();
	}
	
	public static class QueryResult implements Serializable{
		private static final long serialVersionUID = -5053307918175993425L;
		public int totalCount =0;
		public List<ClientSummary> clients =Collections.emptyList();
		
	}
	protected SystemManager getSystemManager(){
		if(this.systemManager==null){
			systemManager = EJBFactory.getBean(SystemManager.class);
		}
		return systemManager;
	}
	private boolean isSCEUtility(){
		boolean flag = getSystemManager().getPss2Features().isParticipantInfoEnabled();
		retrieveCorePropertiesFlag = true;
		sceFlag = flag;
		return sceFlag;
	}
	private SystemManager systemManager;
	private boolean sceFlag;
	private boolean retrieveCorePropertiesFlag = false;

	/**
	 * @return the sceFlag
	 */
	public boolean isSceFlag() {
		if(!retrieveCorePropertiesFlag){
			isSCEUtility();
		}
		return sceFlag;
	}

	/**
	 * @param sceFlag the sceFlag to set
	 */
	public void setSceFlag(boolean sceFlag) {
		this.sceFlag = sceFlag;
	}

	/**
	 * @return the retrieveCorePropertiesFlag
	 */
	public boolean isRetrieveCorePropertiesFlag() {
		return retrieveCorePropertiesFlag;
	}

	/**
	 * @param retrieveCorePropertiesFlag the retrieveCorePropertiesFlag to set
	 */
	public void setRetrieveCorePropertiesFlag(boolean retrieveCorePropertiesFlag) {
		this.retrieveCorePropertiesFlag = retrieveCorePropertiesFlag;
	}

	/**
	 * @param systemManager the systemManager to set
	 */
	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
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
	
	private boolean canDeleteClient;
	private boolean canSearchClient;
	private boolean canExport;
	private boolean canClearClient;
	private boolean canExportContacts;
	private boolean searchInResults;
	private boolean deleteCheckBoxDisable;
	
	public boolean isCanDeleteClient() {
		return canDeleteClient;
	}

	public void setCanDeleteClient(boolean canDeleteClient) {
		this.canDeleteClient = canDeleteClient;
	}

	public boolean isCanSearchClient() {
		return canSearchClient;
	}

	public void setCanSearchClient(boolean canSearchClient) {
		this.canSearchClient = canSearchClient;
	}

	public boolean isCanExport() {
		return canExport;
	}

	public void setCanExport(boolean canExport) {
		this.canExport = canExport;
	}

	public boolean isCanClearClient() {
		return canClearClient;
	}

	public void setCanClearClient(boolean canClearClient) {
		this.canClearClient = canClearClient;
	}

	public boolean isCanExportContacts() {
		return canExportContacts;
	}

	public void setCanExportContacts(boolean canExportContacts) {
		this.canExportContacts = canExportContacts;
	}	

	public boolean isSearchInResults() {
		return searchInResults;
	}

	public void setSearchInResults(boolean searchInResults) {
		this.searchInResults = searchInResults;
	}		

	public boolean isDeleteCheckBoxDisable() {
		return deleteCheckBoxDisable;
	}

	public void setDeleteCheckBoxDisable(boolean deleteCheckBoxDisable) {
		this.deleteCheckBoxDisable = deleteCheckBoxDisable;
	}

	private void buildViewLayout(){
        try {
        	getViewBuilderManager().buildClientViewLayout(this);
        } catch (NamingException e) {            
        	// log exception
        }
	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{
       return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}	
}
