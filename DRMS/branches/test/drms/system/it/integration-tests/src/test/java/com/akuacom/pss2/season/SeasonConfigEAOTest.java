package com.akuacom.pss2.season;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.test.TestUtil;

public class SeasonConfigEAOTest extends
		AbstractVersionedEAOTest<SeasonConfigEAO, SeasonConfig> {

	public SeasonConfigEAOTest() {
		super(SeasonConfigEAOBean.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(SeasonConfig created,
			SeasonConfig found) {
		assertTrue(!created.getName().equals(found.getName()));

	}

	@Override
	protected void mutate(SeasonConfig found) {
		found.setName(TestUtil.generateRandomString());

	}

	@Override
	protected void assertEntityValuesEquals(SeasonConfig created,
			SeasonConfig found) {
		assertTrue(created.getName().equals(found.getName()));

	}

	@Override
	protected SeasonConfig generateRandomEntity() {
		SeasonConfig seasonConfig = new SeasonConfigTest()
				.generateRandomIncompleteEntity();

		assertNotNull(seasonConfig);
		return seasonConfig;
	}

}
