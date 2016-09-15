/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_ResponseStruct.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;


/**
 * The Class PSS2WSSEI_getPriceSchedule_ResponseStruct.
 */
public class PSS2WSSEI_getPriceSchedule_ResponseStruct {
    
    /** The result. */
    protected com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule result;
    
    /**
     * Instantiates a new pS s2 wsse i_get price schedule_ response struct.
     */
    public PSS2WSSEI_getPriceSchedule_ResponseStruct() {
    }
    
    /**
     * Instantiates a new pS s2 wsse i_get price schedule_ response struct.
     * 
     * @param result the result
     */
    public PSS2WSSEI_getPriceSchedule_ResponseStruct(com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule result) {
        this.result = result;
    }
    
    /**
     * Gets the result.
     * 
     * @return the result
     */
    public com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule getResult() {
        return result;
    }
    
    /**
     * Sets the result.
     * 
     * @param result the new result
     */
    public void setResult(com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule result) {
        this.result = result;
    }
}
