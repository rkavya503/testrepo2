/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.scertp.SCERTPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp2013;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.program.scertp.SCERTPProgramEJB;

/**
 * The Interface SCERTPProgramEJB.
 */

public interface SCERTPProgramEJB2013 extends SCERTPProgramEJB {
    @Remote
    public interface R extends SCERTPProgramEJB2013 {}

    @Local
    public interface L extends SCERTPProgramEJB2013 {}

    final int SCERTP_ISSUE_HOUR = 17;

    String getRTPConfigName(String programName, String seasonName, double temperature) throws AppServiceException;
    
    

}