package com.akuacom.pss2.program.participant;

import static com.akuacom.test.TestUtil.generateRandomString;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;

public class ProgramParticipantBidLevelMapTest extends
		BaseEntityFixture<ProgramParticipantBidLevelMap> {

	@Override
	public ProgramParticipantBidLevelMap generateRandomIncompleteEntity() {
		ProgramParticipantBidLevelMap entry = new ProgramParticipantBidLevelMap();

		String programName = generateRandomString();
		entry.setProgramName(programName);
		assertEquals(programName, entry.getProgramName());

		return entry;
	}

}
