/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.bid.Bid.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.bid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * This is the type used to support bids submitted by a
 * Participant. It supports both standing bids and active
 * bids.
 * 
 * 
 * <p>Java class for Bid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bid">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="bidBlocks" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="block" type="{http://www.openadr.org/DRAS/Bid}BidBlock" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="participantAccount" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="programName" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="eventID" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="options" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="signature" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bid", propOrder = {
    "bidBlocks"
})
public class Bid
    implements Serializable
{

    /** The bid blocks. */
    protected Bid.BidBlocks bidBlocks;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The participant account. */
    @XmlAttribute(required = true)
    protected String participantAccount;
    
    /** The program name. */
    @XmlAttribute
    protected String programName;
    
    /** The event id. */
    @XmlAttribute
    protected String eventID;
    
    /** The options. */
    @XmlAttribute
    protected String options;
    
    /** The signature. */
    @XmlAttribute
    protected String signature;

    /**
     * Gets the value of the bidBlocks property.
     * 
     * @return the bid blocks
     * 
     * possible object is
     * {@link Bid.BidBlocks }
     */
    public Bid.BidBlocks getBidBlocks() {
        return bidBlocks;
    }

    /**
     * Sets the value of the bidBlocks property.
     * 
     * @param value allowed object is
     * {@link Bid.BidBlocks }
     */
    public void setBidBlocks(Bid.BidBlocks value) {
        this.bidBlocks = value;
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
     * Gets the value of the participantAccount property.
     * 
     * @return the participant account
     * 
     * possible object is
     * {@link String }
     */
    public String getParticipantAccount() {
        return participantAccount;
    }

    /**
     * Sets the value of the participantAccount property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParticipantAccount(String value) {
        this.participantAccount = value;
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
     * Gets the value of the options property.
     * 
     * @return the options
     * 
     * possible object is
     * {@link String }
     */
    public String getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setOptions(String value) {
        this.options = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="block" type="{http://www.openadr.org/DRAS/Bid}BidBlock" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "block"
    })
    public static class BidBlocks
        implements Serializable
    {

        /** The block. */
        protected List<BidBlock> block;

        /**
         * Gets the value of the block property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the block property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getBlock().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BidBlock }
         * 
         * @return the block
         */
        public List<BidBlock> getBlock() {
            if (block == null) {
                block = new ArrayList<BidBlock>();
            }
            return this.block;
        }

    }

}
