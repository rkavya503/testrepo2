/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.program.scertp;

import java.io.Serializable;

/**
 *
 * @author spierson
 */
public class SCERTPEventRateInfo implements Serializable {
    private SCERTPEventWeatherInfo eventWeatherInfo;
    private double [] rates;
    private String temperatureRange;

    /**
     * @return the eventWeatherInfo
     */
    public SCERTPEventWeatherInfo getEventWeatherInfo() {
        return eventWeatherInfo;
    }

    /**
     * @param eventWeatherInfo the eventWeatherInfo to set
     */
    public void setEventWeatherInfo(SCERTPEventWeatherInfo eventWeatherInfo) {
        this.eventWeatherInfo = eventWeatherInfo;
    }

    /**
     * @return the rates
     */
    public double[] getRates() {
        return rates;
    }

    /**
     * @param rates the rates to set
     */
    public void setRates(double[] rates) {
        this.rates = rates;
    }

    /**
     * @return the temperatureRange
     */
    public String getTemperatureRange() {
        return temperatureRange;
}

    /**
     * @param temperatureRange the temperatureRange to set
     */
    public void setTemperatureRange(String temperatureRange) {
        this.temperatureRange = temperatureRange;
    }

}
