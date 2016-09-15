/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuacontact.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuacontact;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.akuacontact package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.akuacontact
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListOfContacts }.
     * 
     * @return the list of contacts
     */
    public ListOfContacts createListOfContacts() {
        return new ListOfContacts();
    }

    /**
     * Create an instance of {@link AkuaContact }.
     * 
     * @return the akua contact
     */
    public AkuaContact createAkuaContact() {
        return new AkuaContact();
    }

}
