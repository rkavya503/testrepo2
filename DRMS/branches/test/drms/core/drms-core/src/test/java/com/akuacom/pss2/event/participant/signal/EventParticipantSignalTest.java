package com.akuacom.pss2.event.participant.signal;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * Provides unit tests for the EventParticipantSignal entity.
 * 
 * @author Brian Chapman
 * 
 *         Created on 2010.11.11
 * 
 */
public class EventParticipantSignalTest extends
		BaseEntityFixture<EventParticipantSignal> {

	@Override
	public EventParticipantSignal generateRandomIncompleteEntity() {
		EventParticipantSignal signal = new EventParticipantSignal();

		/*
		 * Add getters and setters tests here as they are added to this entity.
		 */

		return signal;
	}

}
