/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.ModifyParticipantAccounts.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;


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
 * &lt;element name="participantAccounts" type="{http://www.openadr.org/DRAS/ParticipantAccount}ListOfParticipantAccounts"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyParticipantAccounts", propOrder = {
    "participantAccounts"
})
public class ModifyParticipantAccounts
    implements Serializable
{

    /** The participant accounts. */
    @XmlElement(required = true)
    protected ListOfParticipantAccounts participantAccounts;

    /**
     * Gets the value of the participantAccounts property.
     * 
     * @return the participant accounts
     * 
     * possible object is
     * {@link ListOfParticipantAccounts }
     */
    public ListOfParticipantAccounts getParticipantAccounts() {
        return participantAccounts;
    }

    /**
     * Sets the value of the participantAccounts property.
     * 
     * @param value allowed object is
     * {@link ListOfParticipantAccounts }
     */
    public void setParticipantAccounts(ListOfParticipantAccounts value) {
        this.participantAccounts = value;
    }

}
