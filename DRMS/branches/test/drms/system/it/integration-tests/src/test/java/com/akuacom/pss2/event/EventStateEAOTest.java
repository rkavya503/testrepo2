package com.akuacom.pss2.event;

import java.sql.Date;
import static org.junit.Assert.*;

import com.akuacom.ejb.AbstractBaseEAOTest;
import com.akuacom.utils.lang.FieldUtil;

public class EventStateEAOTest extends
		AbstractBaseEAOTest<ClientConversationStateEAO, ClientConversationState> {
	public EventStateEAOTest() {
		super(ClientConversationStateEAO.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(ClientConversationState created, ClientConversationState found) {
		assertTrue(!created.getCommTime().equals(found.getCommTime()));
	}

	@Override
	protected void mutate(ClientConversationState found) {
		found.setCommTime(new Date(System.currentTimeMillis()));
	}

	@Override
	protected void assertEntityValuesEquals(ClientConversationState created, ClientConversationState found) {
		assertTrue(created.getCommTime().equals(found.getCommTime()));
	}

	@Override
	protected ClientConversationState generateRandomEntity() {
		ClientConversationState es = new EventStateTest().generateRandomIncompleteEntity();
		return es;
	}
}
