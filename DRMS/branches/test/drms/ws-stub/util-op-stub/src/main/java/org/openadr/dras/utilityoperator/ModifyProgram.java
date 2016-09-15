/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.ModifyProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.utilityprogram.UtilityProgram;


/**
 * <p>Java class for ModifyProgram complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModifyProgram">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="program" type="{http://www.openadr.org/DRAS/UtilityProgram}UtilityProgram"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyProgram", propOrder = {
    "program"
})
public class ModifyProgram
    implements Serializable
{

    /** The program. */
    @XmlElement(required = true)
    protected UtilityProgram program;

    /**
     * Gets the value of the program property.
     * 
     * @return the program
     * 
     * possible object is
     * {@link UtilityProgram }
     */
    public UtilityProgram getProgram() {
        return program;
    }

    /**
     * Sets the value of the program property.
     * 
     * @param value allowed object is
     * {@link UtilityProgram }
     */
    public void setProgram(UtilityProgram value) {
        this.program = value;
    }

}
