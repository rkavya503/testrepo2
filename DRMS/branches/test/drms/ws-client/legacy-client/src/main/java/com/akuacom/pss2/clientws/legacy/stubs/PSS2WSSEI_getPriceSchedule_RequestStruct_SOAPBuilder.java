/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct_SOAPBuilder.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

/**
 * The Class PSS2WSSEI_getPriceSchedule_RequestStruct_SOAPBuilder.
 */
public class PSS2WSSEI_getPriceSchedule_RequestStruct_SOAPBuilder implements SOAPInstanceBuilder {
    
    /** The _instance. */
    private com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct _instance;
    
    /** The price schedule_1. */
    private com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule priceSchedule_1;
    
    /** The Constant myPRICESCHEDULE_1_INDEX. */
    private static final int myPRICESCHEDULE_1_INDEX = 0;
    
    /**
     * Instantiates a new pS s2 wsse i_get price schedule_ request struct_ soap builder.
     */
    public PSS2WSSEI_getPriceSchedule_RequestStruct_SOAPBuilder() {
    }
    
    /**
     * Sets the price schedule_1.
     * 
     * @param priceSchedule_1 the new price schedule_1
     */
    public void setPriceSchedule_1(com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule priceSchedule_1) {
        this.priceSchedule_1 = priceSchedule_1;
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#memberGateType(int)
     */
    public int memberGateType(int memberIndex) {
        switch (memberIndex) {
            case myPRICESCHEDULE_1_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#construct()
     */
    public void construct() {
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#setMember(int, java.lang.Object)
     */
    public void setMember(int index, java.lang.Object memberValue) {
        try {
            switch(index) {
                case myPRICESCHEDULE_1_INDEX:
                    _instance.setPriceSchedule_1((com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule)memberValue);
                    break;
                default:
                    throw new java.lang.IllegalArgumentException();
            }
        }
        catch (java.lang.RuntimeException e) {
            throw e;
        }
        catch (java.lang.Exception e) {
            throw new DeserializationException(new LocalizableExceptionAdapter(e));
        }
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#initialize()
     */
    public void initialize() {
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#setInstance(java.lang.Object)
     */
    public void setInstance(java.lang.Object instance) {
        _instance = (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct)instance;
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#getInstance()
     */
    public java.lang.Object getInstance() {
        return _instance;
    }
}
