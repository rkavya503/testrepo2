/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilitydrevent.ListOfEventIDs.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * List of DR Event ID's
 * 
 * <p>Java class for ListOfEventIDs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfEventIDs">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfEventIDs", propOrder = {
    "eventID"
})
public class ListOfEventIDs
    implements Serializable
{

    /** The event id. */
    @XmlElement(nillable = true)
    protected List<String> eventID;

    /**
     * Gets the value of the eventID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getEventID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * @return the event id
     */
    public List<String> getEventID() {
        if (eventID == null) {
            eventID = new ArrayList<String>();
        }
        return this.eventID;
    }

}
