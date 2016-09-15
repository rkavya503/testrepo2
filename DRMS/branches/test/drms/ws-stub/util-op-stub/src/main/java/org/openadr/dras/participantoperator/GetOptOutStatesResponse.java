/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.GetOptOutStatesResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.optoutstate.ListOfOptOutStates;


/**
 * <p>Java class for GetOptOutStatesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetOptOutStatesResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="optOutStates" type="{http://www.openadr.org/DRAS/OptOutState}ListOfOptOutStates" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetOptOutStatesResponse", propOrder = {
    "optOutStates",
    "returnValue"
})
public class GetOptOutStatesResponse
    implements Serializable
{

    /** The opt out states. */
    @XmlElement(nillable = true)
    protected ListOfOptOutStates optOutStates;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the optOutStates property.
     * 
     * @return the opt out states
     * 
     * possible object is
     * {@link ListOfOptOutStates }
     */
    public ListOfOptOutStates getOptOutStates() {
        return optOutStates;
    }

    /**
     * Sets the value of the optOutStates property.
     * 
     * @param value allowed object is
     * {@link ListOfOptOutStates }
     */
    public void setOptOutStates(ListOfOptOutStates value) {
        this.optOutStates = value;
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
