package com.akuacom.pss2.program.dl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.Depends;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.participant.DemandLimitingProgramParticipantState;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.EventStatus;

/**
 * Message-Driven Bean implementation class for: DemandLimitingDispatcher
 * 
 */
@MessageDriven(name = "DemandLimitingDispatcherMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable") })
@Resources(@Resource(name = "jms/QueueFactory", type = QueueConnectionFactory.class))
@Depends("jboss.messaging.destination:service=Queue,name=participantIntervalMeterDataUpdate")
public class DemandLimitingDispatcher implements MessageListener {

	public static final String PROGRAM_ACRONYM = "DL";
	@EJB
	ProgramParticipantManager.L programParticipantManager;

	@EJB
	EventManager.L eventManager;
	@EJB
	SignalManager.L signalManager;
	@EJB
	DemandLimitingProgramEJB.L demandLimitingProgramEJB;
	@EJB
	SystemManager.L systemManager;
	@EJB
	protected EventEAO.L eventEAO;

	ProgramParticipant demandLimitingProgramParticipant = null;
	DemandLimitingProgramParticipantState demandLimitingProgramParticipantState = null;
	PDataEntry dataEntry = null;
	private static final Logger log = Logger
			.getLogger(DemandLimitingDispatcher.class);

	public DemandLimitingDispatcher() {

	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	// @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void onMessage(Message message) {
		PSS2Features pss2Features = systemManager.getPss2Features();
		if ((pss2Features != null) && (pss2Features.isDemandLimitingEnabled())
				&& (programParticipantManager != null) && (message != null)
				&& (message instanceof ObjectMessage)) {
			try {
				Serializable objectMessage = ((ObjectMessage) message)
						.getObject();
				if (objectMessage instanceof PDataEntry) {
					dataEntry = (PDataEntry) objectMessage;
					if (dataEntry.getDatasource() != null) {

						demandLimitingProgramParticipant = programParticipantManager
								.getProgramParticipant(
										DemandLimitingProgram.PROGRAM_NAME,
										dataEntry.getDatasource().getOwnerID(),
										false);
						demandLimitingProgramParticipantState = new DemandLimitingProgramParticipantState(
								demandLimitingProgramParticipant);

						if ((isParticipantDemandLimitingEnabled())
								&& (isValidDataEntryToProcess())) {
							demandLimitingProgramParticipantState
									.setTelemetryData(dataEntry.getValue(),
											dataEntry.getTime());
							Event event = null;
							if (demandLimitingProgramParticipantState
									.getSignalize()) {
								event = sendEvent();
							}

							if (pss2Features
									.isDemandLimitingNotificationsEnabled()
									&& demandLimitingProgramParticipantState
											.getNotify()) {
								if ((!demandLimitingProgramParticipantState
										.getSignalize()) && (event != null))
									event = getUpdatedEvent();
								sendNotification(event);
							}

							demandLimitingProgramParticipant = demandLimitingProgramParticipantState
									.getProgramParticipant();
							programParticipantManager.updateProgramParticipant(
									DemandLimitingProgram.PROGRAM_NAME,
									demandLimitingProgramParticipant
											.getParticipant()
											.getParticipantName(), false,
									demandLimitingProgramParticipant);
						}
					}
				}
			} catch (JMSException e) {
			}
		}
	}

	private boolean isParticipantDemandLimitingEnabled() {
		return (demandLimitingProgramParticipantState != null)
				&& (!demandLimitingProgramParticipantState
						.getDemandLimitingBlockade())
				&& demandLimitingProgramParticipantState.validRules();
	}

	private boolean isValidDataEntryToProcess() {
		return (demandLimitingProgramParticipantState != null)
				&& (dataEntry != null)
				&& (dataEntry.getTime() != null)
//				&& (isDataEntryWithinEventSpan())
				&& ((demandLimitingProgramParticipantState.getMeterTimestamp() == null) || ((demandLimitingProgramParticipantState
						.getMeterTimestamp() != null) && (dataEntry.getTime()
						.compareTo(
								demandLimitingProgramParticipantState
										.getMeterTimestamp()) > 0)));
	}

//	private boolean isDataEntryWithinEventSpan() {
//		Calendar now = new GregorianCalendar();
//
//		Calendar eventSpanBegin = new GregorianCalendar();
//		eventSpanBegin.setTime(dataEntry.getTime());
//
//		Calendar eventSpanEnd = new GregorianCalendar();
//		eventSpanEnd.setTime(dataEntry.getTime());
//		eventSpanEnd.add(Calendar.MINUTE, demandLimitingProgramParticipantState
//				.getEventSpan().intValue());
//
//		return (eventSpanEnd.after(now) && eventSpanBegin.before(now));
//	}
//
	private Event sendEvent() {
		Event event = this.getActiveEvent();
		if (this.getActiveEvent() == null) {
			try {
				event = this.createEvent();
			} catch (DuplicateKeyException e) {
				log.debug("Error while trying to add Demand Limiting Event"
						+ event.getEventName(), e);
			}
		} else {
			try {
				event = this.updateEvent(event);
			} catch (EntityNotFoundException e) {
				log.debug("Error while trying to update Demand Limiting Event"
						+ event.getEventName(), e);
			}
		}
		return event;
	}

	private Event createEvent() throws DuplicateKeyException {
		Event event = this.prepareEvent();
		event = eventEAO.create(event);
//		demandLimitingProgramEJB.reportEventParticipation(event);
		return event;
	}

	private Event updateEvent(Event event) throws EntityNotFoundException {
		event = this.getUpdatedEvent();
		event = eventEAO.update(event);
		return event;
	}

	private Event getUpdatedEvent() {
		Event event = this.getActiveEvent();
		if (demandLimitingProgramParticipantState.getExtendSignal() || 
				demandLimitingProgramParticipantState.getStartSignal()) {
			Calendar eventEndTimeCal = new GregorianCalendar();
			eventEndTimeCal.setTime(demandLimitingProgramParticipantState
					.getMeterTimestamp());
			eventEndTimeCal.add(Calendar.MINUTE,
					demandLimitingProgramParticipantState.getEventSpan()
							.intValue());
			event.setEndTime(eventEndTimeCal.getTime());
		}
		// if (demandLimitingProgramParticipantState.getModeModSignal()) {
		/*
		 * for (EventSignal eventSignal : event.getEventSignals()) { for
		 * (EventSignalEntry eventSignalEntry :
		 * eventSignal.getEventSignalEntries()) { eventSignalEntry.setLevelValue
		 * (demandLimitingProgramParticipantState .getCurrentMode().toString());
		 * eventSignalEntry.setTime(demandLimitingProgramParticipantState
		 * .getMeterTimestamp()); } }
		 */
		for (EventParticipant eventParticipant : event.getEventParticipants()) {
			for (EventParticipantSignal eventParticipantSignal : eventParticipant
					.getSignals()) {
				for (SignalEntry eventParticipantSignalEntry : eventParticipantSignal
						.getSignalEntries()) {
					if ((eventParticipantSignal.getSignalDef() != null)
							&& (eventParticipantSignal.getSignalDef()
									.getSignalName() != null)
							&& (eventParticipantSignal.getSignalDef()
									.getSignalName()
									.equalsIgnoreCase("interval_load"))) {
						eventParticipantSignalEntry
								.setTime(demandLimitingProgramParticipantState
										.getMeterTimestamp());
						eventParticipantSignalEntry
								.setNumberValue(demandLimitingProgramParticipantState
										.getIntervalLoad());
					} else if ((demandLimitingProgramParticipantState
							.getModeModSignal())
							&& (eventParticipantSignal.getSignalDef() != null)
							&& (eventParticipantSignal.getSignalDef()
									.getSignalName() != null)
							&& (eventParticipantSignal.getSignalDef()
									.getSignalName().equalsIgnoreCase("mode"))) {
						eventParticipantSignalEntry
								.setTime(demandLimitingProgramParticipantState
										.getMeterTimestamp());
						eventParticipantSignalEntry
								.setLevelValue(demandLimitingProgramParticipantState
										.getCurrentMode().toString());

					}
				}
			}
			// this.updateEventParticipantRules(eventParticipant);
			eventParticipant.setEventModNumber(eventParticipant
					.getEventModNumber() + 1);
		}
		// }
		return event;
	}

	private void sendNotification(Event dispatch) {
		if (dispatch == null)
			dispatch = this.prepareEvent();
		List<ParticipantContact> participantContacts = demandLimitingProgramEJB
				.getActiveProgramParticipantContacts(dispatch,
						demandLimitingProgramParticipantState
								.getCurrentThreshold());

		if ((participantContacts != null) && (participantContacts.size() > 0)) {
			if (demandLimitingProgramParticipantState.getStartNotification()) {
				demandLimitingProgramEJB.sendParticipantDispatches(dispatch,
						"started", participantContacts,
						demandLimitingProgramParticipantState
								.getCurrentThreshold());
			} else if (demandLimitingProgramParticipantState
					.getEndNotification()) {
				demandLimitingProgramEJB.sendParticipantDispatches(dispatch,
						"normalized", participantContacts,
						demandLimitingProgramParticipantState
								.getCurrentThreshold());
			}
		}

	}

	private List<ProgramParticipant> getActiveProgramParticipants() {
		List<ProgramParticipant> activeProgramParticipants = null;
		if ((demandLimitingProgramParticipant != null)
				&& (!demandLimitingProgramParticipantState
						.getDemandLimitingBlockade())) {
			activeProgramParticipants = new ArrayList<ProgramParticipant>();
			activeProgramParticipants.add(demandLimitingProgramParticipant);
			for (ProgramParticipant clientProgramParticipant : programParticipantManager
					.getProgramParticipantsByParent(
							demandLimitingProgramParticipant.getProgramName(),
							demandLimitingProgramParticipant
									.getParticipantName(), true)) {
				if ((clientProgramParticipant.getProgram() instanceof DemandLimitingProgram)
						&& (clientProgramParticipant.getOptStatus() != 1)
						&& (clientProgramParticipant.getState() != ProgramParticipant.PROGRAM_PART_DELETED)) {
					activeProgramParticipants.add(clientProgramParticipant);

				}
			}
		}

		return activeProgramParticipants;
	}

	private Event getActiveEvent() {
		if ((eventManager != null)
				&& (demandLimitingProgramParticipant != null))
			return eventManager.getEvent(
					demandLimitingProgramParticipant.getProgramName(),
					this.getEventName());
		else
			return null;

	}

	private Event prepareEvent() {
		Event event = this.getActiveEvent();

		if ((event == null) && (this.getActiveProgramParticipants() != null)
				&& (this.getActiveProgramParticipants().size() > 0)) {
			event = new Event();
			event.setProgramName(demandLimitingProgramParticipant
					.getProgramName());
			event.setEventName(getEventName());
			event.setEventStatus(EventStatus.ACTIVE);
			Date eventStartTime = demandLimitingProgramParticipantState
					.getMeterTimestamp();

			if (eventStartTime == null)
				eventStartTime = (new GregorianCalendar()).getTime();
			Calendar eventEndTimeCal = new GregorianCalendar();
			eventEndTimeCal.setTime(eventStartTime);
			eventEndTimeCal.add(Calendar.MINUTE,
					demandLimitingProgramParticipantState.getEventSpan()
							.intValue());
			event.setIssuedTime(eventStartTime);
			event.setReceivedTime(eventStartTime);
			event.setNearTime(eventStartTime);
			event.setStartTime(eventStartTime);
			event.setManual(false);
			event.setEndTime(eventEndTimeCal.getTime());
			/*
			 * if (event.getEventSignals() == null) event.setEventSignals(new
			 * HashSet<EventSignal>()); event.getEventSignals().add(
			 * getEventSignal(event, event.getStartTime()));
			 */for (ProgramParticipant programParticipant : this
					.getActiveProgramParticipants()) {
				if (event.getEventParticipants() == null) {
					event.setEventParticipants(new HashSet<EventParticipant>());
				}
				event.getEventParticipants().add(
						prepareEventParticipant(event, programParticipant,
								event.getStartTime()));
			}

		}
		return event;
	}

	private EventParticipant prepareEventParticipant(Event event,
			ProgramParticipant programParticipant, Date eventStart) {
		EventParticipant eventParticipant = new EventParticipant();
		if (eventParticipant.getSignals() == null) {
			eventParticipant.setSignals(new HashSet<EventParticipantSignal>());
		}
		eventParticipant.setParticipant(programParticipant.getParticipant());
		eventParticipant.getSignals().add(
				getEventParticipantSignal(eventParticipant, eventStart));
		eventParticipant.getSignals().add(
				getEventParticipantIntervalLoad(eventParticipant, eventStart));

		// this.getEventParticipantRules(eventParticipant);
		eventParticipant.setEvent(event);
		return eventParticipant;
	}

	private EventParticipantSignal getEventParticipantSignal(
			EventParticipant eventParticipant, Date eventStartTime) {

		EventParticipantSignal modeSignal = new EventParticipantSignal();
		SignalDef modeSignalDef = signalManager.getSignal("mode");
		modeSignal.setSignalDef(modeSignalDef);
		if (modeSignal.getEventParticipantSignalEntries() == null) {
			modeSignal
					.setEventParticipantSignalEntries(new HashSet<EventParticipantSignalEntry>());
		}

		EventParticipantSignalEntry signalEntry = new EventParticipantSignalEntry();
		signalEntry.setTime(eventStartTime);
		signalEntry.setLevelValue(demandLimitingProgramParticipantState
				.getCurrentMode().toString());
		signalEntry.setEventParticipantSignal(modeSignal);
		modeSignal.getEventParticipantSignalEntries().add(signalEntry);
		modeSignal.setEventParticipant(eventParticipant);
		return modeSignal;

	}

	private EventParticipantSignal getEventParticipantIntervalLoad(
			EventParticipant eventParticipant, Date eventStartTime) {

		EventParticipantSignal intervalLoadSignal = new EventParticipantSignal();
		SignalDef intervalLoadSignalDef = signalManager
				.getSignal("interval_load");
		intervalLoadSignal.setSignalDef(intervalLoadSignalDef);
		if (intervalLoadSignal.getEventParticipantSignalEntries() == null) {
			intervalLoadSignal
					.setEventParticipantSignalEntries(new HashSet<EventParticipantSignalEntry>());
		}

		EventParticipantSignalEntry signalEntry = new EventParticipantSignalEntry();
		signalEntry.setTime(eventStartTime);
		signalEntry.setNumberValue(demandLimitingProgramParticipantState
				.getIntervalLoad());
		signalEntry.setEventParticipantSignal(intervalLoadSignal);
		intervalLoadSignal.getEventParticipantSignalEntries().add(signalEntry);
		intervalLoadSignal.setEventParticipant(eventParticipant);
		return intervalLoadSignal;

	}

	private Collection<EventParticipantRule> getEventParticipantRules(
			EventParticipant eventParticipant) {
		if (eventParticipant != null) {
			if (eventParticipant.getEventRules() == null)
				eventParticipant
						.setEventRules(new HashSet<EventParticipantRule>());
			for (ProgramParticipantRule programParticipantRule : demandLimitingProgramParticipantState
					.getProgramParticipantRules()) {
				EventParticipantRule eventParticipantRule = new EventParticipantRule();
				eventParticipantRule.setEventParticipant(eventParticipant);
				eventParticipantRule.setMode(programParticipantRule.getMode());
				eventParticipantRule.setThreshold(programParticipantRule
						.getThreshold());
				eventParticipantRule.setVariable(programParticipantRule
						.getVariable());
				eventParticipantRule.setOperator(programParticipantRule
						.getOperator());
				eventParticipantRule
						.setStart(programParticipantRule.getStart());
				eventParticipantRule.setEnd(programParticipantRule.getEnd());
				eventParticipantRule
						.setValue(programParticipantRule.getValue());
				eventParticipantRule.setSource(programParticipantRule
						.getSource());
				eventParticipantRule.setSortOrder(programParticipantRule
						.getSortOrder());
				eventParticipantRule.setSignalAction(programParticipantRule
						.getSignalAction());
				eventParticipantRule.setNotifyAction(programParticipantRule
						.getNotifyAction());
				eventParticipant.getEventRules().add(eventParticipantRule);
			}

		}
		return eventParticipant.getEventRules();
	}

	private Collection<EventParticipantRule> updateEventParticipantRules(
			EventParticipant eventParticipant) {
		if ((eventParticipant != null)
				&& (eventParticipant.getEventRules() != null)) {
			for (ProgramParticipantRule programParticipantRule : demandLimitingProgramParticipantState
					.getProgramParticipantRules()) {
				for (EventParticipantRule eventParticipantRule : eventParticipant
						.getEventRules()) {
					if (eventParticipantRule.getSortOrder().equals(
							programParticipantRule.getSortOrder())) {
						eventParticipantRule.setValue(programParticipantRule
								.getValue());
					}
				}
			}

		}
		return eventParticipant.getEventRules();

	}

	private EventSignal getEventSignal(Event event, Date eventStartTime) {

		EventSignal eventSignal = new EventSignal();
		SignalDef modeSignalDef = signalManager.getSignal("mode");
		eventSignal.setSignalDef(modeSignalDef);
		if (eventSignal.getEventSignalEntries() == null) {
			eventSignal.setEventSignalEntries(new HashSet<EventSignalEntry>());
		}

		EventSignalEntry signalEntry = new EventSignalEntry();
		signalEntry.setTime(eventStartTime);
		signalEntry.setLevelValue(demandLimitingProgramParticipantState
				.getCurrentMode().toString());
		signalEntry.setEventSignal(eventSignal);
		eventSignal.getEventSignalEntries().add(signalEntry);
		eventSignal.setEvent(event);
		return eventSignal;

	}

	private String getEventName() {
		if (demandLimitingProgramParticipant != null)
			return PROGRAM_ACRONYM + "_" 
			// + demandLimitingProgramParticipant.getUUID();
					+ demandLimitingProgramParticipant.getParticipantName();
		else
			return null;
	}

}
