/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.InitiateDREvent.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for InitiateDREvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InitiateDREvent">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DR_Event" type="{http://www.openadr.org/DRAS/UtilityDREvent}UtilityDREvent"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InitiateDREvent", propOrder = {
    "drEvent"
})
public class InitiateDREvent
    implements Serializable
{

    /** The dr event. */
    @XmlElement(name = "DR_Event", required = true)
    protected UtilityDREvent drEvent;

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
