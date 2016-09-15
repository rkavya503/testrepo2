/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.demo.DemoProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.demo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.utils.lang.Dbg;
import com.akuacom.utils.lang.StackTraceUtil;

import java.util.Collection;

/**
 * The Class DemoProgramEJBBean.
 */
@Stateless
public class DemoProgramEJBBean extends ProgramEJBBean implements
DemoProgramEJB.R, DemoProgramEJB.L {

    private static final Logger log = Logger.getLogger(ProgramEJBBean.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.core.ProgramEJBBean#createProgramObject()
	 */
	public Program createProgramObject() {
		return new DemoProgram();
	}

	/**
	 * Gets the pending signals.
	 * 
	 * @param programName
	 *            the program name
	 * @param event
	 *            the event
	 * @param programSignals
	 *            the program signals
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected Set<EventParticipantSignalEntry> getPendingSignalEntries(
			Program program, EventTiming eventTiming, Date now) {
		Set<EventParticipantSignalEntry> pendingEntries = new HashSet<EventParticipantSignalEntry>();
		EventParticipantSignalEntry pendingEntry = new EventParticipantSignalEntry();
		pendingEntry.setTime(eventTiming.getNearTime());
		pendingEntry.setLevelValue("on");
		pendingEntries.add(pendingEntry);

		return pendingEntries;
	}

	/**
	 * Gets the mode signals.
	 * 
	 * @param programName
	 *            the program name
	 * @param event
	 *            the event
	 * @param eventInfo
	 *            the event info
	 * @param rules
	 *            the rules
	 */
	@SuppressWarnings("deprecation")
	protected void getModeSignals(String programName, EventTiming event,
			org.openadr.dras.eventinfo.EventInfoInstance eventInfo,
			List<EventParticipantRule> rules) {
		GregorianCalendar calender = new GregorianCalendar();
		calender.setTime(event.getStartTime());
		for (org.openadr.dras.eventinfo.EventInfoValue value : eventInfo
				.getValues().getValue()) {
			calender.setTime(event.getStartTime());
			calender.add(Calendar.SECOND, (int) (value.getStartTime() + 0.5));
			EventParticipantRule rule = new EventParticipantRule();
			rule.setStart(calender.getTime());
			rule.setMode(valueToMode(value.getValue()));
			rule.setOperator(Operator.ALWAYS);
			rules.add(rule);
		}
		// fill in the end times
		Date endTime = event.getEndTime();
		for (int i = (rules.size() - 1); i >= 0; i--) {
			EventParticipantRule rule = rules.get(i);
			rule.setEnd(endTime);
			endTime = rule.getStart();
		}
	}

	/**
	 * Gets the price signal.
	 * 
	 * @param programName
	 *            the program name
	 * @param event
	 *            the event
	 * @param eventInfo
	 *            the event info
	 * @param signals
	 *            the signals
	 * @param signalName
	 *            the signal name
	 */
	@SuppressWarnings("deprecation")
	protected EventSignal getNumberSignal(String programName, Event event,
			org.openadr.dras.eventinfo.EventInfoInstance eventInfo,
			String signalName) {
		Signal signal = super.getSignal(programName, signalName);

		EventSignal eventSignal = new EventSignal();
		eventSignal.setSignalDef(signal.getSignalDef());
		Set<EventSignalEntry> entries = new HashSet<EventSignalEntry>();
		GregorianCalendar calender = new GregorianCalendar();
		calender.setTime(event.getStartTime());
		for (org.openadr.dras.eventinfo.EventInfoValue value : eventInfo
				.getValues().getValue()) {
			EventSignalEntry entry = new EventSignalEntry();
			calender.setTime(event.getStartTime());
			calender.add(Calendar.SECOND, (int) (value.getStartTime() + 0.5));
			entry.setTime(calender.getTime());
			entry.setNumberValue(value.getValue());
			entries.add(entry);
		}
		eventSignal.setSignalEntries(entries);
		return eventSignal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.core.ProgramEJBBean#getEventSignals(com.akuacom.pss2
	 * .core.model.Program, com.akuacom.pss2.core.model.Event,
	 * org.openadr.dras.utilitydrevent.UtilityDREvent)
	 */
	@Override
	public Set<EventSignal> initializeEvent(Program program, Event event,
			UtilityDREvent utilityDREvent) throws ProgramValidationException {
		Set<EventSignal> inputSignals = new HashSet<EventSignal>();

		for (org.openadr.dras.eventinfo.EventInfoInstance eventInfo : utilityDREvent
				.getEventInformation().getEventInfoInstance()) {
			final String typeName = eventInfo.getEventInfoTypeName();
			// skip mode
			if (!typeName.equals("OperationModeValue")) {
				inputSignals.add(getNumberSignal(program.getProgramName(),
						event, eventInfo, typeName));
			}
		}
		return inputSignals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.core.ProgramEJBBean#getEventRules(com.akuacom.pss2.core
	 * .model.Program, com.akuacom.pss2.core.model.Event,
	 * org.openadr.dras.utilitydrevent.UtilityDREvent)
	 */
    @Override
	protected List<EventParticipantRule> getProgramRules(Program program,
			EventTiming event, UtilityDREvent utilityDREvent) {
		List<EventParticipantRule> rules = new ArrayList<EventParticipantRule>();

		for (org.openadr.dras.eventinfo.EventInfoInstance eventInfo : utilityDREvent
				.getEventInformation().getEventInfoInstance()) {
			final String typeName = eventInfo.getEventInfoTypeName();

			if (typeName.equals("OperationModeValue")) {
				getModeSignals(program.getProgramName(), event, eventInfo,
						rules);
				break;
			}
		}
		return rules;
	}

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramDataAccess#persistEvent(java.lang.String,
     * com.akuacom.pss2.core.model.Event, java.util.List)
     */
    @Override
    protected Event persistEvent(String programName, Event event) {
        StackTraceUtil.dumpStack();
        if (programName == null) {
            String message = "program name is null";
            throw new EJBException(message);
        }
        if (event == null) {
            String message = "event is null";
            throw new EJBException(message);
        }

        try {
            log.debug("parts " + Dbg.oS(event.getParticipants()));
            event = eventEAO.create(event);
            return event;
        } catch (Exception e) {
            String message = "error adding event for program:" + programName;
            throw new EJBException(message, e);
        }
    }
 
}
