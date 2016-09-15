/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.GetOptOutStates.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for GetOptOutStates complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetOptOutStates">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="drasClientID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="optOutID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetOptOutStates", propOrder = {
    "participantID",
    "programName",
    "drasClientID",
    "optOutID",
    "eventID"
})
public class GetOptOutStates
    implements Serializable
{

    /** The participant id. */
    @XmlElement(nillable = true)
    protected String participantID;
    
    /** The program name. */
    @XmlElement(nillable = true)
    protected String programName;
    
    /** The dras client id. */
    @XmlElement(nillable = true)
    protected String drasClientID;
    
    /** The opt out id. */
    @XmlElement(nillable = true)
    protected String optOutID;
    
    /** The event id. */
    @XmlElement(nillable = true)
    protected String eventID;

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

    /**
     * Gets the value of the programName property.
     * 
     * @return the program name
     * 
     * possible object is
     * {@link String }
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the value of the programName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setProgramName(String value) {
        this.programName = value;
    }

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
     * Gets the value of the optOutID property.
     * 
     * @return the opt out id
     * 
     * possible object is
     * {@link String }
     */
    public String getOptOutID() {
        return optOutID;
    }

    /**
     * Sets the value of the optOutID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setOptOutID(String value) {
        this.optOutID = value;
    }

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

}
