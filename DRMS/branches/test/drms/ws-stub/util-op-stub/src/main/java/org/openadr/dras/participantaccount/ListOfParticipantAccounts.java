/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantaccount.ListOfParticipantAccounts.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * This is a list of ParticipantAccount objects.
 * 
 * 
 * <p>Java class for ListOfParticipantAccounts complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfParticipantAccounts">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantAccount" type="{http://www.openadr.org/DRAS/ParticipantAccount}ParticipantAccount" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfParticipantAccounts", propOrder = {
    "participantAccount"
})
public class ListOfParticipantAccounts
    implements Serializable
{

    /** The participant account. */
    @XmlElement(nillable = true)
    protected List<ParticipantAccount> participantAccount;

    /**
     * Gets the value of the participantAccount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participantAccount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getParticipantAccount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParticipantAccount }
     * 
     * @return the participant account
     */
    public List<ParticipantAccount> getParticipantAccount() {
        if (participantAccount == null) {
            participantAccount = new ArrayList<ParticipantAccount>();
        }
        return this.participantAccount;
    }

}
