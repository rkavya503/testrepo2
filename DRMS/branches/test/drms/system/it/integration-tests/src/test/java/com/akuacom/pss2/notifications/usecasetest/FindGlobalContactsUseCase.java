/**
 * 
 */
package com.akuacom.pss2.notifications.usecasetest;

import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.contact.Contact;

/**
 * @author Linda
 *
 */
public class FindGlobalContactsUseCase extends AbstractNotificationUseCase {

	private boolean onlyEventNotification=false;
	
	public boolean isOnlyEventNotification() {
		return onlyEventNotification;
	}

	public void setOnlyEventNotification(boolean onlyEventNotification) {
		this.onlyEventNotification = onlyEventNotification;
	}

	public FindGlobalContactsUseCase() {
		super();
	}
	
	public FindGlobalContactsUseCase(boolean onlyEventNotification){
		this.onlyEventNotification=onlyEventNotification;
	}

	
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.usecasetest.cases.AbstractUseCase#runCase()
	 */
	@Override
	public Object runCase() throws Exception {
		List<Contact> contacts = new ArrayList<Contact>();
		List<Contact> coreContacts = getContactManager().findCoreContacts();
		if (coreContacts != null) {
			if (onlyEventNotification) {
				for (Contact contact : coreContacts) {
					if (contact.eventNotificationOn())
						contacts.add(contact);
				}
			}else{
				contacts.addAll(coreContacts);
			}
		}
		return contacts;
	}

}
