/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.signal;

import java.util.List;
import java.util.Properties;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAO;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface SignalEAO extends BaseEAO<SignalDef> {
    @Remote
    public interface R extends SignalEAO {}

    @Local
    public interface L extends SignalEAO {}

    /**
     * Find signals.
     * 
     * @return the list< signal>
     * 
     * @throws AppServiceException
     *             the app service exception
     */
    List<SignalDef> findSignals() throws AppServiceException;
    
    List<SignalDef> findSignalsPerf() throws AppServiceException;
    
    /**
     * Sets the signal.
     * 
     * @param signals
     *            the new signal
     */
    void setSignal(List<SignalDef> signals);

    /**
     * Gets the signal.
     * 
     * @param name
     *            the name
     * 
     * @return the signal
     * 
     * @throws AppServiceException
     *             the app service exception
     */
    SignalDef getSignal(String name) throws AppServiceException;
    
    List<SignalDef> getSignals(List<String> name) throws AppServiceException;
    


    /**
     * Import signal.
     * 
     * @param config
     *            the config
     * 
     * @return the list<com.akuacom.pss2.core.model. signal>
     * 
     * @throws AppServiceException
     *             the app service exception
     */
    List<SignalDef> importSignal(Properties config) throws AppServiceException;

    void addSignal(SignalDef signal);

}