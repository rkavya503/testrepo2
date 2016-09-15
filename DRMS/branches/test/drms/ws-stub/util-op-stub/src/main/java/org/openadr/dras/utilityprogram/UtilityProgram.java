/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityprogram.UtilityProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityprogram;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.eventinfo.EventInfoType;
import org.openadr.dras.programconstraint.ProgramConstraint;


/**
 * This represents all the information associated with a DR
 * program that is created by a Utility or ISO. Each
 * program has a set of attributes that describe how the
 * program is managed and run from the point of view of the
 * DRAS and Participants. The attributes include the
 * following type of information: Time and date
 * attributes which specify when a DR event can be issued.
 * Type of events that can be issued List of
 * participants that participate in the program.
 * Parameters that control how bidding is performed by a
 * Participant as part of the program
 * 
 * 
 * 
 * <p>Java class for UtilityProgram complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UtilityProgram">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programConstraints" type="{http://www.openadr.org/DRAS/ProgramConstraint}ProgramConstraint" minOccurs="0"/>
 * &lt;element name="biddingConfiguration" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="submitImmediate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="submitTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;element name="fixedPrice" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="fixedSchedule" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="fixedLoad" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="eventInfoTypes" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventInfoType" type="{http://www.openadr.org/DRAS/EventInfo}EventInfoType" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="participants" type="{http://www.openadr.org/DRAS/UtilityProgram}ParticipantList" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;attribute name="priority" type="{http://www.w3.org/2001/XMLSchema}integer" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UtilityProgram", propOrder = {
    "programConstraints",
    "biddingConfiguration",
    "eventInfoTypes",
    "participants"
})
public class UtilityProgram
    implements Serializable
{

    /** The program constraints. */
    @XmlElement(nillable = true)
    protected ProgramConstraint programConstraints;
    
    /** The bidding configuration. */
    @XmlElement(nillable = true)
    protected UtilityProgram.BiddingConfiguration biddingConfiguration;
    
    /** The event info types. */
    @XmlElement(nillable = true)
    protected UtilityProgram.EventInfoTypes eventInfoTypes;
    
    /** The participants. */
    @XmlElement(nillable = true)
    protected ParticipantList participants;
    
    /** The name. */
    @XmlAttribute(required = true)
    protected String name;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The priority. */
    @XmlAttribute
    protected BigInteger priority;

    /**
     * Gets the value of the programConstraints property.
     * 
     * @return the program constraints
     * 
     * possible object is
     * {@link ProgramConstraint }
     */
    public ProgramConstraint getProgramConstraints() {
        return programConstraints;
    }

    /**
     * Sets the value of the programConstraints property.
     * 
     * @param value allowed object is
     * {@link ProgramConstraint }
     */
    public void setProgramConstraints(ProgramConstraint value) {
        this.programConstraints = value;
    }

    /**
     * Gets the value of the biddingConfiguration property.
     * 
     * @return the bidding configuration
     * 
     * possible object is
     * {@link UtilityProgram.BiddingConfiguration }
     */
    public UtilityProgram.BiddingConfiguration getBiddingConfiguration() {
        return biddingConfiguration;
    }

    /**
     * Sets the value of the biddingConfiguration property.
     * 
     * @param value allowed object is
     * {@link UtilityProgram.BiddingConfiguration }
     */
    public void setBiddingConfiguration(UtilityProgram.BiddingConfiguration value) {
        this.biddingConfiguration = value;
    }

    /**
     * Gets the value of the eventInfoTypes property.
     * 
     * @return the event info types
     * 
     * possible object is
     * {@link UtilityProgram.EventInfoTypes }
     */
    public UtilityProgram.EventInfoTypes getEventInfoTypes() {
        return eventInfoTypes;
    }

    /**
     * Sets the value of the eventInfoTypes property.
     * 
     * @param value allowed object is
     * {@link UtilityProgram.EventInfoTypes }
     */
    public void setEventInfoTypes(UtilityProgram.EventInfoTypes value) {
        this.eventInfoTypes = value;
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
     * Gets the value of the priority property.
     * 
     * @return the priority
     * 
     * possible object is
     * {@link BigInteger }
     */
    public BigInteger getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value allowed object is
     * {@link BigInteger }
     */
    public void setPriority(BigInteger value) {
        this.priority = value;
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
     * &lt;element name="submitImmediate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     * &lt;element name="submitTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
     * &lt;element name="fixedPrice" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     * &lt;element name="fixedSchedule" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     * &lt;element name="fixedLoad" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "submitImmediate",
        "submitTime",
        "fixedPrice",
        "fixedSchedule",
        "fixedLoad"
    })
    public static class BiddingConfiguration
        implements Serializable
    {

        /** The submit immediate. */
        protected boolean submitImmediate;
        
        /** The submit time. */
        protected double submitTime;
        
        /** The fixed price. */
        protected boolean fixedPrice;
        
        /** The fixed schedule. */
        protected boolean fixedSchedule;
        
        /** The fixed load. */
        protected boolean fixedLoad;

        /**
         * Gets the value of the submitImmediate property.
         * 
         * @return true, if checks if is submit immediate
         */
        public boolean isSubmitImmediate() {
            return submitImmediate;
        }

        /**
         * Sets the value of the submitImmediate property.
         * 
         * @param value the value
         */
        public void setSubmitImmediate(boolean value) {
            this.submitImmediate = value;
        }

        /**
         * Gets the value of the submitTime property.
         * 
         * @return the submit time
         */
        public double getSubmitTime() {
            return submitTime;
        }

        /**
         * Sets the value of the submitTime property.
         * 
         * @param value the value
         */
        public void setSubmitTime(double value) {
            this.submitTime = value;
        }

        /**
         * Gets the value of the fixedPrice property.
         * 
         * @return true, if checks if is fixed price
         */
        public boolean isFixedPrice() {
            return fixedPrice;
        }

        /**
         * Sets the value of the fixedPrice property.
         * 
         * @param value the value
         */
        public void setFixedPrice(boolean value) {
            this.fixedPrice = value;
        }

        /**
         * Gets the value of the fixedSchedule property.
         * 
         * @return true, if checks if is fixed schedule
         */
        public boolean isFixedSchedule() {
            return fixedSchedule;
        }

        /**
         * Sets the value of the fixedSchedule property.
         * 
         * @param value the value
         */
        public void setFixedSchedule(boolean value) {
            this.fixedSchedule = value;
        }

        /**
         * Gets the value of the fixedLoad property.
         * 
         * @return true, if checks if is fixed load
         */
        public boolean isFixedLoad() {
            return fixedLoad;
        }

        /**
         * Sets the value of the fixedLoad property.
         * 
         * @param value the value
         */
        public void setFixedLoad(boolean value) {
            this.fixedLoad = value;
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
     * &lt;element name="eventInfoType" type="{http://www.openadr.org/DRAS/EventInfo}EventInfoType" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "eventInfoType"
    })
    public static class EventInfoTypes
        implements Serializable
    {

        /** The event info type. */
        @XmlElement(nillable = true)
        protected List<EventInfoType> eventInfoType;

        /**
         * Gets the value of the eventInfoType property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the eventInfoType property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getEventInfoType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link EventInfoType }
         * 
         * @return the event info type
         */
        public List<EventInfoType> getEventInfoType() {
            if (eventInfoType == null) {
                eventInfoType = new ArrayList<EventInfoType>();
            }
            return this.eventInfoType;
        }

    }

}
