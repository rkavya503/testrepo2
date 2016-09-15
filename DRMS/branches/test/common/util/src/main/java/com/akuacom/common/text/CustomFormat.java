/**
 * 
 */
package com.akuacom.common.text;

import java.util.Locale;

/**
 * Formats objects in the system based on the locale. indicates the type of
 * formats each instance handles.
 * 
 * @author roller
 * 
 */
public interface CustomFormat {

	/**
	 * The method called to transform an object into the specific format.
	 * 
	 * @param object
	 * @return
	 */
	public String format(Object object, Locale locale);

	Class<? extends Object> handlesType();
}
