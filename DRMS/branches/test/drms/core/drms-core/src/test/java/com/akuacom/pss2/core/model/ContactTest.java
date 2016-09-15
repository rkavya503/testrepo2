package com.akuacom.pss2.core.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;

/**
 * This test is initialed by Li Fei on 2010.8.25 for test 
 *  
 * Related issues DRMS-1132, DRMS-1391
 * 
 * @author Li Fei
 */

public class ContactTest {

	protected Contact contact = new Contact();
	
	/**
     * Related issues DRMS-1132, DRMS-1391
	 * Initial date Aug 25, 2010
	 * @author Li Fei
	 */
	
    @Test
	public void eventNotificationOnTest(){
    	
		contact.setEventNotification(ContactEventNotificationType.FullNotification);		
		assertTrue(contact.eventNotificationOn());
		
		contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
		assertTrue(contact.eventNotificationOn());
		
		contact.setEventNotification(ContactEventNotificationType.NoNotification);
		assertFalse(contact.eventNotificationOn());

		contact.setEventNotification(null);
		assertFalse(contact.eventNotificationOn());
	}
}
