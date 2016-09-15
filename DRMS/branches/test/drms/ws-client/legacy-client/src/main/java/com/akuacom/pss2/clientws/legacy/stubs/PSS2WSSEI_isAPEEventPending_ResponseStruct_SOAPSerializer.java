/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct_SOAPSerializer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

/**
 * The Class PSS2WSSEI_isAPEEventPending_ResponseStruct_SOAPSerializer.
 */
public class PSS2WSSEI_isAPEEventPending_ResponseStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {
    
    /** The Constant ns1_result_QNAME. */
    private static final javax.xml.namespace.QName ns1_result_QNAME = new QName("", "result");
    
    /** The Constant ns2_boolean_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns2_boolean_TYPE_QNAME = SchemaConstants.QNAME_TYPE_BOOLEAN;
    
    /** The ns2_myns2__boolean__boolean_ boolean_ serializer. */
    private CombinedSerializer ns2_myns2__boolean__boolean_Boolean_Serializer;
    
    /** The Constant myRESULT_INDEX. */
    private static final int myRESULT_INDEX = 0;
    
    /**
     * Instantiates a new pS s2 wsse i_is ape event pending_ response struct_ soap serializer.
     * 
     * @param type the type
     * @param encodeType the encode type
     * @param isNullable the is nullable
     * @param encodingStyle the encoding style
     */
    public PSS2WSSEI_isAPEEventPending_ResponseStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.Initializable#initialize(com.sun.xml.rpc.encoding.InternalTypeMappingRegistry)
     */
    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns2_myns2__boolean__boolean_Boolean_Serializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, boolean.class, ns2_boolean_TYPE_QNAME);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doDeserialize(com.sun.xml.rpc.encoding.SOAPDeserializationState, com.sun.xml.rpc.streaming.XMLReader, com.sun.xml.rpc.encoding.SOAPDeserializationContext)
     */
    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct instance = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct();
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_result_QNAME)) {
                member = ns2_myns2__boolean__boolean_Boolean_Serializer.deserialize(ns1_result_QNAME, reader, context);
                instance.setResult(((Boolean)member).booleanValue());
                reader.nextElementContent();
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] {ns1_result_QNAME, elementName});
            }
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object)instance : (java.lang.Object)state);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeAttributes(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct instance = (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct)obj;
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeInstance(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct instance = (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_isAPEEventPending_ResponseStruct)obj;
        
        ns2_myns2__boolean__boolean_Boolean_Serializer.serialize(new Boolean(instance.isResult()), ns1_result_QNAME, null, writer, context);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SerializerBase#verifyName(com.sun.xml.rpc.streaming.XMLReader, javax.xml.namespace.QName)
     */
    protected void verifyName(XMLReader reader, javax.xml.namespace.QName expectedName) throws java.lang.Exception {
    }
}
