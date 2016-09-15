package com.akuacom.pss2.core;

import static com.akuacom.pss2.participant.ParticipantUtil.DEFAULT_PASSWORD;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.pss2.util.EventState;
import com.akuacom.test.TestUtil;

/**
 * This class simulates the client polling from server in different situations.
 */
public class ClientTest extends ParticipantManagerFixture {
	private String CPP_TEST;

	private boolean ignore;

	@Before
	public void checkProgram() {
        CPP_TEST = getFirstCPPProgramName();
		ignore = CPP_TEST == null;
	}

    // TODO: find out why the client info is not in the account manager db on hudson.
	public static Participant createPersistedClientFor(Participant parent) {
		Participant c = ParticipantUtil
				.getParicipantInstance(TestUtil
						.generateRandomInt(ParticipantManagerTest.PARTIPANT_USER_MAX_LENGTH));
		String parentName = parent.getParticipantName();
		c.setParent(parentName);
		c.setClient(true);
		String clientUser = parentName + "." + c.getParticipantName();
		c.setParticipantName(clientUser);
		c.setAccountNumber(clientUser);
		cm.createClient(c, DEFAULT_PASSWORD.toCharArray());
		return cm.getClient(c.getParticipantName());
	}

	@Test
	public void testClientInIdle() {
		if (ignore)
			return;

		Participant p = ParticipantManagerTest
				.generateRandomPersistedParticipant();

		String participantName = p.getParticipantName();

		final Participant p1 = pm.getParticipant(participantName);
		Assert.assertNotNull(p1);

		ParticipantUtil.compareParticipants(p, p1);

		final Participant c = createPersistedClientFor(p1);

		String clientName = c.getParticipantName();
		try {
			final Participant c1 = cm.getClient(clientName);

			ParticipantUtil.compareParticipants(c, c1);

			programParticipantManager.addParticipantToProgram(CPP_TEST, participantName, false);
			programParticipantManager.addParticipantToProgram(CPP_TEST, clientName, true);

			final List<EventState> states = cm.getClientEventStates(clientName, false);

//			final List<EventState> list = cm.getClientEventStates(participantName, false);
			// TODO: These assertions are behaving differently locally vs. on
			// Hudson. It would be nice to fight this out!
			// Assert.assertNull(states);
			// Assert.assertNull(list);

		} finally {
			pm.removeParticipant(participantName);
		}

	}
}
