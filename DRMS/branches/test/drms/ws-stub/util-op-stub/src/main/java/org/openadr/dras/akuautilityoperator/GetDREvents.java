/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.GetDREvents.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuautilitydrevent.ListOfEventIDs;


/**
 * <p>Java class for GetDREvents complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDREvents">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="DREventNames" type="{http://www.openadr.org/DRAS/AkuaUtilityDREvent}ListOfEventIDs" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDREvents", propOrder = {
    "programName",
    "drEventNames"
})
public class GetDREvents
    implements Serializable
{

    /** The program name. */
    @XmlElement(required = true)
    protected String programName;
    
    /** The dr event names. */
    @XmlElement(name = "DREventNames", nillable = true)
    protected ListOfEventIDs drEventNames;

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
     * Gets the value of the drEventNames property.
     * 
     * @return the DR event names
     * 
     * possible object is
     * {@link ListOfEventIDs }
     */
    public ListOfEventIDs getDREventNames() {
        return drEventNames;
    }

    /**
     * Sets the value of the drEventNames property.
     * 
     * @param value allowed object is
     * {@link ListOfEventIDs }
     */
    public void setDREventNames(ListOfEventIDs value) {
        this.drEventNames = value;
    }

}
