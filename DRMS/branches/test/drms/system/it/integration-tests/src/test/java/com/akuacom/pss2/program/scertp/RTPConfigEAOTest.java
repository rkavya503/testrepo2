package com.akuacom.pss2.program.scertp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramEAOBean;
import com.akuacom.pss2.program.ProgramEAOTest;
import com.akuacom.test.TestUtil;

public class RTPConfigEAOTest  extends
			AbstractVersionedEAOTest<RTPConfigEAO, RTPConfig> {

		public RTPConfigEAOTest() {
			super(RTPConfigEAOBean.class);
		}

		@Override
		protected void assertEntityValuesNotEquals(RTPConfig created,
				RTPConfig found) {
			assertTrue(found.getName() != created.getName());
		}

		@Override
		protected void mutate(RTPConfig found) {
			found.setName(TestUtil.generateRandomString());
		}

		@Override
		protected void assertEntityValuesEquals(RTPConfig created,
				RTPConfig found) {
			assertEquals(created.getName(), found.getName());
		}

		@Override
		protected RTPConfig generateRandomEntity() {
			RTPConfig rtp = new RTPConfigTest().generateRandomIncompleteEntity();
			
			Program p = null;
			try {
				p = new ProgramEAOTest().generateRandomPersistedEntity();
			} catch (DuplicateKeyException e) {
				e.printStackTrace();
			}
			rtp.setProgramVersionUuid(p.getUUID());
			
			assertNotNull(rtp);
			return rtp;
		}
		
		@Override
		public void delete(RTPConfig rtp) throws EntityNotFoundException {
			eao.delete(rtp);
			ProgramEAOTest programEaoTest = new ProgramEAOTest();
			programEaoTest.delete(rtp.getProgramVersionUuid());
		}
	}
