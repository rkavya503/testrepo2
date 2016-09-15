/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.core.entities.Participant.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.client.ClientManualSignalESPerf;
import com.akuacom.pss2.event.EventESPerf;
import com.akuacom.pss2.event.participant.EventParticipantESPerf;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntryESPerf;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.MemorySequence;
import com.akuacom.pss2.util.ModeSlot;
import com.akuacom.pss2.util.OperationModeValue;

/**
 * ESPerf family designed to optimize client polling for Event State
 * 
 * The Entity Bean Participant.
 */
@Entity
@Table(name = "participant")
@NamedQueries({
        @NamedQuery(name = "ParticipantESPerf.findByName",
                query = "select p from ParticipantESPerf p where p.participantName = :name and p.client = :client",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class ParticipantESPerf extends VersionedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4886853904613771109L;

	/** The participant name. */
    private String participantName;

    /** The manual control. */
    private boolean manualControl;

    /** The manual signals. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "participant")
    @OrderBy("name, value")
    private Set<ClientManualSignalESPerf> manualSignals;

    /** The client. */
    private boolean client;

    /** The parent. */
    private String parent;

    @OneToMany( fetch = FetchType.EAGER, mappedBy = "participant")
    private Set<EventParticipantESPerf> eventParticipants;

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
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    /**
     * Checks if is manual control.
     *
     * @return true, if is manual control
     */
    public boolean isManualControl() {
        return manualControl;
    }

    /**
     * Sets the manual control.
     *
     * @param manualControl the new manual control
     */
    public void setManualControl(boolean manualControl) {
        this.manualControl = manualControl;
    }

    /**
     * Gets the manual signals - WARNING: may have null entries in the list.
     *
     * @return the manual signals
     */
    public Set<ClientManualSignalESPerf> getManualSignals() {
        return manualSignals;
    }

    /**
     * Sets the manual signals.
     *
     * @param manualSignals the new manual signals
     */
    public void setManualSignals(Set<ClientManualSignalESPerf> manualSignals) {
        this.manualSignals = manualSignals;
    }

    /**
     * Checks if is client.
     *
     * @return true, if is client
     */
    public boolean isClient() {
        return client;
    }

    /**
     * Sets the client.
     *
     * @param client the new client
     */
    public void setClient(boolean client) {
        this.client = client;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public String getParent() {
        return parent;
    }

    /**
     * Sets the parent.
     *
     * @param parent the new parent
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

	public Set<EventParticipantESPerf> getEventParticipants() {
		return eventParticipants;
	}

	public void setEventParticipants(Set<EventParticipantESPerf> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}
	
	/**
	 * Returns a list of fully loaded event states based on event information
	 * loaded with this entity.
	 * 
	 * It is called "quick" because it is faster than ClientManager.getClientEventStates
	 * because it uses data in memory in this object graph. Currently, this method should
	 * return exactly the same results as ClientManager.  However, ClientManager
	 * calls ProgramEJB newEventState and getClientEventState which may be overridden
	 * in the future.  This method does not.
	 * 
	 * Also, to avoid manager calls from an entity bean, this method hard codes 
	 * signal defaults for pending and mode.
	 * 
	 * @return
	 */
    /*
	public List<EventState> getQuickEventStates(HashMap<String, Integer> programPriorities) {
		long nowMS = System.currentTimeMillis();
		List<EventState> eventStates = new ArrayList<EventState>();
		
        if (this.getEventParticipants() != null && getEventParticipants().size() > 0) {
            for (EventParticipantESPerf eventPart : getEventParticipants()) {
                EventESPerf event = eventPart.getEvent();
                // only add to list of the event is issued
                if (event != null) {
                    if (event.isIssued()) {
                        EventState eventState = new EventState(); // programEJB.newEventState();
                        eventState.setDrasClientID(getParticipantName());
                        eventState.setEventIdentifier(event.getEventName());
                        eventState.setEventModNumber(eventPart.getEventModNumber());
                        eventState.setEventStateID(MemorySequence.getNextSequenceId());
                        eventState.setProgramName(event.getProgramName());

                        long startTimeMS = event.getStartTime().getTime();

                        if (isManualControl()) {
                            String pendingValue = getClientManualSignalValueAsString("pending");
                            if (pendingValue == null) {
                            	// hardcoded default
                            	pendingValue = "off";
                            }
                            if (pendingValue.equals("on")) {
                                eventState.setEventStatus(EventState.EventStatus.ACTIVE);
                            } else {
                                eventState.setEventStatus(EventState.EventStatus.NONE);
                            }
                        } else {
                            String pendingValue = eventPart.getSignalValueForEventParticipantAsString("pending");
                            if (pendingValue == null) {
                            	pendingValue = "off";
                            }
                            if (startTimeMS < nowMS) {
                                eventState.setEventStatus(EventState.EventStatus.ACTIVE);
                            } else if (pendingValue.equals("on")) {
                                eventState.setEventStatus(EventState.EventStatus.NEAR);
                            } else {
                                eventState.setEventStatus(EventState.EventStatus.FAR);
                            }
                        }

                        String modeSignalValue;
                        if (isManualControl()) {
                            modeSignalValue = getClientManualSignalValueAsString("mode");
                            if (modeSignalValue == null) {
                            	// hardcoded default.
                            	modeSignalValue = "normal";
                            }
                        } else {
                            modeSignalValue = eventPart.getSignalValueForEventParticipantAsString("mode");
                            if (modeSignalValue == null) {
                            	modeSignalValue = "normal";
                            }
                        }
                        eventState.setOperationModeValue(modeSignalValue);
                        eventState.setManualControl(isManualControl());
                        eventState.setCurrentTimeS((nowMS - startTimeMS) / 1000.0);

                        List<ModeSlot> operationModeSchedule = new ArrayList<ModeSlot>();

                        List<EventParticipantSignalEntryESPerf> signalEntries = eventPart.getSignalEntries();
                        for (EventParticipantSignalEntryESPerf entry : signalEntries) {
                            if (!entry.getParentSignal().getSignalDef().getSignalName().equals("mode")) {
                                continue;
                            }

                            ModeSlot modeSlot = new ModeSlot();
                            modeSlot.setOperationModeValue((entry)
                                    .getLevelValue());
                            modeSlot.setTimeSlotS((entry.getTime()
                                    .getTime() - startTimeMS) / 1000.0);
                            operationModeSchedule.add(modeSlot);
                        }
                        eventState.setOperationModeSchedule(operationModeSchedule);
                        eventState.setNotificationTime(event.getIssuedTime());
                        eventState.setStartTime(event.getStartTime());
                        eventState.setEndTime(event.getEndTime());

                        eventState.setEventInfoInstances(eventPart.getEventInfoInstance(eventState.getStartTime().getTime()));

                        eventStates.add(eventState);
                    }
                }
            }
        }
        
        if (eventStates.size() == 0) {
            // create event state based on manual control or a empty/no event NORMAL one
            EventState eventState = new EventState();
            eventState.setDrasClientID(getParticipantName());
            eventState.setEventIdentifier("");
            eventState.setEventModNumber(0);
            eventState.setEventStateID(MemorySequence.getNextSequenceId());
            eventState.setProgramName("");

            String pendingSignalValue = "";
            String modeSignalValue = "";
            if (isManualControl()) {
                pendingSignalValue = getClientManualSignalValueAsString("pending");
                modeSignalValue = getClientManualSignalValueAsString("mode");
            } else {
                pendingSignalValue = "off";
                modeSignalValue = "normal";
            }

            if (pendingSignalValue.equals("on")) {
                eventState.setEventStatus(EventState.EventStatus.ACTIVE);
            } else {
                eventState.setEventStatus(EventState.EventStatus.NONE);
            }
            eventState.setOperationModeValue(modeSignalValue);
            eventState.setManualControl(isManualControl());
            eventStates.add(eventState);
        }

        if (isClient()) {
            Collections.sort(eventStates, 
            		new EventState.PriorityComparator(programPriorities));
        }
        
        return eventStates;
	}
	*/
	/**
	 * Utility method returns the string value for the signal or null if signal is not found
	 * @param signalName
	 * @return
	 */
	public String getClientManualSignalValueAsString(String signalName) {
		// mode and pending would always be available in client manual signals
        // If not, manual control will not work.
		
		String res = null;
        if (getManualSignals() != null) {
            for (ClientManualSignalESPerf signalState : getManualSignals()) {
                if (signalState != null) {
                    if (signalState.getName().equals(signalName)) {
                        res = signalState.getValue();
                        break;
                    }
                }
            }
        }

        return res;
    }
	
	/**
	 * Returns the OperationModeValue from this object's getQuickEventStates.
	 * 
	 * See the docs for getQuickEventStates for warnings about using this method
	 */
	//public OperationModeValue getQuickOperationModeValue(HashMap<String, Integer> programPriorities) {
	//	return getQuickEventStates(programPriorities).get(0).getOperationModeValue();
	//}
}