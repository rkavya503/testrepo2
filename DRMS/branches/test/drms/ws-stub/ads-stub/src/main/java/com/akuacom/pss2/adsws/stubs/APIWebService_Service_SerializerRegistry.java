/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.stubs.APIWebService_Service_SerializerRegistry.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.adsws.stubs;

import com.sun.xml.rpc.client.BasicService;
import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.*;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.soap.SOAPVersion;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.rpc.*;
import javax.xml.rpc.encoding.*;
import javax.xml.namespace.QName;

/**
 * The Class APIWebService_Service_SerializerRegistry.
 */
public class APIWebService_Service_SerializerRegistry implements SerializerConstants {
    
    /**
     * Instantiates a new aPI web service_ service_ serializer registry.
     */
    public APIWebService_Service_SerializerRegistry() {
    }
    
    /**
     * Gets the registry.
     * 
     * @return the registry
     */
    public TypeMappingRegistry getRegistry() {
        
        TypeMappingRegistry registry = BasicService.createStandardTypeMappingRegistry();
        TypeMapping mapping12 = registry.getTypeMapping(SOAP12Constants.NS_SOAP_ENCODING);
        TypeMapping mapping = registry.getTypeMapping(SOAPConstants.NS_SOAP_ENCODING);
        TypeMapping mapping2 = registry.getTypeMapping("");
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "validateDispatchBatchResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getTrajectoryDataResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_ResponseStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_ResponseStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getBatchStatusResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_ResponseStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_ResponseStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "submitMSSLFRequest");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "isNewTrajDataResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_ResponseStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_ResponseStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_ResponseStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_ResponseStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getBatchHeaderResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_ResponseStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_ResponseStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "validateDispatchBatch");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_RequestStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_RequestStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getTrajectoryData");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_RequestStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getTrajectoryData_RequestStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchesSinceUID");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_RequestStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_RequestStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getBatchHeader");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_RequestStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getBatchHeader_RequestStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getBatchStatus");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_RequestStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getBatchStatus_RequestStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "isNewTrajData");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_RequestStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_isNewTrajData_RequestStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatch");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_RequestStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatch_RequestStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "getDispatchBatchesSinceUIDResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_ResponseStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_getDispatchBatchesSinceUID_ResponseStruct2.class, type, serializer);
        }
        {
            QName type = new QName("urn-com-caiso-ads-webservices-api", "submitMSSLFRequestResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping,com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_ResponseStruct2.class, type, serializer);
        }
        return registry;
    }
    
    /**
     * Register serializer.
     * 
     * @param mapping the mapping
     * @param javaType the java type
     * @param xmlType the xml type
     * @param ser the ser
     */
    private static void registerSerializer(TypeMapping mapping, java.lang.Class javaType, javax.xml.namespace.QName xmlType,
        Serializer ser) {
        mapping.register(javaType, xmlType, new SingletonSerializerFactory(ser),
            new SingletonDeserializerFactory((Deserializer)ser));
    }
    
}
