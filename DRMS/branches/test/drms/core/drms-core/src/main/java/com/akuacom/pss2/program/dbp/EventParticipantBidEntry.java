/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.BidEntryEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.event.participant.EventParticipant;

/**
 * The Class BidEntryDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "event_participant_bid_entry")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class EventParticipantBidEntry extends BaseEntity {

	private static final long serialVersionUID = 4758697258120207963L;

	@Temporal( TemporalType.TIMESTAMP)
	private Date startTime;
    
	@Temporal( TemporalType.TIMESTAMP)
	private Date endTime;
    
    private double reductionKW;
    private double priceLevel;
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "event_participant_uuid")
    private EventParticipant eventParticipant;

    /**
     * Checks if is active.
     * 
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     * 
     * @param active the new active
     */
    public void setActive(boolean active) {
        this.active = active;
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
     * Gets the event participant.
     * 
     * @return the event participant
     */
    public EventParticipant getEventParticipant() {
        return eventParticipant;
    }

    /**
     * Sets the event participant.
     * 
     * @param eventParticipant the new event participant
     */
    public void setEventParticipant(EventParticipant eventParticipant) {
        this.eventParticipant = eventParticipant;
    }

    /**
     * Gets the price level.
     * 
     * @return the price level
     */
    public double getPriceLevel() {
        return priceLevel;
    }

    /**
     * Sets the price level.
     * 
     * @param priceLevel the new price level
     */
    public void setPriceLevel(double priceLevel) {
        this.priceLevel = priceLevel;
    }

    /**
     * Gets the reduction kw.
     * 
     * @return the reduction kw
     */
    public double getReductionKW() {
        return reductionKW;
    }

    /**
     * Sets the reduction kw.
     * 
     * @param reductionKW the new reduction kw
     */
    public void setReductionKW(double reductionKW) {
        this.reductionKW = reductionKW;
    }
}
