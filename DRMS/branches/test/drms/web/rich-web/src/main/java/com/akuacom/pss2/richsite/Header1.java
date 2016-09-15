

/**
 * The Class Header1.
 */
/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.facdash.Header1.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.richsite;

import java.util.Date;
import java.util.TimeZone;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.price.australia.LocationPriceManager;
import com.akuacom.pss2.price.australia.LocationPriceManagerBean;
import com.akuacom.pss2.price.australia.PriceRecord;
import com.akuacom.pss2.richsite.participant.ClientDataProvider;
import com.akuacom.pss2.richsite.participant.ParticipantDataProvider;
import com.akuacom.pss2.richsite.participant.SearchHistory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.HeaderStyle;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class Header1.
 */
public class Header1 implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8307050506969071912L;

	/** The participant name. */
	String participantName;

	/** This is string displayed in the header indicating who is logged in. */
	String welcomeName;

	/** This is string displayed in the header indicating who is logged in. */
	String userRole;
	
	/**
	 * These select the current tab. Only one should be non-empty at any given
	 * time. The active tab should be set to "current".
	 */
	String status;

	String clients;
	String events;
	String programs;
	String usage;
	String news;
	String subAccounts;
	String options;
	String about;
	String participant;
	String uid;
	String utilityName;

	PSS2Features features = null;
	
	private SystemManager systemManager;
	private LocationPriceManager LocationPriceManager;

	/** True is an operator or administrator is logged in as a participant. */
	boolean substituteUser;

	private static final Logger log = Logger
			.getLogger(Header1.class.getName());

	/**
	 * Instantiates a new header1.
	 */
	public Header1() {

		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		
		Boolean isProductionServer = getSystemManager().getPss2Features().isProductionServer();
    	if(isProductionServer){
    		headerStyle = HeaderStyle.PRODUCTION_SERVER_UTILITY_OPERATOR.getStyleName();
    	}else{
    		headerStyle = HeaderStyle.TEST_SERVER_UTILITY_OPERATOR.getStyleName();
    	}

		try {
			response.sendRedirect("/pss2.website/Login");
		} catch (Exception e) {
		}

		uid = request.getParameter("uid");
	}

	/**
	 * Clear all.
	 */
	private void clearAll() {
		status = "";
		clients = "";
		events = "";
		usage = "";
		programs = "";
		news = "";
		subAccounts = "";
		options = "";
		about = "";
		participant = "";
		
		features = null;
	}

	/**
	 * Logout action.
	 * 
	 * @return the string
	 */
	public String logoutAction() {
		clearAll();
		return "logout";

	}
	
	public String logoutAndCleanSession(){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpSession session = request.getSession();
		try {
			ParticipantDataProvider participantDataProvider = (ParticipantDataProvider) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("participantDataModel");
			ClientDataProvider clientDataProvider = (ClientDataProvider) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientDataModel");
			if(clientDataProvider!=null){
				clientDataProvider.getSearchHistory().clear();
			}
			if(participantDataProvider!=null){
				participantDataProvider.getSearchHistory().clear();
			}
            session.invalidate();
        } catch (IllegalStateException e) {
            // ignore that
        }
        String path = context.getRequestServletPath();
        
        if(path!=null){
        	int pathDepth = path.split("/").length-1;
        	//DRMS-7308
        	if(pathDepth==1){
        		//return "depth1";
        		return "success";
        	}else if(pathDepth==2){
        		return "depth2";
        	}else if(pathDepth==3){
        		return "depth3";
        	}else if(pathDepth==4){
        		return "depth4";
        	}
        }
		return "success";
	}
	public boolean isRulesEnabled() {
		return (getFeatures().isFeatureRulesEnabled().booleanValue());
	}
	
	public boolean isScorecardEnabled() {
		return getFeatures().isScorecardEnabled().booleanValue();
	}
	public boolean isInterruptibleProgramsEnabled() {
		return getFeatures().isInterruptibleProgramsEnabled().booleanValue();
	}
	public boolean isExtendedNotification() {
		return getFeatures().isFeatureExtendedNotificationEnabled().booleanValue();
	}

	public boolean isDisplayStatus() {
		return getFeatures().isUsageEnabled();
	}

	public boolean isDisplayUsage() {
		return getFeatures().isUsageEnabled();
	}

	public boolean isDisplayNews() {
		return getFeatures().isNewsEnabled().booleanValue();
	}
	
	public boolean isAggregationEnabled() {
		// this has session scope
		return getFeatures().isAggregationEnabled().booleanValue();
	}
	
	public boolean isDisplayPrice() {
		// this has session scope
		return getFeatures().isFeatureAustraliaPriceEnabled();
	}
	
	private PSS2Features getFeatures() {
		if (features == null) {
			features = getSystemManager().getPss2Features();
		}
		return features;
	}
	
	public boolean isDisplaySubAccounts() {
		if (substituteUser && getFeatures().isSubAccountsEnabled().booleanValue()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the participant name.
	 * 
	 * @return the participant name
	 */
	public String getParticipantName() {
		return participantName;
	}

	/**
	 * Sets the participant name.
	 * 
	 * @param participantName
	 *            the new participant name
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Gets the welcome name.
	 * 
	 * @return the welcome name
	 */
	public String getWelcomeName() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		return request.getRemoteUser();
	}

	/**
	 * Gets the User Role.
	 * 
	 * @return the User Role
	 */
	public String getUserRole() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		if (request.isUserInRole(DrasRole.Admin.toString())) {
			userRole = DrasRole.Admin.toString();
		} else if (request.isUserInRole(DrasRole.Operator.toString())) {
			userRole = DrasRole.Operator.toString();
		}else if (request.isUserInRole(DrasRole.FacilityManager.toString())) {
			userRole = DrasRole.FacilityManager.toString();
		}else if (request.isUserInRole(DrasRole.UtilityOperator.toString())) {
			userRole = DrasRole.UtilityOperator.toString();
		}else if (request.isUserInRole(DrasRole.Readonly.toString())) {
			userRole = DrasRole.Readonly.toString();
		} else  {
			userRole = DrasRole.Dispatcher.toString();
		}
		return userRole;
	}		
	
	
	/**
	 * Gets the server time.
	 * 
	 * @return the server time
	 */
	public Date getServerTime() {
		return new Date();
	}

	/**
	 * Status action.
	 * 
	 * @return the string
	 */
	public String statusAction() {
		clearAll();
		status = "current";
		return "status";
	}

	/**
	 * Clients action.
	 * 
	 * @return the string
	 */
	public String clientsAction() {
		clearAll();
		clients = "current";
		return "clients";
	}

	/**
	 * Clients action.
	 * 
	 * @return the string
	 */
	public String participantAction() {

		clearAll();
		participant = "current";
		return "participant";
	}

	/**
	 * Events action.
	 * 
	 * @return the string
	 */
	public String eventsAction() {
		clearAll();
		events = "current";
		return "events";
	}

	/**
	 * Programs action.
	 * 
	 * @return the string
	 */
	public String programsAction() {
		clearAll();
		programs = "current";
		/*
		 * ProgramTable programTable = new ProgramTable();
		 * FacesContext.getCurrentInstance().
		 * getExternalContext().getSessionMap().put("programs", programTable);
		 */
		return "programs";
	}

	/**
	 * News action.
	 * 
	 * @return the string
	 */
	public String usageAction() {
		clearAll();
		usage = "current";
		return "usage";
	}

	/**
	 * News action.
	 * 
	 * @return the string
	 */
	public String newsAction() {
		clearAll();
		news = "current";
		return "news";
	}

	/**
	 * sub account action.
	 * 
	 * @return the string
	 */
	public String subAccountsAction() {
		clearAll();
		subAccounts = "current";
		return "subAccounts";
	}

	/**
	 * Options action.
	 * 
	 * @return the string
	 */
	public String optionsAction() {
		clearAll();
		options = "current";
		return "options";
	}

	/**
	 * About action.
	 * 
	 * @return the string
	 */
	public String aboutAction() {
		clearAll();
		about = "current";

		// FDUtils.setAbout(new About());
		return "about";
	}

	/**
	 * Gets the events.
	 * 
	 * @return the events
	 */
	public String getEvents() {
		return events;
	}

	/**
	 * Sets the events.
	 * 
	 * @param events
	 *            the new events
	 */
	public void setEvents(String events) {
		this.events = events;
	}

	/**
	 * Gets the clients.
	 * 
	 * @return the clients
	 */
	public String getClients() {
		return clients;
	}

	/**
	 * Sets the clients.
	 * 
	 * @param clients
	 *            the new clients
	 */
	public void setClients(String clients) {
		this.clients = clients;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	/**
	 * Gets the options.
	 * 
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * Sets the options.
	 * 
	 * @param options
	 *            the new options
	 */
	public void setOptions(String options) {
		this.options = options;
	}

	/**
	 * Gets the about.
	 * 
	 * @return the about
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * Sets the about.
	 * 
	 * @param about
	 *            the new about
	 */
	public void setAbout(String about) {
		this.about = about;
	}

	/**
	 * Gets the news.
	 * 
	 * @return the news
	 */
	public String getNews() {
		return news;
	}

	/**
	 * Sets the news.
	 * 
	 * @param news
	 *            the new news
	 */
	public void setNews(String news) {
		this.news = news;
	}

	/**
	 * Gets the programs.
	 * 
	 * @return the programs
	 */
	public String getPrograms() {
		return programs;
	}

	/**
	 * Sets the programs.
	 * 
	 * @param programs
	 *            the new programs
	 */
	public void setPrograms(String programs) {
		this.programs = programs;
	}

	/**
	 * Gets the logout display.
	 * 
	 * @return the logout display
	 */
	public String getLogoutDisplay() {
		if (substituteUser) {
			return "Done";
		} else {
			return "Logout";
		}
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the usage.
	 * 
	 * @return the usage
	 */
	public String getUsage() {
		return usage;
	}

	/**
	 * Sets the usage.
	 * 
	 * @param usage
	 *            the usage to set
	 */
	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getSubAccounts() {
		return subAccounts;
	}

	public void setSubAccounts(String subAccounts) {
		this.subAccounts = subAccounts;
	}

	public String getUtilityName() {
		String uName = "";
		for (CoreProperty corp : getSystemManager().getAllProperties()) {
			if (corp.getPropertyName().equalsIgnoreCase("utilityName"))
				uName = corp.getStringValue();
		}
		return uName;
	}
	
	public String getAustrialiaPrice(){
		PriceRecord pr=getLocationPriceManager().getPrice();
		if(pr==null|| pr.getPrice()==null) return "N/A";
		return pr.getPrice()+"$/MW";
	}
	
	public String getPriceInformation(){
		PriceRecord pr=getLocationPriceManager().getPrice();
		if(pr==null|| pr.getPrice()==null) return "No price available";
		
		String format=(String) FacesContext.getCurrentInstance().
			getExternalContext().getApplicationMap().get("headerDateTimeFormat");
		
		return "Price for "+pr.getLocation()+",updated to "+ 
			DateUtil.format(pr.getTime(), format, TimeZone.getTimeZone("GMT+10"));
	}
	
    private String headerStyle;

	
    public String getHeaderStyle(){
    	return this.headerStyle;
    }
    
    public SystemManager getSystemManager(){
    	if(systemManager==null){
    		systemManager =  EJBFactory.getBean(SystemManagerBean.class);
    	}
    	return systemManager;
    }
    
    public LocationPriceManager getLocationPriceManager(){
    	if(LocationPriceManager==null){
    		LocationPriceManager =  EJBFactory.getBean(LocationPriceManagerBean.class);
    	}
    	return LocationPriceManager;
    }
    
    public static void main(String args[]){
    	String a="";
    	String b[] = a.split("/");
    	for(int i = 0;i<b.length;i++){
    		System.out.println(b[i]);
    	}
    	System.out.println(a.split("/").length);
    }
}
