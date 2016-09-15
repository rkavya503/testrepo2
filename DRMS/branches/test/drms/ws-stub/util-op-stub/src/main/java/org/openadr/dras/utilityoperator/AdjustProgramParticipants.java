/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.AdjustProgramParticipants.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.utilityprogram.ParticipantList;


/**
 * <p>Java class for AdjustProgramParticipants complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdjustProgramParticipants">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="addParticipants" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="participantList" type="{http://www.openadr.org/DRAS/UtilityProgram}ParticipantList"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdjustProgramParticipants", propOrder = {
    "programName",
    "addParticipants",
    "participantList"
})
public class AdjustProgramParticipants
    implements Serializable
{

    /** The program name. */
    @XmlElement(required = true)
    protected String programName;
    
    /** The add participants. */
    protected boolean addParticipants;
    
    /** The participant list. */
    @XmlElement(required = true)
    protected ParticipantList participantList;

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
     * Gets the value of the addParticipants property.
     * 
     * @return true, if checks if is add participants
     */
    public boolean isAddParticipants() {
        return addParticipants;
    }

    /**
     * Sets the value of the addParticipants property.
     * 
     * @param value the value
     */
    public void setAddParticipants(boolean value) {
        this.addParticipants = value;
    }

    /**
     * Gets the value of the participantList property.
     * 
     * @return the participant list
     * 
     * possible object is
     * {@link ParticipantList }
     */
    public ParticipantList getParticipantList() {
        return participantList;
    }

    /**
     * Sets the value of the participantList property.
     * 
     * @param value allowed object is
     * {@link ParticipantList }
     */
    public void setParticipantList(ParticipantList value) {
        this.participantList = value;
    }

}
