/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.ModifyDREvent.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.utilitydrevent.UtilityDREvent;


/**
 * <p>Java class for ModifyDREvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModifyDREvent">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="modificationType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="DR_Event" type="{http://www.openadr.org/DRAS/UtilityDREvent}UtilityDREvent"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyDREvent", propOrder = {
    "eventID",
    "modificationType",
    "drEvent"
})
public class ModifyDREvent
    implements Serializable
{

    /** The event id. */
    @XmlElement(required = true)
    protected String eventID;
    
    /** The modification type. */
    @XmlElement(required = true)
    protected String modificationType;
    
    /** The dr event. */
    @XmlElement(name = "DR_Event", required = true)
    protected UtilityDREvent drEvent;

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
     * Gets the value of the modificationType property.
     * 
     * @return the modification type
     * 
     * possible object is
     * {@link String }
     */
    public String getModificationType() {
        return modificationType;
    }

    /**
     * Sets the value of the modificationType property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setModificationType(String value) {
        this.modificationType = value;
    }

    /**
     * Gets the value of the drEvent property.
     * 
     * @return the DR event
     * 
     * possible object is
     * {@link UtilityDREvent }
     */
    public UtilityDREvent getDREvent() {
        return drEvent;
    }

    /**
     * Sets the value of the drEvent property.
     * 
     * @param value allowed object is
     * {@link UtilityDREvent }
     */
    public void setDREvent(UtilityDREvent value) {
        this.drEvent = value;
    }

}
