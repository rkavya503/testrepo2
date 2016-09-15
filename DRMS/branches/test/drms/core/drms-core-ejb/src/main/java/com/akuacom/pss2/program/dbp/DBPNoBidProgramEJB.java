/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.dbp.DBPProgramEJB.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import javax.ejb.Local;
import javax.ejb.Remote;

import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.program.sceftp.CreationFailureException;
import com.akuacom.pss2.program.sceftp.SCEFTPConfig;

/**
 * The Interface DBPProgramEJB.
 */

public interface DBPNoBidProgramEJB extends DBPProgramEJB {
    @Remote
    public interface R extends DBPNoBidProgramEJB {}
    @Local
    public interface L extends DBPNoBidProgramEJB {}
	
	int process(SCEFTPConfig config, boolean lastScan);

	/**
	 * parse the bid file
	 * @param filename
	 * @param fileString
	 * @return
	 * @throws ProgramValidationException
	 */
	public UtilityDREvent parseBidFile(String filename, String fileString) throws ProgramValidationException;

	void createEvent(String fileString, String filename) throws ProgramValidationException, CreationFailureException;

}
