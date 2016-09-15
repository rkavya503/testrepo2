/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.ButtonOnlyEventDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class ButtonOnlyEventDetailAction.
 */
public class ButtonOnlyEventDetailAction extends BasicEventDetailAction {

    /* (non-Javadoc)
     * @see com.akuacom.pss2.web.event.BasicEventDetailAction#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward save(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        assert (actionForm instanceof ButtonOnlyEventDetailForm);
        ButtonOnlyEventDetailForm form = (ButtonOnlyEventDetailForm)actionForm;
        final String programName = form.getProgramName();

        ProgramParticipantManager programParticipantManager = EJBFactory.getBean(ProgramParticipantManager.class);

        List<Participant> participants = programParticipantManager.getParticipantsForProgramAsObject(programName);
        List<EventParticipant> epList = getEventParticpants(participants);

        try {
        	UtilityDREvent utilityDREvent = new UtilityDREvent();
	
			// basic stuff
			utilityDREvent.setProgramName(programName);
			utilityDREvent.setEventIdentifier(EventUtil.getEventName());
	
			EJBFactory.getEventManager().createEvent(utilityDREvent, true);
			return actionMapping.findForward("events");
        
         } catch (Exception e) {
            final String s = ErrorUtil.getErrorMessage(e);
            final ActionErrors errors = new ActionErrors();
            errors.add("Event creation failed", new ActionMessage("pss2.event.create.creationError", s));
            saveErrors(request, errors);
            return actionMapping.findForward("success");
        }         
    }

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.web.event.BasicEventDetailAction#view(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward view(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		ButtonOnlyEventDetailForm form = (ButtonOnlyEventDetailForm) actionForm;
		final String eventName = form.getEventName();

        final EventManager em = EJBFactory.getBean(EventManager.class);
        Event event = em.getByEventNameWithParticipants(eventName);
		if (event == null)
		{
			return actionMapping.findForward("parent");
		}
		request.setAttribute("event", event);
        //todo: participantUUID: this portion doesnt' work.
        final ParticipantManager pm = EJB3Factory.getLocalBean(ParticipantManager.class);
        final List<Participant> list = pm.findParticipantsByProgramName(event.getProgramName());
        final Map<String,Participant> map = new HashMap<String, Participant>();
        for (Participant p : list) {
            map.put(p.getUUID(), p);
        }

        final List<Participant> clients = new ArrayList<Participant>();
        List<EventParticipant> eventParticipants = event.getParticipants();
        for (EventParticipant ep : eventParticipants) {
            //String uuid = pm.getParticipantByParticipantName(ep.getParticipant().getParticipantName(), ep.getParticipant().isClient()).getUUID();
        	String uuid = ep.getParticipant().getUUID();
            final Participant p = map.get(uuid);
            if (p!= null && p.isClient()) {
                clients.add(p);
            }
        }
        request.setAttribute("clients", clients);

		return actionMapping.findForward("view");
	}
}
