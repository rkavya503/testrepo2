/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.scertp.SCERTPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.util.Date;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.timer.TimerManager;

/**
 * The Interface SCERTPProgramEJB.
 */

public interface SCERTPProgramEJB extends ProgramEJB, TimerManager {
    @Remote
    public interface R extends SCERTPProgramEJB {}

    @Local
    public interface L extends SCERTPProgramEJB {}

    final int SCERTP_ISSUE_HOUR = 17;

    /**
     * Gets the season.
     * 
     * @param programName
     *            the program name
     * @param date
     *            the date
     * 
     * @return the season
     */
    String getSeason(String programName, Date date)
            throws NotConfiguredException;

    /**
     * Gets the rate.
     * 
     * @param programName
     *            the program name
     * @param time
     *            the time
     * @param temperature
     *            the temperature
     * 
     * @return the rate
     */
    double getRate(String programName, Date time) throws NotConfiguredException;

    /**
     * Gets the price signal and puts that one signal into a list and returns
     * it.
     * 
     * @param programName
     *            the program name
     * @param eventTiming
     *            the event timing
     * @param rateInfo
     *            the rateInfo that is filled in (can't be null)
     * 
     * @return the rate
     */
    Set<EventSignal> getPriceSignals(Program program, EventTiming eventTiming,
            SCERTPEventRateInfo rateInfo);

    void checkForRevisedPrices(String programName);

}