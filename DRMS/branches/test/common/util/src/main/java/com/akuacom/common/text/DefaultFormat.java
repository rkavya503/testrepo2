/**
 * 
 */
package com.akuacom.common.text;

import java.util.Locale;

/**
 * When no other format is recognized this will just provide the toString
 * version of the object.
 * 
 * If you don't like it, provide a CustomFormat that will do a better job.
 * 
 * @author roller
 * 
 */
public class DefaultFormat implements CustomFormat {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.common.text.CustomFormat#format(java.lang.Object,
	 * java.util.Locale)
	 */
	@Override
	public String format(Object object, Locale locale) {
		return object.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.common.text.CustomFormat#handlesType()
	 */
	@Override
	public Class<? extends Object> handlesType() {

		return Object.class;
	}

}
