/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.participant.Participant;

import java.util.List;
import java.util.Set;


/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface ProgramParticipantEAO extends ProgramUserBaseEAO{
    @Remote
    public interface R extends ProgramParticipantEAO {}
    @Local
    public interface L extends ProgramParticipantEAO {}

    void addChild(ProgramParticipant parent, ProgramParticipant child);

    Set<ProgramParticipant> getFlatDescendants(ProgramParticipant parent);

    void removeChildren(ProgramParticipant parent, Set<ProgramParticipant> children);

    void removeParent(ProgramParticipant child);

    Set<ProgramParticipant> getDescendants(ProgramParticipant parent);

    ProgramParticipant getAncestor(ProgramParticipant child);

    ProgramParticipant getRoot(ProgramParticipant child);

    ProgramParticipant getCommonAncestor(ProgramParticipant pp1, ProgramParticipant pp2);

    boolean isDescendant(ProgramParticipant parent, ProgramParticipant child);

    boolean isAncestor(ProgramParticipant parent, ProgramParticipant child);
    
    void mergeAll(List<ProgramParticipant> ppList);

	List<String> findClientProgram(String parentName);
	
	List<TreeNodeVo> getChildren(String participantName, String programName);
	
	List<Participant> getDescendantClients(String participantName, String programName);
	
	List<Participant> getSiblingClients(String participantName, String programName);
	
	List<ProgramParticipant> getProgramParticipantsByProgram(String programName);
	
	Set<ProgramParticipant> getDescendantsForSpecifiedProgram(ProgramParticipant parent, String programName);
	
	List<ProgramParticipant> findProgramClientByParticipantName(String participantName);
}