package com.akuacom.pss2.contact;

import static com.akuacom.test.TestUtil.generateRandomBoolean;
import static com.akuacom.test.TestUtil.generateRandomDouble;
import static com.akuacom.test.TestUtil.generateRandomInt;
import static com.akuacom.test.TestUtil.generateRandomString;
import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;

/**
 * Unit tests for the Contact Entity.
 * 
 * @author Brian Chapman
 * 
 */

public class ContactTest extends BaseEntityFixture<Contact> {

	@Override
	public Contact generateRandomIncompleteEntity() {
		Contact contact = new Contact();

		String type = generateRandomString();
		contact.setType(type);
		assertEquals(type, contact.getType());

		String address = generateRandomString();
		contact.setAddress(address);
		assertEquals(address, contact.getAddress());

		String description = generateRandomString();
		contact.setDescription(description);
		assertEquals(description, contact.getDescription());

		boolean commNotification = generateRandomBoolean();
		contact.setCommNotification(commNotification);
		assertEquals(commNotification, contact.isCommNotification());

		ContactEventNotificationType eventNotification = ContactEventNotificationType.FullNotification;
		contact.setEventNotification(eventNotification);
		assertEquals(eventNotification, contact.getEventNotification());

		String ownerid = generateRandomStringOfLength(64);
		contact.setOwnerID(ownerid);
		assertEquals(ownerid, contact.getOwnerID());

		String firstname = generateRandomString();
		contact.setFirstName(firstname);
		assertEquals(firstname, contact.getFirstName());

		String lastname = generateRandomString();
		contact.setLastName(lastname);
		assertEquals(lastname, contact.getLastName());

		String supporttype = generateRandomString();
		contact.setSupportType(supporttype);
		assertEquals(supporttype, contact.getSupportType());

		String ownertype = generateRandomStringOfLength(64);
		contact.setOwnerType(ownertype);
		assertEquals(ownertype, contact.getOwnerType());

		boolean external = generateRandomBoolean();
		contact.setExternal(external);
		assertEquals(external, contact.isExternal());

		int state = generateRandomInt(2048);
		contact.setState(state);
		assertEquals(state, contact.getState());

		double offseasonnotihours = generateRandomDouble();
		contact.setOffSeasonNotiHours(offseasonnotihours);
		assertEquals(offseasonnotihours, contact.getOffSeasonNotiHours(), 0.01);

		double onseasonnotihours = generateRandomDouble();
		contact.setOnSeasonNotiHours(onseasonnotihours);
		assertEquals(onseasonnotihours, contact.getOnSeasonNotiHours(), 0.01);

		return contact;
	}

	@Test
	public void testEventNotificationOn() {
		Contact contact = new Contact();

		assertTrue(!contact.eventNotificationOn());

		contact.setEventNotification(ContactEventNotificationType.FullNotification);
		assertTrue(contact.eventNotificationOn());

		contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
		assertTrue(contact.eventNotificationOn());

		contact.setEventNotification(ContactEventNotificationType.NoNotification);
		assertTrue(!contact.eventNotificationOn());
	}

	@Test
	public void testOfflineErrorThresholdMinutesForSeason() {
		Contact contact = new Contact();
		int onSeason = generateRandomInt();
		contact.setOnSeasonNotiHours(onSeason);
		
		int offSeason = generateRandomInt();
		contact.setOffSeasonNotiHours(offSeason);
		
		assertEquals(new Double(onSeason*60.0).intValue(), contact.getOfflineErrorThresholdMinutesForSeason(true), 0.01);
		assertEquals(new Double(offSeason*60.0).intValue(), contact.getOfflineErrorThresholdMinutesForSeason(false), 0.01);
	}

}
