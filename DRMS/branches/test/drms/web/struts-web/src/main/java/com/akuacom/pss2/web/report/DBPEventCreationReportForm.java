/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.report.ClientParticipationReportForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.report;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.akuacom.pss2.program.dbp.DBPEventCreation;

/**
 * The Class ClientParticipationReportForm.
 */
public class DBPEventCreationReportForm extends ActionForm {
    
    /** The start date. */
    private String startDate;
    
    /** The end date. */
    private String endDate;
    
    /** The DBPEventCreation list. */
    private List<DBPEventCreation> dbpEventCreationList;

    /**
     * Gets the end date.
     * 
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     * 
     * @param endDate the new end date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the start date.
     * 
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     * 
     * @param startDate the new start date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

	public List<DBPEventCreation> getDbpEventCreationList() {
		return dbpEventCreationList;
	}

	public void setDbpEventCreationList(List<DBPEventCreation> dbpEventCreationList) {
		this.dbpEventCreationList = dbpEventCreationList;
	}
    
}
