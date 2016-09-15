/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.rds.RDSValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.signal.SignalLevelDef;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.util.PSS2Util;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * The Class RDSValidator.
 */
public class RDSValidator extends ProgramValidator {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(ProgramValidator.class);

    /** The TIM e_ buffe r_ ms. */
    private static long TIME_BUFFER_MS = 60 * 1000; // 1 min

    /**
     * confirm the event confirms to the rules
     * 
     * things to check: - requried tags exists.
     * 
     *
     * @param event
     *            the event
     * @throws ProgramValidationException
     *             the program validatation exception
     */
    // empty list == no errors
    public void validateEvent(Event event)
            throws ProgramValidationException {
        List<ProgramValidationMessage> errors = new ArrayList<ProgramValidationMessage>();
        List<ProgramValidationMessage> warnings = new ArrayList<ProgramValidationMessage>();

        // check the name
        if (!PSS2Util.isLegalName(event.getEventName())) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("illegal name ("
                    + PSS2Util.LEGAL_NAME_DESCRIPTION + ")");
            error.setParameterName("eventName");
            errors.add(error);
        }
        if (event.getEventName().length() > PSS2Util.MAX_EVENT_NAME_LENGTH) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("event name can't be more than "
                    + PSS2Util.MAX_EVENT_NAME_LENGTH + " characters");
            error.setParameterName("eventName");
            errors.add(error);
        }

        long nowMS = new Date().getTime();
        GregorianCalendar issueCal = new GregorianCalendar();
        issueCal.setTime(event.getIssuedTime());
        GregorianCalendar maxIssueCal = new GregorianCalendar(
                issueCal.get(Calendar.YEAR), issueCal.get(Calendar.MONTH),
                issueCal.get(Calendar.DAY_OF_MONTH),
                program.getMaxIssueTimeH(), program.getMaxIssueTimeM(), 59);
        maxIssueCal.set(Calendar.MILLISECOND, 999);
        GregorianCalendar startCal = new GregorianCalendar();
        startCal.setTime(event.getStartTime());
        GregorianCalendar minStartCal = new GregorianCalendar(
                startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH),
                startCal.get(Calendar.DAY_OF_MONTH),
                program.getMinStartTimeH(), program.getMinStartTimeM(), 0);
        GregorianCalendar maxStartCal = new GregorianCalendar(
                startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH),
                startCal.get(Calendar.DAY_OF_MONTH),
                program.getMaxStartTimeH(), program.getMaxStartTimeM(), 59);
        maxStartCal.set(Calendar.MILLISECOND, 999);
        GregorianCalendar endCal = new GregorianCalendar();
        endCal.setTime(event.getEndTime());
        GregorianCalendar minEndCal = new GregorianCalendar(
                endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH),
                endCal.get(Calendar.DAY_OF_MONTH), program.getMinEndTimeH(),
                program.getMinEndTimeM(), 0);
        GregorianCalendar maxEndCal = new GregorianCalendar(
                endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH),
                endCal.get(Calendar.DAY_OF_MONTH), program.getMaxEndTimeH(),
                program.getMaxEndTimeM(), 59);
        maxEndCal.set(Calendar.MILLISECOND, 999);

        // check that requires tags exist (none for base class)

        // make sure issue time is within the last 60 seconds
        if (event.getIssuedTime().getTime() < (nowMS - TIME_BUFFER_MS)) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("issue time is in the past");
            error.setParameterName("issuedTime");
            errors.add(error);
        }

        // confirm issue day is in range
        if (startCal.get(Calendar.YEAR) != issueCal.get(Calendar.YEAR)
                || startCal.get(Calendar.DAY_OF_YEAR) != issueCal
                        .get(Calendar.DAY_OF_YEAR)) {
            // it isn't a day of, so just make sure the day is in the future
            boolean issueError = false;
            if (startCal.get(Calendar.DAY_OF_YEAR) == 1) {
                // if the start is on jan 1st, then the issue year must be
                // less
                // than the start year
                if (startCal.get(Calendar.YEAR) <= issueCal.get(Calendar.YEAR)) {
                    issueError = true;
                }
            } else {
                if (startCal.get(Calendar.YEAR) < issueCal.get(Calendar.YEAR)) {
                    issueError = true;
                } else if (startCal.get(Calendar.YEAR) == issueCal
                        .get(Calendar.YEAR)
                        && startCal.get(Calendar.DAY_OF_YEAR) <= issueCal
                                .get(Calendar.DAY_OF_YEAR)) {
                    issueError = true;
                }
            }
            if (issueError) {
                ProgramValidationMessage error = new ProgramValidationMessage();
                error.setDescription("start day must be on or after issue day");
                error.setParameterName("issuedTime");
                errors.add(error);
            }
        }

        // confirm the issue time is in range
        boolean issueTimeError = false;
        if (issueCal.get(Calendar.HOUR_OF_DAY) > maxIssueCal
                .get(Calendar.HOUR_OF_DAY)) {
            issueTimeError = true;
        } else if (issueCal.get(Calendar.HOUR_OF_DAY) == maxIssueCal
                .get(Calendar.HOUR_OF_DAY)
                && issueCal.get(Calendar.MINUTE) > maxIssueCal
                        .get(Calendar.MINUTE)) {
            issueTimeError = true;
        }
        if (issueTimeError) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("issue time must be before "
                    + maxIssueCal.get(Calendar.HOUR_OF_DAY) + ":"
                    + maxIssueCal.get(Calendar.MINUTE));
            error.setParameterName("issuedTime");
            warnings.add(error);
        }

        // confirm start time is in range
        if (minStartCal.getTimeInMillis() > startCal.getTimeInMillis()
                || maxStartCal.getTimeInMillis() < startCal.getTimeInMillis()) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("start time must be between "
                    + program.getMinStartTimeH() + ":"
                    + program.getMinStartTimeM() + " and "
                    + program.getMaxStartTimeH() + ":"
                    + program.getMaxStartTimeM());
            error.setParameterName("startTime");
            errors.add(error);
        }

        // confirm end time is in range
        if (event.getEndTime().getTime() < event.getStartTime().getTime()) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("end time is before start time");
            error.setParameterName("endTime");
            errors.add(error);
        }

        if (minEndCal.getTimeInMillis() > endCal.getTimeInMillis()
                || maxEndCal.getTimeInMillis() < endCal.getTimeInMillis()) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("end time must be between "
                    + program.getMinEndTimeH() + ":" + program.getMinEndTimeM()
                    + " and " + program.getMaxEndTimeH() + ":"
                    + program.getMaxEndTimeM());
            error.setParameterName("endTime");
            errors.add(error);
        }

        // confirm event duration is within range
        long durationMS = event.getEndTime().getTime()
                - event.getStartTime().getTime();
        if (durationMS < program.getMinDurationMS()
                || durationMS > program.getMaxDurationMS()) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("event duration must be between "
                    + program.getMinDurationM() + " and "
                    + program.getMaxDurationM() + " minutes");
            error.setParameterName("endTime");
            warnings.add(error);
        }

        if (warnings.size() > 0) {
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(event.getProgramName());
            logEntry.setDescription("event validation failed");
            logEntry.setLongDescr(warnings.toString() + "\n" + event);
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            log.error(logEntry);
        }

        if (errors.size() > 0) {
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(event.getProgramName());
            logEntry.setDescription("event validation failed (creation aborted)");
            logEntry.setLongDescr(errors.toString() + "\n" + event);
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            log.error(logEntry);
            ProgramValidationException error = new ProgramValidationException();
            error.setErrors(errors);
            throw error;
        }

    }

    /**
     * confirm the signal entries generated are legal
     * 
     * empty list == no errors.
     * 
     * @param event
     *            DocMe!
     * @param utilityDREvent
     *            DocMe!
     * @param signalEntries
     *            DocMe!
     * 
     * @throws ProgramValidationException
     *             DocMe!
     */
    public void validateEvent(Event event,
            UtilityDREvent utilityDREvent,
            List<SignalEntry> signalEntries)
            throws ProgramValidationException {
        if (signalEntries == null) {
            return;
        }

        List<ProgramValidationMessage> errors = new ArrayList<ProgramValidationMessage>();

        // make sure signals are supprted by the program
        for (SignalEntry signalEntry : signalEntries) {
            for (ProgramSignal signal : program.getSignals()) {
                if (signalEntry.getParentSignal().getSignalDef()
                        .getSignalName()
                        .equals(signal.getSignalDef().getSignalName())) {
                    if (signalEntry.getParentSignal().getSignalDef()
                            .isLevelSignal()) {
                        if (!(signal.getSignalDef().isInputSignal())) {
                            String message = "signal types don't match";
                            ProgramValidationMessage error = new ProgramValidationMessage();
                            error.setDescription(message);
                            error.setParameterName(signalEntry.toString());
                            errors.add(error);
                            break;
                        }
                        boolean found = false;
                        for (SignalLevelDef level : signal.getSignalDef()
                                .getSignalLevels()) {
                            if (level.getStringValue().equals(
                                    signalEntry.getLevelValue())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            String message = "entry signal level not in program signal";
                            ProgramValidationMessage error = new ProgramValidationMessage();
                            error.setDescription(message);
                            error.setParameterName(signalEntry.toString());
                            errors.add(error);
                        }
                    } else if (!signalEntry.getParentSignal().getSignalDef()
                            .isLevelSignal()) {
                        // TODO: fill in validation rules here.
                    } else {
                        String message = "only level signals supported right now";
                        ProgramValidationMessage error = new ProgramValidationMessage();
                        error.setDescription(message);
                        error.setParameterName(signalEntry.toString());
                        errors.add(error);
                    }
                }
            }

            // TODO: make sure the time are valid
        }

        if (errors.size() > 0) {
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(event.getProgramName());
            logEntry.setDescription("event signal entry validation failed");
            logEntry.setLongDescr(errors.toString() + "\n" + event + "\n"
                    + signalEntries);
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            log.warn(logEntry);
            ProgramValidationException error = new ProgramValidationException();
            error.setErrors(errors);
            throw error;
        }
    }
}
