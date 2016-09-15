/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2_SOAPBuilder.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.adsws.stubs;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

/**
 * The Class APIWebService_submitMSSLFRequest_ResponseStruct2_SOAPBuilder.
 */
public class APIWebService_submitMSSLFRequest_ResponseStruct2_SOAPBuilder implements SOAPInstanceBuilder {
    
    /** The _instance. */
    private com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2 _instance;
    
    /** The result. */
    private java.lang.String result;
    
    /** The Constant myRESULT_INDEX. */
    private static final int myRESULT_INDEX = 0;
    
    /**
     * Instantiates a new aPI web service_submit msslf request_ response struct2_ soap builder.
     */
    public APIWebService_submitMSSLFRequest_ResponseStruct2_SOAPBuilder() {
    }
    
    /**
     * Sets the result.
     * 
     * @param result the new result
     */
    public void setResult(java.lang.String result) {
        this.result = result;
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#memberGateType(int)
     */
    public int memberGateType(int memberIndex) {
        switch (memberIndex) {
            case myRESULT_INDEX:
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
                case myRESULT_INDEX:
                    _instance.setResult((java.lang.String)memberValue);
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
        _instance = (com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2)instance;
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SOAPInstanceBuilder#getInstance()
     */
    public java.lang.Object getInstance() {
        return _instance;
    }
}
