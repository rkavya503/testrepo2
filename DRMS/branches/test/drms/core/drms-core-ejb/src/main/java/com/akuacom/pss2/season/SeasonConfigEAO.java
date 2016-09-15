/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.season;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAO;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface SeasonConfigEAO extends BaseEAO<SeasonConfig> {
    @Remote
    public interface R extends SeasonConfigEAO {}

    @Local
    public interface L extends SeasonConfigEAO {}

    /**
     * Find season configs.
     * 
     * @param program
     *            the program
     * 
     * @return the list< season config>
     */
    List<SeasonConfig> findSeasonConfigs(String programVersionUuid);

    /**
     * Creates the season config.
     * 
     * @param value
     *            the value
     * 
     * @return the season config
     */
    SeasonConfig createSeasonConfig(SeasonConfig value);

    /**
     * Update season config.
     * 
     * @param value
     *            the value
     * 
     * @return the season config
     */
    SeasonConfig updateSeasonConfig(SeasonConfig value);

    /**
     * Gets the season config.
     * 
     * @param programName
     *            the program name
     * @param date
     *            the date
     * 
     * @return the season config
     * 
     * @throws AppServiceException
     *             the app service exception
     */
    List<SeasonConfig> getSeasonConfig(String programVersionUuid, Date date)
            throws AppServiceException;

    SeasonConfig get(String uuid) throws AppServiceException;
    

    /**
     * Find season configs.
     * 
     * @param program
     *            the program
     * 
     * @return the list< season config>
     */
    Set<Date> findHolidays();

}