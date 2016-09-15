package com.akuacom.pss2.data;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.test.TestUtil;

/**
 * PDataSourceTest is the unit test class for the PDataSource Entity.
 * 
 * @author Brian Chapman
 * 
 *         Initial date 2010.11.01
 */

public class PDataSourceTest extends BaseEntityFixture<PDataSource> {

	@Override
	public PDataSource generateRandomIncompleteEntity() {
		PDataSource dataSource = new PDataSource();

		String ownerID = TestUtil.generateRandomString();
		dataSource.setOwnerID(ownerID);
		assertEquals(ownerID, dataSource.getOwnerID());

		String name = TestUtil.generateRandomString();
		dataSource.setName(name);
		assertEquals(name, dataSource.getName());

		return dataSource;
	}

}
