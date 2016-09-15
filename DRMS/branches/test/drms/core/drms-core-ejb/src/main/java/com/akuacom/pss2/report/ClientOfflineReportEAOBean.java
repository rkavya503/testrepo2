/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report;

import javax.ejb.Stateless;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.pss2.report.entities.ClientOfflineReport;

/**
 * EAO Bean implementation for {@link com.akuacom.pss2.email.MessageEntity} 
 * 
 * @see com.akuacom.pss2.email.MessageEntity
 */

@Stateless
public class ClientOfflineReportEAOBean extends BaseEAOBean<ClientOfflineReport> implements ClientOfflineReportEAO.R, ClientOfflineReportEAO.L {
	
    public ClientOfflineReportEAOBean() {
        super(ClientOfflineReport.class);
    }
}
