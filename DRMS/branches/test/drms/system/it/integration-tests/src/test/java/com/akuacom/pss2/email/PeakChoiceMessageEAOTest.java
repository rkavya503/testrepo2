package com.akuacom.pss2.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.akuacom.ejb.AbstractBaseEAOTest;
import com.akuacom.test.TestUtil;

/**
 * PeakChoiceMessageEAOTest is a test for the PeakChoiceMessageEAO.
 * 
 * @author Brian Chapman
 * 
 *         Initial date 2010.11.01
 */
public class PeakChoiceMessageEAOTest extends
		AbstractBaseEAOTest<PeakChoiceMessageEAO, PeakChoiceMessageEntity> {

	public PeakChoiceMessageEAOTest() {
		super(PeakChoiceMessageEAO.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(PeakChoiceMessageEntity created,
			PeakChoiceMessageEntity found) {
		assertTrue(found.getContent() != created.getContent());
	}

	@Override
	protected void mutate(PeakChoiceMessageEntity found) {
		found.setContent(TestUtil.generateRandomString());
	}

	@Override
	protected void assertEntityValuesEquals(PeakChoiceMessageEntity created,
			PeakChoiceMessageEntity found) {
		assertEquals(found.getContent(), created.getContent());
	}

	@Override
	protected PeakChoiceMessageEntity generateRandomEntity() {
		PeakChoiceMessageEntity message = new PeakChoiceMessageTest()
				.generateRandomIncompleteEntity();

		assertNotNull(message);
		return message;
	}

}
