/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.program.ChartVO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.program;

import java.util.Date;
import java.util.Map;

/**
 * The Class ChartVO.
 */
public class ChartVO {
    
    /** The time. */
    private Date time;
    
    /** The usage. */
    private double usage;
    
    /** The usage2. */
    private double usage2;
    
    /** The base. */
    private double base;
    
    /** The base2. */
    private double base2;
    
    /** The level. */
    private double level;

    /** The device data. */
    private Map deviceData;

    /**
     * Gets the time.
     * 
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Sets the time.
     * 
     * @param time the new time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Gets the usage.
     * 
     * @return the usage
     */
    public double getUsage() {
        return usage;
    }

    /**
     * Sets the usage.
     * 
     * @param usage the new usage
     */
    public void setUsage(double usage) {
        this.usage = usage;
    }

    /**
     * Gets the usage2.
     * 
     * @return the usage2
     */
    public double getUsage2() {
        return usage2;
    }

    /**
     * Sets the usage2.
     * 
     * @param usage2 the new usage2
     */
    public void setUsage2(double usage2) {
        this.usage2 = usage2;
    }

    /**
     * Gets the base.
     * 
     * @return the base
     */
    public double getBase() {
        return base;
    }

    /**
     * Sets the base.
     * 
     * @param base the new base
     */
    public void setBase(double base) {
        this.base = base;
    }

    /**
     * Gets the base2.
     * 
     * @return the base2
     */
    public double getBase2() {
        return base2;
    }

    /**
     * Sets the base2.
     * 
     * @param base2 the new base2
     */
    public void setBase2(double base2) {
        this.base2 = base2;
    }

    /**
     * Gets the level.
     * 
     * @return the level
     */
    public double getLevel() {
        return level;
    }

    /**
     * Sets the level.
     * 
     * @param level the new level
     */
    public void setLevel(double level) {
        this.level = level;
    }

    /**
     * Gets the device data.
     * 
     * @return the device data
     */
    public Map getDeviceData() {
        return deviceData;
    }

    /**
     * Sets the device data.
     * 
     * @param deviceData the new device data
     */
    public void setDeviceData(Map deviceData) {
        this.deviceData = deviceData;
    }
}
