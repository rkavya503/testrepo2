/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.cpp.CPPValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.demo;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;
import com.akuacom.utils.lang.DateUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The Class CPPValidator.
 */
public class DemoValidator extends ProgramValidator
{

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.core.ProgramValidator#validateEvent(com.akuacom.pss2.core.model.Event, org.openadr.dras.utilitydrevent.UtilityDREvent)
	 */
	public void validateEvent(Event event)
		throws ProgramValidationException
	{
		super.validateEvent(event);
	}


    @Override
    protected void validateIssueTime()
    {
        // TODO this is the temperary fix for not being able to issue demo event for next day. We will later
        // replace this fix by adding another DA flag once the spec is clear on that.
            // make sure issue time is within the last 60 seconds
            if(this.getEvent().getIssuedTime().getTime() < (this.getNowMS() - TIME_BUFFER_MS))
            {
                ProgramValidationMessage error = new ProgramValidationMessage();
                error.setDescription("issue time is in the past");
                error.setParameterName("issuedTime");
                this.getErrors().add(error);
            }

            // confirm issue day is in range
                if (program.isMustIssueBDBE())
                {
                    // TODO: for now, since we don't have a holiday interface, we'll
                    // just make sure it is at least the day before. we really
                    // should
                    // check for weekends and holidays
                    boolean issueError = false;
                    if (this.getStartCal().get(Calendar.DAY_OF_YEAR) == 1)
                    {
                        // if the start is on jan 1st, then the issue year must be
                        // less
                        // than the start year
                        if (this.getStartCal().get(Calendar.YEAR) <=
                            this.getIssueCal().get(Calendar.YEAR))
                        {
                            issueError = true;
                        }
                    }
                    else
                    {
                        if (this.getStartCal().get(Calendar.YEAR) < this.getIssueCal().get(Calendar.YEAR))
                        {
                            issueError = true;
                        }
                        else if (this.getStartCal().get(Calendar.YEAR) ==
                            this.getIssueCal().get(Calendar.YEAR) &&
                            this.getStartCal().get(Calendar.DAY_OF_YEAR) <=
                            this.getIssueCal().get(Calendar.DAY_OF_YEAR))
                        {
                            issueError = true;
                        }
                    }
                    if (issueError)
                    {
                        ProgramValidationMessage error =
                            new ProgramValidationMessage();
                        error.setDescription(
                            "issue day must be the business day before the start day");
                        error.setParameterName("issuedTime");
                        this.getErrors().add(error);
                    }
                }


            // confirm the issue time is in range
            boolean issueTimeError = false;
            if(this.getIssueCal().get(Calendar.HOUR_OF_DAY) >
                this.getMaxIssueCal().get(Calendar.HOUR_OF_DAY))
            {
                issueTimeError = true;
            }
            else if (this.getIssueCal().get(Calendar.HOUR_OF_DAY) ==
                this.getMaxIssueCal().get(Calendar.HOUR_OF_DAY) &&
                this.getIssueCal().get(Calendar.MINUTE) > this.getMaxIssueCal().get(Calendar.MINUTE))
            {
                issueTimeError = true;
            }
            if(issueTimeError)
            {
                ProgramValidationMessage error = new ProgramValidationMessage();
                error.setDescription("issue time must be before " +
                    DateUtil.dateFormatHHmm().format(getMaxIssueCal().getTime()));
                error.setParameterName("issuedTime");
                this.getWarnings().add(error);
            }
            // DA: throw exception if issue time > pending on signal time
            if(program.isMustIssueBDBE())
            {
                issueTimeError = false;
                if(this.getIssueCal().get(Calendar.HOUR_OF_DAY) >
                    program.getPendingTimeDBEH())
                {
                    issueTimeError = true;
                }
                else if (this.getIssueCal().get(Calendar.HOUR_OF_DAY) ==
                    program.getPendingTimeDBEH() &&
                    this.getIssueCal().get(Calendar.MINUTE) > program.getPendingTimeDBEM())
                {
                    issueTimeError = true;
                }
                if(issueTimeError)
                {
                    Calendar pendingTimeCal = new GregorianCalendar();
                    pendingTimeCal.set(Calendar.HOUR_OF_DAY, program.getPendingTimeDBEH());
                    pendingTimeCal.set(Calendar.MINUTE, program.getPendingTimeDBEM());
                    ProgramValidationMessage error = new ProgramValidationMessage();
                    error.setDescription("issue time must be before pending time on (" +
                        DateUtil.dateFormatHHmm().format(pendingTimeCal.getTime()) + ")");
                    error.setParameterName("issuedTime");
                    this.getErrors().add(error);
                }
            }
        }


}