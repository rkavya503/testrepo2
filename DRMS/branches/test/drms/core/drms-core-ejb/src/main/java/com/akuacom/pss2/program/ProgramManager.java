/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.ProgramManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.matrix.ProgramMatrix;
import com.akuacom.pss2.program.matrix.ProgramMatrixTrig;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.TreeNodeVo;
import com.akuacom.pss2.program.rtp.RTPPrice;
import com.akuacom.pss2.season.SeasonConfig;
import com.akuacom.pss2.signal.SignalDef;
import java.util.HashMap;

/**
 * Business object facade for working with programs.
 */

public interface ProgramManager {
    @Remote
    public interface R extends ProgramManager {}
    @Local
    public interface L extends ProgramManager {}

    /**
     * Get program class by name
     * @param programName program name
     * @return program class
     */

    public String getProgramClassByName(String programName);
    

    /**
     * Get program class  name
     * @param programName program name
     * @return program class name
     */

    public String getProgramClassName(String programName);

    
    /**
     * Checks if bidding is anbled by program name
     * @param programName program name
     * @return
     */

    public Boolean isDrasBiddingByProgramName(String programName);
    
    /**
     * Fetches participant row count by program and client
     * @param programName program name 
     * @param isClient true if is client
     * @return row count
     */
    Integer getParticipantRowCountByProgramAndClient(String programName, boolean isClient);

    Integer getParticipantRowCountByProgramAndClientAndState(String programName, boolean isClient);
    
    /**
     * Create a program. {@link Program#getProgramName()} and
     * {@link Program#getUtilityProgramName()} must be unique
     * 
     * -- NOTE THAT THIS METHOD IS PRESENTLY NOT IMPLEMENTED. ---
     * 
     * @param program
     *            the Program to create
     * 
     * @throws EJBException
     *             when a unique constraint fails
     */
    Program createProgram(Program program) throws AppServiceException;

    /**
     * Delivers the ProgramEJB to all who need it. TODO: ProgramEJB will change
     * and this method may be removed or moved to a more appropriate "Factory"
     * if it becomes a business POJO.
     * 
     * @param programName
     * @return
     * @throws EJBException
     */
    ProgramEJB lookupProgramBean(String programName) throws EJBException;

    public ProgramEJB lookupProgramBean(Program program) throws EJBException;
    
    ProgramEJB lookupProgramBeanFromClassName(String classNameBase) throws EJBException;
    
    /**
     * Get a list all programs names (keys).
     * 
     * @return the list of program names
     */
    List<String> getPrograms();

    
    public String getUiScheduleEventString(String programName);
    
    
    /**
     * Get a program given its name (key).
     * 
     * @param programName
     *            the program name
     * 
     * @return the program
     * 
     * @throws EJBException
     *             when program name (key) not found
     */
    @Deprecated //too expensive, use fine-grained API instead
    Program getProgram(String programName);
    
    
    
    /**
     * Utility program name is the name that utility information systems (via
     * the OpenADR UtilityOperator interface) use to identify the program.
     * 
     * @param utilityProgramName
     *            the utility program name
     * 
     * @return the program from utility program name
     */
    Program getProgramFromUtilityProgramName(String utilityProgramName);

    // TODO: will exist when framework gets integrated
    /**
     * Update program.
     * 
     * @param program
     *            the program
     */
    void updateProgram(Program program);

    /**
     * Removes the program.
     * 
     * @param programName
     *            the program name
     */
    void removeProgram(String programName);

    HashMap<String, Integer> getProgramPriority();

    // TODO: change name to getProgramEvents()
    /**
     * Get a list of events that were issued against a program.
     * 
     * @param programName
     *            the program name
     * 
     * @return the list of events
     */
    List<EventInfo> getEventsForProgram(String programName);
    
    List<EventInfo> getEventsForProgram(Program program);

    // TODO: where is the removeProgramParticipant?

    /**
     * Save program matrix trig.
     * 
     * @param matrix
     *            the matrix
     */
    void saveProgramMatrixTrig(List<ProgramMatrix> matrix);

    /**
     * Gets the program matrix trig.
     * 
     * @return the program matrix trig
     */
    ProgramMatrixTrig getProgramMatrixTrig();

    /**
     * Get the rule variables supported by the program
     * 
     * @param programName
     *            program name
     * @return the list of rule variables
     */
    List<String> getProgramRuleVariables(String programName);

    /**
     * Save season config.
     * 
     * @param value
     *            the value
     * @param programName
     *            the program name
     */
    void saveSeasonConfig(Set<SeasonConfig> value, String programName);

    List<SeasonConfig> findSeasonConfigs(String programName);
    
    List<SeasonConfig> findSeasonConfigsByProgramId(String programUUID);

    String getSeason(String programName, Date date);

    List<SignalDef> findSignals(String programName);
    
    List<SignalDef> findSignalsPerf(String programName);
    

    void setProgram(Program program);

    void setRealTimePrice(RTPPrice price);

    RTPPrice getLastRealTimePrice(String programName);

    List<RTPPrice> getCurrentRealTimePrices(String programName);

    Program copyProgram(String uuid, String newProgramName);

    ProgramPerf getProgramPerf(String programName);
    
    List<Program> getProgramsAsPrograms();
    
    List<Program> getProgramsAsProgramsByPriorities();
    
    public Program refreshProgram(String programName);
    
    int getNextPriority();
    
    List<Program> findProgramsByProgramClass(List<String> programClasses) throws AppServiceException;
    
    List<Program> findProgramsByLongProgramName(String longProgramName) throws AppServiceException;
    
    Set<EventParticipant> filterEventParticipants(String programName);
    
    Set<EventParticipant> filterEventParticipants(Program program);
    
    Set<EventParticipant> filterEventParticipants(Set<EventParticipant> eventParticipants, Program program,
    													EventTiming eventTiming);
    Set<EventParticipant> filterEventParticipants(Set<EventParticipant> eventParticipants, Program programWithParticipants,
    													EventTiming eventTiming, boolean eventCreation);
    
    List<Program> getProgramsAsProgramsLazy();
    
    List<Contact> getOperatorContacts(String programName);
    
    
    //fine grained API
    /**
     * get program only, no referenced objects 
     * @param programName
     * @return
     */
    Program getProgramOnly(String programName);
//    Program getProgramWithContacts(String  programName);
	Program getProgramWithLoadBid(String programName);
	Program getProgramWithParticipants(String programName);
	List<Program> getProgramsWithParticipants(List<String> programNames);
    Program getProgramWithSignals(String programName);
    Program getProgramWithRules(String programName);
    Program getProgramWithAllConfig(String programName);
    Program getProgramWithParticipantsAndPRules(String programName);
    Program getProgramWithBidAndProgramParticipantsAndPRules(String programName);
    
    List<Program> getAllPrograms();
    
    Program findProgramAndRulesdByProgramName(String programName);


	Program findProgramPerfByProgramName(String programName);
	
	List<TreeNodeVo> getChildren(String participantName,
			String programName);
	
	List<Participant> getDescendantClients(String participantName, String programName);
	
	List<Participant> getSiblingClients(String participantName, String programName);


	Program getBySecondaryUtilityName(String secondaryUtilityName);
	List<ProgramParticipant> findProgramClientByProgramAndParticipant(
            String progName, List<String> partNames);
	Program getProgramWithParticipantsAndPRules(String programName,List<String> partList);
}
