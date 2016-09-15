/*
 *
 */

package com.akuacom.pss2.program.scertp;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.pss2.event.Event;
import javax.persistence.Column;
/**
 *
 * @author spierson
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "scertp_event")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SCERTPEvent extends Event {

	private static final long serialVersionUID = 6756355368268570752L;

	private String reportingWeatherStation = "NA";
    private Double highTemperature;
    private boolean amended = false;

    @Column(name = "temp_range")
    private String temperatureRange;

    /**
     * @return the reportingWeatherStation
     */
    public String getReportingWeatherStation() {
        return reportingWeatherStation;
    }

    /**
     * @param reportingWeatherStation the reportingWeatherStation to set
     */
    public void setReportingWeatherStation(String reportingWeatherStation) {
        this.reportingWeatherStation = reportingWeatherStation;
    }

    /**
     * @return the highTemperature
     */
    public Double getHighTemperature() {
        return highTemperature;
    }

    /**
     * @param highTemperature the highTemperature to set
     */
    public void setHighTemperature(Double highTemperature) {
        this.highTemperature = highTemperature;
    }

    /**
     * @return the amended
     */
    public boolean isAmended() {
        return amended;
    }

    /**
     * @param amended the amended to set
     */
    public void setAmended(boolean amended) {
        this.amended = amended;
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
