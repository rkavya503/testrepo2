package com.akuacom.pss2.program.scertp;

import java.io.Serializable;

public class HistoricalWeatherEntry implements Serializable {
    private String date;
    private String time;
    private int high;
    private int sixHourHigh;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getSixHourHigh() {
        return sixHourHigh;
    }

    public void setSixHourHigh(int sixHourHigh) {
        this.sixHourHigh = sixHourHigh;
    }
}
