/**
 * 
 */
package com.akuacom.common.text;

/**
 * @author roller
 *
 */
public class DoubleFormat extends DecimalFormat<Double> {

	@Override
	public Class<Double> handlesType() {
		return Double.class;
	}

}
