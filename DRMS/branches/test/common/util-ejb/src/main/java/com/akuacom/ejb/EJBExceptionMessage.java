/**
 * 
 */
package com.akuacom.ejb;

import com.akuacom.common.resource.KeyedMessageFormat;
import com.akuacom.common.resource.ResourceBundleMessage;
import com.akuacom.common.resource.ResourceBundleMessageParameterKey;
import com.akuacom.common.text.CustomFormat;

/**
 * The main messages displayed for the PSS2 application related specifically to
 * the Exceptions thrown.
 * 
 * Notice there are enumerations that should each have Javadoc. Each Enumeration
 * may accept the message which will be used instead of a message provided in a
 * resource bundle properties file.
 * 
 * Each enum also accepts one or more parameters that will must match the keyed
 * parameters in the message indicated by curly braces {MY_KEY} (see
 * {@link KeyedMessageFormat}).
 * 
 * @author roller
 * 
 */
public enum EJBExceptionMessage implements ResourceBundleMessage {
	/**
	 * @see DuplicateKeyException
	 */
	DUPLICATE_KEY("A duplicate id of {IDENTIFIER} was found.",Param.IDENTIFIER),
	/**
	 * @see EntityNotFoundException
	 */
	ENTITY_NOT_FOUND("Unable to find {" + Param.IDENTIFIER_KEY + "} {" + Param.IDENTIFIER
			+ "}.", Param.IDENTIFIER,Param.IDENTIFIER_KEY);

	private ResourceBundleMessageParameterKey[] parameterKeys;
	private String message;

	private EJBExceptionMessage(String message,
			ResourceBundleMessageParameterKey... parameters) {
		this.message = message;
		this.parameterKeys = parameters;
	}

	private EJBExceptionMessage(
			ResourceBundleMessageParameterKey... parameterKeys) {
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
		/**
		 * Used to provide the identifier that may be missing (user Id,
		 * username, etc).
		 */
		IDENTIFIER, 
		/**The key desribing what the identifier is that wasn't found (User ID, username, primary key, etc).*/
		IDENTIFIER_KEY;

		private CustomFormat format;
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
