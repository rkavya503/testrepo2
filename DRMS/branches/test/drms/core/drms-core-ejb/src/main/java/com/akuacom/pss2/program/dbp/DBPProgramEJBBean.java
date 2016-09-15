// $Revision$ $Date$
package com.akuacom.pss2.program.dbp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.SignalLevelMapper;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.bidding.BidBlock;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.util.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;

public abstract class DBPProgramEJBBean extends ProgramEJBBean {
    protected static final String BID_SIGNAL_NAME = "bid";

 	protected SignalDef bidSignalDef = null;

    private static final Logger log = Logger.getLogger(DBPBidProgramEJBBean.class.getName());

    /** The dbp da. */
    @EJB
    protected DBPDataAccess.L dbpDA;

    @Override
    protected Program getProgramForEventCreation(String programName){
    	Program program=programManager
    			.getProgramWithBidAndProgramParticipantsAndPRules(programName);
    	return program;
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
        try {
            DBPEvent dbpEvent = (DBPEvent) event;
            DBPProgram dbpProgram = (DBPProgram) program;

            if (utilityDREvent != null) {
                dbpEvent.setRespondBy(utilityDREvent.getBiddingInformation()
                        .getClosingTime().toGregorianCalendar().getTime());
            }
            dbpEvent.setDrasRespondBy(new Date(dbpEvent.getRespondBy()
                    .getTime()
                    - dbpProgram.getBidConfig().getDrasRespondByPeriodM()
                    * 60
                    * 1000));
            
            if(event.getUUID()==null){

            	dbpEvent.setBidBlocks(DBPUtils.getEventBidBlocks(dbpProgram,
                    dbpEvent));
            }

            if (dbpEvent.isManual()) {
                dbpEvent.setCurrentBidState(BidState.IDLE);
            }

            return super.initializeEvent(program, event, utilityDREvent);
        } catch (Exception e) {
            String message = "can't add event: " + event.getEventName();
            // DRMS-1664
            log.error(LogUtils.createLogEntry(program.getProgramName(),
                    LogUtils.CATAGORY_EVENT, message, event.toString()));
            log.debug(LogUtils.createExceptionLogEntry(
                    program.getProgramName(), LogUtils.CATAGORY_EVENT, e));
            throw new EJBException(message, e);
        }
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#newProgramEvent()
     */
    @Override
    public Event newProgramEvent() {
        return new DBPEvent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#createProgramObject()
     */
    @Override
    public Program createProgramObject() {
        return new DBPProgram();
    }

    /**
     * Get the signals for the event.
     * 
     * @param program
     *            the program
     * @param event
     *            the event
     *
     * @return the event default signals
     */
    @Override
    protected List<EventParticipantRule> getEventParticipantRules(
            Program program, EventTiming event,
            ProgramParticipant programParticipant) {
        List<EventParticipantRule> rules = new ArrayList<EventParticipantRule>();
        rules.addAll(super.getEventParticipantRules(program, event, programParticipant));
        
        Map<String, List<String>> levelMap = dbpDA.getLevelMap(program
                .getProgramName(), programParticipant.getParticipantName(),
                programParticipant.getParticipant().isClient());
        Calendar cal = new GregorianCalendar();
        cal.setTime(event.getStartTime());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        for (String timeBlockString : levelMap.keySet()) {
            TimeBlock timeBlock = SignalLevelMapper
                    .getTimeBlock(timeBlockString);
            double[] values = SignalLevelMapper.getValues(levelMap
                    .get(timeBlockString));
            if (values[2] > 0.0) {
                EventParticipantRule rule = new EventParticipantRule();
                rule.setStart(timeBlock.getStartTime(year, month, day));
                rule.setEnd(timeBlock.getEndTime(year, month, day));
                rule.setMode(Rule.Mode.HIGH);
                rule.setVariable(BID_SIGNAL_NAME);
                rule.setOperator(Operator.GREATER_THAN_OR_EQUAL);
                rule.setValue(values[2]);
                rule.setSource("Bid Mapping " + timeBlockString + " HIGH");
                rules.add(rule);
                if (values[2] == 1.0) {
                    continue;
                }
            }
            if (values[1] > 0.0) {
                EventParticipantRule rule = new EventParticipantRule();
                rule.setStart(timeBlock.getStartTime(year, month, day));
                rule.setEnd(timeBlock.getEndTime(year, month, day));
                rule.setMode(Rule.Mode.MODERATE);
                rule.setVariable(BID_SIGNAL_NAME);
                rule.setOperator(Operator.GREATER_THAN_OR_EQUAL);
                rule.setValue(values[1]);
                rule.setSource("Bid Mapping " + timeBlockString + " MODERATE");
                rules.add(rule);
                if (values[1] == 0.0) {
                    continue;
                }
            }
            EventParticipantRule rule = new EventParticipantRule();
            rule.setStart(timeBlock.getStartTime(year, month, day));
            rule.setEnd(timeBlock.getEndTime(year, month, day));
            rule.setMode(Rule.Mode.NORMAL);
            rule.setVariable(BID_SIGNAL_NAME);
            rule.setOperator(Operator.ALWAYS);
            rule.setValue(0.0);
            rule.setSource("Bid Mapping " + timeBlockString + " NORMAL");
            rules.add(rule);

        }
        return rules;
    }

    private void getBidData(Program program, Event event,
		Participant participant, boolean isClient,
            Set<? extends EventParticipantSignalEntry> bidSignalEntries) {
		List<BidEntry> bidEntries =
			getCurrentBid(program.getProgramName(), event,
			participant, participant.isClient());
        for (BidEntry bidEntry : bidEntries) {
            if (bidEntry.isActive()) {
                EventParticipantSignalEntry bidSignalEntry = new EventParticipantSignalEntry();
                bidSignalEntry.setTime(bidEntry.getBlockStart());
                bidSignalEntry.setNumberValue(bidEntry.getReductionKW());
                ((Set) bidSignalEntries).add(bidSignalEntry);
            }
        }
    }

    private List<BidEntry> getCurrentBid(String programName, Event event,
            Participant participant, boolean isClient) {

        try {
            EventParticipant eventParticipant = null;
            final String participantName = participant.getParticipantName();
            for (EventParticipant ep : event.getEventParticipants()) {
                if (ep != null && participantName.equals(ep.getParticipant().getParticipantName())
                        && isClient == ep.getParticipant().isClient()) {
                    eventParticipant = ep;
                    break;
                }
            }

            if (eventParticipant == null) {
                String message = "participant " + participantName
                        + " for event " + event
                        + " in program " + programName
                        + " doesn't exist";
                //DRMS-1664
                log.warn(message);
                throw new EJBException(message);
            }

            final ArrayList<BidEntry> results = new ArrayList<BidEntry>();
            for (EventParticipantBidEntry entryDao : eventParticipant.getBidEntries()) {
                final BidEntry entry = new BidEntry();
                entry.setActive(entryDao.isActive());
                entry.setBlockEnd(entryDao.getEndTime());
                entry.setBlockStart(entryDao.getStartTime());
                entry.setId(entryDao.getUUID());
                entry.setPriceLevel(entryDao.getPriceLevel());
                entry.setReductionKW(entryDao.getReductionKW());
                entry.setParticipantName(participantName);
                entry.setProgramName(programName);
                results.add(entry);
            }
            return results;
        } catch (Exception ex) {
            String message = "error getting bids for participant "
                    + participant + " for event " + event
                    + " in program " + programName;
            // DRMS-1664
            log.warn(message, ex);
            throw new EJBException(message, ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramEJBBean#getEventSignals(com.akuacom.pss2
     * .core.model.Program, com.akuacom.pss2.core.model.Event,
     * org.openadr.dras.utilitydrevent.UtilityDREvent)
     */
    @Override
    protected Set<EventParticipantSignal> getParticipantInputEventSignals(
            Program program, Event event, UtilityDREvent utilityDREvent,
            Participant participant, boolean isClient, Date now)
            throws ProgramValidationException {
        EventParticipantSignal bidSignal = new EventParticipantSignal();
		if (bidSignalDef == null) {
			bidSignalDef = EJBFactory.getBean(SignalManager.class).getSignal(BID_SIGNAL_NAME);
		}
		bidSignal.setSignalDef(bidSignalDef);

        // TODO: refactor: this used to be a SortedArrayList
        HashSet<EventParticipantSignalEntry> bidSignalEntries = new HashSet<EventParticipantSignalEntry>();

		getBidData(program, event,  participant, isClient, bidSignalEntries);
		for(EventParticipantSignalEntry entry: bidSignalEntries)
		{
			entry.setParentSignal(bidSignal);
		}
		bidSignal.setSignalEntries(bidSignalEntries);

		Set<EventParticipantSignal> inputSignals =
			new HashSet<EventParticipantSignal>();
        inputSignals.add(bidSignal);
        return inputSignals;
    }
    @Override
    public void addParticipant(String programName, String participantName,
            boolean isClient) {
        super.addParticipant(programName, participantName, isClient);
        Program program = programManager.getProgramWithLoadBid(programName);
        TimeBlock[] timeBlocks = DBPUtils.getTimeBlocks(program.getBidConfig().getBidBlocks());
        final Map<String, List<String>> result = new HashMap<String, List<String>>();
        for (TimeBlock timeBlock : timeBlocks)
        {
            final String timeBlockString = SignalLevelMapper.getTimeBlock(timeBlock);
            List<String> rules = new ArrayList<String>(3);
			rules.add("x");
			rules.add("x");
			rules.add("1");
            result.put(timeBlockString, rules);
        }
        dbpDA.setLevelMap(programName, participantName, isClient,result);
    }
}
