/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.signal.SignalListAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event.signal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.web.util.EJBFactory;
import java.util.Collections;
import java.util.Date;

/**
 * The Class SignalListAction.
 */
public class SignalListAction extends DispatchAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts
	 * .action.ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward unspecified(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SignalListForm form = (SignalListForm) actionForm;

		final String eventName = form.getEventName();
		final String participantName = form.getParticipantName();

		final List<SignalEntry> newSignalEntries = new ArrayList<SignalEntry>();

		EventParticipant eventParticipant = EJBFactory.getEventManager()
			.getEventParticipant(eventName, participantName, true);

		if (eventParticipant != null) {
			Set<Signal> signals = new HashSet<Signal>();
        	// Get the client signals
			for (Signal signal : eventParticipant.getSignals()) {
            	signals.add(signal);
        	}
        	// Add the event-level signals
        	Set<EventSignal> eventSignals = (eventParticipant.getEvent().getEventSignals());
        	for(Signal signal : eventSignals) {
            	signals.add(signal);
        	}
        	Date now = new Date();
			for (Signal signal : signals) {
				for (SignalEntry signalEntry : signal.getSignalEntries()) {
                	final SignalEntry vo;
                	if (signalEntry instanceof EventSignalEntry) { 
                    	vo = new EventSignalEntry();
                    	((EventSignalEntry)vo).setUUID(signalEntry.getUUID());
                	}
                	else { 
                    	vo = new EventParticipantSignalEntry();
                    	((EventParticipantSignalEntry)vo).setUUID(signalEntry.getUUID());
                	}
                	vo.setTime(signalEntry.getTime());
                	vo.setParentSignal(signalEntry.getParentSignal());
                	vo.setLevelValue(signalEntry.getLevelValue());
                	vo.setNumberValue(signalEntry.getNumberValue());
                	vo.setExpired(signalEntry.getTime().before(now)); 
                	newSignalEntries.add(vo);
				}
			}
        	Collections.sort(newSignalEntries);
		}
		request.setAttribute("signalEntries", newSignalEntries);

		return actionMapping.findForward("success");
	}

	/**
	 * Delete.
	 * 
	 * @param actionMapping
	 *            the action mapping
	 * @param actionForm
	 *            the action form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * 
	 * @return the action forward
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward delete(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SignalListForm form = (SignalListForm) actionForm;

		String[] entryIds = form.getSignalEntryIds();
		if (entryIds == null || entryIds.length == 0) {
			return unspecified(actionMapping, actionForm, request, response);
		}

		final String eventName = form.getEventName();
		final String participantName = form.getParticipantName();

		EventManager eventManager = EJBFactory.getEventManager();
		EventParticipant ep = eventManager.getEventParticipant(eventName,
				participantName, true);

		if (ep != null) {
			Set<EventParticipantSignal> signals = ep.getSignals();
			for (EventParticipantSignal signal : signals) {
				Set<EventParticipantSignalEntry> signalEntries =
                    (Set<EventParticipantSignalEntry>) signal.getSignalEntries();
				for (Iterator<EventParticipantSignalEntry> iterator = signalEntries.iterator(); iterator.hasNext();) {
					EventParticipantSignalEntry signalEntry = iterator.next();
					if (signalEntry == null) {
						continue;
					}
					String id = signalEntry.getUUID();
					for (String entryId : entryIds) {
						if (id.equals(entryId)) {
							iterator.remove();
							break;
						}
					}

				}

			}
		}

		eventManager.setEventParticipant(ep);

		return unspecified(actionMapping, actionForm, request, response);
	}
}
