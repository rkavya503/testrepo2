/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.report.ReportManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.program.DRwebsite.PREvent;
import com.akuacom.pss2.program.dbp.DBPEventCreation;
import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.report.entities.Account;
import com.akuacom.pss2.report.entities.ClientInfo;
import com.akuacom.pss2.report.entities.ClientShedStrategy;
import com.akuacom.pss2.report.entities.OfflineRecord;

/**
 * The Interface ReportManager.
 */

public interface ReportManager {
    @Remote
    public interface R extends ReportManager {}

    @Local
    public interface L extends ReportManager {}

    /**
     * Gets the accounts.
     * 
     * @return the accounts
     */
    List<Account> getAccounts();
    
    List<ClientInfo> getClientInfoResults();
    List<ClientShedStrategy> getClientShedStrategysForCPP();
    List<ClientShedStrategy> getClientShedStrategysForDBP();
   public List<OfflineRecord> getObixCommReport();

    /**
     * Gets the weather records.
     * 
     * @param start
     *            the start
     * @param end
     *            the end
     * 
     * @return the weather records
     */
    List<Weather> getWeatherRecords(Date start, Date end);
    
    List<DBPEventCreation> getDBPEventCreation(Date start, Date end);

	List<PREvent> getEventListByProgramClassWithHistoryEvent(
			String programClass, Date start, Date end)
			throws AppServiceException;

	/**
	 * @param programName
	 * @return
	 */
	List<ClientShedStrategy> getClientShedStrategysForCPP(String programName);

	/**
	 * @param programName
	 * @return
	 */
	List<ClientShedStrategy> getClientShedStrategysForDBP(String programName);

}
