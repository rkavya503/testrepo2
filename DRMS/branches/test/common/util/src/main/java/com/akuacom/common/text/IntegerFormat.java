/**
 * 
 */
package com.akuacom.common.text;

import java.text.NumberFormat;
import java.util.Locale;


/**
 * @author roller
 *
 */
public class IntegerFormat extends
		CustomNumberFormat<Integer> {

	@Override
	protected NumberFormat getNumberFormat(Locale locale) {
		return NumberFormat.getIntegerInstance(locale);
	}

	@Override
	public Class<Integer> handlesType() {
		return Integer.class;
	}

}
