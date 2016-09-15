/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.drasclient.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.drasclient;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.drasclient package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.drasclient
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CommsStatus }.
     * 
     * @return the comms status
     */
    public CommsStatus createCommsStatus() {
        return new CommsStatus();
    }

    /**
     * Create an instance of {@link ListOfDRASClients }.
     * 
     * @return the list of dras clients
     */
    public ListOfDRASClients createListOfDRASClients() {
        return new ListOfDRASClients();
    }

    /**
     * Create an instance of {@link DRASClient.Programs }
     * 
     * @return the programs
     */
    public DRASClient.Programs createDRASClientPrograms() {
        return new DRASClient.Programs();
    }

    /**
     * Create an instance of {@link DRASClient.CommsParams }
     * 
     * @return the comms params
     */
    public DRASClient.CommsParams createDRASClientCommsParams() {
        return new DRASClient.CommsParams();
    }

    /**
     * Create an instance of {@link ListOfCommsStatus }.
     * 
     * @return the list of comms status
     */
    public ListOfCommsStatus createListOfCommsStatus() {
        return new ListOfCommsStatus();
    }

    /**
     * Create an instance of {@link DRASClient.SimpleClientResponseSchedules }
     * 
     * @return the simple client response schedules
     */
    public DRASClient.SimpleClientResponseSchedules createDRASClientSimpleClientResponseSchedules() {
        return new DRASClient.SimpleClientResponseSchedules();
    }

    /**
     * Create an instance of {@link DRASClient }.
     * 
     * @return the DRAS client
     */
    public DRASClient createDRASClient() {
        return new DRASClient();
    }

    /**
     * Create an instance of {@link Location.Coordinate }
     * 
     * @return the coordinate
     */
    public Location.Coordinate createLocationCoordinate() {
        return new Location.Coordinate();
    }

    /**
     * Create an instance of {@link Location }.
     * 
     * @return the location
     */
    public Location createLocation() {
        return new Location();
    }

}
