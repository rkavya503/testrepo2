/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule_SOAPBuilder.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * The Class PriceSchedule_SOAPBuilder.
 */
public class PriceSchedule_SOAPBuilder implements SOAPInstanceBuilder {
    
    /** The _instance. */
    private com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule _instance;
    
    /** The current price dpkwh. */
    private double currentPriceDPKWH;
    
    /** The entries. */
    private com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[] entries;
    
    /** The Constant myCURRENTPRICEDPKWH_INDEX. */
    private static final int myCURRENTPRICEDPKWH_INDEX = 0;
    
    /** The Constant myENTRIES_INDEX. */
    private static final int myENTRIES_INDEX = 1;
    
    /**
     * Instantiates a new price schedule_ soap builder.
     */
    public PriceSchedule_SOAPBuilder() {
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
     * Sets the entries.
     * 
     * @param entries the new entries
     */
    public void setEntries(com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[] entries) {
        this.entries = entries;
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#memberGateType(int)
     */
    public int memberGateType(int memberIndex) {
        switch (memberIndex) {
            case myENTRIES_INDEX:
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
                case myENTRIES_INDEX:
                    _instance.setEntries((com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[])memberValue);
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
        _instance = (com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule)instance;
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#getInstance()
     */
    public java.lang.Object getInstance() {
        return _instance;
    }
}
