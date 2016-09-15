/**
 * 
 */
package com.akuacom.common.text;

/**
 * @author roller
 *
 */
public class FloatFormat extends DecimalFormat<Float> {

	@Override
	public Class<Float> handlesType() {
		return Float.class;
	}

}
