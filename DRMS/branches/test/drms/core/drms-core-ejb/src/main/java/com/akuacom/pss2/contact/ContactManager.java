/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.system.SystemManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.contact;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * The Interface ContactManager.
 */

public interface ContactManager {
    @Remote
    public interface R extends ContactManager {}
    @Local
    public interface L extends ContactManager {}

    /**
     * Find core contacts.
     * 
     * @return the list< contact impl>
     */
    List<Contact> findCoreContacts();

    /**
     * Update core contacts.
     * 
     * @param contacts
     *            the contacts
     */
    void updateCoreContacts(List<Contact> contacts);

    /**
     * Update contacts.
     * 
     * @param contacts
     *            the contacts
     * @param ownerType
     *            the owner type
     * @param ownerID
     *            the owner id
     */
    void updateContacts(List<Contact> contacts, String ownerType, String ownerID);

    /**
     * Gets the operator contacts.
     * 
     * @return the operator contacts
     */
    List<Contact> getOperatorContacts();

    ContactsOfflineError getContactOfflineError(String programName,
            String participantUuid, String contactsUuid);

    List<ContactsOfflineError> getAllContactOfflineErrors();

    void setContactsOfflineError(ContactsOfflineError coe);

    void removeContactsOfflineError(ContactsOfflineError coe);

}
