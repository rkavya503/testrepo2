/*

 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.system.SystemManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import scala.Tuple2;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.irr.TreeDataSet;
import com.akuacom.pss2.event.Event;

/**
 * The Interface DataManager.
 */

public interface DataManager {
    @Remote
    public interface R extends DataManager {}
    @Local
    public interface L extends DataManager {}

    void createDataEntries(Set<PDataEntry> dataEntryList);

    PDataSource getDataSourceByNameAndOwner(String name, String owner);

    PDataSet getDataSetByName(String name);

    PDataSet createPDataSet(PDataSet dataset);

    void deletePDataSet(String uuid);

    PDataSet getPDataSet(String uuid) throws EntityNotFoundException;

    PDataSet updatePDataSet(PDataSet dataset);

    List<PDataSet> getDataSets();

    PDataSource createPDataSource(PDataSource dataSource);

    void deletePDataSource(String uuid);

    PDataSource getPDataSource(String uuid) throws EntityNotFoundException;

    PDataSource getPDataSourceByOwnerIdAndName(String ownerId, String name);

    PDataSource updatePDataSource(PDataSource dataSource);

    PDataEntry createPDataEntry(PDataEntry dataEntry);

    void deletePDataEntry(String uuid);

    PDataEntry getPDataEntry(String uuid) throws EntityNotFoundException;

    PDataEntry updatePDataEntry(PDataEntry dataEntry);
    
    List<Tuple2<String, Date>> getExisting(String datasetUUID,
            String datasourceUUID, Set<Date> times);

    List<PDataEntry> getDataEntryList(String datasetUUID,
            String datasourceUUID, DateRange range, boolean showRawData)
            throws EntityNotFoundException;

    List<String> getDatasetNamesByOwnerID(String ownerID)
            throws EntityNotFoundException;

    List<java.util.Date> getDataDays(List<String> datasetUUID,
            String datasourceUUID, DateRange range)
            throws EntityNotFoundException;

    List<PDataEntry> getLatestDataEntry(String datasourceUUID)
            throws EntityNotFoundException;

    java.util.Date getLastestContact(String datasourceUUID, String datasetUUID)
            throws EntityNotFoundException;

//    List<PDataEntry> getDataEntryList(String datasetUUID,
//            List<String> datasourceUUIDs, DateRange range, boolean showRawData)
//            throws EntityNotFoundException;

    List<PDataSource> getDataSourceByNameAndProgramName(String name,
            String programName);

    //List<PDataSource> getDataSourceByNameAndEventName(String name,
     //       String eventName);

    PDataEntry getLatestDataEntry(String datasourceUUID, String datasetUUID,
            Date time);
    void deleteDataEntryByDatasource(String datasourceUUID);
    
    void removeDuplicates(Collection<PDataEntry> dataEntries, PDataSet pDataSet, PDataSource pDataSource);
    
    List<String> getDataSourcesById(List<String> id);
    
    List<String> getDataSourceIdByNameAndOwner(String name, List<String> owners);
	
    List<TreeDataSet> getHierarchyDataSet(String participantName, DateRange dateRange,
			boolean includeProgramDataSources, boolean includeEventDataSources,
			boolean includeParticipantDataSources) throws AppServiceException;	
    List<TreeDataSet> getAllDataSet();
    PDataEntry getNextDataEntry(String datasourceUUID,
            String datasetUUID, Date time);
    List<PDataEntry> findByDataSourceDataSetAndDates(String sourceUUID, String setUUID, Date begin, Date end);
	List<PDataEntry> findByDataSourceDataSetAndTime(String sourceUUID, String setUUID, Date entryTime);

	void createGridDataEntries(Set<PDataEntry> dataEntryList);
	
	void createOrUpdateEventDataEntries(String eventName, String dataSetId);
	
	/** 
	 * this method should be get called whenever a historical usage point changes 
	 * (new point or point value changed)
	 * Historical means the point is not for today
	 * @param shouldUpdateBaseline TODO
	 **/
	void onUsageChange(String dataSourceId,Date date, boolean shouldUpdateBaseline);
	
	
	void onEventCreated(String eventName);
	
	
	void onEventCompleted(String eventName);

	void clearUsageData(List<String> participantNames, Date start, Date end);

	Event findFirstEventOfDay(String participantName, Date date);
}