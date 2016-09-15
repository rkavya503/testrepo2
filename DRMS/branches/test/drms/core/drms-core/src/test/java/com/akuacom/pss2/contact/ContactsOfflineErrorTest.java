package com.akuacom.pss2.contact;

import static com.akuacom.test.TestUtil.generateRandomString;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * ContactsOffilneErrorTest is the unit test class for the ContactsOfflineError
 * Entity.
 * 
 * @author Brian Chapman
 * 
 *         Initial date 2010.11.10
 */
public class ContactsOfflineErrorTest extends
		BaseEntityFixture<ContactsOfflineError> {

	@Override
	public ContactsOfflineError generateRandomIncompleteEntity() {
		ContactsOfflineError contacts = new ContactsOfflineError();

		boolean offlineError = true;
		contacts.setOfflineError(offlineError);
		assertEquals(offlineError, contacts.isOfflineError());

		String programName = generateRandomString();
		contacts.setProgramName(programName);
		assertEquals(programName, contacts.getProgramName());

		String contactsUuid = genereateRandomUUID();
		contacts.setContactsUuid(contactsUuid);
		assertEquals(contactsUuid, contacts.getContactsUuid());

		String participantUuid = genereateRandomUUID();
		contacts.setParticipantUuid(participantUuid);
		assertEquals(participantUuid, contacts.getParticipantUuid());

		return contacts;
	}

}
