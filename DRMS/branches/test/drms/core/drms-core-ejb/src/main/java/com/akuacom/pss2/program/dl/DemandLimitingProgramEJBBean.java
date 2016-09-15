package com.akuacom.pss2.program.dl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timer;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventEmailFormatter;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.rule.Rule.Threshold;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.LogUtils;

@Stateless
public class DemandLimitingProgramEJBBean extends ProgramEJBBean implements
		DemandLimitingProgramEJB.R, DemandLimitingProgramEJB.L {
	private static final Logger log = Logger
			.getLogger(DemandLimitingProgramEJBBean.class);

	@EJB
	private ProgramManager.L programManager;

	@EJB
	private SignalManager.L signalManager;

	@EJB
	protected EventEAO.L eventEAO;

	@Override
	public Program createProgramObject() {
		return new DemandLimitingProgram();
	}

	@Override
	public void initialize(String programName) {

	}

	@Override
	protected boolean programSendsStartedNotifications(String programName) {
		return false; // Demand Limiting will not send "started" notifications
	}

	@Override
	protected boolean programSendsCompletedNotifications(String programName) {
		return false; // Demand Limiting will not send "completed" notifications
	}

	@Override
	public void processTimeout(Timer timer) {
	}

	@Override
	public Set<EventSignal> initializeEvent(Program program, Event event,
			UtilityDREvent utilityDREvent) throws ProgramValidationException {
		// Need to initialize Demand Limiting Event
		Set<EventSignal> emptySet = new HashSet<EventSignal>();
		return emptySet;
	}

	@Override
	public Set<ProgramSignal> getProgramInputEventSignals(Program program) {
		// Template for demand limiting program signal
		return new HashSet<ProgramSignal>();
	}

	@Override
	protected void sendDRASOperatorNotifications(Event event, String subject) {

	}

	@Override
	protected void sendProgramOperatorNotifications(Program program,
			String subject, String content) {
	}

	@Override
	protected void sendProgramOperatorNotifications(Event event, String subject) {

	}

	@Override
	protected void sendParticipantNotifications(Event event, String verb) {

	}

	@Override
	protected void sendParticipantIssuedNotifications(Event event, String verb) {
	}

	@Override
	protected void sendParticipantNotifications(Event event, String verb,
			boolean showClientStatus, boolean isRevision) {
	}

	public void sendParticipantDispatches(Event event, String verb,
			List<ParticipantContact> participantContacts, Threshold threshold) {
		PSS2Properties pss2Props = systemManager.getPss2Properties();
		boolean isRevision = false;
		for (EventParticipant eventParticipant : event.getParticipants()) {
			Participant participant = eventParticipant.getParticipant();

			if ((participant != null) && (participant.isClient())) {

				String stateString = "";
				String clientString = " Auto DR Client "
						+ participant.getParticipantName() + " ";
				String salutationString = "Your ";
				String eventLiteral = " ";

				if (participant.getType() != Participant.TYPE_MANUAL) {
					if (participant.getStatus().intValue() == ClientStatus.OFFLINE
							.ordinal()) {
						stateString = " is offline and ";
						if (participant.isManualControl()) {
							// stateString =
							// " is offline and in manual mode and ";
							salutationString = "";
							clientString = " ";
							eventLiteral = " event ";
							stateString = "";
						}
						// else {
						// stateString = " is offline and ";
						// }
					} else if (participant.isManualControl()) {
						// stateString = " is in manual mode and ";
						salutationString = "";
						clientString = " ";
						eventLiteral = " event ";
					} else if (participant.getStatus().intValue() == ClientStatus.ONLINE
							.ordinal()) {
						stateString = " is online and ";
					}
				} else if (participant.getType() == Participant.TYPE_MANUAL) {
					salutationString = "";
					clientString = " ";
					eventLiteral = " event ";
				}

				String actionString = verb;
				if (verb.equalsIgnoreCase("started")) {
					actionString = " has started " + "at "
							+ threshold.toString() + " threshold";
				} else if (verb.equalsIgnoreCase("deleted")) {
					actionString = " has been cancelled";
				} else if (verb.equalsIgnoreCase("cancelled")) {
					actionString = " has been cancelled";
				} else if (verb.equalsIgnoreCase("completed")) {
					actionString = " has completed";
				} else if (verb.equalsIgnoreCase("ended")) {
					actionString = " has ended";
				} else if (verb.equalsIgnoreCase("normalized")) {
					actionString = " has entered normal mode";
				} else if (verb.equalsIgnoreCase("updated")) {
					actionString = " has been updated";
					isRevision = true;
				}

				String subject = salutationString
						+ pss2Props.getUtilityDisplayName() + clientString
						+ stateString + "demand limiting" + eventLiteral
						+ actionString;

				final Set<EventParticipantSignal> participantSignals = eventParticipant
						.getSignals();

				List<Signal> combinedSignals = new ArrayList<Signal>();
				combinedSignals.addAll(participantSignals);

				String emailContentType = pss2Props.getEmailContentType();
				EventEmailFormatter mailFactory = new EventEmailFormatter();
				String serverHost = pss2Props
						.getStringValue(PSS2Properties.PropertyName.SERVER_HOST);
				if (participantContacts != null) {
					for (ParticipantContact pc : participantContacts) {
						if (wantsParticipantEventNotification(eventParticipant,
								pc, isRevision)) {
							String emailContent = mailFactory
									.generateEmailContent(event,
											combinedSignals, serverHost,
											emailContentType, isRevision,
											event.getNearTime(),null);
							notifier.sendNotification(
									pc.getParticipantContactAsContact(),
									participant.getParticipantName(), subject,
									emailContent, emailContentType,
									NotificationMethod.getInstance(),
									new NotificationParametersVO(),
									Environment.isAkuacomEmailOnly(), true,
									false, event.getProgramName());
						}
					}
				}
			}
		}

	}

	@Override
	public List<ProgramParticipantRule> createDefaultClientRules(Program program) {
		List<ProgramParticipantRule> demandLimitingProgramParticipantRules = new ArrayList<ProgramParticipantRule>();

		for (ProgramRule demandLimitingProgramRule : program.getRules()) {
			ProgramParticipantRule demandLimitingProgramParticipantRule = new ProgramParticipantRule();

			demandLimitingProgramParticipantRule
					.setSortOrder(demandLimitingProgramRule.getSortOrder());
			demandLimitingProgramParticipantRule
					.setMode(demandLimitingProgramRule.getMode());
			demandLimitingProgramParticipantRule
					.setThreshold(demandLimitingProgramRule.getThreshold());
			demandLimitingProgramParticipantRule
					.setStart(demandLimitingProgramRule.getStart());
			demandLimitingProgramParticipantRule
					.setEnd(demandLimitingProgramRule.getEnd());
			demandLimitingProgramParticipantRule
					.setVariable(demandLimitingProgramRule.getVariable());
			demandLimitingProgramParticipantRule
					.setOperator(demandLimitingProgramRule.getOperator());
			demandLimitingProgramParticipantRule
					.setValue(demandLimitingProgramRule.getValue());
			demandLimitingProgramParticipantRule
					.setSource(demandLimitingProgramRule.getSource());
			demandLimitingProgramParticipantRule
					.setSignalAction(demandLimitingProgramRule
							.getSignalAction());
			demandLimitingProgramParticipantRule
					.setNotifyAction(demandLimitingProgramRule
							.getNotifyAction());

			demandLimitingProgramParticipantRules
					.add(demandLimitingProgramParticipantRule);

		}

		return demandLimitingProgramParticipantRules;
	}

	@Override
	protected List<ProgramRule> getProgramRules(Program program) {
		ArrayList<ProgramRule> demandLimitingProgramRules = new ArrayList<ProgramRule>();
		demandLimitingProgramRules.addAll(program.getRules());
		return demandLimitingProgramRules;
	}

	public Event createEvent(Event event) {
		return super.persistEvent(DemandLimitingProgram.PROGRAM_NAME, event);
	}

	public void updateEvent(Event event) {

		super.mergeEvent(event);
	}

	@Override
	public void updateEvent(String programName, String eventName, Event event,
			UtilityDREvent utilityDREvent) {
		try {
			eventEAO.update(event);
		} catch (EntityNotFoundException e) {
			log.error(LogUtils.createLogEntry(event.getProgramName(),
					LogUtils.CATAGORY_EVENT, "Error updating event signal",
					null));
			throw new EJBException(e);
		} catch (Exception ex) {
			log.error(LogUtils.createLogEntry(event.getProgramName(),
					LogUtils.CATAGORY_EVENT, "Error updating event signal",
					null));
		}
	}

//	@Override
//	protected void reportParticipation(Event event) throws AppServiceException {
//		super.reportParticipation(event);
//	}
//
//	public void reportEventParticipation(Event event) {
//		try {
//			this.reportParticipation(event);
//		} catch (AppServiceException e) {
//			log.debug("Error while transfering event information to reports "
//					+ event.getEventName(), e);
//		}
//	}

	@Override
	public void deleteEvent(Event event) {
/*		if ((event != null)
				&& (event.getProgramName()
						.equals(DemandLimitingProgram.PROGRAM_NAME))) {
			for (EventParticipant eventParticipant : event
					.getEventParticipants()) {
				if (eventParticipant.getParticipant() != null) {
					for (ProgramParticipant programParticipant : eventParticipant
							.getParticipant().getProgramParticipants()) {
						if (programParticipant.getProgram() instanceof DemandLimitingProgram) {
							DemandLimitingProgramParticipantState demandLimitingProgramParticipantState = new DemandLimitingProgramParticipantState(
									programParticipant);
							if (demandLimitingProgramParticipantState != null) {
								demandLimitingProgramParticipantState
										.setCurrentMode(Mode.NORMAL);
								demandLimitingProgramParticipantState
										.setCurrentThreshold(Threshold.normal);
								ProgramParticipant updatedProgramParticipant = demandLimitingProgramParticipantState
										.getProgramParticipant();
								programParticipantManager
										.updateProgramParticipant(
												DemandLimitingProgram.PROGRAM_NAME,
												eventParticipant
														.getParticipant()
														.getParticipantName(),
												eventParticipant
														.getParticipant()
														.isClient(),
												updatedProgramParticipant);
							}
						}
					}
				}
			}
		}
*/
		if (event != null) {
			List<ParticipantContact> participantContacts = this
					.getActiveProgramParticipantContacts(event,
							Threshold.normal);

			if ((participantContacts != null)
					&& (participantContacts.size() > 0)) {
				this.sendParticipantDispatches(event, "ended",
						participantContacts, Threshold.normal);
			}
		}
		super.deleteEvent(event);
	}

	public List<ParticipantContact> getActiveProgramParticipantContacts(
			Event event, Threshold threshold) {
		List<ParticipantContact> activeProgramParticipantContacts = null;
		if (event != null) {
			activeProgramParticipantContacts = new ArrayList<ParticipantContact>();
			if ((event.getParticipants() != null)
					&& (event.getParticipants().size() > 0)) {
				for (EventParticipant eventParticipant : event
						.getParticipants()) {
					Participant participant = eventParticipant.getParticipant();
					if ((participant != null) && (participant.isClient())
							&& (participant.getContacts() != null)
							&& (participant.getContacts().size() > 0)) {
						for (ParticipantContact contact : participant
								.getContacts()) {
							if (contact.getDemandLimitingNotifications()) {
								switch (threshold) {
								case exceeded:
									if (contact.getDemandHighNotifications())
										activeProgramParticipantContacts
												.add(contact);
									break;
								case high:
									if (contact.getDemandHighNotifications())
										activeProgramParticipantContacts
												.add(contact);
									break;
								case moderate:
									if (contact
											.getDemandModerateNotifications())
										activeProgramParticipantContacts
												.add(contact);
									break;
								case warning:
									if (contact.getDemandWarningNotifications())
										activeProgramParticipantContacts
												.add(contact);
									break;
								case normal:
									if (contact.getDemandNormalNotifications())
										activeProgramParticipantContacts
												.add(contact);
									break;
								case NA:
									break;
								default:
									break;
								}
							}
						}
					}

				}
			}
		}
		return activeProgramParticipantContacts;
	}

}
