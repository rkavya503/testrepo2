/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.cpp.CPPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.cpp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramEJB;

/**
 * The Interface CPPProgramEJB.
 */

public interface CPPProgramEJB extends ProgramEJB {
    @Remote
    public interface R extends CPPProgramEJB {  }
    @Local
    public interface L extends CPPProgramEJB {  }
}