/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.SetTestMode.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for SetTestMode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SetTestMode">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="drasClientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="mode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetTestMode", propOrder = {
    "drasClientID",
    "mode"
})
public class SetTestMode
    implements Serializable
{

    /** The dras client id. */
    @XmlElement(required = true)
    protected String drasClientID;
    
    /** The mode. */
    @XmlElement(required = true)
    protected String mode;

    /**
     * Gets the value of the drasClientID property.
     * 
     * @return the dras client id
     * 
     * possible object is
     * {@link String }
     */
    public String getDrasClientID() {
        return drasClientID;
    }

    /**
     * Sets the value of the drasClientID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDrasClientID(String value) {
        this.drasClientID = value;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return the mode
     * 
     * possible object is
     * {@link String }
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setMode(String value) {
        this.mode = value;
    }

}
