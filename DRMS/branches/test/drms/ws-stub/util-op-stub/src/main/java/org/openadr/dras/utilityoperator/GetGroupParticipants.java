/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.GetGroupParticipants.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetGroupParticipants complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetGroupParticipants">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="GetGroupParticipantsRequest" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetGroupParticipants", propOrder = {
    "getGroupParticipantsRequest"
})
public class GetGroupParticipants
    implements Serializable
{

    /** The get group participants request. */
    @XmlElement(name = "GetGroupParticipantsRequest", required = true)
    protected String getGroupParticipantsRequest;

    /**
     * Gets the value of the getGroupParticipantsRequest property.
     * 
     * @return the gets the group participants request
     * 
     * possible object is
     * {@link String }
     */
    public String getGetGroupParticipantsRequest() {
        return getGroupParticipantsRequest;
    }

    /**
     * Sets the value of the getGroupParticipantsRequest property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setGetGroupParticipantsRequest(String value) {
        this.getGroupParticipantsRequest = value;
    }

}
