/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data.usage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface CurrentUsageDataEntryEAO extends
        BaseEAO<CurrentUsageDataEntry> {
    @Remote
    public interface R extends CurrentUsageDataEntryEAO {}
    @Local
    public interface L extends CurrentUsageDataEntryEAO {}

    List<String> getDatasetNamesByOwnerID(String ownerID)
            throws EntityNotFoundException;

    List<Date> getDataDays(List<String> datasetUUIDs, String datasourceUUID,
            DateRange range) throws EntityNotFoundException;

    Date getLastestContact(String datasourceUUID, String datasetUUID)
            throws EntityNotFoundException;

    List<CurrentUsageDataEntry> getLatestDataEntry(String datasourceUUID,
            List<String> dataSets) throws EntityNotFoundException;

    List<CurrentUsageDataEntry> getDataEntryForThreeTen(String datasourceUUID,
            int numberOfDays, Date endDate);

    List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUIDAndDate(
            String datasourceUUID, Date date);

    List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUIDAndDates(
            String datasourceUUID, Date start, Date end);

    List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUID(
            String datasourceUUID);

    List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUIDs(
            List<String> datasourceUUIDs);

    List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUIDsAndDates(
            List<String> datasourceUUIDs, Date start, Date end);

    List<UsageSummary> getUsageReport(Map<String, String> dsUUIDs, Date start,
            Date end);

    UsageSummary getUsageSummary(List<String> dsUUIDs, Date start, Date end);

    Date getMaxTime();

    void pushFromCurrentToHistory();
    
    int insertOrUpdate(PDataEntry entry);

	Date getLastActualTimeByDatasourceOwner(String ownerId);

}