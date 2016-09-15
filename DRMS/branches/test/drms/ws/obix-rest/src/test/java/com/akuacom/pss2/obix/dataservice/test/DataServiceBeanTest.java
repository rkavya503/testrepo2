/**
 * 
 */
package com.akuacom.pss2.obix.dataservice.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import obix.Abstime;
import obix.Obj;
import obix.Status;
import obix.io.ObixDecoder;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.obix.dataservice.DataServiceBean;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.utils.lang.CacheUtil;

/**
 *
 */
public class DataServiceBeanTest {
    
	private static final String DATASOURCE_UUID = "datasource_uuid";
	private static final String DATASET_UUID = "dataset_uuid";
	private static final String OBIX_TEST = "obix.test";
	private static final String OBIX_METER1 = "obix.meter1";
	private static final String OBIX = "obix";
	private static final String METER1 = "meter1";
	private static final String USAGE = "usage";
	MockDataManager mockDataManager = new MockDataManager();
	ParticipantManager.L participantManager;

	DataServiceBean service = new DataServiceBean();
		
	Logger log = Logger.getLogger(DataServiceBeanTest.class);
	{	    log.info("pre-constructor");
	}
	@Test
	public void getDataEntrySetListTest(){
	    log.info("getDataEntrySetListTest");
		String xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			+"<list name=\"meter1\">"
			+"<obj name=\"point\">"
			+"<real name=\"value\" val=\"3.0\" unit=\"obix:kwh\"/>"
			+"<abstime name=\"time\" val=\"2010-06-30T09:08:38.335Z\" tz=\"Asia/Beijing\"/>"
			+"</obj>"
			+"</list>"
			+"</obj>";	
		
		PDataSet pDataSet=new PDataSet();
		pDataSet.setName("test dataset");
		pDataSet.setUUID(DATASET_UUID);
		
		PDataSource pDataSource =new PDataSource();
		pDataSource.setUUID(DATASOURCE_UUID);
		pDataSource.setName("tester");
		pDataSource.setOwnerID("test owner");
		pDataSource.setEnabled(true);
		mockDataManager.createPDataSet(pDataSet);
		mockDataManager.createPDataSource(pDataSource);
		service.setDataManager(mockDataManager);
		Obj obj = ObixDecoder.fromString(xml);
		java.util.List<Set<PDataEntry>> pDataEntryList=
			service.getDataEntrySetList(obj, pDataSet, pDataSource);
		
		assertEquals(1, pDataEntryList.size());
		assertEquals(1, pDataEntryList.get(0).size());
		
        for (PDataEntry pDataEntry : pDataEntryList.get(0)) {
        	assertEquals(pDataSet, pDataEntry.getDataSet());
        	assertEquals(pDataSource, pDataEntry.getDatasource());
        }
        
        
        xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			  +"<list name=\"meter1\">"
			    +"<obj name=\"point0\">"
			      +"<real name=\"value\" val=\"1.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:09:45.459Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			    +"<obj name=\"point1\">"
			      +"<real name=\"value\" val=\"2.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:09:58.403Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			    +"<obj name=\"point2\">"
			      +"<real name=\"value\" val=\"3.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:10:08.626Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			  +"</list>"
			+"</obj>";
        obj = ObixDecoder.fromString(xml);
		pDataEntryList = service.getDataEntrySetList(obj, pDataSet, pDataSource);
		
		assertEquals(1, pDataEntryList.size());
		assertEquals(3, pDataEntryList.get(0).size());
		
        for (PDataEntry pDataEntry : pDataEntryList.get(0)) {
        	assertEquals(pDataSet, pDataEntry.getDataSet());
        	assertEquals(pDataSource, pDataEntry.getDatasource());
        }
        
        
	}
	
	protected void prepareDataSource(String usage, String meter, String ownerId){
		
        log.info("prepareDataSource usage / meter / ownerId " + usage + " / " + meter + " / " + ownerId);
		PDataSource ds=new PDataSource();
		ds.setName(meter);
		ds.setOwnerID(ownerId);
		ds.setUUID(DATASOURCE_UUID);
		ds.setEnabled(true);
		mockDataManager.createPDataSource(ds);

		PDataSet set=new PDataSet();
		set.setName(usage);
		set.setUnit("kwh");
		set.setUUID(DATASET_UUID);
		mockDataManager.createPDataSet(set);

		service.setDataManager(mockDataManager);
	}

	protected void clearDataSource(){
		if (mockDataManager.getDataEntry() !=null)
			mockDataManager.getDataEntry().clear();
		
		if (mockDataManager.getDataSet() !=null)
			mockDataManager.getDataSet().clear();
		
		if (mockDataManager.getDataSource() !=null)
			mockDataManager.getDataSource().clear();
		
	}

	@Test
	public void persistTest() throws Exception {
        log.info("persistTest");
		
		String usage=USAGE;
		String ownerID="Linda";
		
		prepareDataSource(usage, METER1, ownerID);
		
		String xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			+"<list name=\"meter1\">"
			+"<obj name=\"point\">"
			+"<real name=\"value\" val=\"3.0\" unit=\"obix:kwh\"/>"
			+"<abstime name=\"time\" val=\"2010-06-30T09:08:38.335Z\" tz=\"Asia/Beijing\"/>"
			+"</obj>"
			+"</list>"
			+"</obj>";	
		boolean result=service.persist(ownerID, xml);
		
		assertEquals(true, result);
		assertEquals(1, mockDataManager.getDataEntry().size());
		
        xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			  +"<list name=\"meter1\">"
			    +"<obj name=\"point0\">"
			      +"<real name=\"value\" val=\"1.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:09:45.459Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			    +"<obj name=\"point1\">"
			      +"<real name=\"value\" val=\"2.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:09:58.403Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			    +"<obj name=\"point2\">"
			      +"<real name=\"value\" val=\"3.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:10:08.626Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			  +"</list>"
			+"</obj>";
        
		clearDataSource();		
		prepareDataSource(usage, METER1, ownerID);
		result=service.persist(ownerID, xml);
		assertEquals(true, result);
		assertEquals(3, mockDataManager.getDataEntry().size());
               
		//false
		clearDataSource();		
		CacheUtil.flush(mockDataManager, "getPDataSourceByOwnerIdAndName");
		prepareDataSource(usage, METER1, OBIX);
		result=service.persist(ownerID, xml);
		assertEquals(false, result);
		assertEquals(0, mockDataManager.getDataEntry().size());
		
		//false
		usage="usage1";
		
		clearDataSource();		
		prepareDataSource(usage, METER1, ownerID);
		result=service.persist(ownerID, xml);
		assertEquals(false, result);
		assertEquals(0, mockDataManager.getDataEntry().size());		
	}
	
	protected void prepareParticipant(String clientId, boolean isClient, String parent){
		Participant part=new Participant();
		part.setParticipantName(parent);
		part.setClient(false);
		part.setUUID("participant_uuid");
		
		part=new Participant();
		part.setParticipantName(clientId);
		part.setClient(isClient);
		part.setParent(parent);
		part.setUUID("client_uuid");
		
//		mockParticipantManager.getAllParticipants().add(part);
		
		participantManager=new ParticipantManagerBean(){
			List<Participant> partList=new ArrayList<Participant>();
			
			@Override
		    public List<Participant> getAllParticipants() {
		        return partList;
		    }

		};
		
		participantManager.getAllParticipants().add(part);
		
		service.setParticipantManager(participantManager);
	}
	
	protected void clearParticipant(){
		if (participantManager.getAllParticipants()!=null)
			participantManager.getAllParticipants().clear();
	}
	
	@Test
	@Ignore
	public void serviceTest() throws Exception {
		String clientId=OBIX_METER1;
		String parent=OBIX;
		prepareParticipant(clientId, true, parent);
		prepareDataSource(USAGE, METER1, parent);
		
		String xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			+"<list name=\"meter1\">"
			+"<obj name=\"point\">"
			+"<real name=\"value\" val=\"3.0\" unit=\"obix:kwh\"/>"
			+"<abstime name=\"time\" val=\"2010-06-30T09:08:38.335Z\" tz=\"Asia/Beijing\"/>"
			+"</obj>"
			+"</list>"
			+"</obj>";			
		String response=service.service(clientId, xml);
		log.info("1st response " + response);
        assertEquals( Status.ok, ObixDecoder.fromString(response).getStatus());
        
        xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			  +"<list name=\"meter1\">"
			    +"<obj name=\"point0\">"
			      +"<real name=\"value\" val=\"1.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:09:45.459Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			    +"<obj name=\"point1\">"
			      +"<real name=\"value\" val=\"2.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:09:58.403Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			    +"<obj name=\"point2\">"
			      +"<real name=\"value\" val=\"3.0\"/>"
			      +"<abstime name=\"time\" val=\"2010-07-07T09:10:08.626Z\" tz=\"Asia/Beijing\"/>"
			    +"</obj>"
			  +"</list>"
			+"</obj>";
		response=service.service(clientId, xml);		
        log.info("2nd response " + response);
        assertEquals(ObixDecoder.fromString(response).getStatus(), Status.ok);
        
        //Disable data source first
        PDataSource ds = mockDataManager.getDataSourceByNameAndOwner(METER1,parent);
        ds.setEnabled(false);
        log.info("expecting fault");
    	response=service.service(clientId, xml);
    	log.info("response " + response);
        assertEquals(ObixDecoder.fromString(response).getStatus(), Status.fault);
        
        //Now clear all data test again
		clearParticipant();
		clearDataSource();
		prepareParticipant(clientId, true, parent);
		prepareDataSource(USAGE, METER1, OBIX_TEST);
		
		response=service.service(clientId, xml);
        assertEquals(ObixDecoder.fromString(response).getStatus(), Status.fault);
	}
	
	@Test
	@Ignore
	public void repeatDataTest() throws Exception {
		String clientId=OBIX_METER1;
		String parent=OBIX;
		prepareParticipant(clientId, true, parent);
		prepareDataSource(USAGE, METER1, parent);
		CacheUtil.flush(mockDataManager, "getPDataSourceByOwnerIdAndName");
		CacheUtil.flush(mockDataManager, "getDataSetByName");
        
		String xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			+"<list name=\"meter1\">"
			+"<obj name=\"point\">"
			+"<real name=\"value\" val=\"3.0\" unit=\"obix:kwh\"/>"
			+"<abstime name=\"time\" val=\"2010-06-30T09:08:38.335Z\" tz=\"Asia/Beijing\"/>"
			+"</obj>"
			+"</list>"
			+"</obj>";			
		String response=service.service(clientId, xml);
		//System.exit(0);
        assertEquals(ObixDecoder.fromString(response).getStatus(), Status.ok);
        assertEquals(1, mockDataManager.getDataEntry().size());
        
        PDataEntry entry=mockDataManager.getDataEntry().get(0);
        java.util.Date time=entry.getTime();
        
        try {
        	assertEquals(time, new java.util.Date(Abstime.parse("2010-06-30T09:08:38.335Z").get()));
        
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        //object with same time
		xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			+"<list name=\"meter1\">"
			+"<obj name=\"point\">"
			+"<real name=\"value\" val=\"4.0\" unit=\"obix:kwh\"/>"
			+"<abstime name=\"time\" val=\"2010-06-30T09:08:38.335Z\" tz=\"Asia/Beijing\"/>"
			+"</obj>"
			+"</list>"
			+"</obj>";			
		response=service.service(clientId, xml);
        assertEquals(ObixDecoder.fromString(response).getStatus(), Status.ok);
        assertEquals(1, mockDataManager.getDataEntry().size());

		xml="<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\">"
			+"<list name=\"meter1\">"
			+"<obj name=\"point\">"
			+"<real name=\"value\" val=\"5.0\" unit=\"obix:kwh\"/>"
			+"<abstime name=\"time\" val=\"2010-06-30T09:08:38.335Z\" tz=\"Asia/Beijing\"/>"
			+"</obj>"
			+"</list>"
			+"</obj>";			
		response=service.service(clientId, xml);
        assertEquals(ObixDecoder.fromString(response).getStatus(), Status.ok);
        assertEquals(1, mockDataManager.getDataEntry().size());
        
	}
	
}
