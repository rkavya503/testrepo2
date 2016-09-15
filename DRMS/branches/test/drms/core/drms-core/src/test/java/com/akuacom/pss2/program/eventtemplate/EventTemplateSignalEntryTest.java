package com.akuacom.pss2.program.eventtemplate;

import static com.akuacom.test.TestUtil.generateRandomInt;
import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * Tests the {@link EventTemplateSignalEntry} Entity
 * 
 * @author Brian Chapman
 * 
 */
public class EventTemplateSignalEntryTest extends
		BaseEntityFixture<EventTemplateSignalEntry> {

	@Override
	public EventTemplateSignalEntry generateRandomIncompleteEntity() {
		EventTemplateSignalEntry entry = new EventTemplateSignalEntry();

		String value = generateRandomStringOfLength(45);
		entry.setValue(value);
		assertEquals(value, entry.getValue());

		int relativeStartTime = generateRandomInt();
		entry.setRelativeStartTime(relativeStartTime);
		assertEquals(relativeStartTime, entry.getRelativeStartTime());

		String signalType = generateRandomStringOfLength(45);
		entry.setSignalType(signalType);
		assertEquals(signalType, entry.getSignalType());

		String name = generateRandomStringOfLength(45);
		entry.setSignalType(name);
		assertEquals(name, entry.getSignalType());

		return entry;
	}
}
