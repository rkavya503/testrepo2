/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.SignalEntryEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event.participant.signal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.annotations.DubiousColumnDefinition;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;


/**
 * ESPerf family designed to optimize client polling for Event State
 */
@Entity
@Table(name = "event_participant_signal_entry")
public class EventParticipantSignalEntryESPerf extends VersionedEntity implements SignalEntry  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4227686326796187869L;

	/** The expired. */
	@Column(columnDefinition="tinyint(3)")
	@DubiousColumnDefinition
    private Boolean expired = false;
    
    @Column(name="`time`")
	private Date time;
    
    private Integer ruleId;
    
    private String stringValue;
    
    private Double numberValue;
    
    @ManyToOne
	@JoinColumn(name="eventParticipantSignalUuid", nullable=false)
    private EventParticipantSignalESPerf eventParticipantSignal;
    
    /**
     * Checks if is expired.
     * 
     * @return true, if is expired
     */
    public Boolean getExpired() {
        return expired;
    }

    /**
     * Sets the expired.
     * 
     * @param expired the new expired
     */
	public void setExpired(Boolean expired)
	{
		this.expired = expired;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
		
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
	public String getLevelValue() {
		return this.getStringValue();
	}

	public void setLevelValue(String stringValue) {
		this.setStringValue(stringValue);
	}
	
	public Double getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(Double numberValue) {
		this.numberValue = numberValue;
	}
	
	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	
	public String getValueAsString() {
		if (this.getStringValue() != null) {
			return this.getStringValue();
		} else if (this.getNumberValue() != null) {
			return this.getNumberValue().toString();
		} else {
			return "";
		}
	}
	
	public EventParticipantSignalESPerf getEventParticipantSignal() {
		return this.eventParticipantSignal;
	}

	public void setEventParticipantSignal(EventParticipantSignalESPerf eventParticipantSignal) {
		this.eventParticipantSignal = eventParticipantSignal;
	}
	
	@Override
	public Signal getParentSignal()
	{
		return this.getEventParticipantSignal();
	}

	@Override
	public void setParentSignal(Signal signal)
	{
		if (!(signal instanceof EventParticipantSignalESPerf)) {
			throw new RuntimeException("Only EventParticipantSignal may be set as parent of EventParticipantSignalEntry");
		}
			
		this.setEventParticipantSignal((EventParticipantSignalESPerf)signal);
		
	}

	@Override
	public int compareTo(SignalEntry o) {
		if (o == null) {
			return 1;
		}
		return time.compareTo(o.getTime());
	}
}
