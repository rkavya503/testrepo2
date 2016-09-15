/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.PriceSchedule.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The Class PriceSchedule.
 */
public class PriceSchedule implements Serializable
{
    
    /** The current price dpkwh. */
    private double currentPriceDPKWH;
    
    /** The entries. */
    private PriceScheduleEntry[] entries;
    
    /**
     * Instantiates a new price schedule.
     */
    public PriceSchedule()
    {
    }

    /**
     * Instantiates a new price schedule.
     * 
     * @param theCurrentPriceDPKWH the the current price dpkwh
     * @param theEntries the the entries
     */
    public PriceSchedule(double theCurrentPriceDPKWH,
      PriceScheduleEntry[] theEntries)
    {
        currentPriceDPKWH = theCurrentPriceDPKWH;
        entries = theEntries;        
    }

    /**
     * Sets the current price dpkwh.
     * 
     * @param currentPriceDPKWH the new current price dpkwh
     */
    public void setCurrentPriceDPKWH(double currentPriceDPKWH)
    {
        this.currentPriceDPKWH = currentPriceDPKWH;
    }

    /**
     * Gets the current price dpkwh.
     * 
     * @return the current price dpkwh
     */
    public double getCurrentPriceDPKWH()
    {
        return currentPriceDPKWH;
    }

    /**
     * Gets the entries.
     * 
     * @return the entries
     */
    public PriceScheduleEntry[] getEntries()
    {
        return entries;
    }

    /**
     * Sets the entries.
     * 
     * @param entries the new entries
     */
    public void setEntries(PriceScheduleEntry[] entries)
    {
        this.entries = entries;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String s = "Current Price: $" + currentPriceDPKWH + "/kWh";
        for(int i = 0; i < entries.length; i++)
        {
            PriceScheduleEntry entry = entries[i];            
            s += "\nTime: " + DateFormat.getDateTimeInstance(DateFormat.SHORT,
              DateFormat.MEDIUM).format(entry.getDate()) + 
              ", Price: $" + entry.getPriceDPKWH() + "/kWh   ";
        }
        return s;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object otherObject)
    {
        if(this == otherObject)
        {
            return true;
        }
        
        if(otherObject == null || getClass() != otherObject.getClass())
        {
            return false;
        }
        
        PriceSchedule other = (PriceSchedule)otherObject;
        if(currentPriceDPKWH != other.currentPriceDPKWH)
        {
            return false;
        }
        if(entries.length != other.entries.length)
        {
            return false;
        }
        for(int i = 0; i < entries.length; i++)
        {
            if(!entries[i].equals(other.entries[i]))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets the test price schedule.
     * 
     * @return the test price schedule
     */
    public static PriceSchedule getTestPriceSchedule()
    {
        PriceScheduleEntry[] entries = null;
        
        double currPrice;
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        int min = cal1.get(GregorianCalendar.MINUTE);
        int z = min % 3;
        switch(z)
        {
        case 0:
            currPrice = 1.0;
            cal1.set( GregorianCalendar.MINUTE, min + 1);
            cal1.set( GregorianCalendar.SECOND, 0);
            entries = new PriceScheduleEntry[1];
            entries[0] = new PriceScheduleEntry(cal1.getTime(), 3.0);
            break;
        case 1:
            currPrice = 3.0;
            cal1.set( GregorianCalendar.MINUTE, min + 1);
            cal1.set( GregorianCalendar.SECOND, 0);
            entries = new PriceScheduleEntry[2];
            entries[0] = new PriceScheduleEntry(cal1.getTime(), 5.0);
            cal2.setTime(cal1.getTime());
            cal2.set(GregorianCalendar.SECOND, 30);
            entries[1] = new PriceScheduleEntry(cal2.getTime(), 3.0);
            break;
        default:
            if(cal1.get(GregorianCalendar.SECOND) < 30)
            {
                currPrice = 5.0;
                cal1.set( GregorianCalendar.SECOND, 30);
                entries = new PriceScheduleEntry[2];
                entries[0] = new PriceScheduleEntry(cal1.getTime(), 3.0);
                cal2.setTime(cal1.getTime());
                cal2.set( GregorianCalendar.MINUTE, min + 1);
                cal2.set( GregorianCalendar.SECOND, 0);
                entries[1] = new PriceScheduleEntry(cal2.getTime(), 1.0);
            }
            else
            {
                currPrice = 3.0;
                cal1.set( GregorianCalendar.MINUTE, min + 1);
                cal1.set( GregorianCalendar.SECOND, 0);                
                entries = new PriceScheduleEntry[1];
                entries[0] = new PriceScheduleEntry(cal1.getTime(), 1.0);
            }
            break;
        }
        PriceSchedule ps = new PriceSchedule();
        ps.setCurrentPriceDPKWH(currPrice);
        ps.setEntries(entries);
        return ps;
    }
    
    /*
     * This is a hardcoded test channel for Federspiel
     * The channel name must be customtest1
	 * when DRAS starts (time = t),      the price = 1.0 (level = normal)
       After t+ 60 minutes,              the price = 3.0 (level = moderate)
       After t+120 minutes               the price = 5.0 (level = high)
       After t+180 minutes               the price = 1.0 (level= normal)   
       Set  t = t+180 minutes, repeat above steps in same order
     */
    
    /** The SIXT y_ minute s_ ms. */
    private static long SIXTY_MINUTES_MS = 60*60*1000; //60 min in msecs
    //private static long SIXTY_MINUTES_MS = 3*60*1000; //for testing every 3 minute
    
    //had to make this global since we need old ones between changes
    /** The prev price. */
    private static double prevPrice = 0.0; //start condition
    
    /** The curr price. */
    private static double currPrice = 1.0;
    
    /** The test entries. */
    private static PriceScheduleEntry[] testEntries = null;
    
    /** The start time. */
    private static Date   startTime = null;//start condition
    
    /**
     * Gets the custom test1 price schedule.
     * 
     * @return the custom test1 price schedule
     */
    public static PriceSchedule getCustomTest1PriceSchedule()
    {
        //PriceScheduleEntry[] entries = null;
        //double currPrice;
        int index = (int) prevPrice;
        Date now = new Date();
        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTime(now);
        
        switch (index)
        {
	        case 0:	//start condition
	        	if (startTime == null)
	        	{
	        		startTime = now;
	        		currPrice = 1.0;
	                prevPrice = currPrice;
//	                System.out.println("getCustomTest1PriceSchedule: start:currPrice= "+currPrice);

	                int hr = cal1.get(GregorianCalendar.HOUR_OF_DAY);
	                testEntries = new PriceScheduleEntry[2];
	                cal1.set(GregorianCalendar.HOUR_OF_DAY, hr+1);
	                testEntries[0] = new PriceScheduleEntry(cal1.getTime(), 3.0);
	                cal1.set(GregorianCalendar.HOUR_OF_DAY, hr+2);
	                testEntries[1] = new PriceScheduleEntry(cal1.getTime(), 5.0);	                
//	                testEntries[2] = new PriceScheduleEntry(startTime, currPrice);
	        	}
	        break;
	            
	        case 1:
	        	if ( (now.getTime() - startTime.getTime())
	        				>= SIXTY_MINUTES_MS)
	        	{
	        		startTime = now;
	        		currPrice = 3.0;
	                prevPrice = currPrice;
//	                System.out.println("getCustomTest1PriceSchedule:1 currPrice= "+currPrice);

	                int hr = cal1.get(GregorianCalendar.HOUR_OF_DAY);
	                testEntries = new PriceScheduleEntry[2];
	                cal1.set(GregorianCalendar.HOUR_OF_DAY, hr+1);
	                testEntries[0] = new PriceScheduleEntry(cal1.getTime(), 5.0);
	                cal1.set(GregorianCalendar.HOUR_OF_DAY, hr+2);
	                testEntries[1] = new PriceScheduleEntry(cal1.getTime(), 1.0);	                
//	                testEntries[2] = new PriceScheduleEntry(startTime, currPrice);
	        	}
	        break;
	        
	        case 3:
	        	if ( (now.getTime() - startTime.getTime())
        				>= SIXTY_MINUTES_MS)
	        	{
	        		startTime = now;
	        		currPrice = 5.0;
	                prevPrice = currPrice;
//	                System.out.println("getCustomTest1PriceSchedule:3 currPrice= "+currPrice);
	                int hr = cal1.get(GregorianCalendar.HOUR_OF_DAY);
	                testEntries = new PriceScheduleEntry[2];
	                cal1.set(GregorianCalendar.HOUR_OF_DAY, hr+1);
	                testEntries[0] = new PriceScheduleEntry(cal1.getTime(), 1.0);
	                cal1.set(GregorianCalendar.HOUR_OF_DAY, hr+2);
	                testEntries[1] = new PriceScheduleEntry(cal1.getTime(), 3.0);	                
//	                testEntries[2] = new PriceScheduleEntry(startTime, currPrice);
	        	}
	        break;
        
	        case 5:
	        	if ( (now.getTime() - startTime.getTime())
        				>= SIXTY_MINUTES_MS)
	        	{
	        		startTime = now;
	        		currPrice = 1.0;
	                prevPrice = currPrice;
//	                System.out.println("getCustomTest1PriceSchedule:5 currPrice= "+currPrice);
	                int hr = cal1.get(GregorianCalendar.HOUR_OF_DAY);
	                testEntries = new PriceScheduleEntry[2];
	                cal1.set(GregorianCalendar.HOUR_OF_DAY, hr+1);
	                testEntries[0] = new PriceScheduleEntry(cal1.getTime(), 3.0);
	                cal1.set(GregorianCalendar.HOUR_OF_DAY, hr+2);
	                testEntries[1] = new PriceScheduleEntry(cal1.getTime(), 5.0);	                
//	                testEntries[2] = new PriceScheduleEntry(startTime, currPrice);
	        	}
	        break;
	        
	        default:
	        	currPrice = 1.0;
	        break;
        }
        
//        System.out.println("getCustomTest1PriceSchedule:currPrice= "+currPrice);
        PriceSchedule ps = new PriceSchedule();
        ps.setCurrentPriceDPKWH(currPrice);
        ps.setEntries(testEntries);
        return ps;
    }
}