/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.cpp.CPPValidator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rtp;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.event.Event;

/**
 * The Class CPPValidator.
 */
public class RTPValidator extends ProgramValidator
{

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.core.ProgramValidator#validateEvent(com.akuacom.pss2.core.model.Event, org.openadr.dras.utilitydrevent.UtilityDREvent)
	 */
	public void validateEvent(Event event)
		throws ProgramValidationException
	{
		super.validateEvent(event);
	}

}
