/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.system.SystemManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.signal;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.client.ClientManualSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.participant.Participant;
import java.util.Set;

/**
 * The Interface SignalManager.
 */

public interface SignalManager {
    @Remote
    public interface R extends SignalManager {}
    @Local
    public interface L extends SignalManager {}

    /**
     * Gets the signals.
     * 
     * @return the signals
     */
    List<SignalDef> getSignals();

    /**
     * Gets the signal.
     * 
     * @param signalName
     *            the signal name
     * 
     * @return the signal
     */
    SignalDef getSignal(String signalName);
    
    List<SignalDef> getSignals(List<String> signalName);

    List<com.akuacom.pss2.signal.SignalDef> findSignals();
    
    List<com.akuacom.pss2.signal.SignalDef> findSignalsPerf();

    com.akuacom.pss2.signal.SignalDef findSignal(String signalName);

    void saveSignals(List<com.akuacom.pss2.signal.SignalDef> signals);

    List<ClientManualSignal> findSignalsWithDefaults(Participant p);

    /**
     * Adds the signal after checking for uniqueness.
     * 
     * @param signal
     *            the signal
     */
    void addSignal(SignalDef signal);

    /**
     * Gets the signals as string.
     * 
     * @return the signals as string
     */
    List<String> getSignalsAsString();

    /**
     * Removes the signal - NOT IMPLEMENTED.
     * 
     * @param signalName
     *            the signal name
     */
    void removeSignal(String signalName);

    /**
     * Update signal - NOT IMPLEMENTED.
     * 
     * @param signal
     *            the signal
     */
    void updateSignal(SignalDef signal);

}