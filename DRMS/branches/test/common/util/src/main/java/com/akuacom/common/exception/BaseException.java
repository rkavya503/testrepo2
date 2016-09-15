/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.common.exception.BaseException.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.common.exception;

import com.akuacom.common.resource.DynamicResourceBundleMessage;
import com.akuacom.common.resource.ResourceBundleMessage;


/**
 * The Base Exception to be used for all Exceptions in the system.
 * @see BaseRuntimeException for the runtime version of this.
 */
abstract public class BaseException extends Exception {

	private static final long serialVersionUID = -7628129287188965619L;
	private ExceptionLocalizer exceptionLocalizer;

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param key
	 *            the key
	 */
	public BaseException(String key) {

		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(key));
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param key
	 *            the key
	 */
	public BaseException(String key, String... params) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(key,
				params));
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param key
	 *            the key
	 * @param cause
	 *            the cause
	 */
	public BaseException(String key, Throwable cause) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(key,
				cause));
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public BaseException(Throwable cause) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(
				cause));
	}

	public BaseException(DynamicResourceBundleMessage message){
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(message));
	}
	
	public BaseException(DynamicResourceBundleMessage message,Exception cause){
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(message,cause));
	}
	
	public BaseException(ResourceBundleMessage message) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(
				message));
	}

	public BaseException(ResourceBundleMessage message, Exception e) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(
				message, e));
	}
	private BaseException(ExceptionLocalizer exceptionLocalizer) {
		// message is not ready yet since locale is not known
		super(exceptionLocalizer.getCause());
		this.exceptionLocalizer = exceptionLocalizer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		return this.exceptionLocalizer.getMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {

		return this.exceptionLocalizer.getMessage();

	}
}
