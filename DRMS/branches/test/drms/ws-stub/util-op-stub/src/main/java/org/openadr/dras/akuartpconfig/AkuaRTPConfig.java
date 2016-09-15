/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuartpconfig.AkuaRTPConfig.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuartpconfig;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AkuaRTPConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaRTPConfig">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="seasonName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 * &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 * &lt;element name="startTemperature" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="endTemperature" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="rate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaRTPConfig", propOrder = {
    "id",
    "name",
    "seasonName",
    "programName",
    "startTime",
    "endTime",
    "startTemperature",
    "endTemperature",
    "rate",
    "unit"
})
public class AkuaRTPConfig
    implements Serializable
{

    /** The id. */
    @XmlElement(nillable = true)
    protected String id;
    
    /** The name. */
    @XmlElement(required = true)
    protected String name;
    
    /** The season name. */
    @XmlElement(nillable = true)
    protected String seasonName;
    
    /** The program name. */
    @XmlElement(nillable = true)
    protected String programName;
    
    /** The start time. */
    @XmlElement(nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startTime;
    
    /** The end time. */
    @XmlElement(nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endTime;
    
    /** The start temperature. */
    protected Double startTemperature;
    
    /** The end temperature. */
    protected Double endTemperature;
    
    /** The rate. */
    protected Double rate;
    
    /** The unit. */
    @XmlElement(required = true, nillable = true)
    protected String unit;

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
     * Gets the value of the name property.
     * 
     * @return the name
     * 
     * possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the seasonName property.
     * 
     * @return the season name
     * 
     * possible object is
     * {@link String }
     */
    public String getSeasonName() {
        return seasonName;
    }

    /**
     * Sets the value of the seasonName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setSeasonName(String value) {
        this.seasonName = value;
    }

    /**
     * Gets the value of the programName property.
     * 
     * @return the program name
     * 
     * possible object is
     * {@link String }
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the value of the programName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setProgramName(String value) {
        this.programName = value;
    }

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

    /**
     * Gets the value of the startTemperature property.
     * 
     * @return the start temperature
     * 
     * possible object is
     * {@link Double }
     */
    public Double getStartTemperature() {
        return startTemperature;
    }

    /**
     * Sets the value of the startTemperature property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setStartTemperature(Double value) {
        this.startTemperature = value;
    }

    /**
     * Gets the value of the endTemperature property.
     * 
     * @return the end temperature
     * 
     * possible object is
     * {@link Double }
     */
    public Double getEndTemperature() {
        return endTemperature;
    }

    /**
     * Sets the value of the endTemperature property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setEndTemperature(Double value) {
        this.endTemperature = value;
    }

    /**
     * Gets the value of the rate property.
     * 
     * @return the rate
     * 
     * possible object is
     * {@link Double }
     */
    public Double getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setRate(Double value) {
        this.rate = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return the unit
     * 
     * possible object is
     * {@link String }
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setUnit(String value) {
        this.unit = value;
    }

}
