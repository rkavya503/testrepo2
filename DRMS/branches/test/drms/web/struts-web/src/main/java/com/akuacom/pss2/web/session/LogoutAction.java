/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.session.LogoutAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.session;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * When a new session is created, the SessionListener sets a new UID.
 * So, if the UID is changed, it mean another session already started.
 * In that case, no need to invalidate the session.
 * 
 * @author Dichen Mao
 * @since 4.1
 * @see SessionListener
 * @see SessionManager
 */
public class LogoutAction extends Action {

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        String uid = request.getParameter("uid");
        Object suid = session.getAttribute("uid");
        if( suid != null && suid.equals(uid) ) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // ignore that
            }
        }else{
        	try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // ignore that
            }
        }

        return actionMapping.findForward("success");
    }
}
