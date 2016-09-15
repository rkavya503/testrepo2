package com.akuacom.pss2.core;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DataManagerBean;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;

public class DataManagerTest extends ParticipantManagerFixture {
	private static final String TIMESTAMP3 = "2010-01-01 23:45:00";
	private static final String TIMESTAMP2 = "2010-01-01 00:15:00";
	private static final String TIMESTAMP1 = "2010-01-01 00:00:00";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";	

	private static final String NAME = "meter1";
	private static final String NAME2 = "Usage";
	private static final String NAME3 = "maximum demand";
	
	//!!!For datasource table, we assume that NAME + OWNER_ID is the logical primary key
	private static final String OWNER_ID = "test";
	
	private String dataSourceUUID;
	private String dataSetUUID;
	private String dataEntryUUID1;
	private String dataEntryUUID2;
	private String dataEntryUUID3;
	
	@Before
	public void setUp() throws Exception{
		dataSourceUUID= "";
		dataSetUUID ="";
		dataEntryUUID1 = "";
		dataEntryUUID2 = "";
		dataEntryUUID3 = "";
	}

    @Ignore("todo: need to avoid cascade and eager loading here. will add this back after that is fixed")
    @Test
	public void testObixDataInsert() throws ParseException,
			EntityNotFoundException {

		// The following lines are only for unit test.
		// This should be created when creating participant called test with
		// data enabled.
		PDataSource pds = new PDataSource();
		pds.setName(NAME);
		pds.setOwnerID(OWNER_ID);
		PDataSource created = dm.createPDataSource(pds);
		assertNotNull(created);
		// End block of unit test only ..............

		PDataSource ds = dm.getDataSourceByNameAndOwner(NAME, OWNER_ID);
		assertNotNull(ds);
		assertEquals(created, ds);
		
		dataSourceUUID = ds.getUUID();
		
		//Dont' delete this dataset as this dataset is created by script
		PDataSet dataSet = dm.getDataSetByName(NAME2);
		assertNotNull("can't find by name " + NAME2, dataSet);		
		
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		PDataEntry de1 = new PDataEntry();
		de1.setDatasource(ds);
		de1.setValue(2354.1);
		Date time = sdf.parse(TIMESTAMP1);
		de1.setTime(time);
		de1.setDataSet(dataSet);

		PDataEntry de2 = new PDataEntry();
		de2.setDatasource(ds);
		de2.setValue(2125.7);
		Date time2 = sdf.parse(TIMESTAMP2);
		de2.setTime(time2);
		de2.setDataSet(dataSet);

		PDataEntry de3 = new PDataEntry();
		de3.setDatasource(ds);
		de3.setValue(1955.2);
		Date time3 = sdf.parse(TIMESTAMP3);
		de3.setTime(time3);
		de3.setDataSet(dataSet);

		Set dataEntryList = new HashSet();
		dataEntryList.add(de1);
		dataEntryList.add(de2);
		dataEntryList.add(de3);

		dm.createDataEntries(dataEntryList);

		DateRange daterange = new DateRange();
		daterange.setStartTime(time);
		daterange.setEndTime(time3);

		List<PDataEntry> pdeList = dm.getDataEntryList(dataSet.getUUID(), ds
				.getUUID(), daterange,false);
		assertNotNull(pdeList);
		assertEquals(3, pdeList.size());
		
		if (pdeList.size()==3){
			dataEntryUUID1 = pdeList.get(0).getUUID();
			dataEntryUUID2 = pdeList.get(1).getUUID();
			dataEntryUUID3 = pdeList.get(2).getUUID();
		}
	}	

    @Ignore("todo: need to avoid cascade and eager loading here. will add this back after that is fixed")
    @Test
	public void testInsertAndGetDataSource() {
		PDataSource pds = new PDataSource();
		pds.setName(NAME);
		pds.setOwnerID(OWNER_ID);
		assertNotNull(dm.createPDataSource(pds));		
		PDataSource ds = dm.getDataSourceByNameAndOwner(NAME, OWNER_ID);
		assertNotNull(ds);
		dataSourceUUID = ds.getUUID();
	}

	@Test
	public void testInsertAndGetDataSet() {		
		PDataSet pds = new PDataSet();
		pds.setName(NAME3);
		pds.setUnit("KWh");
		pds.setSync(true);
		pds.setPeriod(60);

		assertNotNull(dm.createPDataSet(pds));
		
		PDataSet ds = dm.getDataSetByName(NAME3);
		assertNotNull(ds);
		
		dataSetUUID = ds.getUUID();
	}
	
	@After
	public void teardDown() throws Exception{
		if (dataEntryUUID1.length()>0)
			dm.deletePDataEntry(dataEntryUUID1);
		if (dataEntryUUID2.length()>0)
			dm.deletePDataEntry(dataEntryUUID2);
		if (dataEntryUUID3.length()>0)
			dm.deletePDataEntry(dataEntryUUID3);
		
		if (dataSetUUID.length()>0)
			dm.deletePDataSet(dataSetUUID);
		
		if (dataSourceUUID.length()>0)
			dm.deletePDataSource(dataSourceUUID);
	}
	

}
