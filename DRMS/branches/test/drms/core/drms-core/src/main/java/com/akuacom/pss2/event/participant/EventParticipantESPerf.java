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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.event.EventESPerf;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalESPerf;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntryESPerf;
import com.akuacom.pss2.event.signal.EventSignalESPerf;
import com.akuacom.pss2.participant.ParticipantESPerf;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.util.EventInfoValue;

/**
 * ESPerf family designed to optimize client polling for Event State
 */
@Entity
@Table(name = "event_participant")
public class EventParticipantESPerf extends VersionedEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2599844395134889085L;

	/** The event. */
    @ManyToOne
    @JoinColumn(name = "event_uuid")
    private EventESPerf event;

    /** The event mod number. */
    private int eventModNumber;
    
    /** The signal definitions. */
    @OneToMany(mappedBy = "eventParticipant", fetch = FetchType.EAGER)
    private Set<EventParticipantSignalESPerf> signals;
    
    /** The participant. */
    @ManyToOne
    @JoinColumn(name = "participant_uuid")
    private ParticipantESPerf participant;

    /**
     * Gets the event.
     *
     * @return the event
     */
    public EventESPerf getEvent() {
        return event;
    }

    /**
     * Sets the event.
     *
     * @param event the new event
     */
    public void setEvent(EventESPerf event) {
        this.event = event;
    }

    /**
     * Gets the signal definitions.
     *
     * @return the signal definitions
     */
    public Set<EventParticipantSignalESPerf> getSignals() {
		return signals;
	}

	/**
	 * Sets the event rules.
	 *
	 * @param signals the new event rules
	 */
	public void setSignals(Set<EventParticipantSignalESPerf> signals) {
		this.setEventModNumber(this.getEventModNumber() + 1);
		this.signals = signals;
	}
    
	/**
	 * Fetches the EventParticipantSignalEntry
	 * @return Set of EventParticipantSignalEntry
	 */
	public List<EventParticipantSignalEntryESPerf> getSignalEntries() {
    	List<EventParticipantSignalEntryESPerf> result = new ArrayList<EventParticipantSignalEntryESPerf>();
        Set<EventParticipantSignalESPerf> sigs = getSignals();
        if (sigs != null) {
            for (EventParticipantSignalESPerf s : getSignals()) {
                result.addAll(s.getEventParticipantSignalEntries());
            }
        }
    	
		return result;
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
    
    public ParticipantESPerf getParticipant() {
		return participant;
	}

	public void setParticipant(ParticipantESPerf participant) {
		this.participant = participant;
	}
    
	/**
	 * Utility method returns the string value for the signal or null if signal is not found
	 * 
	 * @param ePart
	 * @param signalName
	 * @return
	 */
	public String getSignalValueForEventParticipantAsString(String signalName) {
		String res = null;
        List<EventParticipantSignalEntryESPerf> signalEntries = getSignalEntries();
        Collections.sort(signalEntries);
        Collections.reverse(signalEntries);
        for (EventParticipantSignalEntryESPerf signalEntry : signalEntries) {
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
	public List<EventInfoInstance> getEventInfoInstance(long startTime) {
		ArrayList<EventInfoInstance> result = new ArrayList<EventInfoInstance>();
		Set<Signal> signals = new HashSet<Signal>();
		// Get the client signals
		for (Signal signal : getSignals()) {
			signals.add(signal);
		}

		// Add the event-level signals
		Set<EventSignalESPerf> eventSignals = (getEvent().getEventSignals());
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
}
