/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityprogram.ParticipantList.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityprogram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.drasclient.Location;


/**
 * <p>Java class for ParticipantList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParticipantList">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="accounts" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
 * &lt;element name="locations" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="location" type="{http://www.openadr.org/DRAS/DRASClient}Location"/>
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
@XmlType(name = "ParticipantList", propOrder = {
    "accounts",
    "groups",
    "locations"
})
public class ParticipantList
    implements Serializable
{

    /** The accounts. */
    @XmlElement(nillable = true)
    protected ParticipantList.Accounts accounts;
    
    /** The groups. */
    @XmlElement(nillable = true)
    protected ParticipantList.Groups groups;
    
    /** The locations. */
    @XmlElement(nillable = true)
    protected ParticipantList.Locations locations;

    /**
     * Gets the value of the accounts property.
     * 
     * @return the accounts
     * 
     * possible object is
     * {@link ParticipantList.Accounts }
     */
    public ParticipantList.Accounts getAccounts() {
        return accounts;
    }

    /**
     * Sets the value of the accounts property.
     * 
     * @param value allowed object is
     * {@link ParticipantList.Accounts }
     */
    public void setAccounts(ParticipantList.Accounts value) {
        this.accounts = value;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return the groups
     * 
     * possible object is
     * {@link ParticipantList.Groups }
     */
    public ParticipantList.Groups getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value allowed object is
     * {@link ParticipantList.Groups }
     */
    public void setGroups(ParticipantList.Groups value) {
        this.groups = value;
    }

    /**
     * Gets the value of the locations property.
     * 
     * @return the locations
     * 
     * possible object is
     * {@link ParticipantList.Locations }
     */
    public ParticipantList.Locations getLocations() {
        return locations;
    }

    /**
     * Sets the value of the locations property.
     * 
     * @param value allowed object is
     * {@link ParticipantList.Locations }
     */
    public void setLocations(ParticipantList.Locations value) {
        this.locations = value;
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
     * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "participantID"
    })
    public static class Accounts
        implements Serializable
    {

        /** The participant id. */
        @XmlElement(nillable = true)
        protected List<String> participantID;

        /**
         * Gets the value of the participantID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the participantID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getParticipantID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the participant id
         */
        public List<String> getParticipantID() {
            if (participantID == null) {
                participantID = new ArrayList<String>();
            }
            return this.participantID;
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
     * &lt;element name="location" type="{http://www.openadr.org/DRAS/DRASClient}Location"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "location"
    })
    public static class Locations
        implements Serializable
    {

        /** The location. */
        @XmlElement(required = true, nillable = true)
        protected Location location;

        /**
         * Gets the value of the location property.
         * 
         * @return the location
         * 
         * possible object is
         * {@link Location }
         */
        public Location getLocation() {
            return location;
        }

        /**
         * Sets the value of the location property.
         * 
         * @param value allowed object is
         * {@link Location }
         */
        public void setLocation(Location value) {
            this.location = value;
        }

    }

}
