/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityprogram.AkuaBidConfig.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityprogram;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AkuaBidConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaBidConfig">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="minBidKW" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;element name="defaultBidKW" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;element name="minConsectutiveBlocks" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="drasBidding" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="respondByTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="drasRespondByPeriodM" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="acceptTimeoutPeriodM" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="bidBlocks" type="{http://www.openadr.org/DRAS/AkuaUtilityProgram}AkuaBidBlockList" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaBidConfig", propOrder = {
    "minBidKW",
    "defaultBidKW",
    "minConsectutiveBlocks",
    "drasBidding",
    "respondByTime",
    "drasRespondByPeriodM",
    "acceptTimeoutPeriodM",
    "bidBlocks"
})
public class AkuaBidConfig
    implements Serializable
{

    /** The min bid kw. */
    protected double minBidKW;
    
    /** The default bid kw. */
    protected double defaultBidKW;
    
    /** The min consectutive blocks. */
    protected int minConsectutiveBlocks;
    
    /** The dras bidding. */
    protected boolean drasBidding;
    
    /** The respond by time. */
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar respondByTime;
    
    /** The dras respond by period m. */
    protected int drasRespondByPeriodM;
    
    /** The accept timeout period m. */
    protected int acceptTimeoutPeriodM;
    
    /** The bid blocks. */
    @XmlElement(nillable = true)
    protected AkuaBidBlockList bidBlocks;

    /**
     * Gets the value of the minBidKW property.
     * 
     * @return the min bid kw
     */
    public double getMinBidKW() {
        return minBidKW;
    }

    /**
     * Sets the value of the minBidKW property.
     * 
     * @param value the value
     */
    public void setMinBidKW(double value) {
        this.minBidKW = value;
    }

    /**
     * Gets the value of the defaultBidKW property.
     * 
     * @return the default bid kw
     */
    public double getDefaultBidKW() {
        return defaultBidKW;
    }

    /**
     * Sets the value of the defaultBidKW property.
     * 
     * @param value the value
     */
    public void setDefaultBidKW(double value) {
        this.defaultBidKW = value;
    }

    /**
     * Gets the value of the minConsectutiveBlocks property.
     * 
     * @return the min consectutive blocks
     */
    public int getMinConsectutiveBlocks() {
        return minConsectutiveBlocks;
    }

    /**
     * Sets the value of the minConsectutiveBlocks property.
     * 
     * @param value the value
     */
    public void setMinConsectutiveBlocks(int value) {
        this.minConsectutiveBlocks = value;
    }

    /**
     * Gets the value of the drasBidding property.
     * 
     * @return true, if checks if is dras bidding
     */
    public boolean isDrasBidding() {
        return drasBidding;
    }

    /**
     * Sets the value of the drasBidding property.
     * 
     * @param value the value
     */
    public void setDrasBidding(boolean value) {
        this.drasBidding = value;
    }

    /**
     * Gets the value of the respondByTime property.
     * 
     * @return the respond by time
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getRespondByTime() {
        return respondByTime;
    }

    /**
     * Sets the value of the respondByTime property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setRespondByTime(XMLGregorianCalendar value) {
        this.respondByTime = value;
    }

    /**
     * Gets the value of the drasRespondByPeriodM property.
     * 
     * @return the dras respond by period m
     */
    public int getDrasRespondByPeriodM() {
        return drasRespondByPeriodM;
    }

    /**
     * Sets the value of the drasRespondByPeriodM property.
     * 
     * @param value the value
     */
    public void setDrasRespondByPeriodM(int value) {
        this.drasRespondByPeriodM = value;
    }

    /**
     * Gets the value of the acceptTimeoutPeriodM property.
     * 
     * @return the accept timeout period m
     */
    public int getAcceptTimeoutPeriodM() {
        return acceptTimeoutPeriodM;
    }

    /**
     * Sets the value of the acceptTimeoutPeriodM property.
     * 
     * @param value the value
     */
    public void setAcceptTimeoutPeriodM(int value) {
        this.acceptTimeoutPeriodM = value;
    }

    /**
     * Gets the value of the bidBlocks property.
     * 
     * @return the bid blocks
     * 
     * possible object is
     * {@link AkuaBidBlockList }
     */
    public AkuaBidBlockList getBidBlocks() {
        return bidBlocks;
    }

    /**
     * Sets the value of the bidBlocks property.
     * 
     * @param value allowed object is
     * {@link AkuaBidBlockList }
     */
    public void setBidBlocks(AkuaBidBlockList value) {
        this.bidBlocks = value;
    }

}
