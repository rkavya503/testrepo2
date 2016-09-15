/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.scertp.SCERTPProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp2013;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.ejb.Stateless;
import javax.ejb.Timer;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.annotations.EpicMethod;
import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEmailFormatter;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.SceEventEmailFormatter;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantRtpStrategy;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramPerf;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramUserBaseEAO;
import com.akuacom.pss2.program.scertp.NotConfiguredException;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.SCERTPEvent;
import com.akuacom.pss2.program.scertp.SCERTPEventRateInfo;
import com.akuacom.pss2.program.scertp.SCERTPEventWeatherInfo;
import com.akuacom.pss2.program.scertp.SCERTPProgramEJBBean;
import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class SCERTPProgramEJBBean.
 */
@Stateless
public class SCERTPProgramEJB2013Bean extends SCERTPProgramEJBBean implements
        SCERTPProgramEJB2013.R, SCERTPProgramEJB2013.L {

	private static final Logger log =
		Logger.getLogger(SCERTPProgramEJB2013Bean.class);

    @EJB
    private ParticipantManager.L paticipantManager;
    
    @EJB
    ProgramUserBaseEAO.L userEAO;
    
    public static final String MODE_SIGNAL_NAME = "mode";

    /**
	 * This method returns the price for one hour of an SCE RTP day
     * It is expensive, because it calls getRates to fetch a while day
     * 
     * only used to validate season and strategy category configuration
     * 
     */
    @Override
    //OVerride supper class's behavior
    public double getRate(String programName, Date time) throws NotConfiguredException 
    {
    	//FIXME: no longer used
        double rate = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        SCERTPEventRateInfo rateInfo = new SCERTPEventRateInfo();
        getRateList(programName, time, rateInfo);
        double[] rates = rateInfo.getRates();
        if (rates != null && rates.length > 0) {
            rate = rates[Calendar.HOUR_OF_DAY];
        }
        return rate;
    }

    /**
     * This method returns a whole day of prices for SCE rtp
     * This is where daylight saving time corrections are performed
     *
     * 1/ The applicable temperature for each type of day is the prior day's Los Angeles Downtown site maximum temperature as recorded by the National
     * Weather Service. In the event that data is unavailable from Downtown LA as the primary source, data collected by the National Weather Service from
     * Long Beach Airport shall be used. Where data is not available from00 either site, SCE shall enact its procedure for emergency data collection in
     * order to provide substitute temperature data. The seasons and holidays are set forth in the Special Conditions of the Schedule(s) referenced above.
     * 2/ During Pacific Standard Time, the applicable hour and corresponding rate is for the hour ENDING at the hour shown.
     * During Daylight Saving Time, the applicable hour and corresponding rate is for the hour BEGINNING at the hour shown.
     * 3/ During Daylight Saving Time, the applicable rate for the hour beginning 12 midnight is the rate from the applicable day/temperature
     * column in effect on the PRIOR day for the hour beginning 12 midnight.
     * Transition to and from Daylight Saving Time: When Daylight Saving Time begins, the applicable hour skips from 2 a.m.
     * to 3 a.m. and the rate is the rate applicable to the hour beginning at 3 a.m. Similarly, when Pacific Standard Time resumes, the time
     * changes from 2 a.m. back to 1 a.m. and the rate for the beginning of Pacific Standard Time is the rate for the hour ending 2 a.m.
     * Note: In the transition to Daylight Saving Time an hour is lost, while in the transition to Pacific Standard Time, an hour is gained.
     * 4/ The ongoing Competition Transition Charge (CTC) of $0.00411 per kWh is recovered in the URG component.
     * 
     * @param programName
     * @param time
     * @return
     * @throws NotConfiguredException
     */
// rateInfo is populated
    private void getRateList(String programName, Date time,
            SCERTPEventRateInfo rateInfo) throws NotConfiguredException {
        try {
            ProgramManager prgmManager = EJBFactory.getBean(ProgramManager.class);
            ProgramPerf program = prgmManager.getProgramPerf(programName);

            Calendar rateDay = Calendar.getInstance();
            rateDay.setTime(DateUtil.stripTime(time));
            Calendar dayBeforeRateDay = (Calendar) rateDay.clone();
            dayBeforeRateDay.add(Calendar.DATE, -1);

            SCERTPEventWeatherInfo winfo = getDayBeforeWeather(rateDay.getTime());
            Double dayBeforeHigh = winfo.getHighTemp();

			// only for LAABF
			if (isEnabledMultipleRTPEvents()
					&& (winfo.getWeatherRecord() == null))
				throw new NotConfiguredException(
						"No Weather forcast for  programName  " + programName
								+ "and  time: " + time);
			//validation for session configuration
            String season = getSeasonCached(programName, rateDay.getTime());
            List<RTPConfig> configs = rtpConfigEAO.getRTPConfigList(program.getUUID(), season, rateDay, dayBeforeHigh);

            if (configs == null || configs.isEmpty()) {
            	
                throw new NotConfiguredException(
                        "SCE RTP rate is not configured for program: "
                                + programName + " time: " + time);
            }
            //SCE RTP rate is no longer required
        } catch (AppServiceException e) {
            throw new EJBException(e);
        }
    }

  

    /**
     * Override of super to handle timeouts.
     */
    @EpicMethod
    @Override
    public void processTimeout(Timer timer) {

        // Sometimes timers get hooked up to programs that aren't turned on       
        if (!thisProgramIsUsed()) { 
            timer.cancel();
            return; 
        }
                
        if (SCERTP_WEATHER_TIMER.equals(timer.getInfo())) {
            log.debug("SCERTPProgramEJBBean weather polling timer for " + timer.getInfo());
            try {
            	log.debug(LogUtils.createLogEntry("SCERTP", SCERTPProgramEJB2013Bean.class
        				.getName(), "timeout", ""));
                pollWeatherData();

                // let the SCE RTP programs see if a temperature change has
                // changed prices in any active events
                List<String> programs = programManager.getPrograms();
                for (String programName : programs) {
                    Program program = programManager.getProgramOnly(programName);
                    if (program.getClassName().startsWith("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013")) {
                        checkForRevisedPrices(program.getProgramName());
                    }
                }

            } catch (Exception ex) {
                log.error(LogUtils.createExceptionLogEntry("SCERTPProgramEJBBean", "Weather Polling", ex));
            }
        }
                
        else if (timer.getInfo() != null && timer.getInfo().toString().startsWith(this.getClass().getSimpleName())) {
            String progName = timer.getInfo().toString().substring(this.getClass().getSimpleName().length() + 1);
            log.debug(LogUtils.createLogEntry(progName, this.getClass().getName(), 
                    "SCERTPProgramEJBBean processTimeout for " + timer.getInfo() + " and program name: " + progName, null));
            try {
				boolean enableMultipleRTPEvents = this
						.isEnabledMultipleRTPEvents();
				Date today = DateUtil.stripTime(new Date());
				if (enableMultipleRTPEvents) {
					// then create five scheduled events start from tomorrow
					List<Date> days = Arrays.asList(
							DateUtil.getNext(today, 1), // normal schedule event
							DateUtil.getNext(today, 2), // exception scheduel event
							DateUtil.getNext(today, 3), // same as above
							DateUtil.getNext(today, 4), // same as above
							DateUtil.getNext(today, 5));// same as above
					for (Date day : days) {
						processNewSceRtpEvent(progName, day);
					}
				} else if (!eventExistsTomorrow(progName)) {
					// create events for tomorrow only
					processNewSceRtpEvent(progName, DateUtil.getNext(today, 1));
				}
            } catch (Exception ex) {
                log.error(LogUtils.createExceptionLogEntry("SCERTPProgramEJBBean", "processTimeout", ex));
            }
        }
    }
 // priceEntries and rateInfo are both populated    
    protected void getPriceData2(String programName, 
            EventTiming eventTiming,
            Set<EventSignalEntry> priceEntries, EventSignal priceSignal,
            SCERTPEventRateInfo rateInfo) 
            throws NotConfiguredException 
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(eventTiming.getStartTime());
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);

        // rateInfo is populated
        getRateList(programName, calendar.getTime(), rateInfo);
        double[] rates = rateInfo.getRates();

        calendar.setTime(DateUtil.stripTime(eventTiming.getStartTime()));
        calendar.set(Calendar.HOUR, startHour);
        for (int i = startHour; i < 24; i++) 
        {
            EventSignalEntry priceEntry = new EventSignalEntry();
            priceEntry.setEventSignal(priceSignal);
            priceEntry.setTime(calendar.getTime());
            priceEntry.setNumberValue(rates[i]);
            priceEntries.add(priceEntry);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
    }

	// priceEntries and rateInfo are both populated    
    protected void getPriceData(String programName, 
            EventTiming eventTiming,
            Set<EventSignalEntry> priceEntries, EventSignal priceSignal,
            SCERTPEventRateInfo rateInfo) 
            throws NotConfiguredException 
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(eventTiming.getStartTime());
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        
        //init weather
        
        Date time = calendar.getTime();
        ProgramManager prgmManager = EJBFactory.getBean(ProgramManager.class);
        ProgramPerf program = prgmManager.getProgramPerf(programName);

        Calendar rateDay = Calendar.getInstance();
        rateDay.setTime(DateUtil.stripTime(time));
        Calendar dayBeforeRateDay = (Calendar) rateDay.clone();
        dayBeforeRateDay.add(Calendar.DATE, -1);

        SCERTPEventWeatherInfo winfo = getDayBeforeWeather(rateDay.getTime());
        Double dayBeforeHigh = winfo.getHighTemp();

		// only for LAABF
		if (isEnabledMultipleRTPEvents()
				&& (winfo.getWeatherRecord() == null))
			throw new NotConfiguredException(
					"No Weather forcast for  programName  " + programName
							+ "and  time: " + time);
		//validation for session configuration
        String season = getSeasonCached(programName, rateDay.getTime());
        
        rateInfo.setEventWeatherInfo(winfo);
        
        String seasonName = null;
		try {
			seasonName = getSeason(programName, eventTiming.getStartTime());
		} catch (NotConfiguredException ne) {
			ne.printStackTrace();
		}
    	String strategyName = null;
		try {
			strategyName = getRTPConfigName(programName, seasonName, winfo.getWeatherRecord().getForecastHigh0());
		} catch (AppServiceException ae) {
			ae.printStackTrace();
		}
		rateInfo.setTemperatureRange(strategyName);

        calendar.setTime(DateUtil.stripTime(eventTiming.getStartTime()));
        calendar.set(Calendar.HOUR, startHour);
        for (int i = startHour; i < 24; i++) 
        {
            EventSignalEntry priceEntry = new EventSignalEntry();
            priceEntry.setEventSignal(priceSignal);
            priceEntry.setTime(calendar.getTime());
            priceEntry.setNumberValue(0.0);
            priceEntries.add(priceEntry);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
    }
    
    /**
     * This is where prices get translated into program input signals
     * 
     * @param program
     * @param event
     * @return
     */
    @Override
    public Set<EventSignal> initializeEvent(Program program, Event event,
            UtilityDREvent utilityDREvent) {
    	Set<EventSignal> programSignals = new HashSet<EventSignal>();
    	
        SCERTPEventRateInfo rateInfo = new SCERTPEventRateInfo();
        
        SCERTPEventWeatherInfo winfo = getDayBeforeWeather(event.getStartTime());
        
        rateInfo.setEventWeatherInfo(winfo);
        
        String seasonName = null;
		try {
			seasonName = getSeason(program.getProgramName(), event.getStartTime());
		} catch (NotConfiguredException e) {
			e.printStackTrace();
		}
    	String strategyName = null;
		try {
			strategyName = getRTPConfigName(program.getProgramName(), seasonName, winfo.getWeatherRecord().getForecastHigh0());
		} catch (AppServiceException e) {
			e.printStackTrace();
		}
		rateInfo.setTemperatureRange(strategyName);
        
        // This can return a set of prices based on forecast high
        setEventWeather(event, rateInfo);
        
      

        // Special case: If there is no high temp reporting station, the
        // temperature was a forecast and prices cannot be used for a real event
        if (rateInfo.getEventWeatherInfo().getWeatherRecord().getReportingStation() == null) {
            SCERTPEvent sceEvent = (SCERTPEvent) event;
            sceEvent.setHighTemperature(null);
            sceEvent.setReportingWeatherStation(null);
            sceEvent.setTemperatureRange(null);
            log.info(LogUtils.createLogEntry(program.getProgramName(),
            LogUtils.CATAGORY_EVENT, "SCE RTP event created with no price information due to unavailability of either primary or secondary high temperature", null));
        }

        return programSignals;
    }
    
    
    /**
     * Need to be override
     * This method looks up program signals, generates a set of event signals
     * for one event, and passes the combination of those through client rules
     * to populate per-client signals for each participating client
     * 
     * It sets up all the levels of signals for an event. All programs will call
     * this at event creation time, and some programs may call it later to allow
     * for event signal revision (For example, for RTP programs where prices can
     * be revised during the event)
     * 
     * @param programWithPPAndPRules
     *            program
     * @param event
     *            event
     * @param utilityDREvent
     *            DocMe!
     * 
     * @throws ProgramValidationException
     *             validation message
     */
    protected void processRulesAndSignals(Program programWithPPAndPRules, Event event,
            UtilityDREvent utilityDREvent, Date now1)
            throws ProgramValidationException {
        try {

	        SignalDef pendingSignalDef = signalManager.getSignal("pending"); // no different with original pending mode
	        SignalDef modeSignalDef = signalManager.getSignal("mode");
	        
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(event.getStartTime());
	          calendar.add(Calendar.DATE, -1);

	        Weather weather = weatherEAO.getWeatherByDate(calendar.getTime());
            
            if(weather==null){
            	throw new ProgramValidationException("Can't obtain weather info for :"+calendar.getTime(),null);
            }
    	    
    	    String seasonName = null;
			try {
				seasonName = getSeason(programWithPPAndPRules.getProgramName(), event.getStartTime());
			} catch (NotConfiguredException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				String strategyName = null;
				try {
					strategyName = getRTPConfigName(programWithPPAndPRules.getProgramName(), seasonName, weather.getForecastHigh0());
				} catch (AppServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

	        for (EventParticipant eventParticipant : event
	                    .getEventParticipants()) {
	                if (!eventParticipant.getParticipant().isClient()) {
	
	                    processParticipantRulesAndSignals(programWithPPAndPRules, event,
	                            utilityDREvent, eventParticipant,
	                            now1,
	                            pendingSignalDef, modeSignalDef,strategyName);
	                    
	                }
	            }
	        } catch (Exception e) {
	            String message = "can't process rules for event:" + event;
	            log.error(message, e);
	            throw new EJBException(message, e);
	        }
    }
    
    
    // override 
    private void processParticipantRulesAndSignals(Program programWithPPAndPRules, Event event,
            UtilityDREvent utilityDREvent, EventParticipant eventParticipant,
            Date now,
            SignalDef pendingSignalDef, SignalDef modeSignalDef,String strategyName)
            throws ProgramValidationException 
    {

        if(pendingSignalDef == null)
        {
              pendingSignalDef = signalManager.getSignal("pending");
        }
        if(modeSignalDef == null)
        {
        	modeSignalDef = signalManager.getSignal("mode");
        }
        
        //currently inputSignals is empty

        for (EventParticipant clientParticipant : event.getEventParticipants()) {
            if (clientParticipant.getParticipant().isClient()
                    && clientParticipant
                            .getParticipant()
                            .getParent()
                            .equals(eventParticipant.getParticipant()
                                    .getParticipantName())) {
                Set<EventParticipantSignal> updatedOutputSignals = processClientRulesAndSignals(
                		programWithPPAndPRules, event, utilityDREvent, eventParticipant,
                        clientParticipant, now,
                        pendingSignalDef, modeSignalDef,strategyName);

                for (EventParticipantSignal esig : updatedOutputSignals) {
                    esig.setEventParticipant(clientParticipant);
                }

                if (clientParticipant.getSignals() == null) {
                    clientParticipant.setSignals(new HashSet<EventParticipantSignal>());
                } else {
                    clientParticipant.incrementEventModNumber();
                }
                setParticipantSignals(clientParticipant.getSignals(), updatedOutputSignals);


                /**
                 * If this is a push client, then push the event state now
                 */
                Participant theClient = clientParticipant.getParticipant();
                if (theClient.getPush() != 0 && 
                        event.getEventStatus() != EventStatus.NONE &&
                        event.getEventStatus() != EventStatus.RECEIVED) {
                   clientManager.pushClientEventState(theClient);
                }
            }
        }
    }
    
    private Set<EventParticipantSignal> processClientRulesAndSignals(
            Program programWithPPAndPRules, Event event, UtilityDREvent utilityDREvent,
            EventParticipant eventParticipant,
            EventParticipant clientParticipant,
            Date now,
            SignalDef pendingSignalDef, SignalDef modeSignalDef,String strategyName)
            throws ProgramValidationException {


    	List<EventParticipantRule> eventClientRules = new ArrayList<EventParticipantRule>();
        // merge in the new output signals (only those entries in the future)
        Set<EventParticipantSignal> newClientOuputSignals = getClientOutputSignals(clientParticipant,
        		programWithPPAndPRules, event, now,
                pendingSignalDef, modeSignalDef,strategyName);

        return updateOutputSignals(clientParticipant.getSignals(),
                newClientOuputSignals, now);
    }
    
    
    /**
     * Evaluate the rules and generate the signals.
     * 
     * @param program
     *            the program
     * @param participantRules
     *            the participant rules map
     * @throws ProgramValidationException
     *             the exception
     */
    private Set<EventParticipantSignal> getClientOutputSignals(EventParticipant clientParticipant,Program program,
            EventTiming eventTiming,
            Date now,
            SignalDef pendingSignalDef, SignalDef modeSignalDef,String strategyName)
            throws ProgramValidationException {
        Set<EventParticipantSignal> clientOutputSignals = new HashSet<EventParticipantSignal>();

        EventParticipantSignal pendingSignal = new EventParticipantSignal();
        pendingSignal.setSignalDef(pendingSignalDef);
        pendingSignal.setSignalEntries(getPendingSignalEntries(program,
                eventTiming, now));
        clientOutputSignals.add(pendingSignal);

        EventParticipantSignal modeSignal = new EventParticipantSignal();
        modeSignal.setSignalDef(modeSignalDef);
        modeSignal.setSignalEntries(generateModeSignalEntries(clientParticipant,program, eventTiming,
                 now,strategyName));
        clientOutputSignals.add(modeSignal);

        return clientOutputSignals;
    }
    
    private Set<EventParticipantSignalEntry> generateModeSignalEntries(EventParticipant clientParticipant,
            Program program, EventTiming event,
//            List<EventParticipantRule> participantRules,
            Date now,String strategyName)
            throws ProgramValidationException {
    	if(event.getEndTime()==null){
    		throw new ProgramValidationException("Event endTime should not be null.", new Exception());
    	}
    	
        Set<EventParticipantSignalEntry> modeEntries = new HashSet<EventParticipantSignalEntry>();

        List<Date> transitionList = new ArrayList<Date>();
        
        Date startTime;
        if (now.before(event.getStartTime())) {
        	startTime = event.getStartTime();
        } else {
        	startTime = now;
        }
        
        transitionList.add(startTime);
        
        while(true){
        	Calendar start =  Calendar.getInstance();
        	start.setTime(startTime);
        	start.add(Calendar.HOUR, 1);
        	start.set(Calendar.MINUTE, 0);
        	start.set(Calendar.SECOND, 0);
        	start.set(Calendar.MILLISECOND, 0);
        	
        	startTime = start.getTime();
        	if(startTime.before(event.getEndTime())){
        		transitionList.add(startTime);
        	}else{
        		break;
        	}
        }
//        EventParticipantSignalEntry lastSignalEntry = null;
        ProgramParticipant pp = userEAO.findRtpStrategyByProgAndPartiForClient(program.getProgramName(), clientParticipant.getParticipant().getParticipantName(), Boolean.TRUE);
        // for each each transition, evaluate the rules
        for (Date transition : transitionList) {
            // only consider transitions in the future and during the event
            // times
                // all signals must be present before call to rule engine
//        	    Participant client = paticipantManager.getParticipantOnly(clientParticipant.getParticipant().getParticipantName(), true);
        	    Set<ParticipantRtpStrategy>  entries = pp.getRtpStrateges();
        	    Map<String,ParticipantRtpStrategy> strategies = new HashMap<String,ParticipantRtpStrategy>();
                for(ParticipantRtpStrategy entry :entries){
                	strategies.put(entry.getName(), entry);
                }
             
                ParticipantRtpStrategy strategy = strategies.get(strategyName);
                EventParticipantSignalEntry signalEntry = evaluateParticipantEventRules(strategy,transition);
                // only record if the level changes (or rule number taken out
                // for now)
                // TODO: look backwards for an already existing signal entry
                // with the same value
//                if (signalEntry != null
//                        && (lastSignalEntry == null || !signalEntry
//                                .getLevelValue().equals(
//                                        lastSignalEntry.getLevelValue())
//                        )) {
                    modeEntries.add(signalEntry);
//                    lastSignalEntry = signalEntry;
//                }
                
        }

        return modeEntries;
    }
    
    private EventParticipantSignalEntry evaluateParticipantEventRules(ParticipantRtpStrategy strategy,
            Date date) {
        long dateMS = date.getTime();
        EventParticipantSignalEntry signalEntry = new EventParticipantSignalEntry();
        signalEntry.setTime(date);

        signalEntry.setRuleId(-1);
        signalEntry.setLevelValue(getMode(strategy,date));

        return signalEntry;
    }
    
    
    private String getMode(ParticipantRtpStrategy strategy, Date time)
	{
    	int index = 0;
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(time);
    	index = cal.get(Calendar.HOUR_OF_DAY);
		Class<?> c = strategy.getClass();

	    Field field = null;
	    String value = null;
		try {
			field = c.getDeclaredField("value"+index);
			field.setAccessible(true);
			value = (String) field.get(strategy);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	    
		return value.toLowerCase();
	}
    
//FIXME: added by Frank

	@Override
	public String getRTPConfigName(String programName, String seasonName,
			double temperature) throws AppServiceException {
		String name=null;
		try {
			Program program=programManager.getProgramOnly(programName);
			name=rtpConfigEAO.getRTPConfigName(program.getUUID(), seasonName, temperature);
		}catch(Exception ex){
			String message = "could not get RTP config name. program name: "
				+ programName + ", season name: " + seasonName
				+ ", temperature: " + temperature;
			throw new AppServiceException(message, ex);
		}
		return name;
	}
	
	@Override
    public void generateDefaultStrategy(ProgramParticipant client){
		
		Set<ParticipantRtpStrategy> entries = createDefaultStrategy(client);
		if(entries!=null&&(!entries.isEmpty())){
			client.setRtpStrateges(entries);
			try {
				userEAO.update(client);
			} catch (EntityNotFoundException e) {
				log.error("Update strategy failure", e);
				e.printStackTrace();
			}
		}
    	
    }
	
	private static final String DEFAULT_VALUE = "Normal";
	private Set<ParticipantRtpStrategy> createDefaultStrategy(ProgramParticipant client) {
        
		Set<ParticipantRtpStrategy> entries = new HashSet<ParticipantRtpStrategy>();
		
		ParticipantRtpStrategy entry0 = new ParticipantRtpStrategy();
		entry0.setName("EXTREMELY HOT SUMMER WEEKDAY");
		entry0.setProgramParticipant(client);
		entries.add(entry0);
		
		ParticipantRtpStrategy entry1 = new ParticipantRtpStrategy();
		entry1.setName("VERY HOT SUMMER WEEKDAY");
		entry1.setProgramParticipant(client);
		entries.add(entry1);
		
		ParticipantRtpStrategy entry2 = new ParticipantRtpStrategy();
		entry2.setName("HOT SUMMER WEEKDAY");
		entry2.setProgramParticipant(client);
		entries.add( entry2);
		
		ParticipantRtpStrategy entry3 = new ParticipantRtpStrategy();
		entry3.setName("MODERATE SUMMER WEEKDAY");
		entry3.setProgramParticipant(client);
		entries.add(entry3);
		
		ParticipantRtpStrategy entry4 = new ParticipantRtpStrategy();
		entry4.setName("MILD SUMMER WEEKDAY");
		entry4.setProgramParticipant(client);
		entries.add(entry4);
		
		ParticipantRtpStrategy entry5 = new ParticipantRtpStrategy();
		entry5.setName("HIGH COST WINTER WEEKDAY");
		entry5.setProgramParticipant(client);
		entries.add( entry5);
		
		ParticipantRtpStrategy entry6 = new ParticipantRtpStrategy();
		entry6.setName("LOW COST WINTER WEEKDAY");
		entry6.setProgramParticipant(client);
		entries.add(entry6);
		
		ParticipantRtpStrategy entry7 = new ParticipantRtpStrategy();
		entry7.setName("HIGH COST WEEKEND");
		entry7.setProgramParticipant(client);
		entries.add( entry7);
		
		ParticipantRtpStrategy entry8 = new ParticipantRtpStrategy();
		entry8.setName("LOW COST WEEKEND");
		entry8.setProgramParticipant(client);
		entries.add(entry8);
		
		for(ParticipantRtpStrategy entry:entries){
			
			entry.setValue0(DEFAULT_VALUE);
			entry.setValue1(DEFAULT_VALUE);
			entry.setValue2(DEFAULT_VALUE);
			entry.setValue3(DEFAULT_VALUE);
			entry.setValue4(DEFAULT_VALUE);
			entry.setValue5(DEFAULT_VALUE);
			entry.setValue6(DEFAULT_VALUE);
			entry.setValue7(DEFAULT_VALUE);
			entry.setValue8(DEFAULT_VALUE);
			entry.setValue9(DEFAULT_VALUE);
			entry.setValue10(DEFAULT_VALUE);
			entry.setValue11(DEFAULT_VALUE);
			entry.setValue12(DEFAULT_VALUE);
			entry.setValue13(DEFAULT_VALUE);
			entry.setValue14(DEFAULT_VALUE);
			entry.setValue15(DEFAULT_VALUE);
			entry.setValue16(DEFAULT_VALUE);
			entry.setValue17(DEFAULT_VALUE);
			entry.setValue18(DEFAULT_VALUE);
			entry.setValue19(DEFAULT_VALUE);
			entry.setValue20(DEFAULT_VALUE);
			entry.setValue21(DEFAULT_VALUE);
			entry.setValue22(DEFAULT_VALUE);
			entry.setValue23(DEFAULT_VALUE);						
			
		}
		
		return entries;
	}
	
	  /**
     * Called by a regular timer
     * This method checks to see if there are any current RTP events
     * If there are, then the events are inspected to see if any
     * revision of the day-before high temperature has caused
     * the prices to change
     * 
     * @param programName program name
     */
    public void checkForRevisedPrices(String programName) {
    	  log.debug(LogUtils
                  .createLogEntry(
                          programName,
                          LogUtils.CATAGORY_EVENT,
                          "checkForRevisedPrices for program : "+programName,
                          null));
        for (Event e : eventEAO.findByProgramName(programName)) {
            SCERTPEvent event = (SCERTPEvent) e;
            Double highTemp = event.getHighTemperature();

            EventSignal priceProgramSignal = new EventSignal();
            priceProgramSignal.setSignalDef(signalManager.getSignal(MODE_SIGNAL_NAME));
            Set<EventSignalEntry> newPrices = new HashSet<EventSignalEntry>();
            try {

                SCERTPEventRateInfo rateInfo = new SCERTPEventRateInfo();
                
                // this can use forecast weather if there is no reported high
                // newPrices and rateInfo are both populated
                getPriceData(programName, event, newPrices, priceProgramSignal,
                        rateInfo);

                // Check the new rate info.  We will only update using a final
                // non-forecast high
                boolean isFinalTemp = rateInfo.getEventWeatherInfo().getWeatherRecord().isIsFinal();
                Double currentHigh = rateInfo.getEventWeatherInfo().getWeatherRecord().getHigh();

                // If the new prices are based on a different final temperature
                // than the event, then we may need to revise the event
                // with a whole new set of signals
                if (isFinalTemp &&  tempsAreDifferent(currentHigh,highTemp,programName,event.getStartTime())) {
                	log.debug(LogUtils
                            .createLogEntry(
                                    programName,
                                    LogUtils.CATAGORY_EVENT,
                                    "checkForRevisedPrices for event : "+event.getEventName(),
                                    null));
                    setEventWeather(event, rateInfo);
                    event.setHighTemperature(rateInfo.getEventWeatherInfo().getHighTemp());
                    event.setReportingWeatherStation(rateInfo.getEventWeatherInfo().getWeatherRecord().getReportingStation());

                    if(updateEventSignal(event,signalManager.getSignal(MODE_SIGNAL_NAME))){
                    	 // The price signals from the revised temperature represent a change from before
                        // We need to notify participants about the new prices
                        log.info(LogUtils
                                .createLogEntry(
                                        programName,
                                        LogUtils.CATAGORY_EVENT,
                                        "Change in reported high temperature has required an event to be revised with new prices",
                                        null));

                        event.setAmended(true);
                        String opMessage = "revised with new prices";
                        sendDRASOperatorNotifications(event, opMessage);
                        sendProgramOperatorNotifications(event, opMessage);
                        // show client status and indicate that it's a revision
                        sendParticipantNotifications(event, "revised", true, true);
                    }
                }

            } catch (NotConfiguredException ex) {
                log.error("Failed to update signals for event: " + event.getEventName() + ". Continuing.");
            }
        }
    }
    
    
    private Set<EventParticipantSignal> processModeSignals(
            Program programWithPPAndPRules, Event event, 
            EventParticipant clientParticipant,
            Date now,
            SignalDef modeSignalDef,String strategyName)
            throws ProgramValidationException {

        Set<EventParticipantSignal> newClientOuputSignals = getClientModeSignals(clientParticipant,
        		programWithPPAndPRules, event, now,
                modeSignalDef,strategyName);

        return updateOutputSignals(clientParticipant.getSignals(),
                newClientOuputSignals, now);
    }
    
    /**
     * Evaluate the rules and generate the signals.
     * 
     * @param program
     *            the program
     * @param participantRules
     *            the participant rules map
     * @throws ProgramValidationException
     *             the exception
     */
    private Set<EventParticipantSignal> getClientModeSignals(EventParticipant clientParticipant,Program program,
            EventTiming eventTiming,
            Date now,
           SignalDef modeSignalDef,String strategyName)
            throws ProgramValidationException {
        Set<EventParticipantSignal> clientOutputSignals = new HashSet<EventParticipantSignal>();


        EventParticipantSignal modeSignal = new EventParticipantSignal();
        modeSignal.setSignalDef(modeSignalDef);
        modeSignal.setSignalEntries(generateModeSignalEntries(clientParticipant,program, eventTiming,
                now,strategyName));
        clientOutputSignals.add(modeSignal);

        return clientOutputSignals;
    }
    /**
     * This method is called when there are revised event signals after an event
     * has been issued. For example, if prices need to be revised (more than
     * just adding more to the list).
     * 
     * This method is not generally for use when entirely new signal entries are
     * to be appended to the end. For that, use appendEventSignals instead
     * 
     * @param event
     * @param signalName
     * 
     * @return true if signals represent a change. False if no effective change.
     */
    protected boolean updateEventSignal(Event event,
    		SignalDef modeSignalDef) {
    	
    	  Program program = programManager.getProgramWithParticipantsAndPRules(event.getProgramName());
    	  Calendar calendar = Calendar.getInstance();
          calendar.setTime(event.getStartTime());
          calendar.add(Calendar.DATE, -1);

    	  Weather weather = weatherEAO.getWeatherByDate(calendar.getTime());
	    	String seasonName = null;
			try {
				seasonName = getSeason(event.getProgramName(), event.getStartTime());
			} catch (NotConfiguredException e) {
				e.printStackTrace();
			}
			String strategyName = null;
			try {
				strategyName = getRTPConfigName(event.getProgramName(), seasonName, weather.getForecastHigh0());
			} catch (AppServiceException e) {
				e.printStackTrace();
			}
    	
        for (EventParticipant clientParticipant : event.getEventParticipants()) {
            if (clientParticipant.getParticipant().isClient()) {
                Set<EventParticipantSignal> updatedOutputSignals = null;
				try {
					updatedOutputSignals = processModeSignals(
							program,  event, 
					     clientParticipant,
					     new Date(),
					     signalManager.getSignal(MODE_SIGNAL_NAME), strategyName);
				} catch (ProgramValidationException e) {
					e.printStackTrace();
				}
    	

                for (EventParticipantSignal esig : updatedOutputSignals) {
                    esig.setEventParticipant(clientParticipant);
                }

                if (clientParticipant.getSignals() == null) {
                    clientParticipant.setSignals(new HashSet<EventParticipantSignal>());
                } else {
                    clientParticipant.incrementEventModNumber();
                }
                setParticipantSignals(clientParticipant.getSignals(), updatedOutputSignals);


                /**
                 * If this is a push client, then push the event state now
                 */
                Participant theClient = clientParticipant.getParticipant();
                if (theClient.getPush() != 0 && 
                        event.getEventStatus() != EventStatus.NONE &&
                        event.getEventStatus() != EventStatus.RECEIVED) {
                   clientManager.pushClientEventState(theClient);
                }
            }
        }
    	
        return true;
    }
    
    private void setEventWeather(Event event, 
            SCERTPEventRateInfo rateInfo) 
    {
        if (event instanceof SCERTPEvent) {
            // Stash the rate/weather info in the event
            SCERTPEvent sceEvent = (SCERTPEvent) event;
            Weather weather = rateInfo.getEventWeatherInfo().getWeatherRecord();
            sceEvent.setReportingWeatherStation(weather.getReportingStation());
            if (weather.getHigh() == null) {
                // there's no high. The event will have no prices
                sceEvent.setReportingWeatherStation(null);
            }
            sceEvent.setHighTemperature(weather.getForecastHigh0());  // returns the real high if there is one
            sceEvent.setTemperatureRange(rateInfo.getTemperatureRange());

            sceEvent.setAmended(false);
        }
    }
    
    @Override
	protected EventEmailFormatter getMailFactoryInstance() {
		EventEmailFormatter mailFactory = new SceEventEmailFormatter("com.akuacom.pss2.core.SceEmailResourceUtil");
		return mailFactory;
	}

}
