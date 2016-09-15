/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2_SOAPSerializer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * The Class APIWebService_validateDispatchBatch_ResponseStruct2_SOAPSerializer.
 */
public class APIWebService_validateDispatchBatch_ResponseStruct2_SOAPSerializer extends ObjectSerializerBase implements Initializable {
    
    /**
     * Instantiates a new aPI web service_validate dispatch batch_ response struct2_ soap serializer.
     * 
     * @param type the type
     * @param encodeType the encode type
     * @param isNullable the is nullable
     * @param encodingStyle the encoding style
     */
    public APIWebService_validateDispatchBatch_ResponseStruct2_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.Initializable#initialize(com.sun.xml.rpc.encoding.InternalTypeMappingRegistry)
     */
    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doDeserialize(com.sun.xml.rpc.encoding.SOAPDeserializationState, com.sun.xml.rpc.streaming.XMLReader, com.sun.xml.rpc.encoding.SOAPDeserializationContext)
     */
    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2 instance = new com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2();
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        
        reader.nextElementContent();
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object)instance : (java.lang.Object)state);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeAttributes(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2 instance = (com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2)obj;
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeInstance(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2 instance = (com.akuacom.pss2.adsws.stubs.APIWebService_validateDispatchBatch_ResponseStruct2)obj;
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.SerializerBase#verifyName(com.sun.xml.rpc.streaming.XMLReader, javax.xml.namespace.QName)
     */
    protected void verifyName(XMLReader reader, javax.xml.namespace.QName expectedName) throws java.lang.Exception {
    }
}
