/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuacontact.AkuaContact.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuacontact;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AkuaContact complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaContact">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="offSeasonNotiHours" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="onSeasonNotiHours" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="commNotification" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 * &lt;element name="eventNotification" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaContact", propOrder = {
    "id",
    "address",
    "type",
    "firstName",
    "lastName",
    "offSeasonNotiHours",
    "onSeasonNotiHours",
    "commNotification",
    "eventNotification"
})
public class AkuaContact
    implements Serializable
{

    /** The id. */
    @XmlElement(nillable = true)
    protected String id;
    
    /** The address. */
    @XmlElement(required = true)
    protected String address;
    
    /** The type. */
    protected String type;
    
    /** The first name. */
    @XmlElement(nillable = true)
    protected String firstName;
    
    /** The last name. */
    @XmlElement(nillable = true)
    protected String lastName;
    
    /** The off season noti hours. */
    protected Double offSeasonNotiHours;
    
    /** The on season noti hours. */
    protected Double onSeasonNotiHours;
    
    /** The comm notification. */
    protected Boolean commNotification;
    
    /** The event notification. */
    protected Boolean eventNotification;

    /**
     * Gets the value of the id property.
     * 
     * @return the id
     * 
     * possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return the address
     * 
     * possible object is
     * {@link String }
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return the type
     * 
     * possible object is
     * {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return the first name
     * 
     * possible object is
     * {@link String }
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return the last name
     * 
     * possible object is
     * {@link String }
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the offSeasonNotiHours property.
     * 
     * @return the off season noti hours
     * 
     * possible object is
     * {@link Double }
     */
    public Double getOffSeasonNotiHours() {
        return offSeasonNotiHours;
    }

    /**
     * Sets the value of the offSeasonNotiHours property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setOffSeasonNotiHours(Double value) {
        this.offSeasonNotiHours = value;
    }

    /**
     * Gets the value of the onSeasonNotiHours property.
     * 
     * @return the on season noti hours
     * 
     * possible object is
     * {@link Double }
     */
    public Double getOnSeasonNotiHours() {
        return onSeasonNotiHours;
    }

    /**
     * Sets the value of the onSeasonNotiHours property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setOnSeasonNotiHours(Double value) {
        this.onSeasonNotiHours = value;
    }

    /**
     * Gets the value of the commNotification property.
     * 
     * @return true, if checks if is comm notification
     * 
     * possible object is
     * {@link Boolean }
     */
    public Boolean isCommNotification() {
        return commNotification;
    }

    /**
     * Sets the value of the commNotification property.
     * 
     * @param value allowed object is
     * {@link Boolean }
     */
    public void setCommNotification(Boolean value) {
        this.commNotification = value;
    }

    /**
     * Gets the value of the eventNotification property.
     * 
     * @return true, if checks if is event notification
     * 
     * possible object is
     * {@link Boolean }
     */
    public Boolean isEventNotification() {
        return eventNotification;
    }

    /**
     * Sets the value of the eventNotification property.
     * 
     * @param value allowed object is
     * {@link Boolean }
     */
    public void setEventNotification(Boolean value) {
        this.eventNotification = value;
    }

}
