/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilitydrevent.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilitydrevent;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.utilitydrevent package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.utilitydrevent
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UtilityDREvent.Destinations.Locations }
     * 
     * @return the locations
     */
    public UtilityDREvent.Destinations.Locations createUtilityDREventDestinationsLocations() {
        return new UtilityDREvent.Destinations.Locations();
    }

    /**
     * Create an instance of {@link UtilityDREvent.Destinations.Groups }
     * 
     * @return the groups
     */
    public UtilityDREvent.Destinations.Groups createUtilityDREventDestinationsGroups() {
        return new UtilityDREvent.Destinations.Groups();
    }

    /**
     * Create an instance of {@link UtilityDREvent.BiddingInformation }
     * 
     * @return the bidding information
     */
    public UtilityDREvent.BiddingInformation createUtilityDREventBiddingInformation() {
        return new UtilityDREvent.BiddingInformation();
    }

    /**
     * Create an instance of {@link ListOfEventIDs }.
     * 
     * @return the list of event i ds
     */
    public ListOfEventIDs createListOfEventIDs() {
        return new ListOfEventIDs();
    }

    /**
     * Create an instance of {@link UtilityDREvent.EventTiming }
     * 
     * @return the event timing
     */
    public UtilityDREvent.EventTiming createUtilityDREventEventTiming() {
        return new UtilityDREvent.EventTiming();
    }

    /**
     * Create an instance of {@link ListOfUtilityDREvents }.
     * 
     * @return the list of utility dr events
     */
    public ListOfUtilityDREvents createListOfUtilityDREvents() {
        return new ListOfUtilityDREvents();
    }

    /**
     * Create an instance of {@link UtilityDREvent.Destinations.DrasClients }
     * 
     * @return the dras clients
     */
    public UtilityDREvent.Destinations.DrasClients createUtilityDREventDestinationsDrasClients() {
        return new UtilityDREvent.Destinations.DrasClients();
    }

    /**
     * Create an instance of {@link UtilityDREvent.Destinations.Participants }
     * 
     * @return the participants
     */
    public UtilityDREvent.Destinations.Participants createUtilityDREventDestinationsParticipants() {
        return new UtilityDREvent.Destinations.Participants();
    }

    /**
     * Create an instance of {@link UtilityDREvent.Destinations }
     * 
     * @return the destinations
     */
    public UtilityDREvent.Destinations createUtilityDREventDestinations() {
        return new UtilityDREvent.Destinations();
    }

    /**
     * Create an instance of {@link UtilityDREvent.EventInformation }
     * 
     * @return the event information
     */
    public UtilityDREvent.EventInformation createUtilityDREventEventInformation() {
        return new UtilityDREvent.EventInformation();
    }

    /**
     * Create an instance of {@link UtilityDREvent }.
     * 
     * @return the utility dr event
     */
    public UtilityDREvent createUtilityDREvent() {
        return new UtilityDREvent();
    }

}
