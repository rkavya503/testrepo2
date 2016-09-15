/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuasignal.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuasignal;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.akuasignal package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.akuasignal
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListOfSignals }.
     * 
     * @return the list of signals
     */
    public ListOfSignals createListOfSignals() {
        return new ListOfSignals();
    }

    /**
     * Create an instance of {@link ListOfValues }.
     * 
     * @return the list of values
     */
    public ListOfValues createListOfValues() {
        return new ListOfValues();
    }

    /**
     * Create an instance of {@link AkuaSignal }.
     * 
     * @return the akua signal
     */
    public AkuaSignal createAkuaSignal() {
        return new AkuaSignal();
    }

}
