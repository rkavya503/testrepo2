/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ContactServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.contact;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;

/**
 * This class serves all the functions related to the contact management.
 */

public interface ContactEAO extends BaseEAO<Contact> {
    @Remote
    public interface R extends ContactEAO {}
    @Local
    public interface L extends ContactEAO {}

    /**
     * This function creates a contact .
     * 
     * @param contact
     *            the contact
     * 
     * @return the contact impl
     * 
     * @throws com.akuacom.common.exception.AppServiceException
     * 
     *             Contact
     */
    Contact createContact(Contact contact);

    /**
     * This function updates a contact.
     * 
     * @param contact
     *            the contact
     * 
     * @return the contact impl
     * 
     * @throws com.akuacom.common.exception.AppServiceException
     * 
     *             Contact
     */
    Contact updateContact(Contact contact);

    /**
     * This function marked a contact as deleted.
     * 
     * @param contactUUID
     *            the contact uuid
     * 
     * @throws com.akuacom.common.exception.AppServiceException
     * 
     *             void
     */
    void deleteContact(String contactUUID);

    /**
     * This function gets a contact by a given contact UUID.
     * 
     * @param contactUUID
     *            the contact uuid
     * 
     * @return the contact
     * 
     * @throws com.akuacom.common.exception.AppServiceException
     * 
     *             Contact
     */
    Contact getContact(String contactUUID);

    /**
     * Find contacts.
     * 
     * @param ownerID
     *            the owner id
     * @param ownerType
     *            the owner type
     * @param state
     *            the state
     * 
     * @return the list< contact impl>
     */
    List<Contact> findContacts(String ownerID, String ownerType, int state);

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

    ContactsOfflineError getContactOfflineError(String programName,
            String participantUuid, String contactsUuid);

    List<ContactsOfflineError> getAllContactOfflineErrors();

    void setContactsOfflineError(ContactsOfflineError coe);

    void removeContactsOfflineError(ContactsOfflineError coe);
    
    List<Contact> getOperatorContacts(String programName);

}