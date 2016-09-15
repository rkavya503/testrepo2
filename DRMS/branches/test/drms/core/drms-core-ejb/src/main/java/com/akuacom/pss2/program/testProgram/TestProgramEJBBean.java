/**
 * 
 */
package com.akuacom.pss2.program.testProgram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.demo.DemoProgramEJBBean;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.drw.CacheNotificationMessage;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 *
 */
@Stateless
public class TestProgramEJBBean extends DemoProgramEJBBean implements
		TestProgramEJB.L, TestProgramEJB.R {

    @Override
	public Program createProgramObject() {
		return new TestProgram();
	}
	
    @Override
    public Program loadProgram(Properties config, int i) {
    	Program program=super.loadProgram(config, i);
    	program.setProgramName(TestProgram.PROGRAM_NAME);
    	return program;
    }
    
	/**
	 * This method removes the program participant rules from the parent method.
	 * For client test program, all participants are enrolled in test program by default.
	 * 
	 */
	@Override
	protected List<EventParticipantRule> createEventParticipantRules(
			Program program, EventTiming eventTiming, String participantName,
			boolean isClient, UtilityDREvent utilityDREvent) {
		List<EventParticipantRule> eventRuleEAOs = new ArrayList<EventParticipantRule>();

		int sortOrder = 0;

		eventRuleEAOs.addAll(getProgramRules(program, eventTiming,
				utilityDREvent));

		for (EventParticipantRule eventRuleEAO : eventRuleEAOs) {
			eventRuleEAO.setSortOrder(sortOrder++);
		}

		return eventRuleEAOs;
	}

    @Override
    protected Set<EventParticipantSignal> updateOutputSignals(
            Set<EventParticipantSignal> existingSignals,
            Set<EventParticipantSignal> newSignals, Date now) {
        if (existingSignals == null || existingSignals.size() == 0 ) {
            for (EventParticipantSignal newSignal : newSignals) {
                for (SignalEntry entry : newSignal.getSignalEntries()) {
                    entry.setParentSignal(newSignal);
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
            Program program = getProgramForEventCreation(programName);

            if (event.getIssuedTime() == null) {
                setEventTiming(event, utilityDREvent);
            }
            if (event.getStartTime().before(new Date())) {
                event.setEventStatus(EventStatus.ACTIVE);
            } else {
                event.setEventStatus(EventStatus.RECEIVED);
            }

            EventStateCacheHelper esch = EventStateCacheHelper.getInstance();

            for(EventParticipant eventParticipant: event.getParticipants())
            {
                esch.delete(eventParticipant.getParticipant().getParticipantName());
            }

            ProgramValidator programValidator = ValidatorFactory
                    .getProgramValidator(program);
            programValidator.validateEvent(event);

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
            
            if (EventStatus.ACTIVE == event.getEventStatus()) {
                sendDRASOperatorNotifications(event, "started");
                sendParticipantNotifications(event, "started");
            } else {
                sendDRASOperatorNotifications(event, "created");
                sendProgramOperatorNotifications(event, "created");
            }
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

//            topicPublisher.publish(new CacheNotificationMessage(program.getProgramClass(), null, event.getStartTime().before(new Date())));

        } catch (ProgramValidationException e) {
            logEntry.setDescription("event creation failed: "
                    + event.getEventName());
            logEntry.setLongDescr(event.toString());
            throw new EJBException(e);
        }
        return names;
    }
}
