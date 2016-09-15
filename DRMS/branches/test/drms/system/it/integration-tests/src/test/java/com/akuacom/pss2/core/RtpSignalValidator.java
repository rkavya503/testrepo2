package com.akuacom.pss2.core;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.akuacom.drms.test.EventValidator;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.OperationModeValue;


/**
 * Tests creation of an RTP Signal To run this test run and deploy pss2 sce
 * cofiguration locally. Make sure you have RTP Agri program. Configure RTP
 * program - with SUMMER and WINTER Season and a holiday. Put in the price
 * table. Put in weather data for a yesterday INSERT INTO scertp_weather
 * VALUES(REPLACE(uuid(), '-',
 * ''),22,now(),now(),'system','system','2010-06-23',12,76,76,77,76,77,75,75);
 * Create a participant test Create a client test.test Subscribe the participant
 * client - test.test to RTP agri program Now run this program as a JUNI test by
 * removing the ignore.
 * 
 * @author Sunil
 * 
 */
public class RtpSignalValidator extends ParticipantManagerFixture {

	/**
	 * RTP Program Name
	 */
	private static final String RTP_AGRI_PROG = "RTP Agricultural";

	/**
	 * RTP Agriculture Event Name
	 */
	private static final String RTP_AGRI_EVENT_NAME = "RTP Agricultural Event";

	/**
	 * Participant name
	 */
	private static final String PARTICIPANT_NAME = "test.test";

	/**
	 * Test Creation for a RTP Agri Event
	 */
	@Test
	public void testCreateRtpAgriEvent() {

		List<EventInfo> events = null;
		String participantName = null;

		Program rtpAgriProgram = programManager.getProgram(RTP_AGRI_PROG);
		Assert.assertNotNull("No RTP AGRI Program found", rtpAgriProgram);
		events = programManager.getEventsForProgram(RTP_AGRI_PROG);
		try {
			for (EventInfo event : events) {
				em.removeEvent(RTP_AGRI_PROG, event.getEventName());
			}

			Event event = generateEvent();
			em.createEvent(RTP_AGRI_PROG, event);

			events = programManager.getEventsForProgram(RTP_AGRI_PROG);
			Assert.assertNotNull("Event not created properly", events);
			Assert.assertTrue(events.size() == 2);

			List<EventState> eventStates = cm.getClientEventStates(PARTICIPANT_NAME, false);
			// List<EventState> eventStates =
			// pm.getParticipantEventStates(participantName + "." + clientName,
			// true);
			Assert.assertNotNull(eventStates);
			Assert.assertTrue(eventStates.size() == 2);
			EventState eventState = eventStates.get(0);
			Assert.assertNotNull(eventState);
			Assert.assertEquals(eventState.getEventStatus(), EventStatus.ACTIVE);
			Assert.assertEquals(eventState.getOperationModeValue(),
					OperationModeValue.NORMAL);
			Assert.assertNotNull(eventState.getEventInfoInstances());
			Assert.assertTrue(eventState.getEventInfoInstances().size() > 0);
			eventState = eventStates.get(1);
			Assert.assertNotNull(eventState);
			Assert.assertEquals(eventState.getEventStatus(), EventStatus.FAR);
			Assert.assertEquals(eventState.getOperationModeValue(),
					OperationModeValue.NORMAL);
			Assert.assertNotNull(eventState.getEventInfoInstances());
			Assert.assertTrue(eventState.getEventInfoInstances().size() > 0);

		} finally {
			for (EventInfo event : events) {
				em.removeEvent(RTP_AGRI_PROG, event.getEventName());
			}
		}

	}

	/**
	 * Validates RTP Agri signals after creating an event. Cleans up after.
	 * 
	 * @param validationRulesFile
	 *            URL to the validation rule file
	 * @param poolingInterval
	 *            Pooling interval in minutes default is pool every 10 min
	 * @param numberOfTries
	 *            number of tries default is 144 tries
	 */
	public void validateRtpSignals(URL validationRulesFile,
			int poolingInterval, int numberOfTries) {
		Calendar baseTime = Calendar.getInstance();
		if (poolingInterval == 0) {
			poolingInterval = 10;
		}
		if (numberOfTries == 0) {
			numberOfTries = 144;
		}

		List<EventInfo> events = null;
		try {

			EventValidator validator = new EventValidator(baseTime,
					validationRulesFile);
			System.out.println("Validating Signals form RTP AGRI Program");
			events = programManager.getEventsForProgram(RTP_AGRI_PROG);
			for (EventInfo event : events) {
				em.removeEvent(RTP_AGRI_PROG, event.getEventName());
			}

			Event event = generateEvent();
			em.createEvent(RTP_AGRI_PROG, event);

			for (int i = 0; i < numberOfTries; i++) {
				List<EventState> eventStates = cm.getClientEventStates(PARTICIPANT_NAME, false);
				long timeOffset = Calendar.getInstance().getTimeInMillis()
						- baseTime.getTimeInMillis();
				boolean result = validator
						.isEventValid(eventStates, timeOffset);
				StringBuffer sb = new StringBuffer((result ? "SUCCESS "
						: "FAILURE ")
						+ " Time Offset = "
						+ timeOffset
						/ (1000 * 60)
						+ " min. No of Events = "
						+ eventStates.size() + " ");
				for (EventState eventState : eventStates) {
					sb.append("Event Status " + eventState.getEventStatus()
							+ " Event Operation Mode "
							+ eventState.getOperationModeValue() + " ; ");
				}
				System.out.println(sb.toString());
				Thread.sleep(poolingInterval * 60 * 1000);
			}

		} catch (Exception e) {
			System.out.println("Exception: " + e.getLocalizedMessage());
			e.printStackTrace();

		} finally {
			for (EventInfo event : events) {
				em.removeEvent(RTP_AGRI_PROG, event.getEventName());
			}
		}

	}

	/**
	 * Main method
	 * 
	 * @param args
	 *            command line arguments
	 * @throws Exception
	 *             Failure
	 */
	public static void main(String[] args) throws Exception {
		RtpSignalValidator rsv = new RtpSignalValidator();
		rsv.setUpManagers();
		URL url = null;
		int interval = 0;
		int tries = 0;

		if (args == null || args.length < 2) {
			rsv.usage();
			System.exit(1);
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-url")) {
				url = new URL(args[++i]);
			} else if (args[i].equals("-interval")) {
				interval = Integer.valueOf(args[++i]);
			} else if (args[i].equals("-tries")) {
				tries = Integer.valueOf(args[++i]);
			}
		}
		if (url == null) {
			rsv.usage();
			System.exit(1);
		}
		rsv.validateRtpSignals(url, interval, tries);

	}

	/**
	 * Usage
	 */
	private void usage() {
		System.out
				.println("Usage: java com.akuacom.pss2.core.RtpSignalValidator -url <validation_XML_URL> -interval [Pooling interval in min - default 10 min] -tries [Number of tries - default 144]");
	}

	/**
	 * Generate Event
	 * 
	 * @return Event
	 */
	private Event generateEvent() {
		Event event = new Event();
		event.setProgramName(RTP_AGRI_PROG);
		event.setEventName(RTP_AGRI_EVENT_NAME);
		Date now = new Date();
		GregorianCalendar calender = new GregorianCalendar();
		calender.setTime(now);
		calender.add(Calendar.DAY_OF_MONTH, 1);
		calender.set(Calendar.HOUR_OF_DAY, 14);
		calender.set(Calendar.MINUTE, 0);
		calender.set(Calendar.SECOND, 0);
		calender.set(Calendar.MILLISECOND, 0);
		event.setStartTime(calender.getTime());
		calender.set(Calendar.HOUR_OF_DAY, 18);
		event.setEndTime(calender.getTime());
		event.setReceivedTime(now);
		return event;
	}

}
