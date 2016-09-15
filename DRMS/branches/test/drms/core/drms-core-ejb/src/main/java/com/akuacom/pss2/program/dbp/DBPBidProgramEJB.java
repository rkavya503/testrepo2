/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.dbp.DBPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.event.Event;

/**
 * The Interface DBPProgramEJB.
 */

public interface DBPBidProgramEJB extends DBPProgramEJB {
    @Remote
    public interface R extends DBPBidProgramEJB {   }
    @Local
    public interface L extends DBPBidProgramEJB {   }
    
	/**
	 * Sets the bid status.
	 * 
	 * @param eventParticipantAcceptedList
	 *            the event participant accepted list
	 * @param eventParticipantRejectedList
	 *            the event participant rejected list
	 */
	void setBidStatus(List<Bid> eventParticipantAcceptedList,
			List<Bid> eventParticipantRejectedList);

	/**
	 * Sets the bid accepted.
	 * 
	 * @param programName
	 *            the program name
	 * @param eventName
	 *            the event name
	 * @param participantName
	 *            the participant name
	 * @param accepted
	 *            the accepted
	 */
	void setBidAccepted(String programName, String eventName,
			String participantName, boolean isClient, boolean accepted);

	/**
	 * Next state.
	 * 
	 * @param programName
	 *            the program name
	 * @param event
	 *            the event
	 */
	void nextState(String programName, DBPEvent event);

	boolean sendBidsOut(DBPEvent event);
	
	void sendDBPEnvoyNotification(Event event, String verb, boolean showClientStatus, boolean isRevision);
	
}
