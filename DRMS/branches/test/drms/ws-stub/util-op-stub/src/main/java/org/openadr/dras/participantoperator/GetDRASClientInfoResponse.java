/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.GetDRASClientInfoResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.drasclient.ListOfDRASClients;


/**
 * <p>Java class for GetDRASClientInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDRASClientInfoResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DRASClients" type="{http://www.openadr.org/DRAS/DRASClient}ListOfDRASClients" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDRASClientInfoResponse", propOrder = {
    "drasClients",
    "returnValue"
})
public class GetDRASClientInfoResponse
    implements Serializable
{

    /** The dras clients. */
    @XmlElement(name = "DRASClients", nillable = true)
    protected ListOfDRASClients drasClients;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the drasClients property.
     * 
     * @return the DRAS clients
     * 
     * possible object is
     * {@link ListOfDRASClients }
     */
    public ListOfDRASClients getDRASClients() {
        return drasClients;
    }

    /**
     * Sets the value of the drasClients property.
     * 
     * @param value allowed object is
     * {@link ListOfDRASClients }
     */
    public void setDRASClients(ListOfDRASClients value) {
        this.drasClients = value;
    }

    /**
     * Gets the value of the returnValue property.
     * 
     * @return the return value
     * 
     * possible object is
     * {@link String }
     */
    public String getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the value of the returnValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setReturnValue(String value) {
        this.returnValue = value;
    }

}
