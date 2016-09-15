/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventinfo.EventInfoType.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * When a DR Event is issued by the Utility/ISO there is so
 * called "Event Info" that accompanies the DR Event and is
 * valid for the period of time that the DR Event is
 * active. The Event Info can be of various types depending
 * on the requirements of the DR program. The supported
 * types are defined by the enumerations found in the
 * EventInfoTypeID
 * 
 * Part of setting up a DR program in the DRAS is
 * specifying what Event Info Types may be sent as part of
 * issuing a DR Event. There are various attributes
 * associated with each Event Info type that may be
 * configured when the DR program is set up. These
 * attributes define the range of values that may be sent
 * as part of the Event Info that is associated with a DR
 * Event.
 * 
 * In addition each Event Info Type may have a schedule
 * associated with it that defines a schedule of values
 * that cover the period when the DR Event is active. A
 * schedule is defined by a set of "time slots" within the
 * DR Events active period. The slots are defined using the
 * attributes in the scheduleType element as well as the
 * time slots in the schedule element.
 * 
 * 
 * 
 * <p>Java class for EventInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventInfoType">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="scheduleType">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="NONE"/>
 * &lt;enumeration value="DYNAMIC"/>
 * &lt;enumeration value="STATIC"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;element name="schedule" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="timeSlot" type="{http://www.w3.org/2001/XMLSchema}double" maxOccurs="unbounded"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="enumerations" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="typeID" use="required" type="{http://www.openadr.org/DRAS/EventInfo}EventInfoTypeID" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventInfoType", propOrder = {
    "scheduleType",
    "schedule",
    "enumerations",
    "minValue",
    "maxValue"
})
public class EventInfoType
    implements Serializable
{

    /** The schedule type. */
    @XmlElement(required = true)
    protected String scheduleType;
    
    /** The schedule. */
    protected EventInfoType.Schedule schedule;
    
    /** The enumerations. */
    @XmlElement(nillable = true)
    protected EventInfoType.Enumerations enumerations;
    
    /** The min value. */
    protected Double minValue;
    
    /** The max value. */
    protected Double maxValue;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The name. */
    @XmlAttribute(required = true)
    protected String name;
    
    /** The type id. */
    @XmlAttribute(required = true)
    protected EventInfoTypeID typeID;

    /**
     * Gets the value of the scheduleType property.
     * 
     * @return the schedule type
     * 
     * possible object is
     * {@link String }
     */
    public String getScheduleType() {
        return scheduleType;
    }

    /**
     * Sets the value of the scheduleType property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setScheduleType(String value) {
        this.scheduleType = value;
    }

    /**
     * Gets the value of the schedule property.
     * 
     * @return the schedule
     * 
     * possible object is
     * {@link EventInfoType.Schedule }
     */
    public EventInfoType.Schedule getSchedule() {
        return schedule;
    }

    /**
     * Sets the value of the schedule property.
     * 
     * @param value allowed object is
     * {@link EventInfoType.Schedule }
     */
    public void setSchedule(EventInfoType.Schedule value) {
        this.schedule = value;
    }

    /**
     * Gets the value of the enumerations property.
     * 
     * @return the enumerations
     * 
     * possible object is
     * {@link EventInfoType.Enumerations }
     */
    public EventInfoType.Enumerations getEnumerations() {
        return enumerations;
    }

    /**
     * Sets the value of the enumerations property.
     * 
     * @param value allowed object is
     * {@link EventInfoType.Enumerations }
     */
    public void setEnumerations(EventInfoType.Enumerations value) {
        this.enumerations = value;
    }

    /**
     * Gets the value of the minValue property.
     * 
     * @return the min value
     * 
     * possible object is
     * {@link Double }
     */
    public Double getMinValue() {
        return minValue;
    }

    /**
     * Sets the value of the minValue property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setMinValue(Double value) {
        this.minValue = value;
    }

    /**
     * Gets the value of the maxValue property.
     * 
     * @return the max value
     * 
     * possible object is
     * {@link Double }
     */
    public Double getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the value of the maxValue property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setMaxValue(Double value) {
        this.maxValue = value;
    }

    /**
     * Gets the value of the schemaVersion property.
     * 
     * @return the schema version
     * 
     * possible object is
     * {@link String }
     */
    public String getSchemaVersion() {
        if (schemaVersion == null) {
            return "1.0";
        } else {
            return schemaVersion;
        }
    }

    /**
     * Sets the value of the schemaVersion property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setSchemaVersion(String value) {
        this.schemaVersion = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return the name
     * 
     * possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the typeID property.
     * 
     * @return the type id
     * 
     * possible object is
     * {@link EventInfoTypeID }
     */
    public EventInfoTypeID getTypeID() {
        return typeID;
    }

    /**
     * Sets the value of the typeID property.
     * 
     * @param value allowed object is
     * {@link EventInfoTypeID }
     */
    public void setTypeID(EventInfoTypeID value) {
        this.typeID = value;
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
     * &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Enumerations
        implements Serializable
    {

        /** The value. */
        @XmlElement(nillable = true)
        protected List<Double> value;

        /**
         * Gets the value of the value property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the value property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getValue().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * @return the value
         */
        public List<Double> getValue() {
            if (value == null) {
                value = new ArrayList<Double>();
            }
            return this.value;
        }

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
     * &lt;element name="timeSlot" type="{http://www.w3.org/2001/XMLSchema}double" maxOccurs="unbounded"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "timeSlot"
    })
    public static class Schedule
        implements Serializable
    {

        /** The time slot. */
        @XmlElement(type = Double.class)
        protected List<Double> timeSlot;

        /**
         * Gets the value of the timeSlot property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the timeSlot property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getTimeSlot().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * @return the time slot
         */
        public List<Double> getTimeSlot() {
            if (timeSlot == null) {
                timeSlot = new ArrayList<Double>();
            }
            return this.timeSlot;
        }

    }

}
