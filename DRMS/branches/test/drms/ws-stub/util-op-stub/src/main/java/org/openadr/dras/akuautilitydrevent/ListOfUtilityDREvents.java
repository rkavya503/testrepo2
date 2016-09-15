/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilitydrevent.ListOfUtilityDREvents.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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


/**
 * This is simply a list of Utility DR events.
 * 
 * 
 * <p>Java class for ListOfUtilityDREvents complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfUtilityDREvents">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DREvent" type="{http://www.openadr.org/DRAS/AkuaUtilityDREvent}UtilityDREvent" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfUtilityDREvents", propOrder = {
    "drEvent"
})
public class ListOfUtilityDREvents
    implements Serializable
{

    /** The dr event. */
    @XmlElement(name = "DREvent", nillable = true)
    protected List<UtilityDREvent> drEvent;

    /**
     * Gets the value of the drEvent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the drEvent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getDREvent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UtilityDREvent }
     * 
     * @return the DR event
     */
    public List<UtilityDREvent> getDREvent() {
        if (drEvent == null) {
            drEvent = new ArrayList<UtilityDREvent>();
        }
        return this.drEvent;
    }

}
