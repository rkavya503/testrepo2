/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.SetBidStatus.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;
import org.openadr.dras.utilitydrevent.ListOfUtilityDREvents;


/**
 * <p>Java class for SetBidStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SetBidStatus">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="acceptedBids" type="{http://www.openadr.org/DRAS/UtilityDREvent}ListOfUtilityDREvents" minOccurs="0"/>
 * &lt;element name="rejectedBids" type="{http://www.openadr.org/DRAS/ParticipantAccount}ListOfParticipantAccountIDs" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetBidStatus", propOrder = {
    "eventID",
    "acceptedBids",
    "rejectedBids"
})
public class SetBidStatus
    implements Serializable
{

    /** The event id. */
    @XmlElement(required = true)
    protected String eventID;
    
    /** The accepted bids. */
    @XmlElement(nillable = true)
    protected ListOfUtilityDREvents acceptedBids;
    
    /** The rejected bids. */
    @XmlElement(nillable = true)
    protected ListOfParticipantAccountIDs rejectedBids;

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
     * Gets the value of the acceptedBids property.
     * 
     * @return the accepted bids
     * 
     * possible object is
     * {@link ListOfUtilityDREvents }
     */
    public ListOfUtilityDREvents getAcceptedBids() {
        return acceptedBids;
    }

    /**
     * Sets the value of the acceptedBids property.
     * 
     * @param value allowed object is
     * {@link ListOfUtilityDREvents }
     */
    public void setAcceptedBids(ListOfUtilityDREvents value) {
        this.acceptedBids = value;
    }

    /**
     * Gets the value of the rejectedBids property.
     * 
     * @return the rejected bids
     * 
     * possible object is
     * {@link ListOfParticipantAccountIDs }
     */
    public ListOfParticipantAccountIDs getRejectedBids() {
        return rejectedBids;
    }

    /**
     * Sets the value of the rejectedBids property.
     * 
     * @param value allowed object is
     * {@link ListOfParticipantAccountIDs }
     */
    public void setRejectedBids(ListOfParticipantAccountIDs value) {
        this.rejectedBids = value;
    }

}
