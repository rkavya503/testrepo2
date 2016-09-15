/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ProgramValidatationException.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.util.List;

/**
 * Exception used to signal that an event, signal, etc. has failed rule
 * validation.  Contains a list of all ProgramValidationMessages for all failed rules.
 */
public class ProgramValidationException extends Exception
{
    private static final long serialVersionUID = -8469828622301997836L;
    
    /** The errors. */
    private List<ProgramValidationMessage> errors;

    
    /**
     * Instantiates a new program validatation exception.
     */
    public ProgramValidationException() {
        super();
    }

    /**
     * Instantiates a new program validatation exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public ProgramValidationException(String message, Throwable cause) {
        super(message, cause);
    }

	/**
	 * Gets the errors.
	 * 
	 * @return the errors
	 */
	public List<ProgramValidationMessage> getErrors()
	{
		return errors;
	}

	/**
	 * Sets the errors list.
	 * 
	 * @param errors the new errors
	 */
	public void setErrors(List<ProgramValidationMessage> errors)
	{
		this.errors = errors;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
        if (errors != null) {
            for(ProgramValidationMessage error: errors)
            {
                sb.append(error.toString());
            }
        }
		return sb.toString();
	}
}
