/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.CreateProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuautilityprogram.AkuaUtilityProgram;


/**
 * <p>Java class for CreateProgram complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateProgram">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="program" type="{http://www.openadr.org/DRAS/AkuaUtilityProgram}AkuaUtilityProgram"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateProgram", propOrder = {
    "program"
})
public class CreateProgram
    implements Serializable
{

    /** The program. */
    @XmlElement(required = true)
    protected AkuaUtilityProgram program;

    /**
     * Gets the value of the program property.
     * 
     * @return the program
     * 
     * possible object is
     * {@link AkuaUtilityProgram }
     */
    public AkuaUtilityProgram getProgram() {
        return program;
    }

    /**
     * Sets the value of the program property.
     * 
     * @param value allowed object is
     * {@link AkuaUtilityProgram }
     */
    public void setProgram(AkuaUtilityProgram value) {
        this.program = value;
    }

}
