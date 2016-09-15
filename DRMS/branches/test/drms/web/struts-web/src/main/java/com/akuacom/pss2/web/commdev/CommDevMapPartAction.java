
package com.akuacom.pss2.web.commdev;

/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.web.commdev.CommDevMapAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.ejb.search.AkuaCursor;
import com.akuacom.ejb.search.SearchCriterion;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.akuacom.pss2.web.util.EJBFactory;


/**
 * The Class CommDevMapAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class CommDevMapPartAction extends DispatchAction {
    // Logger
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(CommDevMapPartAction.class);

    /**
     * default behavior: list comm devices.
     *
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    protected ActionForward unspecified(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return list(mapping, actionForm, request, response);
    }

    /**
     * List.
     *
     * @param mapping the mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommDevListForm form = (CommDevListForm)actionForm;
        if (DisplayTagUtil.isExport(request)) {
            return mapping.findForward("export");
        } else {
        	String uid = (String) request.getSession().getAttribute("uid");
            ActionForward partForward = new ActionForward ("../pss2.utility/participant.jsf?uid=" + uid, true);
            return partForward;
            //return mapping.findForward("success");
        }
    }



}


