/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.core.ProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.annotations.EpicMethod;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.contact.ContactsOfflineError;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSetEAO;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventEmailFormatter;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.history.CustomerReportManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.aggregator.eao.ProgramAggregatorEAOManager;
import com.akuacom.pss2.program.apx.aggregator.eao.ApxAggregatorEAOManager;
import com.akuacom.pss2.program.bidding.BidBlock;
import com.akuacom.pss2.program.bidding.BidConfig;
import com.akuacom.pss2.program.dbp.DBPBidProgramEJB;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.query.OptedOutClientList;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.topic.TopicPublisher;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.util.ModeSignal;
import com.akuacom.utils.drw.CacheNotificationMessage;
import com.akuacom.utils.lang.DateUtil;
import com.akuacom.utils.lang.Dbg;
import com.akuacom.utils.lang.StackTraceUtil;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * Stateless session bean providing a facade for DRAS programs.
 */
public abstract class ProgramEJBBean extends TimerManagerBean implements ProgramEJB.R, ProgramEJB.L {

    public static final int TIMER_DAILY_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000;
    // 24 hours
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(ProgramEJBBean.class);
    /** The report manager. */
    @EJB
    protected ReportManager.L report;
    @EJB
    protected ParticipantManager.L participantManager;
    @EJB
    protected ProgramParticipantManager.L programParticipantManager;
    @EJB
    protected ProgramParticipantAggregationManager.L prgmPartAggManager;
    @EJB
    protected ParticipantEAO.L participantEAO;
    @EJB
    protected EventEAO.L eventEAO;
    @EJB
    protected EventParticipantEAO.L eventParticipantEAO;
    @EJB
    protected SystemManager.L systemManager;
    @EJB
    protected ClientManager.L clientManager;
    @EJB
    protected ProgramManager.L programManager;
    @EJB
    protected SignalManager.L signalManager;
    @EJB
    protected Notifier.L notifier;
    @EJB
    protected CustomerReportManager.L customerReportManager;
    @EJB
    protected EventManager.L eventManager;
    
    @EJB
    protected ClientEAO.L clientEAO;
    
    @EJB
    protected PDataSetEAO.L dataSetEAO;
    
    @EJB
    protected AsynchCaller.L asynchCaller;
    
    @Resource
    protected SessionContext context;

    @EJB
    protected TopicPublisher.L topicPublisher;
    @EJB
    private EventParticipantEAO.L epEAO;

    private List<OptedOutClientList> optOutProgramCandidates;
    private NativeQueryManager nativeQuery = null;
    
    @EJB
    private ProgramAggregatorEAOManager.L programAggregatorEAOManager;
    // this allows sub classes to create their own version of the Program
    // object (i.e. CPPProgramEJBBean can create a CPPPRogram object)
    /**
     * Creates the program object.
     * 
     * @return the program
     */
    public Program createProgramObject() {
        return new Program();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#loadProgram(java.util.Properties,
     * int)
     */
    @Override
    public Program loadProgram(Properties config, int i) {
        try {
            Program program = createProgramObject();

            program.setPriority(i);
            program.setProgramName(config.getProperty("p" + i + ".name"));
            program.setUtilityProgramName(config.getProperty("p" + i + ".utilityName"));
            Set<ProgramSignal> programSignals = new HashSet<ProgramSignal>();
            for (String signalName : config.getProperty("p" + i + ".signals").split(",")) {
                SignalDef signal = EJBFactory.getBean(SignalManager.class).getSignal(signalName);
                ProgramSignal programSignal = new ProgramSignal();
                programSignal.setProgram(program);
                programSignal.setSignalDef(signal);
                programSignals.add(programSignal);
            }

            program.setSignals(programSignals);
            program.setValidatorClass(config.getProperty("p" + i
                    + ".validatorClass"));
            program.setValidatorConfigFile(config.getProperty("p" + i
                    + ".validatorConfigFile"));
            program.setUiScheduleEventString(config.getProperty("p" + i
                    + ".uiScheduleEventString"));
            program.setUiConfigureProgramString(config.getProperty("p" + i
                    + ".uiConfigureProgramString"));
            program.setUiConfigureEventString(config.getProperty("p" + i
                    + ".uiConfigureEventString"));
//            program.setOperatorEMails(config.getProperty("p" + i
//                    + ".operatorEMails"));
            program.setMinIssueToStartM(new Integer(config.getProperty("p" + i
                    + ".minIssueToStartM")));
            program.setMustIssueBDBE(config.getProperty(
                    "p" + i + ".mustIssueBDBE").equals("yes"));
            program.setMaxIssueTime(config.getProperty("p" + i
                    + ".maxIssueTime"));
            program.setMinStartTime(config.getProperty("p" + i
                    + ".minStartTime"));
            program.setMaxStartTime(config.getProperty("p" + i
                    + ".maxStartTime"));
            program.setMinEndTime(config.getProperty("p" + i + ".minEndTime"));
            program.setMaxEndTime(config.getProperty("p" + i + ".maxEndTime"));
            program.setMinDurationM(new Integer(config.getProperty("p" + i
                    + ".minDurationM")));
            program.setMaxDurationM(new Integer(config.getProperty("p" + i
                    + ".maxDurationM")));
            program.setPendingTimeDBE(config.getProperty("p" + i
                    + ".pendingTimeDBE"));
            program.setNotificationParam1(config.getProperty("p" + i
                    + ".notificationParam1"));
            program.setManualCreatable(!"no".equals(config.getProperty("p" + i
                    + ".manualCreatable")));
            program.setAutoAccept("yes".equals(config.getProperty("p" + i
                    + ".autoAccept")));

            String longProgramName=config.getProperty("p"+i+".longProgramName");
            if (longProgramName!=null)
            	program.setLongProgramName(longProgramName);
            
            String programClass=config.getProperty("p"+i+".programClass");
            if (programClass!=null)
            	program.setProgramClass(programClass);
            
            String programGroup=config.getProperty("p"+i+".programGroup");
            if (programGroup!=null)
            	program.setProgramGroup(programGroup);
            
            String defaultDuration=config.getProperty("p"+i+".defaultDuration");
            if (defaultDuration!=null)
            	program.setDefaultDuration(Integer.valueOf(defaultDuration));
            
            
            final String numBlocks = config.getProperty("p" + i + ".numBlocks");
            if (numBlocks != null) {
                final BidConfig bidConfig = new BidConfig();
                bidConfig.setProgram(program);

                final Integer bidBlockTotal = new Integer(numBlocks);
                final Set<BidBlock> bidBlocks = new HashSet<BidBlock>(
                        bidBlockTotal);
                for (int j = 0; j < bidBlockTotal; j++) {
                    final BidBlock bb = new BidBlock();
                    bb.setBidConfig(bidConfig);
                    String time[] = config.getProperty(
                            "p" + i + ".b" + j + ".time").split(",");
                    String[] timeParts = time[0].split(":");
                    bb.setStartTimeH(new Integer(timeParts[0]));
                    bb.setStartTimeM(new Integer(timeParts[1]));
                    timeParts = time[1].split(":");
                    bb.setEndTimeH(new Integer(timeParts[0]));
                    bb.setEndTimeM(new Integer(timeParts[1]));
                    bidBlocks.add(bb);
                }
                bidConfig.setBidBlocks(bidBlocks);
                bidConfig.setMinBidKW(new Double(config.getProperty("p" + i
                        + ".minBidKW")));
                bidConfig.setDefaultBidKW(new Double(config.getProperty("p" + i
                        + ".defaultBidKW")));
                bidConfig.setMinConsectutiveBlocks(new Integer(config
                        .getProperty("p" + i + ".minConsectutiveBlocks")));
                bidConfig.setDrasBidding(config.getProperty(
                        "p" + i + ".isDrasBidding").equals("yes"));
                final String[] respondBy = config.getProperty(
                        "p" + i + ".respondByTime").split(":");
                bidConfig.setRespondByTimeH(new Integer(respondBy[0]));
                bidConfig.setRespondByTimeM(new Integer(respondBy[1]));
                bidConfig.setDrasRespondByPeriodM(new Integer(config
                        .getProperty("p" + i + ".drasRespondByPeriodM")));
                bidConfig.setAcceptTimeoutPeriodM(new Integer(config
                        .getProperty("p" + i + ".acceptTimeoutPeriodM")));
                program.setBidConfig(bidConfig);
            }

            return program;

        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /**
     * Called once for each program when the application starts up. 
     * 
     * @param programName
     *            program name
     */
    @Override
    public void initialize(String programName) {
       
    }
    
    /**
     * Will schedule a timer if program autoRepeatTimeOfDay is set
     * @param programName
     */
    public void createTimer(String programName) {
    	 javax.ejb.TimerService timerService = context.getTimerService();

        // create program specific timer
        Program prog = programManager.getProgramOnly(programName);
        if (prog != null && prog.getAutoRepeatTimeOfDay() != null) {
            
            String myTimerName = getClass().getSimpleName()+ "-" + programName;
            Collection<Timer> timers = timerService.getTimers();
	        boolean found = false;            
	        for (Timer timer : timers) {
	            if (myTimerName.equals(timer.getInfo())) {
                    log.debug("ProgramEJBBean_avoiding_duplicate_timer for " + programName);
	                found = true;
	                break;
	            }
	        }
            
            if (!found) {
                Date now = new Date();
                Calendar issueCal = new GregorianCalendar();
                Calendar nowCal = new GregorianCalendar();
                nowCal.setTime(now);
                issueCal.setTime(DateUtil.stripTime(now));
                Calendar xferCal = new GregorianCalendar();
                xferCal.setTime(prog.getAutoRepeatTimeOfDay());
                issueCal.add(Calendar.HOUR_OF_DAY,
                        xferCal.get(Calendar.HOUR_OF_DAY));
                issueCal.add(Calendar.MINUTE, xferCal.get(Calendar.MINUTE));

                if (nowCal.compareTo(issueCal) > 0) {
                    nowCal.add(Calendar.MINUTE, 2);
                    timerService.createTimer(nowCal.getTime(), getClass().getSimpleName()
                                    + "-" + programName);
                    issueCal.add(Calendar.DATE, 1);
                }

                log.debug("ProgramEJBBean_creating_timer for " + programName
                         + ". Set to fire first time at "
                         + issueCal.get(Calendar.HOUR_OF_DAY) + ":"
                         + issueCal.get(Calendar.MINUTE));
                timerService.createTimer(issueCal.getTime(),
                             TIMER_DAILY_REFRESH_INTERVAL_MS, myTimerName);
            }
         }
    }

    /**
     * Inheritable timeout handler. Calls processTimout which is the method that
     * should be overridden to customize timer functionality
     * 
     * @param timer
     */
    @EpicMethod
    @Timeout
    public void timeoutHandler(Timer timer) {
        processTimeout(timer);
    }

    /**
     * This method is called by the timer and should be overridden to customize
     * timer functionality
     * 
     * @param timer
     */
    public void processTimeout(Timer timer) {
    }  
    

    private void createEventParticipantClients(
		EventParticipant eventParticipant,
		Set<ProgramParticipant> programParticipants,
		Set<EventParticipant> resultParticipants)
	{
		// find the matching program participant for this client
		String participantName = 
			eventParticipant.getParticipant().getParticipantName();
		for (ProgramParticipant programParticipant : programParticipants)
		{
			if(programParticipant.getParticipant().isClient())
			{
				String parentParticipantName = 
					programParticipant.getParticipant().getParent();
		
				if (parentParticipantName.equals(participantName))
				{
					// if the client is active in the program
					if (programParticipant.getState() != 
						ProgramParticipant.PROGRAM_PART_DELETED)
					{
						// create the new event participant
						EventParticipant eventClient = new EventParticipant();
						eventClient.setEvent(eventParticipant.getEvent());
						eventClient.setParticipant(programParticipant.getParticipant());
						//programParticipant.getParticipant().getEventParticipants().add(eventClient);
						resultParticipants.add(eventClient);
					}
				}
			}
		}
	}

	public Set<EventParticipant> createAggregatorParticipant(Set<EventParticipant> eventParticipants, Event event){
    	Set<EventParticipant> eps=new HashSet<EventParticipant>();    	
    	List<Participant> parts=programParticipantManager.getParticipantsForProgramAsObject(event.getProgramName());
    	List<String> partAccounts=new ArrayList<String>();
    	for (EventParticipant ep:eventParticipants) {
    		partAccounts.add(ep.getParticipant().getAccountNumber());
    	}
    	
    	for (Participant part:parts) {
    		if (part.getAggregator() && !part.getTestParticipant() && 
    				!partAccounts.contains(part.getAccountNumber())) {
    			EventParticipant ep=new EventParticipant();
    			ep.setParticipant(part);
    			ep.setEvent(event);
    			eps.add(ep);
    		}
    	}
    	return eps;
    }

	public Set<EventParticipant> createEventClients(
		Set<EventParticipant> eventParticipants, Program programWithParticipants)
	{  
        Set<EventParticipant> eventClients = new HashSet<EventParticipant>();
		
        // add all the clients that are in the program
        for(EventParticipant eventParticipant: eventParticipants)
        {
//        	if (eventParticipant.isAggregator())
//        		continue;

        	createEventParticipantClients(eventParticipant,
        			programWithParticipants.getProgramParticipants(), eventClients);
        }        
        return eventClients;
	}
	
	protected ProgramParticipant findMatchingProgramParticipant(
		EventParticipant eventParticipant, Program program)
	{
        for (ProgramParticipant progPart : 
        	program.getProgramParticipants()) {
            if (progPart == null || progPart.getParticipant() == null) 
            {
                continue;
            }

            Participant participant = progPart.getParticipant();

            if (participant.getParticipantName().equals(
            	eventParticipant.getParticipant().getParticipantName()) && 
            	participant.isClient() == eventParticipant.getParticipant().isClient()) 
            {
                return progPart;
            }
        }
        return null;
	}

    public Set<EventParticipant> createAggregatedEventParticipants(
    	Set<EventParticipant> eventParticipants, Program program)
	{
    	// does nothing now per DRMS-6430
    	Set<EventParticipant> aggregatedEventParticipants = 
        	new HashSet<EventParticipant>();
        return aggregatedEventParticipants;
	}
	
    /**
     * Update event participants.
     * 
     * @param event
     *            the event
     */
    protected Set<EventParticipant> createEventParticipants(Event event, Program programWithParticipant) 
    {
        Set<EventParticipant> eventParticipants = event.getEventParticipants();

        Set<EventParticipant> resultParticipants = new HashSet<EventParticipant>();
        // if the event doesn't specify a list,
        if (eventParticipants != null ){
        	// only choose the participants that are in both lists
            // if a participant is in the program, move it to result list.
            List<Participant> extraParticipants = new ArrayList<Participant>();
            List<Participant> extraClients = new ArrayList<Participant>();
            for (EventParticipant eventParticipant : eventParticipants) {
                if (eventParticipant.getParticipant().isClient()) {
                    // can't specify clients in list
                    extraClients.add(eventParticipant.getParticipant());
                    continue;
                }

                ProgramParticipant matchingProgramParticipant = 
                		findMatchingProgramParticipant(eventParticipant, programWithParticipant);
                if (matchingProgramParticipant == null) {
                    extraParticipants
                            .add(eventParticipant.getParticipant());
                } else {
                	eventParticipant.setApplyDayOfBaselineAdjustment(matchingProgramParticipant.getApplyDayOfBaselineAdjustment());
                    resultParticipants.add(eventParticipant);
                }
            }
            if (!extraClients.isEmpty()) {
                String message = "event specifies clients";
                StringBuilder longDesc = new StringBuilder();
                for (Participant participant : extraClients) {
                    longDesc.append(participant.getParticipantName());
                    longDesc.append("\n");
                }
                // TODO 2992
                log.warn(LogUtils.createLogEntry(programWithParticipant.getProgramName(),
                        LogUtils.CATAGORY_EVENT, message,
                        longDesc.toString()));
            }
            // now log the extra orphan participants and throw an exception
            if (!extraParticipants.isEmpty()) {
                String message = 
                	"event specifies participants that aren't in program";
                StringBuilder longDesc = new StringBuilder();
                for (Participant participant : extraParticipants) {
                    longDesc.append(participant.getParticipantName());
                    longDesc.append("\n");
                }
                throw new ValidationException(message);
            }
         }

         return resultParticipants;
    }

    private void updateEventParticipants(Event event, 
    	Set<EventParticipant> eventParticipants)
    {
    	
    	event.setEventParticipants(eventParticipants);
    }

    /**
     * 
     * @param program
     *            the program
     * @param eventTiming
     *            event timing
     * @param programParticipant
     *            the participant name
     * 
     * @return the event default signals
     */
    protected List<EventParticipantRule> getEventParticipantRules(
            Program program, EventTiming eventTiming,
            ProgramParticipant programParticipant) {
        
        ArrayList<EventParticipantRule> eventPartRules =
            new ArrayList<EventParticipantRule>();

        // get them, sort them and add them as EventRuleEAOs to the results
        if (programParticipant.getProgramParticipantRules() != null
                && programParticipant.getProgramParticipantRules().size() > 0) {
            // make a list so we can sort them
            ArrayList<ProgramParticipantRule> progPartRules =
                    new ArrayList<ProgramParticipantRule>();
            progPartRules.addAll(programParticipant
                    .getProgramParticipantRules());
            Collections.sort(progPartRules, new Rule.SortOrderComparator());
            for (ProgramParticipantRule rule : progPartRules) {
                eventPartRules.add(new EventParticipantRule(rule, eventTiming
                        .getStartTime()));
            }
        }

        return eventPartRules;
    }

    
    protected List<EventParticipantRule> createEventParticipantRules(
            Program programWithPPandPRules, EventTiming eventTiming, String participantName,
            boolean isClient, UtilityDREvent utilityDREvent) {
        List<EventParticipantRule> eventRuleEAOs = new ArrayList<EventParticipantRule>();

        // Get - in order - program participant and program rules
        // Lists are coming in sorted order from the method calls
        // addAll appends to end of list
        int sortOrder = 0;
        
        ProgramParticipant programParticipant = null;
        for (ProgramParticipant pp : programWithPPandPRules.getProgramParticipants()) {
            if (pp.getParticipantName().equals(participantName)
                    && (pp.getParticipant().isClient()) && isClient) {
                programParticipant = pp;
                break;
            }
        }

        eventRuleEAOs.addAll(getEventParticipantRules(programWithPPandPRules, eventTiming,
                programParticipant));

        eventRuleEAOs.addAll(getProgramRules(programWithPPandPRules, eventTiming,
                utilityDREvent));

        // reset sort order for UI
        for (EventParticipantRule eventRuleEAO : eventRuleEAOs) {
            eventRuleEAO.setSortOrder(sortOrder++);
        }

        return eventRuleEAOs;
    }

    /**
     * Allow a program implementation to do any initial setup on one of its
     * events.
     * 
     * This method is often overridden by specialized program implementations.
     * 
     * NOTE: This is not the place to create Event-level input signals. That
     * should be done in getEventInputEventSignals
     */
    @Override
    public Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) throws ProgramValidationException {
        Set<EventSignal> emptySet = new HashSet<EventSignal>();
        return emptySet;
    }

    // the caller should supply t
    public Set<EventParticipantSignal> getClientForecastSignals(
            Program program, EventTiming eventTiming, String clientName,
            boolean isClient, Set<? extends Signal> inputSignals, Date now)
            throws ProgramValidationException {
        List<EventParticipantRule> eventClientRules = createEventParticipantRules(
                program, eventTiming, clientName, isClient,
                (UtilityDREvent) null);

        SignalDef pendingSignalDef = signalManager.getSignal("pending");
        SignalDef modeSignalDef = signalManager.getSignal("mode");
        return getClientOutputSignals(program, eventTiming, eventClientRules,
                inputSignals, now, pendingSignalDef, modeSignalDef);
    }

    /**
     * Returns the input signals that are associated with an entire program.
     * These are the signals that always apply to all clients in all events in
     * the program. An example might be a global CPP relative price.
     *
     * 
     * @param program
     * @return
     */
    @Override
    public Set<ProgramSignal> getProgramInputEventSignals(Program program) {
        //  TODO fix this
        return new HashSet<ProgramSignal>();
    }

    /**
     * Returns the signals that are associated with a specific participant.
     * These are the signals that apply only to clients associated with that
     * participant.
     * 
     * @param program
     * @return
     */
    protected Set<EventParticipantSignal> getParticipantInputEventSignals(
            Program program, Event event, UtilityDREvent utilityDREvent,
            Participant participant, boolean isClient, Date now)
            throws ProgramValidationException {
        //  TODO fix this
        return new HashSet<EventParticipantSignal>();
    }

    @Override
    public List<ProgramParticipantRule> createDefaultClientRules(Program program) {
        return new ArrayList<ProgramParticipantRule>();
    }

    /**
     * This method looks up program signals, generates a set of event signals
     * for one event, and passes the combination of those through client rules
     * to populate per-client signals for each participating client
     * 
     * It sets up all the levels of signals for an event. All programs will call
     * this at event creation time, and some programs may call it later to allow
     * for event signal revision (For example, for RTP programs where prices can
     * be revised during the event)
     * 
     * @param programWithPPAndPRules
     *            program
     * @param event
     *            event
     * @param utilityDREvent
     *            DocMe!
     * 
     * @throws ProgramValidationException
     *             validation message
     */
    protected void processRulesAndSignals(Program programWithPPAndPRules, Event event,
            UtilityDREvent utilityDREvent, Date now)
            throws ProgramValidationException {
        try {
            // get program level input signals (eg. program CPP price)
            Set<ProgramSignal> programInputSignals = getProgramInputEventSignals(programWithPPAndPRules);

            // get event level input signals (eg. RTP prices)
            Set<EventSignal> eventInputSignals = event.getEventSignals();
            
            EventStateCacheHelper esCacheForSignalDef = EventStateCacheHelper.getInstance();
            SignalDef pendingSignalDef = esCacheForSignalDef.getSignalDef("pending");
	        if(pendingSignalDef == null){
	        	pendingSignalDef = signalManager.getSignal("pending");
	        	esCacheForSignalDef.setSignalDef("pending", pendingSignalDef);
	        }

	        SignalDef modeSignalDef = esCacheForSignalDef.getSignalDef("mode");
	        if(modeSignalDef == null){
	        	modeSignalDef = signalManager.getSignal("mode");
	        	esCacheForSignalDef.setSignalDef("mode", modeSignalDef);
	        }

        for (EventParticipant eventParticipant : event
                    .getEventParticipants()) {
                if (!eventParticipant.getParticipant().isClient()) {

                    processParticipantRulesAndSignals(programWithPPAndPRules, event,
                            utilityDREvent, eventParticipant,
                            programInputSignals, eventInputSignals, now,
                            pendingSignalDef, modeSignalDef);
                }
            }
        } catch (Exception e) {
            String message = "can't process rules for event:" + event;
            log.error(message, e);
            throw new EJBException(message, e);
        }
    }

    @Override
    public void processParticipantRulesAndSignals(Program programWithPPAndPRules, Event event,
            UtilityDREvent utilityDREvent, EventParticipant eventParticipant,
            Set<ProgramSignal> programInputSignals,
            Set<EventSignal> eventInputSignals, Date now,
            SignalDef pendingSignalDef, SignalDef modeSignalDef)
            throws ProgramValidationException 
    {
        if (programInputSignals == null) {
        	//currently return empty set
            programInputSignals = getProgramInputEventSignals(programWithPPAndPRules);
        }

        if (eventInputSignals == null) {
            eventInputSignals = event.getEventSignals();
        }

        if(pendingSignalDef == null)
        {
              pendingSignalDef = signalManager.getSignal("pending");
        }
        if(modeSignalDef == null)
        {
        	modeSignalDef = signalManager.getSignal("mode");
        }
        
        //currently return empty set
        Set<EventParticipantSignal> participantInputSignals = getParticipantInputEventSignals(
        		programWithPPAndPRules, event, utilityDREvent,
                eventParticipant.getParticipant(), false, now);
        for (EventParticipantSignal signal : participantInputSignals) {
            signal.setEventParticipant(eventParticipant);
        }
        if (eventParticipant.getSignals() == null) {
            eventParticipant.setSignals(new HashSet<EventParticipantSignal>());
        } else {
            eventParticipant.incrementEventModNumber();
        }

        setParticipantSignals(eventParticipant.getSignals(), participantInputSignals);

        Set<? extends Signal> inputSignals = mergeInputSignals(
                programInputSignals, eventInputSignals, participantInputSignals);

        for (EventParticipant clientParticipant : event.getEventParticipants()) {
            if (clientParticipant.getParticipant().isClient()
                    && clientParticipant
                            .getParticipant()
                            .getParent()
                            .equals(eventParticipant.getParticipant()
                                    .getParticipantName())) {
                Set<EventParticipantSignal> updatedOutputSignals = processClientRulesAndSignals(
                		programWithPPAndPRules, event, utilityDREvent, eventParticipant,
                        clientParticipant, inputSignals, now,
                        pendingSignalDef, modeSignalDef);

                for (EventParticipantSignal esig : updatedOutputSignals) {
                    esig.setEventParticipant(clientParticipant);
                }

                if (clientParticipant.getSignals() == null) {
                    clientParticipant.setSignals(new HashSet<EventParticipantSignal>());
                } else {
                    clientParticipant.incrementEventModNumber();
                }
                setParticipantSignals(clientParticipant.getSignals(), updatedOutputSignals);


                /**
                 * If this is a push client, then push the event state now
                 */
                Participant theClient = clientParticipant.getParticipant();
                if (theClient.getPush() != 0 && 
                        event.getEventStatus() != EventStatus.NONE &&
                        event.getEventStatus() != EventStatus.RECEIVED) {
                   clientManager.pushClientEventState(theClient);
                }
            }
        }
    }

    private void pushEventClientSignals(Event event) {
        if (systemManager.getPss2Features().isIrrClientPushEnabled()) {
            for (EventParticipant clientParticipant : event.getEventParticipants()) {
                /**
                 * If this is a push client, then push the event state now
                 */
                Participant theClient = clientParticipant.getParticipant();
                if (theClient.getPush() != 0) {
                    clientManager.pushClientEventState(theClient);
                }
            }
        }
    }

    @Override
    public Set<EventParticipantSignal> processClientRulesAndSignals(
            Program programWithPPAndPRules, Event event, UtilityDREvent utilityDREvent,
            EventParticipant eventParticipant,
            EventParticipant clientParticipant,
            Set<? extends Signal> inputSignals, Date now,
            SignalDef pendingSignalDef, SignalDef modeSignalDef)
            throws ProgramValidationException {
        if (inputSignals == null) {
        	//currently return empty set
            Set<ProgramSignal> programInputSignals = getProgramInputEventSignals(programWithPPAndPRules);

            Set<EventSignal> eventInputSignals = event.getEventSignals();

            Set<EventParticipantSignal> participantInputSignals = eventParticipant
                    .getSignals();

            inputSignals = mergeInputSignals(programInputSignals,
                    eventInputSignals, participantInputSignals);
        }

        /*
         * Now we have the complete set of input signals, ready to run through
         * per-client rules and produce any appropriate output signals for this
         * client.
         */
        List<EventParticipantRule> eventClientRules = createEventParticipantRules(
        		programWithPPAndPRules, event, clientParticipant.getParticipant()
                        .getParticipantName(), clientParticipant
                        .getParticipant().isClient(), utilityDREvent);

        // update the event participant rules
        // TODO: why can't i just do eventPartEAO.setEventRules() with a new
        // list?
        if (clientParticipant.getEventRules() == null) {
            clientParticipant
                    .setEventRules(new HashSet<EventParticipantRule>());
        } else {
            clientParticipant.getEventRules().clear();
        }
        for (EventParticipantRule participantRule : eventClientRules) {
            participantRule.setEventParticipant(clientParticipant);
            clientParticipant.getEventRules().add(participantRule);
        }

        // merge in the new output signals (only those entries in the future)
        Set<EventParticipantSignal> newClientOuputSignals = getClientOutputSignals(
        		programWithPPAndPRules, event, eventClientRules, inputSignals, now,
                pendingSignalDef, modeSignalDef);

        return updateOutputSignals(clientParticipant.getSignals(),
                newClientOuputSignals, now);
    }

    /**
     * Combine program, event, and participant input signals into a single set,
     * and discard past signal entries that are no longer in effect.
     * 
     * @param programInputSignals
     * @param eventInputSignals
     * @param participantInputSignals
     * @return
     */
    private Set<Signal> mergeInputSignals(
            Set<ProgramSignal> programInputSignals,
            Set<EventSignal> eventInputSignals,
            Set<EventParticipantSignal> participantInputSignals) {

        Set<Signal> mergedSignals;
        mergedSignals = new HashSet<Signal>();

        for (Signal sig : programInputSignals) {
            mergedSignals.add(sig);
        }
        for (Signal sig : eventInputSignals) {
            mergedSignals.add(sig);
        }
        for (Signal sig : participantInputSignals) {
            mergedSignals.add(sig);
        }
        return mergedSignals;
    }

    protected Set<EventParticipantSignal> updateOutputSignals(
            Set<EventParticipantSignal> existingSignals,
            Set<EventParticipantSignal> newSignals, Date now) {
        if (existingSignals == null || existingSignals.size() == 0 ) {
            for (EventParticipantSignal newSignal : newSignals) {
                for (SignalEntry entry : newSignal.getSignalEntries()) {
                    if (entry.getTime().getTime() >= now.getTime()) {
                        entry.setParentSignal(newSignal);
                    }
                }
            }
            return newSignals;
        }

        // merge in the new input signals (only those entries in the future)
        for (EventParticipantSignal existingSignal : existingSignals) {
            // trim future entries
            Iterator<? extends SignalEntry> it = existingSignal
                    .getSignalEntries().iterator();
            while (it.hasNext()) {
                SignalEntry pse = it.next();
                if (pse.getTime().getTime() >= now.getTime()) {
                    it.remove();
                }
            }

            // merge future entries
            for (EventParticipantSignal newSignal : newSignals) {
                if (existingSignal.getSignalDef().getSignalName()
                        .equals(newSignal.getSignalDef().getSignalName())) {

                    boolean limitSlots = programLimitsModeEntries();
                    if (limitSlots && existingSignal.getSignalDef().getSignalName().equals("mode")) {
                        existingSignal.getSignalEntries().clear();
                    }
                    for (SignalEntry entry : newSignal.getSignalEntries()) {
                        if (entry.getTime().getTime() >= now.getTime()) {
                            entry.setParentSignal(existingSignal);
                            ((Set) existingSignal.getSignalEntries())
                                    .add(entry);
                        }
                    }
                }
            }
        }
        return existingSignals;
    }

    // This is a helper method that is rarely overridden by derivative programs
    // The default (false) allows ModeSlot signal elements to accumulate during
    // the course of an event.  Returning true keeps the list of ModeSlot
    // elements limited to just the most recent mode
    protected boolean programLimitsModeEntries() {
        return false;
    }

    @Override
    public List<String> getProgramRuleVariables(String programName) {
        ArrayList<String> signalNames = new ArrayList<String>();

        Program prog = programManager.getProgramWithSignals(programName);

        if (prog != null && prog.getSignals() != null
                && prog.getSignals().size() > 0) {
            // there are program signals.
            for (com.akuacom.pss2.program.signal.ProgramSignal ps : prog
                    .getSignals()) {
                if (ps == null
                        || "pending".equals(ps.getSignalDef().getSignalName())
                        || "mode".equals(ps.getSignalDef().getSignalName())) {
                    continue;
                }
                signalNames.add(ps.getSignalDef().getSignalName());
            }
        }
        Collections.sort(signalNames);

        return signalNames;
    }

    protected double getCurrentValue(String variableName,
            Set<? extends Signal> signals, long dateMS) {
        double lastValue = 0.0;
        for (Signal signal : signals) {
            // find the signal that matches the variable (numeric signals only)
            if (signal.getSignalDef().getSignalName().equals(variableName)
                    && !signal
                            .getSignalDef()
                            .getType()
                            .equals(EventInfoInstance.SignalType.LOAD_LEVEL
                                    .name())) {
                List<SignalEntry> signalEntryList = new ArrayList<SignalEntry>(
                        signal.getSignalEntries());
                Collections.sort(signalEntryList);
                for (SignalEntry entry : signalEntryList) {
                    // loop until we get to the last entry before the current
                    // time
                    if (dateMS < entry.getTime().getTime()) {
                        break;
                    }
                    SignalEntry numberEntry = (SignalEntry) entry;
                    lastValue = numberEntry.getNumberValue();
                }
                break;
            }
        }
        return lastValue;
    }

    /**
     * Evalute the rules for a given date and return the mode and the rule
     * number
     * 
     * @param rules
     *            the rules
     * @param participantSignals
     *            the map
     * @param date
     *            the date
     * @return result
     */
    protected EventParticipantSignalEntry evaluateParticipantEventRules(
            List<EventParticipantRule> rules,
            Set<? extends Signal> participantSignals, Date date) {
        long dateMS = date.getTime();
        EventParticipantSignalEntry signalEntry = new EventParticipantSignalEntry();
        signalEntry.setTime(date);

        for (EventParticipantRule rule : rules) {
            // TODO: this should really be <= not < since the rule end time is
            // not the millisecond after the actual end. when we fix the 24:00
            // issue, then this will return to be correct. we are leaving it
            // broken since being 1 ms off is no big deal here

        	if (dateMS >= rule.getStart().getTime()
                    && dateMS < rule.getEnd().getTime()) {
                signalEntry.setRuleId(rule.getSortOrder());
                signalEntry.setLevelValue(rule.getMode().toString()
                        .toLowerCase());
                if (rule.getOperator() == Operator.ALWAYS) {
                    return signalEntry;
                }
                double ruleValue = rule.getValue();
                double currentValue = getCurrentValue(rule.getVariable(),
                        participantSignals, dateMS);
                switch (rule.getOperator()) {
                case LESS_THAN:
                    if (currentValue < ruleValue) {
                        return signalEntry;
                    }
                    break;
                case LESS_THAN_OR_EQUAL:
                    if (currentValue <= ruleValue) {
                        return signalEntry;
                    }
                    break;
                case EQUAL:
                    if (currentValue == ruleValue) {
                        signalEntry.setLevelValue(rule.getMode().toString()
                                .toLowerCase());
                        return signalEntry;
                    }
                    break;
                case GREATER_THAN_OR_EQUAL:
                    if (currentValue >= ruleValue) {
                        signalEntry.setLevelValue(rule.getMode().toString()
                                .toLowerCase());
                        return signalEntry;
                    }
                    break;
                case GREATER_THAN:
                    if (currentValue > ruleValue) {
                        signalEntry.setLevelValue(rule.getMode().toString()
                                .toLowerCase());
                        return signalEntry;
                    }
                    break;
                case NOT_EQUAL:
                    if (currentValue != ruleValue) {
                        signalEntry.setLevelValue(rule.getMode().toString()
                                .toLowerCase());
                        return signalEntry;
                    }
                    break;
                }
            }
        }

        signalEntry.setRuleId(-1);
        signalEntry.setLevelValue(ModeSignal.Levels.normal.toString());

        return signalEntry;
    }

    protected void getRuleTransitions(EventTiming eventTiming,
            List<EventParticipantRule> rules, Set<? extends Signal> signals,
            Set<Date> transitionSet, Date now) {
        // get the sorted list of all time transitions plus the start time
       
    	Date nowDate = new Date();
        if(eventTiming.getStartTime().before(nowDate) || eventTiming.getStartTime().equals(nowDate)){
        	Calendar cal = new GregorianCalendar();
        	cal.setTime(now);
        	cal.add(Calendar.SECOND,1);
        	transitionSet.add(cal.getTime());
        }else{
        	transitionSet.add(eventTiming.getStartTime());	
        }
        
        for (EventParticipantRule rule : rules) {
        	transitionSet.add(rule.getStart());
            transitionSet.add(rule.getEnd());
        }
        for (Signal signal : signals) {
            // TODO replace with isOutputSignal
            if (!signal.getSignalDef().getSignalName().equals("pending")
                    && !signal.getSignalDef().getSignalName().equals("mode")) {
                for (SignalEntry entry : signal.getSignalEntries()) {
                    transitionSet.add(entry.getTime());
                }
            }
        }
    }

    /**
     * Gets the pending signals.
     * 
     * @param program
     *            the program
     * @param eventTiming
     *            event timing
     * @param now
     *            the time
     * @return set
     */
    @SuppressWarnings("deprecation")
    protected Set<EventParticipantSignalEntry> getPendingSignalEntries(
            Program program, EventTiming eventTiming, Date now1) {
        Set<EventParticipantSignalEntry> pendingEntries = new HashSet<EventParticipantSignalEntry>();

        // TODO: handle far, near, etc.
        Date pendingOnTime;
        if (eventTiming.getIssuedTime().getTime() <= eventTiming.getStartTime()
                .getTime() - program.getPendingLeadMS(eventTiming)) {
            // issue time is before start of pending, so turn in
            // on using calc
            pendingOnTime = new Date(eventTiming.getStartTime().getTime()
                    - program.getPendingLeadMS(eventTiming));
        } else {
            // turn pending on now
            pendingOnTime = eventTiming.getIssuedTime();
        }
        EventParticipantSignalEntry pendingEntry = new EventParticipantSignalEntry();
        pendingEntry.setTime(pendingOnTime);
        pendingEntry.setLevelValue("on");
        pendingEntries.add(pendingEntry);

        return pendingEntries;
    }

    protected Set<EventParticipantSignalEntry> getModeSignalEntries(
            Program program, EventTiming event,
            List<EventParticipantRule> participantRules,
            Set<? extends Signal> participantInputSignals, Date now)
            throws ProgramValidationException {
        Set<EventParticipantSignalEntry> modeEntries = new HashSet<EventParticipantSignalEntry>();

        EventParticipantSignalEntry lastSignalEntry = null;
        ValidatorFactory.getProgramValidator(program).validateEventRules(event,
                participantRules);

        Set<Date> transitionSet = new HashSet<Date>();
        getRuleTransitions(event, participantRules, participantInputSignals,
                transitionSet,now);
        List<Date> transitionList = new ArrayList<Date>();
        // if the are elements in the list that are only 1 millisecond apart,
        // just add the second one
        
        if (transitionSet.size() > 0) {
            List<Date> tempTransitionList = new ArrayList<Date>(transitionSet);
            Collections.sort(tempTransitionList);
            Iterator<Date> i = tempTransitionList.iterator();
            Date lastDate = i.next();
            Date date = null;
            while (i.hasNext()) {
                date = i.next();
                if (date.getTime() != lastDate.getTime() + 1) {
                    transitionList.add(lastDate);
                }
                lastDate = date;
            }
            if (date != null) {
                transitionList.add(date);
            }
        }

        // for each each transition, evaluate the rules
        for (Date transition : transitionList) {
            // only consider transitions in the future and during the event
            // times
            if (transition.getTime() >= now.getTime() 
            		&& transition.getTime() >= event.getStartTime().getTime()
                    && ( event.getEndTime()==null || transition.getTime() < event.getEndTime().getTime())) {
                // all signals must be present before call to rule engine
                EventParticipantSignalEntry signalEntry = evaluateParticipantEventRules(
                        participantRules, participantInputSignals, transition);
                // only record if the level changes (or rule number taken out
                // for now)
                // TODO: look backwards for an already existing signal entry
                // with the same value
                
                if (signalEntry != null && (lastSignalEntry == null || !signalEntry.getLevelValue().equals(lastSignalEntry.getLevelValue())
                        // || (lastSignalEntry !=null && lastSignalEntry.getTime().before(now))
                         )) {
                	modeEntries.add(signalEntry);
                    lastSignalEntry = signalEntry;
                }
            }
        }

        return modeEntries;
    }

    /**
     * Evaluate the rules and generate the signals.
     * 
     * @param program
     *            the program
     * @param participantRules
     *            the participant rules map
     * @throws ProgramValidationException
     *             the exception
     */
    private Set<EventParticipantSignal> getClientOutputSignals(Program program,
            EventTiming eventTiming,
            List<EventParticipantRule> participantRules,
            Set<? extends Signal> inputSignals, Date now1,
            SignalDef pendingSignalDef, SignalDef modeSignalDef)
            throws ProgramValidationException {
        Set<EventParticipantSignal> clientOutputSignals = new HashSet<EventParticipantSignal>();

        EventParticipantSignal pendingSignal = new EventParticipantSignal();
        pendingSignal.setSignalDef(pendingSignalDef);
        pendingSignal.setSignalEntries(getPendingSignalEntries(program,
                eventTiming, now1));
        clientOutputSignals.add(pendingSignal);

        EventParticipantSignal modeSignal = new EventParticipantSignal();
        modeSignal.setSignalDef(modeSignalDef);
        modeSignal.setSignalEntries(getModeSignalEntries(program, eventTiming,
                participantRules, inputSignals, now1));
        clientOutputSignals.add(modeSignal);

        return clientOutputSignals;
    }

    /**
     * Get the signals for the event.
     * 
     * @param program
     *            the program
     * 
     * @return the event default signals
     */
    protected List<EventParticipantRule> getProgramRules(Program program,
            EventTiming eventTiming, UtilityDREvent utilityDREvent) {
        ArrayList<EventParticipantRule> res = new ArrayList<EventParticipantRule>();
        //currently just return empty rules
        List<ProgramRule> progRules = getProgramRules(program, eventTiming);
        // add pre-sorted rules as EventRuleEAOs to the results
        for (ProgramRule rule : progRules) {
            res.add(new EventParticipantRule(rule, eventTiming.getStartTime()));
        }

        return res;
    }

    protected List<ProgramRule> getProgramRules(Program program, EventTiming eventTiming) {
        // Get Custom rules
        ArrayList<ProgramRule> progRules = new ArrayList<ProgramRule>();

        return progRules;
    }

    protected List<ProgramRule> getProgramRules(Program program) {
        // Get Custom rules
        return getProgramRules(program, null);
    }

    protected void setEventTiming(Event event, UtilityDREvent utilityDREvent) {
        // this timing doesn't make sense - event would have no duration
        // This method is only being called when issue time is null.
        // TODO: need more comments when and why issue time is null.
        event.setReceivedTime(new Date());
        event.setIssuedTime(event.getReceivedTime());
        event.setStartTime(event.getReceivedTime());
        event.setEndTime(event.getReceivedTime());
    }

    
    protected Program getProgramForEventCreation(String programName){
    	 Program program = programManager.getProgramWithParticipantsAndPRules(programName);
    	 return program;
    }
    
    protected Program getProgramForApxEventCreation(String programName, List<String> partList){
    	Program program = programManager.getProgramWithParticipantsAndPRules(programName,partList);
    	return program ;
    	}
    
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#createEvent(java.lang.String,
     * com.akuacom.pss2.core.model.Event,
     * org.openadr.dras.utilitydrevent.UtilityDREvent)
     */   
    @Override
    public Collection<String> createEvent(String programName,  Event event,
            UtilityDREvent utilityDREvent) {
        Collection<String> names = new ArrayList<String>();
        FireLogEntry logEntry = new FireLogEntry();
        logEntry.setUserParam1(programName);
        logEntry.setCategory(LogUtils.CATAGORY_EVENT);

        if (!programName.equals(event.getProgramName())) {
            String message = "program name " + programName + " doesn't match "
                    + "program name in event";
            logEntry.setDescription(message);
            logEntry.setLongDescr(event.toString());
            throw new EJBException(message);
        }
        
        try {
        	Program program = null;
        	if(event.getMessage()!=null && event.getMessage().equalsIgnoreCase("apxservice")){
	        	List<String> partList = new ArrayList<String>();
	        		for(EventParticipant ep : event.getParticipants()){
		        		if(!ep.getParticipant().isClient()){
		        			partList.add(ep.getParticipant().getParticipantName());
		        		}
	        		}
	        		if(!partList.isEmpty()){
	        			program = getProgramForApxEventCreation(programName,partList);
	        		}
	        		else{
	        			 log.info("event "+event.getEventName()+"created without any participant::::");
	        			 program = getProgramForEventCreation(programName);
	        		}
        	}else{
        		 program = getProgramForEventCreation(programName);
        	}
            if (event.getIssuedTime() == null) {
                setEventTiming(event, utilityDREvent);
            }
          
            setEventStatus(event);
            

            if(!(program instanceof TestProgram))
            {
		    	Set<EventParticipant> eventParticipants = 
		    		createEventParticipants(event, program);
	            
	            Set<EventParticipant> aggregatedEventParticipants = 
	            	createAggregatedEventParticipants(eventParticipants, program);
	            eventParticipants.addAll(aggregatedEventParticipants);

				Set<EventParticipant> filteredEventParticipants =
					programManager.filterEventParticipants(eventParticipants, program, event, true);
	
	            Set<EventParticipant> eventClients =
	            	createEventClients(filteredEventParticipants, program);
	            filteredEventParticipants.addAll(eventClients);
	            
	            EventStateCacheHelper esch = EventStateCacheHelper.getInstance();
	            
	            for(EventParticipant eventParticipant: filteredEventParticipants)
	            {
	            	eventParticipant.setEvent(event);
            		String participantName = eventParticipant.getParticipant().getParticipantName();
            		esch.delete(participantName);
	            }
	            event.setEventParticipants(filteredEventParticipants);
            }
            
            ProgramValidator programValidator = ValidatorFactory
                    .getProgramValidator(program);
            if(null == event.getUUID()){
            	programValidator.validateEvent(event);
            }

            // Allow custom program implementations to initialize
            // their own special signals or event information
            Set<EventSignal> eventSignals = initializeEvent(program, event,
                    utilityDREvent);
            for (EventSignal signal : eventSignals) {
                signal.setEvent(event);
                for (EventSignalEntry entry : signal.getEventSignalEntries()) {
                    entry.setEventSignal(signal);
                }
            }
            event.setEventSignals(eventSignals);

            
            processRulesAndSignals(program, event, utilityDREvent,
                    event.getReceivedTime());
            
            event = persistEvent(program.getProgramName(), event);
            
            sendDRASOperatorNotifications(event, "created");
            
            names.add(event.getEventName());
            
            //asynchronous call to create aggregated baseline for all event participants
            //to improve performance
            final String evtname = event.getEventName();
            PDataSet set = this.dataSetEAO.getDataSetByName("Baseline");
            final String setId = set.getUUID();
            asynchCaller.call(new EJBAsynchRunable(DataManager.class, 
            				"createOrUpdateEventDataEntries",
            				 new Class[]{String.class,String.class},
            				 new Object[]{evtname,setId}
            ));
            
            publicToDRwebsite(program, event);
            
        } catch (ProgramValidationException e) {
            logEntry.setDescription("event creation failed: "
                    + event.getEventName());
            logEntry.setLongDescr(event.toString());
            throw new EJBException(e);
        }
        return names;
    }
    
    @Override
   	public void updateProgramAggregatorTable(String eventName) {
       	
   			programAggregatorEAOManager.createProgramAggregator(eventName);
    }
   	

    protected void setEventStatus(Event event){
    	 event.setEventStatus(EventStatus.RECEIVED);
    }
    
    protected void publicToDRwebsite(Program program, Event event) {
		topicPublisher.publish(new CacheNotificationMessage(program.getProgramClass(), null, event.getEventStatus() == EventStatus.ACTIVE, event.getUUID()));
    }
    
    protected void reportEventHistory(Event event, boolean cancel, Program program){
    	 asynchCaller.call(new EJBAsynchRunable(CustomerReportManager.class, 
   				"reportEventHistory",
   				 new Class[]{Event.class,boolean.class,Program.class},
   				 new Object[]{event,cancel,program}
     	  	));
        
    	 //customerReportManager.reportEventHistory(event, cancel, program);
    }
    
    /**
     * record event history for customer report
     * 
     * @param eventWithParticipantAndSignals the event
     */
    protected void reportEventHistory(Event eventWithParticipantAndSignals, boolean cancel){
    	//asynch call
    	  asynchCaller.call(new EJBAsynchRunable(CustomerReportManager.class, 
  				"reportEventHistory",
  				 new Class[]{Event.class,boolean.class},
  				 new Object[]{eventWithParticipantAndSignals,cancel}
    	  	));
        //customerReportManager.reportEventHistory(eventWithParticipantAndSignals, cancel);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramDataAccess#persistEvent(java.lang.String,
     * com.akuacom.pss2.core.model.Event, java.util.List)
     */
    protected Event persistEvent(String programName, Event event) {
        StackTraceUtil.dumpStack();
        if (programName == null) {
            String message = "program name is null";
            throw new EJBException(message);
        }
        if (event == null) {
            String message = "event is null";
            throw new EJBException(message);
        }

        try {
            log.debug("parts " + Dbg.oS(event.getParticipants()));
           // event = eventEAO.create(event);
            if(null != event.getUUID()){
            	event = eventEAO.update(event);
            }else{
            	event = eventEAO.create(event);
            }
//            reportParticipation(event);
            return event;
        } catch (Exception e) {
            // TODO 2992
            String message = "error adding event for program:" + programName;
            throw new EJBException(message, e);
        }
    }

//    protected void reportParticipation(Event event) throws AppServiceException {
//        // all the participants listed in the program
//        final List<Participant> clients = clientEAO
//                .findClientsByProgramName(event.getProgramName());
//
//        // the participants that actually got called in this event
//        final List<EventParticipant> eventParticipantList = event
//                .getParticipants();
//        // go through participants (not clients) and report participation
//
//        for (Participant participant : clients) {
//            // for (Participant participant : participants) {
//            final String name = participant.getParticipantName();
//            boolean called = false;
//            for (EventParticipant ep : eventParticipantList) {
//                if (name.equals(ep.getParticipant().getParticipantName())) {
//                    called = true;
//                    break;
//                }
//            }
//            String reason;
//            if (called) {
//                if (participant.isClient() && participant.isManualControl()) {
//                    reason = "MANUAL";
//                } else {
//                    reason = "AUTO-DR";
//                }
//            } else {
//                reason = "NOT CALLED";
//            }
//            final EventParticipation ep = new EventParticipation();
//            ep.setParticipantName(name, participant.isClient());
//            ep.setReason(reason);
//            ep.setType(participant.getTypeString());
//            ep.setAccountId(participant.getAccountNumber());
//            ep.setEntryTime(event.getStartTime());
//            ep.setEventName(event.getEventName());
//            ep.setProgramName(event.getProgramName());
//            report.reportParticipation(ep);
//        }
//    }

    
    private void updateCache(Event event){
    	if (event != null) {
    		EventStateCacheHelper esch = EventStateCacheHelper.getInstance();
    		for(EventParticipant ep : event.getEventParticipants()){
        		String participantName = ep.getParticipant().getParticipantName();
        		esch.delete(participantName);
        	}
    	}
    }
    
    
    @Override
    public void deleteEvent(Event event) {
        // we need to do all this because we have hibernate cache enabled
        // and we need to clean up cache to make sure everything is consistent.
        try {
            if (event != null) {
            	updateCache(event);
                eventEAO.delete(event);
            }
        } catch (EntityNotFoundException e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#getEvents(java.lang.String)
     */
    @Override
    public List<EventInfo> getEvents(String programName) {
        List<EventInfo> eventInfos = new ArrayList<EventInfo>();
        final List<Event> events = eventEAO.findEventOnlyByProgramName(programName);
        for (Event event : events) {
            EventInfo eventInfo = new EventInfo();
            eventInfo.setProgramName(programName);
            eventInfo.setEventName(event.getEventName());
            eventInfos.add(eventInfo);
        }
        return eventInfos;
    }

    // TODO: this should be implemented directly by ProgramDataAccess
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#getEvent(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Event getEvent(String programName, String eventName) {
        try {
            return eventEAO.getByEventAndProgramName(eventName, programName);
        } catch (EntityNotFoundException e) {
            String message = "error getting event " + eventName
                    + " for program " + programName;
            throw new EJBException(message, e);
        }
    }

    
    public void cancelEvent(String programName,String eventName,boolean needNotification){
        long start = System.currentTimeMillis();
    	Event event=eventEAO.findEventWithParticipantsAndSignalsAndContacts(eventName);
    	  
//    	Set<EventParticipant>  eps =  event.getEventParticipants();
//    	for(EventParticipant ep:eps){
//            Participant p = ep.getParticipant();

//    	}
    	  
        Program program = programManager.getProgramWithParticipants(event.getProgramName());
          
        reportEventHistory(event, true, program);
          
        log.info(event.getEventStatus().toString());
        
        if (event.getProgramName().startsWith("DBP")) {
            DBPEvent dbpEvent = (DBPEvent) event;
            List<EventParticipant> eventParticipantList = getEventParticipantsByEvent(dbpEvent);
            event.setParticipants(eventParticipantList);
        }
        // the reason delete happens before sending notification is because
        // they can't rollback.
        deleteEvent(event);
        if(needNotification) sendDRASOperatorNotifications(event, "canceled");
        
        if (event.getEventStatus() != EventStatus.RECEIVED) {
            if(needNotification) {
            	EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
            	String utilityName=cache.getUtilityName("utilityName");
            	if(utilityName != null && utilityName.equalsIgnoreCase("heco") 
            			&& program.getClassName().equalsIgnoreCase("com.akuacom.pss2.program.fastdr.FastDRProgramEJB")){            
            		sendVarolliParticipantCancelNotifications(event, "canceled");
            	} else {
                	if (event.getProgramName().startsWith("DBP")) {
                		sendDBPEnvoyCancelNotifications(event, "canceled");
//                		sendParticipantCancelNotifications(event, "canceled");
                	} else {
                		sendParticipantCancelNotifications(event, "canceled");
                	}
            	}
            }
            pushEventClientSignals(event);  // Tell any push clients                                 
        }
          
        publicCancelToDRwebsite(program, event);
        
        // TODO 2992
        log.info(LogUtils.createLogEntry(programName, LogUtils.CATAGORY_EVENT,
                "event canceled: " + eventName, null));
        long end = System.currentTimeMillis();
        log.info("Cancel event "+eventName+" costs :"+(end-start)+"milliseconds");
    }
    
    private List<EventParticipant> getEventParticipantsByEvent(DBPEvent event) {
        return epEAO.findEventWithoutOptOut(event.getEventName());
    }
    
    protected void publicCancelToDRwebsite(Program program, Event event) {
        topicPublisher.publish(new CacheNotificationMessage(program.getProgramClass(), null, 
  				event.getStartTime().before(new Date()), event.getUUID()));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#cancelEvent(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void cancelEvent(String programName, String eventName) {
//        Event event = getEvent(programName, eventName);
    	cancelEvent(programName,eventName,true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#updateEvent(java.lang.String,
     * java.lang.String, com.akuacom.pss2.core.model.Event,
     * org.openadr.dras.utilitydrevent.UtilityDREvent)
     */
    @Override
    public void updateEvent(String programName, String eventName, Event event,
            UtilityDREvent utilityDREvent) {
        throw new EJBException("updateEvent not supported for program "
                + programName);

        // TODO lin: this was added for UtilityOperator standard support. Will
        // comment this out
        // since the current system may not support this well.
        /*
         * try { List<String> progNames =
         * EJBFactory.getProgramManager().getPrograms(); for(String progName :
         * progNames) { ProgramEvent existingEvent = getEvent(progName,
         * eventName); {
         * if(existingEvent.getEventName().equals(event.getEventName())) {
         * existingEvent.setProgramName(event.getProgramName());
         * existingEvent.setEventName(event.getEventName());
         * existingEvent.setReceivedTime(event.getReceivedTime());
         * existingEvent.setIssuedTime(event.getIssuedTime());
         * existingEvent.setStartTime(event.getStartTime());
         * existingEvent.setEndTime(event.getEndTime()); } } } } catch
         * (RemoteException e) { throw new EJBException(e); }
         */
    }

    public void endEvent(String programName, String eventName, Date endTime){
    	throw new EJBException("End event not supported for program "
                + programName);
    }
    
    /**
     * Recalculates the input signals for an existing event and runs them
     * through rules to (potentially) revise client output signals This may
     * modify event signals for all clients, and possibly per-client signals if
     * they are derived through rules
     * 
     * @param programName
     * @param eventName
     * @param event
     * @throws ProgramValidationException
     */
    public void updateSignals(String programName, String eventName, Event event)
            throws ProgramValidationException {
        Program program = programManager.getProgramWithParticipantsAndPRules(programName);
        Date now = new Date();
        processRulesAndSignals(program, event, null, now);
    }

    /**
     * This method is called when there are revised event signals after an event
     * has been issued. For example, if prices need to be revised (more than
     * just adding more to the list).
     * 
     * This method is not generally for use when entirely new signal entries are
     * to be appended to the end. For that, use appendEventSignals instead
     * 
     * @param event
     * @param signalName
     * 
     * @return true if signals represent a change. False if no effective change.
     */
    protected boolean updateEventSignal(Event event, String signalName,
            Set<EventSignalEntry> newEntries,
            UtilityDREvent utilityDREvent, boolean keepPast) {

        try {
            Set<EventSignalEntry> oldEntries = eventManager.getSignalEntries(event.getUUID(), signalName);

            // If the new entries are no different from the old ones, then return
            if (compareSignalLists(oldEntries, newEntries)) { return false; }

            // Make a new list of entries, with the future coming from the new list
            // and possibly preserving past entries from the old one
            Set<EventSignalEntry> mergedEntries = mergeSignalLists(oldEntries, newEntries, keepPast);

            // Set the new list of signal entries to the event
            eventManager.setSignalEntries(event.getUUID(), signalName, mergedEntries, true);

            // Persist the modified event
            eventEAO.update(event);

            Program program = programManager.getProgramWithParticipantsAndPRules(event.getProgramName());
            processRulesAndSignals(program, event, utilityDREvent, new Date());
            // Persist the modified event
            eventEAO.update(event);
        }
        catch (EntityNotFoundException e) {
            log.error(LogUtils.createLogEntry(event.getProgramName(),
                    LogUtils.CATAGORY_EVENT, "Error updating event signal", null));
            throw new EJBException(e);
        } catch (ProgramValidationException ve) {
            log.error(LogUtils.createLogEntry(event.getProgramName(),
                    LogUtils.CATAGORY_EVENT, "Error updating event signal", null));
            throw new EJBException(ve);
        } catch (Exception ex) {
            log.error(LogUtils.createLogEntry(event.getProgramName(),
                    LogUtils.CATAGORY_EVENT, "Error updating event signal", null));
            throw new EJBException(ex);
        }

        return true;
    }


    /**
     * compares two sorted lists of SignalEntry, returning true if they're the
     * same
     * 
     * @param oldList
     *            The original list, from which past entries may be preserved.
     * @param newList
     *            The new list that will contribute new entries for the future
     * @return merged list with old entries from the old list and future entries
     *         from the new list
     */
    protected Set<EventSignalEntry> mergeSignalLists(
            Set<EventSignalEntry> oldList,
            Set<EventSignalEntry> newList,
            boolean keepPast) {

        Date now = new Date();
        Set<EventSignalEntry> mergedList = new HashSet<EventSignalEntry>();

        if ( (oldList == null || oldList.isEmpty()) &&
             (newList == null || newList.isEmpty()) ) {
            return mergedList; 
        }
        
        // I'm going to be a little paranoid and make certain these are sorted
        List<EventSignalEntry> sortedNew = new ArrayList<EventSignalEntry>();
        if (newList != null) { sortedNew.addAll(newList); }
        Collections.sort(sortedNew);

        List<EventSignalEntry> sortedOld = new ArrayList<EventSignalEntry>();
        if (oldList != null) { sortedOld.addAll(oldList); }
        Collections.sort(sortedOld);

        Date earliestNewTime = now;
        if (!sortedNew.isEmpty()) {
            earliestNewTime = sortedNew.get(0).getTime();
        }
        Date latestOldTime = null;
        for (EventSignalEntry old : sortedOld) {
            Date oldTime = old.getTime();
            if (oldTime.before(now)) {
                if (keepPast) {
                    mergedList.add(old);
                    latestOldTime = old.getTime();
                }
            } else {
                // old entry that starts in the future
                // might still keep if the new entries don't start until later
                if (oldTime.before(earliestNewTime)) {
                    // The new list doesn't begin until
                    // sometime in the future. So keep filling in old
                    mergedList.add(old);
                    latestOldTime = old.getTime();
                }
            }
        }

        // Now we've kept whatever entries from the old list that need kept
        // Time to figure out what new entries to add

        SignalEntry currentNew = null;
        for (SignalEntry possibleCurrent : sortedNew) {
            if (possibleCurrent.getTime().before(now)) {
                currentNew = possibleCurrent;
            }
        }
        if (currentNew != null) {
            // truncate the current new entry to start now
            currentNew.setTime(now);
        }

        for (EventSignalEntry newEntry : sortedNew) {
            Date newTime = newEntry.getTime();
            if (newTime.after(now) || newTime.equals(now)) {
                mergedList.add(newEntry);
            }
        }

        return mergedList;
    }

    /**
     * compares two sorted lists of SignalEntry, returning true if they're the
     * same
     * 
     * @param list1
     * @param list2
     * @return true if lists are the same. False if lists are different
     */
    protected boolean compareSignalLists(Set<? extends SignalEntry> list1,
            Set<? extends SignalEntry> list2) {

        if (list1.size() != list2.size()) {
            return false;
        }

        List<SignalEntry> sorted1 = new ArrayList<SignalEntry>();
        sorted1.addAll(list1);
        Collections.sort(sorted1);

        List<SignalEntry> sorted2 = new ArrayList<SignalEntry>();
        sorted2.addAll(list2);
        Collections.sort(sorted2);

        boolean foundDifferences = false;

        Date now = new Date();
        for (int i = 0; i < list1.size(); ++i) {
            SignalEntry sig1 = sorted1.get(i);
            SignalEntry sig2 = sorted2.get(i);
            Date s1Time = sig1.getTime();
            String s1Value = sig1.getValueAsString();

            Date s2Time = sig2.getTime();
            String s2Value = sig2.getValueAsString();

            if (s1Time.compareTo(s2Time) != 0) {
                foundDifferences = true;
            }
            if (s1Time.after(now)) {
                // compare values for future signals. Ignore different past
                // ones.
                if (!s1Value.equals(s2Value)) {
                    foundDifferences = true;
                }
            }
        }

        return !foundDifferences;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJB#getEventStatusString(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getEventStatusString(Event event) {
        final EventStatus status = event.getEventStatus();
        if(status == EventStatus.FAR || status == EventStatus.NEAR)
        {
        	return "ISSUED";
        }
        else
        {
        	return status.toString();
        }
    }

    public void addParticipant(String programName, List<Participant> participants,
            boolean isClient){
        FireLogEntry logEntry = new FireLogEntry();
        logEntry.setUserParam1(programName);
        logEntry.setCategory(LogUtils.CATAGORY_CONFIG_CHANGE);
        try {
            programParticipantManager.addProgramParticipant(programName,
            		participants, isClient);
        } catch (Exception e) {
            logEntry.setDescription("add participant failed: " + programName);
            logEntry.setLongDescr(e.getMessage());
            throw new EJBException(e);
        }
    	
    }


    
    // TODO: should we add the participant to the program's events? maybe
    // those that have been received but not issued? hard to say since the
    // participant list might have been specified in the event. for now,
    // we're not going to do it.
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#addParticipant(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void addParticipant(String programName, String participantName,
            boolean isClient) {
        FireLogEntry logEntry = new FireLogEntry();
        logEntry.setUserParam1(programName);
        logEntry.setCategory(LogUtils.CATAGORY_CONFIG_CHANGE);
        try {
            // final Program program = programManager.getProgram(programName);
            // TODO: get participant
            // Participant part = dataAccess.getParticipant(participant);
            // ValidatorFactory.getProgramValidator(program).validateProgramParticipant(part);
            programParticipantManager.addProgramParticipant(programName,
                    participantName, isClient);
        } catch (Exception e) {
            logEntry.setDescription("add participant failed: " + programName);
            logEntry.setLongDescr(e.getMessage());
            // TODO 2992
//            log.error(logEntry);
            throw new EJBException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#getParticipants(java.lang.String)
     */
    @Override
    public List<String> getParticipants(String programName) {
        return participantManager.getParticipantsForProgram(programName);
    }

    /**
     * Send dras operator notifications.
     * 
     * @param event
     *            the event
     * @param subject
     *            the subject
     */
    protected void sendDRASOperatorNotifications(Event event, String subject) {
    	EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
    	String utilityDisplayName = cache.getUtilityName("utilityDisplayName");
        String fullSubject = utilityDisplayName
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
            
        sendDRASOperatorEventNotification(fullSubject, sb.toString(),
                NotificationMethod.getInstance(),
                new NotificationParametersVO(), event,notifier);
    }

     public static synchronized void sendDRASOperatorEventNotification(
            String subject, String content, NotificationMethod method,
            NotificationParametersVO params, Event event, Notifier notifier) {
        // bug 761
        ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);

        EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        List<Contact> contacts =  cache.getEscacheforoperatorcontacts();
        if(contacts.isEmpty()){
        	contacts = cm.getOperatorContacts();
        	cache.setEscacheforoperatorcontacts("OperatorContacts", contacts);
        }
        List<Contact> sendList = new ArrayList<Contact>();
        
        // EventManager eventManager = EJBFactory.getBean(EventManager.class);
        //List<EventParticipant> eps = eventManager
        //       .getEventParticipantsForEvent(event.getEventName());
        //NO need to load again 
        List<EventParticipant> eps = new ArrayList<EventParticipant>(event.getEventParticipants());
        for(Iterator<EventParticipant> it=eps.iterator();it.hasNext();){
        	if(it.next().getEventOptOut()==0){
        		it.remove();
        	}
        }
        for (Contact c : contacts) {
            if (wantsOperatorEventNotification(c, eps)) {
                sendList.add(c);
            }
        }
        if (sendList.size() > 0) {
        	notifier.sendNotification(sendList, "operator", subject, content,
                    method, params, Environment.isAkuacomEmailOnly(),
                    event.getProgramName());
        }
    }
     
     public NativeQueryManager getNativeQueryManager() {
 		if(nativeQuery==null){
 			nativeQuery=EJBFactory.getBean(NativeQueryManager.class);
 		}
 		return nativeQuery;
 	}
     
    private static boolean isModeSignalEntriesNotNormal(EventParticipant ep) {
        if (ep.getSignals() != null) {
            for (Signal signal : ep.getSignals()) {
                if ("mode".equals(signal.getSignalDef().getSignalName())) {
                    if (signal.getSignalEntries() != null) {
                        for (SignalEntry se : signal.getSignalEntries()) {
                            EventParticipantSignalEntry lse = (EventParticipantSignalEntry) se;
                            if (!"normal".equals(lse.getLevelValue())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;

    }

    private static boolean wantsOperatorEventNotification(Contact contact,List<EventParticipant> epWightSignals){
    	  if (!contact.eventNotificationOn()) {
              return false;
          }
          if (contact.getEventNotification() == ContactEventNotificationType.FullNotification) {
              return true;
          }
          for (EventParticipant ep : epWightSignals) {
              if (isModeSignalEntriesNotNormal(ep)) {
                  return true;
              }
          }
          return false;
    }
    
    /**
     * Returns true if this participant contact wants event notifications.
     * Checks to see if the contact is set for no notification, full notification,
     * or strategy-only notification.  This version of the method does not
     * consider the case of an event revision.
     * 
     * @param eventParticipant
     * @param contact
     * @return 
     */
    public static boolean wantsParticipantEventNotification(
            EventParticipant eventParticipant, ParticipantContact contact) {
        return wantsParticipantEventNotification(eventParticipant, contact, false);
    }
    
    /**
     * Returns true if this participant contact wants event notifications.
     * Checks to see if the contact is set for no notification, full notification,
     * or strategy-only notification.  This version of the method 
     * will notify strategy-only contacts in cases of event revision.
     * 
     * @param eventParticipant
     * @param contact
     * @param isRevision indicates that the proposed note is from an event revision
     * @return 
     */
    public static boolean wantsParticipantEventNotification(
            EventParticipant eventParticipant, ParticipantContact contact,
            boolean isRevision) {
        if (contact.getEventNotification() == ContactEventNotificationType.NoNotification) {
            // Do not Disturb
            return false;
        }
        if (contact.getEventNotification() == ContactEventNotificationType.FullNotification) {
            // Always notify
            return true;
        }

        // This is a strategy-only contact, and generally gets notified
        // only when event mode signals are non-normal due to shed rules
        if (isRevision) {
            // something has changed.  We notify them even though we
            // can't be certain if strategy rule output was altered.
            return true;
        }
        
        return isModeSignalEntriesNotNormal(eventParticipant);
    }

    public static Map<String, ContactsOfflineError> getErrorMap(){
    	ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);
    	List<ContactsOfflineError> coeList = cm.getAllContactOfflineErrors();
    	Map<String, ContactsOfflineError> errorMap = new HashMap<String, ContactsOfflineError>();
    	if(coeList != null && coeList.size() > 0){
    		for(ContactsOfflineError er: coeList){
    			String key = er.getProgramName() + er.getParticipantUuid() + er.getContactsUuid();
    			errorMap.put(key, er);
    		}
    	}
    	return errorMap;
    }
    
    
    /**
     * Notify DRAS Operator of a change in a client comm status
     * 
     * @param subject
     * @param content
     * @param method
     * @param params
     * @param programName
     * @param timeSinceLastContact
     *            if -1 then online message
     */
    public static synchronized void sendDRASOperatorCommNotification(
            String subject, String content, NotificationMethod method,
            NotificationParametersVO params, String programName,
            Participant participant, long timeSinceLastContact,
            PSS2Properties props, Notifier notifier, Map<String, ContactsOfflineError> errorMap) {
        ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);
        EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        List<Contact> contacts =  cache.getEscacheforoperatorcontacts();
        if(contacts.isEmpty()){
        	contacts = cm.getOperatorContacts();
        	cache.setEscacheforoperatorcontacts("OperatorContacts", contacts);
        }
        sendDRASOperatorCommNotification(contacts, subject, content, method,
                params, programName, participant, timeSinceLastContact,
                props, notifier, errorMap);
    }
    
    public static synchronized void sendDRASOperatorCommNotification(
    		List<Contact> contacts,
            String subject, String content, NotificationMethod method,
            NotificationParametersVO params, String programName,
            Participant participant, long timeSinceLastContact,
            PSS2Properties props, Notifier notifier, Map<String, ContactsOfflineError> errorMap) {
        ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);
        List<Contact> sendList = new ArrayList<Contact>();
        
        boolean onSeason = props.isOnSeason();
        
        for (Contact c : contacts) {
        	String key = programName + participant.getUUID() + c.getUUID();
        	ContactsOfflineError coe = errorMap.get(key);
            if (c.isCommNotification()) {
                if (timeSinceLastContact < 0) {
                    // means comms back online
                    if (coe != null) {
                        if (coe.isOfflineError()) {
                            sendList.add(c);
                        }
                        // operator only gets online message once so delete
                        cm.removeContactsOfflineError(coe);
                    }
                } else if (timeSinceLastContact >= c.getOfflineErrorThresholdMinutesForSeason(onSeason)) {
                    // offline error
                    if (coe == null) {
                        coe = new ContactsOfflineError();
                        coe.setContactsUuid(c.getUUID());
                        coe.setOfflineError(false);
                        coe.setParticipantUuid(participant.getUUID());
                        coe.setProgramName(programName);
                    }
                    if (!coe.isOfflineError()) {
                        sendList.add(c);
                        coe.setOfflineError(true);
                        cm.setContactsOfflineError(coe);
                    }
                }
            }
        }

        if (sendList.size() > 0) {
        	notifier.sendNotification(sendList, "operator", subject, content,
                    method, params, Environment.isAkuacomEmailOnly(),
                    programName);
        }
    }

 

    /**
     * Send program operator notifications.
     * 
     * @param program
     *            the program
     * @param subject
     *            the subject
     * @param content
     *            the content
     */
    protected void sendProgramOperatorNotifications(Program program,
            String subject, String content) {
    	//TODO: remove this method due to the program operator is unavailable. DRMS-7792
    	
//        String fullSubject = systemManager.getPss2Properties()
//                .getUtilityDisplayName()
//                + " program "
//                + program.getProgramName() + " " + subject;
//        notifier.sendNotification(
//                programManager.getOperatorContacts(program.getProgramName()), "operator", fullSubject,
//                content, NotificationMethod.getInstance(),
//                new NotificationParametersVO(), Environment
//                        .isAkuacomEmailOnly(), program.getProgramName());
    }

    /**
     * Send program operator notifications.
     * 
     * @param event
     *            the event
     * @param subject
     *            the subject
     */
    protected void sendProgramOperatorNotifications(Event event, String subject, Program program) {
    	//TODO: remove this method due to the program operator is unavailable. DRMS-7792
    	
//        String fullSubject = systemManager.getPss2Properties()
//                .getUtilityDisplayName()
//                + " program "
//                + event.getProgramName()
//                + " event " + event.getEventName() + " " + subject;
//        notifier.sendNotification(
//        		program.getOperatorContactList(), "operator", fullSubject, event
//                        .toOperatorString(), NotificationMethod.getInstance(),
//                new NotificationParametersVO(), Environment
//                        .isAkuacomEmailOnly(), event.getProgramName());
    }

    
    
    
    /**
     * Send program operator notifications.
     * 
     * @param event
     *            the event
     * @param subject
     *            the subject
     */
    protected void sendProgramOperatorNotifications(Event event, String subject) {
    	//TODO: remove this method due to the program operator is unavailable. DRMS-7792
    	
//        String fullSubject = systemManager.getPss2Properties()
//                .getUtilityDisplayName()
//                + " program "
//                + event.getProgramName()
//                + " event " + event.getEventName() + " " + subject;
//        notifier.sendNotification(
//                programManager.getOperatorContacts(event.getProgramName()), "operator", fullSubject, event
//                        .toOperatorString(), NotificationMethod.getInstance(),
//                new NotificationParametersVO(), Environment
//                        .isAkuacomEmailOnly(), event.getProgramName());
    }

    private void sendParticipantCancelNotifications(Event event, String verb) {
    	 asynchCaller.call(new EJBAsynchRunable(ParticipantNotification.class, 
 				"sendParticipantNotifications",
 				 new Class[]{Event.class,String.class,boolean.class,boolean.class},
 				 new Object[]{event, verb, true, false}
   	  	));
    }
    
    private void sendVarolliParticipantCancelNotifications(Event event, String verb) {
   	 asynchCaller.call(new EJBAsynchRunable(VaroliNotification.class, 
				"sendVaroliParticipantNotifications",
				 new Class[]{Event.class,String.class},
				 new Object[]{event, verb}
  	  	));
   }
    
    private void sendDBPEnvoyCancelNotifications(Event event, String verb) {
      	 asynchCaller.call(new EJBAsynchRunable(DBPBidProgramEJB.class, 
   				"sendDBPEnvoyNotification",
   				 new Class[]{Event.class,String.class,boolean.class,boolean.class},
   				 new Object[]{event, verb, true, false}
     	  	));
      }
    

    protected void sendParticipantNotifications(Event event, String verb) {
        sendParticipantNotifications(event, verb, true, false);
    }

    protected void sendParticipantIssuedNotifications(Event event, String verb) {
        sendParticipantNotifications(event, verb, true, false);
    }

    /**
     * Send participant notifications.
     * 
     * @param event
     *            the event
     */
    protected void sendParticipantNotifications(Event event, String verb,
            boolean showClientStatus, boolean isRevision) {
        PSS2Properties pss2Props = systemManager.getPss2Properties();

        for (EventParticipant eventParticipant : event.getParticipants()) {
        	if (eventParticipant.getEventOptOut()!=0)
        		continue;
        	
            Participant participant = eventParticipant.getParticipant();

            String stateString = "";
            String clientString = " Auto DR Client " + participant.getParticipantName() + " ";
            String salutationString = "Your ";
            String eventLiteral = " ";

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

            String actionString = verb;
            if (verb.equalsIgnoreCase("created")) {
            	actionString = " has been scheduled";
            } else if (verb.equalsIgnoreCase("issued")) {
            	actionString = " has been scheduled";            	
            } else if (verb.equalsIgnoreCase("started")) {
            	actionString = " has started";            	
            } else if (verb.equalsIgnoreCase("deleted")) {
            	actionString = " has been cancelled";            	
            } else if (verb.equalsIgnoreCase("cancelled")) {
            	actionString = " has been cancelled";            	
            } else if (verb.equalsIgnoreCase("completed")) {
            	actionString = " has completed";            	
            }

            EventStateCacheHelper eventCache = EventStateCacheHelper.getInstance();
            String utilityDisplayName = eventCache.getUtilityName("utilityDisplayName");
            String subject = salutationString + utilityDisplayName
                    + clientString + stateString + event.getProgramName() + eventLiteral + actionString;

            final Set<EventParticipantSignal> participantSignals = eventParticipant
                    .getSignals();
            Set<EventSignal> eventSignals = eventParticipant.getEvent()
                    .getEventSignals();

            List<Signal> combinedSignals = new ArrayList<Signal>();
            combinedSignals.addAll(eventSignals);
            combinedSignals.addAll(participantSignals);

            String emailContentType = pss2Props.getEmailContentType();
            EventEmailFormatter mailFactory = getMailFactoryInstance();
            String serverHost = pss2Props
                    .getStringValue(PSS2Properties.PropertyName.SERVER_HOST);

//            Participant p = participantEAO.findParticipantWithContacts(participant.getParticipantName(), participant.isClient());
            for (ParticipantContact pc : participant.getContacts()) {
                if (pc == null
                        || !wantsParticipantEventNotification(eventParticipant,
                                pc, isRevision)) {
                    continue;
                }
                    String emailContent = mailFactory.generateEmailContent(event,
                            combinedSignals, serverHost, emailContentType,
                            isRevision, null, null);
                    notifier.sendNotification(pc.getParticipantContactAsContact(),
                            participant.getParticipantName(), subject,
                            emailContent, emailContentType,
                            NotificationMethod.getInstance(),
                            new NotificationParametersVO(),
                            Environment.isAkuacomEmailOnly(), true, false,
                            event.getProgramName());
                }
            }
        }

	protected EventEmailFormatter getMailFactoryInstance() {
		EventEmailFormatter mailFactory = new EventEmailFormatter();
		return mailFactory;
	}
    
    private void sendParticipantCancelNotifications(Event event, String verb,
            boolean showClientStatus, boolean isRevision) {
        PSS2Properties pss2Props = systemManager.getPss2Properties();

        for (EventParticipant eventParticipant : event.getParticipants()) {
        	if (eventParticipant.getEventOptOut()!=0)
        		continue;
        	 asynchCaller.call(new EJBAsynchRunable(ParticipantNotification.class, 
        				"sendNotificationToParticipant",
        				 new Class[]{EventParticipant.class,Event.class,String.class,boolean.class,boolean.class,PSS2Properties.class},
        				 new Object[]{eventParticipant, event, verb, showClientStatus, isRevision, pss2Props}
          	  	));
//        	 this.sendNotificationToParticipant(eventParticipant, event, verb, showClientStatus, isRevision, pss2Props)
            }//end of loop
        }
    
    

    /**
     * gets called every 5 seconds
     * 
     * make sure you call super if you override this since it calls
     * executeSignals().
     * 
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void tick5Seconds(Event event) {
        try {
            long now = System.currentTimeMillis();
            processEvent(event, now);
        } catch (Exception e) {
            log.error(LogUtils.createLogEntry(event.getProgramName(),
                    LogUtils.CATAGORY_EVENT, "event processing error", null));
        }
    }

    /**
     * Process event.
     * 
     * @param event
     *            the event
     * @param ms
     *            the ms
     */
    protected void processEvent(Event event, long ms) {
        String eventName = event.getEventName();
        if(!event.isAggregatorUpdated()){
        	try{
        	updateProgramAggregatorTable (eventName);	
        	event.setAggregatorUpdated(true);
        	eventEAO.update(event);
        	} catch (EntityNotFoundException e) {
                String message = "error getting event " + eventName
                        + " for program " + event.getProgramName();
                throw new EJBException(message, e);
            }
        }
        if (event.getEndTime()!=null && event.getEndTime().getTime() < ms) {
           this.completeEvent(event);
        } else if (event.getEventStatus() == EventStatus.RECEIVED) {
            if (event.getIssuedTime().getTime() < ms) {
                try {
                	event = eventManager.getEventWithParticipantsAndSignals(eventName);
                    event.setEventStatus(EventStatus.FAR);
                    eventEAO.update(event);
                    sendDRASOperatorNotifications(event, "issued");
                    sendParticipantIssuedNotifications(event, "issued");
                    pushEventClientSignals(event);  // Tell any push clients 
                    updateCache(event);
                } catch (EntityNotFoundException e) {
                    String message = "error getting event " + eventName
                            + " for program " + event.getProgramName();
                    throw new EJBException(message, e);
                }
            }

        } // TODO: need NEAR transition
        else if (event.getEventStatus() == EventStatus.FAR) {
            if (event.getStartTime().getTime() < ms) {
                try {
                	event = eventManager.getEventWithParticipantsAndSignals(eventName);
                    event.setEventStatus(EventStatus.ACTIVE);
                    eventEAO.update(event);
                    // If this program sends messages for event completion, send
                    // them now
                    if (programSendsStartedNotifications(event.getProgramName())) {
                        sendDRASOperatorNotifications(event, "started");
                        sendParticipantNotifications(event, "started");
                    } 
                    pushEventClientSignals(event);  // Tell any push clients
                    updateCache(event);
                } catch (EntityNotFoundException e) {
                    String message = "error getting event " + eventName
                            + " for program " + event.getProgramName();
                    throw new EJBException(message, e);
                }

            }
        }
    }

    
    protected void completeEvent(Event event){
    	 event = eventManager.getEventWithParticipantsAndSignals(event.getEventName());
     	// create report data
         //report.doReport(eventName, false);

         //log event info to history tables for customer report
         reportEventHistory(event, false);

         // If this program sends messages for event start, send them now
         if (programSendsCompletedNotifications(event.getProgramName())) {
             sendDRASOperatorNotifications(event, "completed");
             
             sendParticipantNotifications(event, "completed");
             updateCache(event);
         } 

         // PERF_COMM use efficient delete
         // programDataAccess.removeEvent(programName, eventName);
         deleteEvent(event);
    }
    
    /**
     * programSendsStartedNotifications
     * 
     * Program EJB Beans may override this method to control whether or not
     * notifications get sent when events start For example, ongoing RTP
     * programs may not want these notifications
     */
    protected boolean programSendsStartedNotifications(String programName) {
        return true; // override for programs that need to control "started"
        // notifications
    }

    /**
     * programSendsCompletedNotifications
     * 
     * Program EJB Beans may override this method to control whether or not
     * notifications get sent when events complete For example, ongoing RTP
     * programs may not want these notifications
     */
    protected boolean programSendsCompletedNotifications(String programName) {
        return true; // override for programs that need to control "completed"
        // notifications
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJB#isParticipantInProgram(java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean isParticipantInProgram(String programName,
            String participantName, boolean isClient) {
        for (Participant part : participantManager.getAllParticipants()) {
            if (participantName.equals(part.getParticipantName())
                    && (part.isClient() == isClient)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJB#isParticipantEventSignalActive(java.
     * lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean isParticipantEventSignalActive(String programName,
            String eventName, String participantName, boolean isClient,
            String signalName) {
        try {
            Program program = programManager.getProgramOnly(programName);
            Event event = getEvent(programName, eventName);
            for (EventParticipant eventParticipant : event.getParticipants()) {
                if (eventParticipant.getParticipant().getParticipantName()
                        .equals(participantName)) {
                    long nowMS = new Date().getTime();
                    if (nowMS < event.getIssuedTime().getTime()) {
                        return false;
                    }
                    if (signalName.equals("pending")) {
                        if (nowMS > event.getStartTime().getTime()
                                - program.getPendingLeadMS(event)
                                && (event.getEndTime()==null || nowMS < event.getEndTime().getTime()) ) {
                            return true;
                        }
                    } else if (signalName.equals("mode")) {
                        if (nowMS > event.getStartTime().getTime()
                                && (event.getEndTime()==null || nowMS < event.getEndTime().getTime()) ) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            String message = "isParticipantEventSignalActive failed";
            throw new EJBException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJB#getProgramConfigJSPPage(java.lang.String
     * )
     */
    @Override
    public String getProgramConfigJSPPage(String programName) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJB#getEventConfigJSPPage(java.lang.String)
     */
    @Override
    public String getEventConfigJSPPage(String programName) {
        return null;
    }

    /**
     * Gets the signal.
     * 
     * @param programName
     *            the program name
     * @param signalName
     *            the signal name
     * 
     * @return the signal
     */
    protected Signal getSignal(String programName, String signalName) {
        for (Signal signal : programManager.getProgramWithSignals(programName)
                .getSignals()) {
            if (signal.getSignalDef().getSignalName().equals(signalName)) {
                return signal;
            }
        }
        String message = "can't find signal named " + signalName
                + " in program " + programName;
        throw new EJBException(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#newEventState()
     */
    @Override
    public EventState newEventState() {
        return new EventState();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJB#createDREventData(java.lang.StringBuilder
     * , com.akuacom.pss2.util.EventState)
     */
    @Override
    public StringBuilder createDREventData(StringBuilder sb,
            EventState eventState) {
        return sb;
    }

    /**
     * This is the place to get a new, empty event object that is properly
     * matched to the program. (Some programs have derivative event classes)
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#newProgramEvent()
     */
    @Override
    public Event newProgramEvent() {
        return new Event();
    }

    /**
     * Value to mode string.
     * 
     * @param value
     *            the value
     * 
     * @return the string
     */
    protected Mode valueToMode(double value) {
        int intValue = (int) (value + 0.5);
        switch (intValue) {
        case 2:
            return Mode.MODERATE;
        case 3:
            return Mode.HIGH;
        case 4:
            // TODO: support special
            return Mode.SPECIAL;
        case 1:
        default:
            return Mode.NORMAL;
        }
    }

    /**
     * Value to mode string.
     * 
     * @param value
     *            the value
     * 
     * @return the string
     */
    protected Mode valueToMode(String value) {
        // TODO: support special
        if (value.toLowerCase().equals("moderate")) {
            return Mode.MODERATE;
        } else if (value.toLowerCase().equals("high")) {
            return Mode.HIGH;
        } else if (value.toLowerCase().equals("special")) {
            return Mode.SPECIAL;
        } else {
            return Mode.NORMAL;
        }
    }

    /**
     * Quick check to see if there is any event today and tomorrow for this
     * program. Need to check today and tomorrow so need a way to return 2 flags
     * which is why a BitSet is returned.
     * 
     * @param programName
     * @return If bit 0 is set then there is an event for today. If bit 1 is set
     *         then there is an event for tomorrow.
     */
    protected BitSet getEventsForTodayAndTomorrow(String programName) {
        BitSet daysNeeded = new BitSet(1);
        Date now = new Date();
        Calendar tomorrowCal = new GregorianCalendar();
        tomorrowCal.setTime(DateUtil.stripTime(now));
        tomorrowCal.add(Calendar.DATE, 1);

        Calendar todayCal = new GregorianCalendar();
        todayCal.setTime(DateUtil.stripTime(now));

        final List<Event> events = eventEAO.findEventOnlyByProgramName(programName);
        for (Event event : events) {
            if (DateUtil.stripTime(event.getStartTime()).getTime() == tomorrowCal
                    .getTimeInMillis()) {
                daysNeeded.set(1);
            } else if (DateUtil.stripTime(event.getStartTime()).getTime() == todayCal
                    .getTimeInMillis()) {
                daysNeeded.set(0);
            }
        }

        return daysNeeded;
    }

    
    /***  NOTE: The following signal utility methods are here and not in
     * the managers for signals and participants because by-value serialization
     * of parameters makes it advantageous to keep them nearby.
     * Candidate for a better idea in the future
     */
    /**
     * Given an existing set of event participant signals
     * (as in, the one currently associated with an event participant)
     * set or replace its contents with a provided set
     * If the existing collection is empty, the new signals are simply added
     * If the existing collection has signals, then the entries are copied
     * New entries are assured to have their parent signal pointer set properly
     *
     * @param existingSignals
     * @param replacements
     */
    protected void setParticipantSignals(Set<EventParticipantSignal> existingSignals, Set<EventParticipantSignal> replacements) {
        // existingSignals better not be null
        // Just better not.
        // It would be nice if the parent object could implement the
        // ThingWithSignals interface or some such.

        if (existingSignals.isEmpty()) {
            for (EventParticipantSignal newSignal : replacements) {
                existingSignals.add(newSignal);
            }
        }
        else {
            // There are already some signals in the existing set
            for (EventParticipantSignal replacement : replacements) {
                setParticipantSignal(existingSignals, replacement);
            }
        }
    }

    /**
     * Given an existing set of signals, add or replace the contents of one
     * If there is already a matching signal in the set, then the entries
     * are copied
     * New entries are assured to have their parent signal pointer set properly
     * @param existingSignals
     * @param replacement
     */
    protected void setParticipantSignal(Set<EventParticipantSignal> existingSignals, EventParticipantSignal replacement) {
        String sigName = replacement.getSignalDef().getSignalName();
        EventParticipantSignal matchingSignal = null;
        for (EventParticipantSignal existing : existingSignals) {
            if (existing.getSignalDef().getSignalName().equals(sigName)) {
                matchingSignal = existing;
            }
        }
        if (matchingSignal == null) {
            // There is no matching signal already in the existing signals set
            // So just jam the new one in there
            Set<? extends SignalEntry> replacementEntries = replacement.getSignalEntries();
            for (SignalEntry entr : replacementEntries) {
                // make sure the back pointer is set for all the entries
                entr.setParentSignal(replacement);
            }
            existingSignals.add(replacement);
        }
        else {
            // There is already a matching signal in the set.
            // So we need to move down and see about very carefully setting
            // or replacing its signal entries
            setSignalEntries(matchingSignal, (Set<SignalEntry>)replacement.getSignalEntries());
        }
    }

    /**
     * Given an existing signal (better not be null) and a set of new entries
     * performs the logic of setting or clearing / replacing the signal entries
     * for that signal
     */
    protected void setSignalEntries(Signal signal, Set<SignalEntry> newEntries) {
        if (signal.getSignalEntries() == null) {
            Set<? extends SignalEntry> empty = new HashSet<EventParticipantSignalEntry>();
            signal.setSignalEntries(empty);
        }
        Set<SignalEntry> existingEntries = (Set<SignalEntry>)signal.getSignalEntries();
        if (newEntries == existingEntries) {
            // exact same collection
            // nothing to do
        } else {
            existingEntries.clear();
            for(SignalEntry ent : newEntries) {
                ent.setParentSignal(signal);
                existingEntries.add(ent);
            }
        }
    }

    /**
     * call this helper method to check whether a program EJB is in-use
     * by any active DRAS program.  This method was implemented as a general
     * helper for use by program implementation beans that can sometimes
     * get their timer processes started even though they are for a program
     * that's unused in the current runtime configuration.
     * 
     * @return true if "this" class name matches the implementation class of an active program
     */
    protected boolean thisProgramIsUsed() {
        boolean thisProgramIsUsed = false;
        List<String> programs = programManager.getPrograms();
        if (programs != null && !programs.isEmpty()) {
            for (String programName : programs) {
                String className = programManager.getProgramClassName(programName);
                //if (program.getClassName().startsWith("com.akuacom.pss2.program.scertp.SCERTPProgramEJB")) {
                String pgn = className+"Bean";
                String tsn = this.getClass().getName();
                if (pgn.equals(tsn)) {
                    thisProgramIsUsed = true;
                }
            }
        }
        return thisProgramIsUsed;
    }

    @Override
    public void addParticipant(Event e, Participant p) {
        //generate event participants
        EventParticipant eventParticipant = generateEventParticipant(e, p);
        Set<EventParticipant> set = new HashSet<EventParticipant>();
        set.add(eventParticipant);
        List<Participant> clients = clientManager.getClients(p.getParticipantName());
        for (Participant client : clients) {
            set.add(generateEventParticipant(e, client));
        }
        e.getEventParticipants().addAll(set);

        // create default signals
        Program program = programManager.getProgramWithParticipantsAndPRules(e.getProgramName());
        try {
            processParticipantRulesAndSignals(program, e, null, eventParticipant, null, null, new Date(), null, null);
        } catch (ProgramValidationException e1) {
            throw new EJBException(e1.getMessage(), e1);
        }

        // adjust pending signals
        for (EventParticipant ep : set) {
            List<EventParticipantSignalEntry> signalEntries = ep.getSignalEntries();
            for (EventParticipantSignalEntry entry : signalEntries) {
                String signalName = entry.getEventParticipantSignal().getSignalDef().getSignalName();
                if ("pending".equals(signalName)) {
                    entry.setTime(new Date());
                }
            }
        }

        // persist
        mergeEvent(e);
    }

    private EventParticipant generateEventParticipant(Event e, Participant p) {
        EventParticipant eventParticipant = new EventParticipant();
        eventParticipant.setEvent(e);
        eventParticipant.setParticipant(p);
        return eventParticipant;
    }

    protected Event mergeEvent(Event event) {
        StackTraceUtil.dumpStack();
        try {
            log.debug("parts " + Dbg.oS(event.getParticipants()));
            event = eventEAO.merge(event);
//            reportParticipation(event);
            return event;
        } catch (Exception e) {
            throw new EJBException(e.getMessage(), e);
        }
    }

    @Override
    public void addEventParticipant(String programName, String eventName, List<EventParticipant> optInParticipants) {
    	
    	try {
			Program program = programManager.getProgramWithParticipantsAndPRules(programName);

			Event event = eventEAO.findEventWithParticipantsAndSignals(eventName);

			List<String> optInParticipantNames = new ArrayList<String>();
			Set<EventParticipant> optInParticipantSet = new HashSet<EventParticipant>();

			optInParticipantSet.addAll(optInParticipants);

			Set<EventParticipant> aggregatedEventParticipants = createAggregatedEventParticipants(
					optInParticipantSet, program);
			optInParticipantSet.addAll(aggregatedEventParticipants);

			Set<EventParticipant> filteredEventParticipants = programManager
					.filterEventParticipants(optInParticipantSet, program, event, true);

			Set<EventParticipant> eventClients = createEventClients(filteredEventParticipants, program);
			filteredEventParticipants.addAll(eventClients);

			for (EventParticipant eventParticipant : filteredEventParticipants) {
				optInParticipantNames.add(eventParticipant.getParticipant().getParticipantName());
			}

			// remove optout participant from current event participants
			List<EventParticipant> toRemoves = new ArrayList<EventParticipant>();
			for (EventParticipant eventPart : event.getEventParticipants()) {
				if (eventPart.getEventOptOut() != 0
						&& optInParticipantNames.contains(eventPart.getParticipant().getParticipantName())) {
					eventPart.getParticipant().getEventParticipants().remove(eventPart);
					toRemoves.add(eventPart);
				}
			}
			if (toRemoves.size() > 0) {
				event.getEventParticipants().removeAll(toRemoves);
			}

			EventStateCacheHelper esch = EventStateCacheHelper.getInstance();
			for (EventParticipant eventParticipant: filteredEventParticipants) {
				if (event.isActive()) {
					eventParticipant.setOptInTime(new Date());
				}
				eventParticipant.setEvent(event);
				esch.delete(eventParticipant.getParticipant().getParticipantName());				
			}
			event.getEventParticipants().addAll(filteredEventParticipants);

			processAddParticipantRulesAndSignals(program, event, filteredEventParticipants);			

			eventEAO.update(event);
			log.info(LogUtils.createLogEntry(
					event.getProgramName(),
					LogUtils.CATAGORY_EVENT,
					"New participants have been opted in to event "+event.getEventName(),
					null));

			sendOptInNotifications(event, filteredEventParticipants);
		} catch (Exception e) {
            String message = "failed to opt in participants to event " + eventName;
            log.error(message, e);
            throw new EJBException(message, e);
		}
    }
    
    protected void processAddParticipantRulesAndSignals(Program programWithPPAndPRules, Event event,
    		Set<EventParticipant> eventParticipantSet)
            throws ProgramValidationException {
		try {
			
			Set<EventSignal> eventSignal=event.getEventSignals();
	        SignalDef pendingSignalDef = signalManager.getSignal("pending");
	        SignalDef modeSignalDef = signalManager.getSignal("mode");
	        
			for (EventParticipant eventParticipant : eventParticipantSet) {
				if (!eventParticipant.getParticipant().isClient()) {
					processParticipantRulesAndSignals(programWithPPAndPRules,
							event, null, eventParticipant,
							null, eventSignal, new Date(),
							pendingSignalDef, modeSignalDef);
					
			        for (EventParticipant ep : eventParticipantSet) {
			        	if (ep.getParticipant().isClient() 
			        			&& ep.getParticipant().getParent().equals(eventParticipant.getParticipant().getParticipantName())) {
				        	for (EventParticipantSignal signal: ep.getSignals()) {
				        		if ("pending".equals(signal.getSignalDef().getSignalName())) {
				        			for (SignalEntry entry:signal.getSignalEntries()) {
//				        				if (event.getEventStatus().equals(EventStatus.ACTIVE))
//				        					entry.setTime(new Date());
				        				entry.setParentSignal(signal);
				        			}
				        		}
				        	}
			        	}
			        }
				}
			}
			

		} catch (Exception e) {
			String message = "can't process rules for event:" + event;
			log.error(message, e);
			throw new EJBException(message, e);
		}
    }
    
    protected void sendOptInNotifications(Event event, Set<EventParticipant> added) {
        
		String emailContentType = null;
		String serverHost = null;
		String utilityDisplayName = null;
		try {
			emailContentType = systemManager.getProperty(
					PropertyName.EMAIL_CONTENT_TYPE).getStringValue();
			serverHost = systemManager.getProperty(PropertyName.SERVER_HOST)
					.getStringValue();
			EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
			utilityDisplayName = cache.getUtilityName("utilityDisplayName");
		} catch (EntityNotFoundException ignore) {
		}

		EventEmailFormatter mailFactory = new EventEmailFormatter();
		StringBuilder operatorMessage = new StringBuilder(
				"The following participants have opted in:\n");

		// send the participant notifications
		for (EventParticipant eventParticipant : added) {
			Participant participant = eventParticipant.getParticipant();
			if (!participant.isClient()) {
				operatorMessage.append(participant.getParticipantName());
				operatorMessage.append("\n");
			}
			String subject = "Your " + utilityDisplayName + " DRAS client "
					+ participant.getParticipantName() + " opted in event "
					+ event.getEventName();
			participant = participantEAO.findParticipantWithContacts(
					participant.getParticipantName(), participant.isClient());
			for (ParticipantContact pc : participant.getContacts()) {
				if (pc == null
						|| !ProgramEJBBean.wantsParticipantEventNotification(
								eventParticipant, pc)) {
					continue;
				}
				String emailContent = mailFactory.generateEmailContent(event,
						new ArrayList<Signal>(), serverHost, emailContentType,
						false, new Date(),null);
				notifier.sendNotification(pc.getParticipantContactAsContact(),
						participant.getParticipantName(), subject,
						emailContent, emailContentType,
						NotificationMethod.getInstance(),
						new NotificationParametersVO(),
						Environment.isAkuacomEmailOnly(), true, false,
						event.getProgramName());
			}
		}

		if (added.size() > 0) {
			EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
			String subject = "Participants have opted in the "
					+ cache.getUtilityName("utilityDisplayName")
					+ " program " + event.getProgramName() + " event "
					+ event.getEventName();

			ProgramEJBBean.sendDRASOperatorEventNotification(subject,
					operatorMessage.toString(),
					NotificationMethod.getInstance(),
					new NotificationParametersVO(), event, notifier);
		}
  	}
    
    @Override
    public void generateDefaultStrategy(ProgramParticipant client){
    	
    }
    
    protected void publicToDRwebsite(String program) {
		topicPublisher.publish(new CacheNotificationMessage(program, null, false, null));
    	
    }
    
}
