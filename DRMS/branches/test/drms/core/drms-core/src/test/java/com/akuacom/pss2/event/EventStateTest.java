package com.akuacom.pss2.event;

import java.sql.Date;

import com.akuacom.ejb.BaseEntityFixture;
import static com.akuacom.test.TestUtil.*;
import static org.junit.Assert.assertEquals;

public class EventStateTest extends BaseEntityFixture<ClientConversationState> {

	@Override
	public ClientConversationState generateRandomIncompleteEntity() {
		final ClientConversationState e = new ClientConversationState();
		
		Date commTime = new Date(System.currentTimeMillis());
		e.setCommTime(commTime);
		assertEquals(commTime, e.getCommTime());

		String dras = generateRandomStringOfLength(64);
		e.setDrasClientId(dras);
		assertEquals(dras, e.getDrasClientId());
		
		String eventIdentifier = generateRandomStringOfLength(64);
		e.setEventIdentifier(eventIdentifier);
		assertEquals(eventIdentifier, e.getEventIdentifier());
		
		int mod =  generateRandomInt(1024);
		e.setEventModNumber(mod);
		assertEquals(mod, e.getEventModNumber());
		
		int state =  generateRandomInt(1024);
		e.setConversationStateId(state);
		assertEquals(state, e.getConversationStateId());
		
		String name = generateRandomString();
		e.setProgramName(name);
		assertEquals(name, e.getProgramName());
		
		return e;
	}
}
