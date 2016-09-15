package com.akuacom.drms.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.OperationModeValue;

/**
 * Tests the class EventValidator
 * @author Sunil
 *
 */
public class EventValidatorTest {

	/**
	 * Tests EventsUnmarshaller
	 * @throws Exception Failure
	 */
	@Test public void testEventValidation() throws Exception {
		URL eventsFile = Thread.currentThread().getContextClassLoader().getResource("events.xml");
		EventValidator validator = new EventValidator(Calendar.getInstance(),eventsFile);
		Assert.assertNotNull(validator);
		List<EventState> eventStates = new ArrayList<EventState>();
		EventState es = new EventState();
		es.setEventStatus(EventStatus.FAR);
		es.setOperationModeValue(OperationModeValue.NORMAL);
		eventStates.add(es);
		EventState es1 = new EventState();
		es1.setEventStatus(EventStatus.ACTIVE);
		es1.setOperationModeValue(OperationModeValue.NORMAL);
		eventStates.add(es1);
		Assert.assertTrue(validator.isEventValid(eventStates, 0));
		Assert.assertTrue(validator.isEventValid(eventStates, 600000));
		Assert.assertFalse(validator.isEventValid(eventStates, 3600000));
		Assert.assertTrue(validator.isEventValid(eventStates, 900000000));
		
		
	}

}
