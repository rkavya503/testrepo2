/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.signal;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.pss2.signal.SignalDef;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface ProgramSignalEAO extends BaseEAO<ProgramSignal> {
    @Remote
    public interface R extends ProgramSignalEAO {}
    @Local
    public interface L extends ProgramSignalEAO {}

    /**
     * Find signals.
     * 
     * @param programName
     *            the program name
     * 
     * @return the list< signal>
     */
    //List<SignalDef> findSignals(String programName);

}