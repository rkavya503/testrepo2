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
abstract public class CustomNumberFormat<NumberType extends Number>
		implements CustomFormat {

	private Boolean groupingUsed;

	/**
	 * Provides the configured NumberFormat for the specific type of
	 * NumberFormat needed. Override if the general NumberFormat is needed.
	 * 
	 * @see NumberFormat#getNumberInstance(Locale)
	 * 
	 * @param locale
	 * @return
	 */
	protected NumberFormat getNumberFormat(Locale locale) {
		return NumberFormat.getNumberInstance(locale);
	}

	/**
	 * Provides the defaults for all the configurations.
	 * 
	 */
	public CustomNumberFormat() {
		this.groupingUsed = true;
	}

	public CustomNumberFormat(Boolean groupingUsed) {
		super();
		this.groupingUsed = groupingUsed;
	}

	/**mutates the numberformat applying all properties.  Encouraged to be overloaded by extensions that need to change their properties.
	 * 
	 * @param format
	 */
	protected void applyProperties(NumberFormat format){
		format.setGroupingUsed(isGroupingUsed());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.common.resource.ResourceBundleMessageParamFormat#format(java
	 * .lang.Object, java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String format(Object object, Locale locale) {


		NumberType value = (NumberType) object;

		NumberFormat format = getNumberFormat(locale);
		applyProperties(format);
		return format.format(value.doubleValue());

	}

	/**
	 * @return the groupingUsed
	 */
	public Boolean isGroupingUsed() {
		return groupingUsed;
	}

	@Override
	abstract public Class<NumberType> handlesType();

}
