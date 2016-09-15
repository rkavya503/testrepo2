/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.program.scertp;

import com.akuacom.pss2.program.scertp.entities.Weather;

/**
 *
 * @author spierson
 */
public interface ForecastTemperatureSource {
    public Weather getWeatherForecast(String programName, Weather previousTodayForecast, Weather yesterdayWeather);
}
