/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.UpdateSignals.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuasignal.ListOfSignals;


/**
 * <p>Java class for UpdateSignals complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateSignals">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="signals" type="{http://www.openadr.org/DRAS/AkuaSignal}ListOfSignals"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateSignals", propOrder = {
    "signals"
})
public class UpdateSignals
    implements Serializable
{

    /** The signals. */
    @XmlElement(required = true)
    protected ListOfSignals signals;

    /**
     * Gets the value of the signals property.
     * 
     * @return the signals
     * 
     * possible object is
     * {@link ListOfSignals }
     */
    public ListOfSignals getSignals() {
        return signals;
    }

    /**
     * Sets the value of the signals property.
     * 
     * @param value allowed object is
     * {@link ListOfSignals }
     */
    public void setSignals(ListOfSignals value) {
        this.signals = value;
    }

}
