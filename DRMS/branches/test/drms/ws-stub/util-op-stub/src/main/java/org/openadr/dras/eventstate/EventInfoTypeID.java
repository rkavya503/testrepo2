/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventstate.EventInfoTypeID.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventstate;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * This identifies the data type of an EventInfoType definition and may take on any of the following values:
 *	PRICE_ABSOLUTE - Price number, i.e. $0.25
 *	PRICE_RELATIVE - Change in price, i.e. -$0.05
 *	PRICE_MULTIPLE - Multiple of current price, i.e. 1.5
 *	LOAD_LEVEL - Amount of load based on an enumeration, i.e. moderate, high, etc
 *	LOAD_AMOUNT - Fixed amount of load to shed, i.e. 5 MW
 *	LOAD_PERCENTAGE - Percentage of load to shed, i.e. 10%
 *	GRID_RELIABILITY - Number signifying the reliability of the grid
 * 
 * 
 * <p>Java class for EventInfoTypeID complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventInfoTypeID">
 * &lt;simpleContent>
 * &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 * &lt;/extension>
 * &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventInfoTypeID", propOrder = {
    "value"
})
public class EventInfoTypeID
    implements Serializable
{

    /** The value. */
    @XmlValue
    protected String value;

    /**
     * Gets the value of the value property.
     * 
     * @return the value
     * 
     * possible object is
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

}
