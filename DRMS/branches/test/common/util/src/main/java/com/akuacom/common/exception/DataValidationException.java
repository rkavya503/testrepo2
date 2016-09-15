/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.common.exception.DataValidationException.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.common.exception;



/**
 * The Class DataValidationException.
 */
public class DataValidationException extends AppServiceException
{
	private static final long serialVersionUID = -4774495061256323153L;

	/**
     * Instantiates a new data validation exception.
     * 
     * @param key the key
     */
    public DataValidationException(String key)
    {
        super(key);
    }

    /**
     * Instantiates a new data validation exception.
     * 
     * @param key the key
     * @param cause the cause
     */
    public DataValidationException(String key, Throwable cause)
    {
        super(key, cause);
    }

    /**
     * Instantiates a new data validation exception.
     * 
     * @param cause the cause
     */
    public DataValidationException(Throwable cause)
    {
        super(cause);
    }

    /* (non-Javadoc)
     * @see com.akuacom.common.exception.AppServiceException#getLocalizedMessage()
     */
    public String getLocalizedMessage()
    {
        return super.getLocalizedMessage();
    }
}
