/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.GetDRASEventStates.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.utilitydrevent.ListOfEventIDs;
import org.openadr.dras.utilityprogram.ListOfIDs;
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.utilityprogram.ParticipantList;


/**
 * <p>Java class for GetDRASEventStates complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDRASEventStates">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventIDs" type="{http://www.openadr.org/DRAS/UtilityDREvent}ListOfEventIDs" minOccurs="0"/>
 * &lt;element name="programNames" type="{http://www.openadr.org/DRAS/UtilityProgram}ListOfProgramNames" minOccurs="0"/>
 * &lt;element name="participants" type="{http://www.openadr.org/DRAS/UtilityProgram}ParticipantList" minOccurs="0"/>
 * &lt;element name="drasClients" type="{http://www.openadr.org/DRAS/UtilityProgram}ListOfIDs" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDRASEventStates", propOrder = {
    "eventIDs",
    "programNames",
    "participants",
    "drasClients"
})
public class GetDRASEventStates
    implements Serializable
{

    /** The event i ds. */
    @XmlElement(nillable = true)
    protected ListOfEventIDs eventIDs;
    
    /** The program names. */
    @XmlElement(nillable = true)
    protected ListOfProgramNames programNames;
    
    /** The participants. */
    @XmlElement(nillable = true)
    protected ParticipantList participants;
    
    /** The dras clients. */
    @XmlElement(nillable = true)
    protected ListOfIDs drasClients;

    /**
     * Gets the value of the eventIDs property.
     * 
     * @return the event i ds
     * 
     * possible object is
     * {@link ListOfEventIDs }
     */
    public ListOfEventIDs getEventIDs() {
        return eventIDs;
    }

    /**
     * Sets the value of the eventIDs property.
     * 
     * @param value allowed object is
     * {@link ListOfEventIDs }
     */
    public void setEventIDs(ListOfEventIDs value) {
        this.eventIDs = value;
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
     * Gets the value of the drasClients property.
     * 
     * @return the dras clients
     * 
     * possible object is
     * {@link ListOfIDs }
     */
    public ListOfIDs getDrasClients() {
        return drasClients;
    }

    /**
     * Sets the value of the drasClients property.
     * 
     * @param value allowed object is
     * {@link ListOfIDs }
     */
    public void setDrasClients(ListOfIDs value) {
        this.drasClients = value;
    }

}
