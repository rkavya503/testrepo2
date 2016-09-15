/**
 * 
 */
package com.akuacom.pss2.event;

import static com.akuacom.test.TestUtil.generateRandomDate;
import static com.akuacom.test.TestUtil.generateRandomInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.system.property.CoreProperty.PropertyType;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.PSS2Util;
import com.akuacom.test.TestUtil;

/**
 * @author Aaron Roller
 * 
 */
public class EventTest extends BaseEntityFixture<Event> {

	/**
	 * Use {@link getNewInstance} instead
	 */
	@Deprecated
	public static EventTest getInstance() { return EventTest.getNewInstance(); }
	
	public static EventTest getNewInstance() {
		return new EventTest();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.ejb.BaseEntityFixture#generateRandomIncompleteEntity()
	 */
	@Override
	public Event generateRandomIncompleteEntity() {
		Event event = new Event();
		event = generateRandomIncompleteEntity(event);
		
		return event;
	}
	
	/**
	 * Helpful for allowing entities that extend this to use this to create their own
	 * generateRandomIncompleteEntity method that extends from this.
	 * 
	 * @param <E>
	 * @param event
	 * @return
	 */
	protected <E extends Event> E generateRandomIncompleteEntity(E event) {

		ValidEventProvider eventProvider = new ValidEventProvider();
		
		String eventName = generateRandomEventName();
		event.setEventName(eventName);
		assertEquals(eventName, event.getEventName());

		Date startTime = eventProvider.startTime;
		event.setStartTime(startTime);
		assertEquals(startTime, event.getStartTime());

		Date endTime = eventProvider.endTime;
		event.setEndTime(endTime);
		assertEquals(endTime, event.getEndTime());

		Date receivedTime = generateRandomDate();
		event.setReceivedTime(receivedTime);
		assertEquals(receivedTime, event.getReceivedTime());

		
		Date issuedTime = eventProvider.issueTime;
		event.setIssuedTime(issuedTime);
		assertEquals(issuedTime, event.getIssuedTime());

		boolean manual = TestUtil.generateRandomBoolean();
		event.setManual(manual);
		assertEquals(manual, event.isManual());

		//TODO:should probably use a real program name from ProgramTest
		String programName = generateRandomEventName();
		event.setProgramName(programName);
		assertEquals(programName, event.getProgramName());

		String state = TestUtil.generateRandomString();
		event.setState(state);
		assertEquals(state, event.getState());

		EventStatus status = generateRandomEventStatus();
		event.setEventStatus(status);
		assertEquals(status, event.getEventStatus());

//		try {
//			ProgramValidator validator = new ProgramValidator();
//			validator.setProgram(ProgramTest.getProgramTest()
//					.generateRandomEntity());
//			validator.validateEvent(event, null);
//		} catch (ProgramValidationException e) {
//			fail(e.getErrors().toString());
//		}
		return event;
	}

	public static String generateRandomEventName() {
		String name = "Event " + TestUtil.generateRandomInt();
		assertTrue(PSS2Util.isLegalName(name));
		return name;
	}

	public static EventStatus generateRandomEventStatus() {
		return TestUtil.generateRandomFromArray(EventStatus.values());
	}

	/**Use this to help generate some random values, but still valid according to {@link ProgramValidator}
	 * 
	 * 
	 * @author Aaron Roller
	 *
	 */
	@SuppressWarnings("serial")
	public static class ValidEventProvider  {
		protected Date issueTime;
		protected Date startTime;
		private Date endTime;
		public ValidEventProvider(){
			issueTime = new Date();
			//this should come from ProgramValidator.TIME_BUFFER.
			int TIME_BUFFER = 10000;
			issueTime = new Date(
					issueTime.getTime()
							+ TestUtil
									.generateRandomInt((int) TIME_BUFFER));
			
			startTime = new Date(issueTime.getTime() + TestUtil.generateRandomInt(1000));
			endTime = new Date(startTime.getTime() + generateRandomInt(1000));
		}
		
	}
}
