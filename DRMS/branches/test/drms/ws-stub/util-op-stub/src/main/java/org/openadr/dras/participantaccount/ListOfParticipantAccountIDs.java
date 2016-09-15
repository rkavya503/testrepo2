/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantaccount.ListOfParticipantAccountIDs.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantaccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents a list of ParticipantAccount identifiers.
 * 
 * 
 * <p>Java class for ListOfParticipantAccountIDs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfParticipantAccountIDs">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantAccountID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfParticipantAccountIDs", propOrder = {
    "participantAccountID"
})
public class ListOfParticipantAccountIDs
    implements Serializable
{

    /** The participant account id. */
    @XmlElement(nillable = true)
    protected List<String> participantAccountID;

    /**
     * Gets the value of the participantAccountID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participantAccountID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getParticipantAccountID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * @return the participant account id
     */
    public List<String> getParticipantAccountID() {
        if (participantAccountID == null) {
            participantAccountID = new ArrayList<String>();
        }
        return this.participantAccountID;
    }

}
