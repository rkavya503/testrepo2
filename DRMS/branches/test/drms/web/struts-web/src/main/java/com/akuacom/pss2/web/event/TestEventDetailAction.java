/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.TestEventDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class TestEventDetailAction.
 */
public class TestEventDetailAction extends BasicEventDetailAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.web.event.BasicEventDetailAction#save(org.apache.struts
	 * .action.ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward save(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		assert (actionForm instanceof EventDetailForm);
		EventDetailForm form = (EventDetailForm) actionForm;
		final String programName = form.getProgramName();

		final EventManager eventManager = EJBFactory.getEventManager();
	
		ProgramParticipantManager programParticipantManager = EJBFactory
				.getBean(ProgramParticipantManager.class);
		List<Participant> participants = programParticipantManager
				.getParticipantsForProgramAsObject(programName);
		List<EventParticipant> epList = getEventParticpants(participants);

		Event event = new Event();
		event.setEventName(EventUtil.getEventName());
		event.setProgramName(programName);
		event.setStartTime(getTime(form.getEventDate(), form.getStartHour(),
				form.getStartMin(), form.getStartSec()));
		event.setEndTime(getTime(form.getEventDate(), form.getEndHour(),
				form.getEndMin(), form.getEndSec()));
		event.setIssuedTime(new Date());
		event.setReceivedTime(new Date());
		event.setParticipants(epList);
		event.setManual(true);

		try {
			eventManager.createEvent(programName, event);
		} catch (Exception e) {
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
	 * Gets the time.
	 * 
	 * @param date
	 *            the date
	 * @param hour
	 *            the hour
	 * @param min
	 *            the min
	 * @param sec
	 *            the sec
	 * 
	 * @return the time
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	private Date getTime(String date, String hour, String min, String sec)
			throws ParseException {
		return new SimpleDateFormat("MM/dd/yyyy H m s").
                parse(date + " " + hour + " " + min + " " + sec);
	}
}
