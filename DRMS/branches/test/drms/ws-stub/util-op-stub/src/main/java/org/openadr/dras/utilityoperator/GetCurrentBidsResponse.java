/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.GetCurrentBidsResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.bid.ListOfBids;


/**
 * <p>Java class for GetCurrentBidsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCurrentBidsResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="bids" type="{http://www.openadr.org/DRAS/Bid}ListOfBids" minOccurs="0"/>
 * &lt;element name="biddingOpen" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCurrentBidsResponse", propOrder = {
    "bids",
    "biddingOpen",
    "returnValue"
})
public class GetCurrentBidsResponse
    implements Serializable
{

    /** The bids. */
    @XmlElement(nillable = true)
    protected ListOfBids bids;
    
    /** The bidding open. */
    protected boolean biddingOpen;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the bids property.
     * 
     * @return the bids
     * 
     * possible object is
     * {@link ListOfBids }
     */
    public ListOfBids getBids() {
        return bids;
    }

    /**
     * Sets the value of the bids property.
     * 
     * @param value allowed object is
     * {@link ListOfBids }
     */
    public void setBids(ListOfBids value) {
        this.bids = value;
    }

    /**
     * Gets the value of the biddingOpen property.
     * 
     * @return true, if checks if is bidding open
     */
    public boolean isBiddingOpen() {
        return biddingOpen;
    }

    /**
     * Sets the value of the biddingOpen property.
     * 
     * @param value the value
     */
    public void setBiddingOpen(boolean value) {
        this.biddingOpen = value;
    }

    /**
     * Gets the value of the returnValue property.
     * 
     * @return the return value
     * 
     * possible object is
     * {@link String }
     */
    public String getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the value of the returnValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setReturnValue(String value) {
        this.returnValue = value;
    }

}
