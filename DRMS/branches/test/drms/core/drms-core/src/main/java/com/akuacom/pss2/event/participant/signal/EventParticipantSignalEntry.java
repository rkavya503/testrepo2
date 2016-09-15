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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.annotations.DubiousColumnDefinition;
import com.akuacom.pss2.program.participant.DemandLimitingProgramParticipantState;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;


/**
 * The Class SignalEntryDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
//@BatchSize(size=50)
@Table(name = "event_participant_signal_entry")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
        @NamedQuery(name = "EventParticipantSignalEntry.findByEventParticipant",
                query = "select se from EventParticipantSignalEntry se " +
                        "where se.eventParticipantSignal.eventParticipant.event.eventName = :eventName " +
                        "and se.eventParticipantSignal.eventParticipant.participant.participantName = :participantName " +
                        "order by se.time asc",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipantSignalEntry.findByEventParticipantAndSignalName",
                query = "select se from EventParticipantSignalEntry se " +
                        "where se.eventParticipantSignal.eventParticipant.event.eventName = :eventName " +
                        "and se.eventParticipantSignal.eventParticipant.participant.participantName = :participantName " +
                        "and se.eventParticipantSignal.signalDef.signalName = :signalName " +
                        "order by se.time desc",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipantSignalEntry.findAllUnexecutedSignalEntries",
                query = "select se from EventParticipantSignalEntry se " +
                        "where se.creationTime < :now " +
                        "and se.expired = false ",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
       @NamedQuery(name = "EventParticipantSignalEntry.getLatestSignalStartTimeInEvent",
                        query = "select max(se.time) from EventParticipantSignalEntry se " +
                                "where se.eventParticipantSignal.eventParticipant.event.eventName=:eventName",
                        hints={@QueryHint(name="org.hibernate.cacheable", value="false")})
})
public class EventParticipantSignalEntry extends VersionedEntity implements SignalEntry {
    
	private static final long serialVersionUID = -7246042259548208607L;

	/** The expired. */
	@Column(columnDefinition="tinyint(3)")
	@DubiousColumnDefinition
    private Boolean expired = false;
    
    @Column(name="`time`")
	private Date time;
    
    private Integer ruleId;
    
	@ManyToOne
	@JoinColumn(name="eventParticipantSignalUuid", nullable=false)
    private EventParticipantSignal eventParticipantSignal;
    
    private String stringValue;
    
    private Double numberValue;
    
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
	@Override
	public void setExpired(Boolean expired)
	{
		this.expired = expired;
	}

	@Override
	public Date getTime() {
		return time;
	}

	@Override
	public void setTime(Date time) {
		this.time = time;
		
	}

	@Override
	public Signal getParentSignal()
	{
		return this.getEventParticipantSignal();
	}

	@Override
	public void setParentSignal(Signal signal)
	{
		if (!(signal instanceof EventParticipantSignal)) {
			throw new RuntimeException("Only EventParticipantSignal may be set as parent of EventParticipantSignalEntry");
		}
			
		this.setEventParticipantSignal((EventParticipantSignal)signal);
		
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
		return (numberValue !=null) ? numberValue : 0.0;
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
	
	public EventParticipantSignal getEventParticipantSignal() {
		return this.eventParticipantSignal;
	}

	public void setEventParticipantSignal(EventParticipantSignal eventParticipantSignal) {
		this.eventParticipantSignal = eventParticipantSignal;
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

	@Override
	public int compareTo(SignalEntry o) {
		if (o == null) {
			return 1;
		}
		return time.compareTo(o.getTime());
	}
}
