/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.GetDRASTransactions.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.programconstraint.DateTimeWindow;
import org.openadr.dras.utilityprogram.ListOfIDs;


/**
 * <p>Java class for GetDRASTransactions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDRASTransactions">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="userNames" type="{http://www.openadr.org/DRAS/UtilityProgram}ListOfIDs"/>
 * &lt;element name="DRASClientIDs" type="{http://www.openadr.org/DRAS/UtilityProgram}ListOfIDs" minOccurs="0"/>
 * &lt;element name="participantIDs" type="{http://www.openadr.org/DRAS/UtilityProgram}ListOfIDs" minOccurs="0"/>
 * &lt;element name="methodName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="timeInterval" type="{http://www.openadr.org/DRAS/ProgramConstraint}DateTimeWindow" minOccurs="0"/>
 * &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDRASTransactions", propOrder = {
    "userNames",
    "drasClientIDs",
    "participantIDs",
    "methodName",
    "timeInterval",
    "resultCode"
})
public class GetDRASTransactions
    implements Serializable
{

    /** The user names. */
    @XmlElement(required = true)
    protected ListOfIDs userNames;
    
    /** The dras client i ds. */
    @XmlElement(name = "DRASClientIDs", nillable = true)
    protected ListOfIDs drasClientIDs;
    
    /** The participant i ds. */
    @XmlElement(nillable = true)
    protected ListOfIDs participantIDs;
    
    /** The method name. */
    @XmlElement(nillable = true)
    protected String methodName;
    
    /** The time interval. */
    @XmlElement(nillable = true)
    protected DateTimeWindow timeInterval;
    
    /** The result code. */
    @XmlElement(nillable = true)
    protected String resultCode;

    /**
     * Gets the value of the userNames property.
     * 
     * @return the user names
     * 
     * possible object is
     * {@link ListOfIDs }
     */
    public ListOfIDs getUserNames() {
        return userNames;
    }

    /**
     * Sets the value of the userNames property.
     * 
     * @param value allowed object is
     * {@link ListOfIDs }
     */
    public void setUserNames(ListOfIDs value) {
        this.userNames = value;
    }

    /**
     * Gets the value of the drasClientIDs property.
     * 
     * @return the DRAS client i ds
     * 
     * possible object is
     * {@link ListOfIDs }
     */
    public ListOfIDs getDRASClientIDs() {
        return drasClientIDs;
    }

    /**
     * Sets the value of the drasClientIDs property.
     * 
     * @param value allowed object is
     * {@link ListOfIDs }
     */
    public void setDRASClientIDs(ListOfIDs value) {
        this.drasClientIDs = value;
    }

    /**
     * Gets the value of the participantIDs property.
     * 
     * @return the participant i ds
     * 
     * possible object is
     * {@link ListOfIDs }
     */
    public ListOfIDs getParticipantIDs() {
        return participantIDs;
    }

    /**
     * Sets the value of the participantIDs property.
     * 
     * @param value allowed object is
     * {@link ListOfIDs }
     */
    public void setParticipantIDs(ListOfIDs value) {
        this.participantIDs = value;
    }

    /**
     * Gets the value of the methodName property.
     * 
     * @return the method name
     * 
     * possible object is
     * {@link String }
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Sets the value of the methodName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setMethodName(String value) {
        this.methodName = value;
    }

    /**
     * Gets the value of the timeInterval property.
     * 
     * @return the time interval
     * 
     * possible object is
     * {@link DateTimeWindow }
     */
    public DateTimeWindow getTimeInterval() {
        return timeInterval;
    }

    /**
     * Sets the value of the timeInterval property.
     * 
     * @param value allowed object is
     * {@link DateTimeWindow }
     */
    public void setTimeInterval(DateTimeWindow value) {
        this.timeInterval = value;
    }

    /**
     * Gets the value of the resultCode property.
     * 
     * @return the result code
     * 
     * possible object is
     * {@link String }
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setResultCode(String value) {
        this.resultCode = value;
    }

}
