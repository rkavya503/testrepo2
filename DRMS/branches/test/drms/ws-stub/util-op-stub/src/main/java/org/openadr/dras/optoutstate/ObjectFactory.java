/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.optoutstate.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.optoutstate;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.optoutstate package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.optoutstate
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OptOutState.DRASClients }
     * 
     * @return the DRAS clients
     */
    public OptOutState.DRASClients createOptOutStateDRASClients() {
        return new OptOutState.DRASClients();
    }

    /**
     * Create an instance of {@link ListOfOptOutStates }.
     * 
     * @return the list of opt out states
     */
    public ListOfOptOutStates createListOfOptOutStates() {
        return new ListOfOptOutStates();
    }

    /**
     * Create an instance of {@link OptOutState.Schedule }
     * 
     * @return the schedule
     */
    public OptOutState.Schedule createOptOutStateSchedule() {
        return new OptOutState.Schedule();
    }

    /**
     * Create an instance of {@link OptOutState }.
     * 
     * @return the opt out state
     */
    public OptOutState createOptOutState() {
        return new OptOutState();
    }

    /**
     * Create an instance of {@link OptOutState.Schedule.TimeElement }
     * 
     * @return the time element
     */
    public OptOutState.Schedule.TimeElement createOptOutStateScheduleTimeElement() {
        return new OptOutState.Schedule.TimeElement();
    }

}
