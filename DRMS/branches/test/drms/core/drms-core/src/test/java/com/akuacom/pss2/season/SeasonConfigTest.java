package com.akuacom.pss2.season;

import static com.akuacom.test.TestUtil.generateRandomString;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * SeasonConfigTest is the unit test class for the SeasonConfig Entity.
 * 
 * @author Brian Chapman
 * 
 *         Created on 2010.11.8
 */
public class SeasonConfigTest extends BaseEntityFixture<SeasonConfig> {

	@Override
	public SeasonConfig generateRandomIncompleteEntity() {
		SeasonConfig sc = new SeasonConfig();

		String name = generateRandomString();
		sc.setName(name);
		assertEquals(name, sc.getName());

		return sc;
	}

}
