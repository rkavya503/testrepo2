/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct_SOAPSerializer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * The Class PSS2WSSEI_getPrice_RequestStruct_SOAPSerializer.
 */
public class PSS2WSSEI_getPrice_RequestStruct_SOAPSerializer extends ObjectSerializerBase implements Initializable {
    
    /** The Constant ns1_double_1_QNAME. */
    private static final javax.xml.namespace.QName ns1_double_1_QNAME = new QName("", "double_1");
    
    /** The Constant ns2_double_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns2_double_TYPE_QNAME = SchemaConstants.QNAME_TYPE_DOUBLE;
    
    /** The ns2_myns2__double__double_ double_ serializer. */
    private CombinedSerializer ns2_myns2__double__double_Double_Serializer;
    
    /** The Constant myDOUBLE_1_INDEX. */
    private static final int myDOUBLE_1_INDEX = 0;
    
    /**
     * Instantiates a new pS s2 wsse i_get price_ request struct_ soap serializer.
     * 
     * @param type the type
     * @param encodeType the encode type
     * @param isNullable the is nullable
     * @param encodingStyle the encoding style
     */
    public PSS2WSSEI_getPrice_RequestStruct_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.Initializable#initialize(com.sun.xml.rpc.encoding.InternalTypeMappingRegistry)
     */
    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns2_myns2__double__double_Double_Serializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, double.class, ns2_double_TYPE_QNAME);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doDeserialize(com.sun.xml.rpc.encoding.SOAPDeserializationState, com.sun.xml.rpc.streaming.XMLReader, com.sun.xml.rpc.encoding.SOAPDeserializationContext)
     */
    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct instance = new com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct();
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_double_1_QNAME)) {
                member = ns2_myns2__double__double_Double_Serializer.deserialize(ns1_double_1_QNAME, reader, context);
                instance.setDouble_1(((Double)member).doubleValue());
                reader.nextElementContent();
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] {ns1_double_1_QNAME, elementName});
            }
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object)instance : (java.lang.Object)state);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeAttributes(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct instance = (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct)obj;
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeInstance(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct instance = (com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI_getPrice_RequestStruct)obj;
        
        ns2_myns2__double__double_Double_Serializer.serialize(new Double(instance.getDouble_1()), ns1_double_1_QNAME, null, writer, context);
    }
}
