/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAO;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface RTPConfigEAO extends BaseEAO<RTPConfig> {
    @Remote
    public interface R extends RTPConfigEAO {}
    @Local
    public interface L extends RTPConfigEAO {}

    /**
     * Creates the rtp config.
     * 
     * @param value
     *            the value
     * 
     * @return the rTP config
     */
    RTPConfig createRTPConfig(RTPConfig value);

    /**
     * Find rtp configs.
     * 
     * @param program
     *            the program
     * 
     * @return the list< rtp config>
     */
    List<RTPConfig> findRTPConfigs(String programVersionUuid);

    /**
     * Delete rtp configs.
     * 
     * @param program
     *            the program
     */
    void deleteRTPConfigs(String program);

    List<RTPConfig> getRTPConfigList(String programVersionUuid,
            String seasonName, Calendar cal, double temperature)
            throws AppServiceException, NotConfiguredException;
    
    String getRTPConfigName(String programUUID, String seasonName, double temperature) throws AppServiceException;

}