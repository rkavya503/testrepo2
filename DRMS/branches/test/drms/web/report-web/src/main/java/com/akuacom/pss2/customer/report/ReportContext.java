/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.Header1.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.customer.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.HeaderStyle;
import com.akuacom.pss2.util.DrasRole;

//getSystemManager

/**
 * The Class ReportWebContext.
 */
public class ReportContext implements java.io.Serializable {

	private static final long serialVersionUID = -2025762200274708774L;

	/** The participant name. */
	String participantName;

	/** This is string displayed in the header indicating who is logged in. */
	String welcomeName;
	
	/** This is string displayed in the report indicating what is the User role. */
	String userRole;

	String parentParticipant = null; // for aggregation
	String switchingParticipant; // tmp value for update current participant
									// from agg

	String swtichingProgram = "";
	List<String> switchingPrograms = new ArrayList<String>();
	String parentProgram;
	String logoutDisplay;
	private String headerStyle;
	public String getHeaderStyle(){
		return this.headerStyle;
	}

	private String hostName;

	/**
	 * Instantiates a new header1.
	 */
	public ReportContext(SystemManager systemManager) {
		refreshContext(systemManager);
	}
	
	public void refreshContext(SystemManager systemManager) {
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		boolean isFacilityOperator = false; 
		if (request.getParameter("isFacilityOperator")!=null&&Boolean.valueOf(request.getParameter("isFacilityOperator"))) {
			participantName = context.getUserPrincipal().getName();
			welcomeName = participantName;
			isFacilityOperator = true;
		} else {
			participantName = request.getParameter("user");
			welcomeName = context.getUserPrincipal().getName() + " ("
					+ participantName + ")";
			isFacilityOperator = false;
		}
//		if (request.getParameter("isFacilityOperator") == null) {
//			participantName = context.getUserPrincipal().getName();
//			welcomeName = participantName;
//			isFacilityOperator = true;
//		} else {
//			participantName = request.getParameter("user");
//			welcomeName = context.getUserPrincipal().getName() + " ("
//					+ participantName + ")";
//			isFacilityOperator = false;
//		}
		
		 Boolean isProductionServer = systemManager.getPss2Features().isProductionServer();
	    	if(isProductionServer){
	    		if(isFacilityOperator){
	    			headerStyle = HeaderStyle.PRODUCTION_SERVER_FACILITY_OPERATOR.getStyleName();
	    		}else{
	    			headerStyle = HeaderStyle.PRODUCTION_SERVER_UTILITY_OPERATOR.getStyleName();
	    		}
	    		
	    	}else{
	    		if(isFacilityOperator){
	    			headerStyle = HeaderStyle.TEST_SERVER_FACILITY_OPERATOR.getStyleName();
	    		}else{
	    			headerStyle = HeaderStyle.TEST_SERVER_UTILITY_OPERATOR.getStyleName();
	    		}
	    		
	    	}
	}

	public String getHostName() {
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		hostName = request.getServerName();
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Clear all.
	 */
	private void clearAll() {
		this.participantName = "";
		this.welcomeName = "";
		this.parentParticipant = null;
		this.userRole="";
		this.switchingParticipant = "";
		this.swtichingProgram = "";
		this.switchingPrograms = new ArrayList<String>();
		this.parentProgram = "";
		this.logoutDisplay = "";
	}

	/**
	 * Logout action.
	 * 
	 * @return the string
	 */
	public String logoutAction() {
		clearAll();
		// FDUtils.setAggTree(null);
		return "closewindow";
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

	/**
	 * Gets the welcome name.
	 * @return the welcome name
	 */
	public String getWelcomeName() {
		return welcomeName;
	}
	
	/**
	 * Gets the user role.
	 * @return the user role
	 */	
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
		}else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.UtilityOperator.toString())) {
			userRole = DrasRole.UtilityOperator.toString();
		}else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Readonly.toString())) {
			userRole = DrasRole.Readonly.toString();
		} else  {
			userRole = DrasRole.Dispatcher.toString();
		}
		
		return userRole;
	}

public void setUserRole(String userRole) {
	this.userRole = userRole;
}

	

	/**
	 * Gets the server time.
	 * @return the server time
	 */
	public Date getServerTime() {
		return new Date();
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

	}

	public List<String> getSwitchingPrograms() {
		return switchingPrograms;
	}

	public void setSwitchingPrograms(List<String> switchingPrograms) {
		this.switchingPrograms = switchingPrograms;
	}
	
	public String getLogoutDisplay() {
		return "Close";
	}

	public void setLogoutDisplay(String logoutDisplay) {
		this.logoutDisplay = logoutDisplay;
	}
	

}
