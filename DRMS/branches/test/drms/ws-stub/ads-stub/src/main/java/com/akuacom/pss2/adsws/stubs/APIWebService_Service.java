/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.stubs.APIWebService_Service.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.adsws.stubs;

import javax.xml.rpc.*;

/**
 * The Interface APIWebService_Service.
 */
public interface APIWebService_Service extends javax.xml.rpc.Service {
    
    /**
     * Gets the aPI web service port.
     * 
     * @return the aPI web service port
     * 
     * @throws ServiceException the service exception
     */
    public com.akuacom.pss2.adsws.stubs.APIWebService_PortType getAPIWebServicePort() throws ServiceException;
}
