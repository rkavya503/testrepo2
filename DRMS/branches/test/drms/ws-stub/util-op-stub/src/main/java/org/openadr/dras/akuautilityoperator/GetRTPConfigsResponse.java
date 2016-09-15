/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.GetRTPConfigsResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuartpconfig.ListOfRTPConfigs;


/**
 * <p>Java class for GetRTPConfigsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetRTPConfigsResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="rtpConfigTable" type="{http://www.openadr.org/DRAS/AkuaRTPConfig}ListOfRTPConfigs" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetRTPConfigsResponse", propOrder = {
    "rtpConfigTable",
    "returnValue"
})
public class GetRTPConfigsResponse
    implements Serializable
{

    /** The rtp config table. */
    @XmlElement(nillable = true)
    protected ListOfRTPConfigs rtpConfigTable;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the rtpConfigTable property.
     * 
     * @return the rtp config table
     * 
     * possible object is
     * {@link ListOfRTPConfigs }
     */
    public ListOfRTPConfigs getRtpConfigTable() {
        return rtpConfigTable;
    }

    /**
     * Sets the value of the rtpConfigTable property.
     * 
     * @param value allowed object is
     * {@link ListOfRTPConfigs }
     */
    public void setRtpConfigTable(ListOfRTPConfigs value) {
        this.rtpConfigTable = value;
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
