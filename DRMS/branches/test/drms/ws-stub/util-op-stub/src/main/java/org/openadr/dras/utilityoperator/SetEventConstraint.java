/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.SetEventConstraint.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.programconstraint.ProgramConstraint;


/**
 * <p>Java class for SetEventConstraint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SetEventConstraint">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="constraint" type="{http://www.openadr.org/DRAS/ProgramConstraint}ProgramConstraint"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetEventConstraint", propOrder = {
    "eventID",
    "constraint"
})
public class SetEventConstraint
    implements Serializable
{

    /** The event id. */
    @XmlElement(required = true)
    protected String eventID;
    
    /** The constraint. */
    @XmlElement(required = true)
    protected ProgramConstraint constraint;

    /**
     * Gets the value of the eventID property.
     * 
     * @return the event id
     * 
     * possible object is
     * {@link String }
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the value of the eventID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setEventID(String value) {
        this.eventID = value;
    }

    /**
     * Gets the value of the constraint property.
     * 
     * @return the constraint
     * 
     * possible object is
     * {@link ProgramConstraint }
     */
    public ProgramConstraint getConstraint() {
        return constraint;
    }

    /**
     * Sets the value of the constraint property.
     * 
     * @param value allowed object is
     * {@link ProgramConstraint }
     */
    public void setConstraint(ProgramConstraint value) {
        this.constraint = value;
    }

}
