/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuaeventinfo.ListOfEventInfoType.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuaeventinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents a list of EventInfoTypes.
 * 
 * 
 * <p>Java class for ListOfEventInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfEventInfoType">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventInfoType" type="{http://www.openadr.org/DRAS/AkuaEventInfo}EventInfoType" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfEventInfoType", propOrder = {
    "eventInfoType"
})
public class ListOfEventInfoType
    implements Serializable
{

    /** The event info type. */
    protected List<EventInfoType> eventInfoType;

    /**
     * Gets the value of the eventInfoType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventInfoType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getEventInfoType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventInfoType }
     * 
     * @return the event info type
     */
    public List<EventInfoType> getEventInfoType() {
        if (eventInfoType == null) {
            eventInfoType = new ArrayList<EventInfoType>();
        }
        return this.eventInfoType;
    }

}
