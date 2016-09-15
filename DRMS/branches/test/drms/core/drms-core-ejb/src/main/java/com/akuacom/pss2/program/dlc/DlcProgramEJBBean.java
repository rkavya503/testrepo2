package com.akuacom.pss2.program.dlc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.dlc.signal.SignalType;
import com.akuacom.pss2.program.dlc.signal.SignalsType;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class DlcProgramEJBBean extends ProgramEJBBean implements DlcProgramEJB.L, DlcProgramEJB.R {
    private static final Logger log = Logger.getLogger(DlcProgramEJBBean.class);

    public static final String PROGRAM_NAME = "Viridity DLC";

    /**
     * This method will only create event, participant and it's client.
     * Also the default pending signals for the client.
     * The level signal will be added separately.
     *
     * @param programName the program name
     * @param event the event
     * @param utilityDREvent obsoleted param
     * @return what's this value for?
     */
    @Override
    public Collection<String> createEvent(String programName, Event event, UtilityDREvent utilityDREvent) {

        // general validation
        FireLogEntry logEntry = new FireLogEntry();
        logEntry.setUserParam1(programName);
        logEntry.setCategory(LogUtils.CATAGORY_EVENT);
        if (!programName.equals(event.getProgramName())) {
            String message = "program name " + programName + " doesn't match "
                    + "program name in event";
            logEntry.setDescription(message);
            logEntry.setLongDescr(event.toString());
            throw new ValidationException(message);
        }

        // validate participants, there should be exactly 1 participant
        Set<EventParticipant> eventParticipantList = event.getEventParticipants();
        if (eventParticipantList == null || eventParticipantList.size() != 1) {
            String message = "invalid participant number of dlc event " +
                    (eventParticipantList == null ? "null" : eventParticipantList.size());
            logEntry.setDescription(message);
            logEntry.setLongDescr(event.toString());
            throw new ValidationException(message);
        }

        try{
            Program program = programManager.getProgramWithParticipants(programName);
            // validate participant in program
            Set<EventParticipant> eventParticipants = createEventParticipants(event, program);
            // add client
            Set<EventParticipant> eventClients = createEventClients(eventParticipants, program);
            eventParticipants.addAll(eventClients);
            // clean up event status cache
            EventStateCacheHelper esch = EventStateCacheHelper.getInstance();
            for (EventParticipant eventParticipant : eventParticipants) {
                eventParticipant.setEvent(event);
                String participantName = eventParticipant.getParticipant().getParticipantName();
                esch.delete(participantName);
            }
            // clear event participant list and add back the participant and the client
            eventParticipantList.clear();
            eventParticipantList.addAll(eventParticipants);

            ProgramValidator programValidator = ValidatorFactory.getProgramValidator(program);
            programValidator.validateEvent(event);

            // add default signals
            processRulesAndSignals(program, event, utilityDREvent, new Date());

            Set<EventParticipant> eventParticipantSet = event.getEventParticipants();
            for (EventParticipant eventParticipant : eventParticipantSet) {
                if (eventParticipant.getParticipant().isClient()) {
                    Set<EventParticipantSignal> signals = eventParticipant.getSignals();
                    for (EventParticipantSignal signal : signals) {
                        Set<EventParticipantSignalEntry> entries = signal.getEventParticipantSignalEntries();
                        for (EventParticipantSignalEntry entry : entries) {
                            entry.setEventParticipantSignal(signal);
                        }
                    }
                }
            }

            // save to db
            event = persistEvent(program.getProgramName(), event);

            // send out notifications
            sendDRASOperatorNotifications(event, "created");
            sendProgramOperatorNotifications(event, "created");

        } catch (ProgramValidationException e) {
            logEntry.setDescription("event creation failed: " + event.getEventName());
            logEntry.setLongDescr(event.toString());
            throw new EJBException(e);
        }

        // return event name list
        ArrayList<String> list = new ArrayList<String>();
        list.add(event.getEventName());
        return list;
    }

    @Override
    protected Set<EventParticipant> createEventParticipants(Event event, Program programWithParticipant) {
        EventParticipant eventParticipant = event.getParticipants().get(0);

        Set<ProgramParticipant> programParticipants = programWithParticipant.getProgramParticipants();
        String participantName = eventParticipant.getParticipant().getParticipantName();
        boolean exist = false;
        for (ProgramParticipant programParticipant : programParticipants) {
            if (programParticipant.getParticipant().getParticipantName().equals(participantName)) {
                exist = true;
                break;
            }
        }

        if (!exist) {
            String message = "event specifies participants that aren't in program";
            throw new ValidationException(message);
        }

        HashSet<EventParticipant> set = new HashSet<EventParticipant>();
        set.add(eventParticipant);
        return set;
    }

    @Override
    public void updateSignals(SignalsType signals) {
        List<SignalType> signalList = signals.getSignal();

        Map<String, String> valid=new HashMap<String, String>();
        Map<String, String> invalid=new HashMap<String, String>();
        Map<String, ProgramValidationMessage> messages = new HashMap<String, ProgramValidationMessage>();
        for (SignalType signal : signalList) {
            String serviceId = signal.getServiceId();
            String level = signal.getLevel();
            try {
                ProgramValidationMessage message = updateSignal(signal);
                if (message != null) {
                    invalid.put(serviceId, level);
                    messages.put(serviceId, message);
                } else {
                    valid.put(serviceId, level);
                }
            }catch(Exception e) {
                String msg = "failed to update signal " + level + " for account " + serviceId;
                log.error(LogUtils.createLogEntry(PROGRAM_NAME, LogUtils.CATAGORY_EVENT, msg, null));
                log.debug(LogUtils.createExceptionLogEntry(PROGRAM_NAME, LogUtils.CATAGORY_EVENT, e));
                invalid.put(serviceId, level);
            }
        }

        sendUpdateSignalNotification(valid, invalid, messages);
    }

    /**
     * This method tries to update signal according the input. It won't throw
     * any exceptions.
     *
     * Note: The TransactionAttribute annotation is REQUIRES_NEW which makes
     * each call independent from a batch process. So that the RuntimeException
     * thrown from lower layer will not rollback the whole batch transaction.
     *
     * @param signal The input value object
     * @return action summary, either success or failure
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ProgramValidationMessage updateSignal(SignalType signal) {
        String serviceId = signal.getServiceId();
        String level = signal.getLevel();

        ProgramValidationMessage result = validateLevel(level);
        if (result != null) {
            return result;
        }

        try {
            // do participant/client lookup
            boolean ok = participantEAO.checkAccount(serviceId);
            if (!ok) {
                result = new ProgramValidationMessage();
                result.setParameterName(serviceId);
                result.setDescription("couldn't locate client with service id: " + serviceId);
                return result;
            }
            Participant p = participantEAO.getParticipantByAccount(serviceId);
            String participantName = p.getParticipantName();
            List<Participant> clients = clientEAO.getClientsByParticipant(participantName);
            if (clients == null || clients.size() == 0) {
                result = new ProgramValidationMessage();
                result.setParameterName(serviceId);
                result.setDescription("client info incomplete. please update customer with service id: "
                        + serviceId);
                return result;
            }
            Participant client = clients.get(0);
            String clientName = client.getParticipantName();

            EventParticipant ep = getEventClient(clientName);
            // if normal signal is published, cancel event if exist, otherwise, no action.
            if ("normal".equals(level)) {
                if (ep != null) {
                    Event event = ep.getEvent();
                    cancelEvent(event.getProgramName(), event.getEventName());
                }
            } else {
                long now = System.currentTimeMillis();
                String eventName = serviceId + "-" + EventUtil.getEventName(now);

                if (ep == null) {
                    // create event
                    Event event = new Event();
                    event.setProgramName(PROGRAM_NAME);
                    event.setEventName(eventName);
                    Date date = new Date(now);
                    event.setStartTime(date);
                    event.setIssuedTime(date);
                    event.setEndTime(DateUtil.getEndOfDay(date));
                    event.setReceivedTime(date);
                    event.setEventStatus(EventStatus.ACTIVE);
                    event.setEventSignals(new HashSet<EventSignal>());

                    EventParticipant eventParticipant = new EventParticipant();
                    eventParticipant.setParticipant(p);
                    eventParticipant.setEvent(event);
                    Set<EventParticipant> eventParticipants = event.getEventParticipants();
                    eventParticipants.add(eventParticipant);

                    createEvent(event.getProgramName(), event, null);
                } else {
                    eventName = ep.getEvent().getEventName();
                }

                updateSignal(level, clientName, eventName);
            }

            return null;

        } catch (EJBException e) {
            result = new ProgramValidationMessage();
            result.setParameterName(serviceId);
            result.setDescription("internal error. please contact DRAS admin");
            return result;
        }
    }

    private ProgramValidationMessage validateLevel(String level) {
        if (!"normal".equals(level) && !"moderate".equals(level) && !"high".equals(level)) {
            ProgramValidationMessage message = new ProgramValidationMessage();
            message.setDescription("illegal level value: " + level);
            message.setParameterName("level");
            return message;
        }
        return null;
    }

    private void updateSignal(String signalLevel, String clientName, String eventName) {
        Event event = getEvent(PROGRAM_NAME, eventName);
        Set<EventParticipant> eventParticipants = event.getEventParticipants();
        for (EventParticipant ep : eventParticipants) {
            if (clientName.equals(ep.getParticipant().getParticipantName())) {
                updateEventParticipantSignal(ep, signalLevel);
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void updateEventParticipantSignal(EventParticipant ep, String signalLevel) {
        Set<EventParticipantSignal> signals = ep.getSignals();
        for (EventParticipantSignal signal : signals) {
            if ("mode".equals(signal.getSignalDef().getSignalName())) {
                Set<? extends SignalEntry> signalEntries = signal.getSignalEntries();
                EventParticipantSignalEntry result = findCurrentSignalEntry(signalEntries);

                if (result == null || !signalLevel.equals(result.getStringValue())) {
                    EventParticipantSignalEntry entry = new EventParticipantSignalEntry();
                    entry.setEventParticipantSignal(signal);
                    entry.setLevelValue(signalLevel);
                    entry.setTime(new Date());
                    ((Set) signalEntries).add(entry);
                }
            }
        }
        try {
            eventParticipantEAO.update(ep);
        } catch (EntityNotFoundException e) {
            throw new EJBException(e.getMessage(), e);
        }

    }

    private EventParticipantSignalEntry findCurrentSignalEntry(Set<? extends SignalEntry> signalEntries) {
        EventParticipantSignalEntry result = null;
        for (SignalEntry signalEntry : signalEntries) {
            EventParticipantSignalEntry entry = (EventParticipantSignalEntry) signalEntry;
            Date time = entry.getTime();
            if (result == null || time.after(result.getTime())) {
                result = entry;
            }
        }
        return result;
    }

    private EventParticipant getEventClient(String clientName) {
        EventParticipant result = null;
        List<EventParticipant> eventParticipants = participantEAO.findEventParticipants(clientName, true);
        for (EventParticipant ep : eventParticipants) {
            Event event = ep.getEvent();
            if (PROGRAM_NAME.equals(event.getProgramName())) {
                result = ep;
                break;
            }
        }
        return result;
    }

    public void sendUpdateSignalNotification(Map<String, String> valid, Map<String, String> invalid, Map<String, ProgramValidationMessage> messages){
    	String subject="Viridity:update signals";
    	StringBuilder builder=new StringBuilder();
    	builder.append("Date: ");
    	
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	builder.append(format.format(new Date()));
    	builder.append("\n");
    	builder.append("Updated signals successfully for the following accounts: \n");
    	for (String key: valid.keySet()) {
    		builder.append("  Customer service ID: ");
    		builder.append(key);
    		builder.append(", signal: ");
    		builder.append(valid.get(key));
    		builder.append("\n");
    	}
		builder.append("\n");
    	builder.append("Failed to update the following signals: \n");
    	for (String key: invalid.keySet()) {
    		builder.append("  Customer service ID: ");
    		builder.append(key);
    		builder.append(", signal: ");
    		builder.append(invalid.get(key));
    		builder.append("\n");
            ProgramValidationMessage message = messages.get(key);
            if (message != null) {
                builder.append(message.getDescription()).append("\n");
            }
    	}
    	
    	sendDRASOperatorEventNotification(subject, builder.toString(), 
    			NotificationMethod.getInstance(), new NotificationParametersVO(), PROGRAM_NAME, notifier);
    }
    

    @Override
    protected void processEvent(Event event, long ms) {
        String eventName = event.getEventName();
        if (event.getEndTime().getTime() < ms) {
            event = eventManager.getEventWithParticipantsAndSignals(eventName);

            //log event info to history tables for customer report
            reportEventHistory(event, false);

            // If this program sends messages for event start, send them now
            if (programSendsCompletedNotifications(event.getProgramName())) {
                sendDRASOperatorNotifications(event, "completed");
                sendProgramOperatorNotifications(event, "completed");
                sendParticipantNotifications(event, "completed");
            }

            deleteEvent(event);
        }
    }
    
    public synchronized void sendDRASOperatorEventNotification(
            String subject, String content, NotificationMethod method,
            NotificationParametersVO params, String programName, Notifier notifier) {
        ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);

        List<Contact> contacts = cm.getOperatorContacts();
        List<Contact> sendList = new ArrayList<Contact>();

        for (Contact c : contacts) {
        	if (c.eventNotificationOn()) {
                sendList.add(c);
            }
        }

        if (sendList.size() > 0) {
        	notifier.sendNotification(sendList, "operator", subject, content,
                    method, params, Environment.isAkuacomEmailOnly(),
                    programName);
        }
    }

}
