/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuasignal.AkuaSignal.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuasignal;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AkuaSignal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaSignal">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="signalName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="numberType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="value" type="{http://www.openadr.org/DRAS/AkuaSignal}ListOfValues" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaSignal", propOrder = {
    "id",
    "signalName",
    "type",
    "numberType",
    "value"
})
public class AkuaSignal
    implements Serializable
{

    /** The id. */
    @XmlElement(required = true)
    protected String id;
    
    /** The signal name. */
    @XmlElement(required = true)
    protected String signalName;
    
    /** The type. */
    protected String type;
    
    /** The number type. */
    protected String numberType;
    
    /** The value. */
    protected ListOfValues value;

    /**
     * Gets the value of the id property.
     * 
     * @return the id
     * 
     * possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the signalName property.
     * 
     * @return the signal name
     * 
     * possible object is
     * {@link String }
     */
    public String getSignalName() {
        return signalName;
    }

    /**
     * Sets the value of the signalName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setSignalName(String value) {
        this.signalName = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return the type
     * 
     * possible object is
     * {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the numberType property.
     * 
     * @return the number type
     * 
     * possible object is
     * {@link String }
     */
    public String getNumberType() {
        return numberType;
    }

    /**
     * Sets the value of the numberType property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setNumberType(String value) {
        this.numberType = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return the value
     * 
     * possible object is
     * {@link ListOfValues }
     */
    public ListOfValues getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value allowed object is
     * {@link ListOfValues }
     */
    public void setValue(ListOfValues value) {
        this.value = value;
    }

}
