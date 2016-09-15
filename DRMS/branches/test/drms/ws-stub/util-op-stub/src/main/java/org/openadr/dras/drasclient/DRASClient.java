/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.drasclient.DRASClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.drasclient;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.responseschedule.ResponseSchedule;
import org.openadr.dras.utilityprogram.ProgramInfo;


/**
 * This is the information that describes a DRAS Client.
 * The DRAS Client is the entity within a Participants
 * facility that communicates with the DRAS to receive DR
 * event information. A Participant may have more than one
 * DRAS Client associated with their Participant Account. A
 * DRAS Client may belong to only one Participant Account.
 * 
 * 
 * <p>Java class for DRASClient complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DRASClient">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="clientType" minOccurs="0">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="SIMPLE"/>
 * &lt;enumeration value="SMART"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;element name="onLine" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="programs" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="program" type="{http://www.openadr.org/DRAS/UtilityProgram}ProgramInfo" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="commsParams" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="connectionType">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="PUSH"/>
 * &lt;enumeration value="PULL"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;element name="clientURI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="clientAuthentication" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="pollingPeriod" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 * &lt;element name="retryCount" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 * &lt;element name="retryPeriod" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="locationInformation" type="{http://www.openadr.org/DRAS/DRASClient}Location" minOccurs="0"/>
 * &lt;element name="simpleClientResponseSchedules" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="responseSchedule" type="{http://www.openadr.org/DRAS/ResponseSchedule}ResponseSchedule" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="identifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="participantID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DRASClient", propOrder = {
    "clientType",
    "onLine",
    "programs",
    "commsParams",
    "locationInformation",
    "simpleClientResponseSchedules"
})
public class DRASClient
    implements Serializable
{

    /** The client type. */
    protected String clientType;
    
    /** The on line. */
    protected boolean onLine;
    
    /** The programs. */
    @XmlElement(nillable = true)
    protected DRASClient.Programs programs;
    
    /** The comms params. */
    protected DRASClient.CommsParams commsParams;
    
    /** The location information. */
    protected Location locationInformation;
    
    /** The simple client response schedules. */
    protected DRASClient.SimpleClientResponseSchedules simpleClientResponseSchedules;
    
    /** The identifier. */
    @XmlAttribute(required = true)
    protected String identifier;
    
    /** The participant id. */
    @XmlAttribute(required = true)
    protected String participantID;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;

    /**
     * Gets the value of the clientType property.
     * 
     * @return the client type
     * 
     * possible object is
     * {@link String }
     */
    public String getClientType() {
        return clientType;
    }

    /**
     * Sets the value of the clientType property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setClientType(String value) {
        this.clientType = value;
    }

    /**
     * Gets the value of the onLine property.
     * 
     * @return true, if checks if is on line
     */
    public boolean isOnLine() {
        return onLine;
    }

    /**
     * Sets the value of the onLine property.
     * 
     * @param value the value
     */
    public void setOnLine(boolean value) {
        this.onLine = value;
    }

    /**
     * Gets the value of the programs property.
     * 
     * @return the programs
     * 
     * possible object is
     * {@link DRASClient.Programs }
     */
    public DRASClient.Programs getPrograms() {
        return programs;
    }

    /**
     * Sets the value of the programs property.
     * 
     * @param value allowed object is
     * {@link DRASClient.Programs }
     */
    public void setPrograms(DRASClient.Programs value) {
        this.programs = value;
    }

    /**
     * Gets the value of the commsParams property.
     * 
     * @return the comms params
     * 
     * possible object is
     * {@link DRASClient.CommsParams }
     */
    public DRASClient.CommsParams getCommsParams() {
        return commsParams;
    }

    /**
     * Sets the value of the commsParams property.
     * 
     * @param value allowed object is
     * {@link DRASClient.CommsParams }
     */
    public void setCommsParams(DRASClient.CommsParams value) {
        this.commsParams = value;
    }

    /**
     * Gets the value of the locationInformation property.
     * 
     * @return the location information
     * 
     * possible object is
     * {@link Location }
     */
    public Location getLocationInformation() {
        return locationInformation;
    }

    /**
     * Sets the value of the locationInformation property.
     * 
     * @param value allowed object is
     * {@link Location }
     */
    public void setLocationInformation(Location value) {
        this.locationInformation = value;
    }

    /**
     * Gets the value of the simpleClientResponseSchedules property.
     * 
     * @return the simple client response schedules
     * 
     * possible object is
     * {@link DRASClient.SimpleClientResponseSchedules }
     */
    public DRASClient.SimpleClientResponseSchedules getSimpleClientResponseSchedules() {
        return simpleClientResponseSchedules;
    }

    /**
     * Sets the value of the simpleClientResponseSchedules property.
     * 
     * @param value allowed object is
     * {@link DRASClient.SimpleClientResponseSchedules }
     */
    public void setSimpleClientResponseSchedules(DRASClient.SimpleClientResponseSchedules value) {
        this.simpleClientResponseSchedules = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="connectionType">
     * &lt;simpleType>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     * &lt;enumeration value="PUSH"/>
     * &lt;enumeration value="PULL"/>
     * &lt;/restriction>
     * &lt;/simpleType>
     * &lt;/element>
     * &lt;element name="clientURI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     * &lt;element name="clientAuthentication" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     * &lt;element name="pollingPeriod" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
     * &lt;element name="retryCount" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
     * &lt;element name="retryPeriod" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "connectionType",
        "clientURI",
        "clientAuthentication",
        "pollingPeriod",
        "retryCount",
        "retryPeriod"
    })
    public static class CommsParams
        implements Serializable
    {

        /** The connection type. */
        @XmlElement(required = true)
        protected String connectionType;
        
        /** The client uri. */
        protected String clientURI;
        
        /** The client authentication. */
        protected String clientAuthentication;
        
        /** The polling period. */
        @XmlSchemaType(name = "unsignedLong")
        protected BigInteger pollingPeriod;
        
        /** The retry count. */
        @XmlSchemaType(name = "unsignedInt")
        protected Long retryCount;
        
        /** The retry period. */
        @XmlSchemaType(name = "unsignedLong")
        protected BigInteger retryPeriod;

        /**
         * Gets the value of the connectionType property.
         * 
         * @return the connection type
         * 
         * possible object is
         * {@link String }
         */
        public String getConnectionType() {
            return connectionType;
        }

        /**
         * Sets the value of the connectionType property.
         * 
         * @param value allowed object is
         * {@link String }
         */
        public void setConnectionType(String value) {
            this.connectionType = value;
        }

        /**
         * Gets the value of the clientURI property.
         * 
         * @return the client uri
         * 
         * possible object is
         * {@link String }
         */
        public String getClientURI() {
            return clientURI;
        }

        /**
         * Sets the value of the clientURI property.
         * 
         * @param value allowed object is
         * {@link String }
         */
        public void setClientURI(String value) {
            this.clientURI = value;
        }

        /**
         * Gets the value of the clientAuthentication property.
         * 
         * @return the client authentication
         * 
         * possible object is
         * {@link String }
         */
        public String getClientAuthentication() {
            return clientAuthentication;
        }

        /**
         * Sets the value of the clientAuthentication property.
         * 
         * @param value allowed object is
         * {@link String }
         */
        public void setClientAuthentication(String value) {
            this.clientAuthentication = value;
        }

        /**
         * Gets the value of the pollingPeriod property.
         * 
         * @return the polling period
         * 
         * possible object is
         * {@link BigInteger }
         */
        public BigInteger getPollingPeriod() {
            return pollingPeriod;
        }

        /**
         * Sets the value of the pollingPeriod property.
         * 
         * @param value allowed object is
         * {@link BigInteger }
         */
        public void setPollingPeriod(BigInteger value) {
            this.pollingPeriod = value;
        }

        /**
         * Gets the value of the retryCount property.
         * 
         * @return the retry count
         * 
         * possible object is
         * {@link Long }
         */
        public Long getRetryCount() {
            return retryCount;
        }

        /**
         * Sets the value of the retryCount property.
         * 
         * @param value allowed object is
         * {@link Long }
         */
        public void setRetryCount(Long value) {
            this.retryCount = value;
        }

        /**
         * Gets the value of the retryPeriod property.
         * 
         * @return the retry period
         * 
         * possible object is
         * {@link BigInteger }
         */
        public BigInteger getRetryPeriod() {
            return retryPeriod;
        }

        /**
         * Sets the value of the retryPeriod property.
         * 
         * @param value allowed object is
         * {@link BigInteger }
         */
        public void setRetryPeriod(BigInteger value) {
            this.retryPeriod = value;
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
     * &lt;element name="program" type="{http://www.openadr.org/DRAS/UtilityProgram}ProgramInfo" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "program"
    })
    public static class Programs
        implements Serializable
    {

        /** The program. */
        @XmlElement(nillable = true)
        protected List<ProgramInfo> program;

        /**
         * Gets the value of the program property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the program property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getProgram().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProgramInfo }
         * 
         * @return the program
         */
        public List<ProgramInfo> getProgram() {
            if (program == null) {
                program = new ArrayList<ProgramInfo>();
            }
            return this.program;
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
     * &lt;element name="responseSchedule" type="{http://www.openadr.org/DRAS/ResponseSchedule}ResponseSchedule" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "responseSchedule"
    })
    public static class SimpleClientResponseSchedules
        implements Serializable
    {

        /** The response schedule. */
        protected List<ResponseSchedule> responseSchedule;

        /**
         * Gets the value of the responseSchedule property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the responseSchedule property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getResponseSchedule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ResponseSchedule }
         * 
         * @return the response schedule
         */
        public List<ResponseSchedule> getResponseSchedule() {
            if (responseSchedule == null) {
                responseSchedule = new ArrayList<ResponseSchedule>();
            }
            return this.responseSchedule;
        }

    }

}
