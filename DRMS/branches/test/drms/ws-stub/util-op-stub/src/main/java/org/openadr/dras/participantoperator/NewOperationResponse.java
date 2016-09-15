/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.NewOperationResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for NewOperationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NewOperationResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="NewOperationResponse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NewOperationResponse", propOrder = {
    "newOperationResponse"
})
public class NewOperationResponse
    implements Serializable
{

    /** The new operation response. */
    @XmlElement(name = "NewOperationResponse", required = true)
    protected String newOperationResponse;

    /**
     * Gets the value of the newOperationResponse property.
     * 
     * @return the new operation response
     * 
     * possible object is
     * {@link String }
     */
    public String getNewOperationResponse() {
        return newOperationResponse;
    }

    /**
     * Sets the value of the newOperationResponse property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setNewOperationResponse(String value) {
        this.newOperationResponse = value;
    }

}
