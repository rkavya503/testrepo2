/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.ModifyDREventResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.programconstraint.ConstraintFilter;


/**
 * <p>Java class for ModifyDREventResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModifyDREventResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="constraintValue" type="{http://www.openadr.org/DRAS/ProgramConstraint}ConstraintFilter"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyDREventResponse", propOrder = {
    "returnValue",
    "constraintValue"
})
public class ModifyDREventResponse
    implements Serializable
{

    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;
    
    /** The constraint value. */
    @XmlElement(required = true)
    protected ConstraintFilter constraintValue;

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

    /**
     * Gets the value of the constraintValue property.
     * 
     * @return the constraint value
     * 
     * possible object is
     * {@link ConstraintFilter }
     */
    public ConstraintFilter getConstraintValue() {
        return constraintValue;
    }

    /**
     * Sets the value of the constraintValue property.
     * 
     * @param value allowed object is
     * {@link ConstraintFilter }
     */
    public void setConstraintValue(ConstraintFilter value) {
        this.constraintValue = value;
    }

}
