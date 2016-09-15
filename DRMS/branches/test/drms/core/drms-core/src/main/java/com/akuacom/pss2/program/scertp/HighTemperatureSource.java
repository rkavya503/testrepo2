/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.program.scertp;

/**
 *
 * @author spierson
 */
public interface HighTemperatureSource {

    public DailyWeatherSummary getWeather(
            String programName, 
            String primaryStationID, 
            String primaryStationName,
            String secondaryStationID,
            String secondaryStationName
            ) throws NOAARestClientException;
    
}
