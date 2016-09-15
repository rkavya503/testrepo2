/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.dbp.DbpNoBidEventDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event.dbp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.BiddingInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.SignalLevelMapper;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.bidding.BiddingProgramManager;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.EventBidBlock;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.pss2.web.util.StrutsErrorUtil;
import com.akuacom.utils.lang.StackTraceUtil;

/**
 * The Class DbpNoBidEventDetailAction.
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class DbpNoBidEventDetailAction extends DispatchAction {
	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(DbpNoBidEventDetailAction.class);

    /**
	 * Creates the.
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
	public ActionForward create(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return actionMapping.findForward("success");
	}

	/**
	 * Cancel.
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
	public ActionForward cancel(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return actionMapping.findForward("parent");
	}

	/**
	 * Gets the time.
	 * 
	 * @param date
	 *            the date
	 * @param hour
	 *            the hour
	 * 
	 * @return the time
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	private Date getTime(String date, String hour) throws ParseException {
		return new SimpleDateFormat("MM/dd/yyyy H m").parse(date + " " + hour + " 0");
	}

	private ActionForward createError(ActionMapping actionMapping,
			HttpServletRequest request, String programName, String key,
			Object value0, Object value1, Object value2) {
		ActionMessage message = new ActionMessage(key, value0, value1, value2);
		saveErrors(request, StrutsErrorUtil.createActionErrors(
				ActionMessages.GLOBAL_MESSAGE, message));
		log.error(LogUtils.createLogEntry(programName, LogUtils.CATAGORY_EVENT,
				message.toString(), ""));
		return actionMapping.findForward("success");
	}

	/**
	 * Save.
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
	public ActionForward save(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DbpNoBidEventDetailForm form = (DbpNoBidEventDetailForm) actionForm;
		final String programName = form.getProgramName();
		try {
			EventManager eventManager = EJBFactory.getEventManager();
			BiddingProgramManager biddingManager = EJBFactory
					.getBiddingProgramManager();
			UtilityDREvent utilityDREvent = new UtilityDREvent();

			// basic stuff
			utilityDREvent.setProgramName(programName);
			String eventName = EventUtil.getEventName();
			utilityDREvent.setEventIdentifier(eventName);

			// event timing
			EventTiming eventTiming = new EventTiming();

			eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(
					new GregorianCalendar()));

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(getTime(form.getEventDate(), form.getStartHour()));
			eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));

			cal.setTime(getTime(form.getEventDate(), form.getEndHour()));
			eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

			utilityDREvent.setEventTiming(eventTiming);

			BiddingInformation biddingInformation = new BiddingInformation();
			biddingInformation.setOpeningTime(eventTiming.getStartTime());
			biddingInformation.setClosingTime(eventTiming.getStartTime());
			utilityDREvent.setBiddingInformation(biddingInformation);

			// convert bid file to a String
			BufferedReader in = new BufferedReader(new InputStreamReader(form
					.getDataFile().getInputStream()));
			StringBuilder bidString = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				bidString.append(line);
				bidString.append('\n');
			}

//			// parse the bid file
//			EventInformation eventInformation = biddingManager.parseBidFile(
//					utilityDREvent, form.getDataFile().getFileName(), bidString
//							.toString());
//			utilityDREvent.setEventInformation(eventInformation);
//
//			eventManager.createEvent(utilityDREvent, true, false);

			return actionMapping.findForward("events");
		} catch (Exception e) {
			ValidationException ve = ErrorUtil.getValidationException(e);
			if (ve != null) {
				ActionErrors errors = new ActionErrors();
				ActionMessage error = new ActionMessage(ve
						.getLocalizedMessage(), false);
				errors.add("participantValidation", error);
				addErrors(request, errors);
			} else {
				saveErrors(request, StrutsErrorUtil.createActionErrors(e));
				log.error(LogUtils.createLogEntry(programName,
						LogUtils.CATAGORY_EVENT, ErrorUtil.getErrorMessage(e),
						ErrorUtil.getErrorMessage(e)
								+ StackTraceUtil.getStackTrace(e)));
			}
		}
		return actionMapping.findForward("success");
	}

	public ActionForward confirm(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			final DBPEvent event = (DBPEvent) request.getSession()
					.getAttribute("event");
			final UtilityDREvent uEvent = (UtilityDREvent) request.getSession()
					.getAttribute("UtilityDREvent");

			EventManager eventManager = EJBFactory.getEventManager();
			final Date date = new Date();
			event.setIssuedTime(date);
			event.setReceivedTime(date);
			eventManager.createEvent(event.getProgramName(), event, uEvent);
		} catch (Exception e) {
			final String s = ErrorUtil.getErrorMessage(e);
			final ActionErrors errors = new ActionErrors();
			errors.add("Event creation failed", new ActionMessage(
					"pss2.event.create.creationError", s));
			saveErrors(request, errors);
			return actionMapping.findForward("success");
		}

		return actionMapping.findForward("events");
	}

	/**
	 * View.
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
	public ActionForward view(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DbpNoBidEventDetailForm form = (DbpNoBidEventDetailForm) actionForm;
		final String programName = form.getProgramName();
		final String eventName = form.getEventName();

		EventManager pm = EJBFactory.getEventManager();

		DBPEvent event = (DBPEvent) pm.getEvent(programName, eventName);
		if (event == null) {
			return actionMapping.findForward("parent");
		}

		List<String> blockTimes = new ArrayList<String>();
		List<EventBidBlock> timeBlocks = event.getBidBlocks();
		Collections.sort(timeBlocks);

		// filter out outbound event blocks
		final Calendar cal = Calendar.getInstance();
		cal.setTime(event.getStartTime());
		final int start = cal.get(Calendar.HOUR_OF_DAY);
		cal.setTime(event.getEndTime());
		final int end = cal.get(Calendar.HOUR_OF_DAY);
		for (EventBidBlock timeBlock : timeBlocks) {
			if (timeBlock.getStartTime() / 100 >= start
					&& timeBlock.getEndTime() / 100 <= end) {
				String blockTime = SignalLevelMapper.getTimeBlock(timeBlock);
				blockTimes.add(blockTime);
			}
		}
		request.setAttribute("blockTimes", blockTimes);

		event.setState(pm.getEventStatusString(event));
		request.setAttribute("event", event);

		// TODO: change this to state based
		if (event.getIssuedTime().getTime() < System.currentTimeMillis()) {
			List<CurrentBidVO> currentBids = new ArrayList<CurrentBidVO>();
			BiddingProgramManager bidding = EJBFactory
					.getBiddingProgramManager();
			List<EventParticipant> eventParticipants = event.getParticipants();
			double[] reductionTotals = new double[blockTimes.size()];
			List<Participant> clients = new ArrayList<Participant>();
			for (EventParticipant eventParticipant : eventParticipants) {
				// filter client out
				final Participant vo1 = eventParticipant.getParticipant();
				if (vo1.isClient()) {
					clients.add(vo1);
					continue;
				}
				// do data population
				final CurrentBidVO vo = new CurrentBidVO();
				vo.setAccountNumber(eventParticipant.getParticipant()
						.getAccountNumber());
				final String participantName = eventParticipant
						.getParticipant().getParticipantName();
				vo.setParticipantName(participantName);

				final boolean bidAccepted = bidding.isBidAccepted(programName,
						event, participantName, false);
				final boolean bidAcked = bidding.isBidAcknowledged(programName,
						event, participantName, false);
				vo.setBidStatus(getBidStatus(bidAcked, bidAccepted));

				final List<Double> reductions = new ArrayList<Double>();
				List<BidEntry> bids = bidding.getCurrentBid(programName, event,
						eventParticipant.getParticipant(), false);
				Collections.sort(bids);
				double total = 0.0;
				for (int i = 0, bidsSize = bids.size(); i < bidsSize; i++) {
					BidEntry bid = bids.get(i);
					double kw = 0.0;
					if (bid.isActive()) {
						kw = bid.getReductionKW();
					}
					reductions.add(kw);
					reductionTotals[i] += kw;
				}
				vo.setReductions(reductions);
				currentBids.add(vo);
			}
			request.setAttribute("currentBids", currentBids);
			request.setAttribute("reductionTotals", reductionTotals);
			request.setAttribute("clients", clients);
		} else {
			double[] reductionTotals = new double[timeBlocks.size()];
			for (int j = 0; j < timeBlocks.size(); j++) {
				reductionTotals[j] = 0.0;
			}
			request.setAttribute("currentBids", null);
			request.setAttribute("reductionTotals", reductionTotals);
		}

		return actionMapping.findForward("view");
	}

	private int getBidTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.HOUR_OF_DAY) * 100 + cal.get(Calendar.MINUTE);
	}

	private void setAttributes(DBPEvent event, UtilityDREvent utilityDREvent,
			HttpServletRequest request) throws Exception {
		List<String> blockTimes = new ArrayList<String>();
		List<EventBidBlock> timeBlocks = event.getBidBlocks();
		for (EventBidBlock timeBlock : timeBlocks) {
			String blockTime = SignalLevelMapper.getTimeBlock(timeBlock);
			blockTimes.add(blockTime);
		}
		request.setAttribute("blockTimes", blockTimes);

		// TODO: change this to state based
		if (event.getIssuedTime().getTime() < System.currentTimeMillis()) {
			List<CurrentBidVO> currentBids = new ArrayList<CurrentBidVO>();
			BiddingProgramManager bidding = EJBFactory
					.getBiddingProgramManager();
			List<EventParticipant> eventParticipants = event.getParticipants();
			double[] reductionTotals = new double[timeBlocks.size()];
			List<Participant> clients = new ArrayList<Participant>();
			for (EventParticipant eventParticipant : eventParticipants) {
				// filter client out
				final Participant vo1 = eventParticipant.getParticipant();
				if (vo1.isClient()) {
					clients.add(vo1);
					continue;
				}
				// do data population
				final CurrentBidVO vo = new CurrentBidVO();
				vo.setAccountNumber(eventParticipant.getParticipant()
						.getAccountNumber());
				final String participantName = eventParticipant
						.getParticipant().getParticipantName();
				vo.setParticipantName(participantName);

				final List<Double> reductions = new ArrayList<Double>();

				List<EventInfoInstance> eventInfoInstances = utilityDREvent
						.getEventInformation().getEventInfoInstance();
				for (int j = 0; j < timeBlocks.size(); j++) {
					double kw = 0.0;
					if (eventInfoInstances != null
							&& eventInfoInstances.size() > 0) {
						EventInfoInstance info = null;
						for (EventInfoInstance info1 : eventInfoInstances) {
							if (info1.getParticipants().getAccountID().get(0)
									.equalsIgnoreCase(vo1.getAccountNumber())) {
								info = info1;
								break;
							}
						}
						List<EventInfoValue> values = info.getValues()
								.getValue();
						kw = values.get(j).getValue();
						reductions.add(kw);
						reductionTotals[j] += kw;
					}
				}

				vo.setReductions(reductions);
				currentBids.add(vo);
			}
			request.setAttribute("currentBids", currentBids);
			request.setAttribute("reductionTotals", reductionTotals);
			request.setAttribute("clients", clients);
		} else {
			double[] reductionTotals = new double[timeBlocks.size()];
			for (int j = 0; j < timeBlocks.size(); j++) {
				reductionTotals[j] = 0.0;
			}
			request.setAttribute("currentBids", null);
			request.setAttribute("reductionTotals", reductionTotals);
		}
	}

	/**
	 * Gets the bid status.
	 * 
	 * @param bidAcked
	 *            the bid acked
	 * @param bidAccepted
	 *            the bid accepted
	 * 
	 * @return the bid status
	 */
	private String getBidStatus(boolean bidAcked, boolean bidAccepted) {
		String status;
		if (bidAcked) {
			if (bidAccepted) {
				status = "Accepted";
			} else {
				status = "Rejected";
			}
		} else {
			status = "Pending";
		}
		return status;
	}
}
