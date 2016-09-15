/**
 * 
 */
package com.akuacom.common.resource;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

import com.akuacom.common.text.CustomFormat;
import com.akuacom.common.text.CustomFormatFactory;

/**
 * A simple class that encourages good use of resource bundles and their
 * associated enumerations.
 * 
 * 
 * Basically, if there is gonna be any reference to a resource bundle key, it
 * must be done via an enumeration that extends {@link ResourceBundleMessage}
 * key.
 * 
 * This class will take the associated ResourceBundleKey enum class and look for
 * an associated properties file. If no properties file exists, then it will
 * just use the message provided by {@link ResourceBundleMessage#getValue()}
 * (which is easier to maintain by coders).
 * 
 * Upon start-up, this ManagedResourceBundle will {@link #validate()} the
 * provided {@link ResourceBundleMessage} to make sure all parameters match the
 * messages provided.
 * 
 * @see PSS2CoreExceptionMessage to view an example of how the messages should
 *      be handled.
 * 
 * 
 * @author roller
 * 
 */
public class ManagedResourceBundle implements Serializable {

	private static final long serialVersionUID = 7820798223036414397L;
	private PropertyResourceBundle resourceBundle;
	private Class<? extends Enum<? extends ResourceBundleMessage>> keyClass;
	private String bundleBaseName;

	/**
	 * Given the Enumeration Class representing the keys in the bundle this will
	 * look for the resource bundle matches exactly the fully qualified name of
	 * the Enumeration provied.
	 * 
	 * @param keyClass
	 *            the enumeration matching the keys in the properties file.
	 */
	public ManagedResourceBundle(
			Class<? extends Enum<? extends ResourceBundleMessage>> keyClass) {
		this(keyClass, keyClass.getName());

	}

	/**
	 * provides an alternate bundle name to be used if it doesn't match teh
	 * standard pattern as described in {@link #ManagedResourceBundle(Class)}.
	 * 
	 * @param keyClass
	 * @param baseName
	 */
	public ManagedResourceBundle(
			Class<? extends Enum<? extends ResourceBundleMessage>> keyClass,
			String baseName) {
		super();
		this.keyClass = keyClass;
		this.bundleBaseName = baseName;
		try {
			ResourceBundle rb = ResourceBundle.getBundle(baseName);
			if (rb instanceof PropertyResourceBundle) {
				this.resourceBundle = (PropertyResourceBundle) rb;
			} else {
				throw new RuntimeException(
						"unable to find resource bundle with name of "
								+ baseName);
			}
		} catch (MissingResourceException e) {
			Logger
					.getLogger(ManagedResourceBundle.class)
					.warn(
							"No properties file matching "
									+ baseName
									+ ".  No localization will be supported, but messages will work per the rules of "
									+ ResourceBundleMessage.class
											.getSimpleName());
		}
		validate();
	}

	/**
	 * Answers the simple question if a key is in the resource bundle indicating
	 * this is a qualified supplier.
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		// check to see if it is in the properties file or constants
		return this.resourceBundle != null
				&& this.resourceBundle.containsKey(key)
				|| containsResourceBundleMessage(key);
	}

	/**
	 * Just uses toString from the enumeration to search for the key.
	 * 
	 * @see #containsKey(String)
	 * 
	 * @param message
	 * @return
	 */
	public boolean containsKey(ResourceBundleMessage message) {
		return containsKey(message.toString());
	}

	public boolean containsResourceBundleMessage(String key) {
		try {
			return getResouceBundleMessage(key) != null;

		} catch (MissingResourceException e) {
			return false;
		}
	}

	public String getString(ResourceBundleMessage message,

	Locale locale) {
		String rawMessage = message.getValue();

		// use properties file if message not provided
		if (rawMessage == null) {
			if (this.resourceBundle == null) {
				throw new MissingResourceException(
						message.toString()
								+ " is enumerated, but provides no value and there is no matching properties file for "
								+ this.bundleBaseName, message.getClass()
								.getName(), message.toString());
			}
			PropertyResourceBundle preferredBundle;
			// only lookup non-default if locale is different than default
			if (this.resourceBundle.getLocale().equals(locale)) {
				preferredBundle = this.resourceBundle;
			} else {
				preferredBundle = (PropertyResourceBundle) ResourceBundle
						.getBundle(bundleBaseName);
			}
			// throws MissingResource if still not found
			rawMessage = preferredBundle.getString(message.toString());
		}

		return rawMessage;
	}

	/**
	 * Formats a message with the default locale.
	 * 
	 * @see #formatMessage(DynamicResourceBundleMessage, Locale)
	 * @param message
	 * @return
	 */
	public String formatMessage(DynamicResourceBundleMessage message) {
		return this.formatMessage(message, Locale.getDefault());

	}

	/**
	 * Given the dynamic message containing the defained message with dynamic
	 * parameters this will format the message for the locale provided replacing
	 * all targets in the message value with the parameter values.
	 * 
	 * @see KeyedMessageFormat
	 * 
	 * @param dynamicMessage
	 *            the message enumarations and the dynamic parameters
	 * @param locale
	 *            the desired language
	 * @return the completed message with parameters replaced
	 * @throws IllegalArgumentException
	 *             when parameters do not match the message value.
	 * 
	 */
	public String formatMessage(DynamicResourceBundleMessage dynamicMessage,
			Locale locale) {
		return formatMessage(dynamicMessage, locale, true);
	}

	/**
	 * Exactly as {@link #formatMessage(DynamicResourceBundleMessage, Locale)}
	 * explains, but won't format the parameters. Only provided for validation
	 * since validator can't provide type-safe parameter values.
	 * 
	 * @param dynamicMessage
	 * @param locale
	 * @param formatParameters
	 *            should we format the parameters or not
	 * @return
	 */
	private String formatMessage(DynamicResourceBundleMessage dynamicMessage,
			Locale locale, boolean formatParameters) {

		String formattedMessage;
		Map<ResourceBundleMessageParameterKey, Object> parameters = dynamicMessage
				.getParameters();

		// only
		String unformattedMessage = getString(dynamicMessage.getMessage(),
				locale);
		// only try to format if there are parameters.
		if (parameters == null || parameters.isEmpty()) {
			formattedMessage = unformattedMessage;
		} else {
			KeyedMessageFormat messageFormat = new KeyedMessageFormat(
					unformattedMessage);

			if (formatParameters) {
				parameters = formatParameters(locale, parameters);
			}
			formattedMessage = messageFormat.format(parameters);
		}

		return formattedMessage;

	}

	/**
	 * Takes the given parameters and formats them either by the
	 * {@link CustomFormat} provided in the
	 * {@link ResourceBundleMessageParameterKey} itself or by using the default
	 * CustomFormats provided by the {@link CustomFormatFactory}.
	 * 
	 * @param locale
	 * @param parameters
	 * @return
	 */
	private Map<ResourceBundleMessageParameterKey, Object> formatParameters(
			Locale locale,
			Map<ResourceBundleMessageParameterKey, Object> parameters) {
		Set<Map.Entry<ResourceBundleMessageParameterKey, Object>> parameterEntries = parameters
				.entrySet();
		Map<ResourceBundleMessageParameterKey, Object> formattedParameters = new HashMap<ResourceBundleMessageParameterKey, Object>();
		// iterate each entry and provide the formatted value instead of the
		// object
		for (Map.Entry<ResourceBundleMessageParameterKey, Object> entry : parameterEntries) {
			ResourceBundleMessageParameterKey paramKey = entry.getKey();
			CustomFormat format = paramKey.getFormat();
			if (format == null) {
				format = CustomFormatFactory.getInstance().getFormat(
						entry.getValue().getClass());
			}
			Object value = entry.getValue();
			String formattedValue = format.format(value, locale);
			formattedParameters.put(paramKey, formattedValue);
		}
		return formattedParameters;
	}

	@SuppressWarnings("unchecked")
	public Enum<? extends ResourceBundleMessage> getResouceBundleMessage(
			String key) throws MissingResourceException {

		// this doesn't make any sense, but couldn't get it to work with
		// this.keyClass
		Enum<? extends ResourceBundleMessage> enum1 = keyClass
				.getEnumConstants()[0];

		try {
			// throws illegal argument if not available as enum
			return Enum.valueOf(enum1.getClass(), key);
		} catch (IllegalArgumentException e) {
			throw new MissingResourceException("No message with key of " + key,
					this.keyClass.getSimpleName(), key);
		}
	}

	protected void validate() {
		enumsMatchKeysInBundle();
		keysInBundleMatchEnums();
	}

	/**
	 * Iterates all enums making sure there is a matching key.
	 * 
	 * @throws MissingResourceException
	 */
	protected void enumsMatchKeysInBundle() throws MissingResourceException {

		Enum<? extends ResourceBundleMessage>[] enums = keyClass
				.getEnumConstants();
		for (Enum<? extends ResourceBundleMessage> currentMessage : enums) {
			validateMessage(currentMessage);
		}

	}

	/**
	 * Given a message this will inspect it and throw an exception if any of the
	 * messages or keys don't match the message provided.
	 * 
	 * @param enum1
	 */
	protected void validateMessage(
			Enum<? extends ResourceBundleMessage> enumMessage) {
		// this cast seems to be necesary for Eclipse.
		ResourceBundleMessage message = (ResourceBundleMessage) enumMessage;
		ResourceBundleMessageParameterKey[] params = message.getParameterKeys();
		String[] paramValues = new String[params.length];

		// populate values with fake values.
		for (int index = 0; index < paramValues.length; index++) {
			paramValues[index] = "Value" + index;
		}

		DynamicResourceBundleMessage dynamicMessage = new DynamicResourceBundleMessage(
				message, params, paramValues);
		// validates parameters match given string...runtime exception otherwise
		boolean formatParameters = false;
		formatMessage(dynamicMessage, Locale.getDefault(), formatParameters);
	}

	/**
	 * Iterates all keys in bundle looking for matching enum.
	 * 
	 * @see #validateMessage(ResourceBundleMessage)
	 * @see #enumsMatchKeysInBundle()
	 * 
	 * @throws IllegalArgumentException
	 */

	protected void keysInBundleMatchEnums() throws IllegalArgumentException {
		// only validate if a resource bundle is provided. If none then the enum
		// validation will be enough.
		if (this.resourceBundle != null) {
			Enumeration<String> keys = this.resourceBundle.getKeys();

			while (keys.hasMoreElements()) {
				String key = keys.nextElement();

				validateMessage(getResouceBundleMessage(key));

			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ManagedResourceBundle) {
			ManagedResourceBundle that = (ManagedResourceBundle) obj;
			return that.keyClass.equals(this.keyClass);
		}
		return false;
	}

	@Override
	public int hashCode() {

		return this.keyClass.hashCode();
	}

}
