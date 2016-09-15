/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.EventListAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class EventListAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class EventListAction extends DispatchAction {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(EventListAction.class);

    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return list(actionMapping, actionForm, request, response);
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
        log.debug("listing schedule");

        EventListForm form = (EventListForm) actionForm;

        // Create the event lists here, cppList and dbpList
        EventManager pm = EJBFactory.getEventManager();

		List<Event> eventList = new ArrayList<Event>();
        // Note: since these actions are not atomic, the events can be deleted
        // right after been retrieved. So, a null check is necessary.
        for(EventInfo eventInfo: pm.getEvents())
		{
            final String programName = eventInfo.getProgramName();
            final String eventName = eventInfo.getEventName();
            final Event event = pm.getEvent(programName, eventName);
            if (event != null) {
                event.setState(pm.getEventStatusString(event));
                eventList.add(event);
            }
		}
        form.setEventList(eventList);

        List<Program> programs = new ArrayList<Program>();
        com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);

        List<String> programList = programManager1.getPrograms();
        for (String programName : programList) {
            final Program program = programManager1.getProgram(programName);
            programs.add(program);
        }
        form.setProgramList(programs);

        return mapping.findForward("success");
    }

    /**
     * Delete.
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
    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, 
    	HttpServletRequest request, HttpServletResponse response) throws Exception {
        EventListForm form = (EventListForm) actionForm;
        final EventManager eventManager = EJBFactory.getEventManager();
        final List<EventInfo> eventInfoList = eventManager.getEvents();
        final String[] eventNames = form.getEventNames();
        for (String eventName : eventNames) {
            for (EventInfo eventInfo : eventInfoList) {
                if (eventInfo.getEventName().equals(eventName)) {
                    eventManager.removeEvent(eventInfo.getProgramName(), eventName);
                }
            }
        }

        return mapping.findForward("updated");
    }

}
