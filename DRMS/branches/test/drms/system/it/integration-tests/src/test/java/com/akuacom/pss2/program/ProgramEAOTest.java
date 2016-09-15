/**
 * 
 */
package com.akuacom.pss2.program;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEAO;
import com.akuacom.pss2.signal.SignalEAOBean;
import com.akuacom.test.TestUtil;
/**
 * @author roller
 * 
 */
public class ProgramEAOTest extends
		AbstractVersionedEAOTest<ProgramEAO, Program> {

	public ProgramEAOTest() {
		super(ProgramEAOBean.class, Program.class, ProgramEAO.class,
				ProgramEAOBean.class, Signal.class, SignalEAO.class, 
				SignalEAOBean.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(Program created, Program found) {
	assertTrue("Descritpion should not be equal", !created.getDescription().equals(found.getDescription()))	;		
	}

	@Override
	protected void mutate(Program found) {
	 found.setDescription(TestUtil.generateRandomString());	
	}

	@Override
	protected void assertEntityValuesEquals(Program created, Program found) {
		assertEquals("description should be equal",created.getDescription(), found.getDescription());		
	}

	@Override
	protected Program generateRandomEntity() {
		Program program = new ProgramTest().generateRandomIncompleteEntity();

		assertNotNull(program);
		return program;
	}

}
