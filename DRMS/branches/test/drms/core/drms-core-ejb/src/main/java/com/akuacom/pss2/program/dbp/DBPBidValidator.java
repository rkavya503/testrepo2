/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.dbp.DBPValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.kanaeki.firelog.util.FireLogEntry;
import org.apache.log4j.Logger;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.bidding.BidConfig;
import com.akuacom.pss2.util.LogUtils;

/**
 * The Class DBPValidator.
 */
public class DBPBidValidator extends DBPNoBidValidator {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(DBPBidValidator.class);

    /** The Constant ONE_HOUR_MS. */
    private static final long ONE_HOUR_MS = 60 * 60 * 1000;

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
    	List<ProgramValidationMessage> parsewarning=event.getWarnings();
    	
        super.validateEvent(event);

        DBPEvent dpbEvent = (DBPEvent) event;
        final BidConfig config = program.getBidConfig();

        List<ProgramValidationMessage> errors = new ArrayList<ProgramValidationMessage>();
        List<ProgramValidationMessage> warnings = new ArrayList<ProgramValidationMessage>();

        GregorianCalendar respondByTimeCal = new GregorianCalendar();
        respondByTimeCal.setTime(dpbEvent.getRespondBy());
        GregorianCalendar checkRespondByTimeCal = new GregorianCalendar(
                respondByTimeCal.get(Calendar.YEAR),
                respondByTimeCal.get(Calendar.MONTH),
                respondByTimeCal.get(Calendar.DAY_OF_MONTH),
                config.getRespondByTimeH(), config.getRespondByTimeM(), 0);

        // DA: warn if respond by time is not the set time
        if (program.isMustIssueBDBE()) {
            if (checkRespondByTimeCal.getTimeInMillis() != respondByTimeCal
                    .getTimeInMillis()) {
                ProgramValidationMessage error = new ProgramValidationMessage();
                error.setDescription("respond by time must be "
                        + config.getRespondByTimeH() + ":"
                        + config.getRespondByTimeM());
                error.setParameterName("respondByTime");
                warnings.add(error);
            }
        } else {
            // DO: error if respond by time is not at least 60 minutes past
            // issue time
            if (respondByTimeCal.getTimeInMillis() < (event.getIssuedTime()
                    .getTime() + ONE_HOUR_MS)) {
                ProgramValidationMessage error = new ProgramValidationMessage();
                error.setDescription("respond by time must be at least 60 minutes past issue time");
                error.setParameterName("respondByTime");
                errors.add(error);
            }
        }

        // error if respond by time is after start time
        if (respondByTimeCal.getTimeInMillis() > event.getStartTime().getTime()) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("respond by time must be before start time ");
            error.setParameterName("respondByTime");
            errors.add(error);
        }

        if (warnings.size() > 0) {
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(event.getProgramName());
            logEntry.setDescription("event validation failed");
            logEntry.setLongDescr(warnings.toString() + "\n" + event);
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            if (this.event != null) {
                log.warn(logEntry);
            }

        }

        if (errors.size() > 0) {
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(event.getProgramName());
            logEntry.setDescription("event validation failed (creation aborted)");
            logEntry.setLongDescr(errors.toString() + "\n" + event);
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            if (this.event != null) {
                log.error(logEntry);
            }
            ProgramValidationException error = new ProgramValidationException();
            error.setErrors(errors);
            throw error;
        }
        
        if (parsewarning!=null && parsewarning.size()>0) {
        	event.getWarnings().addAll(parsewarning);
        }
    }
}
