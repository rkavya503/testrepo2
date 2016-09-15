/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.bid.BidBlock.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.bid;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BidBlock complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BidBlock">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="load" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="timePeriod" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BidBlock", propOrder = {
    "load",
    "price",
    "duration",
    "timePeriod"
})
public class BidBlock
    implements Serializable
{

    /** The load. */
    protected Double load;
    
    /** The price. */
    protected Double price;
    
    /** The duration. */
    protected Double duration;
    
    /** The time period. */
    protected BidBlock.TimePeriod timePeriod;

    /**
     * Gets the value of the load property.
     * 
     * @return the load
     * 
     * possible object is
     * {@link Double }
     */
    public Double getLoad() {
        return load;
    }

    /**
     * Sets the value of the load property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setLoad(Double value) {
        this.load = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return the price
     * 
     * possible object is
     * {@link Double }
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setPrice(Double value) {
        this.price = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return the duration
     * 
     * possible object is
     * {@link Double }
     */
    public Double getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setDuration(Double value) {
        this.duration = value;
    }

    /**
     * Gets the value of the timePeriod property.
     * 
     * @return the time period
     * 
     * possible object is
     * {@link BidBlock.TimePeriod }
     */
    public BidBlock.TimePeriod getTimePeriod() {
        return timePeriod;
    }

    /**
     * Sets the value of the timePeriod property.
     * 
     * @param value allowed object is
     * {@link BidBlock.TimePeriod }
     */
    public void setTimePeriod(BidBlock.TimePeriod value) {
        this.timePeriod = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     * &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "startTime",
        "endTime"
    })
    public static class TimePeriod
        implements Serializable
    {

        /** The start time. */
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar startTime;
        
        /** The end time. */
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar endTime;

        /**
         * Gets the value of the startTime property.
         * 
         * @return the start time
         * 
         * possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getStartTime() {
            return startTime;
        }

        /**
         * Sets the value of the startTime property.
         * 
         * @param value allowed object is
         * {@link XMLGregorianCalendar }
         */
        public void setStartTime(XMLGregorianCalendar value) {
            this.startTime = value;
        }

        /**
         * Gets the value of the endTime property.
         * 
         * @return the end time
         * 
         * possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getEndTime() {
            return endTime;
        }

        /**
         * Sets the value of the endTime property.
         * 
         * @param value allowed object is
         * {@link XMLGregorianCalendar }
         */
        public void setEndTime(XMLGregorianCalendar value) {
            this.endTime = value;
        }

    }

}
