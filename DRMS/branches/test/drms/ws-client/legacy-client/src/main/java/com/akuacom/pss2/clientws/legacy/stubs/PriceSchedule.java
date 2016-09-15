/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;


/**
 * The Class PriceSchedule.
 */
public class PriceSchedule {
    
    /** The current price dpkwh. */
    protected double currentPriceDPKWH;
    
    /** The entries. */
    protected com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[] entries;
    
    /**
     * Instantiates a new price schedule.
     */
    public PriceSchedule() {
    }
    
    /**
     * Instantiates a new price schedule.
     * 
     * @param currentPriceDPKWH the current price dpkwh
     * @param entries the entries
     */
    public PriceSchedule(double currentPriceDPKWH, com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[] entries) {
        this.currentPriceDPKWH = currentPriceDPKWH;
        this.entries = entries;
    }
    
    /**
     * Gets the current price dpkwh.
     * 
     * @return the current price dpkwh
     */
    public double getCurrentPriceDPKWH() {
        return currentPriceDPKWH;
    }
    
    /**
     * Sets the current price dpkwh.
     * 
     * @param currentPriceDPKWH the new current price dpkwh
     */
    public void setCurrentPriceDPKWH(double currentPriceDPKWH) {
        this.currentPriceDPKWH = currentPriceDPKWH;
    }
    
    /**
     * Gets the entries.
     * 
     * @return the entries
     */
    public com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[] getEntries() {
        return entries;
    }
    
    /**
     * Sets the entries.
     * 
     * @param entries the new entries
     */
    public void setEntries(com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[] entries) {
        this.entries = entries;
    }
}
