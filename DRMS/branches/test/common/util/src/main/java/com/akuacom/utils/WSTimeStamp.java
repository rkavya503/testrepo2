/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.WSTimeStamp.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.io.Serializable;

/**
 * The Class WSTimeStamp.
 */
public class WSTimeStamp implements Serializable
{
	
	/** The year. */
	private int year;
	
	/** The month. */
	private int month;
	
	/** The day. */
	private int day;
	
	/** The hour. */
	private int hour;
	
	/** The minute. */
	private int minute;
	
	/** The second. */
	private int second;
	
	/** The milliseconds. */
	private int milliseconds;
	
	/**
	 * Instantiates a new wS time stamp.
	 */
	public WSTimeStamp()
	{
	}

	/**
	 * Instantiates a new wS time stamp.
	 * 
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @param hour the hour
	 * @param minute the minute
	 * @param second the second
	 * @param milliseconds the milliseconds
	 */
	public WSTimeStamp(int year, int month, int day, int hour, int minute, 
	  int second, int milliseconds)
	{
		super();
		// TODO Auto-generated constructor stub
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.milliseconds = milliseconds;
	}
	
	/**
	 * Gets the day.
	 * 
	 * @return the day
	 */
	public int getDay()
	{
		return day;
	}
	
	/**
	 * Sets the day.
	 * 
	 * @param day the new day
	 */
	public void setDay(int day)
	{
		this.day = day;
	}
	
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
	 * Gets the milliseconds.
	 * 
	 * @return the milliseconds
	 */
	public int getMilliseconds()
	{
		return milliseconds;
	}
	
	/**
	 * Sets the milliseconds.
	 * 
	 * @param milliseconds the new milliseconds
	 */
	public void setMilliseconds(int milliseconds)
	{
		this.milliseconds = milliseconds;
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
	 * Gets the month.
	 * 
	 * @return the month
	 */
	public int getMonth()
	{
		return month;
	}
	
	/**
	 * Sets the month.
	 * 
	 * @param month the new month
	 */
	public void setMonth(int month)
	{
		this.month = month;
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
	
	/**
	 * Gets the year.
	 * 
	 * @return the year
	 */
	public int getYear()
	{
		return year;
	}
	
	/**
	 * Sets the year.
	 * 
	 * @param year the new year
	 */
	public void setYear(int year)
	{
		this.year = year;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		return "util:LSTimeStamp: month:"+month
				+" day:"+day +" year:"+year
				+" hour:"+hour+" minute:"+minute
				+" second:"+second;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(Object otherObject)
    {
    	if(this == otherObject)
    	{
    		return true;
    	}
    	if(otherObject == null)
    	{
    		return false;
    	}
    	if(!(otherObject instanceof WSTimeStamp))
    	{
    		return false;
    	}
    	WSTimeStamp other = (WSTimeStamp)otherObject;

    	return 
    		year == other.year && 
    		year == other.month && 
    		year == other.day && 
    		year == other.hour && 
    		year == other.minute && 
    		year == other.second && 
    		year == other.milliseconds;
    }
}

