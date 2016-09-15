/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.irr.IRRProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.irr;

import com.akuacom.pss2.core.EJBFactory;
import javax.ejb.Stateless;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.data.DataManagerBean;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramPerf;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.util.LogUtils;

import com.akuacom.utils.lang.DateUtil;
import java.util.ArrayList;
import java.util.BitSet;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Timer;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.apache.log4j.Logger;

/**
 * The Class IRRProgramEJBBean.
 */
@Stateless
public class IRRProgramEJBBean extends ProgramEJBBean implements IRRProgramEJB.R,
        IRRProgramEJB.L {

    private static final Logger log = Logger
            .getLogger(IRRProgramEJBBean.class);

    @EJB
    protected DataManagerBean.L dataManager;

    public static final String SETPOINT_SIGNAL_NAME = "setpoint";
    public static final String PSEUDOGEN_SIGNAL_NAME = "pseudogen";
    public static final String MODE_SIGNAL_NAME = "mode";

    public static final double DEFAULT_STARTING_SETPOINT = 0.0;

    @Override
    public void initialize(String programName) {

        super.initialize(programName);

    }


    @Override
    public void createTimer(String programName) {
        super.createTimer(programName);
    }

    /**
     * This really should be in the base class
     * The main program timer fires when it's time to make tomorrows event
     */
    @Override
    public void processTimeout(Timer timer) {
          if (timer.getInfo() != null && timer.getInfo().toString().startsWith(this.getClass().getSimpleName())) {
            String progName = timer.getInfo().toString().substring(this.getClass().getSimpleName().length() + 1);
            log.debug(LogUtils.createLogEntry(progName, this.getClass().getName(), "processTimeout for "
                    + timer.getInfo() + " and program name: " + progName, null));
            
              try {
                  BitSet alreadyEvent = getEventsForTodayAndTomorrow(progName);
                  ProgramPerf program = programManager.getProgramPerf(progName);
                  Date autoRepeatTime = program.getAutoRepeatTimeOfDay();
                  if (autoRepeatTime != null) {
                      // There is an auto-repeat time for this program
                      autoRepeatTime = DateUtil.stripDate(autoRepeatTime);
                      Date timeNow = DateUtil.stripDate(new Date());
                      if (timeNow.after(autoRepeatTime)) {
                          // Time now is after the auto-repeat time
                          if (!alreadyEvent.get(1)) {
                              // There is no event for tomorrow
                              // Make an event for tomorrow
                              makeEvent(progName, false);
                          }
                      }
                  }
              } catch (Exception ex) {
                  log.error(LogUtils.createExceptionLogEntry(
                          "IRRProgramEJBBean", "processTimeout", ex));
              }
        }
    }
    
    
    
    @Override
    protected void setEventTiming(Event event, UtilityDREvent utilityDREvent) {
        Date now = new Date();
        event.setReceivedTime(now);
        event.setIssuedTime(now);

        if (event.getStartTime().getTime() < event.getIssuedTime().getTime()) {
            event.setStartTime(event.getIssuedTime());
        }

        Calendar tomorrowCal = new GregorianCalendar();
        tomorrowCal.setTime(event.getStartTime());
        tomorrowCal.set(Calendar.HOUR_OF_DAY, 23);
        tomorrowCal.set(Calendar.MINUTE, 59);
        tomorrowCal.set(Calendar.SECOND, 59);
        tomorrowCal.set(Calendar.MILLISECOND, 999);
        event.setEndTime(tomorrowCal.getTime());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#createProgramObject()
     */
    @Override
    public Program createProgramObject() {
        return new IRRProgram();
    }



    @Override
    public Collection<String> createEvent(String programName, Event event,
            UtilityDREvent utilityDREvent) {
        return makeNewEvent(programName);
    }

    protected Collection<String> makeNewEvent(String programName) {
        Collection<String> eventNames = new ArrayList<String>();
        BitSet alreadyEvent = getEventsForTodayAndTomorrow(programName);
        if (!alreadyEvent.get(0)) {
            // There's not already one for today
            // so generate an event covering the rest of today
            eventNames.add(makeEvent(programName, true));
        }

        ProgramManager pgMan = EJBFactory.getBean(ProgramManager.class);
        ProgramPerf program = pgMan.getProgramPerf(programName);
        Date autoRepeatTime = program.getAutoRepeatTimeOfDay();
        Date timeNow = new Date();
        if (autoRepeatTime == null) {
            autoRepeatTime = (Date) timeNow.clone();
        }
        timeNow = DateUtil.stripDate(timeNow);
        autoRepeatTime = DateUtil.stripDate(autoRepeatTime);
        if (!alreadyEvent.get(1)
                && (autoRepeatTime != null && timeNow.after(autoRepeatTime))
                ) {
            // There is not already an event for tomorrow,
            // There is an auto-repeat time
            // and it's past the time tomorrow event will get automatically
            // generated.
            // So generate an event for tomorrow in addition to the one for the
            // rest of today
            eventNames.add(makeEvent(programName, false));
        }
        return eventNames;
    }


    protected String makeEvent(String progName, boolean today) {
        // create new event
        Event irrEvent = new Event();
        irrEvent.setEventName(EventUtil.getUniqueEventName(progName
                + (today ? "-1" : "-2")));
        irrEvent.setProgramName(progName);

        Date now = new Date();
        if (!today) {
            Calendar tomorrowCal = new GregorianCalendar();
            tomorrowCal.setTime(DateUtil.stripTime(now));
            tomorrowCal.add(Calendar.DATE, 1);
            irrEvent.setStartTime(tomorrowCal.getTime());
        } else {
            irrEvent.setStartTime(now);
        }
        super.createEvent(progName, irrEvent, null);
        return irrEvent.getEventName();
    }


    /**
     * We override getProgramRules in order to insert a hardwired rule
     * that makes the mode HIGH for non-zero setpoints but NORMAL
     * for zero setpoints
     * 
     * @param program
     * @param eventTiming
     * @return
     */
    @Override
    protected List<ProgramRule> getProgramRules(Program program, EventTiming eventTiming) {
        // Get Custom rules
        ArrayList<ProgramRule> progRules = new ArrayList<ProgramRule>();

        ProgramRule rule = new ProgramRule();
        rule.setSource("Program");
        rule.setProgram(program);
        rule.setVariable("setpoint");
        rule.setOperator(Operator.NOT_EQUAL);
        rule.setValue(0.0);
        rule.setMode(Mode.HIGH);
        if (eventTiming != null) {
            rule.setStart(eventTiming.getStartTime());
            rule.setEnd(eventTiming.getEndTime());
        } else {
            Date dateNow = new Date();
            dateNow.setTime(dateNow.getTime() - 10000000);
            rule.setStart(dateNow);
            Date theFuture = new Date();
            theFuture.setTime(theFuture.getTime() + 10000000);
            rule.setEnd(theFuture);
        }
        progRules.add(rule);

        return progRules;
    }

    // This is a helper method that is rarely overridden by derivative programs
    // The default (false) allows ModeSlot signal elements to accumulate during
    // the course of an event.  Returning true keeps the list of ModeSlot
    // elements limited to just the most recent mode
    @Override
    protected boolean programLimitsModeEntries() {
        return true;
    }

    @Override
    public void updateSetpoint(String programName, List<ParticipantSetpoint> Xparticipants) throws ValidationException 
    {
        try {

            Date now = new Date();
            Program programWithPPandPRules = programManager.getProgramWithParticipantsAndPRules(programName);

            List<EventInfo> evts = getEvents(programName);

            // Check to see if there is even an event now. 
            if (evts == null || evts.isEmpty()) {
//                // no event, but a command has come in to change the sepoint
//                makeEvent(programName, true);
//                evts = getEvents(programName);            
                throw new ValidationException("Attempt to push IRR setpoint(s) without an active event");
            }

            for (EventInfo evt : evts) {
                Event event = getEvent(programName, evt.getEventName());
                if (event.getStartTime().before(now)) {
                    // We have an event, and it has started

                    for (ParticipantSetpoint CaisoPart : Xparticipants) {
                        Set<EventParticipant> eParticipants = new HashSet<EventParticipant>();
                        String participantName = CaisoPart.getParticipantName();
                        // Look up the participant account that matches the siteID
                        Participant cpart = participantManager.getParticipant(participantName);
                        if (cpart != null) {
                            Set<EventParticipant> allParts = cpart.getEventParticipants();
                            if (allParts != null) {
                                for (EventParticipant apart : allParts) {
                                    if (apart.getEvent().getEventName().equals(evt.getEventName())) {
                                        eParticipants.add(apart);
                                    }
                                }
                            }
                        }

                        for (EventParticipant part : eParticipants) {
                            if (!part.getParticipant().isClient()) {
                                Set<EventParticipantSignal> signals = part.getSignals();

                                if (signals == null) {
                                    signals = new HashSet<EventParticipantSignal>();
                                    part.setSignals(signals);
                                }

                                if (signals.isEmpty()) {
                                    EventParticipantSignal setpointSignal = new EventParticipantSignal();
                                    setpointSignal.setSignalDef(signalManager.getSignal(SETPOINT_SIGNAL_NAME));
                                    signals.add(setpointSignal);

                                    EventParticipantSignal pseudogenSignal = new EventParticipantSignal();
                                    pseudogenSignal.setSignalDef(signalManager.getSignal(PSEUDOGEN_SIGNAL_NAME));
                                    signals.add(pseudogenSignal);
                                }

                                EventParticipantSignal setpointSignal = new EventParticipantSignal();
                                Set<EventParticipantSignalEntry> setpointEntries =
                                        new HashSet<EventParticipantSignalEntry>();
                                EventParticipantSignalEntry setpointEntry = new EventParticipantSignalEntry();
                                setpointEntry.setParentSignal(setpointSignal);
                                setpointEntry.setNumberValue(CaisoPart.getSetpoint());
                                
                                Date setpointTime = now;
                                setpointEntry.setTime(setpointTime);  // Now is when we heard about it.  Make sure it doesn't get cropped
                                
                                setpointEntries.add(setpointEntry);
                                setpointSignal.setSignalDef(signalManager.getSignal(SETPOINT_SIGNAL_NAME));
                                setpointSignal.setSignalEntries(setpointEntries);

                                EventParticipantSignal pseudogenSignal = new EventParticipantSignal();
                                Set<EventParticipantSignalEntry> pseudogenEntries =
                                        new HashSet<EventParticipantSignalEntry>();
                                EventParticipantSignalEntry pseudogenEntry = new EventParticipantSignalEntry();
                                pseudogenEntry.setParentSignal(pseudogenSignal);
                                if (CaisoPart.getPseudoGenTime() != null) {
                                    pseudogenEntry.setNumberValue(CaisoPart.getPseudoGen());
                                    pseudogenEntry.setTime(CaisoPart.getPseudoGenTime().getTime()); 
                                    pseudogenEntries.add(pseudogenEntry);
                                }
                                pseudogenSignal.setSignalDef(signalManager.getSignal(PSEUDOGEN_SIGNAL_NAME));
                                pseudogenSignal.setSignalEntries(pseudogenEntries);



                                Set<EventParticipantSignal> inSignals = new HashSet<EventParticipantSignal>();
                                inSignals.add(setpointSignal);
                                inSignals.add(pseudogenSignal);
//                                inSignals.add(modeSignal);
                                setParticipantSignals(signals, inSignals);
                                processParticipantRulesAndSignals(programWithPPandPRules, event, null, part, null, null, setpointTime/*now*/,null,null);
                            }
                        }
                    }
                    eventEAO.update(event);
                }
            } // end of active events loop
        } 
        catch(ValidationException vex) {
            log.error(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, "event processing error: "+vex.getMessage(), null));
            throw vex;
        }
        catch (Exception ex) {
            log.error(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_EVENT, "event processing error: "+ex.getMessage(), null));
        }
    }

    /**
     * Returns the signals that are associated with a specific participant.
     * These are the signals that apply only to clients associated with that
     * participant.
     *
     * @param program
     * @return
     */
    @Override
    protected Set<EventParticipantSignal> getParticipantInputEventSignals(
            Program program, Event event, UtilityDREvent utilityDREvent,
            Participant participant, boolean isClient, Date now)
              {

        Set<EventParticipant> eparts = event.getEventParticipants();

        EventParticipant theRightParticipant = null;
        for (EventParticipant part : eparts) {
            if (part.getParticipant().getParticipantName().equals(participant.getParticipantName())) {
                theRightParticipant = part;
            }
        }

        Set<EventParticipantSignal> signals = theRightParticipant.getSignals();

        if (signals == null) {
            signals = new HashSet<EventParticipantSignal>();
        }

        if (signals.isEmpty()) {
            EventParticipantSignal setpointSignal = new EventParticipantSignal();
            setpointSignal.setSignalDef(signalManager
                    .getSignal(SETPOINT_SIGNAL_NAME));
            Set<EventParticipantSignalEntry> entries = new HashSet<EventParticipantSignalEntry>();
            EventParticipantSignalEntry setpoint = new EventParticipantSignalEntry();
            setpoint.setParentSignal(setpointSignal);
            setpoint.setNumberValue(DEFAULT_STARTING_SETPOINT);
            setpoint.setTime(new Date());
            entries.add(setpoint);
            setpointSignal.setSignalEntries(entries);
            signals.add(setpointSignal);


            EventParticipantSignal pseudogenSignal = new EventParticipantSignal();
            pseudogenSignal.setSignalDef(signalManager
                    .getSignal(PSEUDOGEN_SIGNAL_NAME));
            Set<EventParticipantSignalEntry> pgEntries = new HashSet<EventParticipantSignalEntry>();
            EventParticipantSignalEntry pseudogen = new EventParticipantSignalEntry();
            pseudogen.setParentSignal(pseudogenSignal);
            pseudogen.setNumberValue(0.0);
            pseudogen.setTime(new Date());
            pgEntries.add(pseudogen);
            pseudogenSignal.setSignalEntries(pgEntries);
            signals.add(pseudogenSignal);

        }
        return signals;
    }



    }
