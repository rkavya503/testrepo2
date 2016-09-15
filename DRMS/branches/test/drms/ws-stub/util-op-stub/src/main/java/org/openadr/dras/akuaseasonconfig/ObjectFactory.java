/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuaseasonconfig.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuaseasonconfig;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.akuaseasonconfig package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.akuaseasonconfig
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AkuaSeasonConfig }.
     * 
     * @return the akua season config
     */
    public AkuaSeasonConfig createAkuaSeasonConfig() {
        return new AkuaSeasonConfig();
    }

    /**
     * Create an instance of {@link ListOfSeasonConfigs }.
     * 
     * @return the list of season configs
     */
    public ListOfSeasonConfigs createListOfSeasonConfigs() {
        return new ListOfSeasonConfigs();
    }

}
