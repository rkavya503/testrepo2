/**
 * 
 */
package com.akuacom.common.resource;

import java.util.Locale;

import com.akuacom.common.text.CustomFormat;

/**
 * Simply demonstrates how to use a formatter to format your parameters.
 * 
 * @author roller
 * 
 */
public class ExampleUsernameCustomFormat implements
		CustomFormat {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.common.resource.ResourceBundleMessageParamFormat#format(java
	 * .lang.Object, java.util.Locale)
	 */
	@Override
	public String format(Object object, Locale locale) {
		String username = object.toString();
		return username.toLowerCase();
	}

	@Override
	public Class<? extends Object> handlesType() {
		
		return String.class;
	}

}
