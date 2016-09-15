/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.rds.RDSProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rds;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoInstance.Values;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.demo.DemoProgramEJBBean;
import com.akuacom.pss2.program.eventtemplate.EventTemplate;
import com.akuacom.pss2.program.eventtemplate.EventTemplateEAO;
import com.akuacom.pss2.program.eventtemplate.EventTemplateSignalEntry;
import com.akuacom.pss2.util.EventInfoInstance.SignalType;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.TimingUtil;
import com.kanaeki.firelog.util.FireLogEntry;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * The Class RDSProgramEJBBean.
 */
@Stateless
public class RDSProgramEJBBean extends DemoProgramEJBBean implements
        RDSProgramEJB.R, RDSProgramEJB.L {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(RDSProgramEJBBean.class
            .getName());

    @EJB
    private EventTemplateEAO.L eventTemplateEAO;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.demo.DemoProgramEJBBean#createProgramObject()
     */
    public Program createProgramObject() {
        return new RDSProgram();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.rds.RDSProgramEJB#updateEventTemplate(com.akuacom
     * .pss2.program.eventtemplate.EventTemplate)
     */
    public void updateEventTemplate(EventTemplate eventTemplate) {
        try {
            eventTemplateEAO.updateEventTemplate(eventTemplate);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.rds.RDSProgramEJB#createEventTemplate(com.akuacom
     * .pss2.program.eventtemplate.EventTemplate)
     */
    public void createEventTemplate(EventTemplate eventTemplate) {
        try {
            eventTemplateEAO.createEventTemplate(eventTemplate);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.rds.RDSProgramEJB#deleteEventTemplate(java.lang
     * .String)
     */
    public void deleteEventTemplate(String eventTemplateName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.rds.RDSProgramEJB#getEventTemplate(java.lang
     * .String)
     */
    public com.akuacom.pss2.program.eventtemplate.EventTemplate getEventTemplate(
            String eventTemplateName) {
        try {
            return eventTemplateEAO.getEventTemplate(eventTemplateName);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.rds.RDSProgramEJB#getEventTemplates(java.lang
     * .String)
     */
    public List<com.akuacom.pss2.program.eventtemplate.EventTemplate> getEventTemplates(
            String programName) {
        try {
            return eventTemplateEAO.findEventTemplates(programName);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.rds.RDSProgramEJB#getEventTemplateByProgram(
     * java.lang.String)
     */
    public com.akuacom.pss2.program.eventtemplate.EventTemplate getEventTemplateByProgram(
            String programName) {
        try {
            List<com.akuacom.pss2.program.eventtemplate.EventTemplate> list = this
                    .getEventTemplates(programName);
            if (list != null && list.size() > 0)
                return list.get(0);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        return null;
    }

    /**
     * Gets the pending signals.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     * 
     * @return the pending signals
     */
    private Set<EventTemplateSignalEntry> getPendingSignals(String programName,
            EventTemplate event) {
        Set<EventTemplateSignalEntry> pendingEntries = new HashSet<EventTemplateSignalEntry>();

        EventTemplateSignalEntry pendingEntry = new EventTemplateSignalEntry();
        pendingEntry.setName(event.getName());
        pendingEntry.setSignalType("pending");
        // todo we can create right away
        pendingEntry.setRelativeStartTime(TimingUtil.MINUTE_MS);
        pendingEntry.setValue("on");
        // pendingEntry.setEventTemplate(event);
        pendingEntries.add(pendingEntry);

        return pendingEntries;
    }

    /**
     * Gets the end signals.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     * 
     * @return the end signals
     */
    private Set<EventTemplateSignalEntry> getEndSignals(String programName,
            EventTemplate event) {
        Set<EventTemplateSignalEntry> pendingEntries = new HashSet<EventTemplateSignalEntry>();

        EventTemplateSignalEntry pendingEntry = new EventTemplateSignalEntry();
        pendingEntry.setName(event.getName());
        pendingEntry.setSignalType("end");

        pendingEntry.setRelativeStartTime(event.getEndTime().getTime()
                - event.getIssuedTime().getTime());
        pendingEntry.setValue("end");
        // pendingEntry.setEventTemplate(event);
        pendingEntries.add(pendingEntry);

        return pendingEntries;
    }

    /**
     * Gets the mode signals for template.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     * @param eventInfo
     *            the event info
     * 
     * @return the mode signals for template
     */
    private Set<EventTemplateSignalEntry> getModeSignalsForTemplate(
            String programName, EventTemplate event, EventInfoInstance eventInfo) {
        Set<EventTemplateSignalEntry> modeEntries = new HashSet<EventTemplateSignalEntry>();

        for (EventInfoValue value : eventInfo.getValues().getValue()) {
            EventTemplateSignalEntry modeEntry = new EventTemplateSignalEntry();
            modeEntry.setName(event.getName());
            modeEntry.setSignalType("mode");
            long startTime = event.getStartTime().getTime()
                    - event.getIssuedTime().getTime()
                    + ((int) (value.getStartTime().doubleValue() + 0.0)) * 1000;
            modeEntry.setRelativeStartTime(startTime);
            // TODO: port to rules
            // modeEntry.setValue(valueToModeString(value.getValue()));
            modeEntries.add(modeEntry);
        }
        return modeEntries;
    }

    /**
     * Gets the price signal for template.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     * @param eventInfo
     *            the event info
     * @param signalName
     *            the signal name
     * 
     * @return the price signal for template
     */
    private Set<EventTemplateSignalEntry> getPriceSignalForTemplate(
            String programName, EventTemplate event,
            EventInfoInstance eventInfo, String signalName) {
        Set<EventTemplateSignalEntry> priceEntries = new HashSet<EventTemplateSignalEntry>();

        for (EventInfoValue value : eventInfo.getValues().getValue()) {
            EventTemplateSignalEntry modeEntry = new EventTemplateSignalEntry();
            modeEntry.setName(event.getName());
            modeEntry.setSignalType(signalName);
            long startTime = event.getStartTime().getTime()
                    - event.getIssuedTime().getTime()
                    + ((int) (value.getStartTime().doubleValue() + 0.0)) * 1000;
            modeEntry.setRelativeStartTime(startTime);
            modeEntry.setValue(String.valueOf(value.getValue()));
            priceEntries.add(modeEntry);
        }
        return priceEntries;

    }

    /**
     * Gets the event default signals.
     * 
     * @param program
     *            the program
     * @param event
     *            the event
     * @param utilityDREvent
     *            the utility dr event
     * 
     * @return the event default signals
     */
    protected Set<EventTemplateSignalEntry> getSignals(Program program,
            EventTemplate event, UtilityDREvent utilityDREvent) {
        Set<EventTemplateSignalEntry> ret = getPendingSignals(
                program.getProgramName(), event);

        for (EventInfoInstance eventInfo : utilityDREvent.getEventInformation()
                .getEventInfoInstance()) {
            final String typeName = eventInfo.getEventInfoTypeName();
            if (typeName.equals("OperationModeValue")) {
                ret.addAll(getModeSignalsForTemplate(program.getProgramName(),
                        event, eventInfo));
            } else if (typeName
                    .equals(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
                            .name())) {
                ret.addAll(getPriceSignalForTemplate(program.getProgramName(),
                        event, eventInfo, typeName));
            } else if (typeName
                    .equals(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_RELATIVE
                            .name())) {
                ret.addAll(getPriceSignalForTemplate(program.getProgramName(),
                        event, eventInfo, typeName));
            } else if (typeName
                    .equals(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_MULTIPLE
                            .name())) {
                // getPriceSignal(program.getProgramName(), event, eventInfo,
                // signals, typeName);
            } else if (typeName
                    .equals(com.akuacom.pss2.util.EventInfoInstance.SignalType.LOAD_LEVEL
                            .name())) {
                // getPriceSignal(program.getProgramName(), event, eventInfo,
                // signals, typeName);
            } else if (typeName
                    .equals(com.akuacom.pss2.util.EventInfoInstance.SignalType.LOAD_AMOUNT
                            .name())) {
                // getPriceSignal(program.getProgramName(), event, eventInfo,
                // signals, typeName);
            } else if (typeName
                    .equals(com.akuacom.pss2.util.EventInfoInstance.SignalType.LOAD_PERCENTAGE
                            .name())) {
            } else if (typeName.equals("GRID_RELIABILITY")) {
                // getPriceSignal(program.getProgramName(), event, eventInfo,
                // signals, typeName);
            } else {
                FireLogEntry logEntry = new FireLogEntry();
                logEntry.setUserParam1(program.getProgramName());
                logEntry.setCategory(LogUtils.CATAGORY_EVENT);
                String message = "EventInfoTypeName " + typeName
                        + " isn't in program definition";
                logEntry.setDescription(message);
                logEntry.setLongDescr(utilityDREvent.toString());

                logEntry.setClassName(this.getClass().getName());
                log.debug(logEntry);
                throw new EJBException(message);
            }
        }

        ret.addAll(getEndSignals(program.getProgramName(), event));
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#updateEvent(java.lang.String,
     * java.lang.String, com.akuacom.pss2.core.model.Event,
     * org.openadr.dras.utilitydrevent.UtilityDREvent)
     */
    public void updateEvent(String programName, String eventName, Event event,
            org.openadr.dras.utilitydrevent.UtilityDREvent utilityDREvent) {
        // delete all the mode and price_absolute signal entries and regenerate
        // them based on the new data
        for (org.openadr.dras.eventinfo.EventInfoInstance eventInfo : utilityDREvent
                .getEventInformation().getEventInfoInstance()) {
            final String typeName = eventInfo.getEventInfoTypeName();
            if (typeName.equals("OperationModeValue")) {
                for (EventParticipant eventParticipant : event
                        .getParticipants()) {
                    final Set<EventParticipantSignal> signals = eventParticipant
                            .getSignals();
                    Iterator<EventParticipantSignal> i = signals.iterator();
                    while (i.hasNext()) {
                        EventParticipantSignal signal = (EventParticipantSignal) i
                                .next();
                        if (signal.getSignalDef().getSignalName()
                                .equals("mode")) {
                            i.remove();
                            // TODO: port to rules
                            // getModeSignals(programName, event, eventInfo,
                            // programSignals);
                            break;
                        }
                    }
                }
            } else if (typeName
                    .equals(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
                            .name())) {
                for (EventParticipant eventParticipant : event
                        .getParticipants()) {
                    final Set<EventParticipantSignal> signals = eventParticipant
                            .getSignals();
                    Iterator<EventParticipantSignal> i = signals.iterator();
                    while (i.hasNext()) {
                        EventParticipantSignal signal = (EventParticipantSignal) i
                                .next();
                        if (signal.getSignalDef().getSignalName()
                                .equals(SignalType.PRICE_ABSOLUTE.name())) {
                            i.remove();
                            // TODO: port this
                            // getPriceSignal(programName, event, eventInfo,
                            // programSignals, typeName);
                            break;
                        }
                    }
                }
            } else {
                FireLogEntry logEntry = new FireLogEntry();
                logEntry.setUserParam1(programName);
                logEntry.setCategory(LogUtils.CATAGORY_EVENT);
                String message = "EventInfoTypeName " + typeName
                        + " modification not supported";
                logEntry.setDescription(message);
                logEntry.setLongDescr(utilityDREvent.toString());
                log.warn(logEntry);
            }
        }
    }

    /**
     * Allow a program implementation to do any initial setup on one of its
     * events.
     * 
     * This method is often overridden by specialized program implementations.
     * 
     * NOTE: This is not the place to create Event-level input signals. That
     * should be done in getEventInputEventSignals
     */
    @Override
    public Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) throws ProgramValidationException {
        // event timing
        EventTiming eventTiming = new EventTiming();

        Date now = new Date();

        GregorianCalendar notificationCal = new GregorianCalendar();
        notificationCal.setTime(now);
        notificationCal.add(Calendar.SECOND, 60);
        eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(
                notificationCal));

        GregorianCalendar startCal = new GregorianCalendar();
        startCal.setTime(now);
        startCal.add(Calendar.SECOND, 120);
        eventTiming.setStartTime(new XMLGregorianCalendarImpl(startCal));

        GregorianCalendar endCal = new GregorianCalendar();
        endCal.setTime(now);
        endCal.add(Calendar.SECOND, 360);
        eventTiming.setEndTime(new XMLGregorianCalendarImpl(endCal));

        utilityDREvent.setEventTiming(eventTiming);

        EventInformation eventInfo = new EventInformation();

        EventInfoInstance modeEventInfoInstance = new EventInfoInstance();
        modeEventInfoInstance.setEventInfoTypeName("OperationModeValue");
        Values modeValues = new Values();
        modeEventInfoInstance.setValues(modeValues);
        List<EventInfoValue> modeEventInfoValues = modeValues.getValue();

        EventInfoValue eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(3.0); // high
        modeEventInfoValues.add(eventInfoValue);

        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(1.0); // normal
        modeEventInfoValues.add(eventInfoValue);

        EventInfoInstance priceEventInfoInstance = new EventInfoInstance();
        priceEventInfoInstance
                .setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
                        .name());
        Values priceValues = new Values();
        priceEventInfoInstance.setValues(priceValues);
        List<EventInfoValue> priceEventInfoValues = priceValues.getValue();

        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(0.15);
        priceEventInfoValues.add(eventInfoValue);

        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(0.08);
        priceEventInfoValues.add(eventInfoValue);

        eventInfo.getEventInfoInstance().add(modeEventInfoInstance);
        eventInfo.getEventInfoInstance().add(priceEventInfoInstance);
        utilityDREvent.setEventInformation(eventInfo);

        return super.initializeEvent(program, event, utilityDREvent);
    }

    public void createEventTemplate(String programName,
            org.openadr.dras.akuautilitydrevent.UtilityDREvent utilityDREvent) {
        // TODO Auto-generated method stub

    }

    public void updateEventTemplate(String programName,
            org.openadr.dras.akuautilitydrevent.UtilityDREvent utilityDREvent) {
        // TODO Auto-generated method stub

    }

}