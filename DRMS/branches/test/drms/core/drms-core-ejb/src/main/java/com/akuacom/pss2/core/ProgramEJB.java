/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantRtpStrategy;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.util.EventState;

/**
 * Stateless session bean providing a facade for DRAS programs.
 */

public interface ProgramEJB {
    @Remote
    public interface R extends ProgramEJB {}
    @Local
    public interface L extends ProgramEJB {}

    public Program createProgramObject();

    /**
     * Load program.
     * 
     * @param config
     *            the config
     * @param programNum
     *            the program num
     * 
     * @return the program
     */
    Program loadProgram(Properties config, int programNum);

    /**
     * Initialize.
     * 
     * @param programName
     *            the program name
     */
    void initialize(String programName);
    
    /**
     * Creates timer
     * @param programName
     */
    void createTimer(String programName);

    /**
     * Creates the event.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     * @param utilityDREvent
     *            the utility dr event
     */
    // TODO This really should return the events themselves
    Collection<String> createEvent(String programName, Event event,
            UtilityDREvent utilityDREvent);

    /**
     * Delete event.
     * 
     * @param event
     *            the event
     */
    void deleteEvent(Event event);

    /**
     * Gets the events.
     * 
     * @param programName
     *            the program name
     * 
     * @return the events
     */
    List<EventInfo> getEvents(String programName);

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
     * Cancel event.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     */
    void cancelEvent(String programName, String eventName);

    /**
     * Gets the event status string.
     * 
     * @param event
     *            the event
     * 
     * @return the event status string
     */
    String getEventStatusString(Event event);

    /**
     * Adds the participant.
     * 
     * @param programName
     *            the program name
     * @param participantName
     *            the participant name
     * @param isClient
     *            is client flag
     */
    void addParticipant(String programName, String participantName,
            boolean isClient);

    void addParticipant(String programName, List<Participant> participants,
            boolean isClient);
    

    
    /**
     * Gets the participants.
     * 
     * @param programName
     *            the program name
     * 
     * @return the participants
     */
    List<String> getParticipants(String programName);

    /**
     * Gets called every 5 seconds to generate events and update pending.
     * 
     * @param event
     *            the event
     */
    void tick5Seconds(Event event);

    /**
     * Checks if is participant in program.
     * 
     * @param programName
     *            the program name
     * @param participantName
     *            the participant name
     * 
     * @param isClient
     *            is client flag
     * @return true, if is participant in program
     */
    boolean isParticipantInProgram(String programName, String participantName,
            boolean isClient);

    /**
     * Checks if is participant event signal active.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param participantName
     *            the participant name
     * @param isClient
     *            is client flag
     * @param signalName
     *            the signal name
     * 
     * @return true, if is participant event signal active
     */
    boolean isParticipantEventSignalActive(String programName,
            String eventName, String participantName, boolean isClient,
            String signalName);

    /**
     * Gets the program config jsp page - NOT IMPLEMENTED.
     * 
     * @param programName
     *            the program name
     * 
     * @return the program config jsp page
     */
    String getProgramConfigJSPPage(String programName);

    /**
     * Gets the event config jsp page - NOT IMPLEMENTED.
     * 
     * @param programName
     *            the program name
     * 
     * @return the event config jsp page
     */
    String getEventConfigJSPPage(String programName);

    /**
     * Update event - NOT IMPLEMENTED.
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
     * New event state.
     * 
     * @return the event state
     */
    EventState newEventState();

    /**
     * Creates the dr event data.
     * 
     * @param sb
     *            the sb
     * @param eventState
     *            the event state
     * 
     * @return the string builder
     */
    StringBuilder createDREventData(StringBuilder sb, EventState eventState);

    /**
     * New program event.
     * 
     * @return the event
     */
    Event newProgramEvent();

    /**
     * Get the rule variables supported by the program
     * 
     * @param programName
     *            program name
     * @return the list of rule variables
     */
    List<String> getProgramRuleVariables(String programName);

    Set<EventParticipantSignal> getClientForecastSignals(Program program,
            EventTiming eventTiming, String clientName, boolean isClient,
            Set<? extends Signal> inputSignals, Date now)
            throws ProgramValidationException;

    void processParticipantRulesAndSignals(Program programWithPPandPRules, Event event,
            UtilityDREvent utilityDREvent, EventParticipant eventParticipant,
            Set<ProgramSignal> programInputSignals,
            Set<EventSignal> eventInputSignals, Date now,
            SignalDef pendingSignalDef, SignalDef modeSignalDef)
            throws ProgramValidationException;

 	public Set<EventParticipantSignal> processClientRulesAndSignals(
		Program program, Event event,
		UtilityDREvent utilityDREvent,
		EventParticipant eventParticipant,
            EventParticipant clientParticipant,
            Set<? extends Signal> inputSignals, Date now,
            SignalDef pendingSignalDef, SignalDef modeSignalDef)
            throws ProgramValidationException;

    /**
     * This is where signals that are common to all events in a program are
     * obtained An example might be a global price that is always in effect for
     * all events and all clients
     * 
     * @param program
     *            program
     * @return set
     */
    Set<ProgramSignal> getProgramInputEventSignals(Program program);

    /**
     * This is where signals that are global for all participants within any
     * given even are obtained. Examples might include RTP prices during the
     * event period, provided everybody is subject to the same price.
     * 
     * @param program
     *            program
     * @param event
     *            event
     * @param utilityDREvent
     *            utility event
     * 
     * @return set
     * @throws ProgramValidationException
     *             e
     */
    Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) throws ProgramValidationException;

    List<ProgramParticipantRule> createDefaultClientRules(Program programWithRules);
    
    Set<EventParticipant> createEventClients(Set<EventParticipant> eventParticipants, Program program);
    
    Set<EventParticipant> createAggregatedEventParticipants(Set<EventParticipant> eventParticipants, Program program);

    void addParticipant(Event e, Participant p);

	void addEventParticipant(String programName, String eventName, List<EventParticipant> eps);
	
	/**
	 * Generate default shed strategy for a given client, currently only useful for SCE RTP 2013 
	 * @param client
	 * @return
	 */
	void generateDefaultStrategy(ProgramParticipant pp);

	/**
	 * force to terminate an event. Terminate event immediately if @endTime is null 
	 * @param programName
	 * @param eventName
	 * @param event
	 * @endTime 
	 */
	void endEvent(String programName, String eventName, Date endTime);
	
	void updateProgramAggregatorTable(String eventName);
    
}
