/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.SetTestModeState.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SetTestModeState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SetTestModeState">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="drasClientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="opertaionModeValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="eventStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetTestModeState", propOrder = {
    "drasClientID",
    "opertaionModeValue",
    "eventStatus"
})
public class SetTestModeState
    implements Serializable
{

    /** The dras client id. */
    @XmlElement(required = true)
    protected String drasClientID;
    
    /** The opertaion mode value. */
    @XmlElement(required = true)
    protected String opertaionModeValue;
    
    /** The event status. */
    @XmlElement(required = true)
    protected String eventStatus;

    /**
     * Gets the value of the drasClientID property.
     * 
     * @return the dras client id
     * 
     * possible object is
     * {@link String }
     */
    public String getDrasClientID() {
        return drasClientID;
    }

    /**
     * Sets the value of the drasClientID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDrasClientID(String value) {
        this.drasClientID = value;
    }

    /**
     * Gets the value of the opertaionModeValue property.
     * 
     * @return the opertaion mode value
     * 
     * possible object is
     * {@link String }
     */
    public String getOpertaionModeValue() {
        return opertaionModeValue;
    }

    /**
     * Sets the value of the opertaionModeValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setOpertaionModeValue(String value) {
        this.opertaionModeValue = value;
    }

    /**
     * Gets the value of the eventStatus property.
     * 
     * @return the event status
     * 
     * possible object is
     * {@link String }
     */
    public String getEventStatus() {
        return eventStatus;
    }

    /**
     * Sets the value of the eventStatus property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setEventStatus(String value) {
        this.eventStatus = value;
    }

}
