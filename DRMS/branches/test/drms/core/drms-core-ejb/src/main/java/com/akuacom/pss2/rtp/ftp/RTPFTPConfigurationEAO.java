/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ContactServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.rtp.ftp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;


public interface RTPFTPConfigurationEAO extends BaseEAO<RTPFTPConfiguration>
{
    @Remote
    public interface R extends RTPFTPConfigurationEAO {}
    @Local
    public interface L extends RTPFTPConfigurationEAO {}

	void saveRTPFTPConfiguration(RTPFTPConfiguration rtpFTPConfiguration) ;

	RTPFTPConfiguration getRTPFTPConfiguration() ;

}