/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.DeleteOptOutState.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for DeleteOptOutState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeleteOptOutState">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="optOutStateID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeleteOptOutState", propOrder = {
    "optOutStateID"
})
public class DeleteOptOutState
    implements Serializable
{

    /** The opt out state id. */
    @XmlElement(required = true)
    protected String optOutStateID;

    /**
     * Gets the value of the optOutStateID property.
     * 
     * @return the opt out state id
     * 
     * possible object is
     * {@link String }
     */
    public String getOptOutStateID() {
        return optOutStateID;
    }

    /**
     * Sets the value of the optOutStateID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setOptOutStateID(String value) {
        this.optOutStateID = value;
    }

}
