/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuaeventinfo.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuaeventinfo;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.akuaeventinfo package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.akuaeventinfo
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Values }.
     * 
     * @return the values
     */
    public Values createValues() {
        return new Values();
    }

    /**
     * Create an instance of {@link EventInfoInstance }.
     * 
     * @return the event info instance
     */
    public EventInfoInstance createEventInfoInstance() {
        return new EventInfoInstance();
    }

    /**
     * Create an instance of {@link ListOfEventInfoType }.
     * 
     * @return the list of event info type
     */
    public ListOfEventInfoType createListOfEventInfoType() {
        return new ListOfEventInfoType();
    }

    /**
     * Create an instance of {@link EventInfoType }.
     * 
     * @return the event info type
     */
    public EventInfoType createEventInfoType() {
        return new EventInfoType();
    }

    /**
     * Create an instance of {@link EventInfoType.Schedule }
     * 
     * @return the schedule
     */
    public EventInfoType.Schedule createEventInfoTypeSchedule() {
        return new EventInfoType.Schedule();
    }

    /**
     * Create an instance of {@link EventInfoInstance.Groups }
     * 
     * @return the groups
     */
    public EventInfoInstance.Groups createEventInfoInstanceGroups() {
        return new EventInfoInstance.Groups();
    }

    /**
     * Create an instance of {@link EventInfoType.Enumerations }
     * 
     * @return the enumerations
     */
    public EventInfoType.Enumerations createEventInfoTypeEnumerations() {
        return new EventInfoType.Enumerations();
    }

    /**
     * Create an instance of {@link EventInfoValue }.
     * 
     * @return the event info value
     */
    public EventInfoValue createEventInfoValue() {
        return new EventInfoValue();
    }

    /**
     * Create an instance of {@link EventInfoInstance.Participants }
     * 
     * @return the participants
     */
    public EventInfoInstance.Participants createEventInfoInstanceParticipants() {
        return new EventInfoInstance.Participants();
    }

}
