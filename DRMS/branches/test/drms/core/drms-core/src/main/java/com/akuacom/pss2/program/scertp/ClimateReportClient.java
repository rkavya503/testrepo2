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
import java.io.InputStreamReader;
import java.net.URL;

import java.net.URLConnection;
import java.util.Calendar;


import org.apache.log4j.Logger;

import com.akuacom.pss2.util.LogUtils;
import java.util.Locale;

/**
 * The Class NOAARestClient.
 */
public class ClimateReportClient implements HighTemperatureSource {

    /** The Constant log. */
    private static final String LOCATION_SUBST = "[location]";
    private static final String BASE_REPORT_URL = "http://www.weather.gov/climate/getclimate.php?wfo=lox&sid="+LOCATION_SUBST+"&pil=CLI";
    private static final String INVALID_REPORT_ERROR = "Unable to parse NOAA climate report";

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
    public DailyWeatherSummary getWeather(
            String programName,
            String primaryStationID,
            String primaryStationName,
            String secondaryStationID,
            String secondaryStationName) throws NOAARestClientException {


        DailyWeatherSummary summary = new DailyWeatherSummary();
        summary.setPrimaryStation(primaryStationID);
        summary.setSecondaryStation(secondaryStationID);
        try {
            try {
                todayYesterdayTemps primaryTemps = getTeletypeReport(programName, primaryStationID, primaryStationName);
                summary.setPrimaryLateAfternoonHigh(primaryTemps.todayTemp);
                summary.setPrimaryFinalYesterdayHigh(primaryTemps.yesterdayTemp);
            } catch (NOAARestClientException nex) {       }
           
            try {
                todayYesterdayTemps secondaryTemps = getTeletypeReport(programName, secondaryStationID, secondaryStationName);
                summary.setSecondaryLateAfternoonHigh(secondaryTemps.todayTemp);
                summary.setSecondaryFinalYesterdayHigh(secondaryTemps.yesterdayTemp);
            } catch (NOAARestClientException nex) {       }
            
            summary.setWeatherDate(Calendar.getInstance());
        } catch (IOException e) {
            throw new NOAARestClientException("error accessing todays weather" + e.getMessage());
        }


        return summary;
    }

    private class todayYesterdayTemps {

        public Double todayTemp = null;
        public Double yesterdayTemp = null;
        public String toString() {
            return "TodayYesterdayTemps   Today: "+todayTemp+"   Yesterday: "+yesterdayTemp;
        }
    }

    protected todayYesterdayTemps getTeletypeReport(String programName, String stationID, String locationName) 
            throws NOAARestClientException,IOException {
        todayYesterdayTemps temps = new todayYesterdayTemps();
        if (stationID.length() == 4 && stationID.startsWith("K")) {
            stationID = stationID.substring(1);
        }

        Calendar today = Calendar.getInstance();
        String tmonth = today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase();
        Calendar yesterday = (Calendar) today.clone();
        yesterday.add(Calendar.DATE, -1);
        String ymonth = yesterday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase();

        // Go read an HTML climate report page from the national weatherservice
        String reportUrl = BASE_REPORT_URL.replace(LOCATION_SUBST, stationID);
        final URLConnection conn = new URL(reportUrl).openConnection();
        final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuffer result = new StringBuffer("");
        for (String line = in.readLine(); line != null; line = in.readLine()) {  
            result.append(line).append("\n");
        }
        String rawReport = result.toString();

        // Now we have to get rid of all HTML formatting, and find the teletype-style text report
        rawReport = rawReport.replaceAll("<(.|\n)*?>", ""); // Strip out everything between angle brackets (all tags)
        String basicHeader = "THE " + locationName + " CLIMATE SUMMARY FOR ";
        int iheadr = rawReport.indexOf(basicHeader);
        if (iheadr < 5000) {
            // That ain't right            
            throw new NOAARestClientException(INVALID_REPORT_ERROR);
        }

        rawReport = rawReport.substring(iheadr);
        rawReport = rawReport.trim();
        String[] lines = rawReport.split("\n");

        boolean foundTemperature = false;
        if (lines[0].startsWith(basicHeader + tmonth + " " + today.get(Calendar.DATE) + " " + today.get(Calendar.YEAR))) {
            if (lines[1].startsWith("VALID AS OF")) {
                if (lines[3].startsWith("CLIMATE NORMAL PERIOD")) {
                    if (lines[7].startsWith("WEATHER ITEM   OBSERVED TIME")) {
                        if (lines[8].startsWith("                VALUE   (LST)")) {
                            if (lines[11].startsWith("TEMPERATURE (F)")) {
                                if (lines[13].startsWith("  MAXIMUM ")) {
                                    try {
                                        String p1 = lines[13].substring("  MAXIMUM ".length()).trim();
                                        p1 = p1.substring(0, p1.indexOf(" ")).trim();
                                        if (p1.endsWith("R")) { // Record temp
                                            p1 = p1.substring(0, p1.length() - 1);
                                        }
                                        Integer hiTemp = Integer.valueOf(p1);
                                        if (lines[12].startsWith(" TODAY")) {
                                            foundTemperature = true;
                                            temps.todayTemp = Double.valueOf(hiTemp);
                                        }
                                    } catch (Exception ex) {
                                        // it looked good, but the number was not forthcoming
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (lines[0].startsWith(basicHeader + ymonth + " " + yesterday.get(Calendar.DATE) + " " + yesterday.get(Calendar.YEAR))) {
            if (lines[2].startsWith("CLIMATE NORMAL PERIOD")) {
                if (lines[6].startsWith("WEATHER ITEM   OBSERVED TIME")) {
                    if (lines[7].startsWith("                VALUE   (LST)")) {
                        if (lines[10].startsWith("TEMPERATURE (F)")) {
                            if (lines[12].startsWith("  MAXIMUM ")) {
                                try {
                                    String p1 = lines[12].substring("  MAXIMUM ".length()).trim();
                                    p1 = p1.substring(0, p1.indexOf(" ")).trim();
                                    if (p1.endsWith("R")) { // Record temp
                                        p1 = p1.substring(0,p1.length()-1);
                                    }
                                    Integer hiTemp = Integer.valueOf(p1);
                                    if (lines[11].startsWith(" YESTERDAY")) {
                                        // System.out.println(locationName+" Yesterday high: " + hiTemp);
                                        foundTemperature = true;                                   
                                        temps.yesterdayTemp = Double.valueOf(hiTemp);
                                    }
                                } catch (Exception ex) {
                                    // it looked good, but the number was not forthcoming
                                }                                
                            }
                        }
                    }
                }
            }
        }
        
        return temps;
    }   
    
    public static void main(String[] args) {
        ClimateReportClient rc = new ClimateReportClient();
        try {
            todayYesterdayTemps temps = rc.getTeletypeReport("TEST","CQT", "DOWNTOWN LOS ANGELES (USC) CA");
            System.out.println(temps);
            temps = rc.getTeletypeReport("TEST","LGB", "LONG BEACH AIRPORT CA");
            System.out.println(temps);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
}
