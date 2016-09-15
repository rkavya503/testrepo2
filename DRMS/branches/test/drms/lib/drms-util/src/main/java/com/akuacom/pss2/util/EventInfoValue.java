/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.EventInfoValue.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.io.Serializable;

/**
 * The Class EventInfoValue.
 */
public class EventInfoValue implements Serializable, Comparable<EventInfoValue>
{
	
	/** The value. */
	private double value;
	
	/** The time offset s. */
	private double timeOffsetS;
	
	/**
	 * Instantiates a new event info value.
	 */
	public EventInfoValue()
	{
	}
	
	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public double getValue()
	{
		return value;
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param value the new value
	 */
	public void setValue(double value)
	{
		this.value = value;
	}
	
	/**
	 * Gets the time offset s.
	 * 
	 * @return the time offset s
	 */
	public double getTimeOffsetS()
	{
		return timeOffsetS;
	}
	
	/**
	 * Sets the time offset s.
	 * 
	 * @param timeOffsetS the new time offset s
	 */
	public void setTimeOffsetS(double timeOffsetS)
	{
		this.timeOffsetS = timeOffsetS;
	}
	
	public int compareTo(EventInfoValue o) {
		if (o == null) {
			return 1;
		}
		
		return new Double(this.getTimeOffsetS()).compareTo(new Double(o.getTimeOffsetS()));
	}
}
