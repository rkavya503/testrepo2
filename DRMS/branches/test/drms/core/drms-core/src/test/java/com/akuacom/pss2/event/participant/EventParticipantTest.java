package com.akuacom.pss2.event.participant;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.participant.bid.ParticipantBidState;

import static com.akuacom.test.TestUtil.*;
import static org.junit.Assert.*;

/**
 * Provides unit tests for the Event Participant entity.
 * 
 * @author Brian Chapman
 * 
 * Created on 2010.11.11
 *
 */
public class EventParticipantTest extends BaseEntityFixture<EventParticipant> {

	@Override
	public EventParticipant generateRandomIncompleteEntity() {
		EventParticipant ep = new EventParticipant();
		
		assertEquals(ParticipantBidState.Pending, ep.getBidState());
		ParticipantBidState bidState = ParticipantBidState.Accepted;
		ep.setBidState(bidState);
		assertEquals(bidState, ep.getBidState());
		
		int eventModNumber = generateRandomInt(1024);
		ep.setEventModNumber(eventModNumber);
		assertEquals(eventModNumber, ep.getEventModNumber());
		
		return ep;
	}

}
