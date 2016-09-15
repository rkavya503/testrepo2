/**
 * 
 */
package com.akuacom.common.exception;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.akuacom.common.exception.ExceptionLocalizerFactory.VariableCriteria;
import com.akuacom.pss2.core.ResourceBundleUtil;
import com.akuacom.utils.lang.StringUtil;

/**
 * Since we need both runtime and checked exceptions and we don't have dual
 * inheritance (since they must extend Exception and RuntimeException
 * accordingly) to be localized, this helper class provides the common
 * functionality between both.
 * 
 * Localizable exceptions depend on the {@link MessageFormat} to do the work of
 * localizing based on the keys and indexed parameters provided.
 * 
 * @author roller
 * 
 */
public class ResourceBundleExceptionLocalizer implements ExceptionLocalizer {

	private static final long serialVersionUID = 5167774523914101554L;

	private Exception cause;

	private String resourceBundleBaseName;

	private ExceptionLocalizerFactory.VariableCriteria criteria;

	/**
	 * TODO:Provide ability for this to be assigned or injected. Injection is
	 * ideal because it is unlikely that locale will be known when an exception
	 * is thrown. That is up to the view, where this is part of the model.
	 */
	private Locale locale = Locale.getDefault();

	/**
	 * 
	 */
	public ResourceBundleExceptionLocalizer(String resourceBundleBaseName,
			ExceptionLocalizerFactory.VariableCriteria criteria, Exception cause) {
		this.criteria = criteria;
		this.resourceBundleBaseName = resourceBundleBaseName;
		this.cause = cause;
	}

	@Override
	public String getMessage() {

		ResourceBundle bundle = ResourceBundle.getBundle(
				resourceBundleBaseName, getLocale());
		String unformattedMessage = bundle.getString(this.criteria.getKey());
		Object[] variables = this.criteria.getVariables();
		String formattedMessage;
		if (variables != null && variables.length > 0) {
			try {
				formattedMessage = MessageFormat.format(unformattedMessage,
						variables);
				if(formattedMessage.contains("${?}")){
					//no formatting took place, try to the legacy to see if there is a change
					
					// some legacy messages apparently don't throw Illegal
					// Arguments....
					// this shouldn't hurt to try and format the parameters if they
					// already exist.
					formattedMessage = getLegacyMessage(unformattedMessage,
							variables);
				}
			} catch (IllegalArgumentException e) {
				// maybe it was a legacy message
				formattedMessage = getLegacyMessage(unformattedMessage,
						variables);
			}
		} else {
			// nothing to format 'cause no variables
			formattedMessage = unformattedMessage;
		}
		return formattedMessage;
	}

	/**
	 * Help method to eventually be removed once all the messages are
	 * transferred to the new format.
	 * 
	 * @param unformattedMessage
	 * @param variables
	 * @return
	 */
	private String getLegacyMessage(String unformattedMessage,
			Object[] variables) {
		String formattedMessage;
		String[] stringVariables;
		// Try Legacy RB handler
		// TODO: Remove this once ResourceBundleUtil can be retired completely
		if (variables instanceof String[]) {
			stringVariables = (String[]) variables;
		} else {
			stringVariables = StringUtil.convertArray(variables);
		}

		List<String> variableList = Arrays.asList(stringVariables);
		formattedMessage = ResourceBundleUtil.getLocalizedString(
				unformattedMessage, variableList);
		return formattedMessage;
	}

	private Locale getLocale() {

		return this.locale;
	}

	@Override
	public Exception getCause() {

		return this.cause;
	}

	protected static class Producer implements ExceptionLocalizerProducer {

		private String resourceBundleBaseName;

		public Producer(String resourceBundleBaseName) {
			this.resourceBundleBaseName = resourceBundleBaseName;
		}

		/**
		 * Default constructor used for registering the producer for default
		 * values.
		 * 
		 */
		Producer() {

		}

		@Override
		public ExceptionLocalizer understands(Object criteriaObj,
				Exception cause) {

			String messageKey;
			ResourceBundleExceptionLocalizer localizer;
			if (this.resourceBundleBaseName == null) {
				// this must be the default producer...don't understand anything
				// and just go with the default if it comes back around
				localizer = null;
			} else {
				VariableCriteria criteria;
				if (criteriaObj instanceof String) {
					messageKey = (String) criteriaObj;
					criteria = new VariableCriteria(messageKey);

				} else if (criteriaObj instanceof VariableCriteria) {
					criteria = (VariableCriteria) criteriaObj;
					messageKey = criteria.getKey();

				} else {
					// we just don't know what it is
					criteria = null;
					messageKey = null;
				}

				if (messageKey != null
						&& getResourceBundle().containsKey(messageKey)) {
					localizer = new ResourceBundleExceptionLocalizer(
							this.resourceBundleBaseName, criteria, cause);
				} else {
					localizer = null;
				}
			}
			return localizer;
		}

		private ResourceBundle getResourceBundle() {

			return ResourceBundle.getBundle(resourceBundleBaseName);
		}

		@Override
		public ExceptionLocalizer getDefault(Object criteria, Exception cause) {
			if (criteria instanceof VariableCriteria) {
				// resource bundle not found so just display the key until they
				// register
				VariableCriteria variableCriteria = (VariableCriteria) criteria;
				return new DefaultExceptionLocalizer(variableCriteria.getKey(),
						cause);

			} else {
				return null;
			}
		}

	}

}
