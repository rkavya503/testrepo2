/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.GetSeasonConfigsResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuaseasonconfig.ListOfSeasonConfigs;


/**
 * <p>Java class for GetSeasonConfigsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSeasonConfigsResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="seasonConfigTable" type="{http://www.openadr.org/DRAS/AkuaSeasonConfig}ListOfSeasonConfigs" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSeasonConfigsResponse", propOrder = {
    "seasonConfigTable",
    "returnValue"
})
public class GetSeasonConfigsResponse
    implements Serializable
{

    /** The season config table. */
    @XmlElement(nillable = true)
    protected ListOfSeasonConfigs seasonConfigTable;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the seasonConfigTable property.
     * 
     * @return the season config table
     * 
     * possible object is
     * {@link ListOfSeasonConfigs }
     */
    public ListOfSeasonConfigs getSeasonConfigTable() {
        return seasonConfigTable;
    }

    /**
     * Sets the value of the seasonConfigTable property.
     * 
     * @param value allowed object is
     * {@link ListOfSeasonConfigs }
     */
    public void setSeasonConfigTable(ListOfSeasonConfigs value) {
        this.seasonConfigTable = value;
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
