/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.responseschedule.Rule.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.responseschedule;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This is used to define a specific Rule. A Rule is a
 * value and corresponding equation such that if the
 * equation evaluates to true then the value is set. If the
 * equation is false then the value is not used.
 * 
 * 
 * <p>Java class for Rule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Rule">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="value">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="NORMAL"/>
 * &lt;enumeration value="MODERATE"/>
 * &lt;enumeration value="HIGH"/>
 * &lt;enumeration value="SPECIAL"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;element name="equation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rule", propOrder = {
    "value",
    "equation"
})
public class Rule
    implements Serializable
{

    /** The value. */
    @XmlElement(required = true)
    protected String value;
    
    /** The equation. */
    @XmlElement(required = true)
    protected String equation;

    /**
     * Gets the value of the value property.
     * 
     * @return the value
     * 
     * possible object is
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the equation property.
     * 
     * @return the equation
     * 
     * possible object is
     * {@link String }
     */
    public String getEquation() {
        return equation;
    }

    /**
     * Sets the value of the equation property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setEquation(String value) {
        this.equation = value;
    }

}
