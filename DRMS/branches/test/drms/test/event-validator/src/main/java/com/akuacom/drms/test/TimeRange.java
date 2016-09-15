package com.akuacom.drms.test;
/**
 * Holds a time range
 * @author Sunil
 *
 */
public class TimeRange {

	/**
	 * Start Time in millis
	 */
	long start;
	
	/**
	 * Stop time in millis
	 */
	long stop;
	
	/**
	 * Constructor
	 * @param start start time for the range in millis
	 * @param stop stop time for the range in millis
	 */
	public TimeRange(long start, long stop){
		this.start = start;
		this.stop = stop;
	}
	
	/**
	 * Checks if the the range provided is within the time range
	 * @param testTime test time in millis
	 * @return true if test time is in the range
	 */
	public boolean isInRange(long testTime){
		if(testTime >= this.start && testTime < this.stop ){
			return true;
		}
		return false;
	}
	
}
