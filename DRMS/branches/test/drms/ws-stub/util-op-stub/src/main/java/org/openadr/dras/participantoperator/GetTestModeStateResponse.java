/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.GetTestModeStateResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for GetTestModeStateResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetTestModeStateResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="GetTestModeStateResponse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetTestModeStateResponse", propOrder = {
    "getTestModeStateResponse",
    "returnValue"
})
public class GetTestModeStateResponse
    implements Serializable
{

    /** The get test mode state response. */
    @XmlElement(name = "GetTestModeStateResponse", required = true)
    protected String getTestModeStateResponse;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the getTestModeStateResponse property.
     * 
     * @return the gets the test mode state response
     * 
     * possible object is
     * {@link String }
     */
    public String getGetTestModeStateResponse() {
        return getTestModeStateResponse;
    }

    /**
     * Sets the value of the getTestModeStateResponse property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setGetTestModeStateResponse(String value) {
        this.getTestModeStateResponse = value;
    }

    /**
     * Gets the value of the returnValue property.
     * 
     * @return the return value
     * 
     * possible object is
     * {@link String }
     */
    public String getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the value of the returnValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setReturnValue(String value) {
        this.returnValue = value;
    }

}
