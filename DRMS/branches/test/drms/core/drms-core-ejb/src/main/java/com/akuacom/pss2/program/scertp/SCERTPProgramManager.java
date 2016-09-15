/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.ProgramManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;

/**
 * Business object facade for working with programs.
 */

public interface SCERTPProgramManager {
    @Remote
    public interface R extends SCERTPProgramManager {}
    @Local
    public interface L extends SCERTPProgramManager {}

    /**
     * Save rtp config.
     * 
     * @param value
     *            the value
     * @param programName
     *            the program name
     */
    void saveRTPConfig(List<RTPConfig> value, String programName);

    List<RTPConfig> findRTPConfigs(String programName);
    
    public List<RTPConfig> findRTPConfigsByProgramId(String programUUID);

    double getRate(String programName, Date time);

    String getRTPConfigName(String programName, String seasonName, double temperature) throws AppServiceException;
}