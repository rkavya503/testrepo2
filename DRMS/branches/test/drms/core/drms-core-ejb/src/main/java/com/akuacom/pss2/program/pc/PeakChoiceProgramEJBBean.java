/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.pc.PeakChoiceProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.pc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import com.kanaeki.firelog.util.FireLogEntry;
import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.BidState;
import com.akuacom.pss2.program.dbp.DBPBidValidator;
import com.akuacom.pss2.program.dbp.DBPDataAccess;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.DBPProgram;
import com.akuacom.pss2.program.dbp.EventParticipantBidEntry;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantBidEntry;
import com.akuacom.pss2.program.participant.ProgramParticipantEAO;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;

import java.util.Collection;

/**
 * The Class PeakChoiceProgramEJBBean.
 */
@Stateless
public class PeakChoiceProgramEJBBean extends ProgramEJBBean implements
        PeakChoiceProgramEJB.R, PeakChoiceProgramEJB.L {

    /** The Constant log. */
    private static final Logger log = Logger
            .getLogger(PeakChoiceProgramEJBBean.class);

    /** The dbp da. */
    @EJB
    protected DBPDataAccess.L dbpDA;
    @EJB
    protected EventManager.L eventManager;
    @EJB
    private ProgramParticipantEAO.L ppEAO;

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#createProgramObject()
     */
    public Program createProgramObject() {
        return new PeakChoiceProgram();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#createEvent(java.lang.String,
     * com.akuacom.pss2.core.model.Event,
     * org.openadr.dras.utilitydrevent.UtilityDREvent)
     */
    public Collection<String> createEvent(String programName, Event event,
            UtilityDREvent utilityDREvent) {
        DBPEvent dbpEvent = (DBPEvent) event;
        ProgramManager programManager = EJBFactory
                .getBean(ProgramManager.class);
        Program program = programManager.getProgramWithLoadBid(programName);
        if (!program.getBidConfig().isDrasBidding()) {
            dbpEvent.setRespondBy(event.getStartTime());
        }
        dbpEvent.setDrasRespondBy(event.getStartTime()); // this is a dummy
                                                         // field
        return super.createEvent(programName, event, utilityDREvent);
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
    @SuppressWarnings("deprecation")
    @Override
    public Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) throws ProgramValidationException {

        String programName = program.getProgramName();
        final String eventName = event.getEventName();
        try {
            DBPEvent dbpEvent = (DBPEvent) event;
            dbpEvent.setDrasRespondBy(dbpEvent.getRespondBy());
            // dbpEvent.setBidBlocks(getBidBlocks(event));
            event.setEventStatus(EventStatus.RECEIVED);

            // note, the dbp data access currently looks up default signal
            // entries
            // by itself. no need to populate here.
            persistEvent(programName, event);

            // set signal entries for each participant
            final int startHour = event.getStartTime().getHours();
            final int endHour = event.getEndTime().getHours();
            for (EventParticipant ep : event.getParticipants()) {
                final List<BidEntry> bidEntryList = dbpDA.getDefaultBid(
                        programName, ep.getParticipant().getParticipantName(),
                        ep.getParticipant().isClient());
                final Set<EventParticipantSignalEntry> modeEntries = new HashSet<EventParticipantSignalEntry>();
                for (BidEntry bidEntry : bidEntryList) {
                    final int blockStartHour = bidEntry.getBlockStart()
                            .getHours();
                    int blockEndHour = bidEntry.getBlockEnd().getHours();
                    if (blockEndHour == 0) {
                        blockEndHour = 24;
                    }
                    // if the block is within the event scope, add signal
                    // NOTE this logic will break when cross day duration/event
                    // is introduced.
                    if (blockStartHour >= startHour && blockEndHour <= endHour) {
                        final EventParticipantSignalEntry signalEntry = new EventParticipantSignalEntry();
                        final Calendar cal = Calendar.getInstance();
                        cal.setTime(event.getStartTime());
                        cal.set(Calendar.HOUR_OF_DAY, blockStartHour);
                        signalEntry.setTime(cal.getTime());
                        signalEntry.setLevelValue(getModeLevel(bidEntry
                                .getPriceLevel()));
                        modeEntries.add(signalEntry);
                    }
                }
                // add ending signal
                final EventParticipantSignalEntry signalEntry = new EventParticipantSignalEntry();
                signalEntry.setTime(event.getEndTime());
                signalEntry.setLevelValue("normal");
                modeEntries.add(signalEntry);

                final Set<EventParticipantSignalEntry> pendingEntries = getPendingSignalEntries(
                        dbpEvent, false, program.getPendingLeadMS(event));

                // persist entries
                for (Signal signal : ep.getSignals()) {
                    if ("mode".equals(signal.getSignalDef().getSignalName())) {
                        signal.setSignalEntries(modeEntries);
                        break;
                    }
                    if ("pending".equals(signal.getSignalDef().getSignalName())) {
                        signal.setSignalEntries(pendingEntries);
                        break;
                    }
                }
                eventManager.setEventParticipant(ep);
            }

            log.info(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, "event created: " + eventName,
                    event.toString()));

            // notify notifications
            sendDRASOperatorNotifications(event, "created");
            sendProgramOperatorNotifications(event, "created");

            return super.initializeEvent(program, event, utilityDREvent);
        } catch (Exception e) {
            String message = "can't add event: " + eventName;
            log.debug(LogUtils.createLogEntry(programName, this.getClass()
                    .getName(), message, event.toString()));
            log.debug(LogUtils.createExceptionLogEntry(programName, this
                    .getClass().getName(), e));
            throw new EJBException(message, e);
        }
    }

    @Override
    public Event persistEvent(String programName, Event event) {
        if (programName == null) {
            String message = "program name is null";
            log.error(message);
            throw new EJBException(message);
        }
        if (event == null) {
            String message = "event is null";
            log.error(message);
            throw new EJBException(message);
        }

        try {
            final String eventName = event.getEventName();
            if (!eventEAO.findByEventName(eventName).isEmpty()) {
                String message = "event " + eventName
                        + " already exists for program " + programName;
                // DRMS-1664
                log.warn(message);
                throw new EJBException(message);
            }

            // add the participants
            for (EventParticipant ep : event.getParticipants()) {
                final Set<EventParticipantBidEntry> bidEntryDAOs = new HashSet<EventParticipantBidEntry>();
                final List<BidEntry> bidEntryList;
                bidEntryList = getDefaultBid(programName, ep.getParticipant()
                        .getParticipantName(), ep.getParticipant().isClient());
                if (bidEntryList != null) {
                    for (BidEntry bidEntry : bidEntryList) {
                        final Date startTime = DateUtil.mergeDate(
                                bidEntry.getBlockStart(), event.getStartTime());
                        final Date endTime = DateUtil.mergeDate(
                                bidEntry.getBlockEnd(), event.getEndTime());
                        // only save entries that is inside event scope
                        if (!startTime.before(event.getStartTime())
                                && !endTime.after(event.getEndTime())) {
                            EventParticipantBidEntry beDao = new EventParticipantBidEntry();
                            beDao.setUUID(bidEntry.getId());
                            beDao.setActive(bidEntry.isActive());
                            beDao.setEndTime(endTime);
                            beDao.setEventParticipant(ep);
                            beDao.setPriceLevel(bidEntry.getPriceLevel());
                            beDao.setReductionKW(bidEntry.getReductionKW());
                            beDao.setStartTime(startTime);
                            bidEntryDAOs.add(beDao);
                        }
                    }
                }
                ep.setBidEntries(bidEntryDAOs);
            }
            event = eventEAO.create(event);

//            reportParticipation(event);
            return event;
        } catch (Exception e) {
            String message = "error adding event for program:" + programName;
            // DRMS-1664
            log.debug(message, e);
            throw new EJBException(message, e);
        }
    }

    /**
     * Gets the mode level.
     * 
     * @param v
     *            the v
     * 
     * @return the mode level
     */
    private String getModeLevel(double v) {
        String level;
        switch ((int) v) {
        case 3:
            level = "moderate";
            break;
        case 5:
            level = "high";
            break;
        default:
            level = "normal";
        }
        return level;
    }

    // add program participant and set up default levels
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJBBean#addParticipant(java.lang.String,
     * java.lang.String)
     */
    public void addParticipant(String programName, String participantName, boolean isClient) {
        try {

            ProgramParticipant pp = ppEAO.findProgramParticipantsByProgramNameAndPartName(
                    programName, participantName);
            if (pp != null && pp.getState() == ProgramParticipant.PROGRAM_PART_ACTIVE) {
                String message = "participant " + participantName + " already exists for program " + programName;
                log.info(message);
                throw new EJBException(message);
            } else {
                // initialize the standing bid
                // add new participant to a program
                pp = ppEAO.createProgramParticipant( programName, participantName, isClient);

                Set<ProgramParticipantBidEntry> bidEntries = new HashSet<ProgramParticipantBidEntry>();
                final Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                for (int i = 0; i < 24; i++) {
                    ProgramParticipantBidEntry bidEntry = new ProgramParticipantBidEntry();
                    cal.set(Calendar.HOUR_OF_DAY, i);
                    final Date date = cal.getTime();
                    bidEntry.setBlockStart(date);
                    bidEntry.setBlockEnd(new Date(date.getTime() + 3600 * 1000));
                    bidEntry.setPriceLevel(3.0);
                    bidEntry.setProgramParticipant(pp);
                    bidEntry.setActive(true);
                    bidEntries.add(bidEntry);
                }

                pp.setBidEntries(bidEntries);

                ppEAO.createProgramParticipant(pp);
            }

        } catch (Exception e) {
            String message = "error adding participant " + participantName
                    + " to program " + programName;
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(programName);
            logEntry.setCategory(this.getClass().getName());
            logEntry.setDescription(message);
            log.debug(logEntry);
            throw new EJBException(message, e);
        }
    }

    /**
     * Gets the pending signal entries.
     * 
     * @param event
     *            the event
     * @param immediate
     *            the immediate
     * @param pendingLeadMS
     *            the pending lead ms
     * 
     * @return the pending signal entries
     */
    private Set<EventParticipantSignalEntry> getPendingSignalEntries(
            DBPEvent event, boolean immediate, long pendingLeadMS) {

        // set up the pending signal
        Set<EventParticipantSignalEntry> results = new HashSet<EventParticipantSignalEntry>();

        // pending = on at issue
        EventParticipantSignalEntry on = new EventParticipantSignalEntry();
        on.setTime(getPendingOnTime(event, immediate, pendingLeadMS));
        on.setLevelValue("on");
        results.add(on);

        // pending = off at end
        EventParticipantSignalEntry off = new EventParticipantSignalEntry();
        off.setTime(event.getEndTime());
        off.setLevelValue("off");
        results.add(off);

        return results;
    }

    /**
     * Gets the pending on time.
     * 
     * @param event
     *            the event
     * @param immediate
     *            the immediate
     * @param pendingLeadMS
     *            the pending lead ms
     * 
     * @return the pending on time
     */
    private Date getPendingOnTime(DBPEvent event, boolean immediate,
            long pendingLeadMS) {
        Date pendingOnTime;
        if (!immediate
                && (event.getIssuedTime().getTime() < event.getStartTime()
                        .getTime() - pendingLeadMS)) {
            // issue time is before start of pending, so turn in on using calc
            pendingOnTime = new Date(event.getStartTime().getTime()
                    - pendingLeadMS);
        } else {
            // turn pending on now
            pendingOnTime = new Date();
        }
        return pendingOnTime;
    }

    // check to see if the event is active for this signal for this participant
    // TODO: this can be done in a query
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJBBean#isParticipantEventSignalActive(java
     * .lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean isParticipantEventSignalActive(String programName,
            String eventName, String participantName, boolean isClient,
            String signalName) {
        try {
            ProgramManager programManager = EJBFactory
                    .getBean(ProgramManager.class);
            Program program = programManager.getProgramOnly(programName);
            DBPEvent event = (DBPEvent) eventEAO.findByEventNameProgramName(
                    eventName, programName);
            for (EventParticipant eventParticipant : event.getParticipants()) {
                if (eventParticipant.getParticipant().getParticipantName()
                        .equals(participantName)) {
                    long nowMS = new Date().getTime();
                    // only consider events that have been issued
                    if (nowMS < event.getIssuedTime().getTime()) {
                        return false;
                    }
                    // only consider events for which processing is complete (
                    // i.e. bids have been accepted)
                    if (event.getCurrentBidState() != BidState.PROCESSING_COMPLETE) {
                        return false;
                    }
                    if (signalName.equals("pending")) {
                        // only true if we are between the pending on time and
                        // the end time
                        if (nowMS > event.getStartTime().getTime()
                                - program.getPendingLeadMS(event)
                                && nowMS < event.getEndTime().getTime()) {
                            return true;
                        }
                    } else if (signalName.equals("mode")) {
                        // only true if we are between the start time and
                        // the end time
                        if (nowMS > event.getStartTime().getTime()
                                && nowMS < event.getEndTime().getTime()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            String message = "isParticipantEventSignalActive failed";
            log.debug(LogUtils.createLogEntry(programName, this.getClass()
                    .getName(), message, "event = " + eventName
                    + ", participant = " + participantName + "signalName = "
                    + signalName));
            log.debug(LogUtils.createExceptionLogEntry(programName, this
                    .getClass().getName(), e));

            throw new EJBException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.pc.PeakChoiceProgramEJB#getDefaultBid(java.lang
     * .String, java.lang.String)
     */
    public List<BidEntry> getDefaultBid(String programName,
            String participantName, boolean isClient) {
        return dbpDA.getDefaultBid(programName, participantName, isClient);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.pc.PeakChoiceProgramEJB#setDefaultBid(java.lang
     * .String, java.lang.String, java.util.List)
     */
    public void setDefaultBid(String programName, String participantName,
            boolean isClient, List<BidEntry> bids)
            throws ProgramValidationException {
        ProgramManager programManager = EJBFactory
                .getBean(ProgramManager.class);
        DBPProgram program = (DBPProgram) programManager
                .getProgramWithLoadBid(programName);
        DBPBidValidator validator = (DBPBidValidator) ValidatorFactory
                .getProgramValidator(program);
        validator.validateDefaultBids(program, bids);
        dbpDA.setDefaultBid(programName, participantName, isClient, bids);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#newProgramEvent()
     */
    public Event newProgramEvent() {
        return new DBPEvent();
    }
}
