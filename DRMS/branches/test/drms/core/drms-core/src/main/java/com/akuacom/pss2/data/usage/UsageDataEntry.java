
package com.akuacom.pss2.data.usage;

import com.akuacom.pss2.data.PDataEntry;

import java.util.Date;


public class UsageDataEntry extends PDataEntry {

	private static final long serialVersionUID = 1L;
	
	/** The daily logs. */
    private DailyUsage dailyUsage;

    /**
     * Instantiates a new usage log.
     */
    public UsageDataEntry() {}

    public UsageDataEntry(PDataEntry de) {
        setValue(de.getValue());
        setTime(de.getTime());
        setUUID(de.getUUID());
        setDataSet(de.getDataSet());
        setDatasource(de.getDatasource());
        setActual(de.isActual());
    }

    /**
     * Instantiates a new usage log.
     *
     * @param time the time
     * @param value the usage
     * @param dailyUsage the daily log
     */
    public UsageDataEntry(long time, Double value, DailyUsage dailyUsage) {
        setTime(new Date(time));
        this.value = value;
        this.dailyUsage = dailyUsage;
    }

    /**
     * Adds the usage.
     *
     * @param usage the usage
     */
    public void addUsage(Double usage) {
        if (value == null) {
            value = usage;
        } else if (usage != null) {
            value += usage;
        } else {
            // if both null, nothing to do, still null
        }
    }

    /**
     * Mult usage.
     *
     * @param usage the usage
     */
    public void multUsage(Double usage) {
        if (value == null) {
        	value = 0.0;
           // value = usage;  // TODO what???
        } else if (usage != null) {
            value *= usage;
        } else {
            // if both null, nothing to do, still null
        }
    }

    /**
     * Sets the usage.
     *
     * @param value the new usage
     */
    public void setValue(Double value) {
        this.value = value;
    }

    public DailyUsage getDailyLogs() {
        return dailyUsage;
    }

    public void setDailyLogs(DailyUsage dailyLogs) {
        this.dailyUsage = dailyLogs;
    }
}