/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.scertp.NOAARestClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.program.scertp.noaa.DataType;
import com.akuacom.pss2.program.scertp.noaa.Dwml;
import com.akuacom.pss2.program.scertp.noaa.ParametersType;
import com.akuacom.pss2.program.scertp.noaa.ParametersType.Temperature;
import com.akuacom.pss2.program.scertp.noaa.StartValidTimeType;
import com.akuacom.pss2.program.scertp.noaa.TempValType;
import com.akuacom.pss2.program.scertp.noaa.TimeLayoutElementType;
import com.akuacom.pss2.util.LogUtils;

/**
 * The Class NOAARestClient.
 */
public class NOAARestClient implements HighTemperatureSource, ForecastTemperatureSource {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(NOAARestClient.class);
    
    /** Base forecast url. */
    public static final String WEATHER_GOV_FORECAST = "http://forecast.weather.gov/MapClick.php?FcstType=dwml";
    
    public static final String LA_FORECAST_SUFFIX = "&lat=34.02000&lon=-118.28000";
    
    private static final int DEFAULT_UNSET_HIGH = -999;

    // DRMS-7582, sce disabled secondary station
    private static final boolean secondaryEnabled = false;

    /**
     * getWeatherSummary
     *
     * Given a calendar day of interest and a string full of newline delimited METAR observations
     * returns a DailyWeatherSummary of the various temperatures available for that day
     *
     * @param programName programName
     * @param reportDay reportDay
     * @param combinedRawObservations combinedRawObservations
     * @return DailyWeatherSummary
     * @throws NOAARestClientException
     */
    public DailyWeatherSummary getWeatherSummary(String programName, String primaryStation, String secondaryStation, Calendar reportDay, String combinedRawObservations) throws NOAARestClientException
    {
    	DailyWeatherSummary today = new DailyWeatherSummary();
        today.setPrimaryStation(primaryStation);
        if (secondaryEnabled) {
            today.setSecondaryStation(secondaryStation);
        }

		List<METARObservation> primaryMetarList = null;
		List<METARObservation> secondaryMetarList = null;

        // Read all of today's observations for both the primary and secondary stations
		try {
			primaryMetarList = decodeMETARs(primaryStation, reportDay, combinedRawObservations);
            if (secondaryEnabled) {
                secondaryMetarList = decodeMETARs(secondaryStation, reportDay, combinedRawObservations);
            }
		} catch (Exception e) {
			
            String msg = "error processing METAR weather";
			log.debug(LogUtils.createLogEntry(programName,LogUtils.CATAGORY_EVENT,msg,null));
			log.debug(LogUtils.createExceptionLogEntry(programName,
					LogUtils.CATAGORY_EVENT,e));
		}

        if ((primaryMetarList == null || primaryMetarList.isEmpty()) &&
                (secondaryMetarList == null || secondaryMetarList.isEmpty())) {
            // There was no temperature data at all
            throw new NOAARestClientException("NOAA temperature data unavailable");
        }        

        if (primaryMetarList != null && primaryMetarList.size() > 0) {
            today.setWeatherDate(primaryMetarList.get(0).getObservationTime());
            today.setPrimaryFinalYesterdayHigh(getFinalYesterdayHigh(primaryMetarList));
            if (getLateAfternoonSixHour(primaryMetarList) != null) {
                today.setPrimaryLateAfternoonHigh(getHighestSixHourHigh(primaryMetarList));
            }
            today.setPrimaryMiddayHigh(getHighestHourlyObservation(primaryMetarList));
        }
        if (secondaryEnabled && secondaryMetarList != null && secondaryMetarList.size() > 0) {
            if (today.getWeatherDate() == null) {
                today.setWeatherDate(secondaryMetarList.get(0).getObservationTime());
            }
            today.setSecondaryFinalYesterdayHigh(getFinalYesterdayHigh(secondaryMetarList));
            if (getLateAfternoonSixHour(secondaryMetarList) != null) {
                today.setSecondaryLateAfternoonHigh(getHighestSixHourHigh(secondaryMetarList));
            }
            today.setSecondaryMiddayHigh(getHighestHourlyObservation(secondaryMetarList));
        }
        
    	return today;
    }

    protected Double getFinalYesterdayHigh(List<METARObservation> metarList) {
        Double high = null;
        for (METARObservation metar : metarList) {
            if (metar.getPrevDayHighF() != null) {
                high = metar.getPrevDayHighF();
            }
        }
        return high;
    }

    // Return the highest six hour highs in a list of metars
    // ignores hourly temps
    protected Double getHighestSixHourHigh(List<METARObservation> metarList) {
        double high = DEFAULT_UNSET_HIGH;
        boolean foundIt = false;
        for (METARObservation metar : metarList) {
            if (metar.getSixHourHighF() != null) {
                high = (Math.max(high, metar.getSixHourHighF()));
                foundIt = true;
            }
        }
        if (!foundIt) {
            return null;
        }
        return high;
    }

    // Return the highest hourly temp in a list of metars
    // ignores six hour highs
    protected Integer getHighestHourlyObservation(List<METARObservation> metarList) {
        double high = DEFAULT_UNSET_HIGH;
        boolean foundIt = false;
        for (METARObservation metar : metarList) {
            if (metar.getCurrentTempF() != null) {
                high = (Math.max(high, metar.getCurrentTempF()));
                foundIt = true;
            }
        }
        if (!foundIt) {
            return null;
        }
        return (int)Math.round(high);
    }

    // Scans through a list of METAR reports
    // looks for a six hour high coming from a report between 3 pm and 7 pm pacific time;
    protected METARObservation getLateAfternoonSixHour(List<METARObservation> metarList)
    {
        METARObservation high = null;
        if (metarList != null) {
            for (METARObservation metar : metarList) {
                if (metar.getSixHourHighF() != null) {
                    Calendar obsTime = (Calendar) metar.getObservationTime().clone();
                    obsTime.setTimeZone(TimeZone.getTimeZone("PST"));
                    int hour = obsTime.get(Calendar.HOUR_OF_DAY);
                    if (hour >= 15 && hour < 19) high = metar;  // this is it
                }
            }
        }
        return high;
    }


    /**
     * getRawReport
     *
     * This method takes a station ID and gets a text list of METAR reports for today
     *
     * @param stationID  as in KCQT
     * @return newline delimited string of un-decoded METAR reports
     * @throws IOException
     */
    protected String getRawReport(String stationID) throws IOException {
        Calendar reportDay = Calendar.getInstance();
        int numObservations = reportDay.get(Calendar.HOUR_OF_DAY)+1;

        String urlBase = "http://aviationweather.gov/adds/metars/index.php?station_ids=";
        String rawReport = getResponseString(urlBase+stationID+"&hoursStr="+numObservations);

        Calendar utcReportDay = (Calendar)reportDay.clone();
        utcReportDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar yesterday = (Calendar)utcReportDay.clone();
        yesterday.add(Calendar.DATE, -1);

        rawReport = rawReport.replaceAll("\n","");  		// Strip out all text newlines
		rawReport = rawReport.replaceAll("<BR>", "\n");		// Replace all html breaks with newlines
		rawReport = rawReport.replaceAll("<(.|\n)*?>", ""); // Strip out everything between angle brackets (all tags)

		// There is a simple header on the page.  We need to skip to the beginning
		// of the first true METAR line.  It will begin with the station ID, then a space,
		// then the day of the month.  The day can begin with a zero.  As in"KCQT 06"
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(2);
		int startIdx = rawReport.indexOf(stationID+" "+nf.format(reportDay.get(Calendar.DATE)));// Search for the first METAR line
		if (startIdx < 0) startIdx = rawReport.indexOf(stationID+" "+nf.format(yesterday.get(Calendar.DATE)));  // might be just past midnight
		if (startIdx > 0 ) rawReport = rawReport.substring(startIdx); // Trim titles, etc to just leave METARs
        if (startIdx < 0) {
            // We haven't found anything at all that looks like the beginning of a METAR observation
            String msg = "Empty NOAA METAR report for "+stationID+" contains no observations";
			log.debug(LogUtils.createLogEntry("",this.getClass().getName(),msg,null));
            return null;
        }

		rawReport = rawReport.trim();
        return rawReport;
    }


    /**
     * decodeMETARs
     *
     * Takes a string containing raw, ugly METAR reports returns a list of decoded METARS
     *
     * Example of input text:
     *   KCQT 242347Z AUTO VRB04KT 10SM CLR 30/11 A2977 RMK AO2 SLPNO T03000106 10344 20300 56010 TSNO
     *   KCQT 242247Z AUTO VRB03KT 10SM CLR 32/10 A2977 RMK AO2 SLPNO T03170100 TSNO
     *   KCQT 242147Z AUTO 00000KT 10SM CLR 32/11 A2978 RMK AO2 SLPNO T03220111 TSNO
     *   KCQT 242047Z AUTO VRB03KT 10SM CLR 33/10 A2980 RMK AO2 SLPNO T03280100 58010 TSNO
     *   KCQT 241947Z AUTO 27005KT 10SM CLR 34/11 A2981 RMK AO2 SLPNO T03440111 TSNO
     *   KCQT 241847Z AUTO VRB03KT 10SM CLR 33/13 A2982 RMK AO2 SLPNO T03280128 TSNO
     *   KCQT 241747Z AUTO 00000KT 10SM CLR A2983 RMK AO2 SLPNO 6//// T03220133 TSNO
     *   KCQT 241647Z AUTO 00000KT 10SM CLR A2983 RMK AO2 SLPNO T02940133 TSNO
     *   KCQT 241547Z AUTO 00000KT 10SM CLR 27/14 A2983 RMK AO2 SLPNO T02670144 TSNO
     *
     * @param stationID reporting station for which reports are desired
     * @param rawReport simple String containing a list of raw METAR reports separated by newlines
     * @return list of METARObservation for the requested station
     * @throws IOException
     */
	protected List<METARObservation> decodeMETARs(String stationID, Calendar reportDay, String rawReport) throws IOException
	{
		List<String> metarList = new ArrayList<String>();
		
		StringReader sbIn = new StringReader(rawReport);
		BufferedReader bin = new BufferedReader(sbIn);
		
		String metar;
		while ((metar = bin.readLine()) != null)
		{
			metarList.add(metar);
		}

        ArrayList<METARObservation> decodedList = new ArrayList<METARObservation>();
        // Now decode the lines into METARObservations
        // Scan all the reports for today and find the highest temp, including the six-hour reports
        for (String sMetar : metarList) {
            try {
                METARObservation obs = new METARObservation(sMetar);
                // Reject reports that are not for today or are not for the right station
                if (!obs.isNil() && stationID.equalsIgnoreCase(obs.getStationID())) {
                    Calendar obsCal = obs.getObservationTime();
                    // Observations are just before the hour.  We round it to the hour so the
                    // almost midnight observation from yestarday becomes first thing today
                    if (obsCal.get(Calendar.MINUTE) > 45 && obsCal.get(Calendar.HOUR_OF_DAY) == 23) {
                        obsCal.set(Calendar.MINUTE, 0);
                        obsCal.add(Calendar.HOUR, 1);
                        obs.setObservationTime(obsCal);
                    }
                    if (obsCal.get(Calendar.DATE) == reportDay.get(Calendar.DATE) &&
                            obsCal.get(Calendar.MONTH) == reportDay.get(Calendar.MONTH)) {
                        decodedList.add(obs);
                    }
                }
            } catch (METARObservationException mex) {
                log.debug(LogUtils.createExceptionLogEntry("", LogUtils.CATAGORY_EVENT, mex));
            }
        }
        return decodedList;
    }
    

    /**
     *
     * Current-day weather data comes from noaa aviation weather reports, called METARs
     * ("METAR" is not an acronym.  It's origins are somewhat murky, but it's based
     * on a French-language descriptive name.)
     *
     * We fetch hourly reports for what has elapsed of today, which will include
     * 6-hour high readings at 6-hour intervals.  The 6-hour high reports are
     * issued at 00:00, 06:00, 12:00 and 18:00 GMT.
     *
     * These reports that are fetched are for a reporting station in downtown Los Angeles
     */
    @Override
    public DailyWeatherSummary getWeather(
            String programName, 
            String primaryStationID, 
            String primaryStationName,
            String secondaryStationID,
            String secondaryStationName) throws NOAARestClientException {
        
        String primaryRawReport;
        String secondaryRawReport = null;
        try {
            primaryRawReport = getRawReport(primaryStationID);
            if (secondaryEnabled) {
                secondaryRawReport = getRawReport(secondaryStationID);
            }
        } catch (IOException ex) {
            throw new NOAARestClientException(ex);
        }

        DailyWeatherSummary summary;
        try {
            Calendar now = Calendar.getInstance();
            // get the current days weather, up until now
            summary = getWeatherSummary(programName, primaryStationID, secondaryStationID,
                    now, primaryRawReport + "\r\n" + (secondaryRawReport == null ? "" : secondaryRawReport));
        } catch (Exception e) {
			log.debug(LogUtils.createExceptionLogEntry(programName,LogUtils.CATAGORY_EVENT,e));
            throw new NOAARestClientException("error accessing todays weather" + e.getMessage());			
        }
        
        return summary;
    }

    /**
     * Obtains a high temperature FORECAST (predicted, not recorded)
     * for the location of the primary LA weather station
     *
     * @param programName programName
     * @return weather
     */
    public Weather getWeatherForecast(String programName) {  return getWeatherForecast(programName, null, null);  }    
    @Override
    public Weather getWeatherForecast(String programName, Weather previousTodayForecast, Weather yesterdayWeather) {
        Weather forecastWeather = new Weather();
        
        // Because five-day forecasts overlap by four days from one day to the next,
        // we can fill in temporary reporting gaps by preserving previous forecasts
        // for today or shifting in the overlapping part of forecasts from yesterday
        //
        // If all goes normally, these copied-in values will be overwritten by fresh numbers
        if (previousTodayForecast != null) {
            // Preserve existing forecasts if this update goes haywire 
            // or returns partial results (like, when todays value disappears)
            forecastWeather.setForecastHigh0(previousTodayForecast.getForecastHigh0());
            forecastWeather.setForecastHigh1(previousTodayForecast.getForecastHigh1());
            forecastWeather.setForecastHigh2(previousTodayForecast.getForecastHigh2());
            forecastWeather.setForecastHigh3(previousTodayForecast.getForecastHigh3());
            forecastWeather.setForecastHigh4(previousTodayForecast.getForecastHigh4());
            forecastWeather.setForecastHigh5(previousTodayForecast.getForecastHigh5());
        } else if (yesterdayWeather != null) {
            // We're totally unsuccessful getting any forecast so far today
            // But the forecast from yesterday can still be carried over,
            // shifted by one day
            forecastWeather.setForecastHigh0(yesterdayWeather.getForecastHigh1());
            forecastWeather.setForecastHigh1(yesterdayWeather.getForecastHigh2());
            forecastWeather.setForecastHigh2(yesterdayWeather.getForecastHigh3());
            forecastWeather.setForecastHigh3(yesterdayWeather.getForecastHigh4());
            forecastWeather.setForecastHigh4(yesterdayWeather.getForecastHigh5());
        }
                
        try {
            String endPoint = WEATHER_GOV_FORECAST+LA_FORECAST_SUFFIX;
            
            URLConnection connection = new URL(endPoint).openConnection();
            InputStream streamIn = connection.getInputStream();
            JAXBContext jc = JAXBContext.newInstance("com.akuacom.pss2.program.scertp.noaa");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Dwml WeatherServiceForecast = (Dwml) unmarshaller.unmarshal(streamIn);

            HashMap<String, TimeLayoutElementType> timeMap = new HashMap<String, TimeLayoutElementType>();
            
            List<DataType> fcastDatas = WeatherServiceForecast.getData();            
            for (DataType fcastData : fcastDatas) {            
                // The TimeLayout objects list the dates and names for sequences of temperatures
                // In the early part of the day, the first forecast high will be for that day
                // but later in the afternoon, forecast highs begin with tomorrow
                for (TimeLayoutElementType timeLay : fcastData.getTimeLayout()) {
                    timeMap.put(timeLay.getLayoutKey(), timeLay);
                }
                
                List<ParametersType> parms = fcastData.getParameters();
                for (ParametersType parm : parms) {
                    List<Temperature> temps = parm.getTemperature();
                    for (Temperature temp : temps) {
                        String tempName = temp.getName();
                        if (tempName != null && tempName.equalsIgnoreCase("Daily Maximum Temperature")) {
                            Calendar today = Calendar.getInstance();
                            Calendar tomorrow = Calendar.getInstance();
                            tomorrow.add(Calendar.DATE, 1);
                            String timeLay = temp.getTimeLayout();
                            TimeLayoutElementType timeFoo = timeMap.get(timeLay);
                            StartValidTimeType starts = (StartValidTimeType)timeFoo.getStartValidTimeAndEndValidTime().get(0);
                            GregorianCalendar gCal = starts.getValue().toGregorianCalendar();
                            int dayNum = 0;
                            if (gCal.get(Calendar.DATE) == today.get(Calendar.DATE)) {  
                                dayNum = 0; // The series of high temps begins with today
                            }
                            if (gCal.get(Calendar.DATE) == tomorrow.get(Calendar.DATE)) {  
                                dayNum = 1; // The series of high temps begins with tomorrow
                            }
                            for (TempValType tempVal : temp.getValue()) {                            
                                BigInteger bigVal = tempVal.getValue();
                                int smallVal = bigVal.intValue();
                                if (dayNum == 0) {  forecastWeather.setForecastHigh0(smallVal); }
                                if (dayNum == 1) {  forecastWeather.setForecastHigh1(smallVal); }
                                if (dayNum == 2) {  forecastWeather.setForecastHigh2(smallVal); }
                                if (dayNum == 3) {  forecastWeather.setForecastHigh3(smallVal); }
                                if (dayNum == 4) {  forecastWeather.setForecastHigh4(smallVal); }
                                if (dayNum == 5) {  forecastWeather.setForecastHigh5(smallVal); }
                                ++dayNum;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
			log.debug(LogUtils.createLogEntry(
                programName,LogUtils.CATAGORY_EVENT,"error processing primary weather forecast",null));
			log.debug(LogUtils.createExceptionLogEntry(programName,LogUtils.CATAGORY_EVENT,e));
        }
        forecastWeather.setDate( Calendar.getInstance().getTime());
        return forecastWeather;
    }



    /**
     * Gets the response string.
     * 
     * @param endPoint the end point
     * 
     * @return the response string
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String getResponseString(String endPoint) throws IOException {
        final URLConnection conn = new URL(endPoint).openConnection();
        final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder result = new StringBuilder("");
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            result.append(line).append("\n");
        }
        return result.toString();
    }
    }
