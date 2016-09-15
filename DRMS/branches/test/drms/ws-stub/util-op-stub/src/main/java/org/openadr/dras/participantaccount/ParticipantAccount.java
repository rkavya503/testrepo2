/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantaccount.ParticipantAccount.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantaccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.bid.Bid;
import org.openadr.dras.utilityprogram.ProgramInfo;


/**
 * This represents information associated with a
 * participant. Note that depending upon how accounts are
 * set a Participant may have more than one Participant
 * Account within the DRAS.
 * 
 * 
 * 
 * <p>Java class for ParticipantAccount complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParticipantAccount">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="userGroups" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
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
 * &lt;element name="utilityGroups" minOccurs="0">
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
 * &lt;element name="participantGroups" minOccurs="0">
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
 * &lt;element name="DRASClients" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="clientID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="contactInformation" type="{http://www.openadr.org/DRAS/ParticipantAccount}ContactInformation" minOccurs="0"/>
 * &lt;element name="standingBids" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="bid" type="{http://www.openadr.org/DRAS/Bid}Bid" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;attribute name="accountID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParticipantAccount", propOrder = {
    "participantName",
    "userName",
    "userGroups",
    "programs",
    "utilityGroups",
    "participantGroups",
    "drasClients",
    "contactInformation",
    "standingBids"
})
public class ParticipantAccount
    implements Serializable
{

    /** The participant name. */
    @XmlElement(required = true)
    protected String participantName;
    
    /** The user name. */
    @XmlElement(required = true)
    protected String userName;
    
    /** The user groups. */
    @XmlElement(nillable = true)
    protected ParticipantAccount.UserGroups userGroups;
    
    /** The programs. */
    @XmlElement(nillable = true)
    protected ParticipantAccount.Programs programs;
    
    /** The utility groups. */
    @XmlElement(nillable = true)
    protected ParticipantAccount.UtilityGroups utilityGroups;
    
    /** The participant groups. */
    @XmlElement(nillable = true)
    protected ParticipantAccount.ParticipantGroups participantGroups;
    
    /** The dras clients. */
    @XmlElement(name = "DRASClients", nillable = true)
    protected ParticipantAccount.DRASClients drasClients;
    
    /** The contact information. */
    @XmlElement(nillable = true)
    protected ContactInformation contactInformation;
    
    /** The standing bids. */
    @XmlElement(nillable = true)
    protected ParticipantAccount.StandingBids standingBids;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The account id. */
    @XmlAttribute(required = true)
    protected String accountID;

    /**
     * Gets the value of the participantName property.
     * 
     * @return the participant name
     * 
     * possible object is
     * {@link String }
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * Sets the value of the participantName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParticipantName(String value) {
        this.participantName = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return the user name
     * 
     * possible object is
     * {@link String }
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the userGroups property.
     * 
     * @return the user groups
     * 
     * possible object is
     * {@link ParticipantAccount.UserGroups }
     */
    public ParticipantAccount.UserGroups getUserGroups() {
        return userGroups;
    }

    /**
     * Sets the value of the userGroups property.
     * 
     * @param value allowed object is
     * {@link ParticipantAccount.UserGroups }
     */
    public void setUserGroups(ParticipantAccount.UserGroups value) {
        this.userGroups = value;
    }

    /**
     * Gets the value of the programs property.
     * 
     * @return the programs
     * 
     * possible object is
     * {@link ParticipantAccount.Programs }
     */
    public ParticipantAccount.Programs getPrograms() {
        return programs;
    }

    /**
     * Sets the value of the programs property.
     * 
     * @param value allowed object is
     * {@link ParticipantAccount.Programs }
     */
    public void setPrograms(ParticipantAccount.Programs value) {
        this.programs = value;
    }

    /**
     * Gets the value of the utilityGroups property.
     * 
     * @return the utility groups
     * 
     * possible object is
     * {@link ParticipantAccount.UtilityGroups }
     */
    public ParticipantAccount.UtilityGroups getUtilityGroups() {
        return utilityGroups;
    }

    /**
     * Sets the value of the utilityGroups property.
     * 
     * @param value allowed object is
     * {@link ParticipantAccount.UtilityGroups }
     */
    public void setUtilityGroups(ParticipantAccount.UtilityGroups value) {
        this.utilityGroups = value;
    }

    /**
     * Gets the value of the participantGroups property.
     * 
     * @return the participant groups
     * 
     * possible object is
     * {@link ParticipantAccount.ParticipantGroups }
     */
    public ParticipantAccount.ParticipantGroups getParticipantGroups() {
        return participantGroups;
    }

    /**
     * Sets the value of the participantGroups property.
     * 
     * @param value allowed object is
     * {@link ParticipantAccount.ParticipantGroups }
     */
    public void setParticipantGroups(ParticipantAccount.ParticipantGroups value) {
        this.participantGroups = value;
    }

    /**
     * Gets the value of the drasClients property.
     * 
     * @return the DRAS clients
     * 
     * possible object is
     * {@link ParticipantAccount.DRASClients }
     */
    public ParticipantAccount.DRASClients getDRASClients() {
        return drasClients;
    }

    /**
     * Sets the value of the drasClients property.
     * 
     * @param value allowed object is
     * {@link ParticipantAccount.DRASClients }
     */
    public void setDRASClients(ParticipantAccount.DRASClients value) {
        this.drasClients = value;
    }

    /**
     * Gets the value of the contactInformation property.
     * 
     * @return the contact information
     * 
     * possible object is
     * {@link ContactInformation }
     */
    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    /**
     * Sets the value of the contactInformation property.
     * 
     * @param value allowed object is
     * {@link ContactInformation }
     */
    public void setContactInformation(ContactInformation value) {
        this.contactInformation = value;
    }

    /**
     * Gets the value of the standingBids property.
     * 
     * @return the standing bids
     * 
     * possible object is
     * {@link ParticipantAccount.StandingBids }
     */
    public ParticipantAccount.StandingBids getStandingBids() {
        return standingBids;
    }

    /**
     * Sets the value of the standingBids property.
     * 
     * @param value allowed object is
     * {@link ParticipantAccount.StandingBids }
     */
    public void setStandingBids(ParticipantAccount.StandingBids value) {
        this.standingBids = value;
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
     * Gets the value of the accountID property.
     * 
     * @return the account id
     * 
     * possible object is
     * {@link String }
     */
    public String getAccountID() {
        return accountID;
    }

    /**
     * Sets the value of the accountID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setAccountID(String value) {
        this.accountID = value;
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
     * &lt;element name="clientID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "clientID"
    })
    public static class DRASClients
        implements Serializable
    {

        /** The client id. */
        @XmlElement(nillable = true)
        protected List<String> clientID;

        /**
         * Gets the value of the clientID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the clientID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getClientID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the client id
         */
        public List<String> getClientID() {
            if (clientID == null) {
                clientID = new ArrayList<String>();
            }
            return this.clientID;
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
    public static class ParticipantGroups
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
     * &lt;element name="bid" type="{http://www.openadr.org/DRAS/Bid}Bid" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "bid"
    })
    public static class StandingBids
        implements Serializable
    {

        /** The bid. */
        @XmlElement(nillable = true)
        protected List<Bid> bid;

        /**
         * Gets the value of the bid property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the bid property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getBid().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Bid }
         * 
         * @return the bid
         */
        public List<Bid> getBid() {
            if (bid == null) {
                bid = new ArrayList<Bid>();
            }
            return this.bid;
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
     * &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "userName"
    })
    public static class UserGroups
        implements Serializable
    {

        /** The user name. */
        @XmlElement(nillable = true)
        protected List<String> userName;

        /**
         * Gets the value of the userName property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the userName property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getUserName().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the user name
         */
        public List<String> getUserName() {
            if (userName == null) {
                userName = new ArrayList<String>();
            }
            return this.userName;
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
    public static class UtilityGroups
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

}
