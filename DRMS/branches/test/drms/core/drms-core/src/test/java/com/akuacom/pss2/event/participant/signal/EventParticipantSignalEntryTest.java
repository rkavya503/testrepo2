package com.akuacom.pss2.event.participant.signal;

import static com.akuacom.test.TestUtil.generateRandomBoolean;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * Unit tests for the EventParticipantSignalEntryTest Entity.
 * 
 * @author Brian Chapman
 * 
 */
public class EventParticipantSignalEntryTest extends
		BaseEntityFixture<EventParticipantSignalEntry> {

	@Override
	public EventParticipantSignalEntry generateRandomIncompleteEntity() {
		EventParticipantSignalEntry entry = new EventParticipantSignalEntry();

		assertEquals(false, entry.getExpired());
		boolean expired = generateRandomBoolean();
		entry.setExpired(expired);

		return entry;
	}

}
