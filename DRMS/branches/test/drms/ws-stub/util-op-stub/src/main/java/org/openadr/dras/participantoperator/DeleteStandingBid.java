/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.DeleteStandingBid.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for DeleteStandingBid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeleteStandingBid">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeleteStandingBid", propOrder = {
    "participantID",
    "programName"
})
public class DeleteStandingBid
    implements Serializable
{

    /** The participant id. */
    @XmlElement(required = true)
    protected String participantID;
    
    /** The program name. */
    @XmlElement(required = true)
    protected String programName;

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

}
