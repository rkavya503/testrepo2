/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;


/**
 * The Class PriceScheduleEntry.
 */
public class PriceScheduleEntry {
    
    /** The date. */
    protected java.util.Calendar date;
    
    /** The price dpkwh. */
    protected double priceDPKWH;
    
    /**
     * Instantiates a new price schedule entry.
     */
    public PriceScheduleEntry() {
    }
    
    /**
     * Instantiates a new price schedule entry.
     * 
     * @param date the date
     * @param priceDPKWH the price dpkwh
     */
    public PriceScheduleEntry(java.util.Calendar date, double priceDPKWH) {
        this.date = date;
        this.priceDPKWH = priceDPKWH;
    }
    
    /**
     * Gets the date.
     * 
     * @return the date
     */
    public java.util.Calendar getDate() {
        return date;
    }
    
    /**
     * Sets the date.
     * 
     * @param date the new date
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }
    
    /**
     * Gets the price dpkwh.
     * 
     * @return the price dpkwh
     */
    public double getPriceDPKWH() {
        return priceDPKWH;
    }
    
    /**
     * Sets the price dpkwh.
     * 
     * @param priceDPKWH the new price dpkwh
     */
    public void setPriceDPKWH(double priceDPKWH) {
        this.priceDPKWH = priceDPKWH;
    }
}
