package com.akuacom.pss2.program.fastdr;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.*;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;
import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import java.util.*;

@Stateless
public class FastDRNProgramEJBBean extends ProgramEJBBean
        implements FastDRNProgramEJB.R, FastDRNProgramEJB.L {

    private static final Logger log = Logger.getLogger(FastDRNProgramEJBBean.class);

    /**
     * This event doesn't generate signals, so it doesn't call signal related functions.
     * And it should not affect Event State cache. It also ignores any aggregation related
     * stuff.
     */
    @Override
    public Collection<String> createEvent(String programName, Event event, UtilityDREvent notUsed) {
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
            Program program = programManager.getProgramWithParticipantsAndPRules(programName);

            event.setEventStatus(EventStatus.RECEIVED);

            // check event participants and add event clients
            Set<EventParticipant> eventParticipants = createEventParticipants(event, program);

            Set<EventParticipant> filteredEventParticipants =
                programManager.filterEventParticipants(eventParticipants, program, event, true);

            Set<EventParticipant> eventClients =
                createEventClients(filteredEventParticipants, program);
            filteredEventParticipants.addAll(eventClients);

            for (EventParticipant eventParticipant : filteredEventParticipants) {
                eventParticipant.setEvent(event);
            }
            event.setEventParticipants(filteredEventParticipants);

            // do validation
            ProgramValidator programValidator = ValidatorFactory.getProgramValidator(program);
            programValidator.validateEvent(event);

            event = persistEvent(program.getProgramName(), event);

            sendDRASOperatorNotifications(event, "created");
            sendProgramOperatorNotifications(event, "created");
            names.add(event.getEventName());

        } catch (ProgramValidationException e) {
            logEntry.setDescription("event creation failed: "
                    + event.getEventName());
            logEntry.setLongDescr(event.toString());
            throw new EJBException(e);
        }
        return names;
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
    protected void processEvent(Event event, long ms) {
        String eventName = event.getEventName();
        if (event.getStartTime().getTime() < ms) {
            event = eventManager.getByEventNameWithParticipants(eventName);

            // If this program sends messages for event start, send them now
            if (programSendsCompletedNotifications(event.getProgramName())) {
                sendDRASOperatorNotifications(event, "completed");
                sendProgramOperatorNotifications(event, "completed");
                sendParticipantNotifications(event, "started");
            }

            reportEventHistory(event, true);
            deleteEvent(event);
        }
    }

    @Override
    protected void sendParticipantNotifications(Event event, String verb, boolean showClientStatus, boolean isRevision) {
        NotificationMethod notificationMethod = NotificationMethod
                .getInstance();
        notificationMethod.setMedia(NotificationMethod.ENVOY_MESSAGE);
        notificationMethod.setEmail(true);
        notificationMethod.setVoice(true);
        notificationMethod.setSms(true);

        NotificationParametersVO np = new NotificationParametersVO();
        np.setTheme("HECO:StartNotification;HECO:;VOICETALENT:M_ENG_4;");
        np.setEmail("energyscout@heco.com");
        np.setProgramName("Fast DR");
        np.setEventStartDate(event.getStartTime());

        String subject = np.getEventId() + " and " + np.getEventStartDate();
        for (EventParticipant eventParticipant : event.getParticipants()) {
            Participant participant = eventParticipant.getParticipant();

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
                notifier.sendVaroliiNotification2(contacts, participant.getParticipantName(), subject, notificationMethod, np, false);
            } catch (JMSException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
