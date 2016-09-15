/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.program.fastdr.FastDRProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.fastdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.core.VaroliNotification;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEmailFormatter;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.cpp.CPPProgram;
import com.akuacom.pss2.program.cpp.CPPUtils;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;

/**
 * The Class FastDRProgramEJBBean.
 */
@Stateless
public class FastDRProgramEJBBean extends ProgramEJBBean implements FastDRProgramEJB.R,
        FastDRProgramEJB.L {

    private static final Logger log = Logger.getLogger(FastDRProgramEJBBean.class);

    @EJB
    private SystemManager.L systemManager;

    @EJB
    protected Notifier.L notifier;
    
    @EJB 
    
    private VaroliNotification.L varoliNotification;

    /*
     * (non-Javadoc)
     *
     * @see com.akuacom.pss2.core.ProgramEJBBean#createProgramObject()
     */
    public Program createProgramObject() {
        return new CPPProgram();
    }

    @Override
    public Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) {
        Set<EventSignal> eventSignals = new HashSet<EventSignal>();

        Signal cppPriceSignal = null;
        for (Signal signal : program.getSignals()) {
            if (signal.getSignalDef().getSignalName().equals("cpp_price")) {
                cppPriceSignal = signal;
            }
        }

        Double value = systemManager.getPss2Properties()
                .getRuleCPPPriceDefaultPrice();

        if (cppPriceSignal == null || value == null || program == null) {
            return eventSignals;
        }

        EventSignal priceProgramSignal = new EventSignal();
        priceProgramSignal.setSignalDef(cppPriceSignal.getSignalDef());
        Set<EventSignalEntry> priceEntries = new HashSet<EventSignalEntry>();

        EventSignalEntry priceEntry = new EventSignalEntry();
        priceEntry.setTime(event.getStartTime());
        priceEntry.setNumberValue(value);
        priceEntries.add(priceEntry);

        priceProgramSignal.setSignalEntries(priceEntries);
        eventSignals.add(priceProgramSignal);

        return eventSignals;
    }

    @Override
    public List<ProgramParticipantRule> createDefaultClientRules(Program program) {
        List<ProgramParticipantRule> rules = new ArrayList<ProgramParticipantRule>();

        List<ProgramRule> progRules = this.getProgramRules(program);
        for (ProgramRule progRule : progRules) {
            CPPUtils.createDefaultClientRules(program, progRule, rules);
        }

        return rules;
    }

    @Override
    protected Set<EventParticipant> createEventParticipants(Event event, Program program) {
        Set<EventParticipant> eventParticipants = event.getEventParticipants();

        Set<EventParticipant> resultParticipants = new HashSet<EventParticipant>();

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
                    findMatchingProgramParticipant(eventParticipant, program);
            if (matchingProgramParticipant == null) {
                extraParticipants
                        .add(eventParticipant.getParticipant());
            } else {
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
            log.warn(LogUtils.createLogEntry(program.getProgramName(),
                    LogUtils.CATAGORY_EVENT, message,
                    longDesc.toString()));
        }
        // now log the extra orphan participants and throw an exception
        if (!extraParticipants.isEmpty()) {
            String message =
                    "event specifies participants that aren't in program";
            throw new ValidationException(message);
        }

        return resultParticipants;
    }

    @Override
    protected List<ProgramRule> getProgramRules(Program program) {
        // Get Custom rules
        ArrayList<ProgramRule> progRules = new ArrayList<ProgramRule>();

        Set<ProgramRule> progRuleset = program.getRules();
        if(progRuleset != null && progRuleset.size() > 0 &&
        	systemManager.getPss2Features().isProgramRuleEnabled())
        {
            for (ProgramRule programRule : progRuleset) {
                progRules.add(programRule);
            }

            return progRules;
        }

        ProgramRule pr = new ProgramRule();
        String mode = systemManager.getPss2Properties().getRuleCPPDefaultMode();
        pr.setMode(Mode.valueOf(mode));
        pr.setOperator(Operator.ALWAYS);
        pr.setValue(0.0);
        pr.setStart(new Date());
        // don't care about these in CCPUtils
        pr.setSource("Program");
        pr.setVariable(null);
        pr.setSortOrder(0);
        pr.setProgram(program);

        progRules.add(pr);

        return progRules;
    }

    // DRMS-7358: make fast-dr program instantly available after issued for now.
    @Override
    public Collection<String> createEvent(String programName, Event event, UtilityDREvent utilityDREvent) {
        Collection<String> results = super.createEvent(programName, event, utilityDREvent);
        processEvent(event, System.currentTimeMillis());
        return results;
    }

    // DRMS-7358: make fast-dr program instantly available after issued for now.
    @Override
    protected void processEvent(Event event, long ms) {
        String eventName = event.getEventName();
        if (event.getEndTime().getTime() < ms) {
            event = eventManager.getEventWithParticipantsAndSignals(eventName);

            // If this program sends messages for event start, send them now
            if (programSendsCompletedNotifications(event.getProgramName())) {
                sendDRASOperatorNotifications(event, "completed");
                sendProgramOperatorNotifications(event, "completed");
                sendParticipantNotifications(event, "completed");
            }

            reportEventHistory(event, false);
            deleteEvent(event);
        } else if (event.getStartTime().getTime() <= ms && EventStatus.ACTIVE != event.getEventStatus()) {
            try {
                event = eventManager.getEventWithParticipantsAndSignals(eventName);
                event.setEventStatus(EventStatus.ACTIVE);
                eventEAO.update(event);
                // If this program sends messages for event completion, send them now
                if (programSendsStartedNotifications(event.getProgramName())) {
                    sendDRASOperatorNotifications(event, "started");
                    sendParticipantNotifications(event, "started");
                }
            } catch (EntityNotFoundException e) {
                String message = "error getting event " + eventName
                        + " for program " + event.getProgramName();
                throw new EJBException(message, e);
            }
        }
    }

    @Override
    protected void sendParticipantNotifications(Event event, String verb) {
    	boolean isVaroliiNotification=systemManager.getPss2Features().isFeatureVaroliiNotificationEnabled();
    	if (!isVaroliiNotification) {
    		sendParticipantNotifications(event, verb, true, false);
    		return;
    	}    	
    	varoliNotification.sendVaroliParticipantNotifications(event, verb);    	
     /*  String theme;
        Date endTime = event.getEndTime();
        if ("started".equals(verb)) {
            theme = "HECO:StartEvent;HECO:;VOICETALENT:M_ENG_4;";
        } else if ("completed".equals(verb)) {
            theme = "HECO:EndEvent;HECO:;VOICETALENT:M_ENG_4;";
        } else if ("canceled".equals(verb)) {
            long now = System.currentTimeMillis();
            // don't send out event end notification since the event has not started yet.
            if (now < event.getStartTime().getTime()) {
                return;
            } else {
                endTime = new Date(now);
            }
            theme = "HECO:EndEvent;HECO:;VOICETALENT:M_ENG_4;";
        } else {
            // do nthing.
            return;
        }

        NotificationMethod notificationMethod = NotificationMethod
                .getInstance();
        notificationMethod.setMedia(NotificationMethod.ENVOY_MESSAGE);
        notificationMethod.setEmail(true);
        notificationMethod.setVoice(true);
        notificationMethod.setSms(true);

        NotificationParametersVO np = new NotificationParametersVO();
        np.setTheme(theme);
        np.setEmail("energyscout@heco.com");
        np.setProgramName("Fast DR");
        np.setEventStartDate(event.getStartTime());
        np.setEventEndDate(endTime);

        String subject = np.getEventId() + " and " + np.getEventStartDate();
        // TODO: combine event participants into one xml request
        for (EventParticipant eventParticipant : event.getParticipants()) {
            //don't send message to the participant has already Opt-out of the event.
            if(eventParticipant.getEventOptOut()!=0) continue;

            Participant p = eventParticipant.getParticipant();
            Participant participant = participantEAO.findByName(p.getParticipantName(), p.isClient());

            ArrayList<Contact> contacts = new ArrayList<Contact>();
            Set<ParticipantContact> participantContacts = participant.getContacts();
            for (ParticipantContact pc : participantContacts) {
                if (pc == null || !wantsParticipantEventNotification(eventParticipant, pc))
                    continue;
                contacts.add(pc.getParticipantContactAsContact());
            }
            if (contacts.size() == 0) {
                continue;
            }

            try {
                notifier.sendVaroliiNotification(contacts, participant.getParticipantName(), subject, notificationMethod, np, false);
            } catch (JMSException e) {
                log.error(e.getMessage(), e);
            }
        }*/

    }
    
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

            String subject = salutationString + pss2Props.getUtilityDisplayName()
                    + clientString + stateString + event.getProgramName() + eventLiteral + actionString;

            final Set<EventParticipantSignal> participantSignals = eventParticipant
                    .getSignals();
            Set<EventSignal> eventSignals = eventParticipant.getEvent()
                    .getEventSignals();

            List<Signal> combinedSignals = new ArrayList<Signal>();
            combinedSignals.addAll(eventSignals);
            combinedSignals.addAll(participantSignals);

            String emailContentType = pss2Props.getEmailContentType();
            EventEmailFormatter mailFactory = new EventEmailFormatter();
            String serverHost = pss2Props
                    .getStringValue(PSS2Properties.PropertyName.SERVER_HOST);

            Participant p = participantEAO.findParticipantWithContacts(participant.getParticipantName(), participant.isClient());
            for (ParticipantContact pc : p.getContacts()) {
                if (pc == null
                        || !wantsParticipantEventNotification(eventParticipant,
                                pc, isRevision)) {
                    continue;
                }
                String emailContent = mailFactory.generateEmailContent(event,
                        combinedSignals, serverHost, emailContentType,
                        isRevision, null,null);
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
}
