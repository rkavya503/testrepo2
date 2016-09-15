/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.UpdateRTPConfigs.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * <p>Java class for UpdateRTPConfigs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateRTPConfigs">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="rtpConfigs" type="{http://www.openadr.org/DRAS/AkuaRTPConfig}ListOfRTPConfigs"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateRTPConfigs", propOrder = {
    "programName",
    "rtpConfigs"
})
public class UpdateRTPConfigs
    implements Serializable
{

    /** The program name. */
    @XmlElement(nillable = true)
    protected String programName;
    
    /** The rtp configs. */
    @XmlElement(required = true)
    protected ListOfRTPConfigs rtpConfigs;

    /**
     * Gets the value of the programName property.
     * 
     * @return the program name
     * 
     * possible object is
     * {@link String }
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the value of the programName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setProgramName(String value) {
        this.programName = value;
    }

    /**
     * Gets the value of the rtpConfigs property.
     * 
     * @return the rtp configs
     * 
     * possible object is
     * {@link ListOfRTPConfigs }
     */
    public ListOfRTPConfigs getRtpConfigs() {
        return rtpConfigs;
    }

    /**
     * Sets the value of the rtpConfigs property.
     * 
     * @param value allowed object is
     * {@link ListOfRTPConfigs }
     */
    public void setRtpConfigs(ListOfRTPConfigs value) {
        this.rtpConfigs = value;
    }

}
