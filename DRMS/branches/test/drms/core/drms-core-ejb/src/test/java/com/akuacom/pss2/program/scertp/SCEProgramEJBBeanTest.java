package com.akuacom.pss2.program.scertp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.util.EventState;

import static org.junit.Assert.assertEquals;

/**
 * @author Daoping Zhang
 * 
 * Test for SCEProgramEJBBean 
 */
//TODO more test needed to cover all the methods 
public class SCEProgramEJBBeanTest {
	
	protected TestableRTPProgramEJBBean rtpProgram;
	
    protected EventEAO.L eventEAO;
	
	public static final String PROGRAM_NAME = "rtp program";
	
	@Before
	public void setup(){
		rtpProgram = new TestableRTPProgramEJBBean();
		
		//mock a programDataAccess
		eventEAO = EasyMock.createMock(EventEAO.L.class);
		rtpProgram.setEventEAO(eventEAO);
	}
	
	protected static Event createEvent(String eventName,String programName,Date startTime){
		Event event = new Event();
		event.setEventName(eventName);
		event.setProgramName(programName);
		event.setStartTime(startTime);
		return event;
	}
	
	protected static Date offSet(Date date,int min){
		return new Date(date.getTime()+min*60*1000);
	}
	
	@Test
    @Ignore
	//DRMS-1388
	public void testGetEventsForTodayAndTomorrow() {
        try {
            //Events for both today and tomorrow
            //9:XX AM
            Date date = offSet(com.akuacom.utils.lang.DateUtil.getStartOfDay(new Date()), 9);

            Event event1 = createEvent("event1", PROGRAM_NAME, date);
            //21:XX PM
            Event event2 = createEvent("event2", PROGRAM_NAME, offSet(date, 12 * 60));
            //next day 9:XX AM
            Event event3 = createEvent("event3", PROGRAM_NAME, offSet(date, 24 * 60));

            List<Event> events = Arrays.asList(event1, event2, event3);

            EasyMock.expect(eventEAO.findByProgramName(PROGRAM_NAME)).andReturn(events).anyTimes();
            EasyMock.expect(eventEAO.getByEventName("event1")).andReturn(event1);
            EasyMock.expect(eventEAO.getByEventName("event2")).andReturn(event2);
            EasyMock.expect(eventEAO.getByEventName("event3")).andReturn(event3);

            EasyMock.replay(eventEAO);

            BitSet bs = rtpProgram.getEventsForTodayAndTomorrow(PROGRAM_NAME);
            assertEquals(true, bs.get(0));
            assertEquals(true, bs.get(1));

            EasyMock.verify(eventEAO);

            //Event only for today
            eventEAO = EasyMock.createMock(EventEAO.L.class);
            rtpProgram.setEventEAO(eventEAO);

            event1 = createEvent("event1", PROGRAM_NAME, date);
            events = Arrays.asList(event1);
            EasyMock.expect(eventEAO.findByProgramName(PROGRAM_NAME)).andReturn(events).anyTimes();
            EasyMock.expect(eventEAO.getByEventName("event1")).andReturn(event1);

            EasyMock.replay(eventEAO);
            bs = rtpProgram.getEventsForTodayAndTomorrow(PROGRAM_NAME);
            assertEquals(true, bs.get(0));
            assertEquals(false, bs.get(1));

            EasyMock.verify(eventEAO);

            //Event for for tomorrow
            eventEAO = EasyMock.createMock(EventEAO.L.class);
            rtpProgram.setEventEAO(eventEAO);

            event1 = createEvent("event1", PROGRAM_NAME, offSet(date, 24 * 60));
            events = Arrays.asList(event1);
            EasyMock.expect(eventEAO.findByProgramName(PROGRAM_NAME)).andReturn(events).anyTimes();
            EasyMock.expect(eventEAO.getByEventName("event1")).andReturn(event1);

            EasyMock.replay(eventEAO);
            bs = rtpProgram.getEventsForTodayAndTomorrow(PROGRAM_NAME);
            assertEquals(false, bs.get(0));
            assertEquals(true, bs.get(1));

            EasyMock.verify(eventEAO);
        } catch (EntityNotFoundException e) {
            Assert.fail(e.getMessage());
        }
    }
	
	@Test
	@Ignore 
	//Depends on the refactoring of SCEPRogramEJBBean
	//TO check if the event is created correctly
	public void testCreateEvent(){
		
	}
	
	@Test
	//DRMS-1392
	public void testGetParticipantEventStates(){
		//prepare data 
		Date now = new Date();
		
		String partifipantName="p1";
		String eventName ="event";
		EventState eventState = new EventState();
		eventState.setEventIdentifier(eventName);
		eventState.setStartTime(offSet(now,45));
		eventState.setEventInfoInstances(new ArrayList<EventInfoInstance>());
		
		Participant part = new Participant();
		part.setParticipantName(partifipantName);
		part.setClient(true);
		
		EventParticipant eventPart = new EventParticipant();
		eventPart.setParticipant(part);
		
		/*  needs signal refactor
		ProgramSignal psignal1 = new ProgramSignal();
		ProgramSignal signal = new ProgramSignal();
		signal.setType(EventInfoInstance.SignalType.LOAD_AMOUNT.name());
		signal.setSignalName("signal");
		psignal1.setSignal(signal);
		
		SortedArrayList<ProgramSignalEntry> programSignalEntries
			= new SortedArrayList<ProgramSignalEntry>();

		ProgramNumberSignalEntry signalEntry1 = new ProgramNumberSignalEntry();
		signalEntry1.setNumber(12.30);
		signalEntry1.setSignalName("signal");
		signalEntry1.setTime(now);
		
		ProgramNumberSignalEntry signalEntry2 = new ProgramNumberSignalEntry();
		signalEntry2.setNumber(16.50);
		signalEntry2.setSignalName("signal");
		signalEntry2.setTime(offSet(now,-60));
		
		ProgramNumberSignalEntry signalEntry3 = new ProgramNumberSignalEntry();
		signalEntry3.setNumber(8.10);
		signalEntry3.setSignalName("signal");
		signalEntry3.setTime(offSet(now,60));
		
		programSignalEntries.add(signalEntry1);
		programSignalEntries.add(signalEntry2);
		programSignalEntries.add(signalEntry3);
		
		psignal1.setSignalEntries(programSignalEntries);
		
		ProgramSignal psignal2 = new ProgramSignal();
		ProgramSignal signal2 = new ProgramSignal();
		signal2.setType(EventInfoInstance.SignalType.LOAD_AMOUNT.name());
		signal2.setSignalName("signal");
		psignal2.setSignal(signal2);
		
		
		SortedArrayList<ProgramSignalEntry> programSignalEntries2
		= new SortedArrayList<ProgramSignalEntry>();

		ProgramNumberSignalEntry signalEntry4 = new ProgramNumberSignalEntry();
		signalEntry4.setNumber(32.30);
		signalEntry4.setSignalName("signal");
		signalEntry4.setTime(now);
	
		ProgramNumberSignalEntry signalEntry5 = new ProgramNumberSignalEntry();
		signalEntry5.setNumber(1.50);
		signalEntry5.setSignalName("signal");
		signalEntry5.setTime(offSet(now,-120));
	
		ProgramNumberSignalEntry signalEntry6 = new ProgramNumberSignalEntry();
		signalEntry6.setNumber(10.10);
		signalEntry6.setSignalName("signal");
		signalEntry6.setTime(offSet(now,120));
	
		programSignalEntries2.add(signalEntry4);
		programSignalEntries2.add(signalEntry5);
		programSignalEntries2.add(signalEntry6);
	
		psignal2.setSignalEntries(programSignalEntries2);
	
		List<ProgramSignal> psignals = new ArrayList<ProgramSignal>();
		psignals.add(psignal1);
		psignals.add(psignal2);
		
		EasyMock.expect(programDataAccess.
					getEventParticipantSignals(PROGRAM_NAME,eventName,partifipantName,true))
					.andReturn(psignals);
		
		EasyMock.replay(programDataAccess);
		
		EventState estate=rtpProgram.getParticipantEventStates
									(PROGRAM_NAME,eventPart,eventState);
		
		List<EventInfoInstance> eventInfoList=estate.getEventInfoInstances();
		assertEquals(2,eventInfoList.size());
		
		EventInfoInstance instance1= eventInfoList.get(0);
		List<com.akuacom.pss2.util.EventInfoValue> values1=instance1.getEventInfoValues();
		assertEquals(3,values1.size());
		
		assertTrue(values1.get(0).getTimeOffsetS()-values1.get(0).getTimeOffsetS()<=0);
		assertTrue(values1.get(1).getTimeOffsetS()-values1.get(2).getTimeOffsetS()<=0);
		
		List<com.akuacom.pss2.util.EventInfoValue> values2=instance1.getEventInfoValues();
		assertEquals(3,values2.size());
		
		assertTrue(values2.get(0).getTimeOffsetS()-values2.get(0).getTimeOffsetS()<=0);
		assertTrue(values2.get(1).getTimeOffsetS()-values2.get(2).getTimeOffsetS()<=0);
		*/
	}
		
	
	/**
	 * Extends RTPProgramEJBBean to make it unit-testable
	 */
	protected static class TestableRTPProgramEJBBean extends SCERTPProgramEJBBean {
		
		@Override
		public BitSet getEventsForTodayAndTomorrow(String programName){
			return super.getEventsForTodayAndTomorrow(programName);
		}
	}
	
}
