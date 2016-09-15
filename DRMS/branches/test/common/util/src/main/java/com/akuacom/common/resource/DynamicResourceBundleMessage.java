/**
 * 
 */
package com.akuacom.common.resource;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author roller
 * 
 */
public class DynamicResourceBundleMessage implements Serializable {

	private static final long serialVersionUID = 1796219064242304151L;

	private ResourceBundleMessage message;
	private Map<ResourceBundleMessageParameterKey, Object> parameters;

	public ResourceBundleMessage getMessage() {
		return message;
	}

	public Map<ResourceBundleMessageParameterKey, Object> getParameters() {
		return parameters;
	}

	public DynamicResourceBundleMessage(ResourceBundleMessage message,
			Map<ResourceBundleMessageParameterKey, Object> parameters) {
		this.message = message;
		this.parameters = Collections.unmodifiableMap(parameters);
	}

	public DynamicResourceBundleMessage(ResourceBundleMessage message) {
		this.message = message;
		this.parameters = new HashMap<ResourceBundleMessageParameterKey, Object>();
	}

	/**
	 * Provids easy access to those that have an array of keys and an array of
	 * values. The key will correspond to the value by the same index.
	 * 
	 * @param message
	 * @param indexedKeys
	 * @param indexedParams
	 * @throws IllegalArgumentException
	 *             when keys length do not match indexed params
	 */
	public DynamicResourceBundleMessage(ResourceBundleMessage message,
			ResourceBundleMessageParameterKey[] indexedKeys,
			Object[] indexedParams) {
		this(message);
		if (indexedKeys.length != indexedParams.length) {
			throw new IllegalArgumentException(indexedKeys.length
					+ " keys found not matching the number of params "
					+ indexedParams.length);
		}
		int counter = 0;
		for (ResourceBundleMessageParameterKey key : indexedKeys) {
			this.addParameter(key, indexedParams[counter]);
			counter++;
		}

	}

	public DynamicResourceBundleMessage addParameter(ResourceBundleMessageParameterKey key, Object value) {
		validate(key);
		Object pushed = this.parameters.put(key, value);
		if (pushed != null) {
			throw new IllegalArgumentException(key + " already had a value "
					+ pushed);
		}
		return this;
	}

	/**Given the key this will ensure the parameter is registered with the message.
	 * 
	 * @param key
	 */
	protected void validate(ResourceBundleMessageParameterKey key) {
		ResourceBundleMessageParameterKey[] keys = this.message
				.getParameterKeys();

		if (!ArrayUtils.contains(keys, key)) {
			throw new IllegalArgumentException(key
					+ " is not a valid parameter for message " + this.message);
		}
	}

	
}
