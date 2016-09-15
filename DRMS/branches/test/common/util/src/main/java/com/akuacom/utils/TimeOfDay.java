/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.TimeOfDay.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

/**
 * The Class TimeOfDay.
 */
public class TimeOfDay
{
	
	/** The hour. */
	private int hour;
	
	/** The minute. */
	private int minute;
	
	/** The second. */
	private int second;
	
	/**
	 * Gets the hour.
	 * 
	 * @return the hour
	 */
	public int getHour()
	{
		return hour;
	}
	
	/**
	 * Sets the hour.
	 * 
	 * @param hour the new hour
	 */
	public void setHour(int hour)
	{
		this.hour = hour;
	}
	
	/**
	 * Gets the minute.
	 * 
	 * @return the minute
	 */
	public int getMinute()
	{
		return minute;
	}
	
	/**
	 * Sets the minute.
	 * 
	 * @param minute the new minute
	 */
	public void setMinute(int minute)
	{
		this.minute = minute;
	}
	
	/**
	 * Gets the second.
	 * 
	 * @return the second
	 */
	public int getSecond()
	{
		return second;
	}
	
	/**
	 * Sets the second.
	 * 
	 * @param second the new second
	 */
	public void setSecond(int second)
	{
		this.second = second;
	}
}
