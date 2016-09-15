/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityprogram.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityprogram;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.akuautilityprogram package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.akuautilityprogram
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AkuaContactEmailList }.
     * 
     * @return the akua contact email list
     */
    public AkuaContactEmailList createAkuaContactEmailList() {
        return new AkuaContactEmailList();
    }

    /**
     * Create an instance of {@link AkuaBidConfig }.
     * 
     * @return the akua bid config
     */
    public AkuaBidConfig createAkuaBidConfig() {
        return new AkuaBidConfig();
    }

    /**
     * Create an instance of {@link AkuaUtilityProgram }.
     * 
     * @return the akua utility program
     */
    public AkuaUtilityProgram createAkuaUtilityProgram() {
        return new AkuaUtilityProgram();
    }

    /**
     * Create an instance of {@link AkuaBidBlock }.
     * 
     * @return the akua bid block
     */
    public AkuaBidBlock createAkuaBidBlock() {
        return new AkuaBidBlock();
    }

    /**
     * Create an instance of {@link AkuaSignalList }.
     * 
     * @return the akua signal list
     */
    public AkuaSignalList createAkuaSignalList() {
        return new AkuaSignalList();
    }

    /**
     * Create an instance of {@link AkuaModeTransition }.
     * 
     * @return the akua mode transition
     */
    public AkuaModeTransition createAkuaModeTransition() {
        return new AkuaModeTransition();
    }

    /**
     * Create an instance of {@link AkuaModeTransitionList }.
     * 
     * @return the akua mode transition list
     */
    public AkuaModeTransitionList createAkuaModeTransitionList() {
        return new AkuaModeTransitionList();
    }

    /**
     * Create an instance of {@link AkuaUtilityProgramList }.
     * 
     * @return the akua utility program list
     */
    public AkuaUtilityProgramList createAkuaUtilityProgramList() {
        return new AkuaUtilityProgramList();
    }

    /**
     * Create an instance of {@link AkuaBidBlockList }.
     * 
     * @return the akua bid block list
     */
    public AkuaBidBlockList createAkuaBidBlockList() {
        return new AkuaBidBlockList();
    }

}
