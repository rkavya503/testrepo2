package com.akuacom.pss2.data.usage;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.utils.DateUtil;

@Ignore
public class DataManagerTest {
	protected static DataManager dataManager;
	//String datasourceUUID = "1fe3f97d36383c0b01363845011c0013";//P1
	String datasourceUUID = "8aceb869364e5c9a01364e6580060004";//P2
	@Before
	public void setup() {
		dataManager = JBossFixture.lookupSessionRemote(DataManager.class);
	}
	
	@Test
	public void testgetLastestContact() throws EntityNotFoundException{
		
		String datasetUUID = "d65cb1c206de11e09bc70382820593d2";
		java.util.Date last =  dataManager.getLastestContact(datasourceUUID, datasetUUID);
		System.out.println(last);
	}
	//getLatestDataEntry
	@Test
	public void testgetLatestDataEntry() throws EntityNotFoundException{
		String datasetUUID = "d65cb1c206de11e09bc70382820593d2";
		List<PDataEntry> last =  dataManager.getLatestDataEntry(datasourceUUID);
		for(PDataEntry entry:last){
			System.out.println(entry.getTime() +"---"+ entry.getValue());
		}
	}
	
	@Test
	public void testfindByDataSourceDataSetAndDates() throws EntityNotFoundException{
		String datasetUUID = "d65cb1c206de11e09bc70382820593d2";
		Date begin = DateUtil.parse("2012-03-25 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date end = new Date();
		List<PDataEntry> last =  dataManager.findByDataSourceDataSetAndDates(datasourceUUID,datasetUUID,begin,end);
		for(PDataEntry entry:last){
			System.out.println(entry.getTime() +"---"+ entry.getValue());
		}
	}
	@Test
	public void testgetPreDataEntry() throws EntityNotFoundException{
		Date begin = DateUtil.parse("2012-03-26 00:00:00", "yyyy-MM-dd HH:mm:ss");
		String datasetUUID = "d65cb1c206de11e09bc70382820593d2";
		PDataEntry last =  dataManager.getLatestDataEntry(datasourceUUID,datasetUUID,begin);
		if(last!=null){
			System.out.println("last "+last.getTime() +"---"+ last.getValue());
		}
	}
	@Test
	public void testgetNextDataEntry() throws EntityNotFoundException{
		Date begin = DateUtil.parse("2012-03-26 00:00:00", "yyyy-MM-dd HH:mm:ss");
		String datasetUUID = "d65cb1c206de11e09bc70382820593d2";
		PDataEntry last =  dataManager.getNextDataEntry(datasourceUUID,datasetUUID,begin);
		if(last!=null){
			System.out.println("next "+last.getTime() +"---"+ last.getValue());
		}
	}
	
}
