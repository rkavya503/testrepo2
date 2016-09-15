/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.core.entities.EventParticipant.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event.participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.bid.ParticipantBidState;
import com.akuacom.pss2.program.dbp.EventParticipantBidEntry;
import com.akuacom.pss2.program.participant.Node;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.util.EventInfoValue;

/**
 * The Class EventParticipantDAO.
 */
@Entity
@Table(name = "event_participant")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
        @NamedQuery(name = "EventParticipant.findByKeys",
                query = "select distinct(ep) from EventParticipant ep left join fetch ep.participant left join fetch ep.event e left join fetch e.eventSignals " +
                		" left join fetch ep.bidEntries " +
                		" left join fetch ep.eventRules left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries "+  
                        " where ep.event.eventName = :eventName " +
                        " and ep.participant.participantName = :participantName and ep.participant.client = :client and eventOptOut='0'",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipant.findByParticipant",
                query = "select distinct(ep) from EventParticipant ep  left join fetch ep.participant left join fetch ep.event " +
                        "where ep.participant.participantName = :participantName and ep.participant.client = :client and eventOptOut='0'",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipant.findByParticipantAndType",
                query = "select distinct(ep) from EventParticipant ep left join fetch ep.participant left join fetch ep.event left join fetch ep.signals eps " +
                		"left join fetch eps.eventParticipantSignalEntries " +
                        "where ep.participant.participantName = :participantName and ep.participant.client = :client and eventOptOut='0'",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipant.findByParticipantUUID",
                query = "select ep from EventParticipant ep " +
                        "where ep.participant.UUID = :uuid and eventOptOut='0'",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipant.findByParticipantByPartNames",
                query = "select distinct(ep) from EventParticipant ep  left join fetch ep.participant p left join fetch p.manualSignals left join fetch ep.event " +
                        "where ep.participant.participantName IN (:participantNames) and ep.participant.client = :client and eventOptOut='0'",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipant.findByEvent",
                query = "select distinct(ep) from EventParticipant ep left join fetch ep.event left join fetch ep.participant left join fetch ep.bidEntries " +
                		" left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
                        "where ep.event.eventName = :eventName and eventOptOut='0'",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipant.findEventWithoutOptOut",
                query = "select distinct(ep) from EventParticipant ep left join fetch ep.event left join fetch ep.participant left join fetch ep.bidEntries " +
                		" left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
                        "where ep.event.eventName = :eventName",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),         
        @NamedQuery(name = "EventParticipant.findPartNameByEvent",
                query = "select ep.participant.participantName from EventParticipant ep  " +
                        "where ep.event.eventName = :eventName  and ep.participant.client = :client and eventOptOut='0'",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipant.findAllAggregationDescendants.list",
                query = "select distinct(ep) from EventParticipant ep left join fetch ep.participant left join fetch ep.event " +
                        "where ep.event.eventName = :eventName and locate(:ancestor ,ep.ancestry) > 0 and eventOptOut='0'"),
        @NamedQuery(name = "EventParticipant.findParentEventParticipant.Single",
                query = "select distinct(ep) from EventParticipant ep  left join fetch ep.event left join fetch ep.participant left join fetch ep.signals " +
                        "where ep.event.eventName = :eventName and ep.participant.participantName = :parentName and eventOptOut='0'"),
        @NamedQuery(name = "EventParticipant.findEventParticipantForParent",
                query = "select distinct(ep) from EventParticipant ep  left join fetch ep.event left join fetch ep.participant left join fetch ep.signals " +
                        "where ep.participant.participantName = :parentName and eventOptOut='0'"),
        @NamedQuery(name = "EventParticipant.findByParticipantAndTypeAndOptOut",
                query = "select  distinct(ep) from EventParticipant ep left join fetch ep.event left join fetch ep.participant  " +
                        "where ep.participant.participantName = :participantName and ep.participant.client = :client and eventOptOut!='0'",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "EventParticipant.findEventParticipantWithSignalsByClientUUID",
        			query = " select distinct(ep) from EventParticipant ep left join fetch ep.participant p left join fetch p.manualSignals " +
        					" left join fetch ep.event left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
        					" where ep.participant.UUID = :uuid and eventOptOut='0'"),
		@NamedQuery(name = "EventParticipant.findEventParticipantWithSignalsByAllEventClientUUID",
		query = "select distinct(ep) from EventParticipant ep left join fetch ep.participant p left join fetch p.manualSignals " +
				" left join fetch ep.event left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
				" where ep.participant.UUID in (:uuid) and eventOptOut='0'"),
				
		@NamedQuery(name = "EventParticipant.findEventParticipantWithSignalsByAllEventClientUUIDAndEventName",
		query = "select distinct(ep) from EventParticipant ep left join fetch ep.participant p left join fetch p.manualSignals " +
				" left join fetch ep.event left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
				" where ep.participant.UUID in (:uuid) and eventName=:eventName and eventOptOut='0'"),
		@NamedQuery(name = "EventParticipant.findEventParticipantWithSignalsByAllEventClientUUIDForEvent",
		query = "select distinct(ep) from EventParticipant ep left join fetch ep.participant p left join fetch p.manualSignals " +
						" left join fetch ep.event left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
						" where ep.participant.UUID in (:uuid) and ep.event.UUID in(:event_uuid) and eventOptOut='0'"),	
        @NamedQuery(name = "EventParticipant.findEventParticipantWithSignalsByAllEventIds",
		query = "select distinct(ep) from EventParticipant ep left join fetch ep.participant p left join fetch p.manualSignals " +
						" left join fetch ep.event left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
						" where ep.event.UUID in(:event_uuid) and eventOptOut='0'")	,
		@NamedQuery(name = "EventParticipant.findEventParticipantWithParticipantByAccont",
		query = "select  distinct(ep) from EventParticipant ep left join fetch ep.event e left join fetch ep.participant p "+
				" where account IN (:accounts) and eventOptOut='0' and e.eventName in (select eventId from ProgramAggregator)",
				hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
		@NamedQuery(name = "EventParticipant.findEventParticipantWithSignalsByAllEventNameAndClientUUIDForEvent",
		query = "select distinct(ep) from EventParticipant ep left join fetch ep.participant p left join fetch p.manualSignals " +
				" left join fetch ep.event left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
				" where ep.participant.participantName in (:participantName) and ep.event.eventName in(:eventName) and eventOptOut='0'",
				hints={@QueryHint(name="org.hibernate.cacheable", value="true")})	
})
public class EventParticipant extends Node<EventParticipant> {
	
	private static final long serialVersionUID = -8288624723612433141L;

    /** The event. */
    @ManyToOne
    @JoinColumn(name = "event_uuid")
    @NotFound(action=NotFoundAction.IGNORE)
    private Event event;

    /** The participant. */
    @ManyToOne 
    @JoinColumn(name = "participant_uuid")
    @NotFound(action=NotFoundAction.IGNORE)
    private Participant participant;

    /** The bid entries. */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "eventParticipant")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
        org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @OrderBy("startTime")
    private Set<EventParticipantBidEntry> bidEntries = new HashSet<EventParticipantBidEntry>();

    /** The event rules. */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "eventParticipant" )
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<EventParticipantRule> eventRules = new HashSet<EventParticipantRule>();

    /** The bid state. */
    @Enumerated(value = EnumType.STRING)
    private ParticipantBidState bidState = ParticipantBidState.Pending;

    /** The event mod number. */
    private int eventModNumber;

	@Transient
    private boolean aggregator=false;
    
    
	private int eventOptOut=0;
	private Date optOutTime;
	private Date optInTime;
	private Integer applyDayOfBaselineAdjustment = 0;
	
    /** The signal definitions. */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "eventParticipant" )
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<EventParticipantSignal> signals = new HashSet<EventParticipantSignal>();

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

    
    public boolean isAggregator() {
		return aggregator;
	}

	public void setAggregator(boolean aggregator) {
		this.aggregator = aggregator;
	}

	/**
     * Gets the participant.
     *
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * Sets the participant.
     *
     * @param participant the new participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Gets the bid entries.
     *
     * @return the bid entries
     */
    public Set<EventParticipantBidEntry> getBidEntries() {
        return bidEntries;
    }

    /**
     * Sets the bid entries.
     *
     * @param bidEntries the new bid entries
     */
    public void setBidEntries(Set<EventParticipantBidEntry> bidEntries) {
        this.bidEntries = bidEntries;
        incrementEventModNumber();
    }

    /**
     * Gets the bid state.
     *
     * @return the bid state
     */
    public ParticipantBidState getBidState() {
        return bidState;
    }

    /**
     * Sets the bid state.
     *
     * @param bidState the new bid state
     */
    public void setBidState(ParticipantBidState bidState) {
        this.bidState = bidState;
        incrementEventModNumber();
    }

    /**
     * Gets the signal definitions.
     *
     * @return the signal definitions
     */
    public Set<EventParticipantSignal> getSignals() {
		return signals;
	}

	/**
	 * Sets the event rules.
	 *
	 * @param signals the new event rules
	 */
	public void setSignals(Set<EventParticipantSignal> signals) {
		this.setEventModNumber(this.getEventModNumber() + 1);
		this.signals = signals;
	}

	/**
	 * Fetches the EventParticipantSignalEntry
	 * @return Set of EventParticipantSignalEntry
	 */
	public List<EventParticipantSignalEntry> getSignalEntries() {
    	List<EventParticipantSignalEntry> result = new ArrayList<EventParticipantSignalEntry>();
        Set<EventParticipantSignal> sigs = getSignals();
        if (sigs != null) {
            for (EventParticipantSignal s : getSignals()) {
                result.addAll(s.getEventParticipantSignalEntries());
            }
        }

		return result;
	}


    /**
     * Gets the event rules.
     *
     * @return the event rules
     */
    public Set<EventParticipantRule> getEventRules() {
		return eventRules;
	}

	/**
	 * Sets the event rules.
	 *
	 * @param eventRules the new event rules
	 */
	public void setEventRules(Set<EventParticipantRule> eventRules) {
		this.eventRules = eventRules;
	}

	/**
     * Gets the event mod number.
     *
     * @return the event mod number
     */
    public int getEventModNumber()
    {
        return eventModNumber;
    }

    /**
     * Sets the event mod number.
     *
     * @param eventModNumber the new event mod number
     */
    public void setEventModNumber(int eventModNumber)
    {
        this.eventModNumber = eventModNumber;
    }


    public int getEventOptOut() {
		return eventOptOut;
	}

	public void setEventOptOut(int eventOptOut) {
		this.eventOptOut = eventOptOut;
	}

	public Date getOptOutTime() {
		return optOutTime;
	}

	public void setOptOutTime(Date optOutTime) {
		this.optOutTime = optOutTime;
	}

	public Date getOptInTime() {
		return optInTime;
	}

	public void setOptInTime(Date optInTime) {
		this.optInTime = optInTime;
	}

	public void incrementEventModNumber() {
		//this.setEventModNumber(this.getEventModNumber() + 1);
    }
    public Integer getApplyDayOfBaselineAdjustment() {
		return applyDayOfBaselineAdjustment;
	}

	public void setApplyDayOfBaselineAdjustment(Integer applyDayOfBaselineAdjustment) {
		this.applyDayOfBaselineAdjustment = applyDayOfBaselineAdjustment;
	}

    /**
     * To operator string.
     *
     * @return the string
     */
    public String toOperatorString()
	{
        StringBuilder rv = new StringBuilder();
        rv.append("Name: ").append(this.getParticipant().getParticipantName());
        if(! this.getParticipant().isClient())
        {
        rv.append(", Account Number: ").append(this.getParticipant().getAccountNumber());
        }
        return rv.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		if(event != null && event.getEventName() != null)
		{
			result = prime * result + event.getEventName().hashCode();
		}
		else
		{
			result = prime * result;
		}
		result = prime * result;
		if(participant != null && participant.getParticipantName() != null)
		{
			result = prime * result + participant.getParticipantName().hashCode();
			result = prime * result + (participant.isClient() ? 1231 : 1237);
		}
		else
		{
			result = prime * result;
			result = prime * result;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
        //todo: participantUUID:
        if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EventParticipant other = (EventParticipant) obj;
		if (event == null)
		{
			if (other.event != null )
				return false;
		}
		else if(event.getEventName() == null)
		{
			if (other.event.getEventName() != null)
				return false;
		}
		else if (!event.getEventName().equals(other.event.getEventName()))
			return false;
		if (this.getParticipant() == null || this.getParticipant().getParticipantName() == null)
		{
			if (other.getParticipant() != null && other.getParticipant().getParticipantName() != null)
				return false;
		}
		else if (!this.getParticipant().getParticipantName().equals(other.getParticipant().getParticipantName())
				|| this.getParticipant().isClient() != other.getParticipant().isClient())
			return false;
		return true;
	}

	
	
	/**
	 * Utility method returns the string value for the signal or null if signal is not found
	 *
	 * @param signalName
	 * @return
	 */
	public EventParticipantSignalEntry getSignalValueForEventParticipantByName(String signalName) {
		EventParticipantSignalEntry res = null;
        List<EventParticipantSignalEntry> signalEntries = getSignalEntries();
        Collections.sort(signalEntries);
        Collections.reverse(signalEntries);
        for (EventParticipantSignalEntry signalEntry : signalEntries) {
            if (signalEntry.getParentSignal().getSignalDef()
                            .getSignalName().equals(signalName)) {
            	res = signalEntry;
            	break;
            }
        }

        return res;
    }	
	/**
	 * Utility method returns the string value for the signal or null if signal is not found
	 *
	 * @param signalName
	 * @return
	 */
	public String getSignalValueForEventParticipantAsString(String signalName) {

		String res = null;
        List<EventParticipantSignalEntry> signalEntries = getSignalEntries();
        Collections.sort(signalEntries);
        Collections.reverse(signalEntries);
        for (EventParticipantSignalEntry signalEntry : signalEntries) {
            if (signalEntry.getTime().getTime() < System.currentTimeMillis()
                    && signalEntry.getParentSignal().getSignalDef()
                            .getSignalName().equals(signalName)) {
                if (signalEntry.getParentSignal().getSignalDef().isLevelSignal()) {
                    res = signalEntry.getStringValue();
                    break;
                } else {
                    res = Double.toString(signalEntry.getNumberValue());
                    break;
                }
            }
        }

        return res;
	}
	
	/**
	 * Utility method to return event info instances for EventStates.  Copied from
	 * ProgramEJB.getClientEventState
	 *
	 * @param startTime
	 * @return
	 */
	public List<EventInfoInstance> getEventInfoInstances(long startTime,
		Set<Signal> signals) {
		ArrayList<EventInfoInstance> result = new ArrayList<EventInfoInstance>();

		// Add the event-level signals
		Set<EventSignal> eventSignals = (getEvent().getEventSignals());
		for (Signal signal : eventSignals) {
			signals.add(signal);
		}

		for (Signal signal : signals) {
			final String type = signal.getSignalDef().getType();
			if (!EventInfoInstance.SignalType.LOAD_LEVEL.name().equals(type)) {
				EventInfoInstance eventInfoInstance = new EventInfoInstance();
				eventInfoInstance.setSignalType(EventInfoInstance.SignalType
						.valueOf(type));
				eventInfoInstance.setSignalName(signal.getSignalDef()
						.getSignalName());
				List<EventInfoValue> eventInfoValues = new ArrayList<EventInfoValue>();
				final Set<? extends SignalEntry> signalEntries = signal.getSignalEntries();
				for (SignalEntry entry : signalEntries) {
					EventInfoValue eventInfoValue = new EventInfoValue();
					eventInfoValue.setValue(entry.getNumberValue());
					eventInfoValue
							.setTimeOffsetS((entry.getTime().getTime() - startTime) / 1000.0);
					eventInfoValues.add(eventInfoValue);
				}

				// DRMS-1133 Client's signal mode not changed to the right mode
				// during an event
				Collections.sort(eventInfoValues);

				eventInfoInstance.setEventInfoValues(eventInfoValues);
				result.add(eventInfoInstance);
			}
		}
		return result;
	}


    	/**
	 * Utility method to return event info instances for EventStates.  Copied from
	 * ProgramEJB.getClientEventState
	 *
	 * @param startTime
	 * @return
	 */
	public List<EventInfoInstance> getEventInfoInstance(long startTime) {
		ArrayList<EventInfoInstance> result = new ArrayList<EventInfoInstance>();
		Set<Signal> signals = new HashSet<Signal>();
		// Get the client signals
		for (Signal signal : getSignals()) {
			signals.add(signal);
		}

		// Add the event-level signals
		Set<EventSignal> eventSignals = (getEvent().getEventSignals());
		for (Signal signal : eventSignals) {
			signals.add(signal);
		}

		for (Signal signal : signals) {
			final String type = signal.getSignalDef().getType();
			if (!EventInfoInstance.SignalType.LOAD_LEVEL.name().equals(type)) {
				EventInfoInstance eventInfoInstance = new EventInfoInstance();
				eventInfoInstance.setSignalType(EventInfoInstance.SignalType
						.valueOf(type));
				eventInfoInstance.setSignalName(signal.getSignalDef()
						.getSignalName());
				List<EventInfoValue> eventInfoValues = new ArrayList<EventInfoValue>();
				final Set<? extends SignalEntry> signalEntries = signal.getSignalEntries();
				for (SignalEntry entry : signalEntries) {
					EventInfoValue eventInfoValue = new EventInfoValue();
					eventInfoValue.setValue(entry.getNumberValue());
					eventInfoValue
							.setTimeOffsetS((entry.getTime().getTime() - startTime) / 1000.0);
					eventInfoValues.add(eventInfoValue);
				}

				// DRMS-1133 Client's signal mode not changed to the right mode
				// during an event
				Collections.sort(eventInfoValues);

				eventInfoInstance.setEventInfoValues(eventInfoValues);
				result.add(eventInfoInstance);
			}
		}
		return result;
	}
	@PrePersist
	  public void createVersionInfo(){
	    setEventModNumber(0);
	    setCreationTime(new Date());
	  }
	  
	  @PreUpdate
	  public void updateVersionInfo(){
	    setEventModNumber(eventModNumber + 1);
	    setModifiedTime(new Date());
	  }


}
