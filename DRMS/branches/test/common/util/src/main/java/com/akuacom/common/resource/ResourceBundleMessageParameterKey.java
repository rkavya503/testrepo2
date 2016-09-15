/**
 * 
 */
package com.akuacom.common.resource;

import com.akuacom.common.text.CustomFormat;

/**
 * Used in message bundles, this provides the key for the parameter when
 * expected to format a message.
 * 
 * @see KeyedMessageFormat to understand how parameters are used to format
 *      messages
 * @author roller
 * 
 */
public interface ResourceBundleMessageParameterKey {

	/**
	 * provides access to the key value, hoping you will use an enum to
	 * represent the constant.
	 * 
	 * @return
	 */
	public String toString();

	/**
	 * Optionally provides the specific format to use for this parameter.
	 * 
	 * @return the implementation that will format for the locale provide or
	 *         null if not specified.
	 */
	public CustomFormat getFormat();
}
