/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.report.ObixOnlineReportAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.report.entities.OfflineRecord;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class ObixOnlineReportAction.
 */
public class ObixOnlineReportAction extends Action {
    
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ObixOnlineReportForm form = (ObixOnlineReportForm) actionForm;
        ReportManager report = EJBFactory.getBean(ReportManager.class);
        final List<OfflineRecord> list = report.getObixCommReport();
        form.setRecords(list);

        return actionMapping.findForward("success");
    }
}
