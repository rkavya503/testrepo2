/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.UpdateEventTemplate.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuautilitydrevent.UtilityDREvent;


/**
 * <p>Java class for UpdateEventTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateEventTemplate">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventTemplate" type="{http://www.openadr.org/DRAS/AkuaUtilityDREvent}UtilityDREvent"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateEventTemplate", propOrder = {
    "eventTemplate"
})
public class UpdateEventTemplate
    implements Serializable
{

    /** The event template. */
    @XmlElement(required = true)
    protected UtilityDREvent eventTemplate;

    /**
     * Gets the value of the eventTemplate property.
     * 
     * @return the event template
     * 
     * possible object is
     * {@link UtilityDREvent }
     */
    public UtilityDREvent getEventTemplate() {
        return eventTemplate;
    }

    /**
     * Sets the value of the eventTemplate property.
     * 
     * @param value allowed object is
     * {@link UtilityDREvent }
     */
    public void setEventTemplate(UtilityDREvent value) {
        this.eventTemplate = value;
    }

}
