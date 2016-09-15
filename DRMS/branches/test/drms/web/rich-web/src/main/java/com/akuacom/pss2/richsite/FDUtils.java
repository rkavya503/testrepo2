/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.facdash.FDUtils.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.richsite;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * The Class FDUtils.
 */
public class FDUtils
{

	/**
	 * Adds the msg info.
	 *
	 * @param message the message
	 */
	static public void addMsgInfo(String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
			message, message));
	}

	/**
	 * Adds the msg error.
	 *
	 * @param message
	 *            the message
	 */
	static public void addMsgError(String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
			message, message));
	}

	
	/**
	 * Adds the msg error.
	 *
	 * @param message
	 *            the message
	 */
	static public void addMsgWarn(String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
			message, message));
	}


}
