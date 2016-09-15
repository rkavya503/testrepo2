/**
 * 
 */
package com.akuacom.common.exception;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import com.akuacom.common.resource.DynamicResourceBundleMessage;
import com.akuacom.common.resource.ManagedResourceBundle;
import com.akuacom.common.resource.ResourceBundleMessage;
import com.akuacom.common.resource.ResourceBundleMessageParameterKey;
import com.akuacom.pss2.core.ResourceBundleUtil;

/**
 * Produces {@link ExceptionLocalizer}s which must be registered as static
 * participants. Each ExceptionLocalizer must provide a
 * {@link ExceptionLocalizerProducer} that will construct a new Localizer given
 * resources (such as {@link ResourceBundle} or {@link ManagedResourceBundle}.
 * 
 * This is a general tool that should not be coupled to every possible resource
 * that may provide localizable messages so it is important that your system
 * initializes those resources that are available in the appropriate place (i.e.
 * Initialization Servlet, Base Test, etc). If the resources are not registered
 * then this will display the keys instead of the messages corresponding to the
 * keys.
 * 
 * 
 * @author roller
 * 
 */
public class ExceptionLocalizerFactory {

	private static ExceptionLocalizerFactory instance;

	/**
	 * The singleton that should be used by everyone in the system to get
	 * Localizer and register localizers.
	 * 
	 * @return
	 */
	public static ExceptionLocalizerFactory getInstance() {
		if (instance == null) {
			instance = new ExceptionLocalizerFactory();
			// register default producers as a backup if the instances with
			// actual resources are not registered at system initialization
			instance.registerProducer(new DefaultExceptionLocalizer.Producer());
			instance
					.registerProducer(new ResourceBundleExceptionLocalizer.Producer());
			instance
					.registerProducer(new ManagedResourceBundleExceptionLocalizer.Producer());

			// TODO: make the PSS2 application register their own darned messages.
			// messages.
			instance
					.registerProducer(new ResourceBundleExceptionLocalizer.Producer(
							ResourceBundleUtil.resourceName));

		}
		return instance;
	}

	private Set<ExceptionLocalizerProducer> producers = new HashSet<ExceptionLocalizerProducer>();

	/* package friendly */ExceptionLocalizerFactory() {

	}

	private ExceptionLocalizer askProducersForLocalizer(Object criteria,
			Throwable throwable) {
		Exception exception;
		if (throwable == null || throwable instanceof Exception) {
			exception = (Exception) throwable;
		} else {
			// you shouldn't be catching Errors...shame on you.
			throw ((Error) throwable);
		}
		ExceptionLocalizer localizer = null;
		Iterator<ExceptionLocalizerProducer> allProducers = this.producers
				.iterator();

		// look for the localizer that understands the critieria
		while (allProducers.hasNext() && localizer == null) {
			ExceptionLocalizerProducer next = allProducers.next();
			localizer = next.understands(criteria, exception);
		}
		// try again.
		allProducers = this.producers.iterator();
		// just create the default if not recognized
		while (allProducers.hasNext() && localizer == null) {
			ExceptionLocalizerProducer next = allProducers.next();
			localizer = next.getDefault(criteria, exception);
		}
		if (localizer == null) {
			throw new UnsupportedOperationException("Unknown criteria "
					+ criteria);
		}
		return localizer;
	}

	public ExceptionLocalizer getExceptionLocalizer(
			DynamicResourceBundleMessage dynamicMessage) {
		return this.getExceptionLocalizer(dynamicMessage, null);
	}

	public ExceptionLocalizer getExceptionLocalizer(
			DynamicResourceBundleMessage dynamicMessage, Throwable cause) {

		return askProducersForLocalizer(dynamicMessage, cause);
	}

	public ExceptionLocalizer getExceptionLocalizer(
			ResourceBundleMessage message) {

		return getExceptionLocalizer(message, null);
	}

	public ExceptionLocalizer getExceptionLocalizer(
			ResourceBundleMessage message,
			ResourceBundleMessageParameterKey[] parameterKeys,
			Object[] indexedValues) {
		return this.getExceptionLocalizer(message, parameterKeys,
				indexedValues, null);
	}

	public ExceptionLocalizer getExceptionLocalizer(
			ResourceBundleMessage message,
			ResourceBundleMessageParameterKey[] parameterKeys,
			Object[] indexedValues, Exception cause) {

		return this.getExceptionLocalizer(new DynamicResourceBundleMessage(
				message, parameterKeys, indexedValues), cause);
	}

	public ExceptionLocalizer getExceptionLocalizer(
			ResourceBundleMessage messageProperties, Throwable exception) {

		return askProducersForLocalizer(messageProperties, exception);
	}

	public ExceptionLocalizer getExceptionLocalizer(String keyOrMessage) {

		return this.getExceptionLocalizer(keyOrMessage, (Throwable) null);
	}

	public ExceptionLocalizer getExceptionLocalizer(String key, Object[] params) {

		return getExceptionLocalizer(key, params, null);
	}

	public ExceptionLocalizer getExceptionLocalizer(String key,
			Object[] params, Throwable exception) {

		return this.askProducersForLocalizer(new VariableCriteria(key, params),
				exception);
	}

	public ExceptionLocalizer getExceptionLocalizer(String keyOrMessage,
			Throwable exception) {
		return askProducersForLocalizer(keyOrMessage, exception);

	}

	public ExceptionLocalizer getExceptionLocalizer(Throwable exception) {

		return getExceptionLocalizer(exception.getMessage(), exception);
	}

	public boolean registerProducer(ExceptionLocalizerProducer producer) {
		return this.producers.add(producer);
	}

	/**
	 * Given a string-based key and some corresponding variables this will use
	 * {@link MessageFormat} to replace the targeted indexes in the value of the
	 * message with the values provided in the indexed variables.
	 * 
	 * @author roller
	 * 
	 */
	protected static class VariableCriteria implements Serializable {
		private static final long serialVersionUID = 5236163301254074178L;

		private String key;

		private Object[] variables;

		public VariableCriteria(String key) {
			this.key = key;
		}

		public VariableCriteria(String key, Object[] variables) {
			super();
			this.key = key;
			this.variables = variables;
		}

		public String getKey() {
			return key;
		}

		public Object[] getVariables() {
			return variables;
		}

	}

}
