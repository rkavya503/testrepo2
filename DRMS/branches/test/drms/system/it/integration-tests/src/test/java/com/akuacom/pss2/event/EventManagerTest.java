package com.akuacom.pss2.event;

import static com.akuacom.pss2.participant.ParticipantUtil.DEFAULT_PASSWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.core.ParticipantManagerFixture;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.test.TestUtil;


public class EventManagerTest extends ParticipantManagerFixture {

	private String programName = null;

	private boolean ignore = false;

	@Before
	public void checkProgram() {
        programName = getFirstCPPProgramName();
        Calendar cal = Calendar.getInstance();
        int h = cal.get(Calendar.HOUR_OF_DAY);
        ignore = programName == null || 21 <= h; // don't run test at late night.
	}

    //@Ignore("not ready yet")
    @Test
	public void testCreateBareEvent() {
		if (ignore)
			return;

		Event event = generateEvent();
		em.createEvent(programName, event);
		String eventName = event.getEventName();
		Event found = em.getEvent(eventName);
		assertEquals(eventName, found.getEventName());
		em.removeEvent(programName, eventName);
		assertNull(em.getEvent(eventName));
	}

	private Event generateEvent() {
		com.akuacom.pss2.event.Event event = new Event();
		event.setProgramName(programName);

		int oneMinute = 60000;
		Date now = new Date(System.currentTimeMillis() + oneMinute);
		String eventName = EventTest.generateRandomEventName();
		event.setEventName(eventName);

		event.setIssuedTime(now);
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

    //@Ignore("not ready yet")
    @Test
	public void testCreateEventWithParticipant() {

		if (ignore)
			return;

		final Participant p0 = ParticipantUtil.getParicipantInstance(TestUtil
				.generateRandomInt());
		pm.createParticipant(p0, DEFAULT_PASSWORD.toCharArray(), null);
		programParticipantManager.addParticipantToProgram(programName,
				p0.getUser(), false);

		Event event = generateEvent();


		em.createEvent(programName, event);


		 //removed sleep event...wait statements require a good explanation
		// about why they are required.
		em.removeEvent(programName, event.getEventName());

		//programManager.removeParticipantFromProgram(CPP_TEST, p0.getUser(),false);
		pm.removeParticipant(p0.getUser());
	
    }
	
	@Ignore("not ready yet")
    @Test
	public void testRemoveParticipantFromActiveEvent() {

		if (ignore)
			return;
		final Participant p0 = ParticipantUtil.getParicipantInstance(TestUtil
				.generateRandomInt());
		pm.createParticipant(p0, DEFAULT_PASSWORD.toCharArray(), null);
		programParticipantManager.addParticipantToProgram(programName,
				p0.getUser(), false);

		Event event = generateEvent();
		em.createEvent(programName, event);

        // Remove participant from event (Opt-out)
        em.removeParticipantFromEvent(event.getEventName(), p0.getParticipantName());

        // delete the event and participant
		em.removeEvent(programName, event.getEventName());
		pm.removeParticipant(p0.getUser());

    }

    @Ignore("not ready yet")
    @Test
	public void testEventParticipantVersions() {
		if (ignore)
			return;

		// Making sure there is no event with same name already existing.
		String eventName = EventTest.generateRandomEventName();
		try {
			em.removeEvent(programName, eventName);
		} catch (Exception e) {// expected}

			// set up
			final Participant p0 = ParticipantUtil
					.getParicipantInstance(TestUtil.generateRandomInt());
			pm.createParticipant(p0, DEFAULT_PASSWORD.toCharArray(), null);
			programParticipantManager.addParticipantToProgram(programName,
					p0.getUser(), false);
			
			Event event = generateEvent();
			em.createEvent(programName, event);
			// removed sleep event...wait statements require a good explanation
			// about why they are required.

			// check versioning
			try {

				EventParticipant ep = em.getEventParticipant(
						event.getEventName(), p0.getParticipantName(),
						p0.isClient());
				assertNotNull("Did not retrieve Event Participant", ep);

				EventParticipant ep2 = em.setEventParticipant(ep);

				org.junit.Assert.assertSame(ep.getEventModNumber(),
						ep2.getEventModNumber());

				// removed sleep event...wait statements require a good
				// explanation about why they are required.

			} finally {
				// clean up
				em.removeEvent(programName, event.getEventName());
				// programManager.removeParticipantFromProgram(CPP_TEST,
				// p0.getUser(),false);
				// pm.removeParticipant(p0.getUser());
			}
		}
	}

	@Test
	/** All the event participant is client participant **/
	public void testDemoEventCreationWithClientParticipant(){
//		String testParticipant ="test participant";
		
//		pm.createParticipant(p);
		//prepared data
		
		//create a participant 
		
		
		//add several clients 
		
		//prepare the UtilityDREvent
	}
}
