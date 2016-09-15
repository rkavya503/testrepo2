/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuaeventinfo.EventInfoTypeID.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuaeventinfo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EventInfoTypeID.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EventInfoTypeID">
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="PRICE_ABSOLUTE"/>
 * &lt;enumeration value="PRICE_RELATIVE"/>
 * &lt;enumeration value="PRICE_MULTIPLE"/>
 * &lt;enumeration value="LOAD_LEVEL"/>
 * &lt;enumeration value="LOAD_AMOUNT"/>
 * &lt;enumeration value="LOAD_PERCENTAGE"/>
 * &lt;enumeration value="GRID_RELIABILITY"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "EventInfoTypeID")
@XmlEnum
public enum EventInfoTypeID {

    /** The PRIC e_ absolute. */
    PRICE_ABSOLUTE,
    
    /** The PRIC e_ relative. */
    PRICE_RELATIVE,
    
    /** The PRIC e_ multiple. */
    PRICE_MULTIPLE,
    
    /** The LOA d_ level. */
    LOAD_LEVEL,
    
    /** The LOA d_ amount. */
    LOAD_AMOUNT,
    
    /** The LOA d_ percentage. */
    LOAD_PERCENTAGE,
    
    /** The GRI d_ reliability. */
    GRID_RELIABILITY;

    /**
     * Value.
     * 
     * @return the string
     */
    public String value() {
        return name();
    }

    /**
     * From value.
     * 
     * @param v the v
     * 
     * @return the event info type id
     */
    public static EventInfoTypeID fromValue(String v) {
        return valueOf(v);
    }

}
