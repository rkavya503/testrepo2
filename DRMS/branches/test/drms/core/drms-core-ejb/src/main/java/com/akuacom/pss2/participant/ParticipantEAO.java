/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.DataAccess.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.query.ParticipantClientListFor20Ob;
import com.akuacom.pss2.util.EventState;

/**
 * Remote stateless session bean that acts as a EAO layer for Participant
 * access.
 */

public interface ParticipantEAO extends UserEAO {
    @Remote
    public interface R extends ParticipantEAO {}
    @Local
    public interface L extends ParticipantEAO {}

    // participants
    
    /**
     * Fetches participant row count by program and client
     * @param programName program name 
     * @param isClient true if is client
     * @return row count
     */
    Integer getParticipantRowCountByProgramAndClient(String programName, boolean isClient);


    Integer getParticipantRowCountByProgramAndClientAndState(String programName, boolean isClient);

    /**
     * Creates a participant. Checks that participant does not already exist.
     * 
     * @param participant
     *            the participant
     * @param password
     *            the password
     */
    Participant createParticipant(Participant participant);

    /**
     * Gets the participant.
     * 
     * @param participantName
     *            the participant name
     * 
     * @return the participant
     */
    Participant getParticipant(String participantName);

    /**
     * Gets the participant and left join fetches eventParticpants,programParticipants
     * 
     * @param participantName
     *            the participant name
     * 
     * @return the participant
     */
    Participant getParticipantLJF(String participantName);

    /**
     * Gets the participant by account.
     * 
     * @param accountNumber
     *            the account number
     * 
     * @return the participant by account
     */
    Participant getParticipantByAccount(String accountNumber);

    /**
     * Gets all the participants sorted by name.
     * 
     * @return the participants
     */
    List<String> getParticipants();

    /**
     * Gets the all participants.
     * 
     * @return the all participants
     */
    List<Participant> getAllParticipants();

    List<ParticipantPerf> getAllParticipantPerfs();

    /**
     * Gets all the participants sorted.
     * 
     * @param sort
     *            the sort
     * 
     * @return the participants
     * 
     * @see Participant Participant sort constants
     */
    List<String> getParticipants(int sort);

    /**
     * Find participants for update status loop.
     * 
     * @return the list< participant impl>
     */
    List<Participant> findParticipantsForUpdateStatusLoop();

    /**
     * Find participants by program name.
     * 
     * @param progName
     *            the prog name
     * 
     * @return the list< participant impl>
     * 
     * @throws AppServiceException
     *             the app service exception
     */
    List<Participant> findParticipantsByProgramName(String progName)
            throws AppServiceException;

    /**
     * Find participants by accounts.
     * 
     * @param accountIDs
     *            the account i ds
     * 
     * @return the list< participant impl>
     */
    List<Participant> findParticipantsBySecondaryAccounts(List<String> accountIDs);

    /**
     * Removes the participant from PSS2 and the account manager.
     * 
     * @param participantName
     *            the participant name
     * @return participant
     */
    void removeParticipant(String participantName);

    /**
     * Gets the participant status (online or offline).
     * 
     * @param participantName
     *            the participant name
     * 
     * @return the participant status
     */
    ClientStatus getParticipantStatus(String participantName);

    /**
     * Sets the participant status.
     * 
     * @param participantName
     *            the participant name
     * @param status
     * @param isClient
     */
    Participant setParticipantStatus(Participant part, ClientStatus status);

    SearchHandler searchParticipants(List<String> programNames,
            List<String> accountIDs, SearchHandler searchHandler)
            throws AppServiceException;

    // List<EventState> getParticipantEventStates(Participant part);

    String parseSignalString(EventState eventState, String signalName);

    List<EventParticipant> findEventParticipants(String participantName);

    List<EventParticipant> findEventParticipants(List<String> names);

    //List<EventParticipant> findEventParticipantsByKeys(String eventName,
     //       String name);

    void removeParticipantFromAllEvents(String name);
    
    
	/**
	 * Get participants
	 * 
	 * @param partNames
	 *            participant names
	 * @param accounts
	 *            participant account numbers
	 * @param programNames
	 *            programs names
	 * @param startPage
	 *            start page
	 * @param pageSize
	 *            page size
	 * @return
	 */
	List<Participant> getParticipants(List<String> partNames,
			List<String> accounts, List<String> programNames,
			Integer startPage, Integer pageSize, String orderBy, String order);


	/**
	 * Get participants by page and by participant name
	 * 
	 * @param start
	 * @param pageSize
	 * @param filterContent
	 * @param orderBy
	 * @param order
	 * @return
	 */
	List<Participant> getParticipants(Integer start, Integer pageSize,
            String filterContent, String filterColumn, String orderBy, String order);
	
	
	/**
	 * Get participant row count
	 * 
	 * @param partNames
	 * @param accounts
	 * @param programNames
	 * @return
	 */
	Integer getParticipantRowCount(List<String> partNames,
			List<String> accounts, List<String> programNames);

	/**
	 * Get participant row count
	 * 
	 * @param filterContent
	 * @param filterColumn
	 * @return
	 */
	Integer getParticipantRowCount(String filterContent, String filterColumn);
	
	/**
	 * Returns whether the participant is online.
	 * The participant is considered online if all of its clients are online; otherwise, it is offline.
	 * @param participantName
	 * @return true if the participant is online
	 */
	public Boolean isParticipantOnline(String participantName);


	List<Participant> findNotInAccounts(List<String> accounts);


	List<String> findUtilityProgramNamesByParticipant(String participantName,
			boolean isClient);


	List<Participant> findInAccounts(Collection<String> accounts);


	List<String> getInactiveAccount(List<String> accounts);
	
	List<Participant> findParticipantsWithEventParticipantsByAccounts(List<String> accounts);
	
	 /**
     * Gets the participant by application id.
     * 
     * @param applicationId
     *            the applicationId 
     * @return the participant by applicationId for DBP 
     */
    Participant getParticipantByApplicationId(String applicationId);
    
    Participant getParticipantByClient(String participantName);
    Participant getParticipantForProgramNameLJF(String participantName,String programName);
    Map<String,ParticipantClientListFor20Ob>getParticipantCLientListFor20Ob(ArrayList<String> clientName);
}
