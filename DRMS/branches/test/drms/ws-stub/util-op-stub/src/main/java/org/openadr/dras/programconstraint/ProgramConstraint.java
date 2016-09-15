/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.programconstraint.ProgramConstraint.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.programconstraint;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Programs, Participant Accounts, and DRAS Clients may
 * have a set of program constraints that define various
 * date/time and duration constraints associated with a DR
 * Event with regards to a particular DR program. For
 * example a Utility may have a DR program that has the
 * flexibility (e.g. PG&amp;amp;E cafeteria plan) that
 * allows their participants to set their own event windows
 * and event durations. For intelligent DRAS Clients these
 * constraints may not need to be reflected in the DRAS
 * since they can be implemented at the facility, but for
 * simple DRAS Clients these constraints may be used to
 * further define the simple shed strategies associated
 * with a DR program and thus they need to be configured
 * within the DRAS.
 * 
 * When a DR event is issued by the Utility/ISO the event
 * is verified against the parameters and constraints that
 * were established when the program was set up. For
 * example if the program was configured to support DR
 * events between 12:00 and 16:00 then when a DR event is
 * issued the DRAS checks to verify that the DR event is
 * within 12:00 and 16:00. If it is not then the DRAS must
 * take some action to resolve the conflict.
 * 
 * For each program parameter (e.g. DR event window) that
 * specifies a constraint on issued DR events there is an
 * additional parameter that specifies how the DRAS will
 * respond to DR events that are issued, but do not satisfy
 * the configured constraints. The DRAS may respond to
 * issued DR events that conflict with configured
 * constraints in the following way ACCEPT simply
 * accept the issued DR event regardless of any conflicts.
 * REJECT reject any DR events that conflict with
 * configured constraints. FORCE regardless of what the
 * issued DR events parameters are (even if there is no
 * conflict) force them to be the parameters that were
 * configured as part of the program. RESTRICT modify
 * the DR event parameters so that they legally fall within
 * the bounds of the configured parameters.
 * 
 * Program constraints are always associated with a
 * specific program and may also be associated with one of
 * the following hierarchical entities Overall Program
 * Specific Participant Accont on the whole Specific DRAS
 * Client
 * 
 * Each of these entities is in essence a subset of the one
 * above it and therefore the time intervals od the
 * constraints that are specified are also by default
 * subsets of the ones above it even if they are not
 * specified in that fashion. For example if the constraint
 * that are associated with a program as a whole state that
 * DR events happen between 12 4 then even if the
 * participant or DRAS Client constraints state that DR
 * event can happen before 12 or after 4 they will never
 * happen in that time frame. Likewise the constraints
 * associated with the Participant as a whole further
 * constrain the DRAS Client constraint attributes by
 * default.
 * 
 * 
 * <p>Java class for ProgramConstraint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProgramConstraint">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventWindow" type="{http://www.openadr.org/DRAS/ProgramConstraint}EventWindow" minOccurs="0"/>
 * &lt;element name="eventWindowFilter" type="{http://www.openadr.org/DRAS/ProgramConstraint}ConstraintFilter" minOccurs="0"/>
 * &lt;element name="maxEventDuration" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 * &lt;element name="maxEventDurationFilter" type="{http://www.openadr.org/DRAS/ProgramConstraint}ConstraintFilter" minOccurs="0"/>
 * &lt;element name="notificationWindow" type="{http://www.openadr.org/DRAS/ProgramConstraint}NotificationWindow" minOccurs="0"/>
 * &lt;element name="notificationFilter" type="{http://www.openadr.org/DRAS/ProgramConstraint}ConstraintFilter" minOccurs="0"/>
 * &lt;element name="blackOutDateTimes" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="dateTimeWindow" type="{http://www.openadr.org/DRAS/ProgramConstraint}DateTimeWindow" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="blackOutDateTimeFilter" type="{http://www.openadr.org/DRAS/ProgramConstraint}ConstraintFilter" minOccurs="0"/>
 * &lt;element name="validDateTimes" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="dateTimeWindow" type="{http://www.openadr.org/DRAS/ProgramConstraint}DateTimeWindow" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="maxConsecutiveDays" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" minOccurs="0"/>
 * &lt;element name="maxConsecutiveDaysFilter" minOccurs="0">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="ACCEPT"/>
 * &lt;enumeration value="REJECT"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;attribute name="constraintID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="programName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="DRASClientID" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProgramConstraint", propOrder = {
    "eventWindow",
    "eventWindowFilter",
    "maxEventDuration",
    "maxEventDurationFilter",
    "notificationWindow",
    "notificationFilter",
    "blackOutDateTimes",
    "blackOutDateTimeFilter",
    "validDateTimes",
    "maxConsecutiveDays",
    "maxConsecutiveDaysFilter"
})
public class ProgramConstraint
    implements Serializable
{

    /** The event window. */
    @XmlElement(nillable = true)
    protected EventWindow eventWindow;
    
    /** The event window filter. */
    @XmlElement(nillable = true)
    protected ConstraintFilter eventWindowFilter;
    
    /** The max event duration. */
    protected Double maxEventDuration;
    
    /** The max event duration filter. */
    @XmlElement(nillable = true)
    protected ConstraintFilter maxEventDurationFilter;
    
    /** The notification window. */
    @XmlElement(nillable = true)
    protected NotificationWindow notificationWindow;
    
    /** The notification filter. */
    @XmlElement(nillable = true)
    protected ConstraintFilter notificationFilter;
    
    /** The black out date times. */
    @XmlElement(nillable = true)
    protected ProgramConstraint.BlackOutDateTimes blackOutDateTimes;
    
    /** The black out date time filter. */
    @XmlElement(nillable = true)
    protected ConstraintFilter blackOutDateTimeFilter;
    
    /** The valid date times. */
    @XmlElement(nillable = true)
    protected ProgramConstraint.ValidDateTimes validDateTimes;
    
    /** The max consecutive days. */
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger maxConsecutiveDays;
    
    /** The max consecutive days filter. */
    @XmlElement(nillable = true)
    protected String maxConsecutiveDaysFilter;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The constraint id. */
    @XmlAttribute(required = true)
    protected String constraintID;
    
    /** The program name. */
    @XmlAttribute(required = true)
    protected String programName;
    
    /** The participant id. */
    @XmlAttribute
    protected String participantID;
    
    /** The dras client id. */
    @XmlAttribute(name = "DRASClientID")
    protected String drasClientID;

    /**
     * Gets the value of the eventWindow property.
     * 
     * @return the event window
     * 
     * possible object is
     * {@link EventWindow }
     */
    public EventWindow getEventWindow() {
        return eventWindow;
    }

    /**
     * Sets the value of the eventWindow property.
     * 
     * @param value allowed object is
     * {@link EventWindow }
     */
    public void setEventWindow(EventWindow value) {
        this.eventWindow = value;
    }

    /**
     * Gets the value of the eventWindowFilter property.
     * 
     * @return the event window filter
     * 
     * possible object is
     * {@link ConstraintFilter }
     */
    public ConstraintFilter getEventWindowFilter() {
        return eventWindowFilter;
    }

    /**
     * Sets the value of the eventWindowFilter property.
     * 
     * @param value allowed object is
     * {@link ConstraintFilter }
     */
    public void setEventWindowFilter(ConstraintFilter value) {
        this.eventWindowFilter = value;
    }

    /**
     * Gets the value of the maxEventDuration property.
     * 
     * @return the max event duration
     * 
     * possible object is
     * {@link Double }
     */
    public Double getMaxEventDuration() {
        return maxEventDuration;
    }

    /**
     * Sets the value of the maxEventDuration property.
     * 
     * @param value allowed object is
     * {@link Double }
     */
    public void setMaxEventDuration(Double value) {
        this.maxEventDuration = value;
    }

    /**
     * Gets the value of the maxEventDurationFilter property.
     * 
     * @return the max event duration filter
     * 
     * possible object is
     * {@link ConstraintFilter }
     */
    public ConstraintFilter getMaxEventDurationFilter() {
        return maxEventDurationFilter;
    }

    /**
     * Sets the value of the maxEventDurationFilter property.
     * 
     * @param value allowed object is
     * {@link ConstraintFilter }
     */
    public void setMaxEventDurationFilter(ConstraintFilter value) {
        this.maxEventDurationFilter = value;
    }

    /**
     * Gets the value of the notificationWindow property.
     * 
     * @return the notification window
     * 
     * possible object is
     * {@link NotificationWindow }
     */
    public NotificationWindow getNotificationWindow() {
        return notificationWindow;
    }

    /**
     * Sets the value of the notificationWindow property.
     * 
     * @param value allowed object is
     * {@link NotificationWindow }
     */
    public void setNotificationWindow(NotificationWindow value) {
        this.notificationWindow = value;
    }

    /**
     * Gets the value of the notificationFilter property.
     * 
     * @return the notification filter
     * 
     * possible object is
     * {@link ConstraintFilter }
     */
    public ConstraintFilter getNotificationFilter() {
        return notificationFilter;
    }

    /**
     * Sets the value of the notificationFilter property.
     * 
     * @param value allowed object is
     * {@link ConstraintFilter }
     */
    public void setNotificationFilter(ConstraintFilter value) {
        this.notificationFilter = value;
    }

    /**
     * Gets the value of the blackOutDateTimes property.
     * 
     * @return the black out date times
     * 
     * possible object is
     * {@link ProgramConstraint.BlackOutDateTimes }
     */
    public ProgramConstraint.BlackOutDateTimes getBlackOutDateTimes() {
        return blackOutDateTimes;
    }

    /**
     * Sets the value of the blackOutDateTimes property.
     * 
     * @param value allowed object is
     * {@link ProgramConstraint.BlackOutDateTimes }
     */
    public void setBlackOutDateTimes(ProgramConstraint.BlackOutDateTimes value) {
        this.blackOutDateTimes = value;
    }

    /**
     * Gets the value of the blackOutDateTimeFilter property.
     * 
     * @return the black out date time filter
     * 
     * possible object is
     * {@link ConstraintFilter }
     */
    public ConstraintFilter getBlackOutDateTimeFilter() {
        return blackOutDateTimeFilter;
    }

    /**
     * Sets the value of the blackOutDateTimeFilter property.
     * 
     * @param value allowed object is
     * {@link ConstraintFilter }
     */
    public void setBlackOutDateTimeFilter(ConstraintFilter value) {
        this.blackOutDateTimeFilter = value;
    }

    /**
     * Gets the value of the validDateTimes property.
     * 
     * @return the valid date times
     * 
     * possible object is
     * {@link ProgramConstraint.ValidDateTimes }
     */
    public ProgramConstraint.ValidDateTimes getValidDateTimes() {
        return validDateTimes;
    }

    /**
     * Sets the value of the validDateTimes property.
     * 
     * @param value allowed object is
     * {@link ProgramConstraint.ValidDateTimes }
     */
    public void setValidDateTimes(ProgramConstraint.ValidDateTimes value) {
        this.validDateTimes = value;
    }

    /**
     * Gets the value of the maxConsecutiveDays property.
     * 
     * @return the max consecutive days
     * 
     * possible object is
     * {@link BigInteger }
     */
    public BigInteger getMaxConsecutiveDays() {
        return maxConsecutiveDays;
    }

    /**
     * Sets the value of the maxConsecutiveDays property.
     * 
     * @param value allowed object is
     * {@link BigInteger }
     */
    public void setMaxConsecutiveDays(BigInteger value) {
        this.maxConsecutiveDays = value;
    }

    /**
     * Gets the value of the maxConsecutiveDaysFilter property.
     * 
     * @return the max consecutive days filter
     * 
     * possible object is
     * {@link String }
     */
    public String getMaxConsecutiveDaysFilter() {
        return maxConsecutiveDaysFilter;
    }

    /**
     * Sets the value of the maxConsecutiveDaysFilter property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setMaxConsecutiveDaysFilter(String value) {
        this.maxConsecutiveDaysFilter = value;
    }

    /**
     * Gets the value of the schemaVersion property.
     * 
     * @return the schema version
     * 
     * possible object is
     * {@link String }
     */
    public String getSchemaVersion() {
        if (schemaVersion == null) {
            return "1.0";
        } else {
            return schemaVersion;
        }
    }

    /**
     * Sets the value of the schemaVersion property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setSchemaVersion(String value) {
        this.schemaVersion = value;
    }

    /**
     * Gets the value of the constraintID property.
     * 
     * @return the constraint id
     * 
     * possible object is
     * {@link String }
     */
    public String getConstraintID() {
        return constraintID;
    }

    /**
     * Sets the value of the constraintID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setConstraintID(String value) {
        this.constraintID = value;
    }

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
     * Gets the value of the participantID property.
     * 
     * @return the participant id
     * 
     * possible object is
     * {@link String }
     */
    public String getParticipantID() {
        return participantID;
    }

    /**
     * Sets the value of the participantID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setParticipantID(String value) {
        this.participantID = value;
    }

    /**
     * Gets the value of the drasClientID property.
     * 
     * @return the DRAS client id
     * 
     * possible object is
     * {@link String }
     */
    public String getDRASClientID() {
        return drasClientID;
    }

    /**
     * Sets the value of the drasClientID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDRASClientID(String value) {
        this.drasClientID = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="dateTimeWindow" type="{http://www.openadr.org/DRAS/ProgramConstraint}DateTimeWindow" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dateTimeWindow"
    })
    public static class BlackOutDateTimes
        implements Serializable
    {

        /** The date time window. */
        @XmlElement(nillable = true)
        protected List<DateTimeWindow> dateTimeWindow;

        /**
         * Gets the value of the dateTimeWindow property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dateTimeWindow property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getDateTimeWindow().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DateTimeWindow }
         * 
         * @return the date time window
         */
        public List<DateTimeWindow> getDateTimeWindow() {
            if (dateTimeWindow == null) {
                dateTimeWindow = new ArrayList<DateTimeWindow>();
            }
            return this.dateTimeWindow;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="dateTimeWindow" type="{http://www.openadr.org/DRAS/ProgramConstraint}DateTimeWindow" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dateTimeWindow"
    })
    public static class ValidDateTimes
        implements Serializable
    {

        /** The date time window. */
        @XmlElement(nillable = true)
        protected List<DateTimeWindow> dateTimeWindow;

        /**
         * Gets the value of the dateTimeWindow property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dateTimeWindow property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getDateTimeWindow().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DateTimeWindow }
         * 
         * @return the date time window
         */
        public List<DateTimeWindow> getDateTimeWindow() {
            if (dateTimeWindow == null) {
                dateTimeWindow = new ArrayList<DateTimeWindow>();
            }
            return this.dateTimeWindow;
        }

    }

}
