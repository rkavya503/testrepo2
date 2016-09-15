/**
 * 
 */
package com.akuacom.common.text;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Provides the localized representation in percent when given a number.
 * 
 * Do not register this with the factory because it handles Double.class and
 * will replace general number formatting. This could be registered if a
 * specific PercentNumber is provided.
 * 
 * @author roller
 * 
 */
public class PercentFormat extends DoubleFormat {

	@Override
	protected NumberFormat getNumberFormat(Locale locale) {
		return NumberFormat.getPercentInstance(locale);
	}

}
