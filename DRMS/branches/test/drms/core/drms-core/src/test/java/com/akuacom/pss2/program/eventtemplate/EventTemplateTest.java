package com.akuacom.pss2.program.eventtemplate;

import static com.akuacom.test.TestUtil.generateRandomDate;
import static com.akuacom.test.TestUtil.generateRandomString;
import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * Tests the {@link EventTemplate} Entity
 * 
 * @author Brian Chapman
 * 
 *         Created 2010.11.03
 * 
 */
public class EventTemplateTest extends BaseEntityFixture<EventTemplate> {

	@Override
	public EventTemplate generateRandomIncompleteEntity() {
		EventTemplate eventTemplate = new EventTemplate();

		String name = generateRandomStringOfLength(64);
		eventTemplate.setName(name);
		assertEquals(name, eventTemplate.getName());

		String programName = generateRandomString();
		eventTemplate.setProgramName(programName);
		assertEquals(programName, eventTemplate.getProgramName());
		
		Date startTime = generateRandomDate();
		eventTemplate.setStartTime(startTime);
		assertEquals(startTime, eventTemplate.getStartTime());
		
		return eventTemplate;
	}
}
