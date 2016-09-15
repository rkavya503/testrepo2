package com.akuacom.pss2.data;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.test.TestUtil;

/**
 * PDataEntryTest is the unit test class for the PDataEntry Entity.
 * 
 * @author Brian Chapman
 * 
 *         Initial date 2010.11.01
 */

public class PDataEntryTest extends BaseEntityFixture<PDataEntry> {

	private PDataSource dataSource;
	private PDataSet dataSet;
	
	@Override
	public PDataEntry generateRandomIncompleteEntity() {
		PDataEntry dataEntry = new PDataEntry();

		Date time = TestUtil.generateRandomDate();
		dataEntry.setTime(time);
		assertEquals(time, dataEntry.getTime());

		double value = TestUtil.generateRandomDouble();
		dataEntry.setValue(value);
		assertEquals(value, dataEntry.getValue(), 0.01);

		String stringValue = TestUtil.generateRandomString();
		dataEntry.setStringValue(stringValue);
		assertEquals(stringValue, dataEntry.getStringValue());

		String valueType = TestUtil.generateRandomString();
		dataEntry.setValueType(valueType);
		assertEquals(valueType, dataEntry.getValueType());
		
		dataSet = new PDataSetTest().generateRandomIncompleteEntity();
		dataEntry.setDataSet(dataSet);

		dataSource = new PDataSourceTest().generateRandomIncompleteEntity();
		dataEntry.setDatasource(dataSource);



		return dataEntry;
	}

}
