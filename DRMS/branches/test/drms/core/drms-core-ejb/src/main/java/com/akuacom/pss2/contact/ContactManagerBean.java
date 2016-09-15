package com.akuacom.pss2.contact;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

@Stateless
public class ContactManagerBean implements ContactManager.R, ContactManager.L {

	@EJB
	ContactEAO.L contactEAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.system.SystemManager#findCoreContacts()
	 */
	public List<Contact> findCoreContacts() {
		List<Contact> contacts;
		try {
			contacts = contactEAO.findCoreContacts();
		} catch (Exception e) {
			throw new EJBException("ERROR_CONTACT_GET4", e);
		}
		return contacts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.system.SystemManager#updateCoreContacts(java.util.List)
	 */
	public void updateCoreContacts(List<Contact> contacts) {
		try {
			contactEAO.updateCoreContacts(contacts);

		} catch (Exception e) {
			throw new EJBException("ERROR_CONTACT_UPDATE", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.system.SystemManager#updateContacts(java.util.List,
	 * java.lang.String, java.lang.String)
	 */
	public void updateContacts(List<Contact> contacts, String ownerType,
			String ownerID) {
		try {
			contactEAO.updateContacts(contacts, ownerType, ownerID);

		} catch (Exception e) {
			throw new EJBException("ERROR_CONTACT_UPDATE", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.system.SystemManager#getOperatorContacts()
	 */
	public List<Contact> getOperatorContacts() {
		try {
			return contactEAO.findCoreContacts();
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public ContactsOfflineError getContactOfflineError(String programName,
			String participantUuid, String contactsUuid) {
		return contactEAO.getContactOfflineError(programName, participantUuid,
				contactsUuid);
	}

	public List<ContactsOfflineError> getAllContactOfflineErrors() {
		return contactEAO.getAllContactOfflineErrors();
	}

	public void setContactsOfflineError(ContactsOfflineError coe) {
		contactEAO.setContactsOfflineError(coe);
	}

	public void removeContactsOfflineError(ContactsOfflineError coe) {
		contactEAO.removeContactsOfflineError(coe);
	}
}