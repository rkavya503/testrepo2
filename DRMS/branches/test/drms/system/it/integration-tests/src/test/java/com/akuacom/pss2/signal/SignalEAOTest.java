package com.akuacom.pss2.signal;

import static org.junit.Assert.*;
import org.junit.Test;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.test.TestUtil;

public class SignalEAOTest extends
		AbstractVersionedEAOTest<SignalEAO, SignalDef> {

	public SignalEAOTest() {
		super(SignalEAOBean.class, SignalEAO.class, SignalEAOBean.class,
				Signal.class, SignalLevelDef.class);

	}

	@Test
	public void testSomething() {
	}

	@Override
	protected void assertEntityValuesEquals(SignalDef created, SignalDef found) {
		SignalTest.assertSignalEquals(created, found);

	}

	@Override
	protected void assertEntityValuesNotEquals(SignalDef created,
			SignalDef found) {
		assertTrue(!created.getType().equals(found.getType()));

	}

	@Override
	protected SignalDef generateRandomEntity() {

		return SignalTest.getInstance().generateRandomIncompleteEntity();
	}

	@Override
	protected void mutate(SignalDef found) {
		found.setType(TestUtil.generateRandomString());

	}

}
