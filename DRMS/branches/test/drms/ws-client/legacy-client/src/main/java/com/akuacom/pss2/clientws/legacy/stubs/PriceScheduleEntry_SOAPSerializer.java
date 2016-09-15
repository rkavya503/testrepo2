/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry_SOAPSerializer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * The Class PriceScheduleEntry_SOAPSerializer.
 */
public class PriceScheduleEntry_SOAPSerializer extends ObjectSerializerBase implements Initializable {
    
    /** The Constant ns1_date_QNAME. */
    private static final javax.xml.namespace.QName ns1_date_QNAME = new QName("", "date");
    
    /** The Constant ns2_dateTime_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns2_dateTime_TYPE_QNAME = SchemaConstants.QNAME_TYPE_DATE_TIME;
    
    /** The ns2_myns2_date time__java_util_ calendar_ date time calendar_ serializer. */
    private CombinedSerializer ns2_myns2_dateTime__java_util_Calendar_DateTimeCalendar_Serializer;
    
    /** The Constant ns1_priceDPKWH_QNAME. */
    private static final javax.xml.namespace.QName ns1_priceDPKWH_QNAME = new QName("", "priceDPKWH");
    
    /** The Constant ns2_double_TYPE_QNAME. */
    private static final javax.xml.namespace.QName ns2_double_TYPE_QNAME = SchemaConstants.QNAME_TYPE_DOUBLE;
    
    /** The ns2_myns2__double__double_ double_ serializer. */
    private CombinedSerializer ns2_myns2__double__double_Double_Serializer;
    
    /** The Constant myDATE_INDEX. */
    private static final int myDATE_INDEX = 0;
    
    /** The Constant myPRICEDPKWH_INDEX. */
    private static final int myPRICEDPKWH_INDEX = 1;
    
    /**
     * Instantiates a new price schedule entry_ soap serializer.
     * 
     * @param type the type
     * @param encodeType the encode type
     * @param isNullable the is nullable
     * @param encodingStyle the encoding style
     */
    public PriceScheduleEntry_SOAPSerializer(QName type, boolean encodeType, boolean isNullable, String encodingStyle) {
        super(type, encodeType, isNullable, encodingStyle);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.Initializable#initialize(com.sun.xml.rpc.encoding.InternalTypeMappingRegistry)
     */
    public void initialize(InternalTypeMappingRegistry registry) throws java.lang.Exception {
        ns2_myns2_dateTime__java_util_Calendar_DateTimeCalendar_Serializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, java.util.Calendar.class, ns2_dateTime_TYPE_QNAME);
        ns2_myns2__double__double_Double_Serializer = (CombinedSerializer)registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, double.class, ns2_double_TYPE_QNAME);
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doDeserialize(com.sun.xml.rpc.encoding.SOAPDeserializationState, com.sun.xml.rpc.streaming.XMLReader, com.sun.xml.rpc.encoding.SOAPDeserializationContext)
     */
    public java.lang.Object doDeserialize(SOAPDeserializationState state, XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry instance = new com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry();
        java.lang.Object member;
        boolean isComplete = true;
        javax.xml.namespace.QName elementName;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_date_QNAME)) {
                member = ns2_myns2_dateTime__java_util_Calendar_DateTimeCalendar_Serializer.deserialize(ns1_date_QNAME, reader, context);
                instance.setDate((java.util.Calendar)member);
                reader.nextElementContent();
            }
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_priceDPKWH_QNAME)) {
                member = ns2_myns2__double__double_Double_Serializer.deserialize(ns1_priceDPKWH_QNAME, reader, context);
                instance.setPriceDPKWH(((Double)member).doubleValue());
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
        com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry instance = (com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry)obj;
        
    }
    
    /* (non-Javadoc)
     * @see com.sun.xml.rpc.encoding.ObjectSerializerBase#doSerializeInstance(java.lang.Object, com.sun.xml.rpc.streaming.XMLWriter, com.sun.xml.rpc.encoding.SOAPSerializationContext)
     */
    public void doSerializeInstance(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry instance = (com.akuacom.pss2.clientws.legacy.stubs.PriceScheduleEntry)obj;
        
        ns2_myns2_dateTime__java_util_Calendar_DateTimeCalendar_Serializer.serialize(instance.getDate(), ns1_date_QNAME, null, writer, context);
        ns2_myns2__double__double_Double_Serializer.serialize(new Double(instance.getPriceDPKWH()), ns1_priceDPKWH_QNAME, null, writer, context);
    }
}
