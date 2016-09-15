/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.irr.IRRValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.irr;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.event.Event;

/**
 * The Class IRRValidator.
 */
public class IRRValidator extends ProgramValidator
{

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.core.ProgramValidator#validateEvent(com.akuacom.pss2.core.model.Event, org.openadr.dras.utilitydrevent.UtilityDREvent)
	 */
    @Override
	public void validateEvent(Event event)
		throws ProgramValidationException
	{
		super.validateEvent(event);
	}
	
}
