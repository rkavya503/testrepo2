/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.UpdateContacts.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuacontact.ListOfContacts;


/**
 * <p>Java class for UpdateContacts complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateContacts">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="ownerType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="ownerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="contacts" type="{http://www.openadr.org/DRAS/AkuaContact}ListOfContacts"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateContacts", propOrder = {
    "ownerType",
    "ownerID",
    "contacts"
})
public class UpdateContacts
    implements Serializable
{

    /** The owner type. */
    @XmlElement(nillable = true)
    protected String ownerType;
    
    /** The owner id. */
    @XmlElement(nillable = true)
    protected String ownerID;
    
    /** The contacts. */
    @XmlElement(required = true)
    protected ListOfContacts contacts;

    /**
     * Gets the value of the ownerType property.
     * 
     * @return the owner type
     * 
     * possible object is
     * {@link String }
     */
    public String getOwnerType() {
        return ownerType;
    }

    /**
     * Sets the value of the ownerType property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setOwnerType(String value) {
        this.ownerType = value;
    }

    /**
     * Gets the value of the ownerID property.
     * 
     * @return the owner id
     * 
     * possible object is
     * {@link String }
     */
    public String getOwnerID() {
        return ownerID;
    }

    /**
     * Sets the value of the ownerID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setOwnerID(String value) {
        this.ownerID = value;
    }

    /**
     * Gets the value of the contacts property.
     * 
     * @return the contacts
     * 
     * possible object is
     * {@link ListOfContacts }
     */
    public ListOfContacts getContacts() {
        return contacts;
    }

    /**
     * Sets the value of the contacts property.
     * 
     * @param value allowed object is
     * {@link ListOfContacts }
     */
    public void setContacts(ListOfContacts value) {
        this.contacts = value;
    }

}
