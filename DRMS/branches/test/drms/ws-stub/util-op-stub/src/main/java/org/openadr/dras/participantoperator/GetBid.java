/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.GetBid.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for GetBid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetBid">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetBid", propOrder = {
    "eventID",
    "participantID"
})
public class GetBid
    implements Serializable
{

    /** The event id. */
    @XmlElement(required = true)
    protected String eventID;
    
    /** The participant id. */
    @XmlElement(required = true)
    protected String participantID;

    /**
     * Gets the value of the eventID property.
     * 
     * @return the event id
     * 
     * possible object is
     * {@link String }
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the value of the eventID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setEventID(String value) {
        this.eventID = value;
    }

    /**
     * Gets the value of the participantID property.
     * 
     * @return the participant id
     * 
     * possible object is
     * {@link String }
     */
    public String getParticipantID() {
        return participantID;
    }

    /**
     * Sets the value of the participantID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParticipantID(String value) {
        this.participantID = value;
    }

}
