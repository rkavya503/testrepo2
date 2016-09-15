/**
 * 
 */
package com.akuacom.common.exception;

import java.util.Locale;

import com.akuacom.common.resource.DynamicResourceBundleMessage;
import com.akuacom.common.resource.ManagedResourceBundle;
import com.akuacom.common.resource.ResourceBundleMessage;
import com.akuacom.common.resource.ResourceBundleMessageParameterKey;

/**
 * Given the strongly type-safe {@link ManagedResourceBundle} and it's
 * associated {@link ResourceBundleMessage} and
 * {@link ResourceBundleMessageParameterKey}s this will produce the localized
 * message from the Managed resource.
 * 
 * @author roller
 * 
 */
public class ManagedResourceBundleExceptionLocalizer implements
		ExceptionLocalizer {

	private static final long serialVersionUID = -8069383932438546417L;
	private DynamicResourceBundleMessage dynamicMessage;
	private Exception cause;
	// TODO:Make this actually localizable...by injection?
	private Locale locale = Locale.getDefault();
	private ManagedResourceBundle resourceBundle;

	ManagedResourceBundleExceptionLocalizer(
			ManagedResourceBundle resourceBundle,
			DynamicResourceBundleMessage dynamicMessage, Exception cause) {
		this.cause = cause;
		this.dynamicMessage = dynamicMessage;
		this.resourceBundle = resourceBundle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.common.exception.ExceptionLocalizer#getCause()
	 */
	@Override
	public Exception getCause() {
		return this.cause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.common.exception.ExceptionLocalizer#getMessage()
	 */
	@Override
	public String getMessage() {
		return this.resourceBundle.formatMessage(this.dynamicMessage,
				this.locale);
	}

	/**
	 * Used to produce instances of the Localizer given the
	 * {@link ManagedResourceBundle} and the
	 * {@link DynamicResourceBundleMessage} indicating the message key and it's
	 * parameters.
	 * 
	 * @author roller
	 * 
	 */
	static class Producer implements ExceptionLocalizerProducer {

		private ManagedResourceBundle resourceBundle;

		/**
		 * Default reserved for Factory loading for defaults.
		 * 
		 */
		Producer() {
		}

		public Producer(
				Class<? extends Enum<? extends ResourceBundleMessage>> keyClass) {
			this.resourceBundle = new ManagedResourceBundle(keyClass);
		}

		@Override
		public ExceptionLocalizer understands(Object criteria, Exception cause) {

			if (criteria instanceof ResourceBundleMessage) {
				criteria = new DynamicResourceBundleMessage(
						(ResourceBundleMessage) criteria);
			}
			if (criteria instanceof DynamicResourceBundleMessage
					&& this.resourceBundle != null) {
				return new ManagedResourceBundleExceptionLocalizer(
						this.resourceBundle,
						(DynamicResourceBundleMessage) criteria, cause);

			}
			return null;

		}

		@Override
		public ExceptionLocalizer getDefault(Object criteria, Exception cause) {

			if (criteria instanceof ResourceBundleMessage) {
				criteria = new DynamicResourceBundleMessage(
						(ResourceBundleMessage) criteria);
			}
			if (criteria instanceof DynamicResourceBundleMessage) {

				ResourceBundleMessage message = ((DynamicResourceBundleMessage) criteria)
						.getMessage();
				String exceptionMessage;
				if (message.getValue() == null) {
					exceptionMessage = message.toString();
				} else {
					// in this case, the resource bundle is not registered, but
					// the value of the message is provided in the Enum
					exceptionMessage = message.getValue();
				}

				return new DefaultExceptionLocalizer(exceptionMessage, cause);
			} else {
				return null;
			}
		}

	}

}
