/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.CreateOptOutState.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.optoutstate.OptOutState;


/**
 * <p>Java class for CreateOptOutState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateOptOutState">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="optOutState" type="{http://www.openadr.org/DRAS/OptOutState}OptOutState"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateOptOutState", propOrder = {
    "optOutState"
})
public class CreateOptOutState
    implements Serializable
{

    /** The opt out state. */
    @XmlElement(required = true)
    protected OptOutState optOutState;

    /**
     * Gets the value of the optOutState property.
     * 
     * @return the opt out state
     * 
     * possible object is
     * {@link OptOutState }
     */
    public OptOutState getOptOutState() {
        return optOutState;
    }

    /**
     * Sets the value of the optOutState property.
     * 
     * @param value allowed object is
     * {@link OptOutState }
     */
    public void setOptOutState(OptOutState value) {
        this.optOutState = value;
    }

}
