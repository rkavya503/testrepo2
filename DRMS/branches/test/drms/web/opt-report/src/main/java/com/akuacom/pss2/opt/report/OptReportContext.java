/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.Header1.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.opt.report;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.akuacom.pss2.util.UserType;

/**
 * The Class ReportWebContext.
 */
public class OptReportContext implements Serializable {

	private static final long serialVersionUID = -2025762200274708774L;

	/** This is string displayed in the header indicating who is logged in. */
	String welcomeName;

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
}
