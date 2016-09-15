/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.scertp.SCERTPProgramEJBBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.annotations.EpicMethod;
import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramPerf;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.program.scertp.entities.WeatherEAO;
import com.akuacom.pss2.season.SeasonConfig;
import com.akuacom.pss2.season.SeasonConfigEAO;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.drw.CacheNotificationMessage;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class SCERTPProgramEJBBean.
 */
@Stateless
public class SCERTPProgramEJBBean extends ProgramEJBBean implements
        SCERTPProgramEJB.R, SCERTPProgramEJB.L {
	public static final String SCERTP_WEATHER_TIMER = "SCERTP_WEATHER";
	public static final int SCERTP_TIMER_INITIAL_WAIT_MS = 5000; // 5 secs
	public static final int SCERTP_TIMER_REFRESH_INTERVAL_MS = 5 * 60 * 1000; // 5 min
    public static final String SCERTP_PRIMARY_WEATHER_STATION = "KCQT";
    public static final String SCERTP_PRIMARY_WEATHER_STATION_NAME = "DOWNTOWN LOS ANGELES (USC) CA";
    public static final String SCERTP_SECONDARY_WEATHER_STATION = "KLGB";
    public static final String SCERTP_SECONDARY_WEATHER_STATION_NAME = "LONG BEACH AIRPORT CA";

    public static final String SCERTP_EVENT_UPDATE_TIMER = "SCERTP_EVENT_UPDATE";
    public static final int SCERTP_EVENT_UPDATE_INITIAL_WAIT_MS = 10000; // 10 secs
	public static final int SCERTP_EVENT_UPDATE_REFRESH_INTERVAL_MS = 30 * 60 * 1000; // 30 min

	private static final Logger log =
		Logger.getLogger(SCERTPProgramEJBBean.class);

    // private DateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyyMMdd");

    public static final String PRICE_SIGNAL_NAME = "price";

    @EJB
    protected ProgramManager.L programManager;

    @EJB
    protected SignalManager.L signalManager;

    @EJB
    protected RTPConfigEAO.L rtpConfigEAO;
    @EJB
    protected SeasonConfigEAO.L seasonEAO;
    @EJB
    protected WeatherEAO.L weatherEAO;

    @EJB
    protected EventEAO.L eventEAO;

    @EJB
    private CorePropertyEAO.L corePropEAO;

    protected Map seasonMap = new HashMap<Date, String>();

    // DRMS-7582, sce disabled secondary station
    private static final boolean secondaryEnabled = false;

	public void setEventEAO(EventEAO.L eao) {
        eventEAO = eao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.ProgramEJBBean#createProgramObject()
     */
    @Override
    public Program createProgramObject() 
    {
        return new SCERTPProgram();
    }

    /**
	 * This method returns the price for one hour of an SCE RTP day
     * It is expensive, because it calls getRates to fetch a while day
     * 
     */
    @Override
    public double getRate(String programName, Date time) throws NotConfiguredException 
    {
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
            String season = getSeasonCached(programName, rateDay.getTime());
            List<RTPConfig> configs = rtpConfigEAO.getRTPConfigList(program.getUUID(), season, rateDay, dayBeforeHigh);

            if (configs == null || configs.isEmpty()) {
                throw new NotConfiguredException(
                        "SCE RTP rate is not configured for program: "
                                + programName + " time: " + time);
            }

            // See if there is a daylight saving time shift to be done
            boolean transitionDay = DaylightSavingTimeHelper.isDSTTransitionDay(rateDay);
            if(transitionDay && DaylightSavingTimeHelper.isDaylightSavingTime(rateDay)) {
                // Transition day from winter standard time to summer saving time
                // The two o'clock hour ends up repeating, which makes too many
                // entries.  So we remove the last one, because it wraps midnight.
                configs.remove(configs.size() - 1);               
            }
            if(!transitionDay && DaylightSavingTimeHelper.isDaylightSavingTime(rateDay)) {
                // It's daylight time, but not a transition day
                // (Fall transition day to standard time takes care of itself)
                // We need prices starting at midnight, and during DST that
                // means looking one more day into the past for temperature
                try {
                    SCERTPEventWeatherInfo winfo2 = getDayBeforeWeather(dayBeforeRateDay.getTime());
                    dayBeforeHigh = winfo2.getHighTemp();
                } catch (Exception ex) {
                    // getting the two-days ago temp failed
                    // It may be a fresh database, or the app may not have been running at that time in the past
                    // Use the yesterday temperature as a substitute
                    // This is not even a problem if the time period in question is already in the past.
                    // The most likely reason for this would be if the operator brought up a system
                    // with an empty database and then created a partial event for the current day.
                    Calendar rightNow = Calendar.getInstance();
                    if (time.after(rightNow.getTime())) {
                        log.warn(LogUtils.createLogEntry(programName, LogUtils.CATAGORY_EVENT, "Error getting DST high temperature from two days ago", ""));
                    }
                }
                season = getSeasonCached(programName, dayBeforeRateDay.getTime());
                List<RTPConfig> configs2 = rtpConfigEAO.getRTPConfigList(program.getUUID(), season, rateDay, dayBeforeHigh);
                if (configs2 == null) {
                    throw new NotConfiguredException(
                            "SCE RTP rate is not configured for program: "
                                    + programName + " time: " + time);
                }
                configs.remove(configs.size() - 1);
                configs.add(0, configs2.get(23)); // first hour is really the last hour from day before price column
            }

            double[] rates = new double[configs.size()];
            int idx = 0;
            for (RTPConfig conf : configs) {
                rates[idx++] = conf.getRate();
            }
            String tempRange = configs.get(5).getName();
            rateInfo.setRates(rates);
            rateInfo.setEventWeatherInfo(winfo);
            rateInfo.setTemperatureRange(tempRange);
        } catch (AppServiceException e) {
            throw new EJBException(e);
        }
    }

    
    protected SCERTPEventWeatherInfo getDayBeforeWeather(Date dayOf) {
        Weather weather;
        SCERTPEventWeatherInfo winfo = new SCERTPEventWeatherInfo();

        Date wantDay = DateUtil.stripTime(dayOf);
        Date nowDay = DateUtil.stripTime(new Date());

        if (nowDay.equals(wantDay) || nowDay.after(wantDay)) 
        {
            // If the prices are for today or earlier
            // So expect a real weather record for the day.
            Calendar yesterdayCal = Calendar.getInstance();
            yesterdayCal.setTime(wantDay);
            yesterdayCal.add(Calendar.DAY_OF_YEAR, -1);
            weather = weatherEAO.getWeatherByDate(yesterdayCal.getTime());
            winfo.setIsForecast(false);
            //TODO how to deal with null weather 
            if (weather!=null && weather.getHigh() == null) {
                winfo.setIsForecast(true);
            }
        } 
        else 
        {
            // If the prices are for a future day, then get
            // todays weather record and use a forecast temp
            weather = weatherEAO.getWeatherByDate(nowDay);
            winfo.setIsForecast(true);
        }
        //TODO how to deal with null weather 
        if(weather!=null){
        	winfo.setWeatherRecord(weather);
        	winfo.setHighTemp(weather.getDayBeforeForecastHigh(wantDay));
        }
        return winfo;
    }

    protected String getSeasonCached(String programName, Date date) throws NotConfiguredException {

        if (!seasonMap.containsKey(date)) {
            seasonMap.put(date, this.getSeason(programName, date));
        }

        return (String) seasonMap.get(date);
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.program.scertp.SCERTPProgramEJB#getSeason(java.lang.String, 
     *      java.util.Date)
     */
    @Override
    public String getSeason(String programName, Date date)
            throws NotConfiguredException 
    {
        try 
        {
            ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
            ProgramPerf program = programManager.getProgramPerf(programName);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) 
            {
                return SeasonConfig.WEEKEND_SEASON;
            }

            List<SeasonConfig> seasons = seasonEAO.getSeasonConfig(program.getUUID(), date);
            if (seasons != null && seasons.size() > 0) 
            {
                for (SeasonConfig sc : seasons) 
                {
                    if (sc.getName().indexOf(SeasonConfig.WEEKEND_SEASON) == 0) 
                    {
                        return SeasonConfig.WEEKEND_SEASON;
                    }
                }
                for (SeasonConfig sc : seasons) 
                {
                    if (sc.getName().indexOf(SeasonConfig.WEEKEND_SEASON) < 0) 
                    {
                        if (SeasonConfig.SUMMER_SEASON.equalsIgnoreCase(sc
                                .getName())) 
                        {
                            return SeasonConfig.SUMMER_SEASON;
                        } 
                        else if (SeasonConfig.WINTER_SEASON.equalsIgnoreCase(sc
                                .getName())) 
                        {
                            return SeasonConfig.WINTER_SEASON;
                        } 
                        else 
                        {
                            throw new EJBException(
                                    "Please check the season config. SCE RTP doesn't support season: "
                                            + sc.getName());
                        }
                    }
                }
            } 
            else 
            {
                throw new NotConfiguredException(
                        "SCE season is not configured for program: " + programName 
                        + " date: " + date);
            }
        } 
        catch (AppServiceException e) 
        {
            throw new EJBException(e);
        }
        return null;
    }

    protected boolean eventExistsTomorrow(String programName) 
    {
        BitSet daysNeeded = getEventsForTodayAndTomorrow(programName);
        return daysNeeded.get(1);
    }

    /**
     * Override of super initialize to also add timer for Weather.
     * 
     * @param programName program name
     */
    @Override
    public void initialize(String programName) 
    {
        super.initialize(programName);
        
        //create timers has been moved to its own method
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
            	log.debug(LogUtils.createLogEntry("SCERTP", SCERTPProgramEJBBean.class
        				.getName(), "timeout", ""));
                pollWeatherData();

                // let the SCE RTP programs see if a temperature change has
                // changed prices in any active events
                List<String> programs = programManager.getPrograms();
                for (String programName : programs) {
                    Program program = programManager.getProgramOnly(programName);
                    if (program.getClassName().startsWith("com.akuacom.pss2.program.scertp.SCERTPProgramEJB")) {
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

    /**
     * Called by a regular timer
     * Checks the national weatherservice observations and forecast and
     * populates a db table with daily high temperature information.
     * This high temp data is used for SCE RTP Event price calculation
     */
    @EpicMethod
	protected void pollWeatherData() {

        // Get any already-existing records for today and yesterday
        Calendar todayCal = Calendar.getInstance();
        Calendar yesterdayCal = (Calendar)todayCal.clone();
        yesterdayCal.add(Calendar.DATE, -1);
        Weather todayStoredWeather = weatherEAO.getWeatherByDate(todayCal.getTime());
        Weather yesterdayStoredWeather = weatherEAO.getWeatherByDate(yesterdayCal.getTime());

        boolean shouldPoll = true;
        if (todayStoredWeather != null && yesterdayStoredWeather != null) {
            Date modifiedTime = todayStoredWeather.getModifiedTime();
            if (modifiedTime != null) {
                long timeSinceLast = todayCal.getTimeInMillis() - todayStoredWeather.getModifiedTime().getTime();
                String highStation = todayStoredWeather.getReportingStation();
                if (highStation != null && todayStoredWeather.getHigh() != null && yesterdayStoredWeather.isIsFinal() ){
                    // We have either a primary or secondary late afternoon high for today
                    // and we have a final high for yesterday.  We might be done for the day
                    if (highStation.equals(SCERTP_PRIMARY_WEATHER_STATION)) {
                        // we've got a late afternoon primary high
                        // it doesn't get better
                        shouldPoll = false;
                    }
                    if (secondaryEnabled && highStation.equals(SCERTP_SECONDARY_WEATHER_STATION) && timeSinceLast > 20*60*1000) {
                        // we've had a secondary late afternoon high for at least 20 minutes
                        // and still no primary late afternoon high
                        // It won't get better
                        shouldPoll = false;
                    }
                }
            }
        }

        if (shouldPoll) {
            try {// get the weather from the nws
                ForecastTemperatureSource forecastGetter =  new NOAARestClient();
                HighTemperatureSource tempGetter = new ClimateReportClient();

                // DRMS-7582: For internal test, use a hidden attribute to disable primary
                // to simulate the scenario when primary station data is not available.
                String primary = SCERTP_PRIMARY_WEATHER_STATION;
                try {
                    CoreProperty property = corePropEAO.getByPropertyName("qa.sce.weather.primary.off");
                    if (property != null && property.isBooleanValue()) {
                        primary = "TEST";
                    }
                } catch (EntityNotFoundException e) {
                    // it's ok.
                }

                DailyWeatherSummary weatherSummary = tempGetter.getWeather(
                        "SCERTP", 
                        primary,
                        SCERTP_PRIMARY_WEATHER_STATION_NAME,
                        SCERTP_SECONDARY_WEATHER_STATION,
                        SCERTP_SECONDARY_WEATHER_STATION_NAME);
                
                Weather weather = forecastGetter.getWeatherForecast("SCERTP", todayStoredWeather, yesterdayStoredWeather);

                weather.setDate(weatherSummary.getWeatherDate().getTime());
                Double today1 = weatherSummary.getPrimaryLateAfternoonHigh();
                Double today2;
                if (secondaryEnabled) {
                    today2 = weatherSummary.getSecondaryLateAfternoonHigh();
                }
                weather.setIsFinal(false);
                if (today1 != null) {
                    // There is a late afternoon high from the primary station
                    weather.setHigh(today1);
                    weather.setReportingStation(weatherSummary.getPrimaryStation());
                } else if (secondaryEnabled && today2 != null) {
                    // no primary late afternoon high, but there is a secondary
                    weather.setHigh(today2);
                    weather.setReportingStation(weatherSummary.getSecondaryStation());
                }

                Weather yesterdayWeather = new Weather();
                yesterdayWeather.setDate(yesterdayCal.getTime());
                if (yesterdayStoredWeather != null) {
                    yesterdayWeather.setForecastHigh1(yesterdayStoredWeather.getForecastHigh1());
                    yesterdayWeather.setForecastHigh2(yesterdayStoredWeather.getForecastHigh2());
                    yesterdayWeather.setForecastHigh3(yesterdayStoredWeather.getForecastHigh3());
                    yesterdayWeather.setForecastHigh4(yesterdayStoredWeather.getForecastHigh4());
                    yesterdayWeather.setForecastHigh5(yesterdayStoredWeather.getForecastHigh5());
                }

                Double yesterday1 = weatherSummary.getPrimaryFinalYesterdayHigh();
                Double yesterday2;
                if (secondaryEnabled) {
                    yesterday2 = weatherSummary.getSecondaryFinalYesterdayHigh();
                }
                if (yesterday1 != null) {
                    // there is a last word final high temp for yesterday
                    // as reported by the primary station.  Normal, wonderful, and final.
                    yesterdayWeather.setHigh(yesterday1);
                    yesterdayWeather.setReportingStation(weatherSummary.getPrimaryStation());
                    yesterdayWeather.setIsFinal(true);
                    // DRMS-6947: don't switch to secondary unless it's later than 3am.
                } else if (secondaryEnabled && yesterday2 != null && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 3) {
                    // if no final primary high for yesterday, but there IS a final secondary
                    // high, AND, it's later than 3am, then the secondary becomes the high for the day
                    yesterdayWeather.setHigh(yesterday2);
                    yesterdayWeather.setReportingStation(weatherSummary.getSecondaryStation());
                    yesterdayWeather.setIsFinal(true);
                }



                if (todayStoredWeather == null) {
                    //09.16.2010 Linda: DRMS-1666 modify
                    weatherEAO.create(weather);
                    log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
                            "SCERTP Created new weather entry. now: " +
                            todayCal.getTime().toString(), weather.toString()));
                } else {
                    todayStoredWeather.update(weather);
                    weatherEAO.update(todayStoredWeather);
                    log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
                            "SCERTP Updated todays weather entry. now: " +
                            todayCal.getTime().toString(), todayStoredWeather.toString()));
                }

                if (yesterdayStoredWeather == null) {
                    weatherEAO.create(yesterdayWeather);  // has yesterdays final high
                    log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
                            "SCERTP Created new yesterday weather entry. now: " +
                            todayCal.getTime().toString(), yesterdayWeather.toString()));
                } else if (yesterdayWeather.getHigh() != null &&
                        !yesterdayStoredWeather.isIsFinal() &&  // Don't keep updating a final yesterday high.
                        yesterdayWeather.isIsFinal()) {
                    yesterdayStoredWeather.update(yesterdayWeather);
                    weatherEAO.update(yesterdayStoredWeather);
                    log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
                            "SCERTP Updated yesterday weather entry. now: " +
                            todayCal.getTime().toString(), yesterdayStoredWeather.toString()));
                }
                publicToDRwebsite("RTP");

            } catch (Exception e) {
                log.debug(LogUtils.createExceptionLogEntry("SCERTP", this.getClass().getName(), e));
            }
        }
    }

    public boolean tempsAreDifferent(Double newTemp, Double oldTemp, String programName, Date eventDate) {
    	if(oldTemp==null) return true;
    	boolean result=false;
    	try {
			String seasonName=getSeasonCached(programName, DateUtil.stripTime(eventDate));
            Program program = programManager.getProgramOnly(programName);

			String newCategory=rtpConfigEAO.getRTPConfigName(program.getUUID(), seasonName, newTemp);
			String oldCategory=rtpConfigEAO.getRTPConfigName(program.getUUID(), seasonName, oldTemp);
			
			if (newCategory!=null && oldCategory !=null && !newCategory.equals(oldCategory))
				result=true;
			
		} catch (NotConfiguredException e) {
			result=false;
		} catch (AppServiceException e) {
			result=false;
		}
    	
    	return result;
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

        for (Event e : eventEAO.findByProgramName(programName)) {
            SCERTPEvent event = (SCERTPEvent) e;
            Double highTemp = event.getHighTemperature();

            EventSignal priceProgramSignal = new EventSignal();
            priceProgramSignal.setSignalDef(signalManager.getSignal(PRICE_SIGNAL_NAME));
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
                    setEventWeather(event, rateInfo);
                    event.setHighTemperature(rateInfo.getEventWeatherInfo().getHighTemp());
                    event.setReportingWeatherStation(rateInfo.getEventWeatherInfo().getWeatherRecord().getReportingStation());

                    if (updateEventSignal(event, PRICE_SIGNAL_NAME, newPrices, null, true)) {
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

    
    /**
     * 
     * make sure you call super if you override this since it calls
     * executeSignals().
     * 
     * @param programName
     *            program name
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Collection<String> processNewSceRtpEvent(String programName,
			Date eventDay) {
		Collection<String> eventNames = new ArrayList<String>();
		Program program = programManager.getProgramOnly(programName);
		Date now = new Date();
		boolean mayNeedErrorNotification = false;
		long dayoff = (DateUtil.stripTime(eventDay).getTime() - DateUtil
				.stripTime(now).getTime()) / DateUtil.MSEC_IN_DAY;

		boolean today = (dayoff == 0);
		String notificationDetail = null;
		try {
			// check to see if the program is configured
			getRate(programName, eventDay);

		} catch (NotConfiguredException e) {
			mayNeedErrorNotification = true;
			notificationDetail = e.getMessage();
			log.error("Unable to create RTP Event for " + eventDay
					+ " because of missing configuration", e);
		}
		
		// remove exception scheduled events before create a new one if there is
		// old one
		if (!mayNeedErrorNotification && dayoff >= 1) {
			Map<String, Date> expEvents = getExceptionScheduledEvents(programName);
			for (String evtName : expEvents.keySet()) {
				Date evtDate = expEvents.get(evtName);
				if (DateUtil.stripTime(eventDay).equals(
						DateUtil.stripTime(evtDate))) {
					super.cancelEvent(programName, evtName, false);
				}
			}
		}

		if (!mayNeedErrorNotification) {
			Event event = newProgramEvent();
			event.setProgramName(programName);
			event.setEventName(EventUtil.getUniqueEventName(programName
					+ postfixOfEventName(eventDay, now)));
			if (today) {
				event.setStartTime(now);
			} else
				event.setStartTime(eventDay);

			event.setIssuedTime(now);
			event.setCreationTime(now);
			event.setReceivedTime(now);
			event.setEndTime(DateUtil.endOfDay(eventDay));

			try {
				eventNames.addAll(super.createEvent(programName, event, null));
			} catch (Exception ex) {
				log.error(LogUtils.createLogEntry(programName,
						LogUtils.CATAGORY_EVENT,
						"Failed to create RTP Event for " + eventDay, null));
				log.debug(LogUtils.createExceptionLogEntry(programName,
						LogUtils.CATAGORY_EVENT, ex));
				Throwable cause = ex.getCause();
				Throwable cause2 = cause.getCause();
				if (cause2 != null
						&& cause2 instanceof ProgramValidationException) {
					ProgramValidationException pex = (ProgramValidationException) cause2;
					List<ProgramValidationMessage> gripes = pex.getErrors();
					StringBuilder detail = new StringBuilder();
					for (ProgramValidationMessage gripe : gripes) {
						detail.append("\r\n -- ");
						detail.append(gripe.getDescription());
					}
					notificationDetail = detail.toString();
				} else {
					notificationDetail = cause.getMessage();
				}
				// send notification if can not automatically create RTP Events
				mayNeedErrorNotification = true;
			}
		}

		if (mayNeedErrorNotification && program != null) {
			if (program.getLastErrorOpContact() == null
					|| program.getLastErrorOpContact().compareTo(now) < 0) {
				// send one time notification
				String subject = "could not automatically generate events";
				String content = program.getProgramName()
						+ " could not automatically generate events for "
						+ eventDay + " " + notificationDetail;
				sendProgramOperatorNotifications(program, subject, content);

				// update flags
				program.setLastErrorOpContact(new Date());
				programManager.setProgram(program);
			}
		}
		return eventNames;
    }

	private String postfixOfEventName(Date eventDay, Date today) {
		Date pureEventDay = DateUtil.stripTime(eventDay);
		Date pureToday = DateUtil.stripTime(today);
		long offset = (pureEventDay.getTime() - pureToday.getTime())
				/ DateUtils.MILLIS_PER_DAY;
		long nameOff = offset + 1;
		return "-" + nameOff;
	}
    /**
     * This is the place to get a new, empty SCERTP Event
     * 
     * @see com.akuacom.pss2.core.ProgramEJB#newProgramEvent()
     */
    @Override
    public Event newProgramEvent() 
    {
        return new SCERTPEvent();
    }

    @Override
    public Collection<String> createEvent(String programName, Event event, UtilityDREvent utilityDREvent) {
        Collection<String> eventNames = new ArrayList<String>();
        BitSet alreadyEvent = getEventsForTodayAndTomorrow(programName);
        if (!alreadyEvent.get(0)) {
            // There's not already one for today
            // so generate an event covering the rest of today
            eventNames.addAll(processNewSceRtpEvent(programName, new Date()));
        }else{
        	if(event.isManual()){
        		throw new ValidationException("Could not generate a new event because this type of event already exists for today.");
        	}
        }

        ProgramManager pgMan = EJBFactory.getBean(ProgramManager.class);
        ProgramPerf program = pgMan.getProgramPerf(programName);
        Date autoRepeatTime = program.getAutoRepeatTimeOfDay();
        Date timeNow = new Date();
        if (autoRepeatTime == null) { autoRepeatTime = (Date) timeNow.clone(); }
        timeNow = DateUtil.stripDate(timeNow);
        autoRepeatTime = DateUtil.stripDate(autoRepeatTime);
        if (!alreadyEvent.get(1) && (autoRepeatTime != null && timeNow.after(autoRepeatTime))) {
            // There is not already an event for tomorrow,
            // There is an auto-repeat time
            // and it's past the time tomorrow event will get automatically generated.
            // So generate an event for tomorrow in addition to the ond for the rest of today
            eventNames.addAll(processNewSceRtpEvent(programName, DateUtil.getNext(new Date(), 1)));
        }
        
        return eventNames;
    }

    @Override
    protected void setEventTiming(Event event, UtilityDREvent utilityDREvent) {
        Date now = new Date();
        Calendar tomorrowCal = new GregorianCalendar();
        tomorrowCal.setTime(DateUtil.stripTime(now));

        event.setReceivedTime(now);
        event.setIssuedTime(now);

        if (event.getStartTime() == null) {
            // event is for tomorrow
            tomorrowCal.add(Calendar.DATE, 1);

            event.setStartTime(tomorrowCal.getTime());
        } else {
            event.setStartTime(now);
        }

        tomorrowCal.set(Calendar.HOUR_OF_DAY, 23);
        tomorrowCal.set(Calendar.MINUTE, 59);
        tomorrowCal.set(Calendar.SECOND, 59);
        tomorrowCal.set(Calendar.MILLISECOND, 999);
        event.setEndTime(tomorrowCal.getTime());
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

        SCERTPEventRateInfo rateInfo = new SCERTPEventRateInfo();
        // This can return a set of prices based on forecast high
        Set<EventSignal> programSignals = getPriceSignals(program, event, rateInfo);
        setEventWeather(event, rateInfo);

        // Special case: If there is no high temp reporting station, the
        // temperature was a forecast and prices cannot be used for a real event
        if (rateInfo.getEventWeatherInfo().getWeatherRecord().getReportingStation() == null) {
            programSignals = new HashSet<EventSignal>();
            EventSignal priceProgramSignal = new EventSignal();
            priceProgramSignal.setSignalDef(signalManager.getSignal(PRICE_SIGNAL_NAME));
            Set<EventSignalEntry> priceEntries = new HashSet<EventSignalEntry>();
            priceProgramSignal.setSignalEntries(priceEntries);
            programSignals.add(priceProgramSignal);            
                        SCERTPEvent sceEvent = (SCERTPEvent) event;
            sceEvent.setHighTemperature(null);
            sceEvent.setReportingWeatherStation(null);
            sceEvent.setTemperatureRange(null);
            log.info(LogUtils.createLogEntry(program.getProgramName(),
            LogUtils.CATAGORY_EVENT, "SCE RTP event created with no price information due to unavailability of either primary or secondary high temperature", null));
        }

        return programSignals;
    }

    @Override
    public Set<EventSignal> getPriceSignals(Program program, EventTiming eventTiming, 
        SCERTPEventRateInfo rateInfo) {
        // get the pending and price signals
        Set<EventSignal> programSignals = new HashSet<EventSignal>();
        EventSignal priceProgramSignal = new EventSignal();
        priceProgramSignal.setSignalDef(signalManager.getSignal(PRICE_SIGNAL_NAME));
        Set<EventSignalEntry> priceEntries = 
                new HashSet<EventSignalEntry>();

        try 
        {
            getPriceData(program.getProgramName(), eventTiming, priceEntries, priceProgramSignal, rateInfo);

        } catch (NotConfiguredException ex) {
            log.debug("SCERTPProgramEJBBean cannot generate price signals because of missing configuration", ex);
        } catch (Exception ex) {
            log.debug(LogUtils.createExceptionLogEntry( program.getProgramName(), LogUtils.CATAGORY_EVENT, ex));
        }
        priceProgramSignal.setSignalEntries(priceEntries);
        programSignals.add(priceProgramSignal);

        return programSignals;
    }

    @Override
    protected List<EventParticipantRule> getEventParticipantRules(Program program, 
        EventTiming eventTiming, ProgramParticipant programParticipant)  
    {
        try 
        {
            return SCERTPUtils.getEventParticipantRules(programParticipant,
                    getSeasonCached(program.getProgramName(), eventTiming.getStartTime()), 
                eventTiming.getStartTime());
        } 
        catch (NotConfiguredException e) 
        {
            return new ArrayList<EventParticipantRule>();
        }
    }
    
    @Override
    protected Set<EventParticipant> createEventParticipants(Event event, Program programWithParticipant) 
    {
        Set<EventParticipant> eventParticipants = event.getEventParticipants();

        Set<EventParticipant> resultParticipants = new HashSet<EventParticipant>();
        if (eventParticipants == null || eventParticipants.isEmpty()) {
            for (ProgramParticipant progPart : programWithParticipant
                    .getProgramParticipants()) {
                if (progPart == null || progPart.getParticipant() == null) {
                    continue;
                }

                if (progPart.getParticipant().isClient()) {
                    continue;
                }

                EventParticipant eventParticipant = new EventParticipant();
                eventParticipant.setEvent(event);
                eventParticipant.setParticipant(progPart.getParticipant());
                progPart.getParticipant().getEventParticipants()
                        .add(eventParticipant);
                resultParticipants.add(eventParticipant);
            }
        } else {
        	resultParticipants.addAll(super.createEventParticipants(event, programWithParticipant));
        }
        
        return resultParticipants;
    }

    /**
     * programSendsStartedNotifications 
     * (Base implementation in ProgramEJBBean)
     * 
     * Program EJB Beans may override this method to control 
     * whether or not notifications get sent when events start 
     * For example, ongoing RTP programs may not want these notifications
     */
    @Override
    protected boolean programSendsStartedNotifications(String programName) {
        return false; // SCE RTP programs should not send "started" notifications
    }

    /**
     * programSendsCompletedNotifications 
     * (Base implementation in ProgramEJBBean)
     * 
     * Program EJB Beans may override this method to control
     * whether or not notifications get sent when events complete
     * For example, ongoing RTP programs may not want these notifications
     */
    @Override
    protected boolean programSendsCompletedNotifications(String programName) {
        return false; // SCE RTP programs should not send "completed" notifications
    }

	@Override
	public void createTimers() {
		 javax.ejb.TimerService timerService = context.getTimerService();

	        // check the list of timers to see if there is a weather polling timer
	        // there should only be one, shared by all programs serviced by this ejb
	        Collection<Timer> timers = timerService.getTimers();
	        boolean found = false;
	        for (Timer timer : timers) {
	            if (SCERTP_WEATHER_TIMER.equals(timer.getInfo())) {
	                found = true;
	                break;
	            }
	        }

	        if (!found) {
	            timerService.createTimer(SCERTP_TIMER_INITIAL_WAIT_MS,
	                    SCERTP_TIMER_REFRESH_INTERVAL_MS, SCERTP_WEATHER_TIMER);
	        }
	}
	
	 @Override
	public void createTimer(String programName) {
	        super.createTimer(programName);
	        
	}

	@Override
	public String getTimersInfo() {
		Collection timersList = context.getTimerService().getTimers();
		return super.getTimersInfo(timersList);
	}
	
	@Override
	public void cancelTimers() {
		 javax.ejb.TimerService timerService = context.getTimerService();
	        Collection timersList = timerService.getTimers();
	        super.cancelTimers(timersList);
	}
	
	@Override
	protected void sendDRASOperatorNotifications(Event event, String subject) {
		if (isExceptionScheduleEvent(event))
			return;
		super.sendDRASOperatorNotifications(event, subject);
	}

	protected void sendProgramOperatorNotifications(Event event, String subject) {
		if (isExceptionScheduleEvent(event))
			return;
		super.sendProgramOperatorNotifications(event, subject);
	}

	protected void sendParticipantNotifications(Event event, String verb,
			boolean showClientStatus, boolean isRevision) {
		if (isExceptionScheduleEvent(event))
			return;
		super.sendParticipantNotifications(event, verb, showClientStatus,
				isRevision);
	}

	protected boolean isExceptionScheduleEvent(Event event) {
		// suppress notifications for exception scheduled event //DRMS-7608
		if (!event.isManual()
				&& (DateUtil.stripTime(event.getStartTime()).getTime() - DateUtil
						.stripTime(new Date()).getTime())
						/ DateUtil.MSEC_IN_DAY >= 2)
			return true;
		return false;
	}

	protected void reportEventHistory(Event event, boolean cancel, Program program){
		if (isExceptionScheduleEvent(event))
			return; //do nothing
		super.reportEventHistory(event,cancel,program);
	 }
	
	// event name, program name pairs
	protected Map<String, Date> getExceptionScheduledEvents(String programName) {
		return eventEAO.findAllExceptionScheduledRTPEvents(programName,
				DateUtil.endOfDay(new Date()));
	}

	protected boolean isEnabledMultipleRTPEvents() {
		return systemManager.getPss2Features().isEnableMultipleRTPEvents();
	}
}
