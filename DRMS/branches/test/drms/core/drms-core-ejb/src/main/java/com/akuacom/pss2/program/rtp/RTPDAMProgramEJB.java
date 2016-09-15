/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.rtp.RTPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rtp;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * The Interface RTPProgramEJB.
 */

public interface RTPDAMProgramEJB extends RTPProgramEJB{
    @Remote
    public interface R extends RTPDAMProgramEJB {}
    @Local
    public interface L extends RTPDAMProgramEJB {}
}