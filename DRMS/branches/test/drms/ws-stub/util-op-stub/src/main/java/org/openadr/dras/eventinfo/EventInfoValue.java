/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventinfo.EventInfoValue.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventinfo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * This is used to specify both an Event Info value and
 * possibly an accompanying time period for that value. If
 * the time period is left unspecified then the value
 * correpsonds to the schedule that was defined when the
 * EventInfoType was defined.
 * 
 * 
 * <p>Java class for EventInfoValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventInfoValue">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventInfoValue", propOrder = {
    "value",
    "startTime"
})
public class EventInfoValue
    implements Serializable
{

    /** The value. */
    protected double value;
    
    /** The start time. */
    protected Double startTime;

    /**
     * Gets the value of the value property.
     * 
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value the value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Gets the value of the startTime property.
     * 
     * @return the start time
     * 
     * possible object is
     * {@link Double }
     */
    public Double getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setStartTime(Double value) {
        this.startTime = value;
    }

}
