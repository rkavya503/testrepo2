/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuadrasclientdata.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuadrasclientdata;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.akuadrasclientdata package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.akuadrasclientdata
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AkuaDRASSearchHandler }.
     * 
     * @return the akua dras search handler
     */
    public AkuaDRASSearchHandler createAkuaDRASSearchHandler() {
        return new AkuaDRASSearchHandler();
    }

    /**
     * Create an instance of {@link ListofSearchCriteria }.
     * 
     * @return the listof search criteria
     */
    public ListofSearchCriteria createListofSearchCriteria() {
        return new ListofSearchCriteria();
    }

    /**
     * Create an instance of {@link AkuaDRASSearchCriteria }.
     * 
     * @return the akua dras search criteria
     */
    public AkuaDRASSearchCriteria createAkuaDRASSearchCriteria() {
        return new AkuaDRASSearchCriteria();
    }

    /**
     * Create an instance of {@link AkuaDRASCursor }.
     * 
     * @return the akua dras cursor
     */
    public AkuaDRASCursor createAkuaDRASCursor() {
        return new AkuaDRASCursor();
    }

    /**
     * Create an instance of {@link ListOfIDs }.
     * 
     * @return the list of i ds
     */
    public ListOfIDs createListOfIDs() {
        return new ListOfIDs();
    }

    /**
     * Create an instance of {@link ListofClientData }.
     * 
     * @return the listof client data
     */
    public ListofClientData createListofClientData() {
        return new ListofClientData();
    }

    /**
     * Create an instance of {@link ClientData }.
     * 
     * @return the client data
     */
    public ClientData createClientData() {
        return new ClientData();
    }

    /**
     * Create an instance of {@link ClientDataAttribute }.
     * 
     * @return the client data attribute
     */
    public ClientDataAttribute createClientDataAttribute() {
        return new ClientDataAttribute();
    }

    /**
     * Create an instance of {@link ListOfClientDataAttributes }.
     * 
     * @return the list of client data attributes
     */
    public ListOfClientDataAttributes createListOfClientDataAttributes() {
        return new ListOfClientDataAttributes();
    }

}
