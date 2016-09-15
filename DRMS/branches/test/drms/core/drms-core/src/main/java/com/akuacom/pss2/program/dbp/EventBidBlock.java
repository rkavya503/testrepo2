/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.EventTimeBlockEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.event.Event;


/**
 * The Class EventBidBlock.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "event_bid_block")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class EventBidBlock extends BaseEntity implements Comparable<EventBidBlock>{
    
    /** The event. */
    @ManyToOne
    @JoinColumn(name = "eventName")
    private Event event;
    
    /** The start time. */
    private int startTime;
    
    /** The end time. */
    private int endTime;

    /**
     * Gets the end time.
     * 
     * @return the end time
     */
    public int getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time.
     * 
     * @param endTime the new end time
     */
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the start time.
     * 
     * @return the start time
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time.
     * 
     * @param startTime the new start time
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the event.
     * 
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the event.
     * 
     * @param event the new event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

	@Override
	public int compareTo(EventBidBlock o)
	{
		if(startTime < o.startTime)
		{
			return -1;
}
		else if (startTime > o.startTime)
		{
			return 1;
		}
		return 0;
	}
}
