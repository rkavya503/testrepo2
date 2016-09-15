/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule_SOAPSerializer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * The Class PriceSchedule_SOAPSerializer.
 */
public class PriceSchedule_SOAPSerializer extends ObjectSerializerBase implements Initializable {
    
    /** The Constant ns1_currentPriceDPKWH_QNAME. */
    private static final javax.xml.namespace.QName ns1_currentPriceDPKWH_QNAME = new QName("", "currentPriceDPKWH");
    
    /** The Constant ns2_double_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns2_double_TYPE_QNAME = SchemaConstants.QNAME_TYPE_DOUBLE;
    
    /** The ns2_myns2__double__double_ double_ serializer. */
    private CombinedSerializer ns2_myns2__double__double_Double_Serializer;
    
    /** The Constant ns1_entries_QNAME. */
    private static final javax.xml.namespace.QName ns1_entries_QNAME = new QName("", "entries");
    
    /** The Constant ns3_ArrayOfPriceScheduleEntry_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns3_ArrayOfPriceScheduleEntry_TYPE_QNAME = new QName("http://pss2.lbl.gov", "ArrayOfPriceScheduleEntry");
    
    /** The ns3_myns3_ array of price schedule entry__ price schedule entry array_ soap serializer1. */
    private CombinedSerializer ns3_myns3_ArrayOfPriceScheduleEntry__PriceScheduleEntryArray_SOAPSerializer1;
    
    /** The Constant myCURRENTPRICEDPKWH_INDEX. */
    private static final int myCURRENTPRICEDPKWH_INDEX = 0;
    
    /** The Constant myENTRIES_INDEX. */
    private static final int myENTRIES_INDEX = 1;
    
    /**
     * Instantiates a new price schedule_ soap serializer.
     * 
     * @param type the type
     * @param encodeType the encode type
     * @param isNullable the is nullable
     * @param encodingStyle the encoding style
     */
    public PriceSchedule_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.Initializable#initialize(com.sun.xml.rpc.encoding.InternalTypeMappingRegistry)
     */
    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns2_myns2__double__double_Double_Serializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, double.class, ns2_double_TYPE_QNAME);
        ns3_myns3_ArrayOfPriceScheduleEntry__PriceScheduleEntryArray_SOAPSerializer1 = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[].class, ns3_ArrayOfPriceScheduleEntry_TYPE_QNAME);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doDeserialize(com.sun.xml.rpc.encoding.SOAPDeserializationState, com.sun.xml.rpc.streaming.XMLReader, com.sun.xml.rpc.encoding.SOAPDeserializationContext)
     */
    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule instance = new com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule();
        com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule_SOAPBuilder builder = null;
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_currentPriceDPKWH_QNAME)) {
                member = ns2_myns2__double__double_Double_Serializer.deserialize(ns1_currentPriceDPKWH_QNAME, reader, context);
                instance.setCurrentPriceDPKWH(((Double)member).doubleValue());
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_entries_QNAME)) {
                member = ns3_myns3_ArrayOfPriceScheduleEntry__PriceScheduleEntryArray_SOAPSerializer1.deserialize(ns1_entries_QNAME, reader, context);
                if (member instanceof SOAPDeserializationState) {
                    if (builder == null) {
                        builder = new com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule_SOAPBuilder();
                    }
                    state = registerWithMemberState(instance, state, member, myENTRIES_INDEX, builder);
                    isComplete = false;
                } else {
                    instance.setEntries((com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry[])member);
                }
                reader.nextElementContent();
            }
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (isComplete ? (java.lang.Object)instance : (java.lang.Object)state);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeAttributes(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule instance = (com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule)obj;
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeInstance(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule instance = (com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule)obj;
        
        ns2_myns2__double__double_Double_Serializer.serialize(new Double(instance.getCurrentPriceDPKWH()), ns1_currentPriceDPKWH_QNAME, null, writer, context);
        ns3_myns3_ArrayOfPriceScheduleEntry__PriceScheduleEntryArray_SOAPSerializer1.serialize(instance.getEntries(), ns1_entries_QNAME, null, writer, context);
    }
}
