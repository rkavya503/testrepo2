/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.GetProgramInformationResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.eventinfo.ListOfEventInfoType;
import org.openadr.dras.programconstraint.ProgramConstraint;


/**
 * <p>Java class for GetProgramInformationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetProgramInformationResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programConstraints" type="{http://www.openadr.org/DRAS/ProgramConstraint}ProgramConstraint" minOccurs="0"/>
 * &lt;element name="eventInfo" type="{http://www.openadr.org/DRAS/EventInfo}ListOfEventInfoType" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetProgramInformationResponse", propOrder = {
    "programConstraints",
    "eventInfo",
    "returnValue"
})
public class GetProgramInformationResponse
    implements Serializable
{

    /** The program constraints. */
    protected ProgramConstraint programConstraints;
    
    /** The event info. */
    @XmlElement(nillable = true)
    protected ListOfEventInfoType eventInfo;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

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

    /**
     * Gets the value of the eventInfo property.
     * 
     * @return the event info
     * 
     * possible object is
     * {@link ListOfEventInfoType }
     */
    public ListOfEventInfoType getEventInfo() {
        return eventInfo;
    }

    /**
     * Sets the value of the eventInfo property.
     * 
     * @param value allowed object is
     * {@link ListOfEventInfoType }
     */
    public void setEventInfo(ListOfEventInfoType value) {
        this.eventInfo = value;
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
