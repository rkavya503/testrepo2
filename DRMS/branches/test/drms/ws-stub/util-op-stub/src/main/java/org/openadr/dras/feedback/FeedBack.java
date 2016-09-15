/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.feedback.FeedBack.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.feedback;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * This represents feedback from a Participant that
 * typically pertains to a particular DR Event, but can be
 * specified to correspond to a wide variety of entities
 * such as UtilityProgram, DRASClient, and
 * ParticipantAccount. This feedback is not used by the
 * DRAS, but can be queried and used by the Utility.
 * 
 * 
 * <p>Java class for FeedBack complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FeedBack">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="DRASClientID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="feedback">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="name">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="SHED_ATTEMPTED"/>
 * &lt;enumeration value="CURRENT_SHED_LEVEL"/>
 * &lt;enumeration value="CURRENT_USAGE"/>
 * &lt;enumeration value="SHED_AVAILABLE"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="timeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FeedBack", propOrder = {
    "programName",
    "participantID",
    "drasClientID",
    "eventID",
    "feedback",
    "timeStamp"
})
public class FeedBack
    implements Serializable
{

    /** The program name. */
    @XmlElement(nillable = true)
    protected String programName;
    
    /** The participant id. */
    @XmlElement(nillable = true)
    protected String participantID;
    
    /** The dras client id. */
    @XmlElement(name = "DRASClientID", nillable = true)
    protected String drasClientID;
    
    /** The event id. */
    @XmlElement(nillable = true)
    protected String eventID;
    
    /** The feedback. */
    @XmlElement(required = true)
    protected FeedBack.Feedback feedback;
    
    /** The time stamp. */
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeStamp;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;

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
     * Gets the value of the drasClientID property.
     * 
     * @return the DRAS client id
     * 
     * possible object is
     * {@link String }
     */
    public String getDRASClientID() {
        return drasClientID;
    }

    /**
     * Sets the value of the drasClientID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDRASClientID(String value) {
        this.drasClientID = value;
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
     * Gets the value of the feedback property.
     * 
     * @return the feedback
     * 
     * possible object is
     * {@link FeedBack.Feedback }
     */
    public FeedBack.Feedback getFeedback() {
        return feedback;
    }

    /**
     * Sets the value of the feedback property.
     * 
     * @param value allowed object is
     * {@link FeedBack.Feedback }
     */
    public void setFeedback(FeedBack.Feedback value) {
        this.feedback = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return the time stamp
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setTimeStamp(XMLGregorianCalendar value) {
        this.timeStamp = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="name">
     * &lt;simpleType>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     * &lt;enumeration value="SHED_ATTEMPTED"/>
     * &lt;enumeration value="CURRENT_SHED_LEVEL"/>
     * &lt;enumeration value="CURRENT_USAGE"/>
     * &lt;enumeration value="SHED_AVAILABLE"/>
     * &lt;/restriction>
     * &lt;/simpleType>
     * &lt;/element>
     * &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "name",
        "value"
    })
    public static class Feedback
        implements Serializable
    {

        /** The name. */
        @XmlElement(required = true)
        protected String name;
        
        /** The value. */
        @XmlElement(required = true)
        protected String value;

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
         * Gets the value of the value property.
         * 
         * @return the value
         * 
         * possible object is
         * {@link String }
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value allowed object is
         * {@link String }
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

}
