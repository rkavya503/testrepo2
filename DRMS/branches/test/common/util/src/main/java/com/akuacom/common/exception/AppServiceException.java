/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.common.exception.AppServiceException.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.common.exception;

/**
 * The Class AppServiceException.
 */
public class AppServiceException extends BaseException {

	private static final long serialVersionUID = -5652593793975686482L;

	/**
	 * Instantiates a new app service exception.
	 * 
	 * @param key
	 *            the key
	 */
	public AppServiceException(String key) {
		super(key);
	}

	/**
	 * Instantiates a new app service exception.
	 * 
	 * @param key
	 *            the key
	 * @param cause
	 *            the cause
	 */
	public AppServiceException(String key, Throwable cause) {
		super(key, cause);
	}

	/**
	 * Instantiates a new app service exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public AppServiceException(Throwable cause) {
		super(cause);
	}

}
