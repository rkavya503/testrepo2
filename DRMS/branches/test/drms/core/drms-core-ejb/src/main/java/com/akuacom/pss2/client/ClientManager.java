
/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.client.ClientManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.cache.ConfirmationResult;
import com.akuacom.pss2.event.ClientConversationState;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.timer.TimerManager;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.OperationModeValue;
import com.akuacom.pss2.util.PriceSchedule;

/**
 * The Interface ClientManager.
 */
public interface ClientManager extends TimerManager {
    @Remote
    public interface R extends ClientManager {}
    @Local
    public interface L extends ClientManager {}

	
    
    
    /**
     * Creates a bunch of clients
     * @param clients list of clients
     * @param passwords list of passwords - indexes should match
     */
    public void createClient(List<Participant> clients, List<char[]> passwords, Program program);
    
    
	/**
	 * Creates the client.
	 * 
	 * @param p
	 *            the p
	 * @param password
	 *            the password
	 */
	void createClient(Participant p, char[] password);

	/**
	 * Gets the clients - NOT IMPLEMENTED.
	 * 
	 * @param participantName
	 *            the participant name
	 * 
	 * @return the clients
	 */
	List<Participant> getClients(String participantName);

	/**
	 * Gets the client - NOT IMPLEMENTED.
	 * 
	 * @param clientName
	 *            the client name
	 *
	 * @return the client
	 */
    Participant getClient(String clientName);
    
    Participant getClientLJF(String clientName);
	
	/**
	 * Removes the client.
	 * 
	 * @param clientName
	 *            the client name
	 */
	void removeClient(String clientName);

	/**
	 * Update client 
	 * 
	 * @param client
	 *            the client
	 */
	void updateClient(Participant client);

	// TODO: change to recordClientLastPrice and change to clientName
	/**
	 * Sets the participant last price.
	 * 
	 * @param participantName
	 *            the participant name
	 * @param lastPrice
	 *            the last price
	 */
	void setParticipantLastPrice(String participantName, double lastPrice);

	// change to recordClientLastPrice and change to clientName
	/**
	 * Gets the participant last price.
	 * 
	 * @param participantName
	 *            the participant name
	 * 
	 * @return the participant last price
	 */
	double getParticipantLastPrice(String participantName);

	// TODO: these are here just to get transaction capability. we can either
	// move these
	// up to the web service code and surround with a transaction, or return a
	// object and have the ws code convert that to the xml.
	//
	/**
	 * Gets the price.
	 * 
	 * @param participantName
	 *            the participant name
	 * @param lastPrice
	 *            the last price
	 * 
	 * @return the price
	 */
	double getPrice(String participantName, double lastPrice);

	/**
	 * Checks if is aPE event pending.
	 * 
	 * @param participantName
	 *            the participant name
	 * 
	 * @return true, if is aPE event pending
	 */
	boolean isAPEEventPending(String participantName);

	/**
	 * Gets the price schedule.
	 * 
	 * @param participantName
	 *            the participant name
	 * @param lastPriceSchedule
	 *            the last price schedule
	 * 
	 * @return the price schedule
	 */
	@Deprecated
	PriceSchedule getPriceSchedule(String participantName,
			PriceSchedule lastPriceSchedule);

	/**
	 * Update participant communications.
	 * 
	 * @param participantName
	 *            the participant name
	 * @param timestamp
	 *            the timestamp
	 * @param success
	 *            the success
	 */
	void updateParticipantCommunications(String participantName,
			Date timestamp, boolean success, ClientConversationState convState);

	void updateParticipantCommunications(String participantName,
			Date timestamp, boolean success, String eventState, String operationModeValue);
	
	/**
	 * Initialize the clients. Should be called once at startup.
	 */
	void initialize();

	void addClientToProgram(String clientName, String programName,Participant client);
	
	void configureClientManualSignals(String clientName);
	
    SearchHandler searchClients(SearchHandler searchHandler, String participantName);
    
    
    ConfirmationResult isConfirmationOk(String participantName, long eventStateID);
    
    List<org.openadr.dras.eventstate.EventState> getClientDrasEventStates(String participantName);

    List<EventState> getClientEventStates(String name, boolean update);
    
    List<EventState> getClientEventStates(Participant p);
    
    List<EventState> getClientEventStates(String name, boolean update, Map<String,Date> nearTimeMap);
    
    List<EventState> getClientEventStates(Participant client, Map<String,Date> nearTimeMap); 
    
	OperationModeValue getOperationModeValue(Participant client);

    List<String> getClientEventNames(String clientName);

    ClientStatus getClientStatus(String name) ;

    List<Participant> findClientsByAccounts(List<String> accountIDs);

    void updateExpireManualControlForClientTimer(String clientName, 
    		Date expireTime, boolean addTimer) throws IllegalArgumentException;

    long countParticipants();
    
    /**
     * Takes a participant name and an internal EventState object
     * and creates an outbound EventState (which is TOTALLY a different thing)
     * suitable for marshaling to XML and sending to a client  
     * 
     * @param participantName The name of a participant
     * @param eventState  The kind of EventState that doesn't turn into XML
     * @return The kind of EventState that does turn into XML.
     */
    org.openadr.dras.eventstate.EventState parseEventState(
    	String participantName, com.akuacom.pss2.util.EventState eventState);
    
	void pushClientEventState(Participant theClient);
	

	//FINE GRAINED API
	List<Participant> getClientsAllInfoByParent(String parentName);
	List<Participant> getClientsAllInfoExceptContactsByParent(String parentName);
	Participant getClientOnly(String name);
	Participant getClientWithContacts(String clientName);
	Participant getClientWithEvents(String clientName);
	Participant getClientWithManualSignals(String clientName);
    List<String> getClientNamesByParticipant(String partName);

    /**
     * Creates the event state.
     *
     * @param ent the ent
     */
    void createClientConversationState(EventState ent);

    void putClientConversationState(ClientConversationState state);

    /**
     * Gets the event state.
     *
     * @param eventStateId the event state id
     *
     * @return the event state
     */
    ClientConversationState getClientConversationState(long eventStateId);

    /**
     * Deletes the EventState
     *
     * @param eventStateId id
     */
    void removeClientConversationState(long eventStateId);


	List<EventState> getAllEventStatesForClient(String name, boolean update,
			Map<String, Date> nearTimeMap);


	List<org.openadr.dras.eventstate.EventState> getAllDrasEventStateForClient(
			String participantName);

	List<org.openadr.dras.eventstate.EventState> getAllDrasEventStateForParentParticipant(
			String participantName);
	
	List<EventState> getAllEventStatesForParentParticipant(String name,
			boolean update, Map<String, Date> nearTimeMap);
			
	public Participant getClientLJFByName(String clientName);
	public String getClientManualSignalValueAsString(Participant client, String signalName);
}