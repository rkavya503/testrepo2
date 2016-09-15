/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.ProgramManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp2013;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.SCERTPProgramManager;

/**
 * Business object facade for working with programs.
 */

public interface SCERTPProgramManager2013 extends SCERTPProgramManager{
    @Remote
    public interface R extends SCERTPProgramManager2013 {}
    @Local
    public interface L extends SCERTPProgramManager2013 {}

    List<RTPConfig> findRtpCategoryByProgramName(String programName);
    
}