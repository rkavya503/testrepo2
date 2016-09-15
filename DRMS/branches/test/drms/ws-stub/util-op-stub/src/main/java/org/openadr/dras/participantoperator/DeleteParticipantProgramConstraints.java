/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.DeleteParticipantProgramConstraints.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;


/**
 * <p>Java class for DeleteParticipantProgramConstraints complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeleteParticipantProgramConstraints">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantIDs" type="{http://www.openadr.org/DRAS/ParticipantAccount}ListOfParticipantAccountIDs"/>
 * &lt;element name="participantGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeleteParticipantProgramConstraints", propOrder = {
    "participantIDs",
    "participantGroup",
    "programName"
})
public class DeleteParticipantProgramConstraints
    implements Serializable
{

    /** The participant i ds. */
    @XmlElement(required = true)
    protected ListOfParticipantAccountIDs participantIDs;
    
    /** The participant group. */
    @XmlElement(nillable = true)
    protected String participantGroup;
    
    /** The program name. */
    @XmlElement(required = true)
    protected String programName;

    /**
     * Gets the value of the participantIDs property.
     * 
     * @return the participant i ds
     * 
     * possible object is
     * {@link ListOfParticipantAccountIDs }
     */
    public ListOfParticipantAccountIDs getParticipantIDs() {
        return participantIDs;
    }

    /**
     * Sets the value of the participantIDs property.
     * 
     * @param value allowed object is
     * {@link ListOfParticipantAccountIDs }
     */
    public void setParticipantIDs(ListOfParticipantAccountIDs value) {
        this.participantIDs = value;
    }

    /**
     * Gets the value of the participantGroup property.
     * 
     * @return the participant group
     * 
     * possible object is
     * {@link String }
     */
    public String getParticipantGroup() {
        return participantGroup;
    }

    /**
     * Sets the value of the participantGroup property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParticipantGroup(String value) {
        this.participantGroup = value;
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
