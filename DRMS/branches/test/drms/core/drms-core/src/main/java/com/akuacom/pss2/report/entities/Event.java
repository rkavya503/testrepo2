/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.report.entities.Event.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report.entities;


import com.akuacom.pss2.participant.Participant;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * The Class Event.
 */
public class Event implements Serializable {
    
    /** The program name. */
    private String programName;
    
    /** The event name. */
    private String eventName;
    
    /** The start time. */
    private Date startTime;
    
    /** The end time. */
    private Date endTime;
    
    private Boolean historicalEvent;

    private List<String> eventParticipants = new ArrayList<String>();

    public List<String> getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(List<String> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    /**
     * Gets the end time.
     * 
     * @return the end time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time.
     * 
     * @param endTime the new end time
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
     * Gets the start time.
     * 
     * @return the start time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time.
     * 
     * @param startTime the new start time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

	public Boolean getHistoricalEvent() {
		return historicalEvent;
	}

	public void setHistoricalEvent(Boolean historicalEvent) {
		this.historicalEvent = historicalEvent;
	}
    
    
}
