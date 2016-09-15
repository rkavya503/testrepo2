/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.report.entities.EventSignal.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class EventSignal.
 */
public class EventSignal implements Serializable, Cloneable {

    /** The id. */
    private int id;

    /** The program name. */
    private String programName;

    /** The event name. */
    private String eventName;

    /** The participant name. */
    private String participantName;

    /** The account id. */
    private String accountId;

    /** The signal time. */
    private Date signalTime;

    /** The signal level. */
    private String signalLevel;

    /** The actual level. */
    private String actualLevel;

    /** The client status On/Off line*/
    private String clientStatus;


    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the program name.
     *
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     *
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the event name.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the event name.
     *
     * @param eventName the new event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the participant name.
     *
     * @return the participant name
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * Sets the participant name.
     *
     * @param participantName the new participant name
     */
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    /**
     * Gets the account id.
     *
     * @return the account id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the account id.
     *
     * @param accountId the new account id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Gets the signal time.
     *
     * @return the signal time
     */
    public Date getSignalTime() {
        return signalTime;
    }

    /**
     * Sets the signal time.
     *
     * @param signalTime the new signal time
     */
    public void setSignalTime(Date signalTime) {
        this.signalTime = signalTime;
    }

    /**
     * Gets the signal level.
     *
     * @return the signal level
     */
    public String getSignalLevel() {
        return signalLevel;
    }

    /**
     * Sets the signal level.
     *
     * @param signalLevel the new signal level
     */
    public void setSignalLevel(String signalLevel) {
        this.signalLevel = signalLevel;
    }

    /**
     * Gets the actual level.
     *
     * @return the actual level
     */
    public String getActualLevel() {
        return actualLevel;
    }

    /**
     * Sets the actual level.
     *
     * @param actualLevel the new actual level
     */
    public void setActualLevel(String actualLevel) {
        this.actualLevel = actualLevel;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }
    
     @Override
    public EventSignal clone() {
        final EventSignal signal = new EventSignal();
        signal.setActualLevel(actualLevel);
        signal.setAccountId(accountId);
        signal.setEventName(eventName);
// signal.setId(this.getId());
        signal.setParticipantName(participantName);
        signal.setProgramName(programName);
        signal.setSignalLevel(signalLevel);
        signal.setSignalTime(signalTime);
        signal.setClientStatus(clientStatus);
        return signal;
    }
}
