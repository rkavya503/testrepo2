/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuaeventinfo.EventInfoInstance.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuaeventinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Whereas the EventInfoType is used to define what type of
 * informaiton may be sent as part of a DR event the
 * EventInfoInstance is an actual instantiation of that
 * information.
 * 
 * 
 * <p>Java class for EventInfoInstance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventInfoInstance">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="values" type="{http://www.openadr.org/DRAS/AkuaEventInfo}Values" minOccurs="0"/>
 * &lt;element name="participants" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="accountID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="groups" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="groupID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;attribute name="eventInfoTypeName" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventInfoInstance", propOrder = {
    "values",
    "participants",
    "groups",
    "endTime"
})
public class EventInfoInstance
    implements Serializable
{

    /** The values. */
    @XmlElement(nillable = true)
    protected Values values;
    
    /** The participants. */
    @XmlElement(nillable = true)
    protected EventInfoInstance.Participants participants;
    
    /** The groups. */
    @XmlElement(nillable = true)
    protected EventInfoInstance.Groups groups;
    
    /** The end time. */
    protected Double endTime;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The event info type name. */
    @XmlAttribute
    protected String eventInfoTypeName;

    /**
     * Gets the value of the values property.
     * 
     * @return the values
     * 
     * possible object is
     * {@link Values }
     */
    public Values getValues() {
        return values;
    }

    /**
     * Sets the value of the values property.
     * 
     * @param value allowed object is
     * {@link Values }
     */
    public void setValues(Values value) {
        this.values = value;
    }

    /**
     * Gets the value of the participants property.
     * 
     * @return the participants
     * 
     * possible object is
     * {@link EventInfoInstance.Participants }
     */
    public EventInfoInstance.Participants getParticipants() {
        return participants;
    }

    /**
     * Sets the value of the participants property.
     * 
     * @param value allowed object is
     * {@link EventInfoInstance.Participants }
     */
    public void setParticipants(EventInfoInstance.Participants value) {
        this.participants = value;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return the groups
     * 
     * possible object is
     * {@link EventInfoInstance.Groups }
     */
    public EventInfoInstance.Groups getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value allowed object is
     * {@link EventInfoInstance.Groups }
     */
    public void setGroups(EventInfoInstance.Groups value) {
        this.groups = value;
    }

    /**
     * Gets the value of the endTime property.
     * 
     * @return the end time
     * 
     * possible object is
     * {@link Double }
     */
    public Double getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setEndTime(Double value) {
        this.endTime = value;
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
     * Gets the value of the eventInfoTypeName property.
     * 
     * @return the event info type name
     * 
     * possible object is
     * {@link String }
     */
    public String getEventInfoTypeName() {
        return eventInfoTypeName;
    }

    /**
     * Sets the value of the eventInfoTypeName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setEventInfoTypeName(String value) {
        this.eventInfoTypeName = value;
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
     * &lt;element name="groupID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "groupID"
    })
    public static class Groups
        implements Serializable
    {

        /** The group id. */
        @XmlElement(nillable = true)
        protected List<String> groupID;

        /**
         * Gets the value of the groupID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the groupID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getGroupID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the group id
         */
        public List<String> getGroupID() {
            if (groupID == null) {
                groupID = new ArrayList<String>();
            }
            return this.groupID;
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
     * &lt;element name="accountID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "accountID"
    })
    public static class Participants
        implements Serializable
    {

        /** The account id. */
        @XmlElement(nillable = true)
        protected List<String> accountID;

        /**
         * Gets the value of the accountID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the accountID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getAccountID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the account id
         */
        public List<String> getAccountID() {
            if (accountID == null) {
                accountID = new ArrayList<String>();
            }
            return this.accountID;
        }

    }

}
