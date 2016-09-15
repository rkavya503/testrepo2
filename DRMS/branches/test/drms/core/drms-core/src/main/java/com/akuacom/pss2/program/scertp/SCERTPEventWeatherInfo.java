/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.program.scertp;

import com.akuacom.pss2.program.scertp.entities.Weather;
import java.io.Serializable;

/**
 *
 * @author spierson
 */
public class SCERTPEventWeatherInfo  implements Serializable {
    private Weather weatherRecord;
    private boolean isForecast;
    private double highTemp;

    /**
     * @return the weatherRecord
     */
    public Weather getWeatherRecord() {
        return weatherRecord;
    }

    /**
     * @param weatherRecord the weatherRecord to set
     */
    public void setWeatherRecord(Weather weatherRecord) {
        this.weatherRecord = weatherRecord;
    }

    /**
     * @return the isForecast
     */
    public boolean isIsForecast() {
        return isForecast;
    }

    /**
     * @param isForecast the isForecast to set
     */
    public void setIsForecast(boolean isForecast) {
        this.isForecast = isForecast;
    }

    /**
     * @return the highTemp
     */
    public double getHighTemp() {
        return highTemp;
    }

    /**
     * @param highTemp the highTemp to set
     */
    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

}
