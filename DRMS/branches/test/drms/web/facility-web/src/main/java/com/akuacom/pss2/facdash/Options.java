/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.Options.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import org.apache.log4j.Logger;

import com.akuacom.accmgr.ws.AccMgrWS;
import com.akuacom.accmgr.ws.User;
import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import java.io.Serializable;

/**
 * The Class Options.
 */
public class Options implements Serializable {
    
    /** The Constant log. */
    private static final Logger log = Logger
		.getLogger(Options.class.getName());

    /** The current pw. */
    private String currentPW;
	
	/** The new pw. */
	private String newPW;
	
	/** The confirm pw. */
	private String confirmPW;

	/**
	 * Instantiates a new options.
	 */
	public Options()
	{
	}

	/**
	 * Update password action.
	 * 
	 * @return the string
	 */
	public String updatePasswordAction()
	{
		final AccMgrWSClient accmgrClient = new AccMgrWSClient();
		accmgrClient.initialize();
		final AccMgrWS stub = accmgrClient.getAccmgr();

		User user = stub.getUserByName("PSS2", FDUtils.getParticipantName());

		if (!ValidatePassword.validate(user.getPassword(), currentPW, newPW,
			confirmPW, false))
		{
			return null;
		}

		// if everything is right, update password.
		stub.changePassword(user.getId(), currentPW, newPW);

		FDUtils.addMsgInfo("Password updated");
		return null;
	}

	/**
	 * Cancel action.
	 * 
	 * @return the string
	 */
	public String cancelAction()
	{
		return "cancel";
	}

	/**
	 * Gets the current pw.
	 * 
	 * @return the current pw
	 */
	public String getCurrentPW()
	{
		return currentPW;
	}

	/**
	 * Sets the current pw.
	 * 
	 * @param currentPW the new current pw
	 */
	public void setCurrentPW(String currentPW)
	{
		this.currentPW = currentPW;
	}

	/**
	 * Gets the new pw.
	 * 
	 * @return the new pw
	 */
	public String getNewPW()
	{
		return newPW;
	}

	/**
	 * Sets the new pw.
	 * 
	 * @param newPW the new new pw
	 */
	public void setNewPW(String newPW)
	{
		this.newPW = newPW;
	}

	/**
	 * Gets the confirm pw.
	 * 
	 * @return the confirm pw
	 */
	public String getConfirmPW()
	{
		return confirmPW;
	}

	/**
	 * Sets the confirm pw.
	 * 
	 * @param confirmPW the new confirm pw
	 */
	public void setConfirmPW(String confirmPW)
	{
		this.confirmPW = confirmPW;
	}
}
