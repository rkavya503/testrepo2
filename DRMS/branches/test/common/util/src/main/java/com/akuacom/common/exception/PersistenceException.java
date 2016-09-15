/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.common.exception.PersistenceException.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.common.exception;

/**
 * The Class PersistenceException.
 */
public class PersistenceException extends BaseException
{
    
	private static final long serialVersionUID = -2341576184091451655L;

	/**
     * Instantiates a new persistence exception.
     * 
     * @param message the message
     */
    public PersistenceException(String message)
    {
        super(message);
    }

    /**
     * Instantiates a new persistence exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public PersistenceException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Instantiates a new persistence exception.
     * 
     * @param cause the cause
     */
    public PersistenceException(Throwable cause)
    {
        super(cause);
    }
}
