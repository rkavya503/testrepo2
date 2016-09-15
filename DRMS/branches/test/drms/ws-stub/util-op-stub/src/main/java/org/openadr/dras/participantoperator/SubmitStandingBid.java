/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.SubmitStandingBid.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.bid.Bid;


/**
 * <p>Java class for SubmitStandingBid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubmitStandingBid">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="bid" type="{http://www.openadr.org/DRAS/Bid}Bid"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmitStandingBid", propOrder = {
    "bid"
})
public class SubmitStandingBid
    implements Serializable
{

    /** The bid. */
    @XmlElement(required = true)
    protected Bid bid;

    /**
     * Gets the value of the bid property.
     * 
     * @return the bid
     * 
     * possible object is
     * {@link Bid }
     */
    public Bid getBid() {
        return bid;
    }

    /**
     * Sets the value of the bid property.
     * 
     * @param value allowed object is
     * {@link Bid }
     */
    public void setBid(Bid value) {
        this.bid = value;
    }

}
