/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventstate.EventState.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventstate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents the state of a single DR Event.  There will be one of these senT for each DR Event that is either pending or active.  If there are no DR Events pending or active then there will be one of these sent with the EventStatus field of the simpleDRModeData set to "NONE".  Note that when the EventStatus is set to NONE then some of the elements and attributes of this entity are not applicable.  See the documentation for each element and attribute to see which are not applicable when the EventStatus is set to NONE.
 * 
 * 
 * <p>Java class for EventState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventState">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="simpleDRModeData" type="{http://www.openadr.org/DRAS/EventState}SimpleClientEventData"/>
 * &lt;element name="drEventData" type="{http://www.openadr.org/DRAS/EventState}SmartClientDREventData" minOccurs="0"/>
 * &lt;element name="customData" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="generalInfoInstances" type="{http://www.openadr.org/DRAS/EventState}GeneralInfoInstance" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="programName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="eventModNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 * &lt;attribute name="eventIdentifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="drasClientID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="eventStateID" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;attribute name="drasName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="testEvent" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 * &lt;attribute name="offLine" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventState", propOrder = {
    "simpleDRModeData",
    "drEventData",
    "customData"
})
public class EventState
    implements Serializable
{

    /** The simple dr mode data. */
    @XmlElement(required = true)
    protected SimpleClientEventData simpleDRModeData;
    
    /** The dr event data. */
    @XmlElement(nillable = true)
    protected SmartClientDREventData drEventData;
    
    /** The custom data. */
    @XmlElement(nillable = true)
    protected EventState.CustomData customData;
    
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
    @XmlSchemaType(name = "unsignedInt")
    protected long eventStateID;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The dras name. */
    @XmlAttribute(required = true)
    protected String drasName;
    
    /** The test event. */
    @XmlAttribute
    protected Boolean testEvent;
    
    /** The off line. */
    @XmlAttribute
    protected Boolean offLine;

    /**
     * Gets the value of the simpleDRModeData property.
     * 
     * @return the simple dr mode data
     * 
     * possible object is
     * {@link SimpleClientEventData }
     */
    public SimpleClientEventData getSimpleDRModeData() {
        return simpleDRModeData;
    }

    /**
     * Sets the value of the simpleDRModeData property.
     * 
     * @param value allowed object is
     * {@link SimpleClientEventData }
     */
    public void setSimpleDRModeData(SimpleClientEventData value) {
        this.simpleDRModeData = value;
    }

    /**
     * Gets the value of the drEventData property.
     * 
     * @return the dr event data
     * 
     * possible object is
     * {@link SmartClientDREventData }
     */
    public SmartClientDREventData getDrEventData() {
        return drEventData;
    }

    /**
     * Sets the value of the drEventData property.
     * 
     * @param value allowed object is
     * {@link SmartClientDREventData }
     */
    public void setDrEventData(SmartClientDREventData value) {
        this.drEventData = value;
    }

    /**
     * Gets the value of the customData property.
     * 
     * @return the custom data
     * 
     * possible object is
     * {@link EventState.CustomData }
     */
    public EventState.CustomData getCustomData() {
        return customData;
    }

    /**
     * Sets the value of the customData property.
     * 
     * @param value allowed object is
     * {@link EventState.CustomData }
     */
    public void setCustomData(EventState.CustomData value) {
        this.customData = value;
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
     */
    public long getEventStateID() {
        return eventStateID;
    }

    /**
     * Sets the value of the eventStateID property.
     * 
     * @param value the value
     */
    public void setEventStateID(long value) {
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

    /**
     * Gets the value of the testEvent property.
     * 
     * @return true, if checks if is test event
     * 
     * possible object is
     * {@link Boolean }
     */
    public Boolean isTestEvent() {
        return testEvent;
    }

    /**
     * Sets the value of the testEvent property.
     * 
     * @param value allowed object is
     * {@link Boolean }
     */
    public void setTestEvent(Boolean value) {
        this.testEvent = value;
    }

    /**
     * Gets the value of the offLine property.
     * 
     * @return true, if checks if is off line
     * 
     * possible object is
     * {@link Boolean }
     */
    public Boolean isOffLine() {
        return offLine;
    }

    /**
     * Sets the value of the offLine property.
     * 
     * @param value allowed object is
     * {@link Boolean }
     */
    public void setOffLine(Boolean value) {
        this.offLine = value;
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
     * &lt;element name="generalInfoInstances" type="{http://www.openadr.org/DRAS/EventState}GeneralInfoInstance" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "generalInfoInstances"
    })
    public static class CustomData
        implements Serializable
    {

        /** The general info instances. */
        @XmlElement(nillable = true)
        protected List<GeneralInfoInstance> generalInfoInstances;

        /**
         * Gets the value of the generalInfoInstances property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the generalInfoInstances property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getGeneralInfoInstances().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GeneralInfoInstance }
         * 
         * @return the general info instances
         */
        public List<GeneralInfoInstance> getGeneralInfoInstances() {
            if (generalInfoInstances == null) {
                generalInfoInstances = new ArrayList<GeneralInfoInstance>();
            }
            return this.generalInfoInstances;
        }

    }

}
