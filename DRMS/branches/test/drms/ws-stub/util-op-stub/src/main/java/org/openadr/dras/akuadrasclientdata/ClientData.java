/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuadrasclientdata.ClientData.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuadrasclientdata;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClientData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClientData">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="clientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="programIDs" type="{http://www.openadr.org/DRAS/AkuaDRASClientData}ListOfIDs" minOccurs="0"/>
 * &lt;element name="pending" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="mode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="lastContact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="aveShedPerHour" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="mySiteURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="graphURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="param1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="param2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="param3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="clientDataAttributeList" type="{http://www.openadr.org/DRAS/AkuaDRASClientData}ListOfClientDataAttributes" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClientData", propOrder = {
    "clientID",
    "participantID",
    "programIDs",
    "pending",
    "mode",
    "lastContact",
    "aveShedPerHour",
    "status",
    "mySiteURL",
    "graphURL",
    "param1",
    "param2",
    "param3",
    "clientDataAttributeList"
})
public class ClientData
    implements Serializable
{

    /** The client id. */
    @XmlElement(required = true)
    protected String clientID;
    
    /** The participant id. */
    protected String participantID;
    
    /** The program i ds. */
    protected ListOfIDs programIDs;
    
    /** The pending. */
    protected String pending;
    
    /** The mode. */
    protected String mode;
    
    /** The last contact. */
    protected String lastContact;
    
    /** The ave shed per hour. */
    protected double aveShedPerHour;
    
    /** The status. */
    protected String status;
    
    /** The my site url. */
    protected String mySiteURL;
    
    /** The graph url. */
    protected String graphURL;
    
    /** The param1. */
    protected String param1;
    
    /** The param2. */
    protected String param2;
    
    /** The param3. */
    protected String param3;
    
    /** The client data attribute list. */
    @XmlElement(nillable = true)
    protected ListOfClientDataAttributes clientDataAttributeList;

    /**
     * Gets the value of the clientID property.
     * 
     * @return the client id
     * 
     * possible object is
     * {@link String }
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * Sets the value of the clientID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setClientID(String value) {
        this.clientID = value;
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
     * Gets the value of the programIDs property.
     * 
     * @return the program i ds
     * 
     * possible object is
     * {@link ListOfIDs }
     */
    public ListOfIDs getProgramIDs() {
        return programIDs;
    }

    /**
     * Sets the value of the programIDs property.
     * 
     * @param value allowed object is
     * {@link ListOfIDs }
     */
    public void setProgramIDs(ListOfIDs value) {
        this.programIDs = value;
    }

    /**
     * Gets the value of the pending property.
     * 
     * @return the pending
     * 
     * possible object is
     * {@link String }
     */
    public String getPending() {
        return pending;
    }

    /**
     * Sets the value of the pending property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setPending(String value) {
        this.pending = value;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return the mode
     * 
     * possible object is
     * {@link String }
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setMode(String value) {
        this.mode = value;
    }

    /**
     * Gets the value of the lastContact property.
     * 
     * @return the last contact
     * 
     * possible object is
     * {@link String }
     */
    public String getLastContact() {
        return lastContact;
    }

    /**
     * Sets the value of the lastContact property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setLastContact(String value) {
        this.lastContact = value;
    }

    /**
     * Gets the value of the aveShedPerHour property.
     * 
     * @return the ave shed per hour
     */
    public double getAveShedPerHour() {
        return aveShedPerHour;
    }

    /**
     * Sets the value of the aveShedPerHour property.
     * 
     * @param value the value
     */
    public void setAveShedPerHour(double value) {
        this.aveShedPerHour = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return the status
     * 
     * possible object is
     * {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the mySiteURL property.
     * 
     * @return the my site url
     * 
     * possible object is
     * {@link String }
     */
    public String getMySiteURL() {
        return mySiteURL;
    }

    /**
     * Sets the value of the mySiteURL property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setMySiteURL(String value) {
        this.mySiteURL = value;
    }

    /**
     * Gets the value of the graphURL property.
     * 
     * @return the graph url
     * 
     * possible object is
     * {@link String }
     */
    public String getGraphURL() {
        return graphURL;
    }

    /**
     * Sets the value of the graphURL property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setGraphURL(String value) {
        this.graphURL = value;
    }

    /**
     * Gets the value of the param1 property.
     * 
     * @return the param1
     * 
     * possible object is
     * {@link String }
     */
    public String getParam1() {
        return param1;
    }

    /**
     * Sets the value of the param1 property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParam1(String value) {
        this.param1 = value;
    }

    /**
     * Gets the value of the param2 property.
     * 
     * @return the param2
     * 
     * possible object is
     * {@link String }
     */
    public String getParam2() {
        return param2;
    }

    /**
     * Sets the value of the param2 property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParam2(String value) {
        this.param2 = value;
    }

    /**
     * Gets the value of the param3 property.
     * 
     * @return the param3
     * 
     * possible object is
     * {@link String }
     */
    public String getParam3() {
        return param3;
    }

    /**
     * Sets the value of the param3 property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParam3(String value) {
        this.param3 = value;
    }

    /**
     * Gets the value of the clientDataAttributeList property.
     * 
     * @return the client data attribute list
     * 
     * possible object is
     * {@link ListOfClientDataAttributes }
     */
    public ListOfClientDataAttributes getClientDataAttributeList() {
        return clientDataAttributeList;
    }

    /**
     * Sets the value of the clientDataAttributeList property.
     * 
     * @param value allowed object is
     * {@link ListOfClientDataAttributes }
     */
    public void setClientDataAttributeList(ListOfClientDataAttributes value) {
        this.clientDataAttributeList = value;
    }

}
