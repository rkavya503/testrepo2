/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.BasicEventDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.rds.RDSProgramEJB;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class BasicEventDetailAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class BasicEventDetailAction extends DispatchAction {
    
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
        return actionMapping.findForward("success");
    }

    /**
     * Cancel.
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
    public ActionForward cancel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return actionMapping.findForward("parent");
    }

    /**
     * Save.
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
    public ActionForward save(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        assert (actionForm instanceof EventDetailForm);
        EventDetailForm form = (EventDetailForm)actionForm;
        final String programName = form.getProgramName();

        final EventManager eventManager = EJBFactory.getBean(EventManager.class);
        ProgramParticipantManager programParticipantManager = EJBFactory.getBean(ProgramParticipantManager.class);
        //final Program program = programManager1.getProgram(programName);

        final List<Participant> participants = programParticipantManager.getParticipantsForProgramAsObject(programName);
        final List<EventParticipant> epList = getEventParticpants(participants);

        final Event event = new Event();
        event.setEventName(EventUtil.getEventName());
        event.setProgramName(programName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy H m");
        event.setStartTime(simpleDateFormat.parse(form.getEventDate() + " " + form.getStartHour() + " " + form.getStartMin()));
        event.setEndTime(simpleDateFormat.parse(form.getEventDate() + " " + form.getEndHour() + " " + form.getEndMin()));
        final Date date = new Date();
        event.setIssuedTime(date);
        event.setReceivedTime(date);
        event.setParticipants(epList);
        event.setManual(true);

        try {
            eventManager.createEvent(programName, event);
        } catch (Exception e) {
            final String s = ErrorUtil.getErrorMessage(e);
            final ActionErrors errors = new ActionErrors();
            errors.add("Event creation failed", new ActionMessage("pss2.event.create.creationError", s));
            saveErrors(request, errors);
            return actionMapping.findForward("success");
        }

        return actionMapping.findForward("parent");
    }
    
    public ActionForward confirm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Event event = (Event)request.getSession().getAttribute("event");
        final EventManager eventManager = EJBFactory.getBean(EventManager.class);
        
        try {
            event.setIssuedTime(new Date());
            eventManager.createEvent(event.getProgramName(), event);
        } catch (Exception e) {
        	final String s = ErrorUtil.getErrorMessage(e);
            final ActionErrors errors = new ActionErrors();
            errors.add("Event creation failed", new ActionMessage("pss2.event.create.creationError", s));
            saveErrors(request, errors);
            return actionMapping.findForward("success");
        }

        return actionMapping.findForward("parent");
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
        EventDetailForm form = (EventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();

        SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
        final EventManager eventManager = EJBFactory.getLocalBean(EventManager.class);
        ProgramEJB pEjb = systemManager.lookupProgramBean(programName)  ;
        if(pEjb instanceof RDSProgramEJB )
        {
            request.setAttribute("actionType", "updateEvent");
            return actionMapping.findForward("viewRDS");   
        }
        Event event = eventManager.getByEventNameWithParticipants(eventName);
        if (event == null) {
            return actionMapping.findForward("parent");
        }
        event.setState(eventManager.getEventStatusString(event));
        request.setAttribute("event", event);

        setClients(event, request);

        return actionMapping.findForward("view");
    }
    
    private void setClients(Event event, HttpServletRequest request) throws Exception
    {
		final List<Participant> clients = new ArrayList<Participant>();
		List<EventParticipant> eventParticipants = event.getParticipants();
		for (EventParticipant ep : eventParticipants) {
			if (ep == null)
				continue;
			final Participant p = ep.getParticipant();
			if (p != null && p.isClient()) {
				clients.add(p);
			}
		}
        request.setAttribute("clients", clients);	
    }

    /**
     * The account number is saved here for later display.
     * 
     * @param names list of names
     * 
     * @return lit of ep
     */
    protected List<EventParticipant> getEventParticpants(List<Participant> parts) {
        List<EventParticipant> list = new ArrayList<EventParticipant>();
       
        for (Participant part : parts) {
            final EventParticipant ep = new EventParticipant();
        
             ep.setParticipant(part);
            //ep.getParticipant().setParticipantName(part.getParticipantName());
           // ep.getParticipant().setAccountNumber(part.getAccountNumber());
            list.add(ep);
        }
       
        return list;
    }
}
