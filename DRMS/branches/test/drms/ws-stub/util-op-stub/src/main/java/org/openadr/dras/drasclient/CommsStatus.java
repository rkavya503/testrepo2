/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.drasclient.CommsStatus.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.drasclient;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This is used to represent the communications status of a
 * DRAS Client.
 * 
 * 
 * <p>Java class for CommsStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommsStatus">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DRASClientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="status">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="OK"/>
 * &lt;enumeration value="OFFLINE"/>
 * &lt;enumeration value="ERRORS"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommsStatus", propOrder = {
    "drasClientID",
    "status"
})
public class CommsStatus
    implements Serializable
{

    /** The dras client id. */
    @XmlElement(name = "DRASClientID", required = true)
    protected String drasClientID;
    
    /** The status. */
    @XmlElement(required = true)
    protected String status;

    /**
     * Gets the value of the drasClientID property.
     * 
     * @return the DRAS client id
     * 
     * possible object is
     * {@link String }
     */
    public String getDRASClientID() {
        return drasClientID;
    }

    /**
     * Sets the value of the drasClientID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDRASClientID(String value) {
        this.drasClientID = value;
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

}
