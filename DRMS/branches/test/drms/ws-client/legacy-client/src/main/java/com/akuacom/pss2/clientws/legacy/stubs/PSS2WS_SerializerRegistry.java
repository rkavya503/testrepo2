/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WS_SerializerRegistry.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;

import com.sun.xml.rpc.client.BasicService;
import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.soap.*;
import com.sun.xml.rpc.soap.SOAPVersion;

import javax.xml.rpc.encoding.*;
import javax.xml.namespace.QName;

/**
 * The Class PSS2WS_SerializerRegistry.
 */
public class PSS2WS_SerializerRegistry implements SerializerConstants {
    
    /**
     * Instantiates a new pS s2 w s_ serializer registry.
     */
    public PSS2WS_SerializerRegistry() {
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
            QName type = new QName("http://pss2.lbl.gov", "isAPEEventPendingResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct.class, type, serializer);
        }
        {
            QName type = new QName("http://pss2.lbl.gov", "ArrayOfPriceScheduleEntry");
            QName elemName = new QName("", "item");
            QName elemType = new QName("http://pss2.lbl.gov", "PriceScheduleEntry");
            CombinedSerializer serializer = new ObjectArraySerializer(type,
                ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING , 
                elemName, elemType, com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry.class, 1, null, SOAPVersion.SOAP_11);
            serializer = new ReferenceableSerializerImpl(SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[].class, type, serializer);
        }
        {
            QName type = new QName("http://pss2.lbl.gov", "PriceSchedule");
            CombinedSerializer serializer = new com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule_SOAPSerializer(type,
                ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule.class, type, serializer);
        }
        {
            QName type = new QName("http://pss2.lbl.gov", "isAPEEventPending");
            CombinedSerializer serializer = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_RequestStruct_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_RequestStruct.class, type, serializer);
        }
        {
            QName type = new QName("http://pss2.lbl.gov", "getPrice");
            CombinedSerializer serializer = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct.class, type, serializer);
        }
        {
            QName type = new QName("http://pss2.lbl.gov", "getPriceScheduleResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_ResponseStruct_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_ResponseStruct.class, type, serializer);
        }
        {
            QName type = new QName("http://pss2.lbl.gov", "PriceScheduleEntry");
            CombinedSerializer serializer = new com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry_SOAPSerializer(type,
                ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry.class, type, serializer);
        }
        {
            QName type = new QName("http://pss2.lbl.gov", "getPriceResponse");
            CombinedSerializer serializer = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_ResponseStruct_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_ResponseStruct.class, type, serializer);
        }
        {
            QName type = new QName("http://pss2.lbl.gov", "getPriceSchedule");
            CombinedSerializer serializer = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct_SOAPSerializer(type,
                DONT_ENCODE_TYPE, NULLABLE, SOAPConstants.NS_SOAP_ENCODING);
            serializer = new ReferenceableSerializerImpl(DONT_SERIALIZE_AS_REF, serializer, SOAPVersion.SOAP_11);
            registerSerializer(mapping, com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPriceSchedule_RequestStruct.class, type, serializer);
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
