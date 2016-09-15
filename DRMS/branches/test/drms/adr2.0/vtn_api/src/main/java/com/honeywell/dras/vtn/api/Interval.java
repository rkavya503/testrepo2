package com.honeywell.dras.vtn.api;

/**
 * Interval
 * @author sunil
 *
 */
public interface Interval {

	/**
	 * Duration in hours, minutes, seconds or milliseconds based on duration resolution
	 */
	public long getDurationInResolution();
	
	public IntervalDurationResolution getResolution();
	
	public String getOffsetId();
	
	public float getValue();
	
	public String getStringValue();
}
