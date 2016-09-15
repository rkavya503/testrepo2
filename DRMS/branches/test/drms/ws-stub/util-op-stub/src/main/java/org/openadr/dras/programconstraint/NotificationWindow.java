/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.programconstraint.NotificationWindow.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.programconstraint;

import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * This is the window of time during whcih notification of
 * the DR event must occur.
 * 
 * 
 * <p>Java class for NotificationWindow complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotificationWindow">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="minimumTime" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 * &lt;element name="maximumTime" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationWindow", propOrder = {
    "minimumTime",
    "maximumTime"
})
public class NotificationWindow
    implements Serializable
{

    /** The minimum time. */
    @XmlElement(required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger minimumTime;
    
    /** The maximum time. */
    @XmlElement(required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger maximumTime;

    /**
     * Gets the value of the minimumTime property.
     * 
     * @return the minimum time
     * 
     * possible object is
     * {@link BigInteger }
     */
    public BigInteger getMinimumTime() {
        return minimumTime;
    }

    /**
     * Sets the value of the minimumTime property.
     * 
     * @param value allowed object is
     * {@link BigInteger }
     */
    public void setMinimumTime(BigInteger value) {
        this.minimumTime = value;
    }

    /**
     * Gets the value of the maximumTime property.
     * 
     * @return the maximum time
     * 
     * possible object is
     * {@link BigInteger }
     */
    public BigInteger getMaximumTime() {
        return maximumTime;
    }

    /**
     * Sets the value of the maximumTime property.
     * 
     * @param value allowed object is
     * {@link BigInteger }
     */
    public void setMaximumTime(BigInteger value) {
        this.maximumTime = value;
    }

}
