/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.program.scertp;

import java.text.DateFormat;
import java.util.Calendar;

/**
 *
 * @author spierson
 */
public class DailyWeatherSummary {

    private Calendar weatherDate = null;
    private String primaryStation = null;
    private String secondaryStation = null;
    private Double primaryFinalYesterdayHigh = null;
    private Double secondaryFinalYesterdayHigh = null;
    private Double primaryLateAfternoonHigh = null;
    private Double secondaryLateAfternoonHigh = null;
    private Integer primaryMiddayHigh = null;
    private Integer secondaryMiddayHigh = null;


    /**
     * @return the weatherDate
     */
    public Calendar getWeatherDate() {
        return weatherDate;
    }

    /**
     * @param weatherDate the weatherDate to set
     */
    public void setWeatherDate(Calendar weatherDate) {
        this.weatherDate = weatherDate;
    }

    /**
     * @return the primaryStation
     */
    public String getPrimaryStation() {
        return primaryStation;
    }

    /**
     * @param primaryStation the primaryStation to set
     */
    public void setPrimaryStation(String primaryStation) {
        this.primaryStation = primaryStation;
    }

    /**
     * @return the secondaryStation
     */
    public String getSecondaryStation() {
        return secondaryStation;
    }

    /**
     * @param secondaryStation the secondaryStation to set
     */
    public void setSecondaryStation(String secondaryStation) {
        this.secondaryStation = secondaryStation;
    }


    /**
     * @return the primaryLateAfternoonHigh
     */
    public Double getPrimaryLateAfternoonHigh() {
        return primaryLateAfternoonHigh;
    }

    /**
     * @param primaryLateAfternoonHigh the primaryLateAfternoonHigh to set
     */
    public void setPrimaryLateAfternoonHigh(Double primaryLateAfternoonHigh) {
        this.primaryLateAfternoonHigh = primaryLateAfternoonHigh;
    }

    /**
     * @return the secondaryLateAfternoonHigh
     */
    public Double getSecondaryLateAfternoonHigh() {
        return secondaryLateAfternoonHigh;
    }

    /**
     * @param secondaryLateAfternoonHigh the secondaryLateAfternoonHigh to set
     */
    public void setSecondaryLateAfternoonHigh(Double secondaryLateAfternoonHigh) {
        this.secondaryLateAfternoonHigh = secondaryLateAfternoonHigh;
    }

    /**
     * @return the primaryMiddayHigh
     */
    public Integer getPrimaryMiddayHigh() {
        return primaryMiddayHigh;
    }

    /**
     * @param primaryMiddayHigh the primaryMiddayHigh to set
     */
    public void setPrimaryMiddayHigh(Integer primaryMiddayHigh) {
        this.primaryMiddayHigh = primaryMiddayHigh;
    }

    /**
     * @return the secondaryMiddayHigh
     */
    public Integer getSecondaryMiddayHigh() {
        return secondaryMiddayHigh;
    }

    /**
     * @param secondaryMiddayHigh the secondaryMiddayHigh to set
     */
    public void setSecondaryMiddayHigh(Integer secondaryMiddayHigh) {
        this.secondaryMiddayHigh = secondaryMiddayHigh;
    }

    public String toString() {

        DateFormat df = DateFormat.getDateTimeInstance();
        DateFormat dtf = DateFormat.getDateTimeInstance();

        StringBuffer sb = new StringBuffer("DailyWeatherSummary: {\r\n");

        if (weatherDate != null) { sb.append("\t weatherDate="+df.format(weatherDate.getTime())+"\r\n"); }
        else { sb.append("\t weatherDate=null\r\n"); }

        sb.append("\t primaryStation="+primaryStation+"\r\n");
        sb.append("\t secondaryStation="+secondaryStation+"\r\n");
        sb.append("\t primaryFinalYesterdayHigh="+getPrimaryFinalYesterdayHigh()+"\r\n");
        sb.append("\t secondaryFinalYesterdayHigh="+getSecondaryFinalYesterdayHigh()+"\r\n");
        sb.append("\t primaryLateAfternoonHigh="+primaryLateAfternoonHigh+"\r\n");
        sb.append("\t secondaryLateAfternoonHigh="+secondaryLateAfternoonHigh+"\r\n");
        sb.append("\t primaryMiddayHigh="+primaryMiddayHigh+"\r\n");
        sb.append("\t secondaryMiddayHigh="+secondaryMiddayHigh+"\r\n");
        sb.append("\t}");
    
        return sb.toString();
    }

    /**
     * @return the primaryFinalYesterdayHigh
     */
    public Double getPrimaryFinalYesterdayHigh() {
        return primaryFinalYesterdayHigh;
    }

    /**
     * @param primaryFinalYesterdayHigh the primaryFinalYesterdayHigh to set
     */
    public void setPrimaryFinalYesterdayHigh(Double primaryFinalYesterdayHigh) {
        this.primaryFinalYesterdayHigh = primaryFinalYesterdayHigh;
    }

    /**
     * @return the secondaryFinaYesterdaylHigh
     */
    public Double getSecondaryFinalYesterdayHigh() {
        return secondaryFinalYesterdayHigh;
    }

    /**
     * @param secondaryFinaYesterdaylHigh the secondaryFinaYesterdaylHigh to set
     */
    public void setSecondaryFinalYesterdayHigh(Double secondaryFinaYesterdaylHigh) {
        this.secondaryFinalYesterdayHigh = secondaryFinaYesterdaylHigh;
    }
}
