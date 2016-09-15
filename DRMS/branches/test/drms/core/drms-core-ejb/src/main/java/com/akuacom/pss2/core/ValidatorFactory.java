/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ValidatorFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import com.akuacom.pss2.program.Program;

/**
 * A factory for creating Validator objects.
 * 
 * @see ProgramValidator ProgramValidator
 */
public class ValidatorFactory
{
	// program name / program validator pairs.
	/** The map. */

	/**
	 * Gets the ProgramValidator for a program.
	 * 
	 * @param program
	 *            not null
	 * 
	 * @return ProgramValidator class that is initialized.
	 * 
	 * @throws ProgramValidationException
	 *             if init failed
	 */
	public static ProgramValidator getProgramValidator(Program program)
		throws ProgramValidationException
	{
		try
		{
			final String className = program.getValidatorClass();
			final Class<?> clazz = Class.forName(className);
			ProgramValidator validator = (ProgramValidator) clazz.newInstance();
			validator.setProgram(program);
			return validator;
		}
		catch (InstantiationException e)
		{
			throw new ProgramValidationException(e.getMessage(), e);
		}
		catch (IllegalAccessException e)
		{
			throw new ProgramValidationException(e.getMessage(), e);
		}
		catch (ClassNotFoundException e)
		{
			throw new ProgramValidationException(e.getMessage(), e);
		}
	}
}
