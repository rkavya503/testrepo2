/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.session.LoginAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.akuacom.pss2.util.DrasRole;

/**
 * The Class LoginAction.
 */
public class LoginAction extends Action {

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping actionMapping, 
    	ActionForm actionForm, HttpServletRequest request, 
    	HttpServletResponse response) throws Exception {
        if (request.isUserInRole(DrasRole.Admin.toString())) {
            return actionMapping.findForward("success");
        } else if (request.isUserInRole(DrasRole.Operator.toString())) {
            return actionMapping.findForward("success");
        } else if (request.isUserInRole(DrasRole.Readonly.toString())) {
            return actionMapping.findForward("success");
        } else if (request.isUserInRole(DrasRole.Dispatcher.toString())) {
            return actionMapping.findForward("success");
        } else if (request.isUserInRole(DrasRole.UtilityOperator.toString())) {
            return actionMapping.findForward("success");
        } else if (request.isUserInRole(DrasRole.FacilityManager.toString())) {
            response.sendRedirect("/facdash");
            return null;
        } else {
            return actionMapping.findForward("logout");
        }
    }
}
