/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.EventDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;

/**
 * This class hides the detail implementations of different programs by calling
 * program name which may be defined in the global forwards instead of local
 * settings.
 * 
 * @author Dichen Mao
 * @since 4.1
 */
@SuppressWarnings({"UnusedDeclaration"})
public class EventDetailAction extends DispatchAction {

    /**
     * Creates the.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward create(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return lookUpForward(request, actionMapping);
    }

    /**
     * Edits the.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward edit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return lookUpForward(request, actionMapping);
    }

    /**
     * View.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward view(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return lookUpForward(request, actionMapping);
    }

    /**
     * Look up forward.
     * 
     * @param request the request
     * @param actionMapping the action mapping
     * 
     * @return the action forward
     */
    private ActionForward lookUpForward(HttpServletRequest request, ActionMapping actionMapping) {
        /* looks up program, and dispatch using program's own knowledge. */
        final ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
        final String programName = request.getParameter("programName");
        final Program program = programManager.getProgramOnly(programName);
        return actionMapping.findForward(program.getUiScheduleEventString());
    }
}
