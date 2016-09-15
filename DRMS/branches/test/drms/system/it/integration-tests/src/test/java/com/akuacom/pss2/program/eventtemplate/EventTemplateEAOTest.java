package com.akuacom.pss2.program.eventtemplate;

import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.ejb.DuplicateKeyException;

/**
 * Tests the {@link EventTeamplateEAO} and {@link EventTemplateEAOBean}
 * implementation.
 * 
 * @author Brian Chapman
 * 
 *         Created 2010.11.03
 * 
 */
public class EventTemplateEAOTest extends
		AbstractVersionedEAOTest<EventTemplateEAO, EventTemplate> {

	public EventTemplateEAOTest() {
		super(EventTemplateEAO.class);
	}

	@Test
	public void testCreate() {
		EventTemplate eventTemplate = generateRandomEntity();
		try {
			eao.create(eventTemplate);
		} catch (DuplicateKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void assertEntityValuesNotEquals(EventTemplate created,
			EventTemplate found) {
		assertTrue(!found.getName().equals(created.getName()));

	}

	@Override
	protected void mutate(EventTemplate found) {
		found.setName(generateRandomStringOfLength(64));

	}

	@Override
	protected void assertEntityValuesEquals(EventTemplate created,
			EventTemplate found) {
		assertEquals(found.getName(), created.getName());

	}

	@Override
	protected EventTemplate generateRandomEntity() {
		EventTemplate eventTemplate = new EventTemplateTest()
				.generateRandomIncompleteEntity();

		assertNotNull(eventTemplate);
		return eventTemplate;
	}
}
