package com.akuacom.pss2.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.akuacom.ejb.AbstractBaseEAOTest;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.test.TestUtil;

/**
 * PDataEntryEaoTest is the unit test class for the PDataEntryEao Object.
 * 
 * @author Brian Chapman
 * 
 *         Initial date 2010.11.01
 */

// DRMS-2026 Will enable after DRMS-2127 is moved. Only doing this to avoid merging manually through SVN
public class PDataEntryEAOTest { 
	
	
	@Test
	public void dummyTest() {}
	/*extends
		AbstractBaseEAOTest<PDataEntryEAO, PDataEntry> {

	public PDataEntryEAOTest() {
		super(PDataEntryEAOBean.class, PDataEntry.class);
	}
	
	@Override
	protected void assertEntityValuesNotEquals(PDataEntry created,
			PDataEntry found) {
		assertTrue(found.getStringValue() != created.getStringValue());
	}

	@Override
	protected void mutate(PDataEntry found) {
		found.setStringValue(TestUtil.generateRandomString());
	}

	@Override
	protected void assertEntityValuesEquals(PDataEntry created, PDataEntry found) {
		assertEquals(created.getStringValue(), found.getStringValue());
	}

	@Override
	protected PDataEntry generateRandomEntity() {
		PDataEntryTest pDataEntryTest = new PDataEntryTest();
		PDataEntry dataEntry = pDataEntryTest.generateRandomIncompleteEntity();

		PDataSet dataSet = null;
		try {
			dataSet = new PDataSetEAOTest().generateRandomPersistedEntity();
		} catch (DuplicateKeyException e) {
			fail("Should not have a duplicate key here");
			e.printStackTrace();
		}
		assertNotNull(dataSet);
		dataEntry.setDataSet(dataSet);

		PDataSource dataSource = null;
		try {
			dataSource = new PDataSourceEAOTest()
					.generateRandomPersistedEntity();
		} catch (DuplicateKeyException e) {
			fail("Should not have a duplicate key here");
			e.printStackTrace();
		}
		assertNotNull(dataSource);
		dataEntry.setDatasource(dataSource);

		assertNotNull(dataEntry);
		return dataEntry;
	}

	@Override
	public void delete(PDataEntry entity)
			throws EntityNotFoundException {
		//Ensures that this will throw the appropriate expection if the entity is not persisted
		entity = eao.getById(entity.getUUID());
		
		PDataSourceEAOTest dataSourceTest = PDataSourceEAOTest.getInstance();
		PDataSource dataSource = entity.getDatasource();
		dataSourceTest.delete(dataSource);

		PDataSetEAOTest dataSetTest = PDataSetEAOTest.getInstance();
		PDataSet dataSet = entity.getDataSet();
		dataSetTest.delete(dataSet);
	} */
}
