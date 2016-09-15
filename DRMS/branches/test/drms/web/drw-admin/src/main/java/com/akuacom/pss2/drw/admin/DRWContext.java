/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.Header1.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.drw.admin;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.UserType;

/**
 * The Class DRWContext.
 */
public class DRWContext implements Serializable {

	private static final long serialVersionUID = -2025762200274708774L;

	public DRWContext(){
		programs = "current";
	}
	/** This is string displayed in the header indicating who is logged in. */
	String welcomeName;
	
	/** This is string displayed in the header indicating the user role. */
	String userRole;
	
	private String programs;
	private String upload;
	private String uploadConf;
	private String geographicConfig;

	/** true if an operator or administrator is logged in as a participant. */
	boolean substituteUser;

	boolean installer;

	UserType userType;

	String CUSTOMER_LABEL = "DRAS Customer Interface";
	String INSTALLER_LABEL = "DRAS Installer Portal";

	private String hostName;

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
	 * Gets the welcome name.
	 * 
	 * @return the welcome name
	 */
	public String getWelcomeName() {
		if (welcomeName == null) {
			ExternalContext context = FacesContext.getCurrentInstance()
					.getExternalContext();
			welcomeName = context.getUserPrincipal().getName();
		}
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


	/**
	 * Gets the server time.
	 * 
	 * @return the server time
	 */
	public Date getServerTime() {
		return new Date();
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

	public boolean isSubstituteUser() {
		return substituteUser;
	}

	public void setWelcomeName(String welcomeName) {
		this.welcomeName = welcomeName;
	}
	
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	/**
	 * Navigate to upload page.
	 * 
	 * @return the string
	 */
	public String uploadAction()
	{
		clearAll();
		upload = "current";
		return "upload";
	}
	
	/**
	 * Navigate to programs page.
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
	 * Navigate to programs page.
	 * 
	 * @return the string
	 */
	public String uploadConfAction()
	{
		clearAll();
		uploadConf = "current";
		return "uploadConf";
	}
	
	public String geographicConfAction()
	{
		clearAll();
		geographicConfig = "current";
		return "geographicConfig";
	}
	
	/**
	 * Clear all.
	 */
	private void clearAll()
	{
		programs = "programs";
		upload = "upload";
		uploadConf = "uploadConf";
		geographicConfig = "geographicConfig";
	}

	public String getPrograms() {
		return programs;
	}

	public String getUpload() {
		return upload;
	}
	
	public String getUploadConf() {
		return uploadConf;
	}

	public String getGeographicConfig() {
		return geographicConfig;
	}
	
}
