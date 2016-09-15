/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.optoutstate.OptOutState.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.optoutstate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * This is used by the Participant to opt out of DR Events.  It can be used to opt out of either an entire program or simply a single DR Event.
 * 
 * <p>Java class for OptOutState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OptOutState">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="DRASClients" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DRASClientID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="schedule" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="timeElement" maxOccurs="unbounded" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="end" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OptOutState", propOrder = {
    "participantID",
    "programName",
    "drasClients",
    "eventID",
    "schedule"
})
public class OptOutState
    implements Serializable
{

    /** The participant id. */
    @XmlElement(nillable = true)
    protected String participantID;
    
    /** The program name. */
    @XmlElement(nillable = true)
    protected String programName;
    
    /** The dras clients. */
    @XmlElement(name = "DRASClients", nillable = true)
    protected OptOutState.DRASClients drasClients;
    
    /** The event id. */
    @XmlElement(nillable = true)
    protected String eventID;
    
    /** The schedule. */
    @XmlElement(nillable = true)
    protected OptOutState.Schedule schedule;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The identifier. */
    @XmlAttribute
    protected String identifier;

    /**
     * Gets the value of the participantID property.
     * 
     * @return the participant id
     * 
     * possible object is
     * {@link String }
     */
    public String getParticipantID() {
        return participantID;
    }

    /**
     * Sets the value of the participantID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParticipantID(String value) {
        this.participantID = value;
    }

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
     * Gets the value of the drasClients property.
     * 
     * @return the DRAS clients
     * 
     * possible object is
     * {@link OptOutState.DRASClients }
     */
    public OptOutState.DRASClients getDRASClients() {
        return drasClients;
    }

    /**
     * Sets the value of the drasClients property.
     * 
     * @param value allowed object is
     * {@link OptOutState.DRASClients }
     */
    public void setDRASClients(OptOutState.DRASClients value) {
        this.drasClients = value;
    }

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
     * Gets the value of the schedule property.
     * 
     * @return the schedule
     * 
     * possible object is
     * {@link OptOutState.Schedule }
     */
    public OptOutState.Schedule getSchedule() {
        return schedule;
    }

    /**
     * Sets the value of the schedule property.
     * 
     * @param value allowed object is
     * {@link OptOutState.Schedule }
     */
    public void setSchedule(OptOutState.Schedule value) {
        this.schedule = value;
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
        return schemaVersion;
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
     * Gets the value of the identifier property.
     * 
     * @return the identifier
     * 
     * possible object is
     * {@link String }
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setIdentifier(String value) {
        this.identifier = value;
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
     * &lt;element name="DRASClientID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "drasClientID"
    })
    public static class DRASClients
        implements Serializable
    {

        /** The dras client id. */
        @XmlElement(name = "DRASClientID", nillable = true)
        protected List<String> drasClientID;

        /**
         * Gets the value of the drasClientID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the drasClientID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getDRASClientID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the DRAS client id
         */
        public List<String> getDRASClientID() {
            if (drasClientID == null) {
                drasClientID = new ArrayList<String>();
            }
            return this.drasClientID;
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
     * &lt;element name="timeElement" maxOccurs="unbounded" minOccurs="0">
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     * &lt;element name="end" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
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
    @XmlType(name = "", propOrder = {
        "timeElement"
    })
    public static class Schedule
        implements Serializable
    {

        /** The time element. */
        @XmlElement(nillable = true)
        protected List<OptOutState.Schedule.TimeElement> timeElement;

        /**
         * Gets the value of the timeElement property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the timeElement property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getTimeElement().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link OptOutState.Schedule.TimeElement }
         * 
         * @return the time element
         */
        public List<OptOutState.Schedule.TimeElement> getTimeElement() {
            if (timeElement == null) {
                timeElement = new ArrayList<OptOutState.Schedule.TimeElement>();
            }
            return this.timeElement;
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
         * &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         * &lt;element name="end" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         * &lt;/sequence>
         * &lt;/restriction>
         * &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "start",
            "end"
        })
        public static class TimeElement
            implements Serializable
        {

            /** The start. */
            @XmlElement(required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar start;
            
            /** The end. */
            @XmlElement(required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar end;

            /**
             * Gets the value of the start property.
             * 
             * @return the start
             * 
             * possible object is
             * {@link XMLGregorianCalendar }
             */
            public XMLGregorianCalendar getStart() {
                return start;
            }

            /**
             * Sets the value of the start property.
             * 
             * @param value allowed object is
             * {@link XMLGregorianCalendar }
             */
            public void setStart(XMLGregorianCalendar value) {
                this.start = value;
            }

            /**
             * Gets the value of the end property.
             * 
             * @return the end
             * 
             * possible object is
             * {@link XMLGregorianCalendar }
             */
            public XMLGregorianCalendar getEnd() {
                return end;
            }

            /**
             * Sets the value of the end property.
             * 
             * @param value allowed object is
             * {@link XMLGregorianCalendar }
             */
            public void setEnd(XMLGregorianCalendar value) {
                this.end = value;
            }

        }

    }

}
