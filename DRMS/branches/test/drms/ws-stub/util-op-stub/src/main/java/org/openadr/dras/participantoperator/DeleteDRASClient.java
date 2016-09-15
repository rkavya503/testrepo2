/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.DeleteDRASClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeleteDRASClient complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeleteDRASClient">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DRASClientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeleteDRASClient", propOrder = {
    "drasClientID"
})
public class DeleteDRASClient
    implements Serializable
{

    /** The dras client id. */
    @XmlElement(name = "DRASClientID", required = true)
    protected String drasClientID;

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

}
