/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ValidationException.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.util.List;

import com.akuacom.common.exception.BaseRuntimeException;

/**
 * The general exception that is through by the facades at various times. It
 * returns a localized (internationalized) error message from a error message
 * property file (e.g. ErrorMessages_en_US.properties) based on the key.
 */
public class ValidationException extends BaseRuntimeException {

	private static final long serialVersionUID = 5167774523914101554L;

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param key
	 *            the key
	 */
	public ValidationException(String key) {
		super(key);
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param key
	 *            the key
	 */
	public ValidationException(String key, String... params) {
		super(key, (Object[])params);

	}

	

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param key
	 *            the key
	 * @param variables
	 *            the variables
	 */
	public ValidationException(String key, List variables) {
		super(key,variables.toArray());
	
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param key
	 *            the key
	 * @param cause
	 *            the cause
	 */
	public ValidationException(String key, Throwable cause) {
		super(key, (Exception) cause);
		
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param key
	 *            the key
	 * @param cause
	 *            the cause
	 * @param variables
	 *            the variables
	 */
	public ValidationException(String key, Throwable cause,
			List<String> variables) {
		super(key, cause,variables.toArray());

	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ValidationException(Throwable cause) {
		super(cause);
	}

}
