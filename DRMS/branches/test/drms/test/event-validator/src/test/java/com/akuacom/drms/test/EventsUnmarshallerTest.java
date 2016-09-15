package com.akuacom.drms.test;



import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.akuacom.pss2.event.validator.EventDataType;
import com.akuacom.pss2.event.validator.EventType;
import com.akuacom.pss2.event.validator.Events;

/**
 * Tests EventsUnmarshaller
 * @author Sunil
 *
 */
public class EventsUnmarshallerTest {
	
	/**
	 * Tests EventsUnmarshaller
	 * @throws Exception Failure
	 */
	@Test public void testUnmarshal() throws Exception {
		URL eventsFile = Thread.currentThread().getContextClassLoader().getResource("events.xml");
		Events events = EventsUnmarshaller.getInstance().unmarshal(eventsFile);
		Assert.assertNotNull(events);
		List<EventType> eventList = events.getEvent();
		Assert.assertNotNull(eventList);
		Assert.assertTrue(eventList.size() == 2);
		EventType event1 = eventList.get(0);
		Assert.assertNotNull(event1);
		Assert.assertEquals(event1.getStart(), 0);
		Assert.assertEquals(event1.getStop(), 60);
		List<EventDataType> event1DataList = event1.getEventData();
		Assert.assertNotNull(event1DataList);
		Assert.assertTrue(event1DataList.size() == 2);
		EventDataType eventData = event1DataList.get(0);
		Assert.assertNotNull(eventData);
		Assert.assertEquals(eventData.getEventStatus(), "FAR");
		Assert.assertEquals(eventData.getOperationMode(), "NORMAL");
	}
}
