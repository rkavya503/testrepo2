/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilitydrevent.UtilityDREvent.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilitydrevent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openadr.dras.drasclient.Location;
import org.openadr.dras.eventinfo.EventInfoInstance;


/**
 * This is the information associated with a DR Event that
 * has been issued by a Utility/ISO.
 * 
 * 
 * <p>Java class for UtilityDREvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UtilityDREvent">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="destinations" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participants" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="groups" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="groupID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="locations" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="location" type="{http://www.openadr.org/DRAS/DRASClient}Location"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="drasClients" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="drasClientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="eventTiming">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="notificationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="biddingInformation" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="openingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;element name="closingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="eventInformation" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="eventInfoInstance" type="{http://www.openadr.org/DRAS/EventInfo}EventInfoInstance" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="eventIdentifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;attribute name="eventModNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 * &lt;attribute name="programName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="utilityITName" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UtilityDREvent", propOrder = {
    "destinations",
    "eventTiming",
    "biddingInformation",
    "eventInformation"
})
public class UtilityDREvent
    implements Serializable
{

    /** The destinations. */
    @XmlElement(nillable = true)
    protected UtilityDREvent.Destinations destinations;
    
    /** The event timing. */
    @XmlElement(required = true)
    protected UtilityDREvent.EventTiming eventTiming;
    
    /** The bidding information. */
    @XmlElement(nillable = true)
    protected UtilityDREvent.BiddingInformation biddingInformation;
    
    /** The event information. */
    @XmlElement(nillable = true)
    protected UtilityDREvent.EventInformation eventInformation;
    
    /** The event identifier. */
    @XmlAttribute(required = true)
    protected String eventIdentifier;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The event mod number. */
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "unsignedInt")
    protected long eventModNumber;
    
    /** The program name. */
    @XmlAttribute(required = true)
    protected String programName;
    
    /** The utility it name. */
    @XmlAttribute
    protected String utilityITName;

    /**
     * Gets the value of the destinations property.
     * 
     * @return the destinations
     * 
     * possible object is
     * {@link UtilityDREvent.Destinations }
     */
    public UtilityDREvent.Destinations getDestinations() {
        return destinations;
    }

    /**
     * Sets the value of the destinations property.
     * 
     * @param value allowed object is
     * {@link UtilityDREvent.Destinations }
     */
    public void setDestinations(UtilityDREvent.Destinations value) {
        this.destinations = value;
    }

    /**
     * Gets the value of the eventTiming property.
     * 
     * @return the event timing
     * 
     * possible object is
     * {@link UtilityDREvent.EventTiming }
     */
    public UtilityDREvent.EventTiming getEventTiming() {
        return eventTiming;
    }

    /**
     * Sets the value of the eventTiming property.
     * 
     * @param value allowed object is
     * {@link UtilityDREvent.EventTiming }
     */
    public void setEventTiming(UtilityDREvent.EventTiming value) {
        this.eventTiming = value;
    }

    /**
     * Gets the value of the biddingInformation property.
     * 
     * @return the bidding information
     * 
     * possible object is
     * {@link UtilityDREvent.BiddingInformation }
     */
    public UtilityDREvent.BiddingInformation getBiddingInformation() {
        return biddingInformation;
    }

    /**
     * Sets the value of the biddingInformation property.
     * 
     * @param value allowed object is
     * {@link UtilityDREvent.BiddingInformation }
     */
    public void setBiddingInformation(UtilityDREvent.BiddingInformation value) {
        this.biddingInformation = value;
    }

    /**
     * Gets the value of the eventInformation property.
     * 
     * @return the event information
     * 
     * possible object is
     * {@link UtilityDREvent.EventInformation }
     */
    public UtilityDREvent.EventInformation getEventInformation() {
        return eventInformation;
    }

    /**
     * Sets the value of the eventInformation property.
     * 
     * @param value allowed object is
     * {@link UtilityDREvent.EventInformation }
     */
    public void setEventInformation(UtilityDREvent.EventInformation value) {
        this.eventInformation = value;
    }

    /**
     * Gets the value of the eventIdentifier property.
     * 
     * @return the event identifier
     * 
     * possible object is
     * {@link String }
     */
    public String getEventIdentifier() {
        return eventIdentifier;
    }

    /**
     * Sets the value of the eventIdentifier property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setEventIdentifier(String value) {
        this.eventIdentifier = value;
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
     * Gets the value of the eventModNumber property.
     * 
     * @return the event mod number
     */
    public long getEventModNumber() {
        return eventModNumber;
    }

    /**
     * Sets the value of the eventModNumber property.
     * 
     * @param value the value
     */
    public void setEventModNumber(long value) {
        this.eventModNumber = value;
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
     * Gets the value of the utilityITName property.
     * 
     * @return the utility it name
     * 
     * possible object is
     * {@link String }
     */
    public String getUtilityITName() {
        return utilityITName;
    }

    /**
     * Sets the value of the utilityITName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setUtilityITName(String value) {
        this.utilityITName = value;
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
     * &lt;element name="openingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     * &lt;element name="closingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "openingTime",
        "closingTime"
    })
    public static class BiddingInformation
        implements Serializable
    {

        /** The opening time. */
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar openingTime;
        
        /** The closing time. */
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar closingTime;

        /**
         * Gets the value of the openingTime property.
         * 
         * @return the opening time
         * 
         * possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getOpeningTime() {
            return openingTime;
        }

        /**
         * Sets the value of the openingTime property.
         * 
         * @param value allowed object is
         * {@link XMLGregorianCalendar }
         */
        public void setOpeningTime(XMLGregorianCalendar value) {
            this.openingTime = value;
        }

        /**
         * Gets the value of the closingTime property.
         * 
         * @return the closing time
         * 
         * possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getClosingTime() {
            return closingTime;
        }

        /**
         * Sets the value of the closingTime property.
         * 
         * @param value allowed object is
         * {@link XMLGregorianCalendar }
         */
        public void setClosingTime(XMLGregorianCalendar value) {
            this.closingTime = value;
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
     * &lt;element name="participants" minOccurs="0">
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * &lt;/element>
     * &lt;element name="groups" minOccurs="0">
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="groupID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * &lt;/element>
     * &lt;element name="locations" minOccurs="0">
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="location" type="{http://www.openadr.org/DRAS/DRASClient}Location"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * &lt;/element>
     * &lt;element name="drasClients" minOccurs="0">
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="drasClientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * &lt;/element>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "participants",
        "groups",
        "locations",
        "drasClients"
    })
    public static class Destinations
        implements Serializable
    {

        /** The participants. */
        @XmlElement(nillable = true)
        protected UtilityDREvent.Destinations.Participants participants;
        
        /** The groups. */
        @XmlElement(nillable = true)
        protected UtilityDREvent.Destinations.Groups groups;
        
        /** The locations. */
        @XmlElement(nillable = true)
        protected UtilityDREvent.Destinations.Locations locations;
        
        /** The dras clients. */
        @XmlElement(nillable = true)
        protected UtilityDREvent.Destinations.DrasClients drasClients;

        /**
         * Gets the value of the participants property.
         * 
         * @return the participants
         * 
         * possible object is
         * {@link UtilityDREvent.Destinations.Participants }
         */
        public UtilityDREvent.Destinations.Participants getParticipants() {
            return participants;
        }

        /**
         * Sets the value of the participants property.
         * 
         * @param value allowed object is
         * {@link UtilityDREvent.Destinations.Participants }
         */
        public void setParticipants(UtilityDREvent.Destinations.Participants value) {
            this.participants = value;
        }

        /**
         * Gets the value of the groups property.
         * 
         * @return the groups
         * 
         * possible object is
         * {@link UtilityDREvent.Destinations.Groups }
         */
        public UtilityDREvent.Destinations.Groups getGroups() {
            return groups;
        }

        /**
         * Sets the value of the groups property.
         * 
         * @param value allowed object is
         * {@link UtilityDREvent.Destinations.Groups }
         */
        public void setGroups(UtilityDREvent.Destinations.Groups value) {
            this.groups = value;
        }

        /**
         * Gets the value of the locations property.
         * 
         * @return the locations
         * 
         * possible object is
         * {@link UtilityDREvent.Destinations.Locations }
         */
        public UtilityDREvent.Destinations.Locations getLocations() {
            return locations;
        }

        /**
         * Sets the value of the locations property.
         * 
         * @param value allowed object is
         * {@link UtilityDREvent.Destinations.Locations }
         */
        public void setLocations(UtilityDREvent.Destinations.Locations value) {
            this.locations = value;
        }

        /**
         * Gets the value of the drasClients property.
         * 
         * @return the dras clients
         * 
         * possible object is
         * {@link UtilityDREvent.Destinations.DrasClients }
         */
        public UtilityDREvent.Destinations.DrasClients getDrasClients() {
            return drasClients;
        }

        /**
         * Sets the value of the drasClients property.
         * 
         * @param value allowed object is
         * {@link UtilityDREvent.Destinations.DrasClients }
         */
        public void setDrasClients(UtilityDREvent.Destinations.DrasClients value) {
            this.drasClients = value;
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
         * &lt;element name="drasClientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
         * &lt;/sequence>
         * &lt;/restriction>
         * &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "drasClientID"
        })
        public static class DrasClients
            implements Serializable
        {

            /** The dras client id. */
            @XmlElement(required = true, nillable = true)
            protected String drasClientID;

            /**
             * Gets the value of the drasClientID property.
             * 
             * @return the dras client id
             * 
             * possible object is
             * {@link String }
             */
            public String getDrasClientID() {
                return drasClientID;
            }

            /**
             * Sets the value of the drasClientID property.
             * 
             * @param value allowed object is
             * {@link String }
             */
            public void setDrasClientID(String value) {
                this.drasClientID = value;
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
         * &lt;element name="groupID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
         * &lt;/sequence>
         * &lt;/restriction>
         * &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "groupID"
        })
        public static class Groups
            implements Serializable
        {

            /** The group id. */
            @XmlElement(nillable = true)
            protected List<String> groupID;

            /**
             * Gets the value of the groupID property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the groupID property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             * getGroupID().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * @return the group id
             */
            public List<String> getGroupID() {
                if (groupID == null) {
                    groupID = new ArrayList<String>();
                }
                return this.groupID;
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
         * &lt;element name="location" type="{http://www.openadr.org/DRAS/DRASClient}Location"/>
         * &lt;/sequence>
         * &lt;/restriction>
         * &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "location"
        })
        public static class Locations
            implements Serializable
        {

            /** The location. */
            @XmlElement(required = true, nillable = true)
            protected Location location;

            /**
             * Gets the value of the location property.
             * 
             * @return the location
             * 
             * possible object is
             * {@link Location }
             */
            public Location getLocation() {
                return location;
            }

            /**
             * Sets the value of the location property.
             * 
             * @param value allowed object is
             * {@link Location }
             */
            public void setLocation(Location value) {
                this.location = value;
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
         * &lt;element name="participantID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
         * &lt;/sequence>
         * &lt;/restriction>
         * &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "participantID"
        })
        public static class Participants
            implements Serializable
        {

            /** The participant id. */
            @XmlElement(nillable = true)
            protected List<String> participantID;

            /**
             * Gets the value of the participantID property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the participantID property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             * getParticipantID().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * @return the participant id
             */
            public List<String> getParticipantID() {
                if (participantID == null) {
                    participantID = new ArrayList<String>();
                }
                return this.participantID;
            }

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
     * &lt;element name="eventInfoInstance" type="{http://www.openadr.org/DRAS/EventInfo}EventInfoInstance" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "eventInfoInstance"
    })
    public static class EventInformation
        implements Serializable
    {

        /** The event info instance. */
        @XmlElement(nillable = true)
        protected List<EventInfoInstance> eventInfoInstance;

        /**
         * Gets the value of the eventInfoInstance property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the eventInfoInstance property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getEventInfoInstance().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link EventInfoInstance }
         * 
         * @return the event info instance
         */
        public List<EventInfoInstance> getEventInfoInstance() {
            if (eventInfoInstance == null) {
                eventInfoInstance = new ArrayList<EventInfoInstance>();
            }
            return this.eventInfoInstance;
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
     * &lt;element name="notificationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     * &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "notificationTime",
        "startTime",
        "endTime"
    })
    public static class EventTiming
        implements Serializable
    {

        /** The notification time. */
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar notificationTime;
        
        /** The start time. */
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar startTime;
        
        /** The end time. */
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar endTime;

        /**
         * Gets the value of the notificationTime property.
         * 
         * @return the notification time
         * 
         * possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getNotificationTime() {
            return notificationTime;
        }

        /**
         * Sets the value of the notificationTime property.
         * 
         * @param value allowed object is
         * {@link XMLGregorianCalendar }
         */
        public void setNotificationTime(XMLGregorianCalendar value) {
            this.notificationTime = value;
        }

        /**
         * Gets the value of the startTime property.
         * 
         * @return the start time
         * 
         * possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getStartTime() {
            return startTime;
        }

        /**
         * Sets the value of the startTime property.
         * 
         * @param value allowed object is
         * {@link XMLGregorianCalendar }
         */
        public void setStartTime(XMLGregorianCalendar value) {
            this.startTime = value;
        }

        /**
         * Gets the value of the endTime property.
         * 
         * @return the end time
         * 
         * possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getEndTime() {
            return endTime;
        }

        /**
         * Sets the value of the endTime property.
         * 
         * @param value allowed object is
         * {@link XMLGregorianCalendar }
         */
        public void setEndTime(XMLGregorianCalendar value) {
            this.endTime = value;
        }

    }

}
