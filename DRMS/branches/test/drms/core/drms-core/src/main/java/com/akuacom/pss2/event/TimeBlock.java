/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.TimeBlock.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The Class TimeBlock.
 */
public class TimeBlock implements Comparable<TimeBlock>, Serializable
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7407312838646629471L;

	/** The Constant REFERENCE_YEAR. */
	private static final int REFERENCE_YEAR = 2000;
	
	/** The Constant REFERENCE_MONTH. */
	private static final int REFERENCE_MONTH = 1;
	
	/** The Constant REFERENCE_DAY. */
	private static final int REFERENCE_DAY = 1;

    /** The id. */
    private String id;
	
	/** The start hour. */
	private int startHour;
	
	/** The start minute. */
	private int startMinute;
	
	/** The start second. */
	private int startSecond;
	
	/** The end hour. */
	private int endHour;
	
	/** The end minute. */
	private int endMinute;
	
	/** The end second. */
	private int endSecond;

	/**
	 * Instantiates a new time block.
	 */
	public TimeBlock()
	{
	}
	
	/**
	 * Instantiates a new time block.
	 * 
	 * @param start the start
	 * @param end the end
	 */
	public TimeBlock(Date start, Date end)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(start);
		startHour = cal.get(Calendar.HOUR_OF_DAY);
		startMinute = cal.get(Calendar.MINUTE);
		startSecond = cal.get(Calendar.SECOND);
		cal.setTime(end);
		endHour = cal.get(Calendar.HOUR_OF_DAY);
		endMinute = cal.get(Calendar.MINUTE);
		endSecond = cal.get(Calendar.SECOND);
	}
	
	/**
	 * Gets the start time.
	 * 
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * 
	 * @return the start time
	 */
	public Date getStartTime(int year, int month, int day)
	{
		return new GregorianCalendar(
		  year,
		  month,
		  day,
		  startHour,
		  startMinute,
		  startSecond).getTime();		
	}

	/**
	 * Gets the end time.
	 * 
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * 
	 * @return the end time
	 */
	public Date getEndTime(int year, int month, int day)
	{
		return new GregorianCalendar(
		  year,
		  month,
		  day,
		  endHour,
		  endMinute,
		  endSecond).getTime();		
	}

	/**
	 * Gets the start reference time.
	 * 
	 * @return the start reference time
	 */
	public Date getStartReferenceTime()
	{
		return getStartTime(
		  REFERENCE_YEAR,
		  REFERENCE_MONTH,
		  REFERENCE_DAY);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		// TODO: add formatting
		StringBuilder sb = new StringBuilder();
		if(startHour < 10)
		{
			sb.append("0");
		}
		sb.append(startHour);
		sb.append(":");
		if(startMinute < 10)
		{
			sb.append("0");
		}
		sb.append(startMinute);
		sb.append(" - ");
		if(endHour < 10)
		{
			sb.append("0");
		}
		sb.append(endHour);
		sb.append(":");
		if(endMinute < 10)
		{
			sb.append("0");
		}
		sb.append(endMinute);
		return sb.toString();	
	}
	
	/**
	 * Gets the end reference time.
	 * 
	 * @return the end reference time
	 */
	public Date getEndReferenceTime()
	{
		return getEndTime(
		  REFERENCE_YEAR,
		  REFERENCE_MONTH,
		  REFERENCE_DAY);
	}
	
	/**
	 * Gets the end hour.
	 * 
	 * @return the end hour
	 */
	public int getEndHour()
	{
		return endHour;
	}

	/**
	 * Sets the end hour.
	 * 
	 * @param endHour the new end hour
	 */
	public void setEndHour(int endHour)
	{
		this.endHour = endHour;
	}

	/**
	 * Gets the end minute.
	 * 
	 * @return the end minute
	 */
	public int getEndMinute()
	{
		return endMinute;
	}

	/**
	 * Sets the end minute.
	 * 
	 * @param endMinute the new end minute
	 */
	public void setEndMinute(int endMinute)
	{
		this.endMinute = endMinute;
	}

	/**
	 * Gets the end second.
	 * 
	 * @return the end second
	 */
	public int getEndSecond()
	{
		return endSecond;
	}

	/**
	 * Sets the end second.
	 * 
	 * @param endSecond the new end second
	 */
	public void setEndSecond(int endSecond)
	{
		this.endSecond = endSecond;
	}

	/**
	 * Gets the start hour.
	 * 
	 * @return the start hour
	 */
	public int getStartHour()
	{
		return startHour;
	}

	/**
	 * Sets the start hour.
	 * 
	 * @param startHour the new start hour
	 */
	public void setStartHour(int startHour)
	{
		this.startHour = startHour;
	}

	/**
	 * Gets the start minute.
	 * 
	 * @return the start minute
	 */
	public int getStartMinute()
	{
		return startMinute;
	}

	/**
	 * Sets the start minute.
	 * 
	 * @param startMinute the new start minute
	 */
	public void setStartMinute(int startMinute)
	{
		this.startMinute = startMinute;
	}

	/**
	 * Gets the start second.
	 * 
	 * @return the start second
	 */
	public int getStartSecond()
	{
		return startSecond;
	}

	/**
	 * Sets the start second.
	 * 
	 * @param startSecond the new start second
	 */
	public void setStartSecond(int startSecond)
	{
		this.startSecond = startSecond;
	}

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Compare start times.
     * 
     * @param timeBlock1 the time block1
     * @param timeBlock2 the time block2
     * 
     * @return the int
     */
    public static int compareStartTimes(TimeBlock timeBlock1, 
    	TimeBlock timeBlock2)
    {
    	if(timeBlock1.startHour < timeBlock2.startHour)
    	{
    		return -1;
    	}
    	if(timeBlock1.startHour > timeBlock2.startHour)
    	{
    		return 1;
    	}
    	if(timeBlock1.startMinute < timeBlock2.startMinute)
    	{
    		return -1;
    	}
    	if(timeBlock1.startMinute > timeBlock2.startMinute)
    	{
    		return 1;
    	}
    	if(timeBlock1.startSecond < timeBlock2.startSecond)
    	{
    		return -1;
    	}
    	if(timeBlock1.startSecond > timeBlock2.startSecond)
    	{
    		return 1;
    	}
    	return 0;
    }

    /**
     * Compare end times.
     * 
     * @param timeBlock1 the time block1
     * @param timeBlock2 the time block2
     * 
     * @return the int
     */
    public static int compareEndTimes(TimeBlock timeBlock1, 
    	TimeBlock timeBlock2)
    {
    	if(timeBlock1.endHour < timeBlock2.endHour)
    	{
    		return -1;
    	}
    	if(timeBlock1.endHour > timeBlock2.endHour)
    	{
    		return 1;
    	}
    	if(timeBlock1.endMinute < timeBlock2.endMinute)
    	{
    		return -1;
    	}
    	if(timeBlock1.endMinute > timeBlock2.endMinute)
    	{
    		return 1;
    	}
    	if(timeBlock1.endSecond < timeBlock2.endSecond)
    	{
    		return -1;
    	}
    	if(timeBlock1.endSecond > timeBlock2.endSecond)
    	{
    		return 1;
    	}
    	return 0;
    }

    /**
     * Compare endto start times.
     * 
     * @param timeBlock1 the time block1
     * @param timeBlock2 the time block2
     * 
     * @return the int
     */
    public static int compareEndtoStartTimes(TimeBlock timeBlock1, 
    	TimeBlock timeBlock2)
    {
    	if(timeBlock1.endHour < timeBlock2.startHour)
    	{
    		return -1;
    	}
    	if(timeBlock1.endHour > timeBlock2.startHour)
    	{
    		return 1;
    	}
    	if(timeBlock1.endMinute < timeBlock2.startMinute)
    	{
    		return -1;
    	}
    	if(timeBlock1.endMinute > timeBlock2.startMinute)
    	{
    		return 1;
    	}
    	if(timeBlock1.endSecond < timeBlock2.startSecond)
    	{
    		return -1;
    	}
    	if(timeBlock1.endSecond > timeBlock2.startSecond)
    	{
    		return 1;
    	}
    	return 0;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(TimeBlock o)
    {
    	if(startHour < o.startHour)
    	{
    		return -1;
    	}
    	if(startHour > o.startHour)
    	{
    		return 1;
    	}
    	if(startMinute < o.startMinute)
    	{
    		return -1;
    	}
    	if(startMinute > o.startMinute)
    	{
    		return 1;
    	}
    	return 0;
    	
    }
}
