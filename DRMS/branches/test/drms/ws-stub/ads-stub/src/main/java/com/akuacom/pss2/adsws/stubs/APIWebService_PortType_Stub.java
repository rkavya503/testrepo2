/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.stubs.APIWebService_PortType_Stub.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.adsws.stubs;

import com.sun.xml.rpc.server.http.MessageContextProperties;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.soap.streaming.*;
import com.sun.xml.rpc.soap.message.*;
import com.sun.xml.rpc.soap.SOAPVersion;
import com.sun.xml.rpc.soap.SOAPEncodingConstants;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.lang.reflect.*;
import java.lang.Class;
import com.sun.xml.rpc.client.SenderException;
import com.sun.xml.rpc.client.*;
import com.sun.xml.rpc.client.http.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.soap.SOAPFaultException;

/**
 * The Class APIWebService_PortType_Stub.
 */
public class APIWebService_PortType_Stub
    extends com.sun.xml.rpc.client.StubBase
    implements com.akuacom.pss2.adsws.stubs.APIWebService_PortType {
    
    
    
    /*
     *  public constructor
     */
    /**
     * Instantiates a new aPI web service_ port type_ stub.
     * 
     * @param handlerChain the handler chain
     */
    public APIWebService_PortType_Stub(HandlerChain handlerChain) {
        super(handlerChain);
        _setProperty(ENDPOINT_ADDRESS_PROPERTY, "http://fpapjbos25:8080/ADS/APIWebService");
    }
    
    
    /*
     *  implementation of getBatchHeader
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_PortType#getBatchHeader(java.lang.String)
     */
    public java.lang.String getBatchHeader(java.lang.String batchUID)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(getBatchHeader_OPCODE);
            com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_RequestStruct2 _myAPIWebService_getBatchHeader_RequestStruct2 =
                new com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_RequestStruct2();
            
            _myAPIWebService_getBatchHeader_RequestStruct2.setBatchUID(batchUID);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_getBatchHeader_getBatchHeader_QNAME);
            _bodyBlock.setValue(_myAPIWebService_getBatchHeader_RequestStruct2);
            _bodyBlock.setSerializer(ns1_myAPIWebService_getBatchHeader_RequestStruct2_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_ResponseStruct2 _myAPIWebService_getBatchHeader_ResponseStruct2 = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myAPIWebService_getBatchHeader_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_ResponseStruct2)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myAPIWebService_getBatchHeader_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_ResponseStruct2)_responseObj;
            }
            
            return _myAPIWebService_getBatchHeader_ResponseStruct2.getResult();
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
     *  implementation of getBatchStatus
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_PortType#getBatchStatus(java.lang.String)
     */
    public int getBatchStatus(java.lang.String batchUID)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(getBatchStatus_OPCODE);
            com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_RequestStruct2 _myAPIWebService_getBatchStatus_RequestStruct2 =
                new com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_RequestStruct2();
            
            _myAPIWebService_getBatchStatus_RequestStruct2.setBatchUID(batchUID);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_getBatchStatus_getBatchStatus_QNAME);
            _bodyBlock.setValue(_myAPIWebService_getBatchStatus_RequestStruct2);
            _bodyBlock.setSerializer(ns1_myAPIWebService_getBatchStatus_RequestStruct2_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_ResponseStruct2 _myAPIWebService_getBatchStatus_ResponseStruct2 = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myAPIWebService_getBatchStatus_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_ResponseStruct2)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myAPIWebService_getBatchStatus_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_ResponseStruct2)_responseObj;
            }
            
            return _myAPIWebService_getBatchStatus_ResponseStruct2.getResult();
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
     *  implementation of getDispatchBatchesSinceUID
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_PortType#getDispatchBatchesSinceUID(java.lang.String)
     */
    public java.lang.String getDispatchBatchesSinceUID(java.lang.String batchUID)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(getDispatchBatchesSinceUID_OPCODE);
            com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_RequestStruct2 _myAPIWebService_getDispatchBatchesSinceUID_RequestStruct2 =
                new com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_RequestStruct2();
            
            _myAPIWebService_getDispatchBatchesSinceUID_RequestStruct2.setBatchUID(batchUID);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_getDispatchBatchesSinceUID_getDispatchBatchesSinceUID_QNAME);
            _bodyBlock.setValue(_myAPIWebService_getDispatchBatchesSinceUID_RequestStruct2);
            _bodyBlock.setSerializer(ns1_myAPIWebService_getDispatchBatchesSinceUID_RequestStruct2_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_ResponseStruct2 _myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2 = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_ResponseStruct2)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_ResponseStruct2)_responseObj;
            }
            
            return _myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2.getResult();
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
     *  implementation of submitMSSLFRequest
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_PortType#submitMSSLFRequest(java.lang.String)
     */
    public java.lang.String submitMSSLFRequest(java.lang.String request)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(submitMSSLFRequest_OPCODE);
            com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2 _myAPIWebService_submitMSSLFRequest_RequestStruct2 =
                new com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2();
            
            _myAPIWebService_submitMSSLFRequest_RequestStruct2.setRequest(request);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_submitMSSLFRequest_submitMSSLFRequest_QNAME);
            _bodyBlock.setValue(_myAPIWebService_submitMSSLFRequest_RequestStruct2);
            _bodyBlock.setSerializer(ns1_myAPIWebService_submitMSSLFRequest_RequestStruct2_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2 _myAPIWebService_submitMSSLFRequest_ResponseStruct2 = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myAPIWebService_submitMSSLFRequest_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myAPIWebService_submitMSSLFRequest_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2)_responseObj;
            }
            
            return _myAPIWebService_submitMSSLFRequest_ResponseStruct2.getResult();
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
     *  implementation of isNewTrajData
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_PortType#isNewTrajData(java.lang.String)
     */
    public boolean isNewTrajData(java.lang.String batchUID)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(isNewTrajData_OPCODE);
            com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_RequestStruct2 _myAPIWebService_isNewTrajData_RequestStruct2 =
                new com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_RequestStruct2();
            
            _myAPIWebService_isNewTrajData_RequestStruct2.setBatchUID(batchUID);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_isNewTrajData_isNewTrajData_QNAME);
            _bodyBlock.setValue(_myAPIWebService_isNewTrajData_RequestStruct2);
            _bodyBlock.setSerializer(ns1_myAPIWebService_isNewTrajData_RequestStruct2_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_ResponseStruct2 _myAPIWebService_isNewTrajData_ResponseStruct2 = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myAPIWebService_isNewTrajData_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_ResponseStruct2)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myAPIWebService_isNewTrajData_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_ResponseStruct2)_responseObj;
            }
            
            return _myAPIWebService_isNewTrajData_ResponseStruct2.isResult();
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
     *  implementation of getDispatchBatch
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_PortType#getDispatchBatch(java.lang.String)
     */
    public java.lang.String getDispatchBatch(java.lang.String batchUID)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(getDispatchBatch_OPCODE);
            com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_RequestStruct2 _myAPIWebService_getDispatchBatch_RequestStruct2 =
                new com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_RequestStruct2();
            
            _myAPIWebService_getDispatchBatch_RequestStruct2.setBatchUID(batchUID);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_getDispatchBatch_getDispatchBatch_QNAME);
            _bodyBlock.setValue(_myAPIWebService_getDispatchBatch_RequestStruct2);
            _bodyBlock.setSerializer(ns1_myAPIWebService_getDispatchBatch_RequestStruct2_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_ResponseStruct2 _myAPIWebService_getDispatchBatch_ResponseStruct2 = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myAPIWebService_getDispatchBatch_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_ResponseStruct2)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myAPIWebService_getDispatchBatch_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_ResponseStruct2)_responseObj;
            }
            
            return _myAPIWebService_getDispatchBatch_ResponseStruct2.getResult();
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
     *  implementation of validateDispatchBatch
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_PortType#validateDispatchBatch(java.lang.String)
     */
    public void validateDispatchBatch(java.lang.String batchUID)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(validateDispatchBatch_OPCODE);
            com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_RequestStruct2 _myAPIWebService_validateDispatchBatch_RequestStruct2 =
                new com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_RequestStruct2();
            
            _myAPIWebService_validateDispatchBatch_RequestStruct2.setBatchUID(batchUID);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_validateDispatchBatch_validateDispatchBatch_QNAME);
            _bodyBlock.setValue(_myAPIWebService_validateDispatchBatch_RequestStruct2);
            _bodyBlock.setSerializer(ns1_myAPIWebService_validateDispatchBatch_RequestStruct2_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2 _myAPIWebService_validateDispatchBatch_ResponseStruct2 = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myAPIWebService_validateDispatchBatch_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myAPIWebService_validateDispatchBatch_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2)_responseObj;
            }
            
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
     *  implementation of getTrajectoryData
     */
    /* (non-Javadoc)
     * @see com.akuacom.pss2.adsws.stubs.APIWebService_PortType#getTrajectoryData(java.lang.String)
     */
    public java.lang.String getTrajectoryData(java.lang.String batchUID)
        throws java.rmi.RemoteException {
        
        try {
            
            StreamingSenderState _state = _start(_handlerChain);
            
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(getTrajectoryData_OPCODE);
            com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_RequestStruct2 _myAPIWebService_getTrajectoryData_RequestStruct2 =
                new com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_RequestStruct2();
            
            _myAPIWebService_getTrajectoryData_RequestStruct2.setBatchUID(batchUID);
            
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_getTrajectoryData_getTrajectoryData_QNAME);
            _bodyBlock.setValue(_myAPIWebService_getTrajectoryData_RequestStruct2);
            _bodyBlock.setSerializer(ns1_myAPIWebService_getTrajectoryData_RequestStruct2_SOAPSerializer);
            _request.setBody(_bodyBlock);
            
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "");
            
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            
            com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_ResponseStruct2 _myAPIWebService_getTrajectoryData_ResponseStruct2 = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _myAPIWebService_getTrajectoryData_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_ResponseStruct2)((SOAPDeserializationState)_responseObj).getInstance();
            } else {
                _myAPIWebService_getTrajectoryData_ResponseStruct2 =
                    (com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_ResponseStruct2)_responseObj;
            }
            
            return _myAPIWebService_getTrajectoryData_ResponseStruct2.getResult();
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
deserializationContext.pushEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
        int opcode = state.getRequest().getOperationCode();
        switch (opcode) {
            case getBatchHeader_OPCODE:
                _deserialize_getBatchHeader(bodyReader, deserializationContext, state);
                break;
            case getBatchStatus_OPCODE:
                _deserialize_getBatchStatus(bodyReader, deserializationContext, state);
                break;
            case getDispatchBatchesSinceUID_OPCODE:
                _deserialize_getDispatchBatchesSinceUID(bodyReader, deserializationContext, state);
                break;
            case submitMSSLFRequest_OPCODE:
                _deserialize_submitMSSLFRequest(bodyReader, deserializationContext, state);
                break;
            case isNewTrajData_OPCODE:
                _deserialize_isNewTrajData(bodyReader, deserializationContext, state);
                break;
            case getDispatchBatch_OPCODE:
                _deserialize_getDispatchBatch(bodyReader, deserializationContext, state);
                break;
            case validateDispatchBatch_OPCODE:
                _deserialize_validateDispatchBatch(bodyReader, deserializationContext, state);
                break;
            case getTrajectoryData_OPCODE:
                _deserialize_getTrajectoryData(bodyReader, deserializationContext, state);
                break;
            default:
                throw new SenderException("sender.response.unrecognizedOperation", java.lang.Integer.toString(opcode));
        }
    }
    
    
    
    /*
     * This method deserializes the body of the getBatchHeader operation.
     */
    /**
     * _deserialize_get batch header.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_getBatchHeader(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myAPIWebService_getBatchHeader_ResponseStruct2Obj =
            ns1_myAPIWebService_getBatchHeader_ResponseStruct2_SOAPSerializer.deserialize(ns1_getBatchHeader_getBatchHeaderResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_getBatchHeader_getBatchHeaderResponse_QNAME);
        bodyBlock.setValue(myAPIWebService_getBatchHeader_ResponseStruct2Obj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the getBatchStatus operation.
     */
    /**
     * _deserialize_get batch status.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_getBatchStatus(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myAPIWebService_getBatchStatus_ResponseStruct2Obj =
            ns1_myAPIWebService_getBatchStatus_ResponseStruct2_SOAPSerializer.deserialize(ns1_getBatchStatus_getBatchStatusResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_getBatchStatus_getBatchStatusResponse_QNAME);
        bodyBlock.setValue(myAPIWebService_getBatchStatus_ResponseStruct2Obj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the getDispatchBatchesSinceUID operation.
     */
    /**
     * _deserialize_get dispatch batches since uid.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_getDispatchBatchesSinceUID(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2Obj =
            ns1_myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2_SOAPSerializer.deserialize(ns1_getDispatchBatchesSinceUID_getDispatchBatchesSinceUIDResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_getDispatchBatchesSinceUID_getDispatchBatchesSinceUIDResponse_QNAME);
        bodyBlock.setValue(myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2Obj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the submitMSSLFRequest operation.
     */
    /**
     * _deserialize_submit msslf request.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_submitMSSLFRequest(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myAPIWebService_submitMSSLFRequest_ResponseStruct2Obj =
            ns1_myAPIWebService_submitMSSLFRequest_ResponseStruct2_SOAPSerializer.deserialize(ns1_submitMSSLFRequest_submitMSSLFRequestResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_submitMSSLFRequest_submitMSSLFRequestResponse_QNAME);
        bodyBlock.setValue(myAPIWebService_submitMSSLFRequest_ResponseStruct2Obj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the isNewTrajData operation.
     */
    /**
     * _deserialize_is new traj data.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_isNewTrajData(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myAPIWebService_isNewTrajData_ResponseStruct2Obj =
            ns1_myAPIWebService_isNewTrajData_ResponseStruct2_SOAPSerializer.deserialize(ns1_isNewTrajData_isNewTrajDataResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_isNewTrajData_isNewTrajDataResponse_QNAME);
        bodyBlock.setValue(myAPIWebService_isNewTrajData_ResponseStruct2Obj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the getDispatchBatch operation.
     */
    /**
     * _deserialize_get dispatch batch.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_getDispatchBatch(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myAPIWebService_getDispatchBatch_ResponseStruct2Obj =
            ns1_myAPIWebService_getDispatchBatch_ResponseStruct2_SOAPSerializer.deserialize(ns1_getDispatchBatch_getDispatchBatchResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_getDispatchBatch_getDispatchBatchResponse_QNAME);
        bodyBlock.setValue(myAPIWebService_getDispatchBatch_ResponseStruct2Obj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the validateDispatchBatch operation.
     */
    /**
     * _deserialize_validate dispatch batch.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_validateDispatchBatch(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myAPIWebService_validateDispatchBatch_ResponseStruct2Obj =
            ns1_myAPIWebService_validateDispatchBatch_ResponseStruct2_SOAPSerializer.deserialize(ns1_validateDispatchBatch_validateDispatchBatchResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_validateDispatchBatch_validateDispatchBatchResponse_QNAME);
        bodyBlock.setValue(myAPIWebService_validateDispatchBatch_ResponseStruct2Obj);
        state.getResponse().setBody(bodyBlock);
    }
    
    /*
     * This method deserializes the body of the getTrajectoryData operation.
     */
    /**
     * _deserialize_get trajectory data.
     * 
     * @param bodyReader the body reader
     * @param deserializationContext the deserialization context
     * @param state the state
     * 
     * @throws Exception the exception
     */
    private void _deserialize_getTrajectoryData(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object myAPIWebService_getTrajectoryData_ResponseStruct2Obj =
            ns1_myAPIWebService_getTrajectoryData_ResponseStruct2_SOAPSerializer.deserialize(ns1_getTrajectoryData_getTrajectoryDataResponse_QNAME,
                bodyReader, deserializationContext);
        
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_getTrajectoryData_getTrajectoryDataResponse_QNAME);
        bodyBlock.setValue(myAPIWebService_getTrajectoryData_ResponseStruct2Obj);
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
        ns1_myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_ResponseStruct2.class, ns1_getDispatchBatchesSinceUIDResponse_TYPE_QNAME);
        ns1_myAPIWebService_getBatchStatus_ResponseStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_ResponseStruct2.class, ns1_getBatchStatusResponse_TYPE_QNAME);
        ns1_myAPIWebService_validateDispatchBatch_RequestStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_RequestStruct2.class, ns1_validateDispatchBatch_TYPE_QNAME);
        ns1_myAPIWebService_getBatchHeader_RequestStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_RequestStruct2.class, ns1_getBatchHeader_TYPE_QNAME);
        ns1_myAPIWebService_isNewTrajData_RequestStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_RequestStruct2.class, ns1_isNewTrajData_TYPE_QNAME);
        ns1_myAPIWebService_submitMSSLFRequest_ResponseStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2.class, ns1_submitMSSLFRequestResponse_TYPE_QNAME);
        ns1_myAPIWebService_getDispatchBatch_RequestStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_RequestStruct2.class, ns1_getDispatchBatch_TYPE_QNAME);
        ns1_myAPIWebService_getDispatchBatchesSinceUID_RequestStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_RequestStruct2.class, ns1_getDispatchBatchesSinceUID_TYPE_QNAME);
        ns1_myAPIWebService_isNewTrajData_ResponseStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_ResponseStruct2.class, ns1_isNewTrajDataResponse_TYPE_QNAME);
        ns1_myAPIWebService_validateDispatchBatch_ResponseStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2.class, ns1_validateDispatchBatchResponse_TYPE_QNAME);
        ns1_myAPIWebService_getTrajectoryData_ResponseStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_ResponseStruct2.class, ns1_getTrajectoryDataResponse_TYPE_QNAME);
        ns1_myAPIWebService_submitMSSLFRequest_RequestStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2.class, ns1_submitMSSLFRequest_TYPE_QNAME);
        ns1_myAPIWebService_getTrajectoryData_RequestStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_RequestStruct2.class, ns1_getTrajectoryData_TYPE_QNAME);
        ns1_myAPIWebService_getBatchStatus_RequestStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_RequestStruct2.class, ns1_getBatchStatus_TYPE_QNAME);
        ns1_myAPIWebService_getBatchHeader_ResponseStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_ResponseStruct2.class, ns1_getBatchHeaderResponse_TYPE_QNAME);
        ns1_myAPIWebService_getDispatchBatch_ResponseStruct2_SOAPSerializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_ResponseStruct2.class, ns1_getDispatchBatchResponse_TYPE_QNAME);
    }
    
    /** The Constant _portName. */
    private static final javax.xml.namespace.QName _portName = new QName("urn-com-caiso-ads-webservices-api", "APIWebServicePort");
    
    /** The Constant getBatchHeader_OPCODE. */
    private static final int getBatchHeader_OPCODE = 0;
    
    /** The Constant getBatchStatus_OPCODE. */
    private static final int getBatchStatus_OPCODE = 1;
    
    /** The Constant getDispatchBatchesSinceUID_OPCODE. */
    private static final int getDispatchBatchesSinceUID_OPCODE = 2;
    
    /** The Constant submitMSSLFRequest_OPCODE. */
    private static final int submitMSSLFRequest_OPCODE = 3;
    
    /** The Constant isNewTrajData_OPCODE. */
    private static final int isNewTrajData_OPCODE = 4;
    
    /** The Constant getDispatchBatch_OPCODE. */
    private static final int getDispatchBatch_OPCODE = 5;
    
    /** The Constant validateDispatchBatch_OPCODE. */
    private static final int validateDispatchBatch_OPCODE = 6;
    
    /** The Constant getTrajectoryData_OPCODE. */
    private static final int getTrajectoryData_OPCODE = 7;
    
    /** The Constant ns1_getBatchHeader_getBatchHeader_QNAME. */
    private static final javax.xml.namespace.QName ns1_getBatchHeader_getBatchHeader_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getBatchHeader");
    
    /** The Constant ns1_getBatchHeader_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getBatchHeader_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getBatchHeader");
    
    /** The ns1_my api web service_get batch header_ request struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getBatchHeader_RequestStruct2_SOAPSerializer;
    
    /** The Constant ns1_getBatchHeader_getBatchHeaderResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_getBatchHeader_getBatchHeaderResponse_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getBatchHeaderResponse");
    
    /** The Constant ns1_getBatchHeaderResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getBatchHeaderResponse_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getBatchHeaderResponse");
    
    /** The ns1_my api web service_get batch header_ response struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getBatchHeader_ResponseStruct2_SOAPSerializer;
    
    /** The Constant ns1_getBatchStatus_getBatchStatus_QNAME. */
    private static final javax.xml.namespace.QName ns1_getBatchStatus_getBatchStatus_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getBatchStatus");
    
    /** The Constant ns1_getBatchStatus_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getBatchStatus_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getBatchStatus");
    
    /** The ns1_my api web service_get batch status_ request struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getBatchStatus_RequestStruct2_SOAPSerializer;
    
    /** The Constant ns1_getBatchStatus_getBatchStatusResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_getBatchStatus_getBatchStatusResponse_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getBatchStatusResponse");
    
    /** The Constant ns1_getBatchStatusResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getBatchStatusResponse_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getBatchStatusResponse");
    
    /** The ns1_my api web service_get batch status_ response struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getBatchStatus_ResponseStruct2_SOAPSerializer;
    
    /** The Constant ns1_getDispatchBatchesSinceUID_getDispatchBatchesSinceUID_QNAME. */
    private static final javax.xml.namespace.QName ns1_getDispatchBatchesSinceUID_getDispatchBatchesSinceUID_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchesSinceUID");
    
    /** The Constant ns1_getDispatchBatchesSinceUID_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getDispatchBatchesSinceUID_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchesSinceUID");
    
    /** The ns1_my api web service_get dispatch batches since ui d_ request struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getDispatchBatchesSinceUID_RequestStruct2_SOAPSerializer;
    
    /** The Constant ns1_getDispatchBatchesSinceUID_getDispatchBatchesSinceUIDResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_getDispatchBatchesSinceUID_getDispatchBatchesSinceUIDResponse_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchesSinceUIDResponse");
    
    /** The Constant ns1_getDispatchBatchesSinceUIDResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getDispatchBatchesSinceUIDResponse_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchesSinceUIDResponse");
    
    /** The ns1_my api web service_get dispatch batches since ui d_ response struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getDispatchBatchesSinceUID_ResponseStruct2_SOAPSerializer;
    
    /** The Constant ns1_submitMSSLFRequest_submitMSSLFRequest_QNAME. */
    private static final javax.xml.namespace.QName ns1_submitMSSLFRequest_submitMSSLFRequest_QNAME = new QName("urn-com-caiso-ads-webservices-api", "submitMSSLFRequest");
    
    /** The Constant ns1_submitMSSLFRequest_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_submitMSSLFRequest_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "submitMSSLFRequest");
    
    /** The ns1_my api web service_submit msslf request_ request struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_submitMSSLFRequest_RequestStruct2_SOAPSerializer;
    
    /** The Constant ns1_submitMSSLFRequest_submitMSSLFRequestResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_submitMSSLFRequest_submitMSSLFRequestResponse_QNAME = new QName("urn-com-caiso-ads-webservices-api", "submitMSSLFRequestResponse");
    
    /** The Constant ns1_submitMSSLFRequestResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_submitMSSLFRequestResponse_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "submitMSSLFRequestResponse");
    
    /** The ns1_my api web service_submit msslf request_ response struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_submitMSSLFRequest_ResponseStruct2_SOAPSerializer;
    
    /** The Constant ns1_isNewTrajData_isNewTrajData_QNAME. */
    private static final javax.xml.namespace.QName ns1_isNewTrajData_isNewTrajData_QNAME = new QName("urn-com-caiso-ads-webservices-api", "isNewTrajData");
    
    /** The Constant ns1_isNewTrajData_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_isNewTrajData_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "isNewTrajData");
    
    /** The ns1_my api web service_is new traj data_ request struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_isNewTrajData_RequestStruct2_SOAPSerializer;
    
    /** The Constant ns1_isNewTrajData_isNewTrajDataResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_isNewTrajData_isNewTrajDataResponse_QNAME = new QName("urn-com-caiso-ads-webservices-api", "isNewTrajDataResponse");
    
    /** The Constant ns1_isNewTrajDataResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_isNewTrajDataResponse_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "isNewTrajDataResponse");
    
    /** The ns1_my api web service_is new traj data_ response struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_isNewTrajData_ResponseStruct2_SOAPSerializer;
    
    /** The Constant ns1_getDispatchBatch_getDispatchBatch_QNAME. */
    private static final javax.xml.namespace.QName ns1_getDispatchBatch_getDispatchBatch_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatch");
    
    /** The Constant ns1_getDispatchBatch_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getDispatchBatch_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatch");
    
    /** The ns1_my api web service_get dispatch batch_ request struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getDispatchBatch_RequestStruct2_SOAPSerializer;
    
    /** The Constant ns1_getDispatchBatch_getDispatchBatchResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_getDispatchBatch_getDispatchBatchResponse_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchResponse");
    
    /** The Constant ns1_getDispatchBatchResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getDispatchBatchResponse_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchResponse");
    
    /** The ns1_my api web service_get dispatch batch_ response struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getDispatchBatch_ResponseStruct2_SOAPSerializer;
    
    /** The Constant ns1_validateDispatchBatch_validateDispatchBatch_QNAME. */
    private static final javax.xml.namespace.QName ns1_validateDispatchBatch_validateDispatchBatch_QNAME = new QName("urn-com-caiso-ads-webservices-api", "validateDispatchBatch");
    
    /** The Constant ns1_validateDispatchBatch_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_validateDispatchBatch_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "validateDispatchBatch");
    
    /** The ns1_my api web service_validate dispatch batch_ request struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_validateDispatchBatch_RequestStruct2_SOAPSerializer;
    
    /** The Constant ns1_validateDispatchBatch_validateDispatchBatchResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_validateDispatchBatch_validateDispatchBatchResponse_QNAME = new QName("urn-com-caiso-ads-webservices-api", "validateDispatchBatchResponse");
    
    /** The Constant ns1_validateDispatchBatchResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_validateDispatchBatchResponse_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "validateDispatchBatchResponse");
    
    /** The ns1_my api web service_validate dispatch batch_ response struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_validateDispatchBatch_ResponseStruct2_SOAPSerializer;
    
    /** The Constant ns1_getTrajectoryData_getTrajectoryData_QNAME. */
    private static final javax.xml.namespace.QName ns1_getTrajectoryData_getTrajectoryData_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getTrajectoryData");
    
    /** The Constant ns1_getTrajectoryData_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getTrajectoryData_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getTrajectoryData");
    
    /** The ns1_my api web service_get trajectory data_ request struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getTrajectoryData_RequestStruct2_SOAPSerializer;
    
    /** The Constant ns1_getTrajectoryData_getTrajectoryDataResponse_QNAME. */
    private static final javax.xml.namespace.QName ns1_getTrajectoryData_getTrajectoryDataResponse_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getTrajectoryDataResponse");
    
    /** The Constant ns1_getTrajectoryDataResponse_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns1_getTrajectoryDataResponse_TYPE_QNAME = new QName("urn-com-caiso-ads-webservices-api", "getTrajectoryDataResponse");
    
    /** The ns1_my api web service_get trajectory data_ response struct2_ soap serializer. */
    private CombinedSerializer ns1_myAPIWebService_getTrajectoryData_ResponseStruct2_SOAPSerializer;
    
    /** The Constant myNamespace_declarations. */
    private static final java.lang.String[] myNamespace_declarations =
                                        new java.lang.String[] {
                                            "ns0", "urn-com-caiso-ads-webservices-api"
                                        };
    
    /** The Constant understoodHeaderNames. */
    private static final QName[] understoodHeaderNames = new QName[] {  };
}
