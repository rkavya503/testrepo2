/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventstate.SimpleClientEventData.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventstate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents a simple representation of the DR Event information.  The information represented here was translated from complete DR Event information represented in the SmartClientDREventData into a sequence of simple operation modes and states so that it can more easily be used by Simple DRAS Clients.
 * 
 * 
 * <p>Java class for SimpleClientEventData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimpleClientEventData">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element ref="{http://www.openadr.org/DRAS/EventState}EventStatus"/>
 * &lt;element ref="{http://www.openadr.org/DRAS/EventState}OperationModeValue"/>
 * &lt;element name="currentTime" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 * &lt;element name="operationModeSchedule" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="modeSlot" type="{http://www.openadr.org/DRAS/EventState}OperationState" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleClientEventData", propOrder = {
    "eventStatus",
    "operationModeValue",
    "currentTime",
    "operationModeSchedule"
})
public class SimpleClientEventData
    implements Serializable
{

    /** The event status. */
    @XmlElement(name = "EventStatus", required = true)
    protected String eventStatus;
    
    /** The operation mode value. */
    @XmlElement(name = "OperationModeValue", required = true)
    protected String operationModeValue;
    
    /** The current time. */
    @XmlElement(required = true)
    protected BigDecimal currentTime;
    
    /** The operation mode schedule. */
    @XmlElement(nillable = true)
    protected SimpleClientEventData.OperationModeSchedule operationModeSchedule;

    /**
     * This is the DR event status that is valid at the time specified by the currentTime field.
     * 
     * @return the event status
     * 
     * possible object is
     * {@link String }
     */
    public String getEventStatus() {
        return eventStatus;
    }

    /**
     * Sets the value of the eventStatus property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setEventStatus(String value) {
        this.eventStatus = value;
    }

    /**
     * This is the operation mode value that is valid at the time specified by the currentTime field.
     * 
     * @return the operation mode value
     * 
     * possible object is
     * {@link String }
     */
    public String getOperationModeValue() {
        return operationModeValue;
    }

    /**
     * Sets the value of the operationModeValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setOperationModeValue(String value) {
        this.operationModeValue = value;
    }

    /**
     * Gets the value of the currentTime property.
     * 
     * @return the current time
     * 
     * possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getCurrentTime() {
        return currentTime;
    }

    /**
     * Sets the value of the currentTime property.
     * 
     * @param value allowed object is
     * {@link BigDecimal }
     */
    public void setCurrentTime(BigDecimal value) {
        this.currentTime = value;
    }

    /**
     * Gets the value of the operationModeSchedule property.
     * 
     * @return the operation mode schedule
     * 
     * possible object is
     * {@link SimpleClientEventData.OperationModeSchedule }
     */
    public SimpleClientEventData.OperationModeSchedule getOperationModeSchedule() {
        return operationModeSchedule;
    }

    /**
     * Sets the value of the operationModeSchedule property.
     * 
     * @param value allowed object is
     * {@link SimpleClientEventData.OperationModeSchedule }
     */
    public void setOperationModeSchedule(SimpleClientEventData.OperationModeSchedule value) {
        this.operationModeSchedule = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="modeSlot" type="{http://www.openadr.org/DRAS/EventState}OperationState" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "modeSlot"
    })
    public static class OperationModeSchedule
        implements Serializable
    {

        /** The mode slot. */
        @XmlElement(nillable = true)
        protected List<OperationState> modeSlot;

        /**
         * Gets the value of the modeSlot property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the modeSlot property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getModeSlot().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link OperationState }
         * 
         * @return the mode slot
         */
        public List<OperationState> getModeSlot() {
            if (modeSlot == null) {
                modeSlot = new ArrayList<OperationState>();
            }
            return this.modeSlot;
        }

    }

}
