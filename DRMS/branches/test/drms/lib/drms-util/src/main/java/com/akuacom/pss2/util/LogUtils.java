/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.LogUtils.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import com.honeywell.drms.logger.SuppressibleLogEntry;
import com.kanaeki.firelog.util.FireLogEntry;
import com.akuacom.utils.ACUUIDGenerator;

/**
 * The Class LogUtils.
 */
public class LogUtils
{
	
	/** The Constant CATAGORY_EVENT. */
	public static final String CATAGORY_EVENT  = "event";
    
    /** The Constant CATAGORY_WEBSERVICE. */
    public static final String CATAGORY_WEBSERVICE  = "webservice";
    
    /** The Constant CATAGORY_COMMS. */
    public static final String CATAGORY_COMMS  = "comms";
    
    /** The Constant CATAGORY_NOTIFICATION. */
    public static final String CATAGORY_NOTIFICATION  = "notification";
    
    /** The Constant CATAGORY_CONFIG_CHANGE. */
    public static final String CATAGORY_CONFIG_CHANGE  = "config change";

	/**
	 * Creates the log entry.
	 * 
	 * @param programName the program name
	 * @param category the category
	 * @param description the description
	 * @param longDescr the long descr
	 * 
	 * @return the fire log entry
	 */
	public static FireLogEntry createLogEntry(String programName, 
		String category, String description, String longDescr)
	{
		// first log the data
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setUserParam1(programName);
		logEntry.setCategory(category);
		logEntry.setDescription(description);
		logEntry.setLongDescr(longDescr);
		return logEntry;
	}

	/**
	 * Creates the log entry user.
	 * 
	 * @param programName the program name
	 * @param username the username
	 * @param category the category
	 * @param description the description
	 * @param longDescr the long descr
	 * 
	 * @return the fire log entry
	 */
	public static FireLogEntry createLogEntryUser(String programName, 
		String username, String category, String description, String longDescr)
	{
		// first log the data
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setUserParam1(programName);
		logEntry.setUserName(username);
		logEntry.setCategory(category);
		logEntry.setDescription(description);
		logEntry.setLongDescr(longDescr);
		return logEntry;
	}

    /**
     * Creates the exception log entry.
     * 
     * @param programName the program name
     * @param category the category
     * @param ex the ex
     * 
     * @return the fire log entry
     */
    public static FireLogEntry createExceptionLogEntry(String programName,
		String category, Exception ex)
	{
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setUserParam1(programName);
		logEntry.setCategory(category);
		logEntry.setDescription(ex.getMessage());
		StringBuilder sb = new StringBuilder();
		sb.append(ex.toString());
		sb.append("\n");
		for(StackTraceElement element: ex.getStackTrace())
		{
			sb.append(element.toString());
			sb.append("\n");
		}
		logEntry.setLongDescr(sb.toString());
		return logEntry;
	}
    
    public static SuppressibleLogEntry createSuppressibleLogEntry(String category,Exception ex,
    		String suppressId,int suppressDuration){
    	SuppressibleLogEntry log = new SuppressibleLogEntry(suppressId,suppressDuration);
		log.setDescription(ex.getMessage());
		log.setCategory(category);
		StringBuilder sb = new StringBuilder();
		sb.append(ex.toString());
		sb.append("\n");
		for(StackTraceElement element: ex.getStackTrace()){
			sb.append(element.toString());
			sb.append("\n");
		}
		log.setLongDescr(sb.toString());
		return log;
    }
    
    
	/**
	 * Creates the log entry.
	 * 
	 * @return the fire log entry
	 */
	public static FireLogEntry createLogEntry()
	{
		// first log the data
		FireLogEntry logEntry = new FireLogEntry();
        String uuid = ACUUIDGenerator.generateUUID(logEntry);
        logEntry.setUserParam2(uuid);
		return logEntry;
	}

    /**
     * Gets the transaction id from log entry.
     * 
     * @param entry the entry
     * 
     * @return the transaction id from log entry
     */
    public static String getTransactionIDFromLogEntry(FireLogEntry entry)
	{
        if(entry == null) return null;
        return entry.getUserParam2();
	}
}
