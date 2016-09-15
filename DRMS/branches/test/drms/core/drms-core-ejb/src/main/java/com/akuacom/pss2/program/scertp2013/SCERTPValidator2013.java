/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.scertp.SCERTPValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp2013;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.scertp.SCERTPProgramManager;


/**
 * The Class SCERTPValidator.
 */
public class SCERTPValidator2013 extends ProgramValidator
{	
	protected void validateAgainstExistingEvents()
	{
		com.akuacom.pss2.program.ProgramManager programManager = 
			EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);
		EventManager pmb =
			EJBFactory.getBean(EventManager.class);
		for(EventInfo eventInfo: 
			programManager.getEventsForProgram(program.getProgramName()))
		{
			Event existingEvent = pmb.getEvent(program.getProgramName(), 
				eventInfo.getEventName());
       // DRMS-2633.  This is probably a hack, and not the real cure
       // For some reason, we've gone from not finding the event we're making to
       // FINDING the event we're making, and having a fit because of it.
   /*foo*/ if (!event.getEventName().equals(existingEvent.getEventName())) { /* DRMS-2633 temporary fix(?)*/
                // if there is overlap
                if((event.getStartTime().getTime() >= existingEvent.getStartTime().getTime() &&
                    event.getStartTime().getTime() < existingEvent.getEndTime().getTime()) ||
                    (event.getEndTime().getTime() > existingEvent.getStartTime().getTime() &&
                    event.getEndTime().getTime() <= existingEvent.getEndTime().getTime()) ||
                    (event.getStartTime().getTime() < existingEvent.getStartTime().getTime() &&
                    event.getEndTime().getTime() >= existingEvent.getEndTime().getTime()))
                {
                    ProgramValidationMessage error = new ProgramValidationMessage();
                    error.setDescription("event overlaps with existing event: " +
                        existingEvent.getEventName());
                    error.setParameterName("startTime");
                    errors.add(error);
                }
            }
		}		
	}

	
	
	
    protected void validateIssueTime() {
    	// no need to validate issue time for RTP - for now
    }

	
	
	
	
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.core.ProgramValidator#validateEvent(com.akuacom.pss2.core.model.Event, org.openadr.dras.utilitydrevent.UtilityDREvent)
	 */
	public void validateEvent(Event event)
		throws ProgramValidationException
	{
		SCERTPProgramManager2013 programManager = EJBFactory.getBean(SCERTPProgramManager2013.class);
        ProgramValidationException exception = null;
        try
        {
            super.validateEvent(event);
            
        }
        catch(ProgramValidationException e)
        {
            exception = e;
        }
        catch(Exception e) // this should not happen based on the parent implementation, save it here if parent changes implementation in the future.
        {           
            exception = new ProgramValidationException();
            String message = e.getMessage();
			ProgramValidationMessage error = new ProgramValidationMessage();
			error.setDescription(message);
			error.setParameterName("SCE RTP Program error: ");
            List<ProgramValidationMessage> errors = exception.getErrors();
            if(errors == null)
            {
                errors = new ArrayList<ProgramValidationMessage>();
            }
            errors.add(error);
            exception.setErrors(errors);
			
        }

        try
        {
        	programManager.getRate(this.program.getProgramName(), event.getStartTime());
        }
        catch(Exception e)
        {
            // will need to append errors to the ones from parent check, if any.
            if(exception == null) exception = new ProgramValidationException();
            String message = e.getMessage();
			ProgramValidationMessage error = new ProgramValidationMessage();
			error.setDescription(message);
			error.setParameterName("SCE RTP Config");
            List<ProgramValidationMessage> errors = exception.getErrors();
            if(errors == null)
            {
                errors = new ArrayList<ProgramValidationMessage>();
            }
            errors.add(error);
            exception.setErrors(errors);

        }

        if(exception != null)
        {
            throw exception;
        }
    }

    /**
     * confirm program signals are valid.
     * 
     * @param participant DocMe!
     * 
     * @throws ProgramValidationException DocMe!
     */
	public void validateProgramParticipant(Participant participant)
		throws ProgramValidationException
	{
        SCERTPProgramManager2013 programManager = EJBFactory.getBean(SCERTPProgramManager2013.class);
        ProgramValidationException exception = null;
        try
        {
            super.validateProgramParticipant(participant);
            
        }
        catch(ProgramValidationException e)
        {
            exception = e;
        }
        catch(Exception e) 
        {
        	// this should not happen based on the parent implementation, 
        	// save it here if parent changes implementation in the future.
            exception = new ProgramValidationException();
            String message = e.getMessage();
			ProgramValidationMessage error = new ProgramValidationMessage();
			error.setDescription(message);
			error.setParameterName("SCE RTP Program error: ");
            List<ProgramValidationMessage> errors = exception.getErrors();
            if(errors == null)
            {
                errors = new ArrayList<ProgramValidationMessage>();
            }
            errors.add(error);
            exception.setErrors(errors);

        }

        try
        {
        	// only used to validate season and strategy category configuration
        	programManager.getRate(this.program.getProgramName(), new Date());
        }
        catch(Exception e) // this should not happen based on the parent implementation
        {
            // will need to append errors to the ones from parent check, if any.
            if(exception == null) exception = new ProgramValidationException();
            String message = e.getMessage();
			ProgramValidationMessage error = new ProgramValidationMessage();
			error.setDescription(message);
			error.setParameterName("SCE RTP Config");
            List<ProgramValidationMessage> errors = exception.getErrors();
            if(errors == null)
            {
                errors = new ArrayList<ProgramValidationMessage>();
            }
            errors.add(error);
            exception.setErrors(errors);

        }

        if(exception != null)
        {
            throw exception;
        }
    }
}