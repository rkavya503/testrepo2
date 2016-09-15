package com.akuacom.pss2.program.participant;

import static com.akuacom.test.TestUtil.generateRandomInt;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * Unit tests for the {@link ProgramParticipantRule} entity
 * 
 * @author Brian Chapman
 *
 */
public class ProgramParticipantRuleTest extends
			BaseEntityFixture<ProgramParticipantRule> {

		@Override
		public ProgramParticipantRule generateRandomIncompleteEntity() {
			ProgramParticipantRule entry = new ProgramParticipantRule();

			Integer sortOrder = generateRandomInt(1024);
			entry.setSortOrder(sortOrder);
			assertEquals(sortOrder, entry.getSortOrder());

			return entry;
		}

	}
