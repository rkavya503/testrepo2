/**
 * 
 */
package com.akuacom.pss2.participant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.test.TestUtil;

/**
 * Tests the {@link UserEAO} and {@link UserEAOBean} implementation.
 * 
 * User does not follow the BaseEAO pattern so these tests are custom.
 * 
 * @author Brian Chapman
 * 
 *         Created 2010.11.03
 * 
 */
public class UserEAOTest extends
		AbstractVersionedEAOTest<ParticipantEAO, Participant> {

	public UserEAOTest() {
		super(ParticipantEAO.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(Participant created,
			Participant found) {
		assertTrue(!found.getFirstName().equals(created.getFirstName()));

	}

	@Override
	protected void mutate(Participant found) {
		found.setFirstName(TestUtil.generateRandomStringOfLength(45));

	}

	@Override
	protected void assertEntityValuesEquals(Participant created,
			Participant found) {
		assertEquals(found.getFirstName(), created.getFirstName());

	}

	@Override
	public Participant generateRandomEntity() {
		Participant participant = new ParticipantTest()
				.generateRandomIncompleteEntity();

		assertNotNull(participant);
		return participant;
	}
}
