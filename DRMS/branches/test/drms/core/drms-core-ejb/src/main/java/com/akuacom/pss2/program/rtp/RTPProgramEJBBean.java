/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.rtp.RTPProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.rtp;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.Timer;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramPerf;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;
import com.akuacom.utils.lang.TimingUtil;

/**
 * The Class RTPProgramEJBBean.
 */
@Stateless
public abstract class RTPProgramEJBBean extends ProgramEJBBean implements
        RTPProgramEJB.R, RTPProgramEJB.L {
    protected static final String PRICE_SIGNAL_NAME = "price";
    // 30 seconds. Why? Just to move it after the initial startup crunch
    public static final int TIMER_INITIAL_WAIT_MS = 30 * TimingUtil.SECOND_MS;
    // 30 minutes
    public static final int TIMER_RETRY_WAIT_MS = 30 * TimingUtil.MINUTE_MS;

    // Default remote polling interval for polled programms
    // where the price connector doesn't have a suggested polling interval
    // 30 minute default
    public static final int DEFAULT_POLLING_INTERVAL = 30 * TimingUtil.MINUTE_MS;
    // Don't poll faster than once per minute
    protected static final int MINIMUM_POLLING_INTERVAL = TimingUtil.MINUTE_MS;

    /** The Constant log. */
    protected static final Logger log = Logger
            .getLogger(RTPProgramEJBBean.class.getName());

    abstract protected String getThisProgramName();

    abstract protected String getPriceConnectorClassName();

    protected int getInitialStartupDelayTime() {
        return TIMER_INITIAL_WAIT_MS;
    }

    // The time the system will wait to retry if a non-polled remote data fetch
    // returns nothing
    protected int getRetryDelayTime() {
        return TIMER_RETRY_WAIT_MS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#createProgramObject()
     */
    @Override
    public Program createProgramObject() {
        return new RTPProgram();
    }

    /**
     * Start time can not be null. Issue time should be null.
     */
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

    protected String makeEvent(String progName, boolean today) {
        // create new event
        Event rptEvent = new Event();
        rptEvent.setEventName(EventUtil.getUniqueEventName(progName
                + (today ? "-1" : "-2")));
        rptEvent.setProgramName(progName);

        Date now = new Date();
        if (!today) {
            Calendar tomorrowCal = new GregorianCalendar();
            tomorrowCal.setTime(DateUtil.stripTime(now));
            tomorrowCal.add(Calendar.DATE, 1);
            rptEvent.setStartTime(tomorrowCal.getTime());
        } else {
            rptEvent.setStartTime(now);
        }

        try {
            super.createEvent(progName, rptEvent, null);
        } catch (Exception ex) {
            // ignore. Exception should have been logged higher up
            // and we want to create 2 events (today and tomorrow)
        }
        return rptEvent.getEventName();
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
                && (autoRepeatTime != null && timeNow.after(autoRepeatTime))) {
            // There is not already an event for tomorrow,
            // There is an auto-repeat time
            // and it's past the time tomorrow event will get automatically
            // generated.
            // So generate an event for tomorrow in addition to the ond for the
            // rest of today
            eventNames.add(makeEvent(programName, false));
        }
        return eventNames;
    }

    /*******************************************
     * The following will attempt to resolve a real time data connector that can
     * be used to retrieve RTP price data
     *******************************************/
    @SuppressWarnings("unchecked")
    protected RTPPriceConnector getPriceConnector() {
        RTPPriceConnector priceConnector = null;
        String connectorName = getPriceConnectorClassName(); // get connector
                                                             // name from
                                                             // implementation
        if (connectorName == null) {

            log.debug(LogUtils.createLogEntry(getThisProgramName(), this
                    .getClass().getName(),
                    "Error finding RTP Price connector name", null));
        }

        try {
            Class<RTPPriceConnector> connectorClass = (Class<RTPPriceConnector>) Class
                    .forName(connectorName);
            priceConnector = connectorClass.newInstance();
        } catch (Exception e) {

            log.debug(LogUtils.createExceptionLogEntry(getThisProgramName(),
                    this.getClass().getName(), e));

        }
        return priceConnector;
    }

    /**
     * This is where RTP price information is requested from whatever remote
     * source it comes from.
     * 
     * An RTPPriceConnector is obtained, queried about its report intervals and
     * then polled to read prices from a starting point up until however far
     * into the future the price connector tells us that prices should normally
     * be published
     */
    protected List<RTPPrice> getRemoteRTPPrices(Date getPricesAfter) {

        List<RTPPrice> res = new ArrayList<RTPPrice>();

        // get a reference to a data driver class that will fetch the price
        // information
        RTPPriceConnector priceConnector = getPriceConnector();
        if (priceConnector == null) {
            return res; // empty list with logged exceptions
        }

        // Now reportHour is the rounded-down hour of the most recent price we
        // have
        // (Or it might be the beginning of this current hour

        // find out how big the report intervals are for this service
        int intervalsPerDay = priceConnector.getIntervalsPerDay();
        int secondsPerInterval = (24 * 60 * 60) / intervalsPerDay;
        int hoursPerInterval = secondsPerInterval / (60 * 60);

        // Limit the possibility of a very long startup
        // if server has been shut down for a long time
        // and thus the most recent stored price is really old
        Calendar thisMorning = Calendar.getInstance();
        thisMorning.set(Calendar.HOUR, 0);
        thisMorning.set(Calendar.MINUTE, 0);
        thisMorning.set(Calendar.SECOND, 0);
        thisMorning.set(Calendar.MILLISECOND, 0);
        if (getPricesAfter.before(thisMorning.getTime())) {
            getPricesAfter = thisMorning.getTime(); // Don't backfill earlier
                                                    // than today
        }

        Calendar reportInterval = Calendar.getInstance();
        reportInterval.setTime(getPricesAfter); // This could be a partial
                                                // interval
        reportInterval.set(Calendar.MINUTE, 0); // so we set it to the beginning
        reportInterval.set(Calendar.SECOND, 0);
        reportInterval.set(Calendar.MILLISECOND, 0);
        reportInterval.set(Calendar.HOUR_OF_DAY,
                (reportInterval.get(Calendar.HOUR_OF_DAY) / hoursPerInterval)
                        * hoursPerInterval); // truncate request time to
                                             // beginning of interval

        // Calculate a point in the future up to which to try to read prices
        Calendar lookAhead = Calendar.getInstance();
        lookAhead.set(Calendar.MINUTE, 0);
        lookAhead.set(Calendar.SECOND, 0);
        lookAhead.set(Calendar.MILLISECOND, 0);
        int currentInterval = (lookAhead.get(Calendar.HOUR_OF_DAY) / hoursPerInterval);
        lookAhead.set(Calendar.HOUR_OF_DAY, currentInterval * hoursPerInterval);
        int lookaheadIntervals = priceConnector.getNumFutureIntervals();
        // try to read promised intervals in the future
        lookAhead.add(Calendar.SECOND, secondsPerInterval); // to end of current
                                                            // interval
        lookAhead.add(Calendar.SECOND, secondsPerInterval * lookaheadIntervals);
        // plus how many more?

        DateFormat df = DateFormat.getDateTimeInstance();
        
        // request reports up until the point in time where we expect them to
        // stop returning data
        while (reportInterval.before(lookAhead)) {
            String reportIntervalTime = df.format(reportInterval.getTime());
            List<PriceTransition> priceList = priceConnector.getPriceReport(
                    reportInterval, null);
            reportInterval.add(Calendar.SECOND, secondsPerInterval);

            if (priceList == null || priceList.size() == 0) {
                log.debug(LogUtils.createLogEntry(getThisProgramName(), this
                        .getClass().getName(), getThisProgramName()
                        + " RTP source returned empty report for "
                        + reportIntervalTime, null));

            }

            if (priceList != null && priceList.size() > 0) {
                for (PriceTransition price : priceList) {
                    Date priceTime = price.getTime();
                    if (priceTime.after(getPricesAfter)) // if this is a new
                                                         // price
                    {
                        RTPPrice rtpPrice = new RTPPrice();
                        rtpPrice.setIntervalTime(price.getTime());
                        rtpPrice.setPrice(price.getPrice());
                        rtpPrice.setProgramName(getThisProgramName());
                        res.add(rtpPrice);
                    }
                }
            }
        }
        return res;
    }

    // Called by timer events
    protected void processRTPTimerUpdate() {
        String programName = getThisProgramName();
        int numNewPrices = 0;

        // This is a polled price stream
        // Bring the RTPPrice db up to date from remote data
        // get current and look-ahead pricing data
        RTPPrice lastPrice = programManager.getLastRealTimePrice(programName);
        Date now = new Date();
        if (lastPrice == null) {

            log.debug(LogUtils.createLogEntry(programName, this.getClass()
                    .getName(), "Could not find latest " + programName
                    + " Price data. Continuing with standin object", null));

            lastPrice = new RTPPrice();
            lastPrice.setProgramName(programName);
            lastPrice.setIntervalTime(now);
        }
        // Access the remote system at the utility and obtain a price report
        List<RTPPrice> prices = getRemoteRTPPrices(lastPrice.getIntervalTime());
        // get all price events, iterate through them and update them into the
        // db
        for (RTPPrice price : prices) {
            programManager.setRealTimePrice(price);
        }
        numNewPrices = prices.size();

        // Now, if there were some new prices, update signals
        if (numNewPrices > 0) {
            List<EventInfo> events = programManager
                    .getEventsForProgram(programName);
            for (EventInfo eventInfo : events) {
                Event event = 
                        this.getEvent(programName, eventInfo.getEventName());
                Set<EventSignalEntry> newPrices = 
                        getRTPPriceSignals(programName, event);
                updateEventSignal(event, PRICE_SIGNAL_NAME, newPrices, null, false);
            }
        }
    }

    /**
     * This is where new prices get translated into program input signals during
     * event creation
     * 
     * @param programName
     * @param event
     * @return
     */
    @Override
    public Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) {
        Set<EventSignalEntry> priceEntries = getRTPPriceSignals(
                program.getProgramName(), event);

        EventSignal priceSignal = new EventSignal();
        priceSignal.setSignalDef(signalManager.getSignal(PRICE_SIGNAL_NAME));
        priceSignal.setSignalEntries(priceEntries);
        Set<EventSignal> newSignals = new HashSet<EventSignal>();
        newSignals.add(priceSignal);

        return newSignals;
    }

    protected Set<EventSignalEntry> getRTPPriceSignals(String programName,
            Event event) {
        // A timer has been filling price records into a db table
        // Read the db price records that polling has been fillin in
        List<RTPPrice> priceList;
        Date now = new Date();
        priceList = programManager.getCurrentRealTimePrices(programName);

        Set<EventSignalEntry> priceEntries = new HashSet<EventSignalEntry>();
        for (RTPPrice price : priceList) {
            EventSignalEntry priceEntry = new EventSignalEntry();
            priceEntry.setTime(price.getIntervalTime());
            priceEntry.setNumberValue(price.getPrice());

            if (price.getIntervalTime().before(event.getEndTime())
                    && price.getIntervalTime().after(now)) {
                priceEntries.add(priceEntry);
            }
        }
        return priceEntries;
    }

    // Typically called by RTP implementation EJB timers
    protected void processNewRtpEvent(String progName) {
        // get program name
        List<String> progNames = EJBFactory.getBean(ProgramManager.class)
                .getPrograms();
        for (String lookName : progNames) {
            if (getThisProgramName().equals(lookName)) {
                // create new event
                makeNewEvent(lookName);
            }
        }
    }

    /**
     * Override of super to handle timeouts.
     */
    @Override
    public void processTimeout(Timer timer) {
        if (getThisProgramName().equals(timer.getInfo())) {

            log.debug(LogUtils.createLogEntry(getThisProgramName(), this
                    .getClass().getName(),
                    " processTimeout for " + timer.getInfo(), null));

            processRTPTimerUpdate();
        } else if (timer.getInfo() != null
                && timer.getInfo().toString()
                        .startsWith(this.getClass().getSimpleName())) {
            String progName = timer.getInfo().toString()
                    .substring(this.getClass().getSimpleName().length() + 1);

            log.debug(LogUtils.createLogEntry(progName, this.getClass()
                    .getName(), " processTimeout for " + timer.getInfo(), null));

            processNewRtpEvent(progName);
        }
    }

    /**
     * Override of super initialize to also add timer for RTP updates.
     * 
     * @param programName
     *            program name
     */
    @Override
    public void initialize(String programName) {
        super.initialize(programName);

        // we need to create a polling timer for this program
        // See if there's already a timer for this program
        javax.ejb.TimerService timerService = context.getTimerService();
        Collection<Timer> timers = timerService.getTimers();
        boolean found = false;
        for (Timer timer : timers) {
            if (getThisProgramName().equals(timer.getInfo())) {
                found = true;
                break; // there is
            }
        }

        // There is not a timer for this program. So we might make one
        if (!found) {
            RTPPriceConnector priceConnector = getPriceConnector();
            long pollingInterval = priceConnector
                    .getRecommendedPollingInterval();
            if (pollingInterval <= 0) {
                pollingInterval = DEFAULT_POLLING_INTERVAL;
            }
            if (pollingInterval < MINIMUM_POLLING_INTERVAL) {
                pollingInterval = MINIMUM_POLLING_INTERVAL;
            }
            // The price connector can return -1 to indicate no polling
            // for this program

            log.debug(LogUtils.createLogEntry(getThisProgramName(), this
                    .getClass().getName(), " initialize for "
                    + getThisProgramName(), null));
            timerService.createTimer(getInitialStartupDelayTime(),
                    pollingInterval, getThisProgramName());
        }
    }

    protected void createOneTimeRetryTimer() {
        log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
                "creating one time timer to retry remote data", null));

        javax.ejb.TimerService timerService = context.getTimerService();
        timerService.createTimer(getInitialStartupDelayTime(),
                getThisProgramName());
    }
}
