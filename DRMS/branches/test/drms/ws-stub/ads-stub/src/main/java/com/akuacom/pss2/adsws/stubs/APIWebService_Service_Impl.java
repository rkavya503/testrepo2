/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.stubs.APIWebService_Service_Impl.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.adsws.stubs;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.client.ServiceExceptionImpl;
import com.sun.xml.rpc.util.exception.*;
import com.sun.xml.rpc.soap.SOAPVersion;
import com.sun.xml.rpc.client.HandlerChainImpl;
import javax.xml.rpc.*;
import javax.xml.rpc.encoding.*;
import javax.xml.rpc.handler.HandlerChain;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.namespace.QName;

/**
 * The Class APIWebService_Service_Impl.
 */
public class APIWebService_Service_Impl extends com.sun.xml.rpc.client.BasicService implements APIWebService_Service {
    
    /** The Constant serviceName. */
    private static final QName serviceName = new QName("urn-com-caiso-ads-webservices-api", "APIWebService");
    
    /** The Constant ns1_APIWebServicePort_QNAME. */
    private static final QName ns1_APIWebServicePort_QNAME = new QName("urn-com-caiso-ads-webservices-api", "APIWebServicePort");
    
    /** The Constant APIWebService_PortType_PortClass. */
    private static final Class APIWebService_PortType_PortClass = com.akuacom.pss2.adsws.stubs.APIWebService_PortType.class;
    
    /**
     * Instantiates a new aPI web service_ service_ impl.
     */
    public APIWebService_Service_Impl() {
        super(serviceName, new QName[] {
                        ns1_APIWebServicePort_QNAME
                    },
            new com.akuacom.pss2.adsws.stubs.APIWebService_Service_SerializerRegistry().getRegistry());
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.BasicService#getPort(javax.xml.namespace.QName, java.lang.Class)
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, java.lang.Class serviceDefInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (portName.equals(ns1_APIWebServicePort_QNAME) &&
                serviceDefInterface.equals(APIWebService_PortType_PortClass)) {
                return getAPIWebServicePort();
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
            if (serviceDefInterface.equals(APIWebService_PortType_PortClass)) {
                return getAPIWebServicePort();
            }
        } catch (Exception e) {
            throw new ServiceExceptionImpl(new LocalizableExceptionAdapter(e));
        }
        return super.getPort(serviceDefInterface);
    }
    
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_Service#getAPIWebServicePort()
     */
    public com.akuacom.pss2.adsws.stubs.APIWebService_PortType getAPIWebServicePort() {
        java.lang.String[] roles = new java.lang.String[] {};
        HandlerChainImpl handlerChain = new HandlerChainImpl(getHandlerRegistry().getHandlerChain(ns1_APIWebServicePort_QNAME));
        handlerChain.setRoles(roles);
        com.akuacom.pss2.adsws.stubs.APIWebService_PortType_Stub stub = new com.akuacom.pss2.adsws.stubs.APIWebService_PortType_Stub(handlerChain);
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
