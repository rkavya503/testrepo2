/**
 * 
 */
package com.akuacom.common.text;

import java.text.NumberFormat;

/**
 * @author roller
 * 
 */
abstract public class DecimalFormat<DecimalType extends Number> extends
		CustomNumberFormat<DecimalType> {

	private int maximumFractionDigits;
	private int minimumFractionDigits;

	public DecimalFormat(int maximumFractionDigits, int minimumFractionDigits,
			boolean isGroupingEnabled) {
		super(isGroupingEnabled);
		this.maximumFractionDigits = maximumFractionDigits;
		this.minimumFractionDigits = minimumFractionDigits;
	}

	public DecimalFormat() {
		super();
		this.maximumFractionDigits = 2;
		this.minimumFractionDigits = 1;
	}

	@Override
	protected void applyProperties(NumberFormat format) {

		super.applyProperties(format);
		format.setMaximumFractionDigits(this.maximumFractionDigits);
		format.setMinimumIntegerDigits(this.minimumFractionDigits);
	}

}
