/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.common.exception.BaseRuntimeException.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.common.exception;

import com.akuacom.common.resource.DynamicResourceBundleMessage;
import com.akuacom.common.resource.ResourceBundleMessage;

/**
 * All RuntimeExceptions should extend this exception to provide valuable information.
 */
abstract public class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 2176551578167720L;
	private ExceptionLocalizer exceptionLocalizer;

	/**
	 * Instantiates a new base runtime exception.
	 * 
	 * @param key
	 *            the key
	 */
	public BaseRuntimeException(String keyOrMessage) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(keyOrMessage));
	}

	/**
	 * Instantiates a new base runtime exception.
	 * 
	 * @param key
	 *            the key
	 * @param cause
	 *            the cause
	 */
	public BaseRuntimeException(String key, Throwable cause) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(key,
				cause));
	}

	/**
	 * Instantiates a new base runtime exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public BaseRuntimeException(Throwable cause) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(
				cause));
	}

	public BaseRuntimeException(String key, Object... params) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(key,
				params));
	}

	public BaseRuntimeException(ExceptionLocalizer exceptionLocalizer) {
		super(exceptionLocalizer.getCause());
		this.exceptionLocalizer = exceptionLocalizer;
	}

	public BaseRuntimeException(
			DynamicResourceBundleMessage dynamicResourceBundleMessage) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(
				dynamicResourceBundleMessage));
	}
	
	public BaseRuntimeException(
			DynamicResourceBundleMessage dynamicResourceBundleMessage, Exception e) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(
				dynamicResourceBundleMessage,e));
	}

	public BaseRuntimeException(ResourceBundleMessage message) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(
				message));
	}

	public BaseRuntimeException(ResourceBundleMessage message, Exception e) {
		this(ExceptionLocalizerFactory.getInstance().getExceptionLocalizer(
				message, e));
	}

	@Override
	public String getLocalizedMessage() {
		return this.exceptionLocalizer.getMessage();
	}

	@Override
	public String getMessage() {

		return this.exceptionLocalizer.getMessage();
	}

}
