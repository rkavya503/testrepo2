/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.dbp.DbpEventDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event.dbp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.SignalLevelMapper;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.bidding.BiddingProgramManager;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.BidState;
import com.akuacom.pss2.program.dbp.DBPBidProgramEJB;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.EventBidBlock;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * The Class DbpEventDetailAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class DbpEventDetailAction extends DispatchAction {

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
        assert (actionForm instanceof DbpEventDetailForm);
        DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();

        ProgramParticipantManager programParticipantManager = EJBFactory.getBean(ProgramParticipantManager.class);
        List<Participant> participants = programParticipantManager.getParticipantsForProgramAsObject(programName);
        List<EventParticipant> epList = getEventParticpants(participants);

        DBPEvent event = new DBPEvent();
        event.setEventName(EventUtil.getEventName());
        event.setProgramName(programName);
        event.setStartTime(getTime(form.getEventDate(), form.getStartHour()));
        event.setEndTime(getTime(form.getEventDate(), form.getEndHour()));
        event.setIssuedTime(new Date());
        event.setReceivedTime(new Date());
        event.setRespondBy(getTime(form.getRespondByDate(), form.getRespondByHour(), form.getRespondByMin()));
        event.setDrasRespondBy(event.getRespondBy()); // a dummy field.
        event.setCurrentBidState(BidState.ACCEPTING);
        event.setParticipants(epList);
        event.setManual(true);

        try {
            EJBFactory.getEventManager().createEvent(programName, event);
        } catch (Exception e) {
            final String s = ErrorUtil.getErrorMessage(e);
            final ActionErrors errors = new ActionErrors();
            errors.add("Event creation failed", new ActionMessage("pss2.event.create.creationError", s));
            saveErrors(request, errors);
            return actionMapping.findForward("success");
        }

        return actionMapping.findForward("parent");
    }

/*
    public ActionForward edit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        assert (actionForm instanceof DbpEventDetailForm);
        DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();

        ProgramDataAccess pda = EJBFactory.getProgramDataAccess();
        DBPEvent event = (DBPEvent)pda.getEvent(programName, eventName);
        form.setParticipants(event.getParticipants());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        form.setEventDate(dateFormat.format(event.getStartTime()));

        Calendar cal = Calendar.getInstance();

        cal.setTime(event.getStartTime());
        form.setStartHour(cal.get(Calendar.HOUR) + "");

        cal.setTime(event.getEndTime());
        form.setEndHour(cal.get(Calendar.HOUR) + "");

//        form.setRespondByDate(dateFormat.format(event.));

        return actionMapping.findForward("edit");
    }
*/

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
        DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();

        EventManager pm = EJBFactory.getEventManager();

        DBPEvent event = (DBPEvent)pm.getEvent(programName, eventName);
        if (event == null) {
            return actionMapping.findForward("parent");
        }

        List<String> blockTimes = new ArrayList<String>();
        List<EventBidBlock> timeBlocks = event.getBidBlocks();
        for (EventBidBlock timeBlock : timeBlocks) {
            String blockTime = SignalLevelMapper.getTimeBlock(timeBlock);
            blockTimes.add(blockTime);
        }
        request.setAttribute("blockTimes", blockTimes);

        event.setState(pm.getEventStatusString(event));
        request.setAttribute("event", event);

        // TODO: change this to state based
        if(event.getIssuedTime().getTime() < System.currentTimeMillis())
        {
            List<CurrentBidVO> currentBids = new ArrayList<CurrentBidVO>();
            BiddingProgramManager bidding = EJBFactory.getBiddingProgramManager();
            List<EventParticipant> eventParticipants = event.getParticipants();
            double[] reductionTotals = new double[timeBlocks.size()];
            List<Participant> clients = new ArrayList<Participant>();
            for (EventParticipant eventParticipant : eventParticipants) {
                // filter client out
                final ParticipantManager parm = EJBFactory.getBean(ParticipantManager.class);
                final Participant vo1 = parm.getParticipant(eventParticipant.getParticipant().getParticipantName(), eventParticipant.getParticipant().isClient());
                if (vo1.isClient()) {
                    clients.add(vo1);
                    continue;
                }
                // do data population
                final CurrentBidVO vo = new CurrentBidVO();
                vo.setAccountNumber(eventParticipant.getParticipant().getAccountNumber());
                final String participantName = eventParticipant.getParticipant().getParticipantName();
                vo.setParticipantName(participantName);
                final boolean bidAccepted =
                        bidding.isBidAccepted(programName, event, participantName, vo1.isClient());
                final boolean bidAcked =
                        bidding.isBidAcknowledged(programName, event,
                                participantName, vo1.isClient());
                vo.setBidStatus(getBidStatus(bidAcked, bidAccepted));

                final List<Double> reductions = new ArrayList<Double>();
                List<BidEntry> bids = bidding.getCurrentBid(programName, 
                	eventName, participantName, vo1.isClient());
                Collections.sort(bids);
                for (int j = 0; j < timeBlocks.size(); j++) {
                    double kw = 0.0;
                    Calendar cal = Calendar.getInstance();
                    // time block start time is now 4 decimal digits in HHmm format.
                    int startTime = timeBlocks.get(j).getStartTime();
                    int hour = startTime / 100;
                    int minute = startTime % 100;
                    long timeBlockStartMS = cal.getTimeInMillis();
                    // match bid with time block.
                    if (bids != null && bids.size() > 0) {
                    	BidEntry bidEntry = null;
                    	for(BidEntry bid: bids) {
                            cal.setTime(bid.getBlockStart());
                            int h = cal.get(Calendar.HOUR_OF_DAY);
                            int m = cal.get(Calendar.MINUTE);
                            if (h == hour && m == minute) {
                    			bidEntry = bid;
                    			break;
                    		}
                    	}
                        if (bidEntry != null && bidEntry.isActive()) {
                            kw = bidEntry.getReductionKW();
                        }
                    }
                    reductions.add(kw);
                    reductionTotals[j] += kw;
                }
                vo.setReductions(reductions);
                currentBids.add(vo);
            }
            request.setAttribute("currentBids", currentBids);
            request.setAttribute("reductionTotals", reductionTotals);
            request.setAttribute("clients", clients);
       }
        else
        {
	        double[] reductionTotals = new double[timeBlocks.size()];
            for (int j = 0; j < timeBlocks.size(); j++) {
                reductionTotals[j] = 0.0;
	        }
	        request.setAttribute("currentBids", null);
	        request.setAttribute("reductionTotals", reductionTotals);
        } 

        return actionMapping.findForward("view");
    }

    /**
     * Gets the bid status.
     * 
     * @param bidAcked the bid acked
     * @param bidAccepted the bid accepted
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

    /**
     * Gets the time.
     * 
     * @param date the date
     * @param hour the hour
     * @param min the min
     * 
     * @return the time
     * 
     * @throws ParseException the parse exception
     */
    private Date getTime(String date, String hour, String min) throws ParseException {
        return new SimpleDateFormat("MM/dd/yyyy H m").parse(date + " " + hour + " " + min);
    }

    /**
     * Gets the time.
     * 
     * @param date the date
     * @param hour the hour
     * 
     * @return the time
     * 
     * @throws ParseException the parse exception
     */
    private Date getTime(String date, String hour) throws ParseException {
        return new SimpleDateFormat("MM/dd/yyyy H m").parse(date + " " + hour + " 0");
    }

    /**
     * The account number is saved here for later display.
     * 
     * @param parts list of parts
     * 
     * @return list of ep
     */
    protected List<EventParticipant> getEventParticpants(List<Participant> parts)
	{
		List<EventParticipant> list = new ArrayList<EventParticipant>();

		for (Participant part : parts)
		{
			final EventParticipant ep = new EventParticipant();

			ep.setParticipant(part);
			list.add(ep);
		}

		return list;
	}

    /**
     * Send bids.
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
    public ActionForward sendBids(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();
        EventManager pm = EJBFactory.getEventManager();
        final Event event = pm.getEvent(eventName);
        DBPBidProgramEJB ejb = EJBFactory.getBean(DBPBidProgramEJB.class);
        ejb.sendBidsOut((DBPEvent) event);

        return view(actionMapping, actionForm, request, response);
    }

    /**
     * Accept.
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
    public ActionForward accept(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();
        final String[] names = request.getParameterValues("biddings");
        if (names != null) {
            for (String username : names) {
                BiddingProgramManager dbpProgram = EJBFactory.getBiddingProgramManager();
                dbpProgram.setBidAccepted(programName, eventName, username, false, true);
            }
        }

        return view(actionMapping, actionForm, request, response);
    }

    /**
     * Accept all.
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
    public ActionForward acceptAll(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();
        
        EventManager pm = EJBFactory.getEventManager();
        final List<EventParticipant> list = pm.getEvent(eventName).getParticipants();
        BiddingProgramManager dbpProgram = EJBFactory.getBiddingProgramManager();
        for (EventParticipant eventParticipant : list) {
            Participant participant = eventParticipant.getParticipant();
            if (!participant.isClient()) {
                final String username = participant.getParticipantName();
                dbpProgram.setBidAccepted(programName, eventName, username, participant.isClient(), true);
            }
        }

        return view(actionMapping, actionForm, request, response);
    }

    /**
     * Reject.
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
    public ActionForward reject(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();
        final String[] names = request.getParameterValues("biddings");
        if (names != null) {
            for (String username : names) {
                BiddingProgramManager dbpProgram = EJBFactory.getBiddingProgramManager();
                dbpProgram.setBidAccepted(programName, eventName, username, false, false);
            }
        }

        return view(actionMapping, actionForm, request, response);
    }

    /**
     * Reject all.
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
    public ActionForward rejectAll(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();

        EventManager pm = EJBFactory.getEventManager();
        final List<EventParticipant> list = pm.getEvent(eventName).getParticipants();
        BiddingProgramManager dbpProgram = EJBFactory.getBiddingProgramManager();
        for (EventParticipant eventParticipant : list) {
            Participant participant = eventParticipant.getParticipant();
            if (!participant.isClient()) {
                final String username = participant.getParticipantName();
                dbpProgram.setBidAccepted(programName, eventName, username, participant.isClient(), false);
            }
        }

        return view(actionMapping, actionForm, request, response);
    }

    /**
     * Next state.
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
    public ActionForward nextState(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final DbpEventDetailForm form = (DbpEventDetailForm)actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();
        final EventManager pm = EJBFactory.getEventManager();
        final BiddingProgramManager dbpProgram = EJBFactory.getBiddingProgramManager();

        Collection<Event> eList = pm.findAllPerf();
        for (Event next : eList) {
            if (next.getEventName().equals(eventName)) {
                dbpProgram.nextState(programName, (DBPEvent)next);
                break;
            }
        }

        return view(actionMapping, actionForm, request, response);
    }
}
