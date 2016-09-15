/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventstate.GeneralInfoValue.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventstate;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * This is the value part of in the name-value pair of an instance of GeneralInfoInstance.  It consists of a value field and an optional time field which can be used to specify the time slot of a schedule for when this value is valid.
 * 
 * 
 * 
 * <p>Java class for GeneralInfoValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeneralInfoValue">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeneralInfoValue", propOrder = {
    "value",
    "timeOffset"
})
public class GeneralInfoValue
    implements Serializable
{

    /** The value. */
    @XmlElement(required = true)
    protected String value;
    
    /** The time offset. */
    @XmlSchemaType(name = "unsignedInt")
    protected long timeOffset;

    /**
     * Gets the value of the value property.
     * 
     * @return the value
     * 
     * possible object is
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the timeOffset property.
     * 
     * @return the time offset
     */
    public long getTimeOffset() {
        return timeOffset;
    }

    /**
     * Sets the value of the timeOffset property.
     * 
     * @param value the value
     */
    public void setTimeOffset(long value) {
        this.timeOffset = value;
    }

}
