/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ContactServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.contact;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.lang.IllegalStateException;
import javax.persistence.Query;

import com.akuacom.ejb.BaseEAOBean;

/**
 * EJB 2 style stateless session bean providing a facade for Contacts..
 */
@Stateless
public class ContactEAOBean extends BaseEAOBean<Contact> implements ContactEAO.R,
        ContactEAO.L {

    public ContactEAOBean() {
        super(Contact.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ContactServicer#createContact(com.akuacom
     * .pss2.contact.Contact)
     */
    public Contact createContact(Contact contact) {
        try {
            contact = super.create(contact);
        } catch (Exception e) {
            throw new EJBException("ERROR_CONTACT_CREATE", e);
        }
        return contact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ContactServicer#updateContact(com.akuacom
     * .pss2.contact.Contact)
     */
    public Contact updateContact(Contact contact) {
        try {
            contact = super.update(contact);
        } catch (Exception e) {
            throw new EJBException("ERROR_CONTACT_UPDATE", e);
        }
        return contact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ContactServicer#deleteContact(java.lang.
     * String)
     */
    public void deleteContact(String contactUUID) {
        try {
            Contact contact = (Contact) super.getById(contactUUID);
            contact.setState(Contact.CONTACT_DELETED);
            updateContact(contact);
        } catch (Exception e) {
            throw new EJBException("ERROR_CONTACT_UPDATE", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ContactServicer#getContact(java.lang.String)
     */
    public Contact getContact(String contactUUID) {
        Contact contact;
        try {
            contact = (Contact) super.getById(contactUUID);
        } catch (Exception e) {
            throw new EJBException("ERROR_CONTACT_GET1", e);
        }
        return contact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ContactServicer#findContacts(java.lang.String
     * , java.lang.String, int)
     */
    public List<Contact> findContacts(String ownerID, String ownerType,
            int state) {
        List<Contact> contacts;
        try {
            final String QUERY_GET_CONTACT_BY_OWNER = "SELECT c FROM Contact c  WHERE c.ownerID = ?1 AND c.ownerType = ?2 AND c.state = ?3";
            List<Object> filters = new ArrayList<Object>();
            filters.add(ownerID);
            filters.add(ownerType);
            filters.add(state);
            contacts = super.getByFilters(QUERY_GET_CONTACT_BY_OWNER, filters);
        } catch (Exception e) {
            throw new EJBException("ERROR_CONTACT_GET2", e);
        }
        return contacts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.program.services.ContactServicer#findCoreContacts()
     */
    public List<Contact> findCoreContacts() {
        return em.createNamedQuery("Contact.findOwnerlessByOwnerTypeAndState")
                .setParameter("type", Contact.CONTACT_OWNER_TYPE_GLOBAL)
                .setParameter("state", Contact.CONTACT_ACTIVE).getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ContactServicer#updateCoreContacts(java.
     * util.List)
     */
    public void updateCoreContacts(List<Contact> contacts) {
        try {
            List<Contact> contactsForUpdate = new ArrayList<Contact>();
            List<Contact> contactsForCreate = new ArrayList<Contact>();
            List<Contact> contactsExist = this.findCoreContacts();
            for (Contact contact : contacts) {
                if (contact.getUUID() == null || contact.getUUID().isEmpty()) {
                    contact.setState(Contact.CONTACT_ACTIVE);
                    contact.setOwnerType(Contact.CONTACT_OWNER_TYPE_GLOBAL);
                    contactsForCreate.add(contact);
                }
            }

            for (Contact contactExist : contactsExist) {
                boolean deleted = true;
                for (Contact contact : contacts) {
                    if (contactExist.getUUID().equals(contact.getUUID())) {
                        contactExist.setAddress(contact.getAddress());
                        contactExist.setCommNotification(contact
                                .isCommNotification());
                        contactExist.setEventNotification(contact
                                .getEventNotification());
                        contactExist.setFirstName(contact.getFirstName());
                        contactExist.setLastName(contact.getLastName());
                        contactExist.setOffSeasonNotiHours(contact
                                .getOffSeasonNotiHours());
                        contactExist.setOnSeasonNotiHours(contact
                                .getOnSeasonNotiHours());
                        contactExist.setType(contact.getType());
                        contactExist.setOptOutDigest(contact.isOptOutDigest());
                        contactExist.setOptOutUndeliveredReport(contact.isOptOutUndeliveredReport());
                        deleted = false;
                        break;
                    }

                }
                if (deleted) {
                    contactExist.setState(Contact.CONTACT_DELETED);
                }

                contactsForUpdate.add(contactExist);
            }

            for (Contact contactUpdate : contactsForUpdate) {
                this.updateContact(contactUpdate);
            }

            for (Contact contactCreate : contactsForCreate) {
                this.createContact(contactCreate);
            }

        } catch (Exception e) {
            throw new EJBException("ERROR_CONTACT_UPDATE", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ContactServicer#updateContacts(java.util
     * .List, java.lang.String, java.lang.String)
     */
    public void updateContacts(List<Contact> contacts, String ownerType,
            String ownerID) {
        try {
            List<Contact> contactsForUpdate = new ArrayList<Contact>();
            List<Contact> contactsForCreate = new ArrayList<Contact>();
            List<Contact> contactsExist = this.findCoreContacts();
            for (Contact contact : contacts) {
                if (contact.getUUID() == null || contact.getUUID().isEmpty()) {
                    contact.setState(Contact.CONTACT_ACTIVE);
                    contact.setOwnerType(ownerType);
                    contactsForCreate.add(contact);
                }
            }

            for (Contact contactExist : contactsExist) {
                boolean deleted = true;
                for (Contact contact : contacts) {
                    if (contactExist.getUUID().equals(contact.getUUID())) {
                        contactExist.setAddress(contact.getAddress());
                        contactExist.setCommNotification(contact
                                .isCommNotification());
                        contactExist.setEventNotification(contact
                                .getEventNotification());
                        contactExist.setFirstName(contact.getFirstName());
                        contactExist.setLastName(contact.getLastName());
                        contactExist.setOffSeasonNotiHours(contact
                                .getOffSeasonNotiHours());
                        contactExist.setOnSeasonNotiHours(contact
                                .getOnSeasonNotiHours());
                        contactExist.setType(contact.getType());
                        deleted = false;
                        break;
                    }

                }
                if (deleted) {
                    contactExist.setState(Contact.CONTACT_DELETED);
                }

                contactsForUpdate.add(contactExist);
            }

            for (Contact contactUpdate : contactsForUpdate) {
                this.updateContact(contactUpdate);
            }

            for (Contact contactCreate : contactsForCreate) {
                this.createContact(contactCreate);
            }

        } catch (Exception e) {
            throw new EJBException("ERROR_CONTACT_UPDATE", e);
        }
    }

    /**
     * Will always return an object - UUID will be if not found. This is how
     * this object is used.
     */
    public ContactsOfflineError getContactOfflineError(String programName,
            String participantUuid, String contactsUuid) {
        ContactsOfflineError coe;
        try {
            Query query = em
                    .createNamedQuery(
                            "ContactsOfflineError.findByProgramAndParticipantAndContactsUuid")
                    .setParameter("programName", programName)
                    .setParameter("participantUuid", participantUuid)
                    .setParameter("contactsUuid", contactsUuid);
            coe = (ContactsOfflineError) query.getSingleResult();
        } catch (NoResultException nrex) {
            coe = null;
        } catch (NonUniqueResultException nuex) {
			coe = null;
		} catch (IllegalStateException ilex) {
			coe = null;
		} catch (Exception e) {
			// for stale state exception
			coe = null;
		}

        return coe;
    }

    public List<ContactsOfflineError> getAllContactOfflineErrors() {
        return em.createNamedQuery("ContactsOfflineError.findAll")
                .getResultList();
    }

    public void setContactsOfflineError(ContactsOfflineError coe) {
        em.merge(coe);
    }

    public void removeContactsOfflineError(ContactsOfflineError coe) {
        ContactsOfflineError tmp = this.getContactOfflineError(
                coe.getProgramName(), coe.getParticipantUuid(),
                coe.getContactsUuid());
        if (tmp != null) {
            try {
                em.remove(tmp);
            } catch (Exception ex) {
                // snuff
            }
        }
    }
    
    public  List<Contact> getOperatorContacts(String programName){
    	  return em.createNamedQuery("Contact.getOperatorContacts").setParameter("programName", programName)
          .getResultList();
    }
    
}