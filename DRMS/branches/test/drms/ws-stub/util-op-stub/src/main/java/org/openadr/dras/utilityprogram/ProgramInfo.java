/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityprogram.ProgramInfo.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityprogram;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.programconstraint.ProgramConstraint;


/**
 * This structure is used to specify information concerning
 * a program.
 * 
 * 
 * <p>Java class for ProgramInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProgramInfo">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="programConstraints" type="{http://www.openadr.org/DRAS/ProgramConstraint}ProgramConstraint" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProgramInfo", propOrder = {
    "programName",
    "programConstraints"
})
public class ProgramInfo
    implements Serializable
{

    /** The program name. */
    @XmlElement(required = true)
    protected String programName;
    
    /** The program constraints. */
    protected ProgramConstraint programConstraints;

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
     * Gets the value of the programConstraints property.
     * 
     * @return the program constraints
     * 
     * possible object is
     * {@link ProgramConstraint }
     */
    public ProgramConstraint getProgramConstraints() {
        return programConstraints;
    }

    /**
     * Sets the value of the programConstraints property.
     * 
     * @param value allowed object is
     * {@link ProgramConstraint }
     */
    public void setProgramConstraints(ProgramConstraint value) {
        this.programConstraints = value;
    }

}
