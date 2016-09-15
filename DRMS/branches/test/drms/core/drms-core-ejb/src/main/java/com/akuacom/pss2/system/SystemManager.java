/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.system.SystemManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.system;

import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactsOfflineError;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.timer.TimerManager;

/**
 * The Interface SystemManager.
 */

public interface SystemManager extends TimerManager {
    @Remote
    public interface R extends SystemManager {}
    @Local
    public interface L extends SystemManager {}

    // properties
    /**
     * Sets the property.
     * 
     * @param prop
     *            the prop
     * 
     * @return the core property
     */
    CoreProperty setProperty(CoreProperty prop);

    /**
     * Gets the property.
     * 
     * @param propName
     *            the prop name
     * 
     * @return the property
     * @throws EntityNotFoundException
     */
    CoreProperty getPropertyByName(String propName)
            throws EntityNotFoundException;

    /**
     * Given an enumerated property name this will return the core property or
     * throw an exception if not found.
     * 
     * @param propertyName
     * @return
     */
    CoreProperty getProperty(PropertyName propertyName)
            throws EntityNotFoundException;

    /**
     * Gets the all properties.
     * 
     * @return the all properties
     */
    List<CoreProperty> getAllProperties();

    /**
     * Given an id from an existing core property this will update the value of
     * the property. Even though properties are a specific type, this will take
     * any string and convert it into the proper type making it easier to use a
     * string from a client.
     * 
     * @param uuid
     * @param propertyValueAsString
     * @return
     * @throws EntityNotFoundException
     * @throws EJBException
     *             wrapping {@link IlTlegalArgumentException} when a string is
     *             provided that can't be converted based on the existing
     *             property type.
     */
    CoreProperty updatePropertyValue(String uuid, String propertyValueAsString)
            throws EntityNotFoundException;

    /**
     * Gets the pss2 properties.
     * 
     * @return the pss2 properties
     */
    PSS2Properties getPss2Properties();

    PSS2Features getPss2Features();

    void initialize();

    ProgramEJB lookupProgramBean(String programName);

    ProgramEJB lookupProgramBeanFromClassName(String classNameBase);
    void processTimeOutCommsStatus(Participant part,ParticipantManager participantManager,
    		Map<String, ContactsOfflineError> errorMap,PSS2Properties params, List<Contact> contacts);

}
