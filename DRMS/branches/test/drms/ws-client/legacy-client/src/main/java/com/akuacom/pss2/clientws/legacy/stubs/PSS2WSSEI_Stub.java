/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_Stub.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;

import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.soap.streaming.*;
import com.sun.xml.rpc.soap.message.*;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;

import com.sun.xml.rpc.client.SenderException;
import com.sun.xml.rpc.client.*;
import com.sun.xml.rpc.client.http.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.JAXRPCException;

/**
 * The Class PSS2WSSEI_Stub.
 */
public class PSS2WSSEI_Stub
    extends com.sun.xml.rpc.client.StubBase
    implements com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI {
    
    
    
    /*
     *  public constructor
     */
    /**
     * Instantiates a new pS s2 wsse i_ stub.
     * 
     * @param handlerChain the handler chain
     */
    public PSS2WSSEI_Stub(HandlerChain handlerChain) {
        super(handlerChain);
        _setProperty(ENDPOINT_ADDRESS_PROPERTY, "http://IRO:8080/PSS2WS/PSS2WS");
    }
    
    
    /*
     *  implementation of isAPEEventPending
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI#isAPEEventPending()
     */
    public boolean isAPEEventPending()
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(isAPEEventPending_OPCODE);
            com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_RequestStruct _myPSS2WSSEI_isAPEEventPending_RequestStruct =
                new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_RequestStruct();
            
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_isAPEEventPending_isAPEEventPending_QNAME);
            _bodyBlock.setValue(_myPSS2WSSEI_isAPEEventPending_RequestStruct);
            _bodyBlock.setSerializer(ns1_myPSS2WSSEI_isAPEEventPending_RequestStruct_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct _myPSS2WSSEI_isAPEEventPending_ResponseStruct = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myPSS2WSSEI_isAPEEventPending_ResponseStruct =
                    (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myPSS2WSSEI_isAPEEventPending_ResponseStruct =
                    (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct)_responseObj;
            }
            
            return _myPSS2WSSEI_isAPEEventPending_ResponseStruct.isResult();
        } catch (RemoteException e) {
            // let this one through unchanged
            throw e;
        } catch (JAXRPCException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RemoteException(e.getMessage(), e);
            }
        }
    }
    
    /*
     *  implementation of getPriceSchedule
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI#getPriceSchedule(com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule)
     */
    public com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule getPriceSchedule(com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule priceSchedule_1)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(getPriceSchedule_OPCODE);
            com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct _myPSS2WSSEI_getPriceSchedule_RequestStruct =
                new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct();
            
            _myPSS2WSSEI_getPriceSchedule_RequestStruct.setPriceSchedule_1(priceSchedule_1);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_getPriceSchedule_getPriceSchedule_QNAME);
            _bodyBlock.setValue(_myPSS2WSSEI_getPriceSchedule_RequestStruct);
            _bodyBlock.setSerializer(ns1_myPSS2WSSEI_getPriceSchedule_RequestStruct_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_ResponseStruct _myPSS2WSSEI_getPriceSchedule_ResponseStruct = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myPSS2WSSEI_getPriceSchedule_ResponseStruct =
                    (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_ResponseStruct)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myPSS2WSSEI_getPriceSchedule_ResponseStruct =
                    (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_ResponseStruct)_responseObj;
            }
            
            return _myPSS2WSSEI_getPriceSchedule_ResponseStruct.getResult();
        } catch (RemoteException e) {
            // let this one through unchanged
            throw e;
        } catch (JAXRPCException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RemoteException(e.getMessage(), e);
            }
        }
    }
    
    /*
     *  implementation of getPrice
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI#getPrice(double)
     */
    public double getPrice(double double_1)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(getPrice_OPCODE);
            com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct _myPSS2WSSEI_getPrice_RequestStruct =
                new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct();
            
            _myPSS2WSSEI_getPrice_RequestStruct.setDouble_1(double_1);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_getPrice_getPrice_QNAME);
            _bodyBlock.setValue(_myPSS2WSSEI_getPrice_RequestStruct);
            _bodyBlock.setSerializer(ns1_myPSS2WSSEI_getPrice_RequestStruct_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_ResponseStruct _myPSS2WSSEI_getPrice_ResponseStruct = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myPSS2WSSEI_getPrice_ResponseStruct =
                    (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_ResponseStruct)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myPSS2WSSEI_getPrice_ResponseStruct =
                    (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_ResponseStruct)_responseObj;
            }
            
            return _myPSS2WSSEI_getPrice_ResponseStruct.getResult();
        } catch (RemoteException e) {
            // let this one through unchanged
            throw e;
        } catch (JAXRPCException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RemoteException(e.getMessage(), e);
            }
        }
    }
    
    
    /*
     *  this method deserializes the request/response structure in the body
     */
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.StreamingSender#_readFirstBodyElement(com.sun.xml.rpc.streaming.XMLReader, com.sun.xml.rpc.encoding.SOAPDeserializationContext, com.sun.xml.rpc.client.StreamingSenderState)
     */
    protected void _readFirstBodyElement(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState  state) throws Exception {
        int opcode = state.getRequest().getOperationCode();
        switch (opcode) {
            case isAPEEventPending_OPCODE:
                _deserialize_isAPEEventPending(bodyReader, deserializationContext, state);
                break;
            case getPriceSchedule_OPCODE:
                _deserialize_getPriceSchedule(bodyReader, deserializationContext, state);
                break;
            case getPrice_OPCODE:
                _deserialize_getPrice(bodyReader, deserializationContext, state);
                break;
            default:
                throw new SenderException("sender.response.unrecognizedOperation", java.lang.Integer.toString(opcode));
        }
    }
    
    
    
    /*
     * This method deserializes the body of the isAPEEventPending operation.
     */
    /**
     * _deserialize_is ape event pending.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_isAPEEventPending(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myPSS2WSSEI_isAPEEventPending_ResponseStructObj =
            ns1_myPSS2WSSEI_isAPEEventPending_ResponseStruct_SOAPSerializer.deserialize(ns1_isAPEEventPending_isAPEEventPendingResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_isAPEEventPending_isAPEEventPendingResponse_QNAME);
        bodyBlock.setValue(myPSS2WSSEI_isAPEEventPending_ResponseStructObj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the getPriceSchedule operation.
     */
    /**
     * _deserialize_get price schedule.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_getPriceSchedule(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myPSS2WSSEI_getPriceSchedule_ResponseStructObj =
            ns1_myPSS2WSSEI_getPriceSchedule_ResponseStruct_SOAPSerializer.deserialize(ns1_getPriceSchedule_getPriceScheduleResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_getPriceSchedule_getPriceScheduleResponse_QNAME);
        bodyBlock.setValue(myPSS2WSSEI_getPriceSchedule_ResponseStructObj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the getPrice operation.
     */
    /**
     * _deserialize_get price.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_getPrice(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myPSS2WSSEI_getPrice_ResponseStructObj =
            ns1_myPSS2WSSEI_getPrice_ResponseStruct_SOAPSerializer.deserialize(ns1_getPrice_getPriceResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_getPrice_getPriceResponse_QNAME);
        bodyBlock.setValue(myPSS2WSSEI_getPrice_ResponseStructObj);
        state.getResponse().setBody(bodyBlock);
    }
    
    
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.StreamingSender#_getDefaultEnvelopeEncodingStyle()
     */
    public java.lang.String _getDefaultEnvelopeEncodingStyle() {
        return SOAPNamespaceConstants.ENCODING;
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.StreamingSender#_getImplicitEnvelopeEncodingStyle()
     */
    public java.lang.String _getImplicitEnvelopeEncodingStyle() {
        return "";
    }
    
    /**
     * _get encoding style.
     * 
     * @return the java.lang. string
     */
    public java.lang.String _getEncodingStyle() {
        return SOAPNamespaceConstants.ENCODING;
    }
    
    /**
     * _set encoding style.
     * 
     * @param encodingStyle the encoding style
     */
    public void _setEncodingStyle(java.lang.String encodingStyle) {
        throw new UnsupportedOperationException("cannot set encoding style");
    }
    
    
    
    
    
    /*
     * This method returns an array containing (prefix, nsURI) pairs.
     */
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.StreamingSender#_getNamespaceDeclarations()
     */
    protected java.lang.String[] _getNamespaceDeclarations() {
        return myNamespace_declarations;
    }
    
    /*
     * This method returns an array containing the names of the headers we understand.
     */
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.StreamingSender#_getUnderstoodHeaders()
     */
    public javax.xml.namespace.QName[] _getUnderstoodHeaders() {
        return understoodHeaderNames;
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.client.StubBase#_initialize(com.sun.xml.rpc.encoding.InternalTypeMappingRegistry)
     */
    public void _initialize(InternalTypeMappingRegistry registry) throws Exception {
        super._initialize(registry);
        ns1_myPSS2WSSEI_getPriceSchedule_RequestStruct_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct.class, ns1_getPriceSchedule_TYPE_QNAME);
        ns1_myPSS2WSSEI_isAPEEventPending_ResponseStruct_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct.class, ns1_isAPEEventPendingResponse_TYPE_QNAME);
        ns1_myPSS2WSSEI_getPrice_RequestStruct_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct.class, ns1_getPrice_TYPE_QNAME);
        ns1_myPSS2WSSEI_isAPEEventPending_RequestStruct_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_RequestStruct.class, ns1_isAPEEventPending_TYPE_QNAME);
        ns1_myPSS2WSSEI_getPriceSchedule_ResponseStruct_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_ResponseStruct.class, ns1_getPriceScheduleResponse_TYPE_QNAME);
        ns1_myPSS2WSSEI_getPrice_ResponseStruct_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_ResponseStruct.class, ns1_getPriceResponse_TYPE_QNAME);
    }
    
    /** The Constant _portName. */
    private static final javax.xml.namespace.QName _portName = new QName("http://pss2.lbl.gov", "PSS2WSSEIPort");
    
    /** The Constant isAPEEventPending_OPCODE. */
    private static final int isAPEEventPending_OPCODE = 0;
    
    /** The Constant getPriceSchedule_OPCODE. */
    private static final int getPriceSchedule_OPCODE = 1;
    
    /** The Constant getPrice_OPCODE. */
    private static final int getPrice_OPCODE = 2;
    
    /** The Constant ns1_isAPEEventPending_isAPEEventPending_QNAME. */
    private static final javax.xml.namespace.QName ns1_isAPEEventPending_isAPEEventPending_QNAME = new QName("http://pss2.lbl.gov", "isAPEEventPending");
    
    /** The Constant ns1_isAPEEventPending_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_isAPEEventPending_TYPE_QNAME = new QName("http://pss2.lbl.gov", "isAPEEventPending");
    
    /** The ns1_my ps s2 wsse i_is ape event pending_ request struct_ soap serializer. */
    private CombinedSerializer ns1_myPSS2WSSEI_isAPEEventPending_RequestStruct_SOAPSerializer;
    
    /** The Constant ns1_isAPEEventPending_isAPEEventPendingResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_isAPEEventPending_isAPEEventPendingResponse_QNAME = new QName("http://pss2.lbl.gov", "isAPEEventPendingResponse");
    
    /** The Constant ns1_isAPEEventPendingResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_isAPEEventPendingResponse_TYPE_QNAME = new QName("http://pss2.lbl.gov", "isAPEEventPendingResponse");
    
    /** The ns1_my ps s2 wsse i_is ape event pending_ response struct_ soap serializer. */
    private CombinedSerializer ns1_myPSS2WSSEI_isAPEEventPending_ResponseStruct_SOAPSerializer;
    
    /** The Constant ns1_getPriceSchedule_getPriceSchedule_QNAME. */
    private static final javax.xml.namespace.QName ns1_getPriceSchedule_getPriceSchedule_QNAME = new QName("http://pss2.lbl.gov", "getPriceSchedule");
    
    /** The Constant ns1_getPriceSchedule_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getPriceSchedule_TYPE_QNAME = new QName("http://pss2.lbl.gov", "getPriceSchedule");
    
    /** The ns1_my ps s2 wsse i_get price schedule_ request struct_ soap serializer. */
    private CombinedSerializer ns1_myPSS2WSSEI_getPriceSchedule_RequestStruct_SOAPSerializer;
    
    /** The Constant ns1_getPriceSchedule_getPriceScheduleResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_getPriceSchedule_getPriceScheduleResponse_QNAME = new QName("http://pss2.lbl.gov", "getPriceScheduleResponse");
    
    /** The Constant ns1_getPriceScheduleResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getPriceScheduleResponse_TYPE_QNAME = new QName("http://pss2.lbl.gov", "getPriceScheduleResponse");
    
    /** The ns1_my ps s2 wsse i_get price schedule_ response struct_ soap serializer. */
    private CombinedSerializer ns1_myPSS2WSSEI_getPriceSchedule_ResponseStruct_SOAPSerializer;
    
    /** The Constant ns1_getPrice_getPrice_QNAME. */
    private static final javax.xml.namespace.QName ns1_getPrice_getPrice_QNAME = new QName("http://pss2.lbl.gov", "getPrice");
    
    /** The Constant ns1_getPrice_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getPrice_TYPE_QNAME = new QName("http://pss2.lbl.gov", "getPrice");
    
    /** The ns1_my ps s2 wsse i_get price_ request struct_ soap serializer. */
    private CombinedSerializer ns1_myPSS2WSSEI_getPrice_RequestStruct_SOAPSerializer;
    
    /** The Constant ns1_getPrice_getPriceResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_getPrice_getPriceResponse_QNAME = new QName("http://pss2.lbl.gov", "getPriceResponse");
    
    /** The Constant ns1_getPriceResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getPriceResponse_TYPE_QNAME = new QName("http://pss2.lbl.gov", "getPriceResponse");
    
    /** The ns1_my ps s2 wsse i_get price_ response struct_ soap serializer. */
    private CombinedSerializer ns1_myPSS2WSSEI_getPrice_ResponseStruct_SOAPSerializer;
    
    /** The Constant myNamespace_declarations. */
    private static final java.lang.String[] myNamespace_declarations =
                                        new java.lang.String[] {
                                            "ns0", "http://pss2.lbl.gov"
                                        };
    
    /** The Constant understoodHeaderNames. */
    private static final QName[] understoodHeaderNames = new QName[] {  };
}
