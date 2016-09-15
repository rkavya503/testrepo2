package com.akuacom.pss2.participant;

import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.program.participant.ProgramParticipant;

/**
 * POJO test for {@link ProgramParticipant}
 * 
 * @author Brian Chapman
 * 
 */
public class ProgramParticipantTest extends
		BaseEntityFixture<ProgramParticipant> {

	public ProgramParticipant generateRandomIncompleteEntity() {
		ProgramParticipant participant = new ProgramParticipant();

		String name = generateRandomStringOfLength(64);
		participant.setProgramName(name);
		assertEquals(name, participant.getProgramName());

		return participant;
	}
}