/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.UpdateProperties.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuaproperty.ListOfProperties;


/**
 * <p>Java class for UpdateProperties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateProperties">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="properties" type="{http://www.openadr.org/DRAS/AkuaProperty}ListOfProperties"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateProperties", propOrder = {
    "properties"
})
public class UpdateProperties
    implements Serializable
{

    /** The properties. */
    @XmlElement(required = true)
    protected ListOfProperties properties;

    /**
     * Gets the value of the properties property.
     * 
     * @return the properties
     * 
     * possible object is
     * {@link ListOfProperties }
     */
    public ListOfProperties getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value allowed object is
     * {@link ListOfProperties }
     */
    public void setProperties(ListOfProperties value) {
        this.properties = value;
    }

}
