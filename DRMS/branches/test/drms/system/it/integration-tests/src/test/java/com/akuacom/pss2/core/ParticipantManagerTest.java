package com.akuacom.pss2.core;

import static com.akuacom.pss2.participant.ParticipantUtil.DEFAULT_PASSWORD;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;


import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.test.TestUtil;

public class ParticipantManagerTest extends ParticipantManagerFixture {

	/**
	 * Largest number that should be generated to avoid database constraint
	 * errors.
	 * 
	 */
	public static final int PARTIPANT_USER_MAX_LENGTH = 99;

	@Test
	public void testCreateParticipant() {
		Participant p = generateRandomPersistedParticipant();

		final Participant p1 = pm.getParticipant(p.getParticipantName());
		try {
			Assert.assertNotNull(p1);

			ParticipantUtil.compareParticipants(p, p1);
		} finally {
			pm.removeParticipant(p.getParticipantName());
		}

	}

	/**
	 * @return
	 */
	public static Participant generateRandomPersistedParticipant() {

		Participant p = ParticipantUtil.getParicipantInstance(TestUtil
				.generateRandomInt(PARTIPANT_USER_MAX_LENGTH));
		try {
			pm.createParticipant(p, DEFAULT_PASSWORD.toCharArray(), null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return p;
	}

	@Test
	public void testUpdateParticipant() {
		Participant p = generateRandomPersistedParticipant();

		String user = p.getParticipantName();
		try {

			final Participant p1 = pm.getParticipant(user);
			Assert.assertNotNull(p1);

			p1.setAccountNumber("account2");
			// p1.setContacts();
			// p1.setExternalContacts();
			p1.setMeterId("meter2");
			pm.updateParticipant(p1);

			final Participant p2 = pm.getParticipant(user);
			ParticipantUtil.compareParticipants(p1, p2);
		} finally {
			pm.removeParticipant(user);
		}

	}
	
	

	@Test
	public void testCreateClient() {
		Participant p = generateRandomPersistedParticipant();

		
		String user = p.getParticipantName();
        try {
			final Participant p1 = pm.getParticipant(user);
			Assert.assertNotNull(p1);

			ParticipantUtil.compareParticipants(p, p1);

	
			Participant c = ClientTest.createPersistedClientFor(p1);
			String clientName = c.getParticipantName();
			final Participant c1 = cm.getClient(clientName);
			assertNotNull(c1);

			ParticipantUtil.compareParticipants(c, c1);

			final List<Participant> infoList = cm.getClients(c.getParent());
			Assert.assertEquals(1, infoList.size());
			for (Participant ci : infoList) {
				if (clientName.equals(ci.getParticipantName())) {
					Assert.assertEquals(c.getParent(), ci.getParent());
				}
			}

		} finally {

            pm.removeParticipant(user);
		}

	}
}
