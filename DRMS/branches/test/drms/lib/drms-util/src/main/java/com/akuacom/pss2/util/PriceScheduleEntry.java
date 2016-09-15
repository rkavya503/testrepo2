/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.PriceScheduleEntry.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class PriceScheduleEntry.
 */
public class PriceScheduleEntry implements Serializable
{
    
    /** The date. */
    private Date date;
    
    /** The price dpkwh. */
    private double priceDPKWH;
    
    /**
     * Instantiates a new price schedule entry.
     */
    public PriceScheduleEntry()
    {
    }

    /**
     * Instantiates a new price schedule entry.
     * 
     * @param theDate the the date
     * @param thePriceDPKWH the the price dpkwh
     */
    public PriceScheduleEntry(Date theDate, double thePriceDPKWH)
    {
        date = theDate;
        priceDPKWH = thePriceDPKWH;
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Sets the date.
     * 
     * @param date the new date
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Gets the price dpkwh.
     * 
     * @return the price dpkwh
     */
    public double getPriceDPKWH()
    {
        return priceDPKWH;
    }

    /**
     * Sets the price dpkwh.
     * 
     * @param priceDPKWH the new price dpkwh
     */
    public void setPriceDPKWH(double priceDPKWH)
    {
        this.priceDPKWH = priceDPKWH;
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
        
        PriceScheduleEntry other = (PriceScheduleEntry)otherObject;
        if(priceDPKWH != other.priceDPKWH || 
          date.getTime() != other.date.getTime())
        {
            return false;
        }
        
        return true;
    }
}