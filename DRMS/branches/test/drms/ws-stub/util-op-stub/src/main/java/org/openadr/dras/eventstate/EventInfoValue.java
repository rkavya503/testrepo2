/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventstate.EventInfoValue.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventstate;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * This is an instance of a value for the information that is associated with a DR event when it is issued by the Utility.  This instance includes a number value for the information and a time parameter that defines a time slot with the DR events active period for when this value is valid.  By putting together sequences of these types there can be a schedule of values that are valid across the entire active period of the DR event.
 * 
 * <p>Java class for EventInfoValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventInfoValue">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 * &lt;element name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventInfoValue", propOrder = {
    "value",
    "timeOffset"
})
public class EventInfoValue
    implements Serializable
{

    /** The value. */
    @XmlElement(required = true)
    protected BigDecimal value;
    
    /** The time offset. */
    @XmlSchemaType(name = "unsignedInt")
    protected long timeOffset;

    /**
     * Gets the value of the value property.
     * 
     * @return the value
     * 
     * possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value allowed object is
     * {@link BigDecimal }
     */
    public void setValue(BigDecimal value) {
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
