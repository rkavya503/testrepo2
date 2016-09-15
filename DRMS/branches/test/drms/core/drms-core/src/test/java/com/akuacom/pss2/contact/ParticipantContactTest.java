package com.akuacom.pss2.contact;

import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.participant.contact.ParticipantContact;

/**
 * Unit tests for {@link ParticipantContact}
 * 
 * @author Brian Chapman
 * 
 */
public class ParticipantContactTest extends BaseEntityFixture<ParticipantContact> {

		public ParticipantContact generateRandomIncompleteEntity() {
			ParticipantContact contact = new ParticipantContact();

			String type = generateRandomStringOfLength(45);
			contact.setType(type);
			assertEquals(type, contact.getType());

			return contact;
		}
	}
