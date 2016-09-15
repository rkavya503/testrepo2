package com.akuacom.pss2.signal;

import java.util.Date;

/**
 * Represents a data point in time.  A Signal has a collection of these.
 * 
 *  A signal entry can be either a level or number signal.  Check parent 
 *  getSignalDef.isLevelSignal to determine which it is.
 *   
 */

public interface SignalEntry extends Comparable<SignalEntry> {
	public Signal getParentSignal();
	public void setParentSignal(Signal signal);
	
	/**
	 * The time this signal is effective.  
	 * @return
	 */
	public Date getTime();
	public void setTime(Date time);
	
	public Boolean getExpired();
	public void setExpired(Boolean isExpired);
	
	/**
	 * Get the string value representing a level value.
	 * 
	 * @return
	 */
	public String getLevelValue();
	public void setLevelValue(String stringValue);
	
	/**
	 * get number value representing a number value
	 * @return
	 */
	public Double getNumberValue();
	public void setNumberValue(Double numberValue);
	
	/**
	 * The value represented as a String regardless of whether this 
	 * is a level or number value
	 * 
	 * @return
	 */
	public String getValueAsString();

	public String getUUID();
}
