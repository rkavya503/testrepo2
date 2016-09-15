/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.cpp.CPPValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.fastdr;

import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.event.participant.EventParticipant;

import java.util.Calendar;
import java.util.Set;

/**
 * The Class FastDRValidator.
 */
public class FastDRValidator extends ProgramValidator {

    @Override
    protected void validateEmptyEvent() {
        Set<EventParticipant> set = event.getEventParticipants();
        if (set == null || set.size() == 0) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("you must pick at least one participant");
            error.setParameterName("participants");
            errors.add(error);
        }
    }

    @Override
    protected void validateEndTime() {
        super.validateEndTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getEndTime());
        int i = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(event.getStartTime());
        if (cal.get(Calendar.DAY_OF_YEAR) != i) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("event can't cross day boundary");
            error.setParameterName("endTime");
            errors.add(error);
        }
    }

    @Override
    protected void validateDuration() {
        super.validateDuration();
        // if duration is violated, move the message from warning to error.
        int size = warnings.size();
        if (size > 0) {
            ProgramValidationMessage message = warnings.get(size - 1);
            if ("duration".equals(message.getParameterName())) {
                warnings.remove(message);
                errors.add(message);
            }
        }
    }
}
