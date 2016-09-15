/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.uohome.UOEventAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.uohome;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.event.EventListForm;
import com.akuacom.pss2.web.tabs.TabsAction;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class UOEventAction.
 */
public class UOEventAction extends DispatchAction {

    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String uid = (String) request.getSession().getAttribute("uid");
        ActionForward partForward = new ActionForward ("../pss2.utility/events.jsf?uid=" + uid, true);
            return partForward;
    	
    	
    	//return list(actionMapping, actionForm, request, response);
    }

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
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();

        EventListForm form = (EventListForm) actionForm;

        return actionMapping.findForward("create");
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
    public ActionForward view(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //hack now should have demo's own view;
        return actionMapping.findForward("view");
    }


    /**
     * List.
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
    public ActionForward list(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        EventListForm form = (EventListForm) actionForm;



        //if(TabsAction.isDemo() && request.isUserInRole(DrasRole.Admin.toString()))
        if(false)
        {
            return actionMapping.findForward("success2");
        }
        else
        {
            List<Event> eventList = getMyEventList(request,actionForm);
            form.setEventList(eventList);

            final boolean export = DisplayTagUtil.isExport(request);


            if (export) {
                return actionMapping.findForward("export");
            } else {
                return actionMapping.findForward("success");
            }

        }
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
        EventManager eventManager = EJBFactory.getEventManager();
        
        Collection<Event> eventInfoList = eventManager.findAll();
        final String[] eventNames = form.getEventNames();
        for (String eventName : eventNames) {
            for (Event eventInfo : eventInfoList) {
                if (eventInfo.getEventName().equals(eventName) && UOEventAction.isMyEvent(request, eventName,actionForm)) {
                    eventManager.removeEvent(eventInfo.getProgramName(), eventName);
                }
            }
        }
        
        if(TabsAction.isDemo() && request.isUserInRole(DrasRole.Admin.toString()))
        {
            return mapping.findForward("success2");
        }
        else
        {
            List<Event> eventList = getMyEventList(request,actionForm);
            form.setEventList(eventList);
            return mapping.findForward("success");
        }
        
    }

    /**
     * Gets the my event list.
     * 
     * @param request the request
     * 
     * @return the my event list
     */
    public List<Event> getMyEventList(HttpServletRequest request,ActionForm actionForm)
    {
        List<Event> eventList = new ArrayList<Event>();
       
        EventManager em = EJBFactory.getEventManager();
        Collection<Event> events = em.findAll();
		for (Event event : events) {
			if (!event.getProgramName().equals(TestProgram.PROGRAM_NAME) ||
					(event.getProgramName().equals(TestProgram.PROGRAM_NAME) && request.isUserInRole(DrasRole.Admin.toString()))) {
				event.setState(em.getEventStatusString(event));
				eventList.add(event);
			}
		}
        
        return eventList;
    }

    /**
     * Checks if is my event.
     * 
     * @param request the request
     * @param eventName the event name
     * 
     * @return true, if is my event
     */
    static public boolean isMyEvent(HttpServletRequest request, String eventName, ActionForm actionForm)
    {
        com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);

        List<Program> progs = UOProgramAction.getMyProgramList(request,actionForm);
        for(int i=0; i<progs.size(); i++)
        {
            final List<EventInfo> events = programManager1.getEventsForProgram(progs.get(i).getProgramName());
            for(EventInfo eventInfo: events)
            {                
                final String eventNameCur = eventInfo.getEventName();
                if (eventNameCur.equalsIgnoreCase(eventName) ) {
                    return true;
                 }
             }
        }
        return false;
    }
}