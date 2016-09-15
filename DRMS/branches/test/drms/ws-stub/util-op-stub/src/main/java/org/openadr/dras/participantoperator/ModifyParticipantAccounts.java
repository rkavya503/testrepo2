/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.ModifyParticipantAccounts.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;
import org.openadr.dras.participantaccount.ParticipantAccount;


/**
 * <p>Java class for ModifyParticipantAccounts complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModifyParticipantAccounts">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantIDs" type="{http://www.openadr.org/DRAS/ParticipantAccount}ListOfParticipantAccountIDs"/>
 * &lt;element name="participantGroup" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="participantAccount" type="{http://www.openadr.org/DRAS/ParticipantAccount}ParticipantAccount"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyParticipantAccounts", propOrder = {
    "participantIDs",
    "participantGroup",
    "participantAccount"
})
public class ModifyParticipantAccounts
    implements Serializable
{

    /** The participant i ds. */
    @XmlElement(required = true)
    protected ListOfParticipantAccountIDs participantIDs;
    
    /** The participant group. */
    @XmlElement(required = true)
    protected String participantGroup;
    
    /** The participant account. */
    @XmlElement(required = true)
    protected ParticipantAccount participantAccount;

    /**
     * Gets the value of the participantIDs property.
     * 
     * @return the participant i ds
     * 
     * possible object is
     * {@link ListOfParticipantAccountIDs }
     */
    public ListOfParticipantAccountIDs getParticipantIDs() {
        return participantIDs;
    }

    /**
     * Sets the value of the participantIDs property.
     * 
     * @param value allowed object is
     * {@link ListOfParticipantAccountIDs }
     */
    public void setParticipantIDs(ListOfParticipantAccountIDs value) {
        this.participantIDs = value;
    }

    /**
     * Gets the value of the participantGroup property.
     * 
     * @return the participant group
     * 
     * possible object is
     * {@link String }
     */
    public String getParticipantGroup() {
        return participantGroup;
    }

    /**
     * Sets the value of the participantGroup property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParticipantGroup(String value) {
        this.participantGroup = value;
    }

    /**
     * Gets the value of the participantAccount property.
     * 
     * @return the participant account
     * 
     * possible object is
     * {@link ParticipantAccount }
     */
    public ParticipantAccount getParticipantAccount() {
        return participantAccount;
    }

    /**
     * Sets the value of the participantAccount property.
     * 
     * @param value allowed object is
     * {@link ParticipantAccount }
     */
    public void setParticipantAccount(ParticipantAccount value) {
        this.participantAccount = value;
    }

}
