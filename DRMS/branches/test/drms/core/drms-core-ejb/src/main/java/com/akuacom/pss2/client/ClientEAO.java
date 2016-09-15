/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.DataAccess.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.client;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantESPerf;
import com.akuacom.pss2.participant.UserEAO;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.util.EventState;

/**
 * Remote stateless session bean that acts as a EAO layer for Participant
 * access.
 */

public interface ClientEAO extends UserEAO {
    @Remote
    public interface R extends ClientEAO {}
    @Local
    public interface L extends ClientEAO {}

	Participant createClient(Participant client);

    Participant getClient(String name);

    Participant getClientByAccount(String accountNumber);

    List<String> getClients();
    
    public ParticipantESPerf getClientESPerf(String name);

    SearchHandler searchClientsByParticipant(SearchHandler searchHandler, String participantName, boolean isClient);

    List<String> getClients(int sort);

    List<Participant> getAllClients();
    long countParticipants();

    List<String> getClientNamesByParticipant(String partName);

    List<Participant> getClientsByParticipant(String partName);

    void removeClient(String name);

    double getClientLastPrice(String name);

    void setClientLastPrice(String name, double lastPrice);

    ClientStatus getClientStatus(String name);

    void setClientStatus(String name, ClientStatus status);

    List<Participant> findClientsByAccounts(List<String> accountIDs);

    List<Participant> findClientsByProgramName(String progName)
			throws AppServiceException;

    Participant getClientByName(String name);

    List<Participant> findClientsForUpdateStatusLoop();

    List<EventParticipant> findEventParticipants(String name) ;

    List<EventParticipant> findEventParticipants(List<String> names) ;

    List<ProgramParticipant> findAllProgramParticipantsForClient(String clientName);

    //List<EventParticipant> findEventParticipantsByKeys(String eventName, String name);

    void removeClientFromAllEvents(String name) ;
    
	String getClientManualSignalValueAsString(Participant participant,
		String signalName);
		
	/**
	 * Get clients
	 * 
	 * @param filterContent
	 * @param filterColumn
	 * @param startPage
	 * @param pageSize
	 * @param orderBy
	 * @param order
	 * @return
	 */
	public List<Participant> getParticipants(String filterContent,
			String filterColumn, Integer startPage, Integer pageSize,
			String orderBy, String order);
	
	/**
	 * Get clients
	 * 
	 * @param clientNames
	 * @param participantNames
	 * @param programNames
	 * @param clientTypes
	 * @param eventStatus
	 * @param modes
	 * @param clientStatus
	 * @param startPage
	 * @param pageSize
	 * @param orderBy
	 * @param order
	 * @return
	 */
	List<Participant> getParticipants(List<String> clientNames,
			List<String> participantNames, List<String> programNames,
			List<String> clientTypes, List<String> clientStatus, Integer startPage,
			Integer pageSize, String orderBy, String order);

	/**
	 * Get client row count by single query condition
	 * 
	 * @param filterContent
	 * @param filterColumn
	 * @return
	 */
	public Integer getParticipantRowCount(String filterContent,
			String filterColumn);
	 
	/**
	 * Get client row count by multiple query condition
	 * 
	 * @param clientNames
	 * @param participantNames
	 * @param programNames
	 * @param clientTypes
	 * @param eventStatus
	 * @param modes
	 * @param clientStatus
	 * @return
	 */
	public Integer getParticipantRowCount(List<String> clientNames,
			List<String> participantNames, List<String> programNames,
			List<String> clientTypes, List<String> clientStatus);
	
	
	//FINE GRAINED API
	public List<Participant> getClientsAllInfoByParent(String parentName);
	public List<Participant> getClientsAllInfoExceptContactsByParent(String parentName);
	Participant findByClientLJF(String clientName, boolean b);
	
}

