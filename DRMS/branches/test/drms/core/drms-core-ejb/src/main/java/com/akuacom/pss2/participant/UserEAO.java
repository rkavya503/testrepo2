/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.DataAccess.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.util.EventState;

/**
 * Remote stateless session bean that acts as a EAO layer for Participant
 * access.
 */

@Deprecated
public interface UserEAO extends ParticipantGenEAO {
    @Remote
    public interface R extends UserEAO {}
    @Local
    public interface L extends UserEAO {}

    boolean checkAccount(String accountNumber);

    Participant getByAccount(String accountNumber, boolean isClient);

    List<Participant> getByUserType(boolean isClient);

    void updateParticipant(String participantName, Participant participant);

    void updateParticipant(Participant participant);

    void updateParticipant(ParticipantPerf participant);

    void remove(String participantName, boolean isClient);

    List<Participant> findParticipantsByAccounts(List<String> accountIDs);

    List<Participant> findParticipantsByProgramName(String progName,
            boolean isClient) throws AppServiceException;

    SearchHandler searchParticipants(List<String> programNames,
            List<String> accountIDs, SearchHandler searchHandler)
            throws AppServiceException;

    String parseSignalString(EventState eventState, String signalName);

    List<ProgramSignal> getNewSignalsInProgram(String programName);

    List<EventParticipant> findCurrentEventOptouts(String participantName, boolean isClient);

    List<EventParticipant> findEventParticipants(String participantName,
            boolean isClient);

    List<EventParticipant> findEventParticipantsByPartUUID(String uuid);

    List<EventParticipant> findEventParticipants(List<String> names,
            boolean isClient);

    List<String> findProgramNamesByName(String partName, boolean isClient);

    List<ProgramParticipant> findAllProgramParticipants(String name,
            boolean isClient);

    List<ProgramParticipant> findAllProgramParticipantsConfig(String name,
            boolean isClient);

    
    //@Deprecated
    //ParticipantPerf findPerfByName(@Deprecated String name, @Deprecated boolean isClient);

    Participant findByName(String name, boolean isClient);

    String getDefaultSignalValueAsString(String signalName);

    List<Participant> findUsersByProgramName(String progName)
            throws AppServiceException;

    List<Participant> getAll();

    int getUserDataSetSize(boolean isClient);
    
    List<Participant> getUserPage(int start, int pageSize, String filter,
            String filterByColumn, String sort, boolean isClient);
    
    long countByParticipantNameAndClient(String participantName, boolean client);
    boolean existsQ(String participantName, boolean client);
}