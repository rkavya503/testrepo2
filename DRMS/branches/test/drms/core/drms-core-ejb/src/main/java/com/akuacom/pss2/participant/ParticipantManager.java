/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.participant.ParticipantManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.query.ParticipantClientListFor20Ob;
import com.akuacom.pss2.subaccount.SubAccount;
import com.honeywell.dras.ssm.api.request.data.AggregatorEnrollmentData;
import com.honeywell.dras.ssm.api.request.data.CustomerEnrollmentData;

/**
 * The Interface ParticipantManager.
 */

public interface ParticipantManager {
    @Remote
    public interface R extends ParticipantManager {}
    @Local
    public interface L extends ParticipantManager {}

    
    
    int getParentParticipantCount();
    
    int getClientParticipantCount();
    
    
    void createPart(Participant p, char[] password,
            Program prog, ProgramEJB programEJB);
    
    void createParticipants(List<Participant> pList, List<char[]> passwords,
            Program prog);
    
    void createParticipant(Participant p, char[] password, String[] assignedProgramNames);
    
    /**
	 * Gets the signals for participant.
	 *
	 * @param participantName
	 *            the participant name
	 *
	 * @param isClient
     * @return the signals for participant
	 */
	List<ProgramSignal> getSignalsForParticipant(String participantName, boolean isClient);

	/**
	 * Gets the programs for participant as object.
	 *
	 * @param participantName
	 *            the participant name
	 *
	 * @param isClient
     * @return the programs for participant as object
	 */
	List<Program> getProgramsForParticipantAsObject(String participantName, boolean isClient);

	/**
	 * Gets the events for participant.
	 *
	 * @param participantName
	 *            the participant name
	 *
	 * @param isClient
     * @return the events for participant
	 */
	List<EventInfo> getEventsForParticipant(String participantName, boolean isClient);

	List<EventInfo> getEventsForParticipant(Program program, String participantName, boolean isClient);
	
	
	/**
	 * Sets the participant password - NOT IMPLEMENTED.
	 *
	 * @param participantName
	 *            the participant name
	 * @param passward
	 *            the passward
	 */
	void setParticipantPassword(String uuid, String passward);

	/**
	 * Update participant password - NOT IMPLEMENTED.
	 *
	 * @param participantName
	 *            the participant name
	 * @param oldPass
	 *            the old pass
	 * @param password
	 *            the password
	 */
	void updateParticipantPassword(String participantName,
			char[] oldPass, char[] password);

	
	// sce accounts
	/**
	 * Gets the sce accounts.
	 *
	 * @param participantName
	 *            the participant name
	 *
	 * @return the sce accounts
	 */
	List<SubAccount> getSubAccounts(String participantName);

	/**
	 * Creates the sce account.
	 *
	 * @param account
	 *            the account
	 * @param participantName
	 *            the participant name
	 */
	void createSubAccount(SubAccount account, String participantName);

	/**
	 * Gets the sce account.
	 *
	 * @param subAccountName
	 *            the sub account name
	 * @param participantName
	 *            the participant name
	 *
	 * @return the sce account
	 */
	SubAccount getSubAccount(String subAccountName,
			String participantName);

	/**
	 * Removes the sce account.
	 *
	 * @param subAccountName
	 *            the sub account name
	 * @param participantName
	 *            the participant name
	 */
	void removeSubAccount(String subAccountName, String participantName);

	/**
	 * Update sce accounts.
	 *
	 * @param accounts
	 *            the accounts
	 * @param participantName
	 *            the participant name
	 */
	void updateSubAccounts(List<SubAccount> accounts,
			String participantName);

	// web method
	// TODO: use List<Participant>
	/**
	 * Creates the participant account.
	 *
	 * @param PAlist
	 *            the p alist
	 */
	void createParticipantAccount(List<ParticipantExtInfo> PAlist);

	/**
	 * Gets the participants.
	 *
	 * @return the participants
	 */
	List<String> getParticipants();

	/**
	 * Gets the participant.
	 *
	 * @param participantName
	 *            the participant name
	 *
	 * @return the participant
	 */
	Participant getParticipant(String participantName);
	
	List<Participant> getAllParticipants();

	/**
	 * Gets the participant by account.
	 *
	 * @param accountNumber
	 *            the account number
	 *
	 * @return the participant by account
	 */
	Participant getParticipantByAccount(String accountNumber) ;

	/**
	 * Removes the participant.
	 *
	 * @param participantName
	 *            the participant name
	 */
	void removeParticipant(String participantName);

	// web method
	/**
	 * Removes the participants by account number.
	 *
	 * @param accountNumberList
	 *            the account number list
	 */
	void removeParticipantsByAccountNumber(List<String> accountNumberList);

	// web method
	// TODO: rename to updateParticipant, and change to List<Participant>
	/**
	 * Update participant account.
	 *
	 * @param PAlist
	 *            the p alist
	 */
	void updateParticipantAccount(List<ParticipantExtInfo> PAlist);

	/**
	 * Removes the participant from all programs.
	 *
	 * @param participantName
	 *            the participant name
	 */
	void removeParticipantFromAllPrograms(String participantName);

	/**
	 * Gets the programs for participant.
	 *
	 * @param participantName
	 *            the participant name
	 *
	 * @param isClient
     * @return the programs for participant
	 */
	List<String> getProgramsForParticipant(String participantName, boolean isClient);

	/**
	 * Gets the programs for participant as string.
	 *
	 * @param participantName
	 *            the participant name
	 *
	 * @param isClient
     * @return the programs for participant as string
	 */
	String getProgramsForParticipantAsString(String participantName, boolean isClient);

	/*
	 * comm log
	 */

	// TODO: gets the event that is driving a signal
	/**
	 * Gets the participant signal active event.
	 *
	 * @param programName
	 *            the program name
	 * @param participantName
	 *            the participant name
	 * @param isClient
     *@param signalName
     *            the signal name
     * @return the participant signal active event
	 */
	//Event getParticipantSignalActiveEvent(String programName,
    //                                           String participantName, boolean isClient, String signalName);

	// TODO: comm log computation
	/**
	 * Gets the participant status.
	 *
	 * @param participantName
	 *            the participant name
	 *
	 * @param isClient
     * @return the participant status
	 */
	ClientStatus getParticipantStatus(String participantName, boolean isClient);

    /**
	 * Search participants.
	 *
	 * @param programNames
	 *            the program names
	 * @param searchHandler
	 *            the search handler
	 *            the load program
	 *
	 * @return the search handler
	 */
	SearchHandler searchParticipants(List<String> programNames,
			SearchHandler searchHandler);

	/**
	 * Find participants by accounts.
	 *
	 * @param accountIDs
	 *            the account i ds
	 *
	 * @return the list< participant impl>
	 */
	List<Participant> getParticipantsByAccounts(List<String> accountIDs);

    /**
	 * Find participants for update status loop.
	 *
	 * @return the list< participant impl>
	 */

    List<Participant> getAllClients();
    
	List<Participant> findParticipantsForUpdateStatusLoop();

	Participant setParticipantStatus(Participant part, ClientStatus status);

    SearchHandler searchParticipants(List<String> programNames,
			List<String> accountIDs, SearchHandler searchHandler);

    List<String> getClientNamesByParticipant(String partName);

    List<EventParticipant> getEventParticipantsByPartNames(List<String> participantNames, boolean isClient) ;

    //Participant createParticipant(Participant p, char[] password, String[] assignedProgramNames);
    

    void updateParticipant(Participant p, String[] assigniungProgramNames);
    
    void updateParticipant(Participant participant);
    
    void updateParticipant(ParticipantPerf participant);
    
    void updateParticipantContact(ParticipantContact participantContact);    
    
    List<ProgramParticipant> getProgramParticpantsForClient(String clientName);
    
    List<ProgramParticipant> getProgramParticpantsForClientConfig(String participantName,boolean isClient);

    SubAccount getSubAccount(String uuid) ;

    Participant getParticipant(String particitpantName, boolean isClient);

    List<Participant> findParticipantsByProgramName(String progName)
			throws AppServiceException;
    
    List<String> getParticipantsForProgram(String programName);

    void createExternalContacts(String programName, 
    	Map<String,List<ParticipantContact>> externalContacts);
    
	/**
	 * Returns whether the participant is online.
	 * The participant is considered online if all of its clients are online; otherwise, it is offline.
	 * @param participantName
	 * @return true if the participant is online
	 */
	public Boolean isParticipantOnline(String participantName);

	Participant getByAccount(String accountNumber, boolean client);
	
	public Participant getParticipantOnly(String name, boolean client);
	
	public Participant getParticipantAndEventsOnly(String name, boolean client);
	
	public List<Participant> getClientsAndEvents(String parent);
	
	public Participant getParticipantAndProgramParticipantsOnly(String name, boolean client);
	
	Participant getParticipantOnly(String participantName);

	Participant getParticipantAndShedsOnly(String name);
	
    void persistParticipantContact(ParticipantContact participantContact, String uuid, List<String> participant_uuids);
    
    void removeParticipantContact(String address, String desc, List<String> participant_uuids);
    
    void removeParticipantContact(List<ParticipantContact> participantContacts, List<String> participant_uuids);
    
    void updateParticipantContact(ParticipantContact participantContact, String orig_type, String orig_address, String orig_desc, List<String> participant_uuids);
    
    /**
	 * Find participants by accounts.
	 *
	 * @param accountIDs
	 *            the account i ds
	 *
	 * @return the list< participant impl>
	 */
	List<Participant> getParticipantsBySecondaryAccounts(List<String> accountIDs);
	 void cascadeRemoveParticipantContact(List<ParticipantContact> participantContacts, String rootUuid, List<String> participant_uuids);
	 
	 void cascadeUpdateParticipantContact(ParticipantContact participantContact, String orig_type,
				String orig_address, String orig_desc,String rootUuid,
				List<String> participant_uuids);
	 List<Participant> findParticipantsWithEventParticipantsByAccounts(List<String> accounts);
	 
	/**
	 * Gets the participant by Application Id for DBP program.
	 *
	 * @param applicationId
	 *            the applicationId
	 *
	 * @return the participant by applicationId
	 */
	Participant getParticipantByApplicationId(String applicationId) ;
    void updateMeterName(String programName, Collection<String> accountNumbers, Map<String, String> meterNames);
    
    public List<String> enrollAggregatorInDras(AggregatorEnrollmentData aggregatorEnrollmentData);
    public void enrollAggregatorClientInDras(AggregatorEnrollmentData aggregatorEnrollmentData);
    public void enrollAggregatorUserInDras(AggregatorEnrollmentData aggregatorEnrollmentData);
    public void removeCustomerCreatedOnAggregator(List<String> participantList);
	public void createAggregatorHierarchy(AggregatorEnrollmentData aggEnrollmentData);
	public Participant getParticipantByClient(String participantName);
	Participant getParticipantForProgramName(String participantName,String programName);
	Map<String,ParticipantClientListFor20Ob> getParticipantCLientListFor20Ob(ArrayList<String> groupValue);
}
