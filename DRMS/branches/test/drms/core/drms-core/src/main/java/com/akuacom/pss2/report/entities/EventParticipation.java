/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.report.entities.EventParticipation.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class EventParticipation.
 */
public class EventParticipation implements Serializable{
    
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
    
    /** The entry time. */
    private Date entryTime;
    
    /** The reason. */
    private String reason;
    
    /** The type. */
    private String type;
    
    // 07.15.2010 JerryM: DRMS-1106
    /** The event start time. */
    private Date startTime;

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
     * Gets the entry time.
     * 
     * @return the entry time
     */
    public Date getEntryTime() {
        return entryTime;
    }

    /**
     * Sets the entry time.
     * 
     * @param entryTime the new entry time
     */
    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
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
    public void setParticipantName(String participantName, boolean isClient) {
        this.participantName = participantName;
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
     * Gets the reason.
     * 
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason.
     * 
     * @param reason the new reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    // 07.15.2010 JerryM: DRMS-1106
    /**
     * Gets the event start time.
     * 
     * @return the type
     */
	public Date getStartTime() {
		return startTime;
	}
	
	// 07.15.2010 JerryM: DRMS-1106
	 /**
     * Sets the new event start time.
     * 
     * @param startTime the new type
     */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
}
