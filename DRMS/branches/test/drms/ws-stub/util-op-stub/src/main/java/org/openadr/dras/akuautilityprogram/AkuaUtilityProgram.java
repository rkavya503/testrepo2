/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityprogram.AkuaUtilityProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityprogram;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openadr.dras.utilityprogram.UtilityProgram;


/**
 * <p>Java class for AkuaUtilityProgram complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaUtilityProgram">
 * &lt;complexContent>
 * &lt;extension base="{http://www.openadr.org/DRAS/UtilityProgram}UtilityProgram">
 * &lt;sequence>
 * &lt;element name="validatorClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="utiltyProgramName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="uiScheduleEventString" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="notificationParam1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="testProgram" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="repeatingEvents" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="remoteProgram" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="contactEmails" type="{http://www.openadr.org/DRAS/AkuaUtilityProgram}AkuaContactEmailList" minOccurs="0"/>
 * &lt;element name="minIssueToStartM" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="mustIssueBDBE" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="maxIssueTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="minStartTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="maxStartTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="minEndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="maxEndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="minDurationM" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="maxDurationM" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="pendingTimeDBE" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="manualCreatable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * &lt;element name="signals" type="{http://www.openadr.org/DRAS/AkuaUtilityProgram}AkuaSignalList"/>
 * &lt;element name="modeTransitions" type="{http://www.openadr.org/DRAS/AkuaUtilityProgram}AkuaModeTransitionList" minOccurs="0"/>
 * &lt;element name="bidConfig" type="{http://www.openadr.org/DRAS/AkuaUtilityProgram}AkuaBidConfig" minOccurs="0"/>
 * &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;/sequence>
 * &lt;/extension>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaUtilityProgram", propOrder = {
    "validatorClass",
    "utiltyProgramName",
    "className",
    "uiScheduleEventString",
    "notificationParam1",
    "testProgram",
    "repeatingEvents",
    "remoteProgram",
    "contactEmails",
    "minIssueToStartM",
    "mustIssueBDBE",
    "maxIssueTime",
    "minStartTime",
    "maxStartTime",
    "minEndTime",
    "maxEndTime",
    "minDurationM",
    "maxDurationM",
    "pendingTimeDBE",
    "manualCreatable",
    "signals",
    "modeTransitions",
    "bidConfig",
    "startDate",
    "endDate"
})
public class AkuaUtilityProgram
    extends UtilityProgram
    implements Serializable
{

    /** The validator class. */
    @XmlElement(required = true)
    protected String validatorClass;
    
    /** The utilty program name. */
    @XmlElement(required = true)
    protected String utiltyProgramName;
    
    /** The class name. */
    @XmlElement(required = true)
    protected String className;
    
    /** The ui schedule event string. */
    @XmlElement(required = true)
    protected String uiScheduleEventString;
    
    /** The notification param1. */
    @XmlElement(required = true)
    protected String notificationParam1;
    
    /** The test program. */
    protected boolean testProgram;
    
    /** The repeating events. */
    protected boolean repeatingEvents;
    
    /** The remote program. */
    protected boolean remoteProgram;
    
    /** The contact emails. */
    @XmlElement(nillable = true)
    protected AkuaContactEmailList contactEmails;
    
    /** The min issue to start m. */
    protected int minIssueToStartM;
    
    /** The must issue bdbe. */
    protected boolean mustIssueBDBE;
    
    /** The max issue time. */
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar maxIssueTime;
    
    /** The min start time. */
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar minStartTime;
    
    /** The max start time. */
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar maxStartTime;
    
    /** The min end time. */
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar minEndTime;
    
    /** The max end time. */
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar maxEndTime;
    
    /** The min duration m. */
    protected int minDurationM;
    
    /** The max duration m. */
    protected int maxDurationM;
    
    /** The pending time dbe. */
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar pendingTimeDBE;
    
    /** The manual creatable. */
    protected boolean manualCreatable;
    
    /** The signals. */
    @XmlElement(required = true)
    protected AkuaSignalList signals;
    
    /** The mode transitions. */
    @XmlElement(nillable = true)
    protected AkuaModeTransitionList modeTransitions;
    
    /** The bid config. */
    @XmlElement(nillable = true)
    protected AkuaBidConfig bidConfig;
    
    /** The start date. */
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startDate;
    
    /** The end date. */
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endDate;

    /**
     * Gets the value of the validatorClass property.
     * 
     * @return the validator class
     * 
     * possible object is
     * {@link String }
     */
    public String getValidatorClass() {
        return validatorClass;
    }

    /**
     * Sets the value of the validatorClass property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setValidatorClass(String value) {
        this.validatorClass = value;
    }

    /**
     * Gets the value of the utiltyProgramName property.
     * 
     * @return the utilty program name
     * 
     * possible object is
     * {@link String }
     */
    public String getUtiltyProgramName() {
        return utiltyProgramName;
    }

    /**
     * Sets the value of the utiltyProgramName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setUtiltyProgramName(String value) {
        this.utiltyProgramName = value;
    }

    /**
     * Gets the value of the className property.
     * 
     * @return the class name
     * 
     * possible object is
     * {@link String }
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the value of the className property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setClassName(String value) {
        this.className = value;
    }

    /**
     * Gets the value of the uiScheduleEventString property.
     * 
     * @return the ui schedule event string
     * 
     * possible object is
     * {@link String }
     */
    public String getUiScheduleEventString() {
        return uiScheduleEventString;
    }

    /**
     * Sets the value of the uiScheduleEventString property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setUiScheduleEventString(String value) {
        this.uiScheduleEventString = value;
    }

    /**
     * Gets the value of the notificationParam1 property.
     * 
     * @return the notification param1
     * 
     * possible object is
     * {@link String }
     */
    public String getNotificationParam1() {
        return notificationParam1;
    }

    /**
     * Sets the value of the notificationParam1 property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setNotificationParam1(String value) {
        this.notificationParam1 = value;
    }

    /**
     * Gets the value of the testProgram property.
     * 
     * @return true, if checks if is test program
     */
    public boolean isTestProgram() {
        return testProgram;
    }

    /**
     * Sets the value of the testProgram property.
     * 
     * @param value the value
     */
    public void setTestProgram(boolean value) {
        this.testProgram = value;
    }

    /**
     * Gets the value of the repeatingEvents property.
     * 
     * @return true, if checks if is repeating events
     */
    public boolean isRepeatingEvents() {
        return repeatingEvents;
    }

    /**
     * Sets the value of the repeatingEvents property.
     * 
     * @param value the value
     */
    public void setRepeatingEvents(boolean value) {
        this.repeatingEvents = value;
    }

    /**
     * Gets the value of the remoteProgram property.
     * 
     * @return true, if checks if is remote program
     */
    public boolean isRemoteProgram() {
        return remoteProgram;
    }

    /**
     * Sets the value of the remoteProgram property.
     * 
     * @param value the value
     */
    public void setRemoteProgram(boolean value) {
        this.remoteProgram = value;
    }

    /**
     * Gets the value of the contactEmails property.
     * 
     * @return the contact emails
     * 
     * possible object is
     * {@link AkuaContactEmailList }
     */
    public AkuaContactEmailList getContactEmails() {
        return contactEmails;
    }

    /**
     * Sets the value of the contactEmails property.
     * 
     * @param value allowed object is
     * {@link AkuaContactEmailList }
     */
    public void setContactEmails(AkuaContactEmailList value) {
        this.contactEmails = value;
    }

    /**
     * Gets the value of the minIssueToStartM property.
     * 
     * @return the min issue to start m
     */
    public int getMinIssueToStartM() {
        return minIssueToStartM;
    }

    /**
     * Sets the value of the minIssueToStartM property.
     * 
     * @param value the value
     */
    public void setMinIssueToStartM(int value) {
        this.minIssueToStartM = value;
    }

    /**
     * Gets the value of the mustIssueBDBE property.
     * 
     * @return true, if checks if is must issue bdbe
     */
    public boolean isMustIssueBDBE() {
        return mustIssueBDBE;
    }

    /**
     * Sets the value of the mustIssueBDBE property.
     * 
     * @param value the value
     */
    public void setMustIssueBDBE(boolean value) {
        this.mustIssueBDBE = value;
    }

    /**
     * Gets the value of the maxIssueTime property.
     * 
     * @return the max issue time
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getMaxIssueTime() {
        return maxIssueTime;
    }

    /**
     * Sets the value of the maxIssueTime property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setMaxIssueTime(XMLGregorianCalendar value) {
        this.maxIssueTime = value;
    }

    /**
     * Gets the value of the minStartTime property.
     * 
     * @return the min start time
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getMinStartTime() {
        return minStartTime;
    }

    /**
     * Sets the value of the minStartTime property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setMinStartTime(XMLGregorianCalendar value) {
        this.minStartTime = value;
    }

    /**
     * Gets the value of the maxStartTime property.
     * 
     * @return the max start time
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getMaxStartTime() {
        return maxStartTime;
    }

    /**
     * Sets the value of the maxStartTime property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setMaxStartTime(XMLGregorianCalendar value) {
        this.maxStartTime = value;
    }

    /**
     * Gets the value of the minEndTime property.
     * 
     * @return the min end time
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getMinEndTime() {
        return minEndTime;
    }

    /**
     * Sets the value of the minEndTime property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setMinEndTime(XMLGregorianCalendar value) {
        this.minEndTime = value;
    }

    /**
     * Gets the value of the maxEndTime property.
     * 
     * @return the max end time
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getMaxEndTime() {
        return maxEndTime;
    }

    /**
     * Sets the value of the maxEndTime property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setMaxEndTime(XMLGregorianCalendar value) {
        this.maxEndTime = value;
    }

    /**
     * Gets the value of the minDurationM property.
     * 
     * @return the min duration m
     */
    public int getMinDurationM() {
        return minDurationM;
    }

    /**
     * Sets the value of the minDurationM property.
     * 
     * @param value the value
     */
    public void setMinDurationM(int value) {
        this.minDurationM = value;
    }

    /**
     * Gets the value of the maxDurationM property.
     * 
     * @return the max duration m
     */
    public int getMaxDurationM() {
        return maxDurationM;
    }

    /**
     * Sets the value of the maxDurationM property.
     * 
     * @param value the value
     */
    public void setMaxDurationM(int value) {
        this.maxDurationM = value;
    }

    /**
     * Gets the value of the pendingTimeDBE property.
     * 
     * @return the pending time dbe
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getPendingTimeDBE() {
        return pendingTimeDBE;
    }

    /**
     * Sets the value of the pendingTimeDBE property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setPendingTimeDBE(XMLGregorianCalendar value) {
        this.pendingTimeDBE = value;
    }

    /**
     * Gets the value of the manualCreatable property.
     * 
     * @return true, if checks if is manual creatable
     */
    public boolean isManualCreatable() {
        return manualCreatable;
    }

    /**
     * Sets the value of the manualCreatable property.
     * 
     * @param value the value
     */
    public void setManualCreatable(boolean value) {
        this.manualCreatable = value;
    }

    /**
     * Gets the value of the signals property.
     * 
     * @return the signals
     * 
     * possible object is
     * {@link AkuaSignalList }
     */
    public AkuaSignalList getSignals() {
        return signals;
    }

    /**
     * Sets the value of the signals property.
     * 
     * @param value allowed object is
     * {@link AkuaSignalList }
     */
    public void setSignals(AkuaSignalList value) {
        this.signals = value;
    }

    /**
     * Gets the value of the modeTransitions property.
     * 
     * @return the mode transitions
     * 
     * possible object is
     * {@link AkuaModeTransitionList }
     */
    public AkuaModeTransitionList getModeTransitions() {
        return modeTransitions;
    }

    /**
     * Sets the value of the modeTransitions property.
     * 
     * @param value allowed object is
     * {@link AkuaModeTransitionList }
     */
    public void setModeTransitions(AkuaModeTransitionList value) {
        this.modeTransitions = value;
    }

    /**
     * Gets the value of the bidConfig property.
     * 
     * @return the bid config
     * 
     * possible object is
     * {@link AkuaBidConfig }
     */
    public AkuaBidConfig getBidConfig() {
        return bidConfig;
    }

    /**
     * Sets the value of the bidConfig property.
     * 
     * @param value allowed object is
     * {@link AkuaBidConfig }
     */
    public void setBidConfig(AkuaBidConfig value) {
        this.bidConfig = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return the start date
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return the end date
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

}
