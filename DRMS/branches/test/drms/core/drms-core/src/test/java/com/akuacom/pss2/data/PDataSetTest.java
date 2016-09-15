package com.akuacom.pss2.data;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.test.TestUtil;

/**
 * PdataSetTest is the unit test class for the PdataSet Entity.
 * 
 * @author Brian Chapman
 * 
 *         Initial date 2010.11.01
 */

public class PDataSetTest extends BaseEntityFixture<PDataSet> {

	@Override
	public PDataSet generateRandomIncompleteEntity() {
		PDataSet dataSet = new PDataSet();

		String name = TestUtil.generateRandomStringOfLength(128);
		dataSet.setName(name);
		assertEquals(name, dataSet.getName());

		boolean sync = TestUtil.generateRandomBoolean();
		dataSet.setSync(sync);
		assertEquals(sync, dataSet.isSync());

		String unit = TestUtil.generateRandomStringOfLength(45);
		dataSet.setUnit(unit);
		assertEquals(unit, dataSet.getUnit());

		long period = TestUtil.generateRandomInt();
		dataSet.setPeriod(period);
		assertEquals(period, dataSet.getPeriod());

		return dataSet;
	}

}
