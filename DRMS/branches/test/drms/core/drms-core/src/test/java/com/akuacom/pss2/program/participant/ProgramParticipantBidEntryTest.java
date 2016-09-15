package com.akuacom.pss2.program.participant;

import com.akuacom.ejb.BaseEntityFixture;
import static com.akuacom.test.TestUtil.*;
import static org.junit.Assert.*;

public class ProgramParticipantBidEntryTest extends BaseEntityFixture<ProgramParticipantBidEntry> {

	@Override
	public ProgramParticipantBidEntry generateRandomIncompleteEntity() {
		ProgramParticipantBidEntry entry = new ProgramParticipantBidEntry();
		
		double reductionKw = generateRandomDouble();
		entry.setReductionKW(reductionKw);
		assertEquals(reductionKw, entry.getReductionKW(), 0.01);
		
		return entry;
	}
	
	

}
