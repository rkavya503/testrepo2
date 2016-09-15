/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilitydrevent.EventTiming.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilitydrevent;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * List of DR Event ID's
 * 
 * <p>Java class for EventTiming complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventTiming">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="notificationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventTiming", propOrder = {
    "notificationTime",
    "startTime",
    "endTime"
})
public class EventTiming
    implements Serializable
{

    /** The notification time. */
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar notificationTime;
    
    /** The start time. */
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startTime;
    
    /** The end time. */
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endTime;

    /**
     * Gets the value of the notificationTime property.
     * 
     * @return the notification time
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getNotificationTime() {
        return notificationTime;
    }

    /**
     * Sets the value of the notificationTime property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setNotificationTime(XMLGregorianCalendar value) {
        this.notificationTime = value;
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

}
