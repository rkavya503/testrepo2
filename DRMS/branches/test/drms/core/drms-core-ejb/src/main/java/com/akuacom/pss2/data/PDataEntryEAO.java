/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.usage.UsageSummary;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface PDataEntryEAO extends BaseEAO<PDataEntry> {
    @Remote
    public interface R extends PDataEntryEAO {}
    @Local
    public interface L extends PDataEntryEAO {}

    List<String> getDatasetNamesByOwnerID(String ownerID)
            throws EntityNotFoundException;

    List<java.util.Date> getDataDays(List<String> datasetUUIDs,
            String datasourceUUID, DateRange range)
            throws EntityNotFoundException;

    Date getLastestContact(String datasourceUUID, String datasetUUID)
            throws EntityNotFoundException;

    List<PDataEntry> getLatestDataEntry(String datasourceUUID,
            List<String> dataSets) throws EntityNotFoundException;
    List<PDataEntry> getLatestDataEntry(String datasourceUUID,
            String dataSetUUID) throws EntityNotFoundException;

    List<PDataEntry> getDataEntryForThreeTen(String datasourceUUID,
            int numberOfDays, Date endDate);

    List<PDataEntry> getDataEntryForByDataSourceUUIDAndDate(
            String datasourceUUID, Date date);

    List<PDataEntry> getDataEntryForByDataSourceUUIDAndDates(
            String datasourceUUID, Date start, Date end);

    List<PDataEntry> getDataEntryForByDataSourceUUID(String datasourceUUID);

    List<PDataEntry> getDataEntryForByDataSourceUUIDs(
            List<String> datasourceUUIDs);

    List<PDataEntry> getDataEntryForByDataSourceUUIDsAndDates(
            List<String> datasourceUUIDs, Date start, Date end);

    List<UsageSummary> getUsageReport(Map<String, String> dsUUIDs, Date start,
            Date end);

    UsageSummary getUsageSummary(List<String> dsUUIDs, Date start, Date end);

    PDataEntry getLatestDataEntry(String datasourceUUID, String dataSetUUID,
            Date time) throws EntityNotFoundException;
    
    void deleteDataEntryByDatasource(String datasourceUUID);

    List<UsageSummary> getUsageSumReport(Map<String, String> dsUUIDs, Date start, Date end);

    List<PDataEntry> getDataEntryList(String datasetUUID, String datasourceUUID, DateRange range)
    throws EntityNotFoundException ;

    Date getLastActualTime(List<String> dsUUIDs, DateRange range);
    
    public Date getLastActualTimeByDatasourceOwner(String ownerId);
	
	long deleteSyntheticDups();
	//PDataEntry.getLatestDataEntry
	PDataEntry getLatestDataEntry(List<String> datasourceUUID,
            Date start, Date end, Date currentTime);
	PDataEntry getNextDataEntry(String datasourceUUID,
            String dataSetUUID, Date time) throws EntityNotFoundException;
	Date getLastActualByDate(List<String> dsUUIDs, DateRange range);

	Map<String, Date> getLastValidDate(String dataset_uuid,
			List<PDataSource> dataSourceList, DateRange range);

	List<PDataEntry> getVirtualData(String dataset_uuid,
			String datasource_uuid, DateRange range);

	List<Date> getDataTime(String dataset_uuid, String datasource_uuid,
			DateRange range);
	
	int insertOrUpdate(PDataEntry entry);

	void removeByParticipantNameAndRange(List<String> participantNames,
			Date start, Date end);
	
	Date getPreviousTimePoint(String datasourceUUID, String datasetUUID, Date current)
            throws EntityNotFoundException;
	
	Date getNextTimePoint(String datasourceUUID, String datasetUUID, Date current)
            throws EntityNotFoundException;
}