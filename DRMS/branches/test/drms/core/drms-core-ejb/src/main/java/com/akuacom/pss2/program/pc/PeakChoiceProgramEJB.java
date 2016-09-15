/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.pc.PeakChoiceProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.pc;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.program.dbp.BidEntry;

/**
 * The Interface PeakChoiceProgramEJB.
 */

public interface PeakChoiceProgramEJB extends ProgramEJB {
    @Remote
    public interface R extends PeakChoiceProgramEJB {}
    @Local
    public interface L extends PeakChoiceProgramEJB {}

    /**
     * Gets the default bid.
     * 
     * @param programName
     *            the program name
     * @param participantName
     *            the participant name
     * 
     * @return the default bid
     */
    List<BidEntry> getDefaultBid(String programName, String participantName,
            boolean isClient);

    /**
     * Sets the default bid.
     * 
     * @param programName
     *            the program name
     * @param participantName
     *            the participant name
     * @param bids
     *            the bids
     * 
     * @throws ProgramValidationException
     *             the program validatation exception
     */
    void setDefaultBid(String programName, String participantName,
            boolean isClient, List<BidEntry> bids)
            throws ProgramValidationException;

}
