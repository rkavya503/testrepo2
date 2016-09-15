/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.NewOperation.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for NewOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NewOperation">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="NewOperationRequest" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NewOperation", propOrder = {
    "newOperationRequest"
})
public class NewOperation
    implements Serializable
{

    /** The new operation request. */
    @XmlElement(name = "NewOperationRequest", required = true)
    protected String newOperationRequest;

    /**
     * Gets the value of the newOperationRequest property.
     * 
     * @return the new operation request
     * 
     * possible object is
     * {@link String }
     */
    public String getNewOperationRequest() {
        return newOperationRequest;
    }

    /**
     * Sets the value of the newOperationRequest property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setNewOperationRequest(String value) {
        this.newOperationRequest = value;
    }

}
