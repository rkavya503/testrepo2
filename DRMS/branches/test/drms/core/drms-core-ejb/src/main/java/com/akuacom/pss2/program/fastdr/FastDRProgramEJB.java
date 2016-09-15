/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.cpp.CPPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.fastdr;

import com.akuacom.pss2.core.ProgramEJB;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * The Interface FastDRProgramEJB.
 */

public interface FastDRProgramEJB extends ProgramEJB {
    @Remote
    public interface R extends FastDRProgramEJB {  }
    @Local
    public interface L extends FastDRProgramEJB {  }
}