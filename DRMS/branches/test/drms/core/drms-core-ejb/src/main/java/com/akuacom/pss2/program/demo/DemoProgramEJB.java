/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.demo.DemoProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.demo;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramEJB;

/**
 * The Interface DemoProgramEJB.
 */

public interface DemoProgramEJB extends ProgramEJB {
    @Remote
    public interface R extends DemoProgramEJB {}
    @Local
    public interface L extends DemoProgramEJB {}

}