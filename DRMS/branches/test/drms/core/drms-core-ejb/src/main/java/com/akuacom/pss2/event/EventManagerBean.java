/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.event.EventManagerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.asynch.EJBAsynHoldingRunnable;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.email.ExceptionNotifierInterceptor;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntryEAO;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.history.ClientParticipationStatus;
import com.akuacom.pss2.history.CustomerReportManager;
import com.akuacom.pss2.history.HistoryEvent;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.dbp.BidState;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.eventtemplate.EventTemplate;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.rds.RDSProgramEJB;
import com.akuacom.pss2.program.sceftp.progAutoDisp.InterruptibleProgramManager;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEAO;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.signal.SignalEntryValue;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.StackTraceUtil;
import com.akuacom.utils.lang.TimingUtil;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * Stateless session bean providing a DRAS Entity BO facade.
 */
@Stateless
public class EventManagerBean implements EventManager.R, EventManager.L {
	// these should probably be properties, but maybe not since the performance
	// of the system is heavily dependent on these
	/** The Constant TIMER_INITIAL_WAIT_MS. */
	// public static final int TIMER_INITIAL_WAIT_MS = 5000; // 5 secs

	/** The Constant TIMER_REFRESH_INTERVAL_MS. */
	// public static final int TIMER_REFRESH_INTERVAL_MS = 5000; // 5 secs

	/** process the event signal entries. */
	private static final int EXECUTE_WINDOW_MS = 10 * TimingUtil.MINUTE_MS; // 10
	// minutes

	//Ram: this has to move to VTN_API
	// Also change in oadrb_adpator PayloadGenerator
	public static final String REQUEST_ID_PREFIX = "AKUACOM.8.3.REQ:";
	public static final String VEN_ID_PREFIX = "AKUACOM.8.3.VEN.ID:";
	public static final String VTN_ID_PROPERTY = "com.honeywell.dras.vtn.VtnId";
	
	
	
	
	/** The participant manager. */
	@EJB
	private ParticipantManager.L participantManager;

	/** The system manager. */
	@EJB
	private SystemManager.L systemManagerBean;

	@EJB
	ClientConversationStateEAO.L clientConversationStateEAO;

	@EJB
	EventEAO.L eventEAO;
	
	@EJB
	HistoryEventEAO.L historyEventEAO;

	@EJB
	EventParticipantEAO.L eventParticipantEAO;

	@EJB
	EventParticipantSignalEntryEAO.L eventParticipantSignalEntryEAO;
	
	@EJB
	CustomerReportManager.L customerReportManager;

	@EJB
	SignalEAO.L signalEAO;

    @EJB
    Notifier.L notifier;
    
    @EJB
	private InterruptibleProgramManager.L irrProgramManager;
    
    /** The session context. */
	// private SessionContext context;

	// private static Map programMap;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(EventManagerBean.class);

	/** The program servicer. */
	@EJB
	private com.akuacom.pss2.program.ProgramManager.L programManager;
	
	@EJB
	private ProgramParticipantAggregationManager.L ppManager;

    /**
     * @see com.akuacom.pss2.email.ExceptionNotifierInterceptor
     */
    @Resource
    private SessionContext sessionContext;

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    @EJB
    protected AsynchCaller.L asynchCaller;
    
	// events
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.event.EventManager#createEvent(java.lang.String,
	 * com.akuacom.pss2.core.model.Event)
	 */
	@Override
	public Collection<String> createEvent(String programName, Event event,
			UtilityDREvent utilityDREvent) {
		Collection<String> names = new ArrayList<String>();
		try {
			ProgramEJB program = systemManagerBean
					.lookupProgramBean(programName);
			names.addAll(program
					.createEvent(programName, event, utilityDREvent));
			
		} catch (Exception e) {
			FireLogEntry logEntry = new FireLogEntry();
			logEntry.setUserParam1(programName);
			logEntry.setCategory(LogUtils.CATAGORY_EVENT);
			String message = "error creating event";
			logEntry.setDescription(message);
			logEntry.setLongDescr(StackTraceUtil.getStackTrace(e));
			throw new EJBException(message, e);
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#createClientConversationState(com.akuacom.pss2
	 * .util.ClientConversationState)
	 */
	@Override
    @Interceptors({ExceptionNotifierInterceptor.class})
	public Collection<String> createEvent(String programName, Event event) {
		return createEvent(programName, event, null);
//        throw new RuntimeException("test failure");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#createClientConversationState(com.akuacom.pss2
	 * .util.ClientConversationState)
	 */
    @Interceptors({ExceptionNotifierInterceptor.class})
	public void createClientConversationState(EventState ent, boolean push) {
		// create a new event state so it can be checked against
		// an event confirmation later
		// Error handling is done in ProgramDataAccessBean
		try {
			com.akuacom.pss2.event.ClientConversationState esDAO = new com.akuacom.pss2.event.ClientConversationState();
			esDAO.setDrasClientId(ent.getDrasClientID());
			esDAO.setEventIdentifier(ent.getEventIdentifier());
			esDAO.setEventModNumber(ent.getEventModNumber());
			esDAO.setConversationStateId((int) ent.getEventStateID());
			esDAO.setProgramName(ent.getProgramName());
			esDAO.setCommTime(new Date());
			esDAO.setPush(push);
            esDAO.setEventStatus(ent.getEventStatus());
            esDAO.setOperationModeValue(ent.getOperationModeValue());
			clientConversationStateEAO.create(esDAO);
		} catch (Exception e) {
			FireLogEntry logEntry = new FireLogEntry();
			logEntry.setUserParam1(ent.getProgramName());
			logEntry.setCategory(LogUtils.CATAGORY_EVENT);
			String message = "ERROR_CREATING_EVENT_STATE";
			logEntry.setDescription(message);
			// logEntry.setLongDescr(StackTraceUtil.getStackTrace(e));
			logEntry.setLongDescr(null);
			log.error(LogUtils.createExceptionLogEntry(ent.getProgramName(),
					LogUtils.CATAGORY_EVENT, e));

			throw new EJBException(message, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.event.EventManager#getClientConversationState(long)
	 */
    @Override
    @Interceptors({ExceptionNotifierInterceptor.class})
	public ClientConversationState getClientConversationState(long eventStateId) {
        return clientConversationStateEAO.findByConversationStateId((int) eventStateId);
	}

	/*
	 * (non-Javadoc)
	 *
     * Deletes a ClientConversationState.
     * ClientConversationState are very dynamic and are tended by concurrent
     * threads, so callers should be prepared to recover from this
     * method throwing an exception.
     * The reason it requires a new transaction is to make
     * DB concurrent access optimistic locking exceptions recoverable,
     * which they otherwise would not be.  Optimistic locking exceptions are
     * common with ClientConversationState because competing threads delete them concurrently
     *
	 * @see com.akuacom.pss2.event.EventManager#removeClientConversationState(long)
	 */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Interceptors({ExceptionNotifierInterceptor.class})
	public void removeClientConversationState(long eventStateId) {
		final com.akuacom.pss2.event.ClientConversationState es = clientConversationStateEAO
				.findByConversationStateId((int) eventStateId);
		if (es != null) {
			try {
				clientConversationStateEAO.delete(es);
			} catch (EntityNotFoundException e) {
				// ignore
			}
		}
	}

	public Collection<String> createEvent(UtilityDREvent utilityDREvent,
			boolean manualCreation) 
	{
		try {
			if (utilityDREvent == null) {
				// TODO: this should throw a validation error
				return null;
			}
			final String programName = utilityDREvent.getProgramName();
			ProgramEJB programEJB = systemManagerBean
					.lookupProgramBean(programName);

			Event event = programEJB.newProgramEvent();
			event.setManual(manualCreation);
			event.setProgramName(programName);
			event.setEventName(utilityDREvent.getEventIdentifier());
			final UtilityDREvent.EventTiming timing = utilityDREvent
					.getEventTiming();
			if (timing != null) {
				event.setReceivedTime(new Date());
				event.setIssuedTime(timing.getNotificationTime()
						.toGregorianCalendar().getTime());
				event.setStartTime(timing.getStartTime().toGregorianCalendar()
						.getTime());
				event.setEndTime(timing.getEndTime().toGregorianCalendar()
						.getTime());
				if (event instanceof DBPEvent) {
					DBPEvent dbp = (DBPEvent) event;
					dbp.setRespondBy(event.getStartTime());
					dbp.setDrasRespondBy(event.getStartTime());
					dbp.setCurrentBidState(BidState.PROCESSING_COMPLETE);
				}
			}

			List<EventParticipant> participants = new ArrayList<EventParticipant>();
			final UtilityDREvent.EventInformation eventInformation = utilityDREvent
					.getEventInformation();
			if (eventInformation != null) {
				for (org.openadr.dras.eventinfo.EventInfoInstance eventInfo : eventInformation
						.getEventInfoInstance()) {
					final org.openadr.dras.eventinfo.EventInfoInstance.Participants parts = eventInfo
							.getParticipants();
					if (parts != null && parts.getAccountID() != null
							&& parts.getAccountID().size() > 0) {
						Participant participant = participantManager
								.getParticipantByAccount(parts.getAccountID()
										.get(0));
						EventParticipant eventParticipant = new EventParticipant();
						eventParticipant.setParticipant(participant);
						eventParticipant.setEvent(event);
						participants.add(eventParticipant);
					}
				}
			}
			event.setParticipants(participants);
			return programEJB.createEvent(programName, event, utilityDREvent);
		} catch (Exception e) {
			String message = "error creating event";
			log.error(LogUtils.createLogEntry(utilityDREvent
					.getProgramName(), LogUtils.CATAGORY_EVENT, message,
					StackTraceUtil.getStackTrace(e)));
			throw new EJBException(message, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.event.EventManager#getEvents()
	 */
	public List<EventInfo> getEvents() {
		List<EventInfo> events = new ArrayList<EventInfo>();
		for (String programName : programManager.getPrograms()) {

			events.addAll(programManager.getEventsForProgram(programName));
		}
		return events;
	}

	public Collection<Event> findAll() {
		return eventEAO.findAll();
	}

	public Collection<Event> findAllPerf() {
		return eventEAO.findAllPerf();
	}
	
	public Event getEventPerf(String eventName){
		try {
			return eventEAO.getEventPerf(eventName);
		} catch (EntityNotFoundException e) {
			return null;
		}
		
	}
	
	
	/**
	 * @deprecated too expensive
	 */
	@Deprecated 
	public Event getEvent(String eventName) {
		try {
			return eventEAO.getByEventName(eventName);
		} catch (EntityNotFoundException e) {
			//String message = "error getting event " + eventName;
			//log.error(LogUtils.createLogEntry(eventName,
			//		LogUtils.CATAGORY_EVENT, message, null));
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.event.EventManager#getEvent(java.lang.String,
	 * java.lang.String)
	 */
	public Event getEvent(String programName, String eventName) {
		return getEvent(eventName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.event.EventManager#getEvent(java.lang.String)
	 */
	public Event getByEventNameWithParticipants(String eventName) {
		try {
			return eventEAO.getByEventNameWithParticipants(eventName);
		} catch (EntityNotFoundException e) {
			String message = "error getting event " + eventName;
			log.warn(LogUtils.createLogEntry(eventName,
					LogUtils.CATAGORY_EVENT, message, null));
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#getEventStatusString(java.lang.String
	 * , java.lang.String)
	 */
	public String getEventStatusString(Event event) {
		ProgramEJB programEJB = systemManagerBean.lookupProgramBean(event
				.getProgramName());
        if (programEJB != null)
        	return programEJB.getEventStatusString(event);
        else
        	return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.event.EventManager#updateEvent(java.lang.String,
	 * java.lang.String, com.akuacom.pss2.core.model.Event,
	 * org.openadr.dras.utilitydrevent.UtilityDREvent)
	 */
	public void updateEvent(String programName, String eventName, Event event,
			UtilityDREvent utilityDREvent) {
		ProgramEJB programEJB = systemManagerBean
				.lookupProgramBean(programName);
		programEJB.updateEvent(programName, eventName, event, utilityDREvent);
	}
	
	public void endEvent(String programName,String eventName,Date endTime){
		ProgramEJB programEJB = systemManagerBean
				.lookupProgramBean(programName);
		programEJB.endEvent(programName, eventName,endTime);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.event.EventManager#removeEvent(java.lang.String,
	 * java.lang.String)
	 */
	public void removeEvent(String programName, String eventName) {
		ProgramEJB programEJB = systemManagerBean
				.lookupProgramBean(programName);
		programEJB.cancelEvent(programName, eventName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#updateEventTemplate(org.openadr.dras
	 * .akuautilitydrevent.UtilityDREvent)
	 */
	public void updateEventTemplate(
			org.openadr.dras.akuautilitydrevent.UtilityDREvent event) {
		try {
			ProgramEJB programEJB = systemManagerBean.lookupProgramBean(event
					.getProgramName());
			if (programEJB instanceof RDSProgramEJB) {
				RDSProgramEJB rds = (RDSProgramEJB) programEJB;
				rds.updateEventTemplate(event.getProgramName(), event);
			} else {
				throw new EJBException(
						"This program doesn't support event template.");
			}
		} catch (Exception e) {
			String message = "error get event template "
					+ event.getProgramName();
			// DRMS-1654
			//log.error(message, e);
			//DRMS-5623
			log.error(LogUtils.createExceptionLogEntry(event.getProgramName(),
					LogUtils.CATAGORY_EVENT, e));
			
			throw new EJBException(message, e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#createEventTemplate(org.openadr.dras
	 * .akuautilitydrevent.UtilityDREvent)
	 */
	public void createEventTemplate(
			org.openadr.dras.akuautilitydrevent.UtilityDREvent event) {
		try {
			if (event == null) {
				throw new Exception("null event template object.");
			}

			final String programName = event.getProgramName();
			ProgramEJB programEJB = systemManagerBean
					.lookupProgramBean(programName);
			if (!(programEJB instanceof RDSProgramEJB)) {
				throw new Exception("Only RDS programs support event template");
			}
			RDSProgramEJB rds = (RDSProgramEJB) programEJB;
			rds.createEventTemplate(programName, event);

		} catch (Exception e) {
			String message = "error creating event:" + e.getMessage();
			throw new EJBException(message, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#deleteEventTemplate(java.lang.String)
	 */
	public void deleteEventTemplate(String programName) {

	}

	/**
	 * Gets the event template.
	 * 
	 * @param programName
	 *            the program name
	 * @param eventTemplateName
	 *            the event template name
	 * 
	 * @return the event template
	 */
	public EventTemplate getEventTemplate(String programName,
			String eventTemplateName) {
		try {
			ProgramEJB programEJB = systemManagerBean
					.lookupProgramBean(programName);
			if (programEJB instanceof RDSProgramEJB) {
				RDSProgramEJB rds = (RDSProgramEJB) programEJB;
				return rds.getEventTemplate(eventTemplateName);
			} else {
				throw new EJBException("This program: " + programName
						+ " doesn't support event template.");
			}
		} catch (Exception e) {
			String message = "error get event template " + eventTemplateName;
			// DRMS-1654
			//log.error(message, e);
			//DRMS-5623
			log.error(LogUtils.createExceptionLogEntry(programName,
					LogUtils.CATAGORY_EVENT, e));
			throw new EJBException(message, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#getEventTemplate(java.lang.String)
	 */
	public EventTemplate getEventTemplate(String programName) {
		try {
			ProgramEJB programEJB = systemManagerBean
					.lookupProgramBean(programName);
			if (programEJB instanceof RDSProgramEJB) {
				RDSProgramEJB rds = (RDSProgramEJB) programEJB;
				return rds.getEventTemplateByProgram(programName);
			} else {
				throw new EJBException(
						"This program doesn't support event template.");
			}
		} catch (Exception e) {
			String message = "error get event template " + programName;
			// DRMS-1654
			//log.error(message, e);
			//DRMS-5623
			log.error(LogUtils.createExceptionLogEntry(programName,
					LogUtils.CATAGORY_EVENT, e));
			throw new EJBException(message, e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#getEventParticipantSignalEntries(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Set<? extends SignalEntry> getEventParticipantSignalEntries(
			EventParticipant eventParticipant, String signalName) {
		for (EventParticipantSignal signal : eventParticipant.getSignals()) {
			if (signal.getSignalDef().getSignalName().equals(signalName)) {
				return signal.getSignalEntries();
			}
		}
		return new HashSet<EventParticipantSignalEntry>();
	}

	private EventParticipantSignal getEventParticipantSignal(
			EventParticipant eventParticipant, String signalName) {
		for (EventParticipantSignal signal : eventParticipant.getSignals()) {
			if (signal.getSignalDef().getSignalName().equals(signalName)) {
				return signal;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#addEventParticipantSignalEntry(java
	 * .lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.util.Date, com.akuacom.pss2.core.model.EventParticipantSignalEntry)
	 */
	public void addEventParticipantSignalEntry(
			EventParticipant eventParticipant, String signalName,
			EventParticipantSignalEntry entry) {
		try {
			final SignalDef def = signalEAO.getSignal(signalName);
			if (def == null) {
				String message = "signal name: " + signalName
						+ " doesn't exist";
				log.error(message);
				throw new EJBException(message);
			}
		} catch (AppServiceException e) {
			throw new EJBException(e);
		}
		// TODO: check entry timing...
		final EventParticipantSignal participantSignal = getEventParticipantSignal(
				eventParticipant, signalName);
		Set<? extends SignalEntry> signalEntries = participantSignal
				.getSignalEntries();
		if (signalEntries.contains(entry)) {
			String message = "entry already exists: signalName = " + signalName
					+ ", time = " + entry.getTime();
			log.error(message);
			throw new EJBException(message);
		}
		entry.setEventParticipantSignal(participantSignal);
		((Set) signalEntries).add(entry);
		// TODO: should be update on eao
		setEventParticipant(eventParticipant);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.ProgramDataAccess#removeEventParticipantSignalEntry
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.util.Date)
	 */
	public void removeEventParticipantSignalEntry(
			EventParticipant eventParticipant, String signalName, Date time) {
		Set<? extends SignalEntry> signalEntries = getEventParticipantSignalEntries(
				eventParticipant, signalName);

		// TODO: make more efficient by taking advantage of sort order
		Iterator<? extends SignalEntry> i = signalEntries.iterator();
		while (i.hasNext()) {
			SignalEntry entry = i.next();
			if (entry.getTime().getTime() == time.getTime()) {
				i.remove();
				setEventParticipant(eventParticipant);
				return;
			}
		}
		String message = "entry doesn't exists: signalName = " + signalName
				+ ", time = " + time;
		log.error(message);
		throw new EJBException(message);
	}

	/*
	 * (non-Javadoc)
	 */
	public void addParticipantsToEvent(String eventID, String participantName) {
        //Event event = getEvent(eventID);
		Event event = this.getEventAll(eventID);
        Participant participant = participantManager.getParticipant(participantName);
        ProgramEJB programEJB = systemManagerBean.lookupProgramBean(event.getProgramName());
        programEJB.addParticipant(event, participant);
    }


	// TODO: this breaks the rule in the DRAS that events don't have to be

	// unique across all programs - talk to ed
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#getParticipantsForEvent(java.lang
	 * .String)
	 */
	public List<String> getParticipantsForEvent(String eventID) {
		return null; // To change body of implemented methods use File |
		// Settings | File Templates.
	}

	private EventEmailFormatter getEventEmailFormatter(EventParticipant ep){
		String programName = ep.getEvent().getProgramName();
		String className = programManager.getProgramClassName(programName);
		if("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013".equalsIgnoreCase(className)){
			return new SceEventEmailFormatter("com.akuacom.pss2.core.SceEmailResourceUtil");
		}
			
		return new EventEmailFormatter();
	}
    private void sendOptOutNotifications(ArrayList<EventParticipant> toRemove, 
    	Date nearTime)
	{
    	if(toRemove == null||toRemove.isEmpty())
    	{
    		return;
    	}
    	
        
    	String emailContentType = null;
    	String serverHost = null;
    	String utilityDisplayName = null;
        try {
        	emailContentType =systemManagerBean.getProperty(PropertyName.EMAIL_CONTENT_TYPE).getStringValue();
        	serverHost = systemManagerBean.getProperty(PropertyName.SERVER_HOST).getStringValue();
        	utilityDisplayName = systemManagerBean.getProperty(PropertyName.UTILITY_DISPLAY_NAME).getStringValue();
		} catch (EntityNotFoundException ignore) {}
        
        EventEmailFormatter mailFactory = getEventEmailFormatter(toRemove.iterator().next());
        StringBuilder operatorMessage = 
        	new StringBuilder("The following participants have opted out:\n");
        
        // send the participant notifications
    	for(EventParticipant eventParticipant: toRemove)
    	{
    		Participant participant = eventParticipant.getParticipant();
    		Event event = eventParticipant.getEvent();
    		if(!participant.isClient())
    		{
	    		operatorMessage.append(participant.getParticipantName());
	    		operatorMessage.append("\n");
    		}
            String subject = "Your " + utilityDisplayName
                    + " DRAS client " + participant.getParticipantName()
                    + " opted out of event " + event.getEventName();
	        for (ParticipantContact pc : participant.getContacts()) 
	        {
				if (pc == null || 
					!ProgramEJBBean.wantsParticipantEventNotification(
					eventParticipant,  pc)) 
				{
				    continue;
				}
                String emailContent = mailFactory.generateEmailContent(event,
                        new ArrayList<Signal>(), serverHost, emailContentType,
                        false, nearTime,Boolean.TRUE);
				notifier.sendNotification(pc.getParticipantContactAsContact(),
				        participant.getParticipantName(), subject,
				        emailContent, emailContentType,
				        NotificationMethod.getInstance(),
				        new NotificationParametersVO(),
				        Environment.isAkuacomEmailOnly(), true, false,
				        event.getProgramName());
	        }
    	}
        	
    	if(toRemove.size() > 0)
    	{
    		Event event = toRemove.get(0).getEvent();
	        String subject = "Participants have opted out of the " +
	        		systemManagerBean.getPss2Properties().getUtilityDisplayName()
	                + " program "
	                + event.getProgramName()
	                + " event " + event.getEventName();
	        
	        ProgramEJBBean.sendDRASOperatorEventNotification(subject, 
	        	operatorMessage.toString(),  NotificationMethod.getInstance(), 
	        	new NotificationParametersVO(), event, notifier);
    	}

	}

    @Override
	public void removeParticipantFromEvent(String eventName, String participantName) {
    	removeParticipantFromEvent(eventName, participantName, false);
    	try {
			String utilityName = systemManagerBean.getProperty(PropertyName.UTILITY_NAME).getStringValue();
			if(utilityName != null && utilityName.equalsIgnoreCase("PGE")) {
				Participant participant = participantManager.getParticipant(participantName);
		    	if(participant != null) {
		    		Set<ProgramParticipant> programParticipants = participant.getProgramParticipants();
		    		for(ProgramParticipant pp: programParticipants) {
		    			Set<ProgramParticipant> descendants = ppManager.getDescendants(pp);		    			
		    			 for(ProgramParticipant ppd: descendants) {
		    				 removeParticipantFromEvent(eventName, ppd.getParticipantName(), false);
						 }	
		    		}
		    	}    
			}
		} catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}   
	}
	/**
	 * Removes participant and it's clients from event
	 * 
	 */
    @Interceptors({ExceptionNotifierInterceptor.class})
	public void removeParticipantFromEvent(String eventName,
        String participantName, boolean delete) {
        try {
        	Event event = eventEAO.findEventWithParticipantsAndSignals(eventName);
                 
            Date nearTime = null;
            
        	ArrayList<EventParticipant> toRemove = new ArrayList<EventParticipant>();
            boolean isActive = event.getEventStatus() == EventStatus.ACTIVE;

            int status=ClientParticipationStatus.INACTIVE_EVENT_OPT_OUT.getValue();
    		if (isActive) {
    			status=ClientParticipationStatus.ACTIVE_EVENT_OPT_OUT.getValue();
    		}

            EventStateCacheHelper esch = EventStateCacheHelper.getInstance();
            for (EventParticipant ep : event.getEventParticipants()) {
            	boolean remove = false;
                if (ep.getParticipant().isClient()) {
	            	// if this is a client of the designated participant
	                // or this is a client of a descendant of the designated participant
                	if(ep.getParticipant().getParent().equals(participantName)) 
                            // || pp.descendantOfQ(programParticipant)
                	{
                		// TODO: this code only get's the first near time which
                		// is fine since all clients are the same. when the near
                		// time is stored in the event participant, 
                		// we'll just get it from there (and we'll have a list of them)
                		if(nearTime == null) {
	                		Set<EventParticipantSignal> participantSignals = ep.getSignals();
					        for (Signal programSignal : participantSignals) {
					        	if(programSignal.getSignalDef().getSignalName().equalsIgnoreCase("pending"))
					    		{
					        		for(SignalEntry pse : programSignal.getSignalEntries())
					        		{
					        			if(pse.getParentSignal().getSignalDef().isLevelSignal()
					        				&& pse.getParentSignal().getSignalDef().getSignalName().equalsIgnoreCase("pending")
					        				&& pse.getLevelValue().equalsIgnoreCase("on"))
					        			{
					        				nearTime = pse.getTime();
					        				break;
					        			}
					        		}
					    			break;
					    		}
					        }
                		}
                		remove = true;
                	}
                }
                else
                {
	            	// if this is the designated participant,
	            	// or this is a descendant of the designated participant,
                	if(ep.getParticipant().getParticipantName().equals(participantName) 
                            // || pp.descendantOfQ(programParticipant)
                            )
                	{
				        remove = true;
                	}
                 }
                if(remove)
                {
                	ep.setEventOptOut(status);
                	ep.setOptOutTime(new Date());
            		esch.delete(ep.getParticipant().getParticipantName());

	                toRemove.add(ep);
//	                // need to remove both sides
	                if (delete)
	                	ep.getParticipant().getEventParticipants().remove(ep);
                }

            }
            // need to delete here to avoid concurrent modification exception on
            // iterator.
            if (delete) {
            	reportEventParticipant(toRemove, event);
            	event.getEventParticipants().removeAll(toRemove);
            }
            
            eventEAO.update(event);

            // notify push clients right now
            pushReturnToIdleSignal(toRemove);
            
            // send notifications
            sendOptOutNotifications(toRemove, nearTime);
        } catch (EntityNotFoundException ex) {
            // ignore
        }
    }
    
    /**
     * This method is used to actively tell opted-out push clients
     * that they're no longer in the event.  Otherwise, they'll
     * just keep doing whatever their last in-event order was.
     *
     * @param participants toRemove
     */
    private void pushReturnToIdleSignal(ArrayList<EventParticipant> participants)
	{
        if (participants != null && !participants.isEmpty()) {
            ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
            for (EventParticipant eventParticipant : participants) {
                Participant participant = eventParticipant.getParticipant();
                if (participant.isClient() && participant.getPush() != 0) {
                    clientManager.pushClientEventState(participant);
                }
            }
        }
    }    
    
	
	protected void reportEventParticipant(List<EventParticipant> eventParticipants, Event event){
			customerReportManager.reportEventParticipant(eventParticipants, event);
//			for(EventParticipant ep:eventParticipants){
//				String participantName = ep.getParticipant().getParticipantName();
//				EventStateCacheHelper.getInstance().delete(participantName);
//			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#updateEventParticipants(java.lang
	 * .String, java.util.List)
	 */
	public void updateEventParticipants(String eventID,
			List<EventParticipant> changedEventParts) {
		// TODO lin: this was added for UtilityOperator standard support.
		// comment this out
		// since the current system may not support this well.

		try {
			List<EventParticipant> droppedParticipants = new ArrayList<EventParticipant>();
			Event event = this.getEventWithParticipants(eventID);

			if (changedEventParts != null && changedEventParts.size() > 0) {
				for (EventParticipant participant : event.getParticipants()) {
					boolean drop = true;
					Iterator<EventParticipant> i = changedEventParts.iterator();
					while (i.hasNext()) {
						EventParticipant eventParticipant = i.next();
						if (participant.getParticipant().getAccountNumber()
								.equals(
										eventParticipant.getParticipant()
												.getAccountNumber())) {
							// participant.setContactTypes(eventParticipant.getContactTypes());

							participant.getParticipant().setParticipantName(
									eventParticipant.getParticipant()
											.getParticipantName());
							drop = false;
							break;
						}
					}

					if (drop) {
						droppedParticipants.add(participant);
					}
				}

				if (droppedParticipants.size() != 0) {
					// TODO lin: need to send signal here as well.
					StringBuilder message = new StringBuilder("event "
							+ event.getEventName() + " specifies "
							+ "participants that aren't in program:\n");
					for (EventParticipant eventParticipant : droppedParticipants) {
						message.append(eventParticipant.getParticipant()
								.getParticipantName());
						message.append("\n");
					}
					log.warn(message.toString());
				}
			}
			event.setParticipants(changedEventParts);
		} catch (Exception e) {
			String message = "error updating event participants for event "
					+ eventID;
			log.warn(message);
			throw new EJBException(message);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.ProgramManager#createProgramParticipant(java
	 * .lang.String, java.lang.String,
	 * com.akuacom.pss2.core.entities.ProgramParticipant)
	 */
	public EventParticipant createEventParticipant(String eventName,
			String participantName, boolean isClient, EventParticipant ep) {
		// DRMS-1654
		if (eventName == null || ep.getEvent().getEventName() == null) {
			String message = "event name is null";
			log.debug(message);
			throw new EJBException(message);
		}
		if (!eventName.equals(ep.getEvent().getEventName())) {
			String message = "event names do not match";
			log.debug(message);
			throw new EJBException(message);
		}
		if (participantName == null
				|| ep.getParticipant().getParticipantName() == null) {
			String message = "participant name is null";
			log.debug(message);
			throw new EJBException(message);
		}
		if (!participantName.equals(ep.getParticipant().getParticipantName())) {
			String message = "participant names do not match";
			log.debug(message);
			throw new EJBException(message);
		}
		if (ep.getUUID() != null) {
			String message = "Id is set during create";
			log.debug(message);
			throw new EJBException(message);
		}

		EventParticipant ep2 = this.getEventParticipant(eventName,
				participantName, isClient);
		if (ep2 != null) {
			String message = "Duplicate event participant";
			log.debug(message);
			throw new EJBException(message);
		}

		return this.setEventParticipant(ep);
	}

	@Override
	public void addEventParticipants(String programName, String eventName, List<EventParticipant> eps)  {
		ProgramEJB programEJB = systemManagerBean
				.lookupProgramBean(programName);
		programEJB.addEventParticipant(programName, eventName, eps);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.ProgramManager#updateProgramParticipant(java
	 * .lang.String, java.lang.String,
	 * com.akuacom.pss2.core.entities.ProgramParticipant)
	 */
	public EventParticipant updateEventParticipant(String eventName,
			String participantName, boolean isClient, EventParticipant ep) {
		// DRMS-1654
		if (eventName == null || ep.getEvent().getEventName() == null) {
			String message = "event name is null";
			log.debug(message);
			throw new EJBException(message);
		}
		if (!eventName.equals(ep.getEvent().getEventName())) {
			String message = "event names do not match";
			log.debug(message);
			throw new EJBException(message);
		}
		if (participantName == null
				|| ep.getParticipant().getParticipantName() == null) {
			String message = "participant name is null";
			log.debug(message);
			throw new EJBException(message);
		}
		if (!participantName.equals(ep.getParticipant().getParticipantName())) {
			String message = "participant names do not match";
			log.debug(message);
			throw new EJBException(message);
		}
		if (ep.getUUID() == null) {
			String message = "Id not set";
			log.debug(message);
			throw new EJBException(message);
		}
		return this.setEventParticipant(ep);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#getEventParticipant(java.lang.String,
	 * java.lang.String)
	 */
	public EventParticipant getEventParticipant(String eventName,
			String partipantName, boolean isClient) {
		EventParticipant ep = eventParticipantEAO.getEventParticipant(
				eventName, partipantName, isClient);

		return ep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.core.ProgramDataAccess#getParticipantsForEvent(java.
	 * lang.String, java.lang.String)
	 */
	public List<EventParticipant> getEventParticipantsForEvent(String eventName) {
		try {
			if (eventName == null) {
				String message = "event name is null";
				// DRMS-1654
				log.debug(message);
				throw new EJBException(message);
			}

			return eventParticipantEAO.findByEvent(eventName);
		} catch (Exception ex) {
			String message = "error getting participant for event " + eventName;
			// DRMS-1654
			log.debug(message, ex);
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.event.EventManager#setEventParticipant(com.akuacom.pss2
	 * .core.entities.EventParticipantEAO)
	 */
	public EventParticipant setEventParticipant(EventParticipant ep) {
		try {
			// check if any rule info has been set
			if (ep.getEventRules() != null && ep.getEventRules().size() > 0) {
				for (EventParticipantRule rule : ep.getEventRules()) {
					if (rule != null
							&& (rule.getStart() != null
									|| rule.getEnd() != null
									|| rule.getMode() != null
									|| rule.getVariable() != null
									|| rule.getOperator() != null
									|| rule.getValue() != null
									|| rule.getSortOrder() != null || rule
									.getSource() != null)) {
						rule.setEventParticipant(ep);
					}
				}
			}

			ep = eventParticipantEAO.update(ep);
		} catch (Exception e) {
			throw new EJBException("ERROR_EVENT_PARTICIPANT_CREATE: " + ep
					+ " | " + e.getMessage(), e);
		}

		EventParticipant resEP = getEventParticipant(ep.getEvent()
				.getEventName(), ep.getParticipant().getParticipantName(), ep
				.getParticipant().isClient());
		return resEP;
	}

	/**
	 * Gets the signal state for event participant.
	 * 
	 * @param ePart
	 *            the e part
	 * @param signalName
	 *            the signal name
	 * 
	 * @return the signal state for event participant
	 */
	public String getSignalValueForEventParticipantAsString(
			EventParticipant ePart, String signalName) {
		List<EventParticipantSignalEntry> signalEntries = ePart
				.getSignalEntries();
		Collections.sort(signalEntries);
		Collections.reverse(signalEntries);
		for (EventParticipantSignalEntry signalEntry : signalEntries) {
			if (signalEntry.getTime().getTime() < System.currentTimeMillis()
					&& signalEntry.getParentSignal().getSignalDef()
							.getSignalName().equals(signalName)) {
				if (signalEntry.getParentSignal().getSignalDef()
						.isLevelSignal()) {
					return signalEntry.getStringValue();
				} else {
					return Double.toString(signalEntry.getNumberValue());
				}
			}
		}

		// return default
		SignalDef signal = EJBFactory.getBean(SignalManager.class).getSignal(
				signalName);
		if (signal.isLevelSignal()) {
			return signal.getLevelDefault();

		} else {
			return Double.toString(signal.getNumberDefault());
		}
	}

	private static SignalManager _signalManager = null;

	SignalManager getSignalManager() {
		if (_signalManager == null) {
			_signalManager = EJBFactory.getBean(SignalManager.class);
		}
		return _signalManager;
	}

	private Event getEventByID(String eventID) {
		Event event;
		try {
			event = eventEAO.getById(eventID);
		} catch (EntityNotFoundException ex) {
			log.warn("can't finde event with id " + eventID, ex);
			throw new EJBException(ex);
		}
		return event;
	}

	private EventSignal getSignal(String eventID, String signalName) {
		EventSignal signal = null;
		Event event = getEventByID(eventID);
		Set<EventSignal> evtSignals = event.getEventSignals();
		if (evtSignals != null) {
			for (EventSignal sig : evtSignals) {
				SignalDef def = sig.getSignalDef();
				if (def.getSignalName().equalsIgnoreCase(signalName)) {
					signal = sig;
				}
			}
		}
		return signal;
	}

	@Override
	public void resetSignal(String eventID, String signalName) {
		EventSignal signal = getSignal(eventID, signalName);
		if (signal != null) {
			Set<EventSignalEntry> entries = signal.getEventSignalEntries();
			if (entries != null) {
				entries.clear();
			}
		}
	}

	@Override
	public void setSignalEntries(String eventID, String signalName,
			Set<EventSignalEntry> newEntries, boolean updateVersion) {
		Signal signal = getSignal(eventID, signalName);
		// Can't use the setSignalEntries in SignalManager because it's a remote
		// call
		// and ends up setting a copy of the signal entries. Foo.
		if (signal.getSignalEntries() == null) {
			Set<EventSignalEntry> empty = new HashSet<EventSignalEntry>();
			signal.setSignalEntries(empty);
		}
		Set<EventSignalEntry> existingEntries = (Set<EventSignalEntry>) signal
				.getSignalEntries();
		if (newEntries == existingEntries) {
			// exact same collection
			// nothing to do
		} else {
			existingEntries.clear();
			for (EventSignalEntry ent : newEntries) {
				ent.setParentSignal(signal);
				existingEntries.add(ent);
			}
		}
	}

	@Override
	public Set<EventSignalEntry> getSignalEntries(String eventID,
			String signalName) {
		EventSignal signal = getSignal(eventID, signalName);
		Set<EventSignalEntry> entries = (Set<EventSignalEntry>)signal.getSignalEntries();
		if (entries == null) {
			// first request. Create an empty collection
			entries = new HashSet<EventSignalEntry>();
		}
		return entries;
	}

	@Override
	public Set<SignalDef> listSignals(String eventID) {
		Set<SignalDef> signalList = new HashSet<SignalDef>();
		Event event = getEventByID(eventID);
		Set<EventSignal> eventSignals = event.getEventSignals();
		if (eventSignals != null && !eventSignals.isEmpty()) {
			for (EventSignal signal : eventSignals) {
				SignalDef def = signal.getSignalDef();
				signalList.add(def);
			}
		}
		return signalList;
	}

	@Override
	public SignalEntry getSignalEntry(String eventID, String signalName,
			Date atTime) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeSignalEntriesAfter(String eventID, String signalName,
			Date afterTime) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removePastSignalEntries(String eventID, String signalName,
			Date afterTime) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setSignalEntry(String eventID, String signalName, Date atTime,
			double numberValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setSignalEntry(String eventID, String signalName, Date atTime,
			String levelValue) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void deleteSignalEntry(String eventID, String signalName, Date atTime) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<SignalEntryValue> getAllEventSignalEntryies(String eventID) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<SignalEntryValue> getAllSignalEntries(String eventID,
			String signalName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void deleteSignalEntries(String eventID, Set<SignalEntryValue> values) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setSignalEntries(String eventID, Set<SignalEntryValue> values) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getSignalEntryString(String eventID, String signalName,
			Date atTime) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Event> getByDate(Date startTime, Date endTime) {
		return eventEAO.findByDate(startTime, endTime);
	}

	@Override
	public List<Event> getByProgramAndDate(Date startTime, Date endTime,
			String programName) {
		return eventEAO.findByProgramAndDate(startTime, endTime, programName);
	}

	@Override
	public HistoryEvent getByEventName(String eventName)
			throws EntityNotFoundException {
		return historyEventEAO.findByEventName(eventName);
	}

	@Override
	public List<HistoryEvent> findByDate(Date startTime, Date endTime) {
		return historyEventEAO.findByDate(startTime, endTime);
	}

	@Override
	public List<HistoryEvent> findByProgramAndDate(Date startTime,
			Date endTime, String programName) {
		return historyEventEAO.findByProgramAndDate(startTime, endTime, programName);
	}

	@Override
	public List<Event> findByParticipantAndDate(Date startTime, Date endTime,
			List<String> participantNames) {
		// TODO Auto-generated method stub
		return eventEAO.findByParticipantAndDate(startTime, endTime, participantNames);
	}

	@Override
	public List<HistoryEvent> findHisEventByParticipantAndDate(Date startTime,
			Date endTime, List<Object> participantNames) {
		return historyEventEAO.findByParticipantAndDate(startTime, endTime, participantNames);
	}

    @Override
    public List<Event> findAllPossibleByParticipant(String participantName) {
        return eventEAO.findAllPossibleByParticipant(participantName);
    }

	@Override
	public List<Event> findByParticipantProgramAndDate(Date startTime,
			Date endTime, List<String> participantNames,
			List<String> programNames) {
		// TODO Auto-generated method stub
		return eventEAO.findByParticipantProgramAndDate(startTime, endTime, participantNames, programNames);
	}

	@Override
	public List<HistoryEvent> findHisEventByParticipantProgramAndDate(
			Date startTime, Date endTime, List<Object> participantNames,
			List<Object> programNames) {
		// TODO Auto-generated method stub
		return historyEventEAO.findByParticipantProgramAndDate(startTime, endTime, participantNames, programNames);
	}
	

	public List<Event> getEventOnlyByProgramName(String programName) {
		return eventEAO.findEventOnlyByProgramName(programName);
	}

	public Event getEventOnly(String eventName) {
		return eventEAO.findEventOnlyByEventName(eventName);
	}

	@Override
	public Event getEventWithEventSignals(String eventName) {
		return eventEAO.findEventWithEventSignalsByEventName(eventName);
	}

	@Override
	public Event getEventWithParticipants(String eventName) {
		return eventEAO.findEventWithParticipantsByEventName(eventName);
	}

	@Override
	public Event getEventWithParticipantAndBids(String eventName) {
		return eventEAO.findEventWithParticipantsAndBidsByEventName(eventName);
	}

	@Override
	public Event getEventWithParticipantsAndSignals(String eventName) {
		return eventEAO.findEventWithParticipantsAndSignals(eventName);
	}

	@Override
	public Event getEventAll(String eventName) {
		return eventEAO.findEventAllByEventName(eventName);
	}

	@Override
	public List<Event> findByAggregatorProgramAndDate(Date startTime,
			Date endTime, List<String> participantNames,
			List<String> programNames) {
		return eventEAO.findByAggregatorProgramAndDate(startTime, endTime, participantNames, programNames);
	}

	@Override
	public List<HistoryEvent> findHisEventByAggregatorProgramAndDate(
			Date startTime, Date endTime, List<String> participantNames,
			List<String> programNames) {
		return historyEventEAO.findByAggregatorProgramAndDate(startTime, endTime, participantNames, programNames);
	}

	@Override
	public void endEvent(String programName, List<String> locations,
			Date startTime, Date endTime) {
		List<Event> events = eventEAO.findEventParticipantByEventTime(programName, startTime);
		for(Event event:events){
			endEvent(programName,event,locations,endTime);
		}
	}
	
	protected void endEvent(String programName,Event event,List<String> locations,Date endTime){
		if(locations==null || locations.size()==0) return;
		List<String> eventLocations = BIPEventUtil.getAllEventLocations(event);
		if(locations.size()==eventLocations.size() && eventLocations.containsAll(locations)){
			//end event directly 
			this.endEvent(programName, event.getEventName(),endTime);
		}else{
			BIPEventUtil.updateEventLocationStr(event, locations, endTime);
			Date max = BIPEventUtil.getBiggestEndTime(event);
			if(endTime!=null && max!=null && endTime.after(max))
				event.setEndTime(endTime);
			else
				event.setEndTime(max);
			try {
				//This is only a temp solution for BIP end IR events by location
				irrProgramManager.endEvent("TOU-BIP", "TOU-BIP", "ABANK", locations,event.getStartTime(), endTime);
				
				eventEAO.update(event);
			} catch (EntityNotFoundException e) {
				log.error(e);
			}
		}
	}
	
	public void removeParticipantsFromEvent(String eventName,String participantNames){
		String names[]= participantNames.split(",");
		for(String p: names){
			this.removeParticipantFromEvent(eventName, p, false);
		}
	}
	public Map<String,Event>findEventIdEventObjectMap(List<String> eventNames){
		return eventEAO.findEventIdEventObjectMap(eventNames);
	}
}