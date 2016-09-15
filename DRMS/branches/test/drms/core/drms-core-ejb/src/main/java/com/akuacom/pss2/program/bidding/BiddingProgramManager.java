/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.bidding.BiddingProgramManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.bidding;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.dbp.Bid;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.DBPEvent;

/**
 * The Interface BiddingProgramManager.
 */

public interface BiddingProgramManager {
    @Remote
    public interface R extends BiddingProgramManager {}
    @Local
    public interface L extends BiddingProgramManager {}

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
    public List<BidEntry> getDefaultBid(String programName,
            String participantName, boolean isClient);

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
    public void setDefaultBid(String programName, String participantName,
            boolean isClient, List<BidEntry> bids)
            throws ProgramValidationException;

    /**
     * Gets the current bid.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param participantName
     *            the participant name
     * 
     * @return the current bid
     */
    public List<BidEntry> getCurrentBid(String programName, String eventName,
            String participantName, boolean isClient);

	public List<BidEntry> getCurrentBid(String programName, Event event,
		Participant participant, boolean isClient);

    /**
     * Sets the current bid.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param participantName
     *            the participant name
     * @param bids
     *            the bids
     * 
     * @throws ProgramValidationException
     *             the program validatation exception
     */
    public void setCurrentBid(String programName, String eventName,
            String participantName, boolean isClient, List<BidEntry> bids, boolean processRules)
            throws ProgramValidationException;

    /**
     * Checks if is bid declined.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param participantName
     *            the participant name
     * 
     * @return true, if is bid declined
     */
    public boolean isBidDeclined(String programName, String eventName,
            String participantName, boolean isClient);

    /**
     * Sets the bid declined.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param participantName
     *            the participant name
     * @param declined
     *            the declined
     */
    public void setBidDeclined(String programName, String eventName,
            String participantName, boolean isClient, boolean declined);

    /**
     * Checks if is bid acknowledged.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event name
     * @param participantName
     *            the participant name
     * 
     * @return true, if is bid acknowledged
     */
	public boolean isBidAcknowledged(String programName, Event event,
            String participantName, boolean isClient);

    /**
     * Checks if is bid accepted.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event name
     * @param participantName
     *            the participant name
     * 
     * @return true, if is bid accepted
     */
	public boolean isBidAccepted(String programName, Event event,
            String participantName, boolean isClient);

    /**
     * Sets the bid accepted.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param participantName
     *            the participant name
     * @param accepted
     *            the accepted
     */
    public void setBidAccepted(String programName, String eventName,
            String participantName, boolean isClient, boolean accepted);

    /**
     * Gets the level map.
     * 
     * @param programName
     *            the program name
     * @param participantName
     *            the participant name
     * 
     * @return the level map
     */
    public Map<String, List<String>> getLevelMap(String programName,
            String participantName, boolean isClient);

    /**
     * Sets the level map.
     * 
     * @param programName
     *            the program name
     * @param participantName
     *            the participant name
     * @param ruleMap
     *            the rule map
     */
    public void setLevelMap(String programName, String participantName,
            boolean isClient, Map<String, List<String>> ruleMap);

    /**
     * Next state.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     */
    public void nextState(String programName, DBPEvent event);

    /**
     * Gets the bid.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param participantName
     *            the participant name
     * 
     * @return the bid
     */
    public List<BidEntry> getBid(String programName, String eventName,
            String participantName, boolean isClient);

    /**
     * Sets the bid status.
     * 
     * @param eventParticipantAcceptedList
     *            the event participant accepted list
     * @param eventParticipantRejectedList
     *            the event participant rejected list
     */
    public void setBidStatus(List<Bid> eventParticipantAcceptedList,
            List<Bid> eventParticipantRejectedList);

    /**
     * Parse the bid file into the dr event
     * 
     * @param filename
     * @param fileString
     * @return
     * @throws ProgramValidationException
     */
	public UtilityDREvent parseBidFile(String filename, String fileString)
			throws ProgramValidationException;
}
