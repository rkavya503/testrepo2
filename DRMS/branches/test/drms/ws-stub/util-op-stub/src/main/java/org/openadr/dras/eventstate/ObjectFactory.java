/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventstate.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventstate;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.eventstate package.
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

    /** The Constant _OperationModeValue_QNAME. */
    private final static QName _OperationModeValue_QNAME = new QName("http://www.openadr.org/DRAS/EventState", "OperationModeValue");
    
    /** The Constant _EventState_QNAME. */
    private final static QName _EventState_QNAME = new QName("http://www.openadr.org/DRAS/EventState", "eventState");
    
    /** The Constant _ListOfEventState_QNAME. */
    private final static QName _ListOfEventState_QNAME = new QName("http://www.openadr.org/DRAS/EventState", "listOfEventState");
    
    /** The Constant _EventStatus_QNAME. */
    private final static QName _EventStatus_QNAME = new QName("http://www.openadr.org/DRAS/EventState", "EventStatus");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.eventstate
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EventInfoTypeID }.
     * 
     * @return the event info type id
     */
    public EventInfoTypeID createEventInfoTypeID() {
        return new EventInfoTypeID();
    }

    /**
     * Create an instance of {@link ListOfEventStates }.
     * 
     * @return the list of event states
     */
    public ListOfEventStates createListOfEventStates() {
        return new ListOfEventStates();
    }

    /**
     * Create an instance of {@link org.openadr.dras.eventstate.CustomData }
     * 
     * @return the custom data
     */
    public org.openadr.dras.eventstate.CustomData createCustomData() {
        return new org.openadr.dras.eventstate.CustomData();
    }

    /**
     * Create an instance of {@link SimpleClientEventData }.
     * 
     * @return the simple client event data
     */
    public SimpleClientEventData createSimpleClientEventData() {
        return new SimpleClientEventData();
    }

    /**
     * Create an instance of {@link SmartClientDREventData }.
     * 
     * @return the smart client dr event data
     */
    public SmartClientDREventData createSmartClientDREventData() {
        return new SmartClientDREventData();
    }

    /**
     * Create an instance of {@link SimpleClientEventData.OperationModeSchedule }
     * 
     * @return the operation mode schedule
     */
    public SimpleClientEventData.OperationModeSchedule createSimpleClientEventDataOperationModeSchedule() {
        return new SimpleClientEventData.OperationModeSchedule();
    }

    /**
     * Create an instance of {@link EventStateConfirmation }.
     * 
     * @return the event state confirmation
     */
    public EventStateConfirmation createEventStateConfirmation() {
        return new EventStateConfirmation();
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
     * Create an instance of {@link EventState.CustomData }
     * 
     * @return the custom data
     */
    public EventState.CustomData createEventStateCustomData() {
        return new EventState.CustomData();
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
     * Create an instance of {@link GeneralInfoInstance }.
     * 
     * @return the general info instance
     */
    public GeneralInfoInstance createGeneralInfoInstance() {
        return new GeneralInfoInstance();
    }

    /**
     * Create an instance of {@link OperationState }.
     * 
     * @return the operation state
     */
    public OperationState createOperationState() {
        return new OperationState();
    }

    /**
     * Create an instance of {@link GeneralInfoValue }.
     * 
     * @return the general info value
     */
    public GeneralInfoValue createGeneralInfoValue() {
        return new GeneralInfoValue();
    }

    /**
     * Create an instance of {@link EventState }.
     * 
     * @return the event state
     */
    public EventState createEventState() {
        return new EventState();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "http://www.openadr.org/DRAS/EventState", name = "OperationModeValue")
    public JAXBElement<String> createOperationModeValue(String value) {
        return new JAXBElement<String>(_OperationModeValue_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EventState }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< event state>
     */
    @XmlElementDecl(namespace = "http://www.openadr.org/DRAS/EventState", name = "eventState")
    public JAXBElement<EventState> createEventState(EventState value) {
        return new JAXBElement<EventState>(_EventState_QNAME, EventState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListOfEventStates }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< list of event states>
     */
    @XmlElementDecl(namespace = "http://www.openadr.org/DRAS/EventState", name = "listOfEventState")
    public JAXBElement<ListOfEventStates> createListOfEventState(ListOfEventStates value) {
        return new JAXBElement<ListOfEventStates>(_ListOfEventState_QNAME, ListOfEventStates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "http://www.openadr.org/DRAS/EventState", name = "EventStatus")
    public JAXBElement<String> createEventStatus(String value) {
        return new JAXBElement<String>(_EventStatus_QNAME, String.class, null, value);
    }

}
