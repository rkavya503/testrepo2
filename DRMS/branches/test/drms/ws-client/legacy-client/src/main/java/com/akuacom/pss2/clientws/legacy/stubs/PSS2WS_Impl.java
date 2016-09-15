/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WS_Impl.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;

import com.sun.xml.rpc.client.ServiceExceptionImpl;
import com.sun.xml.rpc.util.exception.*;
import com.sun.xml.rpc.client.HandlerChainImpl;
import javax.xml.rpc.*;
import javax.xml.namespace.QName;

/**
 * The Class PSS2WS_Impl.
 */
public class PSS2WS_Impl extends com.sun.xml.rpc.client.BasicService implements PSS2WS {
    
    /** The Constant serviceName. */
    private static final QName serviceName = new QName("http://pss2.lbl.gov", "PSS2WS");
    
    /** The Constant ns1_PSS2WSSEIPort_QNAME. */
    private static final QName ns1_PSS2WSSEIPort_QNAME = new QName("http://pss2.lbl.gov", "PSS2WSSEIPort");
    
    /** The Constant PSS2WSSEI_PortClass. */
    private static final Class PSS2WSSEI_PortClass = com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI.class;
    
    /**
     * Instantiates a new pS s2 w s_ impl.
     */
    public PSS2WS_Impl() {
        super(serviceName, new QName[] {
                        ns1_PSS2WSSEIPort_QNAME
                    },
            new com.akuacom.pss2.clientws.legacy.stubs.PSS2WS_SerializerRegistry().getRegistry());
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.BasicService#getPort(javax.xml.namespace.QName, java.lang.Class)
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, java.lang.Class serviceDefInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (portName.equals(ns1_PSS2WSSEIPort_QNAME) &&
                serviceDefInterface.equals(PSS2WSSEI_PortClass)) {
                return getPSS2WSSEIPort();
            }
        } catch (Exception e) {
            throw new ServiceExceptionImpl(new LocalizableExceptionAdapter(e));
        }
        return super.getPort(portName, serviceDefInterface);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.BasicService#getPort(java.lang.Class)
     */
    public java.rmi.Remote getPort(java.lang.Class serviceDefInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (serviceDefInterface.equals(PSS2WSSEI_PortClass)) {
                return getPSS2WSSEIPort();
            }
        } catch (Exception e) {
            throw new ServiceExceptionImpl(new LocalizableExceptionAdapter(e));
        }
        return super.getPort(serviceDefInterface);
    }
    
    /* (non-Javadoc)
     * @see com.akuacom.pss2.clientws.legacy.stubs.PSS2WS#getPSS2WSSEIPort()
     */
    public com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI getPSS2WSSEIPort() {
        java.lang.String[] roles = new java.lang.String[] {};
        HandlerChainImpl handlerChain = new HandlerChainImpl(getHandlerRegistry().getHandlerChain(ns1_PSS2WSSEIPort_QNAME));
        handlerChain.setRoles(roles);
        com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_Stub stub = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_Stub(handlerChain);
        try {
            stub._initialize(super.internalTypeRegistry);
        } catch (JAXRPCException e) {
            throw e;
        } catch (Exception e) {
            throw new JAXRPCException(e.getMessage(), e);
        }
        return stub;
    }
}
