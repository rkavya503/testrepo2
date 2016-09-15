/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.report.ClientParticipationAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.report;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.dbp.DBPEventCreation;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.akuacom.pss2.web.util.ValidateTool;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class ClientParticipationAction.
 */
public class DBPEventCreationReportAction extends DispatchAction {
    
    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        
	    SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
	    String dateFormat=systemManager.getPss2Features().getDateFormat();

    	// default search past month
    	DBPEventCreationReportForm form = (DBPEventCreationReportForm) actionForm;
        final Calendar cal = Calendar.getInstance();
        final Date end = cal.getTime();
        final String endDate = ValidateTool.getDateFormat(dateFormat).format(end);
        form.setEndDate(endDate);
        cal.add(Calendar.MONTH, -1);
        final Date start = cal.getTime();
        final String startDate = ValidateTool.getDateFormat(dateFormat).format(start);
        form.setStartDate(startDate);
        return actionMapping.findForward("success");
    }

    /**
     * List.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param httpServletRequest the http servlet request
     * @param httpServletResponse the http servlet response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward list(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
	    SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
	    String dateFormat=systemManager.getPss2Features().getDateFormat();
	    
    	DBPEventCreationReportForm form = (DBPEventCreationReportForm) actionForm;
        final ActionErrors actionErrors = validateInputs(form, dateFormat);
        if (actionErrors.size() > 0) {
            saveErrors(httpServletRequest, actionErrors);
            return actionMapping.findForward("success");
        }
        final Date startTime = ValidateTool.getDateFormat(dateFormat).parse(form.getStartDate());
        final Date endTime = DateUtil.getEndOfDay(ValidateTool.getDateFormat(dateFormat).parse(form.getEndDate()));
        
        ReportManager report = EJBFactory.getBean(ReportManager.class);
        final List<DBPEventCreation> list = report.getDBPEventCreation(startTime, endTime);
        
        form.setDbpEventCreationList(list);

        if (DisplayTagUtil.isExport(httpServletRequest)) {
            return actionMapping.findForward("export");
        } else {
            return actionMapping.findForward("success");
        }
    }

    private ActionErrors validateInputs(DBPEventCreationReportForm form, String format) {
        ActionErrors errors = new ActionErrors();
        // validate start date
        final String startDate = form.getStartDate();
        ValidateTool.validateDateField(errors, startDate, "startDate", "Start Date", format);
        // validate end date
        final String endDate = form.getEndDate();
        ValidateTool.validateDateField(errors, endDate, "endDate", "End Date", format);

        return errors;
    }
}
