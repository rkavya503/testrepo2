package com.akuacom.pss2.event.signal;

import com.akuacom.ejb.BaseEntityFixture;
import static com.akuacom.test.TestUtil.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the EventSignalEntry Entity.
 * 
 * @author Brian Chapman
 * 
 */
public class EventSignalEntryTest extends BaseEntityFixture<EventSignalEntry> {

		@Override
		public EventSignalEntry generateRandomIncompleteEntity() {
			EventSignalEntry entry = new EventSignalEntry();

			boolean expired = generateRandomBoolean();
			entry.setExpired(expired);
			assertEquals(expired, entry.getExpired());
			
			return entry;
		}

	}
