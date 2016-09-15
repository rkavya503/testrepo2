/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.dbp.DBPDataAccess.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.event.participant.EventParticipant;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * The Interface DBPDataAccess.
 */

public interface DBPDataAccess {
    @Remote
    public interface R extends DBPDataAccess {}
    @Local
    public interface L extends DBPDataAccess {}

    /**
     * Update event state.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param bidState
     *            the bid state
     * 
     * @return the dBP event
     */
    DBPEvent updateEventState(String programName, String eventName,
            BidState bidState);

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
     */
    void setDefaultBid(String programName, String participantName,
            boolean isClient, List<BidEntry> bids);

    /**
     * Gets the current bid.
     * 
     * @param programName
     *            the program name
     * @param event
     * @param participant
     *            @return the current bid
     */
    List<BidEntry> getCurrentBid(String programName, Event event,
            Participant participant, boolean isClient);

    /**
     * Sets the current bid.
     * 
     * @param programName
     *            the program name
     * @param event
     * @param eventParticipant
     * @param bids
     *            the bids
     */
    public Set<EventParticipantBidEntry> setCurrentBid(String programName, Event event,
            EventParticipant eventParticipant, List<BidEntry> bids);

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
    boolean isBidDeclined(String programName, String eventName,
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
     * @param eventParticipant
     */
    void setBidDeclined(String programName, String eventName,
                        String participantName, boolean declined, EventParticipant eventParticipant);

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
     * Sets the bid acknowledged.
     * 
     * @param programName
     *            the program name
     * @param event
     * @param participant
     * @param acknowledged
     *            the acknowledged
     * @param eventParticipant
     */
    void setBidAcknowledged(String programName, Event event,
                            Participant participant, boolean isClient, boolean acknowledged, EventParticipant eventParticipant);

    /**
     * Checks if is bid accepted.
     * 
     * @param programName
     *            the program name
	 * @param event the event
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
     * @param event
     * @param participan
     * @param accepted
     *            the accepted
     * @param eventParticipant
     */
    void setBidAccepted(String programName, Event event,
                        Participant participan, boolean isClient, boolean accepted, EventParticipant eventParticipant);

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
    Map<String, List<String>> getLevelMap(String programName,
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
    void setLevelMap(String programName, String participantName,
            boolean isClient, Map<String, List<String>> ruleMap);
}
