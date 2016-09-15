/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ProgramManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.history.HistoryEvent;
import com.akuacom.pss2.program.eventtemplate.EventTemplate;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.signal.SignalEntryValue;
import com.akuacom.pss2.util.EventState;

/**
 * Remote, stateless session bean BO facade.
 * <p>
 * This facade provides an entry point into the ProgramManager, ClientManager,
 * ParticipantManager, SystemManager and EventManager session facades.
 * 
 * @see com.akuacom.pss2.event.EventManager
 */

public interface EventManager {
    @Remote
    public interface R extends EventManager {}
    @Local
    public interface L extends EventManager {}

    Event getEventPerf(String eventName);
    
	/**
	 * Creates the event.
	 * 
	 * @param programName
	 *            the program name
	 * @param event
	 *            the event
     * @Returns the names of any created events
	 */
	Collection<String> createEvent(String programName, Event event);

	/**
     * Creates the event.
     *
     * @param programName the program name
     * @param event the event
     * @param utilityDREvent the UtilityDREvent
     */
    Collection<String>  createEvent(String programName, Event event, UtilityDREvent utilityDREvent);

    /**
	 * Creates the event state.
	 * 
	 * @param ent
	 *            the ent
	 */
	void createClientConversationState(EventState ent, boolean push);

	
	
	
	/**
	 * Gets the event state.
	 * 
	 * @param eventStateId
	 *            the event state id
	 * 
	 * @return the event state
	 */
	ClientConversationState getClientConversationState(long stateId);

	/**
	 * Deletes the EventState
	 * 
	 * @param eventStateId
	 *            id
	 */
	void removeClientConversationState(long stateId);

	/**
	 * Gets the events.
	 * 
	 * @return the events
	 */
	List<EventInfo> getEvents();

	// web method
	// TODO: this breaks the rule in the DRAS that events don't have to be
	// unique across all programs - talk to ed
	/**
	 * Gets the event.
	 * 
	 * @param eventName
	 *            the event name
	 * @deprecated too expensive
	 * @return the event
	 */
	@Deprecated
	Event getEvent(String eventName);

	/**
	 * Gets the event.
	 * 
	 * @param programName
	 *            the program name
	 * @param eventName
	 *            the event name
	 * 
	 * @return the event
	 */
	Event getEvent(String programName, String eventName);

	/**
	 * Update event template.
	 * 
	 * @param eventTemplate
	 *            the event template
	 */
	void updateEventTemplate(
			org.openadr.dras.akuautilitydrevent.UtilityDREvent eventTemplate);

	/**
	 * Creates the event template.
	 * 
	 * @param eventTemplate
	 *            the event template
	 */
	void createEventTemplate(
			org.openadr.dras.akuautilitydrevent.UtilityDREvent eventTemplate);

	/**
	 * Delete event template - NOT IMPLEMENTED.
	 * 
	 * @param eventTemplateName
	 *            the event template name
	 */
	void deleteEventTemplate(String eventTemplateName);

	/**
	 * Gets the event template.
	 * 
	 * @param programName
	 *            the program name
	 * 
	 * @return the event template
	 */
	EventTemplate getEventTemplate(String programName);

	/**
	 * Update event.
	 * 
	 * @param programName
	 *            the program name
	 * @param eventName
	 *            the event name
	 * @param event
	 *            the event
	 * @param utilityDREvent
	 *            the utility dr event
	 */
	void updateEvent(String programName, String eventName, Event event,
			UtilityDREvent utilityDREvent);

	/**
	 * Removes the event.
	 * 
	 * @param programName
	 *            the program name
	 * @param eventName
	 *            the event name
	 */
	void removeEvent(String programName, String eventName);

	// TODO: get rid of this b/c we are using an ws class as a param
	/**
	 * Creates the event.
	 * 
	 * @param utilityDREvent
	 *            the utility dr event
	 * @param manualCreation
	 *            the manual creation
     * @Returns the names of any created events
     */
	Collection<String> createEvent(UtilityDREvent utilityDREvent, boolean manualCreation);

	/**
	 * Gets the event participant.
	 * 
	 * @param eventName
	 *            the event name
	 * @param partipantName
	 *            the partipant name
	 * @param isClient
	 *            client flag
	 * 
	 * @return the event participant
	 */
	EventParticipant getEventParticipant(String eventName,
			String partipantName, boolean isClient);

	List<EventParticipant> getEventParticipantsForEvent(String eventName);

	/**
	 * Sets the event participant.
	 * 
	 * @param ep
	 *            the ep
	 * 
	 * @return the event participant dao
	 */
	EventParticipant setEventParticipant(EventParticipant ep);

	/*
	 * event / participant
	 */
	// web method
	// TODO: is pariticipantIDs both participants and client? ask lin
	// TODO: this breaks the rule in the DRAS that events don't have to be
	// unique across all programs - talk to ed
	/**
	 * Adds the participants to event - NOT IMPLEMENTED.
	 *
     * @param eventID
     *            the event id
     * @param participantName
     */
	void addParticipantsToEvent(String eventID, String participantName);

	// TODO: this breaks the rule in the DRAS that events don't have to be
	// unique across all programs - talk to ed
	/**
	 * Gets the participants for event - NOT IMPLEMENTED.
	 * 
	 * @param eventID
	 *            the event id
	 * 
	 * @return the participants for event
	 */
	List<String> getParticipantsForEvent(String eventID);

	// TODO: use the same param type (EventParticipant instead of id(String))
	// TODO: this breaks the rule in the DRAS that events don't have to be
	// unique across all programs - talk to ed
	/**
	 * Update event participants - NOT IMPLEMENTED.
	 * 
	 * @param eventID
	 *            the event id
	 * @param changedEventParts
	 *            the changed event parts
	 */
	void updateEventParticipants(String eventID,
			List<EventParticipant> changedEventParts);

	// web method
	// TODO: is pariticipantIDs both participants and client? ask lin
	// TODO: this breaks the rule in the DRAS that events don't have to be
	// unique across all programs - talk to ed
	/**
	 * Removes the participants from event - NOT IMPLEMENTED.
	 * 
	 * @param eventName
	 * @param participantName
	 *            the pariticipant name @return ?
	 */
	void removeParticipantFromEvent(String eventName, String participantName);

	/*
	 * event client signal entry
	 */
	// TODO: change name to create...
	/**
	 * Adds the event participant signal entry.
	 * 
	 * @param eventParticipant
	 *            event participant
	 * @param signalName
	 *            the signal name
	 * @param entry
	 *            the entry
	 */
	void addEventParticipantSignalEntry(EventParticipant eventParticipant,
			String signalName, EventParticipantSignalEntry entry);

	/**
	 * Removes the event participant signal entry.
	 * 
	 * @param eventParticipant
	 *            event participant
	 * @param signalName
	 *            the signal name
	 * @param time
	 *            the time
	 */
	void removeEventParticipantSignalEntry(EventParticipant eventParticipant,
			String signalName, Date time);

	// This is for operator ui
	// NOTE: implementation delegates the compuation to programEJB which
	// was overwritten in the case of Bidding programs (DBP). so this method
	// will stay. But considering consolidating getEventStatus method
	// then this function call can go away.
	/**
	 * Gets the event status string.
	 * 
	 * @param event
	 *            the event name
	 * 
	 * @return the event status string
	 */
	String getEventStatusString(Event event);

	String getSignalValueForEventParticipantAsString(EventParticipant ePart,
			String signalName);

	Collection<Event> findAll();
	
	Collection<Event> findAllPerf();



    // Returns list of available signals in an event
    Set<SignalDef>        listSignals(String eventID);

    // mid-level internal api.  Apply an entire set of signal entries
    // Deals with all the issues of null collections, cascade delete, collection replacement, etc
    void setSignalEntries(String eventID, String signalName,
                          Set<EventSignalEntry> newEntries, boolean updateVersion);

    // mid-level internal api.  Return collection of entries for a signal
    // Always returns a a collection, but it may be empty
    Set<EventSignalEntry> getSignalEntries(String eventID, String signalName);

    // Returns one signal entry for a given signal and time
    SignalEntry getSignalEntry(String eventID, String signalName, Date atTime);

    // Returns one signal entry for a given signal and time
    String getSignalEntryString(String eventID, String signalName, Date atTime);
    
    // delete all entries in a signal
    void resetSignal(String eventID, String signalName);

    // Trim late end of a signal
    void removeSignalEntriesAfter(String eventID, String signalName, Date afterTime);

    // Trim early end of a signal
    void removePastSignalEntries(String eventID, String signalName, Date afterTime);

    // Set a number at a time (the only thing a signal entry is really good for)
    void setSignalEntry    (String eventID, String signalName, Date atTime, double numberValue);

    // Set a level entry at a time (the only thing a signal entry is really good for)
    void setSignalEntry    (String eventID, String signalName, Date atTime, String levelValue);

    // Delete any entry at the given time in the named signal
    void deleteSignalEntry (String eventID, String signalName, Date atTime);

    //***************************
    // The following methods use a specialized transport class to
    // read and write multiple signal entries from UI in coarse-grained calls
    //***************************
    Set<SignalEntryValue> getAllEventSignalEntryies(String eventID);
    Set<SignalEntryValue> getAllSignalEntries(String eventID, String signalName);
    void deleteSignalEntries (String eventID, Set<SignalEntryValue> values);
    void setSignalEntries    (String eventID, Set<SignalEntryValue> values);

    
    Event getByEventNameWithParticipants(String eventName);
    
	List<Event> getByDate(Date startTime, Date endTime);
	
	List<Event> getByProgramAndDate(Date startTime, Date endTime, String programName);
	
	HistoryEvent getByEventName(String eventName) throws EntityNotFoundException;
	
	List<HistoryEvent> findByDate(Date startTime, Date endTime);
	
	List<HistoryEvent> findByProgramAndDate(Date startTime, Date endTime, String programName);
	
	List<Event> findByParticipantAndDate(Date startTime, Date endTime, List<String> participantNames);
	
	List<HistoryEvent> findHisEventByParticipantAndDate(Date startTime, Date endTime, List<Object> participantNames);

    List<Event> findAllPossibleByParticipant(String participantName);
    
    List<Event> findByParticipantProgramAndDate(Date startTime, Date endTime, List<String> participantNames, List<String> programNames);
    
    
    List<HistoryEvent> findHisEventByParticipantProgramAndDate(Date startTime, Date endTime, List<Object> participantNames, List<Object> programNames);

	void removeParticipantFromEvent(String eventName, String participantName, boolean delete) ;
	
	
	void removeParticipantsFromEvent(String eventName,String participantNames);
	
	//FINE GRAINED API
	List<Event> getEventOnlyByProgramName(String programName);
	
	Event getEventOnly(String eventName);
	Event getEventWithEventSignals(String eventName);
	
	Event getEventWithParticipants(String eventName);
    Event getEventWithParticipantAndBids(String eventName);
    Event getEventWithParticipantsAndSignals(String eventName);
    Event getEventAll(String eventName);
    List<Event> findByAggregatorProgramAndDate(Date startTime, Date endTime, List<String> participantNames, List<String> programNames);
    
    List<HistoryEvent> findHisEventByAggregatorProgramAndDate(Date startTime, Date endTime, List<String> participantNames, List<String> programNames);

	void addEventParticipants(String programName, String eventName, List<EventParticipant> ep);

	/***
	 * force to terminate an event. Terminate an event immediately if endTime is null 
	 * @param programName
	 * @param eventName
	 * @param event
	 * @param endTime
	 */
	void endEvent(String programName,String eventName,Date endTime);
	void endEvent(String programName,List<String> locations,Date startTime,Date endTime);
	public Map<String,Event>findEventIdEventObjectMap(List<String> eventNames);
}
