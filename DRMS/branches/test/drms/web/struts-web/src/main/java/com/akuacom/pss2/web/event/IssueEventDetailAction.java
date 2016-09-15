/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.IssueEventDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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

import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class IssueEventDetailAction.
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class IssueEventDetailAction extends DispatchAction
{
	
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
	public ActionForward create(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
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
	public ActionForward cancel(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
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
	public ActionForward save(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		assert (actionForm instanceof IssueEventDetailForm);
		IssueEventDetailForm form = (IssueEventDetailForm)actionForm;
		final String programName = form.getProgramName();

		final EventManager eventManager = EJBFactory.getEventManager();
		ProgramParticipantManager programParticipantManager = EJBFactory.getBean(ProgramParticipantManager.class);
        

		final List<Participant> participants = programParticipantManager.getParticipantsForProgramAsObject(programName);
        final List<EventParticipant> epList = getEventParticpants(participants);

		final Event event = new Event();
		event.setEventName(EventUtil.getEventName());
		event.setProgramName(programName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy H m");
        event.setIssuedTime(simpleDateFormat.parse(form.getIssueDate() + " "
                + form.getIssueHour() + " " + form.getIssueMin()));
		event.setStartTime(simpleDateFormat.parse(form.getEventDate() + " "
                + form.getStartHour() + " " + form.getStartMin()));
		event.setEndTime(simpleDateFormat.parse(form.getEventDate() + " "
                + form.getEndHour() + " " + form.getEndMin()));
		final Date date = new Date();
		event.setReceivedTime(date);
		event.setParticipants(epList);
		event.setManual(true);

		try
		{
			eventManager.createEvent(programName, event);
		}
		catch (Exception e)
		{
			final String s = ErrorUtil.getErrorMessage(e);
			final ActionErrors errors = new ActionErrors();
			errors.add("Event creation failed", new ActionMessage(
				"pss2.event.create.creationError", s));
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
	public ActionForward view(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		EventDetailForm form = (EventDetailForm) actionForm;
		final String programName = form.getProgramName();
		final String eventName = form.getEventName();

        final EventManager eventManager = EJBFactory.getEventManager();
        final Event event = eventManager.getEvent(programName, eventName);
        if (event == null) {
            return actionMapping.findForward("parent");
        }
		event.setState(eventManager.getEventStatusString(event));
		request.setAttribute("event", event);

		List<EventParticipant> participants = event.getParticipants();
		request.setAttribute("participants", participants);

		return actionMapping.findForward("view");
	}

	/**
	 * The account number is saved here for later display.
	 * 
	 * @param names list of names
	 * 
	 * @return lit of ep
	 */
	protected List<EventParticipant> getEventParticpants(List<Participant> parts)
	{
		List<EventParticipant> list = new ArrayList<EventParticipant>();

		ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
		for (Participant part : parts)
		{
			final EventParticipant ep = new EventParticipant();

			ep.getParticipant().setParticipantName(part.getParticipantName());
            ep.getParticipant().setAccountNumber(part.getAccountNumber());
			list.add(ep);
		}

		return list;
	}
}
