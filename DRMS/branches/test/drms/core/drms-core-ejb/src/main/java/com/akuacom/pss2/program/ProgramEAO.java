/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.rtp.RTPPrice;
import java.util.HashMap;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface ProgramEAO extends BaseEAO<Program> {
    @Remote
    public interface R extends ProgramEAO {}
    @Local
    public interface L extends ProgramEAO {}

    /**
     * Get program class by name
     * @param programName program name
     * @return program class
     */
    String getProgramClassByName(String programName);
    

    /**
     * Get program class name
     * @param programName program name
     * @return program class name
     */
    String getProgramClassName(String programName);
    
    
    /**
     * Checks if bidding is anbled by program name
     * @param programName program name
     * @return
     */
    Boolean isDrasBiddingByProgramName(String programName);
    
    /**
     * This function creates a program .
     * 
     * @param program
     *            the program
     * 
     * @return the program
     * 
     * @throws com.akuacom.common.exception.AppServiceException
     *             the app service exception
     * 
     *             Program
     */
    Program createProgram(Program program) throws AppServiceException;

    /**
     * This function updates a program.
     * 
     * @param program
     *            the program
     * 
     * @return the program
     * 
     * @throws com.akuacom.common.exception.AppServiceException
     *             the app service exception
     */
    Program updateProgram(Program program) throws AppServiceException;

    /**
     * This function finds list of programs by a given name.
     * 
     * @param programName
     *            the name
     * @return the program
     * @throws com.akuacom.common.exception.AppServiceException
     *             the app service exception
     */
    @Deprecated /* too expensive */
    Program getProgram(String programName) throws AppServiceException;
    
    Program findProgramPerfByProgramName(String programName);
    
    Program findProgramAndRulesdByProgramName(String programName);

	List<Program> getProgramsHydratePrgmParts() throws AppServiceException;

    List<Program> getProgramsHydratePrgmPartsByPriority() throws AppServiceException;

    ProgramPerf getProgramPerf(String programName);
    
//    Program getProgramPerfWithContact(String programName);
    
    public HashMap<String, Integer> getProgramPriority();

    public String getUiScheduleEventString(String programName);
         /**
     * Delete program by program name.
     * 
     * @param programName
     *            the program name
     * 
     * @throws com.akuacom.common.exception.AppServiceException
     *             the app service exception
     */
    void deleteProgramByProgramName(String programName)
            throws AppServiceException;

    /**
     * Find active program names.
     * 
     * @return the list< string>
     * 
     * @throws com.akuacom.common.exception.AppServiceException
     *             the app service exception
     */
    List<String> findProgramNames() throws AppServiceException;

    /**
     * Find priority sorted program names.
     * 
     * @return the list< string>
     */
    List<String> findPrioritySortedProgramNames();

    void setRealTimePrice(RTPPrice price);

    RTPPrice getLastRealTimePrice(String programName);

    List<RTPPrice> getCurrentRealTimePrices(String programName);

    Program markPriorityForAddLast(Program program);

    Program markPriorityForAddAfter(int before, Program program);

    List<Program> getProgramsForAddAfter(int before);

    void markPriority(Program p) throws Exception;

    public Program refreshProgram(String programName);
    
    int getMaxPriority();
    
    /**
	 * TODO: return value should be change to String if there is no duplicated long program name
	 */
    List<Program> findProgramsByLongProgramName(String longProgramName) throws AppServiceException;
    
    List<Program> getProgramsLazy() throws AppServiceException;
    
    List<Program> findProgramsByProgramClass(List<String> programClasses, boolean state) throws AppServiceException;
    
    //fine Grained API
    Program getProgramOnly(String programName);
    
    Program getProgramWithLoadBid(String programName);
    
    Program getProgramWithParticipants(String programName);
    
    Program getProgramWithSignals(String program);
    
    Program findProgramWithSignalsPerf(String program);
    
    
    Program getProgramWithBidConfig(String programName);
    
    Program getProgramWithRules(String programName);
    
    Program getProgramWithAllConfig(String programName);
    
    Program getProgramWithParticipantsAndPRules(String programName);
    
    Program getProgramWithBidAndProgramParticipantsAndPRules(String programName);
    
    List<Program> getAllPrograms();
    
    Object doQuery(String hql);

    List<Program> getProgramsWithParticipants(List<String> programNames);

	Program getBySecondaryUtilityName(String secondaryUtilityName);
	
	Program getProgramWithParticipantsAndPRules(String programName,List<String> partList);
  
}