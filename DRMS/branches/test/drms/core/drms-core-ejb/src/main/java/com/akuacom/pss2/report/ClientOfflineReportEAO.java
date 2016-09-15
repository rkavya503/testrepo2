/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.report;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.pss2.report.entities.ClientOfflineReport;


public interface ClientOfflineReportEAO extends BaseEAO<ClientOfflineReport> {
    @Remote
    public interface R extends ClientOfflineReportEAO {}
    @Local
    public interface L extends ClientOfflineReportEAO {}

}
