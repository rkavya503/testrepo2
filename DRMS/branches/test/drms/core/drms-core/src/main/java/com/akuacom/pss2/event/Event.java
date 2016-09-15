/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.EventEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.PSS2Util;
import com.akuacom.utils.lang.DateUtil;

/**
 * Events are .... (someone please explain what an event does).
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "event")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
	@NamedQuery(name = "Event.findAll",
        query = "select distinct(e) from Event e",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Event.findAllByEventIds",
        query = "select distinct(e) from Event e where e.eventName IN (:eventNames)",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Event.findByProgramName",
        query = "select distinct(e) from Event e left join fetch e.eventSignals where e.programName = :programName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Event.findByEventName",
        query = "select distinct(e) from Event e left join fetch e.eventSignals where e.eventName = :eventName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Event.findByEventName.Single",
            query = "select distinct(e) from Event e left join fetch e.eventSignals where e.eventName = :eventName",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Event.findByDate",
        query = "select distinct(e) from Event e left join fetch e.eventSignals where (e.startTime >= :startTime and e.endTime <= :endTime)",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),        
    @NamedQuery(name = "Event.findByProgramAndDate",
    	query = "select distinct(e) from Event e left join fetch e.eventSignals where (e.startTime >= :startTime and e.endTime <= :endTime) and e.programName= :programName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Event.findByParticipantAndDate",
        	query = "select e from Event e, in (e.eventParticipants) ep where e.startTime >= :startTime and e.endTime <= :endTime and ep.participant.participantName in (:participantNames)",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Event.findByEventNameProgramName",
        query = "select distinct(e) from Event e left join fetch e.eventSignals where e.eventName = :eventName and e.programName = :programName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Event.findByParticipantProgramAndDate",
    	query = "select e from Event e, in (e.eventParticipants) ep where (ep.optOutTime is null or ep.optOutTime>e.startTime) and e.startTime >= :startTime and e.endTime <= :endTime and ep.participant.participantName in (:participantNames) and e.programName not in (:programNames) order by e.startTime",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    
    @NamedQuery(name = "Event.findByAggregatorProgramAndDate",
    	query = "select e from Event e, in (e.eventParticipants) ep where (ep.optOutTime is null or ep.optOutTime>e.startTime) and e.startTime >= :startTime and e.endTime <= :endTime and ep.participant.participantName in (:participantNames) and e.programName in (:programNames) order by e.startTime",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    
    @NamedQuery(name = "Event.findAllPossibleByParticipant",
        query = "select e from ProgramParticipant pp, Event e where pp.program.programName = e.programName and pp.participant.participantName = :participantName"),
        
    //FINE GRAINED QUERY
    @NamedQuery(name  = "Event.findEventOnlyByEventName.single",
    			query = "select distinct(e) from Event e where e.eventName =:eventName"),
    			
    @NamedQuery(name  = "Event.findEventOnlyByProgramName",	
    			query = "select distinct(e) from Event e where e.programName =:eventName"),
    			
    	
	@NamedQuery(name =  "Event.findEventWithParticipantsByEventName.single",
		   query =  " Select distinct(e) from Event e " +
			        " left join fetch e.eventParticipants ep left join fetch ep.participant " +
					" where e.eventName=:eventName"),
    							
    @NamedQuery(name  = "Event.findEventWithEventSignalsByEventName.single",
    		    query = "Select distinct(e) from Event e left join fetch e.eventSignals es left join fetch es.signalDef"  +
    			        " where e.eventName  =:eventName"),
    
	@NamedQuery(name  = "Event.findEventWithParticipantsAndSignals.single",
					query = "Select distinct(e) from Event e left join fetch e.eventSignals es left join fetch es.signalDef " +
					" left join fetch e.eventParticipants ep left join fetch ep.participant "  + 
					" left join fetch ep.signals eps left join fetch eps.signalDef " + 
					" left join fetch eps.eventParticipantSignalEntries " +
					" where e.eventName=:eventName"),
	@NamedQuery(name  = "Event.findEventWithParticipantsAndSignalsAndContacts.single",
					query = "Select distinct(e) from Event e left join fetch e.eventSignals es left join fetch es.signalDef " +
					" left join fetch e.eventParticipants ep left join fetch ep.participant part left join fetch part.contacts "  + 
					" left join fetch ep.signals eps left join fetch eps.signalDef " + 
					" left join fetch eps.eventParticipantSignalEntries " +
					" where e.eventName=:eventName"),
					
    @NamedQuery(name  = "Event.findEventWithParticipantsAndBidsByEventName.single",
    	    	query = " select distinct(e) from Event e left join fetch e.eventParticipants  ep left join fetch ep.bidEntries " +
    	    			" left join fetch ep.participant where e.eventName =:eventName"),
    
	@NamedQuery(name =  "Event.findEventAllByEventName.single",/* event signals,ep, ep.rules, ep.bid, ep.signals. */
			   query =  " Select distinct(e) from Event e left join fetch e.eventSignals es left join fetch es.signalDef " /* event signal */ +
				        " left join fetch e.eventParticipants ep left join fetch ep.participant " /*event participant */ + 
					    " left join fetch ep.eventRules "  /*ep rules */ +
				        " left join fetch ep.signals eps left join fetch eps.signalDef left join fetch eps.eventParticipantSignalEntries" /* ep signals */+
				        " left join fetch ep.bidEntries" /* bid entrues*/ +
						" where e.eventName=:eventName"),
	@NamedQuery(name  = "Event.findEventParticipantByEventTime",
	    	query = " select distinct(e) from Event e left join fetch e.eventParticipants  ep" +
	    			" left join fetch ep.participant where e.programName=:programName and e.startTime =:startTime")
}) 
public class Event extends BaseEntity implements EventTiming {
    
	private static final long serialVersionUID = -3373955587917552295L;

	/** The event name. */
	@Column(length=PSS2Util.MAX_EVENT_NAME_LENGTH)
    private String eventName;
    
    /** The program name. */
    private String programName;
    
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
    
    /** The near (pending) time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date nearTime;
    
    /** The participants. */
    @OneToMany(mappedBy = "event", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @NotFound(action=NotFoundAction.IGNORE)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<EventParticipant> eventParticipants = new HashSet<EventParticipant>();

    /** The manual. */
    private boolean manual;
    
    /** The event status. */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private EventStatus eventStatus = EventStatus.NONE;
    
    /** The state. */
    @Transient
    private transient String state; // TODO: push this field to to web UI layer
    
    @Transient
    private String message; 

	@Transient
    private List<String> locations;
	
	@Lob
    @Column(columnDefinition="longtext")
	private String locationInfo;
	
    private double shedAmount;
    
	private boolean drEvent=false;
	
	private boolean aggregatorUpdated=false;

	@Transient
    private List<ProgramValidationMessage> warnings;
	
	 /** to indicate an active event or a historical event. */
    @Transient
    private transient boolean isActive; // TODO: push this field to to web UI layer
    
    @OneToMany(mappedBy="event", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @NotFound(action=NotFoundAction.IGNORE)
    //@OnDelete(action=OnDeleteAction.CASCADE)
    private Set<EventSignal> eventSignals;
    
	public List<ProgramValidationMessage> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<ProgramValidationMessage> warnings) {
		this.warnings = warnings;
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

    /**
     * Gets the near time.
     * 
     * @return the near time
     */
	public Date getNearTime() {
        return nearTime;
    }
	
    /**
     * Sets the near time.
     * 
     * @param nearTime the new near time
     */
	public void setNearTime(Date nearTime) {
        this.nearTime = nearTime;
    }
	/**
     * Gets the participants.
     * 
     * @return the participants
     */
//    @NotFound(action=NotFoundAction.IGNORE)
//    @OnDelete(action=OnDeleteAction.CASCADE)
    public List<EventParticipant> getParticipants() {
        Set<EventParticipant> eps = getEventParticipants();
        ArrayList<EventParticipant> parts = new ArrayList<EventParticipant>();
        if (eps != null) {
            parts.addAll(eps);
        }
        return parts;
    }

    /**
     * Sets the participants.
     * 
     * @param participants the new participants
     */
    public void setParticipants(List<EventParticipant> participants) {
        setEventParticipants(new HashSet<EventParticipant>(participants));
    }

    public Set<EventParticipant> getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(Set<EventParticipant> eventParticipants) {
        for (EventParticipant part : eventParticipants) {
            part.setEvent(this);
        }
        this.eventParticipants = eventParticipants;
    }

	public Set<EventSignal> getEventSignals() {
		return this.eventSignals;
	}

	public void setEventSignals(Set<EventSignal> eventSignals) {
		this.eventSignals = eventSignals;
	}

    /**
     * Checks if is manual.
     * 
     * @return true, if is manual
     */
    public boolean isManual() {
        return manual;
    }

    /**
     * Sets the manual.
     * 
     * @param manual the new manual
     */
    public void setManual(boolean manual) {
        this.manual = manual;
    }

    /**
     * Gets the event status.
     * 
     * @return the event status
     */
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    /**
     * Sets the event status.
     * 
     * @param eventStatus the new event status
     */
    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
    
    /**
     * Checks if is issued.
     * 
     * @return true, if is issued
     */
    public boolean isIssued()
	{
		long nowMS = System.currentTimeMillis();
		return nowMS >= issuedTime.getTime() && (endTime==null || nowMS <= endTime.getTime());
	}
    
    /**
     * Gets the state.
     * 
     * @return the state
     */
    public String getState() {
        return state;
    }

    public List<String> getLocations() {
    	if(null != this.locationInfo && !this.locationInfo.isEmpty()){
    		locations = Arrays.asList(this.locationInfo.split(","));
    	}
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
		if(null == locations || locations.isEmpty()){
			return;
		}
		this.locationInfo = StringUtils.join(locations.toArray(), ",");
	}

	public String getLocationInfo() {	
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		
		this.locationInfo = locationInfo;
	}

	public boolean isDrEvent() {
		return drEvent;
	}

	public void setDrEvent(boolean drEvent) {
		this.drEvent = drEvent;
	}

	/**
     * Sets the state.
     * 
     * @param state the new state
     */
    public void setState(String state) {
        this.state = state;
    }
    
    public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    public String toString()
    {
        StringBuilder rv = new StringBuilder("Event:\n");
        rv.append("eventName: " + eventName);
        rv.append("\nprogramName: " + programName);
        rv.append("\nissuedTime: " + issuedTime);
        rv.append("\nstartTime: " + startTime);
        rv.append("\nendTime: " + endTime);
        rv.append("\nreceivedTime: " + receivedTime);
        rv.append("\nmanual: " + manual);
        rv.append("\nstate: " + state);
        rv.append("\neventStatus: " + eventStatus);
        if(eventParticipants != null)
        {
        	rv.append("\n");
//	        for(EventParticipant participant: eventParticipants)
//	        {
//	        	if (participant.getParticipant() != null)
//	        		rv.append("EventParticipant: " + participant.getParticipant().getParticipantName());
//	        	rv.append("\n");
//	        }
         }
        else
        {
        	rv.append("\nparticipants: null");        	
        }
        return rv.toString();
   }

	/**
	 * To operator string.
	 * 
	 * @return the string
	 */
	public String toOperatorString()
	{
		StringBuilder rv = new StringBuilder();
        rv.append("Event Name: " + eventName);
        rv.append("\nProgram: " + programName);
        rv.append("\nReceived Time: " + DateUtil.format(receivedTime));
        rv.append("\nIssue Time: " + DateUtil.format(issuedTime));
        rv.append("\nStart Time: " + DateUtil.format(startTime));
        rv.append("\nEnd Time: " + DateUtil.format(endTime));
        if(eventParticipants != null)
        {

        	rv.append("\nParticipants :\n");
	        for(EventParticipant participant: eventParticipants)
	        {
                if(!participant.getParticipant().isClient())
                {

                	rv.append("Name: ").append(participant.getParticipant().getParticipantName());
                    rv.append(", Account Number: ").append(participant.getParticipant().getAccountNumber());
                    rv.append("\n");
                }
            }

        	rv.append("\nClients:\n");
	        for(EventParticipant participant: eventParticipants)
	        {
                if(participant.getParticipant().isClient())
                {
                    rv.append(participant.toOperatorString());
                    rv.append("\n");
                }
            }
         }
        else
        {
        	rv.append("\nParticipants: none\n");        	
        }
        return rv.toString();	
	}

	/**
	 * @return the shedAmount
	 */
	public double getShedAmount() {
		return shedAmount;
	}

	/**
	 * @param shedAmount the shedAmount to set
	 */
	public void setShedAmount(double shedAmount) {
		this.shedAmount = shedAmount;
	}

	public boolean isAggregatorUpdated() {
		return aggregatorUpdated;
	}

	public void setAggregatorUpdated(boolean aggregatorUpdated) {
		this.aggregatorUpdated = aggregatorUpdated;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
