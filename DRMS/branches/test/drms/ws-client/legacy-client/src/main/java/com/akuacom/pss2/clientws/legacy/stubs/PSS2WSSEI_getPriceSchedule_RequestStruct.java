/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;


/**
 * The Class PSS2WSSEI_getPriceSchedule_RequestStruct.
 */
public class PSS2WSSEI_getPriceSchedule_RequestStruct {
    
    /** The price schedule_1. */
    protected com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule priceSchedule_1;
    
    /**
     * Instantiates a new pS s2 wsse i_get price schedule_ request struct.
     */
    public PSS2WSSEI_getPriceSchedule_RequestStruct() {
    }
    
    /**
     * Instantiates a new pS s2 wsse i_get price schedule_ request struct.
     * 
     * @param priceSchedule_1 the price schedule_1
     */
    public PSS2WSSEI_getPriceSchedule_RequestStruct(com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule priceSchedule_1) {
        this.priceSchedule_1 = priceSchedule_1;
    }
    
    /**
     * Gets the price schedule_1.
     * 
     * @return the price schedule_1
     */
    public com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule getPriceSchedule_1() {
        return priceSchedule_1;
    }
    
    /**
     * Sets the price schedule_1.
     * 
     * @param priceSchedule_1 the new price schedule_1
     */
    public void setPriceSchedule_1(com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule priceSchedule_1) {
        this.priceSchedule_1 = priceSchedule_1;
    }
}
