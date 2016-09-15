package com.akuacom.pss2.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;

/**
 * Unit test for Event manager Bean
 * @author Daoping Zhang
 */
public class EventManagerBeanTest {
	
	public static final String SIGNAL_NUMBER="number";
	public static final String SINGAL_NAME="test signal";
	public static final String SIGNAL_NAME1="test signal 1";
	
	protected EventManagerBean eventManager;
	
	public static Date offSet(Date date,int min){
		return new Date(date.getTime()+min*60*1000);
	}
	
	@Before
	public void setup(){
		eventManager= new EventManagerBean();
	}
	
	@Test
	//DRMS-1392
	public void testGetSignalStateForEventParticipant(){
		Date now = new Date();
		//prepare data
		EventParticipant ePart = new EventParticipant();
		
		List<EventParticipantSignalEntry> entries = new ArrayList<EventParticipantSignalEntry>();
		/*  needs signal refactor
		EventParticipantSignalEntry signalEntry1 = new EventParticipantSignalEntry();
		signalEntry1.setSignalType(SIGNAL_NUMBER);
		//two hours ago
		signalEntry1.setStartTime(offSet(now,-120));
		signalEntry1.setSignalName(SINGAL_NAME);
		signalEntry1.setValue(120+"");
		signalEntry1.setEventParticipant(ePart);
		
		EventParticipantSignalEntry signalEntry2 = new EventParticipantSignalEntry();
		signalEntry2.setSignalType(SIGNAL_NUMBER);
		//one hour ago
		signalEntry2.setStartTime(offSet(now,-60));
		signalEntry2.setSignalName(SINGAL_NAME);
		signalEntry2.setValue(150+"");
		signalEntry2.setEventParticipant(ePart);
		
		EventParticipantSignalEntry signalEntry3 = new EventParticipantSignalEntry();
		signalEntry3.setSignalType(SIGNAL_NUMBER);
		//one hour later
		signalEntry3.setStartTime(offSet(now,60));
		signalEntry3.setSignalName(SINGAL_NAME);
		signalEntry3.setValue(150+"");
		signalEntry3.setEventParticipant(ePart);
		
		
		
		//add to entries in random sequence 
		entries.add(signalEntry3);
		entries.add(signalEntry1);
		entries.add(signalEntry2);
		ePart.setSignalEntries(entries);
		
		SignalState signalState=
				eventManager.getSignalStateForEventParticipant(ePart,SINGAL_NAME);
		
		assertEquals(NumberSignalState.class,signalState.getClass());
		assertEquals(150.0,((NumberSignalState)signalState).getNumber(),0.00001);
		assertEquals(SINGAL_NAME,signalState.getSignalName());
		
		//now add another signal entry
		EventParticipantSignalEntry signalEntry4 = new EventParticipantSignalEntry();
		signalEntry4.setSignalType(SIGNAL_NUMBER+"dd");
		
		//half an hour ago
		signalEntry4.setStartTime(offSet(now,-30));
		signalEntry4.setSignalName(SINGAL_NAME);
		signalEntry4.setValue(124+"");
		signalEntry4.setEventParticipant(ePart);
		entries.add(signalEntry4);
		
		signalState=
			eventManager.getSignalStateForEventParticipant(ePart,SINGAL_NAME);
		
		assertEquals(LevelSignalState.class,signalState.getClass());
		assertEquals(SINGAL_NAME,signalState.getSignalName());
		assertEquals(124+"",signalState.getValue());
		*/
	}
	
	@Test
	//DRMS-1392
	public  void testGetSignalStateForEventParticipantViaProgamCache(){
		
		Date now = new Date();
		//prepare data
		EventParticipant ePart = new EventParticipant();
		List<EventParticipantSignalEntry> entries = new ArrayList<EventParticipantSignalEntry>();
		/*  needs signal refactor
		EventParticipantSignalEntry signalEntry1 = new EventParticipantSignalEntry();
		signalEntry1.setSignalType(SIGNAL_NUMBER);
		//two hours later
		signalEntry1.setStartTime(offSet(now,120));
		signalEntry1.setSignalName(SINGAL_NAME);
		signalEntry1.setValue(110+"");
		signalEntry1.setEventParticipant(ePart);

		entries.add(signalEntry1);
		ePart.setSignalEntries(entries);
		
		Signal signal = new Signal();
		signal.setSignalName(SINGAL_NAME);
		signal.setType(SIGNAL_NUMBER);
		
		EasyMock.expect(
			programNPCache.getSignal(SINGAL_NAME))
				.andReturn(signal);
		EasyMock.replay(programNPCache);
		
		SignalState signalState=
			eventManager.getSignalStateForEventParticipant(ePart,SINGAL_NAME);
		assertEquals(NumberSignalState.class,signalState.getClass());
		//default value is 0.0
		assertEquals(0.0,((NumberSignalState)signalState).getNumber(),0.00001);
		assertEquals(SINGAL_NAME,signalState.getSignalName());
		
		EasyMock.verify(programNPCache);
		*/
	}
	
}
