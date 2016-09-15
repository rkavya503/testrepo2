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
public class LongFormat extends
		CustomNumberFormat<Long> {

	@Override
	public Class<Long> handlesType() {
		return Long.class;
	}

	@Override
	protected NumberFormat getNumberFormat(Locale locale) {
		
		return NumberFormat.getIntegerInstance(locale);
	}

}
