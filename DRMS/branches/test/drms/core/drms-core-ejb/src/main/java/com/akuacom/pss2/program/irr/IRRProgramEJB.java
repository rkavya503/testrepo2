/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.cpp.CPPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.irr;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ValidationException;
import java.util.List;

/**
 * The Interface CPPProgramEJB.
 */

public interface IRRProgramEJB extends ProgramEJB {
    @Remote
    public interface R extends IRRProgramEJB {  }
    @Local
    public interface L extends IRRProgramEJB {  }
    
    public void updateSetpoint(String programName, List<ParticipantSetpoint> participants)
            throws ValidationException;
}