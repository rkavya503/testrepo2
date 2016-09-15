/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.GetProgramsResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuautilityprogram.AkuaUtilityProgramList;


/**
 * <p>Java class for GetProgramsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetProgramsResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programs" type="{http://www.openadr.org/DRAS/AkuaUtilityProgram}AkuaUtilityProgramList" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetProgramsResponse", propOrder = {
    "programs",
    "returnValue"
})
public class GetProgramsResponse
    implements Serializable
{

    /** The programs. */
    @XmlElement(nillable = true)
    protected AkuaUtilityProgramList programs;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the programs property.
     * 
     * @return the programs
     * 
     * possible object is
     * {@link AkuaUtilityProgramList }
     */
    public AkuaUtilityProgramList getPrograms() {
        return programs;
    }

    /**
     * Sets the value of the programs property.
     * 
     * @param value allowed object is
     * {@link AkuaUtilityProgramList }
     */
    public void setPrograms(AkuaUtilityProgramList value) {
        this.programs = value;
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
