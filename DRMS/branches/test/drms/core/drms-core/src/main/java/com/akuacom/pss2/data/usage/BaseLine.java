/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.sc.BaseLine.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data.usage;

import java.util.Date;

/**
 * The Class BaseLine.
 */
public class BaseLine extends DailyUsage {

    /** The type. */
    private BaseLineType type;

    /**
     * Instantiates a new base line.
     *
     * @param type the type
     * @param date the date
     * @param slotSize_msec the slot size_msec
     * @param eventDay the event day
     */
    public BaseLine(BaseLineType type, Date date, long slotSize_msec, boolean eventDay) {
        super(date, slotSize_msec, eventDay);
        this.type = type;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public BaseLineType getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(BaseLineType type) {
        this.type = type;
    }
}