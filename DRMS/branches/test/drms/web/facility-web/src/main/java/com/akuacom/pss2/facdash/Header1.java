/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.Header1.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SerializableDataModel;
import org.apache.commons.lang.StringUtils;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManagerBean;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.HeaderStyle;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.UserType;
import com.akuacom.pss2.web.aggregation.AggregationTree;
import javax.naming.InitialContext;

import javax.naming.NamingException;

import com.akuacom.pss2.userrole.ViewBuilderManager;

import com.akuacom.pss2.userrole.viewlayout.EventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.FacdashHeaderLayout;





/**
 * The Class Header1.
 */
public class Header1 implements Serializable, FacdashHeaderLayout {
	
	/** The participant name. */
	String participantName;
	
	/** This is string displayed in the header indicating who is logged in. */
	String welcomeName;
	
	/** This is string displayed in the header logged in User Role. */
	String userRole;
	
	/** These select the current tab. Only one should be non-empty at any given time. The active tab should be set to "current". */
	String status;
	
	String clients;
	String events;
	String programs;
	String usage;
	String irrUsage;
	String news;
	String subAccounts;
	String options;
	String about;
	String aggregationTree;
	private String loginParticipant;
	String demoEvent;
	String simpleContacts;
	String demandLimitingDashboard;

	String parentParticipant = null;  // for aggregation
	String switchingParticipant;  // tmp value for update current participant from agg

    String swtichingProgram = "";
    List<String> switchingPrograms = new ArrayList<String>();
    String parentProgram;
	
	PSS2Features features;
	PSS2Properties properties;
	
	String participantShed;
	
	private boolean enableClientOptionTab = false;
	private boolean enableClientTestTab = false;
	private boolean enableCompleteInstallation = false;

    private String VIEW_TABLE_NAME = "";
	
	/** true if an operator or administrator is logged in as a participant. */
	boolean substituteUser;

	// true if we came for the clinets page
	boolean fromClients;
	boolean fromEvents;

    boolean installer;
    
    UserType userType;

    boolean userDataEnabled;
    boolean completeInstallation;

    String header_label;
    String CUSTOMER_LABEL = "DRAS Customer Interface";
    String INSTALLER_LABEL = "DRAS Installer Portal";

    boolean iirClientPush;
    private String hostName;

    private boolean adminLogin = false;

    private boolean localAggParticipation = false;
    //DRMS-6769
    private boolean sceOptOutFlag = false;
    
    //DRMS-8733
    private boolean editConstraints = true;
    //DRMS-8708
    private boolean editShed = true;

	/**
	 * Instantiates a new header1.
	 */
	public Header1()
	{
		ExternalContext context = 
		FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();

		clearAll();
		 
		// clear cached objects
		FDUtils.setSubAccounts(null);
		
		SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
        ParticipantManager pManager = EJBFactory.getBean(ParticipantManager.class);
		features = systemManager.getPss2Features();
		properties = systemManager.getPss2Properties();
		
         // for double click done link case
		if(request.getParameter("user") == null ){

            // participant login
			participantName = context.getUserPrincipal().getName();
			loginParticipant = participantName;
			parentParticipant = loginParticipant;
            this.adminLogin = false;


            try{
                    Participant loggedUser = pManager.getParticipant(participantName);
                    this.setLocalAggParticipation(this.inAggregationTree(loggedUser));
                    welcomeName = participantName;
                    substituteUser = false;
                    this.setUserDataEnabled(loggedUser.getDataEnabler());
                    this.setUserType(loggedUser.getUserType());

                    if (!loggedUser.getInstaller()){
                        this.setInstaller(false);
                        this.setHeader_label(CUSTOMER_LABEL);
                    }else{
                        this.setInstaller(true);
                        this.setHeader_label(INSTALLER_LABEL);
                    }

                    String defaultPage = features.getFeatureFacdashDefaultPage();
                    Boolean newsEnabled= features.isNewsEnabled();
                    
                   
                    
                    if (UserType.SIMPLE.equals(this.getUserType())) {
                        status = "current";
                    }
                    else if(newsEnabled && "news".equals(defaultPage))
                    {
                        news = "current";
                    }
                    else
                    {
                        clients = "current";
                    }


            }catch(Exception e){
             
            }
 
		} else if (request.getParameter("installer") != null && request.getParameter("installer").equalsIgnoreCase("true")){
			participantName = request.getParameter("user");
			loginParticipant = participantName;
			parentParticipant = loginParticipant;
            this.adminLogin = false;
            this.installer = true;

            Participant loggedUser = pManager.getParticipant(participantName);
            this.setLocalAggParticipation(this.inAggregationTree(loggedUser));
			
			this.setInstaller(true);
            this.setHeader_label(INSTALLER_LABEL);
            this.setUserType(loggedUser.getUserType());
        
			welcomeName = context.getUserPrincipal().getName() + " (" + participantName + ")";
            this.setUserDataEnabled(loggedUser.getDataEnabler());
			substituteUser = true;
			if (UserType.SIMPLE.equals(this.getUserType())) {
            	status = "current";
            } else {
            	clients = "current";
            }
               if(request.getParameter("createClient") != null ){
                       try{
                            JSFClient jsfClient = new JSFClient();
                            jsfClient.setClientType("AUTO");
                            FDUtils.setJSFClient(jsfClient);
                            context.redirect("/facdash/jsp/client-create.jsf");
                     }catch(Exception e){

                     }
                }
         } else {

             this.setInstaller(false);
             this.setHeader_label(CUSTOMER_LABEL);
			 participantName = request.getParameter("user");
			 loginParticipant = participantName;
			 parentParticipant = loginParticipant;
             this.adminLogin = true;

			 Participant loggedUser = pManager.getParticipant(participantName);
             this.setLocalAggParticipation(this.inAggregationTree(loggedUser));           
			 welcomeName = context.getUserPrincipal().getName() + " (" + participantName + ")";
             this.setUserDataEnabled(loggedUser.getDataEnabler());
             //this.setDemandLimitingEnabled(loggedUser.getDemandLimiting());
             this.setUserType(loggedUser.getUserType());
			 substituteUser = true;
			 if (UserType.SIMPLE.equals(this.getUserType())) {
            	status = "current";
            } else {
            	clients = "current";
            }
             
                 if(request.getParameter("createClient") != null ){
                       try{
                            JSFClient jsfClient = new JSFClient();
                            jsfClient.setClientType("AUTO");
                            FDUtils.setJSFClient(jsfClient);
                            context.redirect("/facdash/jsp/client-create.jsf");
                     }catch(Exception e){

                     }
                }
 		}

		JSFParticipant jsfParticipant = new JSFParticipant(participantName);
		FDUtils.setJSFParticipant(jsfParticipant);	
		
        if(request.getParameter("client") != null)
        {
            fromClients = true;
            if (request.getParameter("fromEvents") != null && "true".equals(request.getParameter("fromEvents"))) {
            	fromEvents = true;
            } else {
            	fromEvents = false;
            }
            JSFClient jsfClient = new JSFClient();
            jsfClient.load(request.getParameter("client"));
            jsfClient.setEdit(true);
            
            if(request.getParameter("subTab") != null){
                String subTab = request.getParameter("subTab");
                jsfClient.setSelectedTab(subTab);  
            }

            FDUtils.setJSFClient(jsfClient);
        }
        else
        {
            fromClients = false;
        }
        //DRMS-6769
        String utilityName = properties.getUtilityName();
        if(utilityName.equalsIgnoreCase("sce")){
        	sceOptOutFlag = adminLogin;
        }else{
        	sceOptOutFlag = true;
        }
        
        Boolean isProductionServer = systemManager.getPss2Features().isProductionServer();
    	if(isProductionServer){
    		if(isFacilityOperator()){
    			headerStyle = HeaderStyle.PRODUCTION_SERVER_FACILITY_OPERATOR.getStyleName();
    		}else{
    			headerStyle = HeaderStyle.PRODUCTION_SERVER_UTILITY_OPERATOR.getStyleName();
    		}
    		
    	}else{
    		if(isFacilityOperator()){
    			headerStyle = HeaderStyle.TEST_SERVER_FACILITY_OPERATOR.getStyleName();
    		}else{
    			headerStyle = HeaderStyle.TEST_SERVER_UTILITY_OPERATOR.getStyleName();
    		}
    		
    	}
    	
    	
    	//DRMS-6982
        if (request.getParameter("aggDisplayMode") != null && request.getParameter("aggDisplayMode").equalsIgnoreCase("reset")){
        	FDUtils.setAggregationTree(new AggregationTree());
        }
        
        buildViewLayout();
    }



    public boolean inAggregationTree(Participant p){
           ProgramParticipantAggregationManager aggMan = (ProgramParticipantAggregationManager)EJBFactory.getBean(ProgramParticipantAggregationManagerBean.class);
           int aggCount = 0;
           for (ProgramParticipant ppp : p.getProgramParticipants()) {
                aggCount += aggMan.getDescendants(ppp).size();
			}
           if (aggCount == 0) return false;
           else return true;
    }

    public String getHostName() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        hostName = request.getServerName();
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }



    public String getHeader_label() {
        return header_label;
    }

    public void setHeader_label(String header_label) {
        this.header_label = header_label;
    }

    public boolean isUserDataEnabled() {
    	
        return userDataEnabled;
    }

    public void setUserDataEnabled(boolean userDataEnabled) {
        this.userDataEnabled = userDataEnabled;
    }

	/**
	 * Clear all.
	 */
	private void clearAll()
	{
		status = "tstatus";
		clients = "tclients";
		events = "tevents";
		usage = "tusage";
		irrUsage = "tirrUsage";
		programs = "tprograms";
		simpleContacts = "tsimpleContacts";
		news = "tnews";
		subAccounts = "tsubAccounts";
		options = "toptions";
		about = "tabout";
		aggregationTree="taggregationTree";
		demoEvent="tdemoEvent";
		demandLimitingDashboard="tdemandlimiting";
		participantShed = "tParticipantShed";
	}

	/**
	 * Logout action.
	 * 
	 * @return the string
	 */
	public String logoutAction()
	{
		clearAll();
		clients = "current";
		if(!substituteUser)
		{

            if (this.isInstaller()&& this.completeInstallation){
                this.installer = false;
                completeInstillationAction();
                FDUtils.setHeader1(null);
            }

			HttpSession session =
				(HttpSession) FacesContext.getCurrentInstance()
					.getExternalContext().getSession(false);
			if (session != null) {
			    session.invalidate();
			}
			return "logout";
		} else if (this.isInstaller()&& this.completeInstallation){
            this.installer = false;
            completeInstillationAction();
            FDUtils.setHeader1(null);
            return "backToUtilityParticipants";
        }else {
            FDUtils.setJSFProgram(null);
			FDUtils.setHeader1(null);
			if (fromEvents) {
                this.fromClients = false;
				return "backToUtilityEvents";
			}
			
            if(fromClients)
            {
                return "backToUtilityClients";
            }
            else
            {
                return "backToUtilityParticipants";
             }
		}
	}


        public void completeInstillationAction(){
            ParticipantManager pManager = EJBFactory.getBean(ParticipantManager.class);
            Participant installerUser = pManager.getParticipant(FDUtils.getJSFParticipant().getName());
                installerUser.setInstaller(false);
                installerUser.setUserType(userType);
            pManager.updateParticipant(installerUser);
    }


	public boolean isCustomRulesEnabled()
	{
		return features.isFeatureRulesEnabled();
	}

	public boolean isExtendedNotification()
	{
		return features.isFeatureExtendedNotificationEnabled();
	}
	
	public boolean isDisplayAggregation()
	{
		return !UserType.SIMPLE.equals(this.getUserType()) 
		  && !this.isInstaller() && ((this.features != null) && this.features.isAggregationEnabled().booleanValue() && this.localAggParticipation );
	}
	
	public boolean isIndividualUpdateAggregation()
	{
		return isDisplayAggregation()&&((this.features != null) && (!this.features.isAggBatchUpdateEnabled().booleanValue()));
	}
	
	public boolean isBatchUpdateAggregation()
	{
		return isDisplayAggregation()&&((this.features != null) && this.features.isAggBatchUpdateEnabled().booleanValue());
	}
	
	public boolean isDisplayStatus()
	{
        try{
		return UserType.SIMPLE.equals(this.getUserType()) 
			|| (features.isUsageEnabled() && isUserDataEnabled()) ;
        }catch(Exception e){

        }
        return false;
	}
	
	public boolean isDisplaySimpleContacts()
	{
      try{
		return UserType.SIMPLE.equals(this.getUserType()) && !this.isInstaller();
        }catch(Exception e){

        }
        return false;
	}
	
	public boolean isDisplayUsage()
	{
		return features.isUsageEnabled() && (!UserType.SIMPLE.equals(this.getUserType()) || isInstaller());
	}
	
	public boolean isDisplayNews()
	{
		return features.isNewsEnabled() && (!UserType.SIMPLE.equals(this.getUserType()) || isInstaller());
	}
	
	public boolean isClientEnabled() {
		return !UserType.SIMPLE.equals(this.getUserType()) || isInstaller();
	}
	
	public boolean isEventsEnabled() {
		return !UserType.SIMPLE.equals(this.getUserType()) || isInstaller();
	}
	public boolean isShedInfoEnabled() {
		return features.isFeatureShedInfoEnabled();
	}
	public boolean isProgramsEnabled() {
        if (isInstaller()) return false;
		return !UserType.SIMPLE.equals(this.getUserType())  ;
	}
	
	public boolean isDemandLimitingEnabled() {
		return features.isDemandLimitingEnabled();
	}
	
	public boolean isDemandLimitingMockMeterEnabled() {
		return isDemandLimitingEnabled() && features.isDemandLimitingMockMeterEnabled();
	}
	
	public boolean isDisplaySubAccounts()
	{
		if (UserType.SIMPLE.equals(this.getUserType()) && !isInstaller()) {
			return false;
		}
		
		if(substituteUser && features.isSubAccountsEnabled())
		{
			return false;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isDisplayCustomerTestEvent(){
		
		return features.isCustumerTestEventEnabled() && (!UserType.SIMPLE.equals(this.getUserType()) || isInstaller()) ;
	}
	
	
	/**
	 * Gets the participant name.
	 * 
	 * @return the participant name
	 */
	public String getParticipantName()
	{
		return participantName;
	}

	/**
	 * Sets the participant name.
	 * 
	 * @param participantName the new participant name
	 */
	public void setParticipantName(String participantName)
	{
		this.participantName = participantName;
	}

	/**
	 * Gets the welcome name.
	 * 
	 * @return the welcome name
	 */
	public String getWelcomeName()
	{
		return welcomeName;
	}

	
	public String getUserRole() {
		
		if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Admin.toString())) {
			userRole = DrasRole.Admin.toString();
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Operator.toString())) {
			userRole = DrasRole.Operator.toString();
		}else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.FacilityManager.toString())) {
			userRole = DrasRole.FacilityManager.toString();
			sceOptOutFlag = true;
		}else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.UtilityOperator.toString())) {
			userRole = DrasRole.UtilityOperator.toString();
		}else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Readonly.toString())) {
			userRole = DrasRole.Readonly.toString();
			sceOptOutFlag = false;
			editConstraints = false;
			editShed = false;
		} else  {
			userRole = DrasRole.Dispatcher.toString();
			sceOptOutFlag = false;
			editConstraints = false;
			editShed = false;
		}
		
		return userRole;
	}

	
    
	public boolean isEditShed() {
		return editShed;
	}



	public void setEditShed(boolean editShed) {
		this.editShed = editShed;
	}
	


	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	/**
	 * Gets the server time.
	 * 
	 * @return the server time
	 */
	public Date getServerTime()
	{
		return new Date();
	}

	/**
	 * Status action.
	 * 
	 * @return the string
	 */
	public String statusAction()
	{
		clearAll();
		status = "current";
		//return "status";
		return "simpleDashboard";
	}

	/**
	 * Demand Limiting Monitor action.
	 * 
	 * @return the string
	 */
	public String demandLimitingDashboardAction()
	{
		clearAll();
		status = "current";
		//return "status";
		return "demandLimitingDashboard";
	}

	/**
	 * Clients action.
	 * 
	 * @return the string
	 */
	public String clientsAction()
	{
		clearAll();
		clients = "current";
		return "clients";
	}

	/**
	 * Events action.
	 * 
	 * @return the string
	 */
	public String eventsAction()
	{
		clearAll();
		events = "current";
		return "events";
	}

	/**
	 * Programs action.
	 * 
	 * @return the string
	 */
	public String programsAction()
	{
		clearAll();
		programs = "current";		
		return "programs";
	}

	/**
	 * News action.
	 * 
	 * @return the string
	 */
	public String usageAction()
	{
		clearAll();
		usage = "current";
		return "usage";
	}

	/**
	 * News action.
	 * 
	 * @return the string
	 */
	public String newsAction()
	{
		clearAll();
		news = "current";
		return "news";
	}

	/**
	 * sub account action.
	 * 
	 * @return the string
	 */
	public String subAccountsAction()
	{
		clearAll();
		subAccounts = "current";
		return "subAccounts";
	}

	/**
	 * Options action.
	 * 
	 * @return the string
	 */
	public String optionsAction()
	{
		clearAll();
		options = "current";
		return "options";
	}
	/**
	 * Options action.
	 * 
	 * @return the string
	 */
	public String participantShedAction()
	{
		clearAll();
		participantShed = "current";
		return "participantShed";
	}	
	/**
	 * 
	 */
	public String demoEventAction(){
		clearAll();
		demoEvent="current";
		return "demoEvent";
	}
	
	public String simpleContactsAction() {
		
		ClientManager cm = EJB3Factory.getBean(ClientManager.class);

		boolean found = false;
        if (cm.getClients(FDUtils.getParticipantName()) != null || cm.getClients(FDUtils.getParticipantName()).size() > 0 ){
            for (Participant clientInfo: cm.getClients(FDUtils.getParticipantName()))
            {
                JSFClient jsfClient = new JSFClient();
                jsfClient.load(clientInfo.getParticipantName());
                jsfClient.setEdit(true);
                FDUtils.setJSFClient(jsfClient);
                found = true;
                break;
            }
        }
        if (!found) {
        	//return "errorSimplifiedUser";
            FDUtils.addMsgError("You need to add clients to this user");
        }
    	
    	clearAll();
		simpleContacts = "current";
		
		return "simpleContacts";
	}

	/**
	 * About action.
	 * 
	 * @return the string
	 */
	public String aboutAction()
	{
		clearAll();
		about = "current";
		FDUtils.setAbout(new About());
		return "about";
	}
	
	public String aggregationTreeAction()
	{
		clearAll();
		aggregationTree = "current";
		FDUtils.setAbout(new About());
		return "aggregationTree";
	}
	
	
		
	/**
	 * Navigate to irrUsage page
	 * 
	 * @return the string
	 */
	public String irrUsageAction()
	{
		clearAll();
		irrUsage = "current";
		return "irrUsage";
	}
	
	public String navigateAction(){
		
		switchAgg();
		
		if("current".equalsIgnoreCase(clients)){
			return clientsAction();
		}else if("current".equalsIgnoreCase(events)){
			return eventsAction();
		}else if("current".equalsIgnoreCase(programs)){
			return programsAction();
		}else if("current".equalsIgnoreCase(usage)){
			return usageAction();
		}else if("current".equalsIgnoreCase(irrUsage)){
			return irrUsageAction();
		}else if("current".equalsIgnoreCase(news)){
			return newsAction();
		}else if("current".equalsIgnoreCase(subAccounts)){
			return subAccountsAction();
		}else if("current".equalsIgnoreCase(options)){
			return optionsAction();
		}else if("current".equalsIgnoreCase(about)){
			return aboutAction();
		}else if("current".equalsIgnoreCase(demoEvent)){
			return demoEventAction();
		}else if("current".equalsIgnoreCase(simpleContacts)){
			return simpleContactsAction();
		}else if("current".equalsIgnoreCase(demandLimitingDashboard)){
			return demandLimitingDashboardAction();
		}else if("current".equalsIgnoreCase(aggregationTree)){
			return aggregationTreeAction();
		}else{
			return clientsAction();
		}
	}
	
	public void switchAggregationTree(String participantName, String programName){
		 setSwitchingParticipant(participantName);
		 setSwtichingProgram(programName);
		 switchAgg();
		 this.navigateAction();
	}
	
	private void switchAgg(){
		//DRMS-7118
		
		if(!switchingParticipant.equalsIgnoreCase("")){
	        ParticipantManager pManager = EJBFactory.getBean(ParticipantManager.class);
	        Participant loggedUser = pManager.getParticipant(switchingParticipant);
	        this.setUserDataEnabled(loggedUser.getDataEnabler());
	        
		}
	}
	/**
	 * Gets the events.
	 * 
	 * @return the events
	 */
	public String getEvents()
	{
		return events;
	}

	/**
	 * Sets the events.
	 * 
	 * @param events the new events
	 */
	public void setEvents(String events)
	{
		this.events = events;
	}

	/**
	 * Gets the clients.
	 * 
	 * @return the clients
	 */
	public String getClients()
	{
		return clients;
	}

	/**
	 * Sets the clients.
	 * 
	 * @param clients the new clients
	 */
	public void setClients(String clients)
	{
		this.clients = clients;
	}

	/**
	 * Gets the options.
	 * 
	 * @return the options
	 */
	public String getOptions()
	{
		return options;
	}

	/**
	 * Sets the options.
	 * 
	 * @param options the new options
	 */
	public void setOptions(String options)
	{
		this.options = options;
	}

	
	/**
	 * Get the DemoEvent
	 * @return the demoEvent
	 */
	public String getDemoEvent() {
		return demoEvent;
	}
	
	
	/**
	 * set the DemoEvent 
	 * @param the demoEvent
	 */
	public void setDemoEvent(String demoEvent) {
		this.demoEvent = demoEvent;
	}

	public String getSimpleContacts() {
		return simpleContacts;
	}


	public void setSimpleContacts(String simpleContacts) {
		this.simpleContacts = simpleContacts;
	}


	/**
	 * Gets the about.
	 * 
	 * @return the about
	 */
	public String getAbout()
	{
		return about;
	}

	/**
	 * Sets the about.
	 * 
	 * @param about the new about
	 */
	public void setAbout(String about)
	{
		this.about = about;
	}

	/**
	 * Gets the news.
	 * 
	 * @return the news
	 */
	public String getNews()
	{
		return news;
	}

	/**
	 * Sets the news.
	 * 
	 * @param news the new news
	 */
	public void setNews(String news)
	{
		this.news = news;
	}

	/**
	 * Gets the programs.
	 * 
	 * @return the programs
	 */
	public String getPrograms()
	{
		return programs;
	}

	/**
	 * Sets the programs.
	 * 
	 * @param programs the new programs
	 */
	public void setPrograms(String programs)
	{
		this.programs = programs;
	}
	
	/**
	 * Gets the logout display.
	 * 
	 * @return the logout display
	 */
	public String getLogoutDisplay()
	{
		if(substituteUser)
		{
			return "Done";
		}
		else
		{
			return "Logout";
		}
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * Gets the usage.
	 * 
	 * @return the usage
	 */
	public String getUsage()
	{
		return usage;
	}

	/**
	 * Sets the usage.
	 * 
	 * @param usage the usage to set
	 */
	public void setUsage(String usage)
	{
		this.usage = usage;
	}

	public String getSubAccounts()
	{
		return subAccounts;
	}

	public void setSubAccounts(String subAccounts)
	{
		this.subAccounts = subAccounts;
	}

	public String getParentParticipant() {
		if (parentParticipant == null) {
			parentParticipant = participantName;
		}
		
		return parentParticipant;
	}

	public void setParentParticipant(String parentParticipant) {
		this.parentParticipant = parentParticipant;
	}

	public String getSwitchingParticipant() {
		return switchingParticipant;
	}

	public void setSwitchingParticipant(String switchingParticipant) {
	    this.switchingParticipant = switchingParticipant;
		participantName = switchingParticipant;
		if (participantName == null) {
			// should not happen
			return;
		}
		
		int x = participantName.indexOf(" (");
		if (x > -1) {
			participantName = participantName.substring(0, x);
		}
		
		JSFParticipant jsfParticipant = new JSFParticipant(participantName);
		FDUtils.setJSFParticipant(jsfParticipant);
	}

    public String getParentProgram() {
        return parentProgram;
    }

    public void setParentProgram(String parentProgram) {
        this.parentProgram = parentProgram;
    }

    public String getSwtichingProgram() {
        return swtichingProgram;
    }

    public void setSwtichingProgram(String swtichingProgram) {
		this.swtichingProgram = swtichingProgram;
		this.parentProgram = swtichingProgram;

		JSFProgram jsfProgram = new JSFProgram(StringUtils.isEmpty(parentProgram)?"Program":parentProgram);
		FDUtils.setJSFProgram(jsfProgram);

    }

    public List<String> getSwitchingPrograms() {
        return switchingPrograms;
    }

    public void setSwitchingPrograms(List<String> switchingPrograms) {
        this.switchingPrograms = switchingPrograms;
    }

    public boolean isInstaller() {
        return installer;
    }

    public void setInstaller(boolean installer) {
        this.installer = installer;
    }

    public UserType getUserType() {
		return userType;
	}


	public void setUserType(UserType userType) {
		this.userType = userType;
	}

    public String getVIEW_TABLE_NAME() {
        return VIEW_TABLE_NAME;
    }

    public void setVIEW_TABLE_NAME(String VIEW_TABLE_NAME) {
        this.VIEW_TABLE_NAME = VIEW_TABLE_NAME;
    }

    public boolean isIirClientPush() {
       iirClientPush = features.isIrrClientPushEnabled();
       return iirClientPush;
    }

    public void setIirClientPush(boolean iirClientPush) {
        this.iirClientPush = iirClientPush;
    }

    public boolean isCompleteInstallation() {
        return completeInstallation;
    }

    public void setCompleteInstallation(boolean completeInstallation) {
        this.completeInstallation = completeInstallation;
    }
	public String getDemandLimitingDashboard() {
		return demandLimitingDashboard;
	}


	public void setDemandLimitingDashboard(String demandLimitingDashboard) {
		this.demandLimitingDashboard = demandLimitingDashboard;
	}

	public String getIrrUsage() {
		return irrUsage;
	}
	
	public void setIrrUsage(String irrUsage) {
		this.irrUsage = irrUsage;
	}

    public boolean isAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(boolean adminLogin) {
        this.adminLogin = adminLogin;
    }

    public boolean isLocalAggParticipation() {
        return localAggParticipation;
    }

    public void setLocalAggParticipation(boolean localAggParticipation) {
        this.localAggParticipation = localAggParticipation;
    }
    public boolean isDeviceTypeVisible() {
		return (isAdminLogin()||isInstaller());
	}

	public boolean isSceOptOutFlag() {
		return sceOptOutFlag;
	}

	public void setSceOptOutFlag(boolean sceOptOutFlag) {
		this.sceOptOutFlag = sceOptOutFlag;
	}
	
	public boolean isEditConstraints() {
		return editConstraints;
	}

	public void setEditConstraints(boolean editConstraints) {
		this.editConstraints = editConstraints;
	}
		
	//return true if a Facility operator is logined
	public boolean isFacilityOperator(){
		return !substituteUser;
	}
	private String headerStyle;
	public String getHeaderStyle(){
		return this.headerStyle;
	}

    public String getUtilityName() {
        return properties.getUtilityName();
    }

	/**
	 * @return the participantShed
	 */
	public String getParticipantShed() {
		return participantShed;
	}

	/**
	 * @param participantShed the participantShed to set
	 */
	public void setParticipantShed(String participantShed) {
		this.participantShed = participantShed;
	}


	public String getLoginParticipant() {
		return loginParticipant;
	}

	public void setLoginParticipant(String loginParticipant) {
		this.loginParticipant = loginParticipant;
	}
    
	public String getAggregationTree() {
		return aggregationTree;
	}

	public void setAggregationTree(String aggregationTree) {
		this.aggregationTree = aggregationTree;
	}
	
	public boolean isEnableClientOptionTab() {
		return enableClientOptionTab;
	}

	public void setEnableClientOptionTab(boolean enableClientOptionTab) {
		this.enableClientOptionTab = enableClientOptionTab;
	}

	public boolean isEnableClientTestTab() {
		return enableClientTestTab;
	}

	public void setEnableClientTestTab(boolean enableClientTestTab) {
		this.enableClientTestTab = enableClientTestTab;
	}
	
	public boolean isEnableCompleteInstallation() {
		return enableCompleteInstallation;
	}



	public void setEnableCompleteInstallation(boolean enableCompleteInstallation) {
		this.enableCompleteInstallation = enableCompleteInstallation;
	}



	private void buildViewLayout(){

		try {

			getViewBuilderManager().buildFacdashHeaderLayout(this);

		} catch (NamingException e) {

			// log exception

		}



	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{

		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");

	}


	
    
}
