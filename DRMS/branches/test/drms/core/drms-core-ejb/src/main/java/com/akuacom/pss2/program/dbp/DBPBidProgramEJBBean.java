/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.dbp.DBPProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventEmailFormatter;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.SceEventEmailFormatter;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntryGenEAO;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.itron.wsclient.ItronClientService;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.bidding.BidBlock;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.query.OptedOutClientList;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * The Class DBPProgramEJBBean.
 */
@Stateless
public class DBPBidProgramEJBBean extends DBPProgramEJBBean implements
        DBPBidProgramEJB.R, DBPBidProgramEJB.L {

    /** The Constant log. */
    private static final Logger log = Logger
            .getLogger(DBPBidProgramEJBBean.class.getName());

    // TODO: these should be in the config file
    /** The Constant DATE_ONLY_FORMAT. */
    private static final String DATE_ONLY_FORMAT = "MM/dd/yyyy";
    
    /**
     * Event condition value for Bid Accept and Event Cancellation
     */
    private static final String EVENT_CANCEL_CONDITION_VAL = "<![CDATA[EventCancellation]]>";
    private static final String BID_ACCEPT_CONDITION_VAL = "<![CDATA[DemandBidAcceptance]]>";
    private static final String EVENT_CANCEL_THEME_VAL = "ITRON:AUTODBP\\EVENTCANCELLATION;ITRON:;VOICETALENT:LESLIE;";
    private static final String BID_ACCEPT_THEME_VAL = "ITRON:AUTODBP\\DEMANDBIDACCEPTANCE;ITRON:;VOICETALENT:LESLIE;";

    /** The dbp da. */
    @EJB
    protected DBPDataAccess.L dbpDA;
    
    @EJB
    private ProgramManager.L programManager;

    @EJB
    SystemManager.L systemManager;

    @EJB
    ParticipantEAO.L participantEAO;
    @EJB
    EventEAO.L eventEAO;

    @EJB(beanName="EventParticipantSignalEntryGenEAOBean")
    private EventParticipantSignalEntryGenEAO.L epSignalEntryEAO;

    @EJB
    private EventParticipantEAO.L epEAO;
    
    @EJB
	private ProgramParticipantAggregationManager.L ppManager;
    
    /**
     * Creates the participant current bids.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     */
    @SuppressWarnings("deprecation")
    private void createParticipantCurrentBids(String programName, Event event) {
        try {
            Program programWithSignals = programManager.getProgramWithSignals(programName);

            // copy the default bids to the current bids for each event
            // participant and initialize acknowledged and accepted flags
            for (EventParticipant eventParticipant : getEventParticipantsByEvent((DBPEvent)event)) {
                final Participant participant = eventParticipant
                        .getParticipant();
                if (participant.isClient()) {
                    continue;
                }

                List<BidEntry> bidEntries = dbpDA.getDefaultBid(programName,
                        participant.getParticipantName(),
                        participant.isClient());
                GregorianCalendar calender = new GregorianCalendar();
                calender.setTime(event.getStartTime());
                for (BidEntry bidEntry : bidEntries) {
                    bidEntry.setBidType(BidEntry.TYPE_CURRENT);

                    Calendar cal = new GregorianCalendar();
                    cal.setTime(bidEntry.getBlockStart());
                    cal.set(Calendar.YEAR, calender.get(Calendar.YEAR));
                    cal.set(Calendar.MONTH, calender.get(Calendar.MONTH));
                    cal.set(Calendar.DAY_OF_MONTH,
                            calender.get(Calendar.DAY_OF_MONTH));
                    bidEntry.setBlockStart(cal.getTime());

                    cal = new GregorianCalendar();
                    cal.setTime(bidEntry.getBlockEnd());
                    cal.set(Calendar.YEAR, calender.get(Calendar.YEAR));
                    cal.set(Calendar.MONTH, calender.get(Calendar.MONTH));
                    cal.set(Calendar.DAY_OF_MONTH,
                            calender.get(Calendar.DAY_OF_MONTH));
                    bidEntry.setBlockEnd(cal.getTime());

                    // deactivate any blocks that aren't within the event window
                    if ((bidEntry.getBlockStart().getTime() < event
                            .getStartTime().getTime())
                            || (bidEntry.getBlockEnd().getTime() > event
                                    .getEndTime().getTime())) {
                        bidEntry.setActive(false);
                    }

                    if (!bidEntry.isActive()) {
                        bidEntry.setReductionKW(0.0);
                        bidEntry.setPriceLevel(1.0);
                    }
                }

                final String eventName = event.getEventName();
                dbpDA.setCurrentBid(programName, event, eventParticipant, bidEntries);
                dbpDA.setBidAcknowledged(programName, event, participant,
                        participant.isClient(), false, eventParticipant);
                dbpDA.setBidAccepted(programName, event, participant,
                        participant.isClient(), false, eventParticipant);
                dbpDA.setBidDeclined(programName, eventName,
                        participant.getParticipantName(),
                        false, eventParticipant);

                // initialize the program signals as empty lists
                Set<EventParticipantSignal> eventParticipantSignals = eventParticipant.getSignals();
                for (ProgramSignal programSignal : programWithSignals.getSignals()) {
                    EventParticipantSignal newSignal = new EventParticipantSignal();
                    newSignal.setEventParticipant(eventParticipant);
                    newSignal.setSignalDef(programSignal.getSignalDef());
                    eventParticipantSignals.add(newSignal);
                }
            }
//            eventEAO.update(event);
        } catch (Exception e) {
            String message = "can't create current bids: "
                    + event.getEventName();
            log.error(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, message, event.toString()));
            log.debug(LogUtils.createExceptionLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, e));
            throw new EJBException(message, e);
        }
    }

    @Override
    protected void processRulesAndSignals(Program program, Event event,
            UtilityDREvent utilityDREvent, Date now)
            throws ProgramValidationException {
        // we don't do anything here since this is called by createEvent
        // before the bidding is complete. we'll do this by hand in
        // processAcceptedBid
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJBBean#addParticipant(java.lang.String,
     * java.lang.String)
     */
    public void addParticipant(String programName, String participantName,
            boolean isClient) {
        super.addParticipant(programName, participantName, isClient);
        try {
            // initialize the standing bid
            List<BidEntry> bidEntries = new ArrayList<BidEntry>();
            Program program = programManager.getProgramWithLoadBid(programName);
            for (BidBlock block : program.getBidConfig().getBidBlocks()) {
                BidEntry bidEntry = new BidEntry();
                bidEntry.setBlockStart(block.getStartReferenceTime());
                bidEntry.setBlockEnd(block.getEndReferenceTime());
                bidEntry.setReductionKW(program.getBidConfig()
                        .getDefaultBidKW());
                // TODO: this should be parameter driven
                bidEntry.setPriceLevel(3.0);
                bidEntry.setParticipantName(participantName);
                bidEntry.setActive(true);
                bidEntry.setProgramName(programName);
                bidEntries.add(bidEntry);
            }

            dbpDA.setDefaultBid(programName, participantName, isClient,
                    bidEntries);

        } catch (Exception e) {
            String message = "error adding participant " + participantName
                    + " to program " + programName;
            FireLogEntry logEntry = new FireLogEntry();
            logEntry.setUserParam1(programName);
            logEntry.setCategory(LogUtils.CATAGORY_EVENT);
            logEntry.setDescription(message);
            log.error(logEntry);
            throw new EJBException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.dbp.DBPProgramEJB#setBidStatus(java.util.List,
     * java.util.List)
     */
    public void setBidStatus(List<Bid> eventParticipantAcceptedList,
            List<Bid> eventParticipantRejectedList) {
        // reject bids
        if (eventParticipantRejectedList != null) {
            for (Bid bid : eventParticipantRejectedList) {
                final Participant participant = participantManager
                        .getParticipant(bid.getAccountName());
                String partName = participant.getParticipantName();
                setBidAccepted(bid.getProgramName(), bid.getEventName(),
                        partName, false, false);
            }
        }

        // accept bids
        if (eventParticipantAcceptedList != null) {
            for (Bid bid : eventParticipantAcceptedList) {
                final Participant participant = participantManager
                        .getParticipant(bid.getAccountName());
                String partName = participant.getParticipantName();
                setBidAccepted(bid.getProgramName(), bid.getEventName(),
                        partName, false, false);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.dbp.DBPProgramEJB#setBidAccepted(java.lang.String
     * , java.lang.String, java.lang.String, boolean)
     */
    public void setBidAccepted(String programName, String eventName,
            String participantName, boolean isClient, boolean accepted) {

        try {
            final Event event = eventEAO.getByEventName(eventName);
            final Participant participant = participantManager
                    .getParticipant(participantName);
            Set<EventParticipant> eventParticipants = event.getEventParticipants();
            EventParticipant eventParticipant = null;
            for (EventParticipant next : eventParticipants) {
                Participant participant1 = next.getParticipant();
                if (participant1.getParticipantName().equals(participantName) && !participant1.isClient()) {
                    eventParticipant = next;
                    break;
                }
            }

            List<BidEntry> bidEntries = dbpDA.getCurrentBid(programName, event,
                    participant, isClient);
            NotificationMethod notificationMethod = NotificationMethod
                    .getInstance();
            dbpDA.setBidAcknowledged(programName, event, participant, isClient,
                    true, eventParticipant);
            
            if (accepted) {
                dbpDA.setBidAccepted(programName, event, participant, isClient,
                        accepted, eventParticipant);
                processAcceptedBid(programName, eventName, participantName,
                        isClient);
                if (event.getProgramName().startsWith("DBP")) {
                	sendDBPAcceptNotification(programName, bidEntries, accepted, notificationMethod, (DBPEvent) event, participant);
                } else {
                	List<EventParticipant> participants = event.getParticipants();
                	for (EventParticipant ep : participants) {
                        Participant client = ep.getParticipant();
                        if (participantName.equals(client.getParent())) {
                            sendAcceptNotification(programName, client,
                                    bidEntries, true, notificationMethod, (DBPEvent) event, participant);
                        }
                    }
                }
            } else {
                dbpDA.setBidDeclined(programName, eventName, participantName, !accepted, eventParticipant);
                removeEventParticipantSignals(eventName, participantName);
                List<EventParticipant> participants = event.getParticipants();
                for (EventParticipant ep : participants) {
                    Participant client = ep.getParticipant();
                    if (participantName.equals(client.getParent())) {
                        sendAcceptNotification(programName, client,
                                bidEntries, false, notificationMethod, (DBPEvent) event, participant);
                    }
                }
            }
            eventEAO.update(event);
           // updateCache(event);//cache is getting updated when event states change, as per the design, so no need to update cache after receiving signals.
        } catch (EntityNotFoundException e) {
            log.info("Error in Bid Acceptance", e);
        } catch (Exception e) {
            log.info("Error in Bid Acceptance", e);
        }
    }

    private void removeEventParticipantSignals(
            String eventName, String participantName) {
        /*
         * This is not working due to a hibernate bug:
         * http://opensource.atlassian.com/projects/hibernate/browse/HHH-4145
         */
        /*
         * final Query query =
         * em.createNamedQuery("SignalEntry.deleteByEventParticipant")
         * .setParameter("eventName", eventName)
         * .setParameter("participantName", participantName);
         * query.executeUpdate();
         */
        List<Participant> participants = participantEAO.findClientsByParticipant(participantName);
        for (Participant p : participants) {
            EventParticipant ep = eventParticipantEAO.getEventParticipant(eventName, p.getParticipantName(), p.isClient());
            if (ep != null) {
                for (EventParticipantSignal signal : ep.getSignals()) {
                    signal.getEventParticipantSignalEntries().clear();
                }
            }
        }
    }

    /**
     * Sets the bid state.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     * @param newBidState
     *            the new bid state
     */
    private void setBidState(String programName, DBPEvent event,
            BidState newBidState) {
        log.trace(LogUtils.createLogEntry(
                programName,
                LogUtils.CATAGORY_EVENT,
                "setting bid state to " + newBidState + ": "
                        + event.getEventName(), null));

        dbpDA.updateEventState(programName, event.getEventName(), newBidState);

        switch (newBidState) {
        case IDLE:
            break;

        case ACCEPTING:
            // TODO: bids is now processed through ProgramDataAccess,
            // so bid can be updated by user at any time.
            // we need the ability to open bidding and close bidding

            createParticipantCurrentBids(programName, event);

            break;

        case MISSED_BIDDING:
            // TODO: send notification to operators
            break;

        case SENDING_TO_UTILITY:
            final Program programVO = programManager.getProgramOnly(programName);
            if (event.isManual()) {
                
                log.debug(LogUtils.createLogEntry(
                        programName,
                        LogUtils.CATAGORY_EVENT,
                        "operator issued event - accepting all bids: "
                                + event.getEventName(), null));

                Map<String, List<BidEntry>> bidMap = getBidsMap(event);
                String subject = systemManager.getPss2Properties()
                        .getUtilityDisplayName()
                        + " "
                        + event.getProgramName()
                        + " event "
                        + event.getEventName()
                        + " was operator issued - accepting all bids";

                sendDRASOperatorEventNotification(subject, bidMap.toString(),
                        NotificationMethod.getInstance(),
                        new NotificationParametersVO(), event,notifier);
//                notifier.sendNotification(programVO.getOperatorContactList(),
//                        "operator", subject, bidMap.toString(),
//                        NotificationMethod.getInstance(),
//                        new NotificationParametersVO(),
//                        Environment.isAkuacomEmailOnly(),
//                        event.getProgramName());

                // if this is operator defined, just accept all the bids
                for (EventParticipant eventParticipant : getEventParticipantsByEvent(event)) {
                    Participant participant = eventParticipant.getParticipant();
                    if (!participant.isClient()) {
                        setBidAccepted(event.getProgramName(), event.getEventName(),
                                participant.getParticipantName(), participant.isClient(), true);
                    }
                }
                setBidState(programName, event, BidState.PROCESSING_COMPLETE);
            } else {
                // this was sent to us from the utility, so send them the bids
                if (sendBidsOut(event)) {
                    if (programVO.getAutoAccept()) {
                        // auto accept all bids
                        for (EventParticipant eventParticipant : getEventParticipantsByEvent(event)) {
                            Participant participant = eventParticipant.getParticipant();
                            if (!participant.isClient()) {
                                setBidAccepted(event.getProgramName(), event.getEventName(),
                                        participant.getParticipantName(), participant.isClient(), true);
                            }
                        }
                        setBidState(programName, event, BidState.PROCESSING_COMPLETE);
                    } else {
                        setBidState(programName, event, BidState.WAITING_FOR_ACCEPTANCE);
                    }
                } else {
                    // TODO: retry after one minute and keep retrying until it
                    // succeeds or we are forced to cancel the event because a
                    // tranistion needs to occur (this should be done on a
                    // paritcipant by partipant basis).
                    
                    log.warn(LogUtils.createLogEntry(
                            programName,
                            LogUtils.CATAGORY_EVENT,
                            "couldn't send bids to utility: "
                                    + event.getEventName(), null));
                    setBidState(programName, event,
                            BidState.SENDING_TO_UTILITY_FAILED);
                }
            }
            break;
        case SENDING_TO_UTILITY_FAILED:
            // wait for external operation
            break;
        case MISSED_RESPOND_BY:
            // TODO: send notification to operators
            break;
        case WAITING_FOR_ACCEPTANCE:
            break;
        case PROCESSING_COMPLETE:
            processUnacknowledgedBids(programName, event);
            break;
        }
    }

    /**
     * Process bid state.
     * 
     * @param currentTime
     *            the current time
     * @param programName
     *            the program name
     * @param event
     *            the event
     */
    private void processBidState(long currentTime, String programName,
            DBPEvent event) {
        switch (event.getCurrentBidState()) {
        case IDLE:
            if (currentTime > (event.getDrasRespondBy().getTime())) {
                // we've missed the bidding period, so we should just log the
                // error and delete the event
                
                log.warn(LogUtils.createLogEntry(programName,
                        LogUtils.CATAGORY_EVENT,
                        "dras respond by period passed before processing: "
                                + event.getEventName(), null));
                setBidState(programName, event, BidState.MISSED_BIDDING);
            } else {
                // TODO: this assumes we aren't getting called until after
                // issue time. should probably confirm this here.
                setBidState(programName, event, BidState.ACCEPTING);
            }
            break;
        case MISSED_BIDDING:
            // DRMS-6410
            if (currentTime > event.getStartTime().getTime()) {
                setBidState(programName, event, BidState.PROCESSING_COMPLETE);
            }
            break;
        case ACCEPTING:
            if (currentTime > event.getRespondBy().getTime()) {
                // we've missed the respond by time, so we should just log the
                // error and delete the event
                
                log.warn(LogUtils.createLogEntry(
                        programName,
                        LogUtils.CATAGORY_EVENT,
                        "respond by period passed before processing: "
                                + event.getEventName(), null));
                setBidState(programName, event, BidState.MISSED_RESPOND_BY);
            } else if (currentTime > (event.getDrasRespondBy().getTime())) {
                setBidState(programName, event, BidState.SENDING_TO_UTILITY);
            }
        case MISSED_RESPOND_BY:
            // DRMS-6410
            if (currentTime > event.getStartTime().getTime()) {
                setBidState(programName, event, BidState.PROCESSING_COMPLETE);
            }
            break;
        case SENDING_TO_UTILITY:
            break;
        case SENDING_TO_UTILITY_FAILED:
            break;
        case WAITING_FOR_ACCEPTANCE:
            // TODO: this needs to be smarter
            if (currentTime > event.getRespondBy().getTime()
                    + programManager.getProgramWithLoadBid(programName).getBidConfig()
                            .getAcceptTimeoutPeriodM() * 60 * 1000) {
                setBidState(programName, event, BidState.PROCESSING_COMPLETE);
            }
            break;
        case PROCESSING_COMPLETE:
            // the event is removed in ProgramEJBBean.tick5Seconds()
            break;
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.dbp.DBPProgramEJB#nextState(java.lang.String,
     * com.akuacom.pss2.core.model.DBPEvent)
     */
    public void nextState(String programName, DBPEvent event) {

        switch (event.getCurrentBidState()) {
        case IDLE:
            break;
        case MISSED_BIDDING:
            setBidState(programName, event, BidState.ACCEPTING);
            break;
        case ACCEPTING:
            break;
        case MISSED_RESPOND_BY:
            setBidState(programName, event, BidState.SENDING_TO_UTILITY);
            break;
        case SENDING_TO_UTILITY:
            break;
        case SENDING_TO_UTILITY_FAILED:
            break;
        case WAITING_FOR_ACCEPTANCE:
            break;
        case PROCESSING_COMPLETE:
            break;
        }
    }

    /**
     * Builds the notifcation header.
     * 
     * @param message
     *            the message
     * @param event
     *            the event
     * @param accountNumber
     *            the account number
     * @param responded
     */
    private void buildNotifcationHeader(StringBuffer message, DBPEvent event,
                                        String accountNumber, boolean responded) {
        message.append("Meter(s):\t");
        message.append(accountNumber);
        message.append("\n");

        message.append("Event ID:\t");
        message.append(event.getEventName());
        message.append("\n");

        message.append("Program:\t");
        message.append(event.getProgramName());
        message.append("\n");

        message.append("Type:\t\tVoluntary participation\n");
        message.append("Settlement:\tBaseline - Actual\n\n");

        message.append("Event Date:\t");
        message.append(new SimpleDateFormat(DATE_ONLY_FORMAT).format(event
                .getStartTime()));
        message.append("\n");

        message.append("Start:\t");
        message.append(event.getStartTime());
        message.append("\n");

        message.append("End:\t\t");
        message.append(event.getEndTime());
        message.append("\n\n");

        if (!responded) {
            message.append("Respond By:\t");
            message.append(event.getRespondBy());
            message.append("\n\n");
        }
    }

    @Override
	protected void sendParticipantIssuedNotifications(Event event, String verb) {
        DBPEvent dbpEvent = (DBPEvent) event;

        DBPProgram program = (DBPProgram) programManager.getProgramOnly(dbpEvent
                .getProgramName());

        // send out notifications to all participants
        NotificationMethod notificationMethod = NotificationMethod
                .getInstance();
        if (program.getProgramName().startsWith("DBP")) {
            notificationMethod.setMedia(NotificationMethod.ENVOY_MESSAGE);
        } else {
            notificationMethod.setMedia(NotificationMethod.MEDIA_AKUA_MAIL);
        }
        notificationMethod.setEmail(true);
        notificationMethod.setFax(true);
        notificationMethod.setEpage(true);
        notificationMethod.setVoice(true);
        notificationMethod.setSms(true);

        NotificationParametersVO notificationParameters = new NotificationParametersVO();
        notificationParameters.setEventId(dbpEvent.getEventName());
        notificationParameters.setProgramName(program.getNotificationParam1());
        notificationParameters.setEventStartDate(dbpEvent.getStartTime());
        notificationParameters.setEventEndDate(dbpEvent.getEndTime());
        notificationParameters.setTimeZone(TimeZone.getDefault()
                .getDisplayName(
                        TimeZone.getDefault().inDaylightTime(new Date()),
                        TimeZone.SHORT));
        notificationParameters.setRespondBy(dbpEvent.getDrasRespondBy());
        notificationParameters
                .setEntries(dbpEvent.getBidBlocks());
        notificationParameters.setUnitPrice(program.isMustIssueBDBE() ? "0.5"
                : "0.6");

        for (EventParticipant eventParticipant : getEventParticipantsByEvent(dbpEvent)) {
            Participant participant = eventParticipant.getParticipant();
            String meterName = participant.getMeterName();
            if (meterName == null || meterName.equals("")) {
                if (participant.isClient()) {
                    meterName = participant.getParent();
                } else {
                    meterName = participant.getParticipantName();
                }
            }
            notificationParameters.setMeterName(meterName);
            sendIssuedNotification(dbpEvent, eventParticipant,
                    notificationMethod, notificationParameters);
        }
    }

    /**
     * Send issued notification.
     * 
     * @param event
     *            the event
     * @param eventParticipant
     *            the event participant
     * @param notificationMethod
     *            the notification method
     * @param notificationParameters
     *            the notification parameters
     */
    private void sendIssuedNotification(DBPEvent event,
            EventParticipant eventParticipant,
            NotificationMethod notificationMethod,
            NotificationParametersVO notificationParameters) {

        String subject = event.getProgramName() + " event issued to "
                + systemManager.getPss2Properties().getUtilityDisplayName()
                + " participant "
                + eventParticipant.getParticipant().getParticipantName();

        StringBuffer message = new StringBuffer();

        buildNotifcationHeader(message, event, eventParticipant
                .getParticipant().getAccountNumber(), false);

        message.append("PRICES:\n");
        // make the price list event time aware.
        List<EventBidBlock> bidBlocks = event.getBidBlocks();
        for (EventBidBlock eventBidBlock : bidBlocks) {
            int start = eventBidBlock.getStartTime() / 100;
            int end = eventBidBlock.getEndTime() / 100;
            message.append(start).append(":00 to ").append(end).append(":00 at 0.5 $/KWH\n");
        }

        Participant participant = eventParticipant.getParticipant();

        for (ParticipantContact pc : participant.getContacts()) {
            if (pc == null
                    || !wantsParticipantEventNotification(eventParticipant, pc))
                continue;
            // use another interface?
            notifier.sendNotification(pc.getParticipantContactAsContact(), 
            		eventParticipant.getParticipant().getParticipantName(), 
            		subject, message.toString(), null,
                    notificationMethod, notificationParameters, 
                    Environment.isAkuacomEmailOnly(), true, false, 
                    event.getProgramName());
        }
    }
    
    private void sendDBPIssuedNotification(DBPEvent event,
            EventParticipant eventParticipant,
            NotificationMethod notificationMethod,
            NotificationParametersVO notificationParameters, String subject, StringBuffer message) {

        buildNotifcationHeader(message, event, eventParticipant
                .getParticipant().getAccountNumber(), false);

        message.append("PRICES:\n");
        // make the price list event time aware.
        List<EventBidBlock> bidBlocks = event.getBidBlocks();
        for (EventBidBlock eventBidBlock : bidBlocks) {
            int start = eventBidBlock.getStartTime() / 100;
            int end = eventBidBlock.getEndTime() / 100;
            message.append(start).append(":00 to ").append(end).append(":00 at 0.5 $/KWH\n");
        }

        Participant participant = eventParticipant.getParticipant();

        for (ParticipantContact pc : participant.getContacts()) {
            if (pc == null
                    || !wantsParticipantEventNotification(eventParticipant, pc))
                continue;
            // use another interface?
            notifier.sendNotification(pc.getParticipantContactAsContact(), 
            		eventParticipant.getParticipant().getParticipantName(), 
            		subject, message.toString(), null,
                    notificationMethod, notificationParameters, 
                    Environment.isAkuacomEmailOnly(), true, false, 
                    event.getProgramName());
        }
    }

    /**
     * Send accept notification.
     *
     * @param programName
     *            the program name
     * @param client
     * @param bidEntries
     *            the bid entries
     * @param accepted
 *            the accepted
     * @param notificationMethod
*            the notification method
     * @param event
     * @param participant
     */
    private void sendAcceptNotification(String programName,
                                        Participant client,
                                        List<BidEntry> bidEntries, boolean accepted,
                                        NotificationMethod notificationMethod, DBPEvent event, Participant participant) {
        String subject = (accepted ? "ACCEPTED " : "REJECTED ") + programName
                + " bids for "
                + systemManager.getPss2Properties().getUtilityDisplayName()
                + " participant " + participant.getParticipantName();

        StringBuffer message = new StringBuffer();

        buildNotifcationHeader(message, event, participant.getAccountNumber(), true);

        message.append("BID:\n");
        for (BidEntry bidEntry : bidEntries) {
            if (bidEntry.isActive()) {
                message.append(bidEntry.getBlockStart());
                message.append(" to ");
                message.append(bidEntry.getBlockEnd());
                message.append(": ");
                message.append(bidEntry.getReductionKW());
                message.append(" KW\n");
            }
        }

        for (ParticipantContact pc : client.getContacts()) {
            if (pc == null || pc.getEventNotification() ==
                    ContactEventNotificationType.NoNotification) {
                continue;
            }
            notifier.sendNotification(pc.getParticipantContactAsContact(),
                    client.getParticipantName(),
                    subject, message.toString(), null, notificationMethod,
                    new NotificationParametersVO(),
                    Environment.isAkuacomEmailOnly(), true, false,
                    programName);
        }
    }
    
    private void sendDBPAcceptNotification(String programName,
            List<BidEntry> bidEntries, boolean accepted,
            NotificationMethod notificationMethod, DBPEvent event, Participant participant) {
		
    	String subject = (accepted ? "ACCEPTED " : "REJECTED ") + programName
			+ " bids for "
			+ systemManager.getPss2Properties().getUtilityDisplayName()
			+ " participant " + participant.getParticipantName();
			
    	StringBuffer message = new StringBuffer();
    	buildNotifcationHeader(message, event, participant.getAccountNumber(), true);
			
    	message.append("BID:\n");
    	for (BidEntry bidEntry : bidEntries) {
    		if (bidEntry.isActive()) {
    			message.append(bidEntry.getBlockStart());
    			message.append(" to ");
    			message.append(bidEntry.getBlockEnd());
    			message.append(": ");
    			message.append(bidEntry.getReductionKW());
    			message.append(" KW\n");
    		}
    	}
    	DBPEvent dbpEvent = (DBPEvent) event;
    	sendEnvoyAcceptNotification(dbpEvent, subject, message, participant);
    }

    /**
     * Send bids out.
     * 
     * @param event
     *            the event
     * 
     * @return true, if successful
     * 
     * @throws NumberFormatException
     *             the number format exception
     */
    public boolean sendBidsOut(DBPEvent event) throws NumberFormatException {

        // TODO: this is async, but we need to capture the return value
        // and make sure that the operator gets it
        Map<String, List<BidEntry>> bidMap = getBidsMap(event);
        Map<String, List<Double>> reductionMap = new HashMap<String, List<Double>>();
        Map<String, List<Boolean>> activeMap = new HashMap<String, List<Boolean>>();
        Map<String, Integer> adjustmentMap = new HashMap<String, Integer>();
        Set<String> keys = bidMap.keySet();
        for (String key : keys) {
            List<BidEntry> list = bidMap.get(key);
            List<Double> listD = new ArrayList<Double>();
            List<Boolean> listB = new ArrayList<Boolean>();
            for (BidEntry bidEntry : list) {
                listD.add(bidEntry.getReductionKW());
                listB.add(bidEntry.isActive());
            }
            reductionMap.put(key, listD);
            activeMap.put(key, listB);
        }
        for (EventParticipant eventParticipant : getEventParticipantsByEvent(event)) {
        	 final Participant participant = eventParticipant.getParticipant();
        	 adjustmentMap.put(participant.getAccountNumber(), eventParticipant.getApplyDayOfBaselineAdjustment());
        	
        }
        PSS2Properties pss2Properties = systemManager.getPss2Properties();
        String subject = pss2Properties.getUtilityDisplayName() + " "
                + event.getProgramName() + " event " + event.getEventName()
                + " bid submission ";
        String body = event.toString() + "\nBids:\n" + bidMap.toString();

        String eventName = event.getEventName();
        String[] strings = eventName.split("-");

        ItronClientService client = EJB3Factory.getBean(ItronClientService.class, "itron");
        if(!client.sendBids(pss2Properties.getItronWSHostname(), new Integer(strings[0]), reductionMap, activeMap,adjustmentMap))
        {
            subject += "failed";
            
            log.warn(LogUtils.createLogEntry(event.getProgramName(),
                    LogUtils.CATAGORY_EVENT, subject, body));
        } else {
            subject += "succeeded";
            
            log.debug(LogUtils.createLogEntry(event.getProgramName(),
                    LogUtils.CATAGORY_EVENT, subject, body));
        }

        sendDRASOperatorEventNotification(subject, body,
                NotificationMethod.getInstance(),
                new NotificationParametersVO(), event,notifier);
        notifier.sendNotification(
                programManager.getOperatorContacts(event.getProgramName()),
                         "operator", subject, body,
                NotificationMethod.getInstance(),
                new NotificationParametersVO(), Environment
                        .isAkuacomEmailOnly(), event.getProgramName());
        return true;
    }

    @Override
    protected void processEvent(Event event, long nowMS) {
        super.processEvent(event, nowMS);
        String eventName = event.getEventName();
        if (event instanceof DBPEvent) {
            event = eventManager.getEventAll(eventName);
            DBPEvent dbpEvent = (DBPEvent) event;
            if (dbpEvent.getCurrentBidState() != BidState.IDLE
                    || (nowMS > dbpEvent.getIssuedTime().getTime() && nowMS < (dbpEvent
                            .getEndTime().getTime()))) {
                processBidState(nowMS, event.getProgramName(), dbpEvent);
            }
        }
    }

    /**
     * Get bids for each participant and create a pojo that itron client
     * recognizes.
     * 
     * @param event
     *            PGE event object
     * 
     * @return a map with account id and a list of bids related to it pair
     */
    private Map<String, List<BidEntry>> getBidsMap(DBPEvent event) {
        Map<String, List<BidEntry>> map = new HashMap<String, List<BidEntry>>();

        for (EventParticipant eventParticipant : getEventParticipantsByEvent(event)) {
            final Participant participant = eventParticipant.getParticipant();
            if (participant.isClient()) {
                continue;
            } 
            Set<ProgramParticipant> programParticipants = participant.getProgramParticipants();	
            boolean isAggregator = false;
			for(ProgramParticipant p: programParticipants) {					 
				Set<ProgramParticipant> descendants = ppManager.getDescendants(p);
				if(!descendants.isEmpty()) {
					isAggregator = true;
					break;
				}				
			}			
			if(isAggregator) {
				continue;
			}
            final String accountId = participant.getAccountNumber();
            final List<BidEntry> bids = dbpDA.getCurrentBid(
                    event.getProgramName(), event, participant,
                    participant.isClient());
            final List<BidEntry> results = new ArrayList<BidEntry>();
            for (BidEntry bidEntry : bids) {
                final long startTime = event.getStartTime().getTime();
                final long endTime = event.getEndTime().getTime();
                if ((bidEntry.getBlockStart().getTime() >= startTime) &&
                        (bidEntry.getBlockEnd().getTime() <= endTime)) {
                    boolean added = false;
                    for (int i = 0, resultsSize = results.size(); i < resultsSize; i++) {
                        BidEntry item = results.get(i);
                        if (item.getBlockStart().after(bidEntry.getBlockStart())) {
                            results.add(i, bidEntry);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        results.add(bidEntry);
                       
                    }
                }
            }
            map.put(accountId, results);
        }

        return map;
    }

    // set the response schedule (for later execution)
    /**
     * Process accepted bid.
     * 
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param participantName
     *            the participant name
     * 
     * @param isClient client flag
     * @throws EJBException
     *             the EJB exception
     */
    private void processAcceptedBid(String programName, String eventName,
            String participantName, boolean isClient) throws EJBException {
        DBPEvent event = (DBPEvent) getEvent(programName, eventName);

        try {
            if (!dbpDA.isBidAccepted(programName, event, participantName,
                    isClient)) {
                // TODO: assert
            }

            final Program program = programManager.getProgramWithParticipantsAndPRules(programName);

            EventManager eventManager = EJBFactory.getBean(EventManager.class);
            EventParticipant eventParticipant = eventManager
                    .getEventParticipant(event.getEventName(), participantName,
                            isClient);

            processParticipantRulesAndSignals(program, event, null,
                    eventParticipant, null, null, new Date(), null, null);
            eventEAO.update(event);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error(LogUtils.createLogEntry(event.getProgramName(),
                    LogUtils.CATAGORY_EVENT, "couldn't add ProgramSignals to "
                            + event.getEventName() + " for " + participantName,
                    ""));
        }
    }

    /**
     * Process unacknowledged bids.
     * 
     * @param programName
     *            the program name
     * @param event
     *            the event
     * 
     * @throws EJBException
     *             the EJB exception
     */
    private void processUnacknowledgedBids(String programName, DBPEvent event)
            throws EJBException {
        boolean unacknowledgedBids = false;
        StringBuilder sb = new StringBuilder();
        for (EventParticipant eventParticipant : getEventParticipantsByEvent(event)) {
            if (!dbpDA.isBidAcknowledged(programName, event,
                    eventParticipant.getParticipant().getParticipantName(),
                    eventParticipant.getParticipant().isClient())) {
                unacknowledgedBids = true;
                sb.append(eventParticipant.getParticipant().getAccountNumber());
                sb.append(" (");
                sb.append(eventParticipant.getParticipant()
                        .getParticipantName());
                sb.append(")\n");
            }
        }
        if (unacknowledgedBids) {
            sendDRASOperatorEventNotification(systemManager.getPss2Properties()
                    .getUtilityDisplayName()
                    + " "
                    + event.getProgramName()
                    + " event bids not acknowledged : " + event.getEventName(),
                    event.toString() + "\nAccounts:\n" + sb.toString(),
                    NotificationMethod.getInstance(),
                    new NotificationParametersVO(), event, notifier);
            notifier.sendNotification(
                    programManager.getOperatorContacts(programName),
                    "operator",
                    systemManager.getPss2Properties().getUtilityDisplayName()
                            + " " + event.getProgramName()
                            + " event bids not acknowledged : "
                            + event.getEventName(), event.toString()
                            + "\nAccounts:\n" + sb.toString(),
                    NotificationMethod.getInstance(),
                    new NotificationParametersVO(), Environment
                            .isAkuacomEmailOnly(), event.getProgramName());
        }
    }

    // check to see if the event is active for this signal for this participant
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJBBean#isParticipantEventSignalActive(java
     * .lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean isParticipantEventSignalActive(String programName,
            String eventName, String participantName, boolean isClient,
            String signalName) {
        try {
            Program program = programManager.getProgramOnly(programName);
            DBPEvent eventCore = (DBPEvent) eventEAO.getByEventAndProgramName(
                    eventName, programName);
            for (EventParticipant eventParticipant : getEventParticipantsByEvent(eventCore)) {
                if (eventParticipant.getParticipant().getParticipantName()
                        .equals(participantName)) {
                    long nowMS = new Date().getTime();
                    // only consider events that have been issued
                    if (nowMS < eventCore.getIssuedTime().getTime()) {
                        return false;
                    }
                    // only consider events for which processing is complete (
                    // i.e. bids have been accepted)
                    if (eventCore.getCurrentBidState() != BidState.PROCESSING_COMPLETE) {
                        return false;
                    }
                    if (signalName.equals("pending")) {
                        // only true if we are between the pending on time and
                        // the end time
                        if (nowMS > eventCore.getStartTime().getTime()
                                - program.getPendingLeadMS(eventCore)
                                && nowMS < eventCore.getEndTime().getTime()) {
                            return true;
                        }
                    } else if (signalName.equals("mode")) {
                        // only true if we are between the start time and
                        // the end time
                        if (nowMS > eventCore.getStartTime().getTime()
                                && nowMS < eventCore.getEndTime().getTime()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            String message = "isParticipantEventSignalActive failed";
            
            log.warn(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, message, "event = " + eventName
                            + ", participant = " + participantName
                            + "signalName = " + signalName));
            log.debug(LogUtils.createExceptionLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, e));

            throw new EJBException(message, e);
        }
    }

    @Override
    public String getEventStatusString(Event event1) {
        DBPEvent event = (DBPEvent) event1;

        if (event == null) {
            return "";
        }
        BidState bidState = event.getCurrentBidState();
        //Program program = programManager.getProgram(event.getProgramName());

        switch (bidState) {
        case IDLE:
            return "Received";
        case ACCEPTING:
            return "Bidding";
        case SENDING_TO_UTILITY:
            return "Processing";
        case WAITING_FOR_ACCEPTANCE:
            return "Processing";
        case PROCESSING_COMPLETE:
            final long startTime = event.getStartTime().getTime();
            final long now = System.currentTimeMillis();
            if (startTime < now) {
                return "Active";
            } else {
                return "Acked";
            }
        case MISSED_BIDDING:
            return "Missed Bidding";
        case MISSED_RESPOND_BY:
            return "Missed Respond By";
        case SENDING_TO_UTILITY_FAILED:
            return "Send Bids Failed";
        default:
            
            log.warn(LogUtils.createLogEntry(event.getProgramName(),
                    LogUtils.CATAGORY_EVENT,
                    "illegal state: " + event.getEventName(), null));
            return "INTERNAL ERROR";
        }
    }

    public Set<EventSignal> initializeEvent(Program program, Event event, UtilityDREvent utilityDREvent)
		throws ProgramValidationException
	{
		DBPEvent dbpEvent = (DBPEvent)event;
        DBPProgram dbpProgram = (DBPProgram)program;
        dbpEvent.setBidBlocks(DBPUtils.getEventBidBlocks(dbpProgram, dbpEvent));
        createBidEntries(dbpProgram, dbpEvent);
        createClientSignals(dbpProgram, dbpEvent);
        return super.initializeEvent(program, event, utilityDREvent);
	}

    private void createBidEntries(DBPProgram program, DBPEvent event) {
        List<EventParticipant> list = getEventParticipantsByEvent(event);
        for (EventParticipant ep : list) {
            Participant p = ep.getParticipant();
            if (!p.isClient()) {    // create bid entries only for participants
                List<BidEntry> defaultBids = dbpDA.getDefaultBid(program.getProgramName(), p.getParticipantName(), true);
                Set<EventParticipantBidEntry> set = ep.getBidEntries();
                if (set == null) {
                    set = new HashSet<EventParticipantBidEntry>();
                    ep.setBidEntries(set);
                } else {
                    set.clear();
                }
                for (BidEntry b : defaultBids) {
                    EventParticipantBidEntry e = new EventParticipantBidEntry();
                    boolean active = b.isActive();
                    e.setActive(active);
                    Date dateRef = event.getStartTime();
                    e.setEndTime(DateUtil.mergeDate(b.getBlockEnd(), dateRef));
                    e.setEventParticipant(ep);
                    if (!active) {
                        e.setPriceLevel(1.0);
                        e.setReductionKW(0.0);
                    } else {
                        e.setPriceLevel(b.getPriceLevel());
                        e.setReductionKW(b.getReductionKW());
                    }

                    e.setStartTime(DateUtil.mergeDate(b.getBlockStart(), dateRef));
                    set.add(e);
                }
            }
        }
    }

    private void createClientSignals(DBPProgram program, DBPEvent event) {
        // initialize the program signals as empty lists
        List<EventParticipant> eventParticipants = getEventParticipantsByEvent(event);
        for (EventParticipant eventParticipant : eventParticipants) {
            Participant participant = eventParticipant.getParticipant();
            if (participant.isClient()) {
                Set<EventParticipantSignal> eventParticipantSignals = eventParticipant.getSignals();
                for(ProgramSignal programSignal: program.getSignals()) {
                    EventParticipantSignal signal = new EventParticipantSignal();
                    signal.setSignalDef(programSignal.getSignalDef());
                    signal.setEventParticipant(eventParticipant);
                    eventParticipantSignals.add(signal);
                }
            }
        }
    }

    private List<EventParticipant> getEventParticipantsByEvent(DBPEvent event) {
        return epEAO.findByEvent(event.getEventName());
    }

    @Override
    protected double getCurrentValue(String variableName,
            Set<? extends Signal> signals, long dateMS) {
        double result = 0.0;
        for (Signal signal : signals) {
            // find the signal that matches the variable (numeric signals only)
            SignalDef signalDef = signal.getSignalDef();
            if (signalDef.getSignalName().equals(variableName)
                    && !signalDef.getType().equals(EventInfoInstance.SignalType.LOAD_LEVEL.name())) {
                List<SignalEntry> signalEntryList = new ArrayList<SignalEntry>(signal.getSignalEntries());
                for (SignalEntry entry : signalEntryList) {
                    if (dateMS == entry.getTime().getTime()) {
                        result = entry.getNumberValue();
                    }
                }
                break;
            }
        }
        return result;
    }
    
    @Override
    protected void sendDRASOperatorNotifications(Event event, String subject) {
        String fullSubject = systemManager.getPss2Properties()
                .getUtilityDisplayName()
                + " program "
                + event.getProgramName()
                + " event " + event.getEventName() + " " + subject;
        StringBuilder sb = new StringBuilder();
        if ("issued".equals(subject) || "started".equals(subject)) {
            for (EventParticipant eventParticipant : event.getParticipants()) {
                if (!eventParticipant.getParticipant().isClient()
                        || eventParticipant.getParticipant().getType() == Participant.TYPE_MANUAL) {
                    continue;
                }
                Participant participant = eventParticipant.getParticipant();

                if (participant.getClientStatus() == ClientStatus.OFFLINE) {
                    if (participant.isManualControl()) {
                        sb.append(eventParticipant.getParticipant()
                                .getParticipantName());
                        sb.append(" is OFFLINE and in MANUAL mode\n");
                    } else {
                        sb.append(eventParticipant.getParticipant()
                                .getParticipantName());
                        sb.append(" is OFFLINE\n");
                    }
                } else if (participant.isManualControl()) {
                    sb.append(eventParticipant.getParticipant()
                            .getParticipantName());
                    sb.append(" is in MANUAL mode\n");
                }
            }
        }
        sb.append(event.toOperatorString());

        if ("issued".equals(subject)) {
            try{
            	List<OptedOutClientList> optOutList = this.getNativeQueryManager().getOptOutClients(event.getProgramName());

            		long optOutSize = optOutList.size();
            	
    	            if(optOutSize > 0 )
    	            {
    	            	sb.append("\nOpted out from Program:\n");
    	            	
    	    	        for(OptedOutClientList List: optOutList)
    	    	        {
    	    	        	sb.append(List.getParticipantName());
    	                    sb.append("\n");
    	                }
    	            }

    			}
            catch(Exception e){
            	log.error("Failed to retrieve program Optout participants ",e);
            	}
            } 
        
        if (event.getWarnings()!=null && event.getWarnings().size()>0) {
	        for (ProgramValidationMessage msg: event.getWarnings()) {
	        	if (msg.getParameterName().equals("ITORN_INVALID_ACCOUNT")) {
	        		sb.append("\nWarnings:\n");
	        		sb.append(msg.getDescription() + "\n");
	        	}
	        }
        }
        sendDRASOperatorEventNotification(fullSubject, sb.toString(),
                NotificationMethod.getInstance(),
                new NotificationParametersVO(), event,notifier);
    }
    @Override
	public void sendDBPEnvoyNotification(Event event, String subject, boolean showClientStatus, boolean isRevision) {
    	sendDBPEnvoyCancelNotification(event, null, null, showClientStatus, isRevision);
    }
        
    private void sendDBPEnvoyCancelNotification(Event event, String subject, StringBuffer message, boolean showClientStatus, boolean isRevision) {
        DBPEvent dbpEvent = (DBPEvent) event;
        PSS2Properties pss2Props = systemManager.getPss2Properties();
        
        DBPProgram program = (DBPProgram) programManager.getProgramOnly(dbpEvent
                .getProgramName());

        // send out notifications to all participants
        NotificationMethod notificationMethod = NotificationMethod
                .getInstance();
        notificationMethod.setMedia(NotificationMethod.ENVOY_MESSAGE);
        notificationMethod.setEmail(true);
        notificationMethod.setFax(true);
        notificationMethod.setEpage(true);
        notificationMethod.setVoice(true);
        notificationMethod.setSms(true);
        
        NotificationParametersVO notificationParameters = new NotificationParametersVO();
        notificationParameters.setEventId(dbpEvent.getEventName());
        notificationParameters.setEventCondition(EVENT_CANCEL_CONDITION_VAL);
        notificationParameters.setTheme(EVENT_CANCEL_THEME_VAL);
        notificationParameters.setProgramName(program.getNotificationParam1());
        notificationParameters.setEventStartDate(dbpEvent.getStartTime());
        notificationParameters.setEventEndDate(dbpEvent.getEndTime());
        notificationParameters.setTimeZone(TimeZone.getDefault()
                .getDisplayName(
                        TimeZone.getDefault().inDaylightTime(new Date()),
                        TimeZone.SHORT));
        notificationParameters.setRespondBy(dbpEvent.getDrasRespondBy());
        notificationParameters
                .setEntries(dbpEvent.getBidBlocks());
        notificationParameters.setUnitPrice(program.isMustIssueBDBE() ? "0.5"
                : "0.6");
        for (EventParticipant eventParticipant : event.getParticipants()) {
            Participant participant = eventParticipant.getParticipant();
            String meterName = participant.getMeterName();
            if (meterName == null || meterName.equals("")) {
                if (participant.isClient()) {
                    meterName = participant.getParent();
                } else {
                    meterName = participant.getParticipantName();
                }
            }
            notificationParameters.setMeterName(meterName);
            subject = cancelEventSubject(participant, showClientStatus, dbpEvent, pss2Props);
            message = cancelEventMessage(eventParticipant, dbpEvent, pss2Props, isRevision);
            sendDBPIssuedNotification(dbpEvent, eventParticipant,
                    notificationMethod, notificationParameters, subject, message);
        }
    }
    
    
    private void sendEnvoyAcceptNotification(Event event, String subject, StringBuffer message, Participant participant) {
        DBPEvent dbpEvent = (DBPEvent) event;
        PSS2Properties pss2Props = systemManager.getPss2Properties();
        
        DBPProgram program = (DBPProgram) programManager.getProgramOnly(dbpEvent
                .getProgramName());

        // send out notifications to all participants
        NotificationMethod notificationMethod = NotificationMethod
                .getInstance();
        notificationMethod.setMedia(NotificationMethod.ENVOY_MESSAGE);
        notificationMethod.setEmail(true);
        notificationMethod.setFax(true);
        notificationMethod.setEpage(true);
        notificationMethod.setVoice(true);
        notificationMethod.setSms(true);
        
        NotificationParametersVO notificationParameters = new NotificationParametersVO();
        notificationParameters.setEventCondition(BID_ACCEPT_CONDITION_VAL);
        notificationParameters.setTheme(BID_ACCEPT_THEME_VAL);
        notificationParameters.setEventId(dbpEvent.getEventName());
        notificationParameters.setProgramName(program.getNotificationParam1());
        notificationParameters.setEventStartDate(dbpEvent.getStartTime());
        notificationParameters.setEventEndDate(dbpEvent.getEndTime());
        notificationParameters.setTimeZone(TimeZone.getDefault()
                .getDisplayName(
                        TimeZone.getDefault().inDaylightTime(new Date()),
                        TimeZone.SHORT));
        notificationParameters.setRespondBy(dbpEvent.getDrasRespondBy());
        notificationParameters
                .setEntries(dbpEvent.getBidBlocks());
        notificationParameters.setUnitPrice(program.isMustIssueBDBE() ? "0.5"
                : "0.6");
        for (EventParticipant eventParticipant : event.getParticipants()) {
            Participant eventPart = eventParticipant.getParticipant();
            String parent = eventPart.getParent();
            if( parent !=null && parent.equalsIgnoreCase(participant.getParticipantName()) ) {
            	String meterName = eventPart.getMeterName();
                if (meterName == null || meterName.equals("")) {
                    if (participant.isClient()) {
                        meterName = participant.getParent();
                    } else {
                        meterName = participant.getParticipantName();
                    }
                }
                notificationParameters.setMeterName(meterName);
                sendDBPIssuedNotification(dbpEvent, eventParticipant,
                        notificationMethod, notificationParameters, subject, message);
            }
        }
    }
    
	private EventEmailFormatter getEventEmailFormatter(EventParticipant ep){
		String programName = ep.getEvent().getProgramName();
		String className = programManager.getProgramClassName(programName);
		if("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013".equalsIgnoreCase(className)){
			return new SceEventEmailFormatter("com.akuacom.pss2.core.SceEmailResourceUtil");
		}
			
		return new EventEmailFormatter();
	}
	
	private String cancelEventSubject(Participant participant, boolean showClientStatus, Event event, PSS2Properties pss2Props) {
		String stateString = "";
		String salutationString = "Your ";
		String eventLiteral = " ";
		String clientString = " Auto DR Client " + participant.getParticipantName() + " ";
		String subject = "";
		if (participant.getType() != Participant.TYPE_MANUAL
	             && showClientStatus) {
			if (participant.getStatus().intValue() == ClientStatus.OFFLINE
	                 .ordinal()) {
	         	stateString = " is offline and ";
	         	if (participant.isManualControl()) {
	                 //stateString = " is offline and in manual mode and ";
	         		salutationString = "";
	         		clientString = " ";
	         		eventLiteral = " event ";
	         		stateString = "";
	         	} 
	             //else {
	             //    stateString = " is offline and ";
	             //}
			} else if (participant.isManualControl()) {
	             //stateString = " is in manual mode and ";
				salutationString = "";
				clientString = " ";
				eventLiteral = " event ";
			} else if (participant.getStatus().intValue() == ClientStatus.ONLINE.ordinal()) {
				stateString = " is online and ";
	         }
		} else if (participant.getType() == Participant.TYPE_MANUAL) {
			salutationString = "";
			clientString = " ";
			eventLiteral = " event ";           	
		}
		subject = salutationString + pss2Props.getUtilityDisplayName()
	            + clientString + stateString + event.getProgramName() + eventLiteral + " has been cancelled";
		return subject;
	}
	
	private StringBuffer cancelEventMessage(EventParticipant eventParticipant, Event event, PSS2Properties pss2Props, boolean isRevision) {
		String emailContentType = pss2Props.getEmailContentType();
		String serverHost = pss2Props
                .getStringValue(PSS2Properties.PropertyName.SERVER_HOST);
		EventEmailFormatter mailFactory = getEventEmailFormatter(eventParticipant);
		final Set<EventParticipantSignal> participantSignals = eventParticipant
	               .getSignals();
		Set<EventSignal> eventSignals = eventParticipant.getEvent()
	               .getEventSignals();
		List<Signal> combinedSignals = new ArrayList<Signal>();
		combinedSignals.addAll(eventSignals);
		combinedSignals.addAll(participantSignals);
		StringBuffer emailContent = new StringBuffer(mailFactory.generateEmailContent(event,
	               combinedSignals, serverHost, emailContentType,
	               isRevision, null,null));
		
		return emailContent;
	}

}
