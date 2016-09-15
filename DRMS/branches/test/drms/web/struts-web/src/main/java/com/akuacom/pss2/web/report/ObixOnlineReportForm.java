/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.report.ObixOnlineReportForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.report;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.akuacom.pss2.report.entities.OfflineRecord;

/**
 * The Class ObixOnlineReportForm.
 */
public class ObixOnlineReportForm extends ActionForm {
    
	private static final long serialVersionUID = -4915651537604697425L;

	/** The Records. */
    private List<OfflineRecord> records;

    /**
     * Gets the Records.
     * 
     * @return the Records
     */
    public List<OfflineRecord> getRecords() {
        return records;
    }

    /**
     * Sets the Records.
     * 
     * @param records the new Records
     */
    public void setRecords(List<OfflineRecord> records) {
        this.records = records;
    }
}
