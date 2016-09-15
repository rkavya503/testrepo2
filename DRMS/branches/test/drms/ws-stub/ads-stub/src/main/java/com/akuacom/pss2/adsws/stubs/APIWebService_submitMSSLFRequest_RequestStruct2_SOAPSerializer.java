/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2_SOAPSerializer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.adsws.stubs;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;

/**
 * The Class APIWebService_submitMSSLFRequest_RequestStruct2_SOAPSerializer.
 */
public class APIWebService_submitMSSLFRequest_RequestStruct2_SOAPSerializer extends ObjectSerializerBase implements Initializable {
    
    /** The Constant ns1_request_QNAME. */
    private static final javax.xml.namespace.QName ns1_request_QNAME = new QName("", "request");
    
    /** The Constant ns2_string_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns2_string_TYPE_QNAME = SchemaConstants.QNAME_TYPE_STRING;
    
    /** The ns2_myns2_string__java_lang_ string_ string_ serializer. */
    private CombinedSerializer ns2_myns2_string__java_lang_String_String_Serializer;
    
    /** The Constant myREQUEST_INDEX. */
    private static final int myREQUEST_INDEX = 0;
    
    /**
     * Instantiates a new aPI web service_submit msslf request_ request struct2_ soap serializer.
     * 
     * @param type the type
     * @param encodeType the encode type
     * @param isNullable the is nullable
     * @param encodingStyle the encoding style
     */
    public APIWebService_submitMSSLFRequest_RequestStruct2_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.Initializable#initialize(com.sun.xml.rpc.encoding.InternalTypeMappingRegistry)
     */
    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns2_myns2_string__java_lang_String_String_Serializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, java.lang.String.class, ns2_string_TYPE_QNAME);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doDeserialize(com.sun.xml.rpc.encoding.SOAPDeserializationState, com.sun.xml.rpc.streaming.XMLReader, com.sun.xml.rpc.encoding.SOAPDeserializationContext)
     */
    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2 instance = new com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2();
        com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2_SOAPBuilder builder = null;
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_request_QNAME)) {
                member = ns2_myns2_string__java_lang_String_String_Serializer.deserialize(ns1_request_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myREQUEST_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setRequest((java.lang.String)member);
                }
                reader.nextElementContent();
            } else {
                throw new DeserializationException("soap.unexpectedElementName", new Object[] {ns1_request_QNAME, elementName});
            }
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object)instance : (java.lang.Object)state);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeAttributes(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2 instance = (com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2)obj;
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeInstance(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2 instance = (com.akuacom.pss2.adsws.stubs.APIWebService_submitMSSLFRequest_RequestStruct2)obj;
        
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getRequest(), ns1_request_QNAME, null, writer, context);
    }
}
