/**
 * 
 */
package com.akuacom.common.resource;

import com.akuacom.common.text.CustomFormat;
import com.akuacom.common.text.PercentFormat;


/**
 * Strictly used for testing {@link ManagedResourceBundle}.
 * 
 * The top level enum is the message that can be present only as a enum or
 * declared in a properites file.
 * 
 * The nested Enum is the list of parameters that are related to the Message and
 * passed to the message during construction.
 * 
 * @see ManagedResourceBundleTest
 * 
 * @author roller
 * 
 */
public enum ExampleMessage implements ResourceBundleMessage {
	/** The message is in the file with no parameters. */
	MESSAGE_IN_FILE,
	/** The message is in the file, but specific params are provided */
	MESSAGE_IN_FILE_WITH_PARAMS(Param.FIRST_NAME, Param.USERNAME),
	/**
	 * The message is in the file, but only indexed parameters are declared
	 * (legacy support).
	 */
	MESSAGE_IN_FILE_WITH_INDEXED_PARAMS,
	/** The message is not in the file so this only this message will be used. */
	MESSAGE_PROVIDED_NOT_IN_FILE("Message not in file"),
	/**
	 * The message in java time is different than in the properties
	 * file...warning is provided.
	 */
	MESSAGE_PROVIDED_DIFFERENT_THAN_IN_FILE("Message different in file"),
	/** Message is in java time with parameters provided */
	MESSAGE_PROVIDED_WITH_PARAMS("{FIRST_NAME} created new user {USERNAME}.",
			Param.FIRST_NAME, Param.USERNAME),
	/**
	 * Ensures the proper formatting of each of the types per the parameter
	 * given.
	 */
	MESSAGE_TESTING_FORMATS(
			"{INTEGER} is an int, {LONG} is a long, {DOUBLE} is a double, {FLOAT} is a float, {PERCENT} is a pecent.",
			Param.INTEGER, Param.LONG, Param.DOUBLE, Param.FLOAT, Param.PERCENT);

	private ResourceBundleMessageParameterKey[] parameterKeys;
	private String message;

	private ExampleMessage(String message,
			ResourceBundleMessageParameterKey... parameters) {
		this.message = message;
		this.parameterKeys = parameters;
	}

	private ExampleMessage(ResourceBundleMessageParameterKey... parameterKeys) {
		this(null, parameterKeys);
	}

	@Override
	public String getValue() {
		return this.message;
	}

	@Override
	public ResourceBundleMessageParameterKey[] getParameterKeys() {
		return this.parameterKeys;
	}

	public enum Param implements ResourceBundleMessageParameterKey {
		/** Simplest example of a parameter that provides a readable key */
		FIRST_NAME,
		/** Simple username with a custom format provided */
		USERNAME(new ExampleUsernameCustomFormat()),
		/** demonstrates how to pass a percent as a double */
		PERCENT(new PercentFormat()),
		/** demonstrates how to pass a float */
		FLOAT,
		/** demonstrates how to pass an Integer */
		INTEGER,
		/** demonstrates how to pass a double */
		DOUBLE,
		/** demonstrates how to pass a long */
		LONG;
		CustomFormat format;

		private Param() {
		}

		private Param(CustomFormat format) {
			this.format = format;
		}

		@Override
		public CustomFormat getFormat() {
			return this.format;
		}
	}
}
