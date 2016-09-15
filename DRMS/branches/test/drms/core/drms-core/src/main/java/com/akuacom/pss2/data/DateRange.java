/*
 * www.akuacom.com Automating Demand Response
 *
 * com.akuacom.pss2.partdata.DateRange.java - Copyright 1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.Date;
import java.util.List;
import java.io.Serializable;

/**
 * The Class DateRange.
 */
public class DateRange implements Serializable {

    /** The start time. */
    private Date startTime;

    /** The end time. */
    private Date endTime;

    private List<Date> excludedDays;

    
    @Override
    public boolean equals(Object otherO) {
        if (otherO == null) { return false; }
        if ((otherO instanceof DateRange) == false) { return false; }
        DateRange other = (DateRange)otherO;
        
        Date oStart = other.getStartTime();
        boolean startEqual = false;
        if (startTime == oStart) {  startEqual = true;   } 
        else if (startTime != null && startTime.equals(oStart)) { startEqual = true;}

        Date oEnd = other.getEndTime();     
        boolean endEqual = false;        
        if (endTime == oEnd) { endEqual = true; }
        else if (endTime != null && endTime.equals(oEnd)) { endEqual = true; }            

        List<Date> oExcluded = other.getExcludedDays();
        boolean excludedEqual = false;
        if (excludedDays == oExcluded) { excludedEqual = true; }
        else if (excludedDays != null && excludedDays.equals(oExcluded)) { excludedEqual = true; }
        
        return (startEqual==true) && (endEqual==true) && (excludedEqual==true);
    }
    
    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public Date getStartTime() {
        return startTime;
    }

    public List<Date> getExcludedDays() {
        return excludedDays;
    }

    public void setExcludedDays(List<Date> excludedDays) {
        this.excludedDays = excludedDays;
    }

    /**
     * Sets the start time.
     *
     * @param startTime the new start time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time.
     *
     * @param endTime the new end time
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
	{
		StringBuilder rv = new StringBuilder("DateRange: ");
        rv.append("startTime: " + startTime);
        rv.append(", endTime: " + endTime);
        return rv.toString();
	}
}