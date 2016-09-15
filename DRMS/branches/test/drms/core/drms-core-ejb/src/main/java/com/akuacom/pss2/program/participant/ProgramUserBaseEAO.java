/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface ProgramUserBaseEAO extends BaseEAO<ProgramParticipant> {
    @Remote
    public interface R extends ProgramUserBaseEAO {}

    @Local
    public interface L extends ProgramUserBaseEAO {}

    Boolean isProgramParticipantInDB(String progName, String partName, boolean isClient);
    
    ProgramParticipant updateProgramParticipant(ProgramParticipant value);

    ProgramParticipant createProgramParticipant(String programName,
            String participantName, boolean isClient);

    void createProgramParticipants(String programName,
            List<Participant> participant, boolean isClient);
    
    void createProgramParticipant(ProgramParticipant pp);

    void deleteProgramParticipant(ProgramParticipant pp);

    List<ProgramParticipant> findProgramParticipantsByProgramName(
            String progName) throws AppServiceException;
    
    public List<ProgramParticipant> findAllProgramParticipantsByProgramName(String progName) throws AppServiceException;
    
    public List<ProgramParticipant> findProgramClientByProgramAndParticipant(String progName, List<String> participantNames);

    List<ProgramParticipant> findByProgramAndParentParticipantForClient(java.lang.String programName, String parentParticipantName, boolean client);
    
    ProgramParticipant findProgramParticipantsByProgramNameAndPartName(
            String progName, String partName) throws AppServiceException;

    ProgramParticipant findClientProgramParticipantsByProgramNameAndPartName(
            String progName, String partName) throws AppServiceException;

    ProgramParticipant findProgramParticipantsByProgramNameAndPartName(
            String progName, String partName, int state)
            throws AppServiceException;

    List<ProgramParticipant> findProgramParticipantsByProgramNames(
            String progName1, String progName2) throws AppServiceException;

    void removeParticipantFromProgram(String programName, String participantName);

    void removeClientParticipantFromProgram(String programName,
            String participantName);

    List<ProgramParticipant> findProgramParticipantsByParticipant(
            Participant participant);
    
    List<ProgramParticipant> findOptedOutProgramParticipants();    
    
    ProgramParticipant findRtpStrategyByProgAndPartiForClient(String programName, String participantName, boolean client);
}