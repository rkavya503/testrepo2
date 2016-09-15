/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.rtp.RTPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rtp;

import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.timer.TimerManager;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * The Interface RTPProgramEJB.
 */

public interface RTPProgramEJB extends ProgramEJB{
    @Remote
    public interface R extends RTPProgramEJB {}
    @Local
    public interface L extends RTPProgramEJB {}
}