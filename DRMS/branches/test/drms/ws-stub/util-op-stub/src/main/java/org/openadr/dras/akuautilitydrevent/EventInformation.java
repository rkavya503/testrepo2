/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilitydrevent.EventInformation.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilitydrevent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuaeventinfo.EventInfoInstance;


/**
 * This is the information associated with the DR
 * event. It is a list of EventInfoInstances
 * wherein the corresponding EventInfoType were
 * defined when the DR Program was first
 * configured.
 * 
 * 
 * <p>Java class for EventInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventInformation">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventInfoInstance" type="{http://www.openadr.org/DRAS/AkuaEventInfo}EventInfoInstance" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventInformation", propOrder = {
    "eventInfoInstance"
})
public class EventInformation
    implements Serializable
{

    /** The event info instance. */
    @XmlElement(nillable = true)
    protected List<EventInfoInstance> eventInfoInstance;

    /**
     * Gets the value of the eventInfoInstance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventInfoInstance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getEventInfoInstance().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventInfoInstance }
     * 
     * @return the event info instance
     */
    public List<EventInfoInstance> getEventInfoInstance() {
        if (eventInfoInstance == null) {
            eventInfoInstance = new ArrayList<EventInfoInstance>();
        }
        return this.eventInfoInstance;
    }

}
