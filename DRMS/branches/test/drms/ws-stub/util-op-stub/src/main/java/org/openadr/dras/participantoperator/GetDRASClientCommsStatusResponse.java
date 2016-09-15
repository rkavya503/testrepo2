/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.GetDRASClientCommsStatusResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.drasclient.ListOfCommsStatus;


/**
 * <p>Java class for GetDRASClientCommsStatusResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDRASClientCommsStatusResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DRASClientStatus" type="{http://www.openadr.org/DRAS/DRASClient}ListOfCommsStatus" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDRASClientCommsStatusResponse", propOrder = {
    "drasClientStatus",
    "returnValue"
})
public class GetDRASClientCommsStatusResponse
    implements Serializable
{

    /** The dras client status. */
    @XmlElement(name = "DRASClientStatus", nillable = true)
    protected ListOfCommsStatus drasClientStatus;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the drasClientStatus property.
     * 
     * @return the DRAS client status
     * 
     * possible object is
     * {@link ListOfCommsStatus }
     */
    public ListOfCommsStatus getDRASClientStatus() {
        return drasClientStatus;
    }

    /**
     * Sets the value of the drasClientStatus property.
     * 
     * @param value allowed object is
     * {@link ListOfCommsStatus }
     */
    public void setDRASClientStatus(ListOfCommsStatus value) {
        this.drasClientStatus = value;
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
