/**
 * 
 */
package com.akuacom.pss2.event;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.akuacom.ejb.AbstractBaseEAOTest;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.test.TestUtil;

/**
 * Tests {@link Event} and {@link EventEAO} methods.
 * 
 * @author Aaron Roller
 * 
 */
public class EventEAOTest extends AbstractBaseEAOTest<EventEAO, Event> {

	/**
	 * 
	 */
	public EventEAOTest() {
		super(EventEAO.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(Event created, Event found) {
		// TODO:move this to a Utility that compares the two.

		String createdEventName = created.getEventName();
		String foundEventName = found.getEventName();
		assertTrue("eventName: " + createdEventName + " == " + foundEventName,
				!createdEventName.equals(foundEventName));

	}

	@Override
	protected void mutate(Event found) {
		found.setEventName(TestUtil.generateRandomString());

	}

	@Override
	protected void assertEntityValuesEquals(Event created, Event found) {
		assertEquals("eventName", created.getEventName(), found.getEventName());

	}

	@Override
	protected Event generateRandomEntity() {
		return EventTest.getInstance().generateRandomEntity();

	}

	@Test
	public void testFindByProgramEventName() throws DuplicateKeyException,
			EntityNotFoundException {
		Event event = generateRandomPersistedEntity();
		Event found;
		try {
			found = eao.getByEventAndProgramName(event.getEventName(),
					event.getProgramName());
			assertNotNull(found);
			assertEquals(event, found);
		} finally {
			eao.delete(event);
		}
	}

}
