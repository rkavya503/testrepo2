/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.GetDREventsResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuautilitydrevent.ListOfUtilityDREvents;


/**
 * <p>Java class for GetDREventsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDREventsResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DREvents" type="{http://www.openadr.org/DRAS/AkuaUtilityDREvent}ListOfUtilityDREvents" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDREventsResponse", propOrder = {
    "drEvents",
    "returnValue"
})
public class GetDREventsResponse
    implements Serializable
{

    /** The dr events. */
    @XmlElement(name = "DREvents", nillable = true)
    protected ListOfUtilityDREvents drEvents;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the drEvents property.
     * 
     * @return the DR events
     * 
     * possible object is
     * {@link ListOfUtilityDREvents }
     */
    public ListOfUtilityDREvents getDREvents() {
        return drEvents;
    }

    /**
     * Sets the value of the drEvents property.
     * 
     * @param value allowed object is
     * {@link ListOfUtilityDREvents }
     */
    public void setDREvents(ListOfUtilityDREvents value) {
        this.drEvents = value;
    }

    /**
     * Gets the value of the returnValue property.
     * 
     * @return the return value
     * 
     * possible object is
     * {@link String }
     */
    public String getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the value of the returnValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setReturnValue(String value) {
        this.returnValue = value;
    }

}
