/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.EventEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.event.participant.EventParticipantESPerf;
import com.akuacom.pss2.event.signal.EventSignalESPerf;
import com.akuacom.pss2.util.PSS2Util;

/**
 * ESPerf family designed to optimize client polling for Event State
 */
@Entity
@Table(name = "event")
public class EventESPerf extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2476646017269432172L;

	/** The event name. */
	@Column(length=PSS2Util.MAX_EVENT_NAME_LENGTH)
    private String eventName;
    
    /** The issued time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date issuedTime;
    
    /** The start time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date startTime;
    
    /** The end time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date endTime;
    
    /** The received time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date receivedTime;
    
    @OneToMany(mappedBy="event", fetch=FetchType.EAGER)
    private Set<EventSignalESPerf> eventSignals;
    
    /** The program name. */
    private String programName;
    
    @OneToMany(mappedBy = "event", fetch=FetchType.LAZY)
    private Set<EventParticipantESPerf> eventParticipants;
    
    public Set<EventParticipantESPerf> getEventParticipants() {
		return eventParticipants;
	}

	public void setEventParticipants(Set<EventParticipantESPerf> eventParticipants) {
		this.eventParticipants = eventParticipants;
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
     * Gets the issued time.
     * 
     * @return the issued time
     */
    public Date getIssuedTime() {
        return issuedTime;
    }

    /**
     * Sets the issued time.
     * 
     * @param issuedTime the new issued time
     */
    public void setIssuedTime(Date issuedTime) {
        this.issuedTime = issuedTime;
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
     * Gets the received time.
     * 
     * @return the received time
     */
    public Date getReceivedTime() {
        return receivedTime;
    }

    /**
     * Sets the received time.
     * 
     * @param receivedTime the new received time
     */
    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

	public Set<EventSignalESPerf> getEventSignals() {
		return this.eventSignals;
	}

	public void setEventSignals(Set<EventSignalESPerf> eventSignals) {
		this.eventSignals = eventSignals;
	}

    /**
     * Checks if is issued.
     * 
     * @return true, if is issued
     */
    public boolean isIssued()
	{
		long nowMS = System.currentTimeMillis();
		return nowMS >= issuedTime.getTime() && nowMS <= endTime.getTime();
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
}
