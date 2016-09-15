/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ProgramValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.bidding.BidConfig;
import com.akuacom.pss2.program.matrix.ProgramMatrixTrig;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.signal.SignalLevelDef;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.util.PSS2Util;
import com.akuacom.utils.lang.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * Validates DRAS entities against rules.
 */
public class ProgramValidator implements Serializable {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(ProgramValidator.class);

    /** The program. */
    protected Program program;

    /** The size of the time buffer in msec. */
    protected static long TIME_BUFFER_MS = 60 * 1000; // 1 min

    protected GregorianCalendar issueCal;
    protected GregorianCalendar maxIssueCal;
    protected GregorianCalendar startCal;
    protected GregorianCalendar maxStartCal;
    protected GregorianCalendar minStartCal;
    protected GregorianCalendar endCal;
    protected GregorianCalendar maxEndCal;
    protected GregorianCalendar minEndCal;
    protected Event event;
    protected List<ProgramValidationMessage> errors = new ArrayList<ProgramValidationMessage>();
    protected List<ProgramValidationMessage> warnings = new ArrayList<ProgramValidationMessage>();
    protected long nowMS;
    protected UtilityDREvent utilityDREvent;
    protected int minIssueToStartM;

    /**
     * Instantiates a new program validator.
     */
    public ProgramValidator() {
    }

    protected void validateAgainstExistingEvents() {
    }

    protected void validateIssueTime() {
        // make sure issue time is within the last 60 seconds
        if (event.getIssuedTime().getTime() < (nowMS - TIME_BUFFER_MS)) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("issue time is in the past");
            error.setParameterName("issuedTime");
            errors.add(error);
        }

        // confirm issue day is in range
            if (program.isMustIssueBDBE()) {
                // TODO: for now, since we don't have a holiday interface, we'll
                // just make sure it is at least the day before. we really
                // should
                // check for weekends and holidays
                boolean issueError = false;
                if (startCal.get(Calendar.DAY_OF_YEAR) == 1) {
                    // if the start is on jan 1st, then the issue year must be
                    // less
                    // than the start year
                    if (startCal.get(Calendar.YEAR) <= issueCal
                            .get(Calendar.YEAR)) {
                        issueError = true;
                    }
                } else {
                    if (startCal.get(Calendar.YEAR) < issueCal
                            .get(Calendar.YEAR)) {
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
                    error.setDescription("issue day must be the business day before the start day");
                    error.setParameterName("issuedTime");
                    errors.add(error);

                }
            } else {
                if (startCal.get(Calendar.YEAR) != issueCal.get(Calendar.YEAR)
                        || startCal.get(Calendar.DAY_OF_YEAR) != issueCal
                                .get(Calendar.DAY_OF_YEAR)) {
                    ProgramValidationMessage error = new ProgramValidationMessage();
                    error.setDescription("issue day must be the same day as start day");
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
                    + DateUtil.dateFormatHHmm().format(maxIssueCal.getTime()));
            error.setParameterName("issuedTime");
            warnings.add(error);
        }
        // DA: throw exception if issue time > pending on signal time
        if (program.isMustIssueBDBE()) {
            issueTimeError = false;
            if (issueCal.get(Calendar.HOUR_OF_DAY) > program
                    .getPendingTimeDBEH()) {
                issueTimeError = true;
            } else if (issueCal.get(Calendar.HOUR_OF_DAY) == program
                    .getPendingTimeDBEH()
                    && issueCal.get(Calendar.MINUTE) > program
                            .getPendingTimeDBEM()) {
                issueTimeError = true;
            }
            if (issueTimeError) {
                Calendar pendingTimeCal = new GregorianCalendar();
                pendingTimeCal.set(Calendar.HOUR_OF_DAY,
                        program.getPendingTimeDBEH());
                pendingTimeCal.set(Calendar.MINUTE,
                        program.getPendingTimeDBEM());
                ProgramValidationMessage error = new ProgramValidationMessage();
                error.setDescription("issue time must be before pending time on ("
                        + DateUtil.dateFormatHHmm().format(pendingTimeCal.getTime()) + ")");
                error.setParameterName("issuedTime");
                errors.add(error);
            }
        }
    }

    protected void validateNearTime() {
        // confirm near time is in range
    	// TODO: this should never be null
    	if(event.getNearTime() != null) {
	        if (event.getNearTime().getTime() < event.getIssuedTime().getTime()) {
	            ProgramValidationMessage error = new ProgramValidationMessage();
	            error.setDescription("near time is before issue time");
	            error.setParameterName("nearTime");
	            errors.add(error);
	        }
    	} 
    }
        
    protected void validateStartTime() {
        // confirm start time is in range
    	if(event.getNearTime() != null) {
	        if (event.getStartTime().getTime() < event.getNearTime().getTime()) {
	            ProgramValidationMessage error = new ProgramValidationMessage();
	            error.setDescription("start time is before near time");
	            error.setParameterName("startTime");
	            errors.add(error);
	        }
    	} else {
    		if (event.getStartTime().getTime() < event.getIssuedTime().getTime()) {
	            ProgramValidationMessage error = new ProgramValidationMessage();
	            error.setDescription("start time is before issue time");
	            error.setParameterName("startTime");
	            errors.add(error);
    		} else if (event.getStartTime().getTime()< (event.getIssuedTime().getTime()+minIssueToStartM*DateUtil.MSEC_IN_MIN)) {
	            ProgramValidationMessage error = new ProgramValidationMessage();
	            error.setDescription("start time must be at least "+minIssueToStartM+ " minutes after issue time");
	            error.setParameterName("startTime");
	            warnings.add(error);
    		}
        }

        if (minStartCal.getTimeInMillis() > startCal.getTimeInMillis()
                || maxStartCal.getTimeInMillis() < startCal.getTimeInMillis()) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            StringBuilder sb = new StringBuilder("start time must be ");
            long dif = Math.abs(maxStartCal.getTimeInMillis() - minStartCal.getTimeInMillis()); // allows for some rounding
            if (dif < 60000) { // less than a minute, essentially the same
                sb.append(DateUtil.dateFormatHHmm().format(maxStartCal.getTime()));                
            } else {
                sb.append("between ").append(DateUtil.dateFormatHHmm().format(minStartCal.getTime()));
                sb.append(" and ").append(DateUtil.dateFormatHHmm().format(maxStartCal.getTime()));
            }
            error.setDescription(sb.toString());
            error.setParameterName("startTime");
            warnings.add(error);
        }

        //TODO:comment this block for future implementation of program season checking.
        /*Calendar cal = new GregorianCalendar();
        cal.setTime(event.getStartTime());
        int year = cal.get(Calendar.YEAR);
        if ((program.getStartDate() != null && event.getStartTime()
                .getTime() < program.getStartDate(year).getTime())
                || (program.getEndDate() != null && event.getStartTime()
                        .getTime() > program.getEndDate(year).getTime())) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("start time must be between "
                    + dateFormatMMdd.format(program.getStartDate(year)
                            .getTime())
                    + " and "
                    + dateFormatMMdd.format(program.getEndDate(year)
                            .getTime()));
            error.setParameterName("startTime");
            warnings.add(error);
        }*/

    }

    protected void validateEndTime() {
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
            StringBuilder sb = new StringBuilder("end time must be ");
            long dif = Math.abs(maxEndCal.getTimeInMillis() - minEndCal.getTimeInMillis()); // allows for some rounding
            if (dif < 60000) { // less than a minute, essentially the same
                sb.append(DateUtil.dateFormatHHmm().format(maxEndCal.getTime()));                
            } else {
                sb.append("between ").append(DateUtil.dateFormatHHmm().format(minEndCal.getTime()));
                sb.append(" and ").append(DateUtil.dateFormatHHmm().format(maxEndCal.getTime()));
            }
            error.setDescription(sb.toString());
            error.setParameterName("endTime");
            warnings.add(error);
        }

        //TODO:comment this block for future implementation of program season checking.
        /*Calendar cal = new GregorianCalendar();
        cal.setTime(event.getEndTime());
        int year = cal.get(Calendar.YEAR);
        if ((program.getStartDate() != null && event.getEndTime()
                .getTime() < program.getStartDate(year).getTime())
                || (program.getStartDate() != null && event.getEndTime()
                        .getTime() > program.getEndDate(year).getTime())) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("end time must be between "
                    + dateFormatMMdd.format(program.getStartDate(year)
                            .getTime())
                    + " and "
                    + dateFormatMMdd.format(program.getEndDate(year)
                            .getTime()));
            error.setParameterName("endTime");
            warnings.add(error);
        }*/
    }

    protected void validateDuration() {
        // confirm event duration is within range
        long durationMS = event.getEndTime().getTime()
                - event.getStartTime().getTime();
        if (durationMS < program.getMinDurationMS()
                || durationMS > program.getMaxDurationMS()) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            StringBuilder sb = new StringBuilder("event duration must be ");
            if (program.getMinDurationM() == program.getMaxDurationM()) {
                sb.append(program.getMaxDurationM());
            } else {
                sb.append("between ").append(program.getMinDurationM());
                sb.append(" and ");
                sb.append(program.getMaxDurationM());                
            }            
            sb.append(" minutes");
            error.setDescription(sb.toString());
            error.setParameterName("duration");
            warnings.add(error);
        }
    }

    /**
     * confirm the event confirms to the rules.
     * <p>
     * Call setProgram before calling this method
     * <p>
     * empty list == no errors.
     * 
     *
     * @param event
     *            The event to validate
     *
     * @throws ProgramValidationException
     *             Thrown if the event breaks a rule. Contains a list of
     *             violations wrapped in ProgramValidationMessages.
     */
    public void validateEvent(Event event)
            throws ProgramValidationException {
        this.event = event;
        this.utilityDREvent = utilityDREvent;
        errors = new ArrayList<ProgramValidationMessage>();
        warnings = new ArrayList<ProgramValidationMessage>();

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

        nowMS = new Date().getTime();
        issueCal = new GregorianCalendar();
        issueCal.setTime(event.getIssuedTime());
        maxIssueCal = new GregorianCalendar(issueCal.get(Calendar.YEAR),
                issueCal.get(Calendar.MONTH),
                issueCal.get(Calendar.DAY_OF_MONTH),
                program.getMaxIssueTimeH(), program.getMaxIssueTimeM(), 59);
        maxIssueCal.set(Calendar.MILLISECOND, 999);
        startCal = new GregorianCalendar();
        startCal.setTime(event.getStartTime());
        minStartCal = new GregorianCalendar(startCal.get(Calendar.YEAR),
                startCal.get(Calendar.MONTH),
                startCal.get(Calendar.DAY_OF_MONTH),
                program.getMinStartTimeH(), program.getMinStartTimeM(), 0);
        maxStartCal = new GregorianCalendar(startCal.get(Calendar.YEAR),
                startCal.get(Calendar.MONTH),
                startCal.get(Calendar.DAY_OF_MONTH),
                program.getMaxStartTimeH(), program.getMaxStartTimeM(), 59);
        maxStartCal.set(Calendar.MILLISECOND, 999);
        
        if(event.getEndTime()!=null){
	        endCal = new GregorianCalendar();
	        endCal.setTime(event.getEndTime());
	        minEndCal = new GregorianCalendar(endCal.get(Calendar.YEAR),
	                endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH),
	                program.getMinEndTimeH(), program.getMinEndTimeM(), 0);
	        maxEndCal = new GregorianCalendar(endCal.get(Calendar.YEAR),
	                endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH),
	                program.getMaxEndTimeH(), program.getMaxEndTimeM(), 59);
	        maxEndCal.set(Calendar.MILLISECOND, 999);
        }

        minIssueToStartM=program.getMinIssueToStartM();
        
        validateAgainstExistingEvents();

        validateIssueTime();

        validateStartTime();

        validateNearTime();

        validateEndTime();

        validateDuration();

        validateEmptyEvent();

        processErrors("event validation failed", "");
    }

    protected void processErrors(String description, String longDescription)
            throws ProgramValidationException {
        if (warnings.size() > 0) {
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(event.getProgramName());
            logEntry.setDescription(description);
            logEntry.setLongDescr(warnings.toString() + "\n" + event);
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            if (event == null || errors.size() > 0) {
                logEntry.setDescription("event validation warning");
                log.warn(logEntry);
            }
            event.setWarnings(warnings);
        }

        if (errors.size() > 0) {
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(event.getProgramName());
            logEntry.setDescription(description + " (operation aborted)");
            logEntry.setLongDescr(errors.toString() + "\n" + event + "\n"
                    + longDescription);
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            ProgramValidationException error = new ProgramValidationException();
            error.setErrors(errors);
            throw error;
        }
    }

    /**
     * confirm program signals are valid.
     * 
     * @param event
     *            event to validate against
     * @param signals
     *            The signals to validate
     * 
     * @throws ProgramValidationException
     *             DocMe!
     */
    public void validateProgramSignals(Event event,
            List<? extends Signal> signals) throws ProgramValidationException {
        this.event = event;
        errors = new ArrayList<ProgramValidationMessage>();
        warnings = new ArrayList<ProgramValidationMessage>();

        for (Signal outside : signals) {
            // check if this signal is supported by the program
            boolean found = false;
            for (ProgramSignal signal : program.getSignals()) {
                if (outside.getSignalDef().getSignalName()
                        .equals(signal.getSignalDef().getSignalName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                String message = "signal "
                        + outside.getSignalDef().getSignalName()
                        + " not in program " + event.getProgramName();
                ProgramValidationMessage error = new ProgramValidationMessage();
                error.setDescription(message);
                error.setParameterName("signals");
                errors.add(error);
                break;
            }

            // confirm no duplicates
            int count = 0;
            for (Signal inside : signals) {
                if (outside.getSignalDef().getSignalName()
                        .equals(inside.getSignalDef().getSignalName())) {
                    count++;
                }
            }
            if (count != 1) {
                String message = "signal "
                        + outside.getSignalDef().getSignalName()
                        + " exists more than once in list";
                ProgramValidationMessage error = new ProgramValidationMessage();
                error.setDescription(message);
                error.setParameterName("signals");
                errors.add(error);
                break;
            }

            // validate the entries themselves
            validateEventSignalEntries(event, outside.getSignalEntries());
        }

        processErrors("event rules validation failed", signals.toString());
    }

    /**
     * confirm the rules are legal
     * 
     * empty list == no errors.
     * 
     * @param eventTiming
     *            event to validate against
     * @param rules
     *            the rules
     * 
     * @throws ProgramValidationException
     *             DocMe!
     */
    public void validateEventRules(EventTiming eventTiming,
            List<EventParticipantRule> rules) throws ProgramValidationException {
        errors = new ArrayList<ProgramValidationMessage>();
        warnings = new ArrayList<ProgramValidationMessage>();

        if (rules == null) {
            return;
        }

        processErrors("event rules validation failed", "");
    }

    protected void validateSignalEntryTimes(SignalEntry signalEntry) {
        // check for times older than the last 60 seconds
        // and if it's dirty or not.
        final Date entryTime = signalEntry.getTime();

        final Date startTime = event.getStartTime();
        final Date endTime = event.getEndTime();
        final Date issueTime = event.getIssuedTime();

        if (entryTime.after(endTime)) {
            addError(errors, "entry signal beyond event duration",
                    signalEntry.toString());
        }
        if ("pending".equals(signalEntry.getParentSignal().getSignalDef()
                .getSignalName())) {
            if (entryTime.before(issueTime)) {
                // TODO: what should we do regarding pending signal?
                addError(errors, "entry signal beyond event duration",
                        signalEntry.toString());
            }
        } else {
            if (entryTime.before(startTime)) {
                addError(errors, "entry signal beyond event duration",
                        signalEntry.toString());
            }
        }
    }

    /**
     * confirm the signal entries generated are legal
     * 
     * empty list == no errors.
     * 
     * @param event
     *            event to validate against
     * @param signalEntries
     *            the signal entries
     * 
     * @throws ProgramValidationException
     *             DocMe!
     */
    protected void validateEventSignalEntries(Event event,
            Set<? extends SignalEntry> signalEntries)
            throws ProgramValidationException {
        if (signalEntries == null) {
            return;
        }

        List<ProgramValidationMessage> errors = new ArrayList<ProgramValidationMessage>();
        nowMS = System.currentTimeMillis();

        // make sure signals are supported by the program
        for (SignalEntry signalEntry : signalEntries) {
            for (ProgramSignal signal : program.getSignals()) {
                if (signalEntry.getParentSignal().getSignalDef()
                        .getSignalName()
                        .equals(signal.getSignalDef().getSignalName())) {
                    if (signalEntry.getParentSignal().getSignalDef()
                            .isLevelSignal()) {
                        if (!(signal.getSignalDef().isInputSignal())) {
                            addError(errors, "signal types don't match",
                                    signalEntry.toString());
                            break;
                        }
                        boolean found = false;
                        for (SignalLevelDef level : signal.getSignalDef()
                                .getSignalLevels()) {
                            if (level.equals(signalEntry.getLevelValue())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            addError(errors,
                                    "entry signal level not in program signal",
                                    signalEntry.toString());
                            break;
                        }
                    } else if (!signalEntry.getParentSignal().getSignalDef()
                            .isLevelSignal()) {
                        // TODO: fill in validation rules here.
                    } else {
                        addError(
                                errors,
                                "only level and number  signals supported right now",
                                signalEntry.toString());
                    }
                }
            }
            validateSignalEntryTimes(signalEntry);
        }
    }
    
    
    public void validateProgramBidConfig() throws ProgramValidationException{
        if(program!=null){
        	ProgramValidationException exception = null;
        	if(program.getClassName().equalsIgnoreCase("com.akuacom.pss2.program.dbp.DBPBidProgramEJB")){
        		BidConfig bc = program.getBidConfig();
        		if(bc==null){
        			String message ="DBPBidProgram must set bid config";
        			ProgramValidationMessage error = new ProgramValidationMessage();
        			 error.setDescription(message);
        			 errors.add(error);
        			 if (exception == null)
                         exception = new ProgramValidationException();
                     exception.setErrors(errors);
        		}
        	}
        	if (exception != null) {
                throw exception;
            }
        }
    }
    

    
    public void validateProgramParticipant(Participant participant, ProgramMatrixTrig pmt)
            throws ProgramValidationException {
        errors = new ArrayList<ProgramValidationMessage>();
        warnings = new ArrayList<ProgramValidationMessage>();
        final com.akuacom.pss2.program.ProgramManager programManager = EJBFactory
                .getBean(com.akuacom.pss2.program.ProgramManager.class);
        String thisProg = program.getProgramName();
        ProgramValidationException exception = null;
        
        validateProgramBidConfig();
        
        final com.akuacom.pss2.system.SystemManager systemManager = EJBFactory
                .getBean(com.akuacom.pss2.system.SystemManager.class);
        Boolean enableProgramMatrixCheck=systemManager.getPss2Features().isEnableProgramMatrixCheck();

        if (participant.getProgramParticipants() != null &&
        		enableProgramMatrixCheck!=null && enableProgramMatrixCheck.booleanValue()) {
            for (ProgramParticipant pp : participant.getProgramParticipants()) {
                if (!pmt.coexistByNames(thisProg, pp.getProgramName())) {
                    if (exception == null)
                        exception = new ProgramValidationException();
                    String message = "You can't add participant into both programs: "
                            + pp.getProgramName() + " and " + thisProg;
                    ProgramValidationMessage error = new ProgramValidationMessage();
                    error.setDescription(message);
                    error.setParameterName("SCR RTP Program error: ");
                    List<ProgramValidationMessage> errors = exception
                            .getErrors();
                    if (errors == null) {
                        errors = new ArrayList<ProgramValidationMessage>();
                    }
                    errors.add(error);
                    exception.setErrors(errors);
                }
            }
        }

        if (exception != null) {
            throw exception;
        }

        processErrors("program participant validation failed", "");
    }
    
    
    
    /**
     * Not implemented!
     * <p>
     * Confirm program signals are valid.
     * 
     * @param participant
     *            DocMe!
     * 
     * @throws ProgramValidationException
     *             DocMe!
     */
    public void validateProgramParticipant(Participant participant)
            throws ProgramValidationException {
        errors = new ArrayList<ProgramValidationMessage>();
        warnings = new ArrayList<ProgramValidationMessage>();
        // ParticipantManager participantManager =
        // EJBFactory.getBean(ParticipantManager.class);
        // List<String> progs =
        // participantManager.getProgramsForParticipant(participant.getParticipantName(),
        // participant.isClient());
        final com.akuacom.pss2.program.ProgramManager programManager = EJBFactory
                .getBean(com.akuacom.pss2.program.ProgramManager.class);
        ProgramMatrixTrig pmt = programManager.getProgramMatrixTrig();
        String thisProg = program.getProgramName();
        ProgramValidationException exception = null;
        
        //DRMS-3954 Special with DBPBidProgram which has not config bid 
        //May cause the create participant operation throw nullpoint error
        validateProgramBidConfig();
        
        final com.akuacom.pss2.system.SystemManager systemManager = EJBFactory
                .getBean(com.akuacom.pss2.system.SystemManager.class);
        Boolean enableProgramMatrixCheck=systemManager.getPss2Features().isEnableProgramMatrixCheck();
        // for(String name: progs)
        if (participant.getProgramParticipants() != null && enableProgramMatrixCheck!=null && enableProgramMatrixCheck.booleanValue()) {
            for (ProgramParticipant pp : participant.getProgramParticipants()) {
                if (!pmt.coexistByNames(thisProg, pp.getProgramName())) {
                    if (exception == null)
                        exception = new ProgramValidationException();
                    String message = "You can't add participant into both programs: "
                            + pp.getProgramName() + " and " + thisProg;
                    ProgramValidationMessage error = new ProgramValidationMessage();
                    error.setDescription(message);
                    error.setParameterName("SCR RTP Program error: ");
                    List<ProgramValidationMessage> errors = exception
                            .getErrors();
                    if (errors == null) {
                        errors = new ArrayList<ProgramValidationMessage>();
                    }
                    errors.add(error);
                    exception.setErrors(errors);
                }
            }
        }

        if (exception != null) {
            throw exception;
        }

        processErrors("program participant validation failed", "");
    }
    
    protected void validateEmptyEvent() {
    }

    /**
     * Adds the error.
     * 
     * @param errors
     *            the errors
     * @param message
     *            the message
     * @param parameterName
     *            the parameter name
     */
    protected void addError(List<ProgramValidationMessage> errors,
            String message, String parameterName) {
        ProgramValidationMessage error = new ProgramValidationMessage();
        error.setDescription(message);
        error.setParameterName(parameterName);
        errors.add(error);
    }

    /**
     * Sets the program. Should be called before the validate methods.
     * 
     * @param program
     *            the new program
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * @return the issueCal
     */
    protected GregorianCalendar getIssueCal() {
        return issueCal;
    }

    /**
     * @return the maxIssueCal
     */
    protected GregorianCalendar getMaxIssueCal() {
        return maxIssueCal;
    }

    /**
     * @return the startCal
     */
    protected GregorianCalendar getStartCal() {
        return startCal;
    }

    /**
     * @return the maxStartCal
     */
    protected GregorianCalendar getMaxStartCal() {
        return maxStartCal;
    }

    /**
     * @return the minStartCal
     */
    protected GregorianCalendar getMinStartCal() {
        return minStartCal;
    }

    /**
     * @return the maxEndCal
     */
    protected GregorianCalendar getMaxEndCal() {
        return maxEndCal;
    }

    /**
     * @return the minEndCal
     */
    protected GregorianCalendar getMinEndCal() {
        return minEndCal;
    }

    /**
     * @return the program
     */
    protected Program getProgram() {
        return program;
    }

    /**
     * @return the event
     */
    protected Event getEvent() {
        return event;
    }

    /**
     * @return the errors
     */
    protected List<ProgramValidationMessage> getErrors() {
        return errors;
    }

    /**
     * @return the warnings
     */
    protected List<ProgramValidationMessage> getWarnings() {
        return warnings;
    }

    /**
     * @return the nowMS
     */
    protected long getNowMS() {
        return nowMS;
    }

    /**
     * @return the utilityDREvent
     */
    protected UtilityDREvent getUtilityDREvent() {
        return utilityDREvent;
    }
}
