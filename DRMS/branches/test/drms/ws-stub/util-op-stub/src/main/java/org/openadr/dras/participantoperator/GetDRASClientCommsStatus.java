/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.GetDRASClientCommsStatus.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.utilityprogram.ParticipantList;


/**
 * <p>Java class for GetDRASClientCommsStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDRASClientCommsStatus">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participants" type="{http://www.openadr.org/DRAS/UtilityProgram}ParticipantList" minOccurs="0"/>
 * &lt;element name="programNames" type="{http://www.openadr.org/DRAS/UtilityProgram}ListOfProgramNames" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDRASClientCommsStatus", propOrder = {
    "participants",
    "programNames"
})
public class GetDRASClientCommsStatus
    implements Serializable
{

    /** The participants. */
    @XmlElement(nillable = true)
    protected ParticipantList participants;
    
    /** The program names. */
    @XmlElement(nillable = true)
    protected ListOfProgramNames programNames;

    /**
     * Gets the value of the participants property.
     * 
     * @return the participants
     * 
     * possible object is
     * {@link ParticipantList }
     */
    public ParticipantList getParticipants() {
        return participants;
    }

    /**
     * Sets the value of the participants property.
     * 
     * @param value allowed object is
     * {@link ParticipantList }
     */
    public void setParticipants(ParticipantList value) {
        this.participants = value;
    }

    /**
     * Gets the value of the programNames property.
     * 
     * @return the program names
     * 
     * possible object is
     * {@link ListOfProgramNames }
     */
    public ListOfProgramNames getProgramNames() {
        return programNames;
    }

    /**
     * Sets the value of the programNames property.
     * 
     * @param value allowed object is
     * {@link ListOfProgramNames }
     */
    public void setProgramNames(ListOfProgramNames value) {
        this.programNames = value;
    }

}
