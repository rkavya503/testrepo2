/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventstate.EventStateConfirmation.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventstate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents the message that is sent from the DRAS Client to the DRAS upon receiving an EventState message.
 * 
 * 
 * <p>Java class for EventStateConfirmation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventStateConfirmation">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;attribute name="programName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="eventModNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 * &lt;attribute name="eventIdentifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="drasClientID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="eventStateID" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 * &lt;attribute name="schemaVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="20080509" />
 * &lt;attribute name="operationModeValue">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="NORMAL"/>
 * &lt;enumeration value="MODERATE"/>
 * &lt;enumeration value="HIGH"/>
 * &lt;enumeration value="SPECIAL"/>
 * &lt;enumeration value="UNKNOWN"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/attribute>
 * &lt;attribute name="optInStatus" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 * &lt;attribute name="currentTime" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 * &lt;attribute name="signature" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="drasName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventStateConfirmation")
public class EventStateConfirmation
    implements Serializable
{

    /** The program name. */
    @XmlAttribute(required = true)
    protected String programName;
    
    /** The event mod number. */
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "unsignedInt")
    protected long eventModNumber;
    
    /** The event identifier. */
    @XmlAttribute(required = true)
    protected String eventIdentifier;
    
    /** The dras client id. */
    @XmlAttribute(required = true)
    protected String drasClientID;
    
    /** The event state id. */
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger eventStateID;
    
    /** The schema version. */
    @XmlAttribute(required = true)
    protected String schemaVersion;
    
    /** The operation mode value. */
    @XmlAttribute
    protected String operationModeValue;
    
    /** The opt in status. */
    @XmlAttribute
    protected Boolean optInStatus;
    
    /** The current time. */
    @XmlAttribute
    protected BigDecimal currentTime;
    
    /** The signature. */
    @XmlAttribute
    protected String signature;
    
    /** The dras name. */
    @XmlAttribute(required = true)
    protected String drasName;

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
     * Gets the value of the eventModNumber property.
     * 
     * @return the event mod number
     */
    public long getEventModNumber() {
        return eventModNumber;
    }

    /**
     * Sets the value of the eventModNumber property.
     * 
     * @param value the value
     */
    public void setEventModNumber(long value) {
        this.eventModNumber = value;
    }

    /**
     * Gets the value of the eventIdentifier property.
     * 
     * @return the event identifier
     * 
     * possible object is
     * {@link String }
     */
    public String getEventIdentifier() {
        return eventIdentifier;
    }

    /**
     * Sets the value of the eventIdentifier property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setEventIdentifier(String value) {
        this.eventIdentifier = value;
    }

    /**
     * Gets the value of the drasClientID property.
     * 
     * @return the dras client id
     * 
     * possible object is
     * {@link String }
     */
    public String getDrasClientID() {
        return drasClientID;
    }

    /**
     * Sets the value of the drasClientID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDrasClientID(String value) {
        this.drasClientID = value;
    }

    /**
     * Gets the value of the eventStateID property.
     * 
     * @return the event state id
     * 
     * possible object is
     * {@link BigInteger }
     */
    public BigInteger getEventStateID() {
        return eventStateID;
    }

    /**
     * Sets the value of the eventStateID property.
     * 
     * @param value allowed object is
     * {@link BigInteger }
     */
    public void setEventStateID(BigInteger value) {
        this.eventStateID = value;
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
            return "20080509";
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
     * Gets the value of the operationModeValue property.
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
     * Gets the value of the optInStatus property.
     * 
     * @return true, if checks if is opt in status
     * 
     * possible object is
     * {@link Boolean }
     */
    public Boolean isOptInStatus() {
        return optInStatus;
    }

    /**
     * Sets the value of the optInStatus property.
     * 
     * @param value allowed object is
     * {@link Boolean }
     */
    public void setOptInStatus(Boolean value) {
        this.optInStatus = value;
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
     * Gets the value of the signature property.
     * 
     * @return the signature
     * 
     * possible object is
     * {@link String }
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setSignature(String value) {
        this.signature = value;
    }

    /**
     * Gets the value of the drasName property.
     * 
     * @return the dras name
     * 
     * possible object is
     * {@link String }
     */
    public String getDrasName() {
        return drasName;
    }

    /**
     * Sets the value of the drasName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDrasName(String value) {
        this.drasName = value;
    }

}
