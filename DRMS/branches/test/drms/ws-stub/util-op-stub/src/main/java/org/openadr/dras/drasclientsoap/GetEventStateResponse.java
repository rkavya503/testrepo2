/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.drasclientsoap.GetEventStateResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.drasclientsoap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.eventstate.ListOfEventStates;


/**
 * <p>Java class for GetEventStateResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetEventStateResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="eventStates" type="{http://www.openadr.org/DRAS/EventState}ListOfEventStates"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetEventStateResponse", propOrder = {
    "returnValue",
    "eventStates"
})
public class GetEventStateResponse {

    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;
    
    /** The event states. */
    @XmlElement(required = true)
    protected ListOfEventStates eventStates;

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

    /**
     * Gets the value of the eventStates property.
     * 
     * @return the event states
     * 
     * possible object is
     * {@link ListOfEventStates }
     */
    public ListOfEventStates getEventStates() {
        return eventStates;
    }

    /**
     * Sets the value of the eventStates property.
     * 
     * @param value allowed object is
     * {@link ListOfEventStates }
     */
    public void setEventStates(ListOfEventStates value) {
        this.eventStates = value;
    }

}
