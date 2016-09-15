package com.akuacom.pss2.rtp.ftp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface RTPTemperatureUpdateTimer extends TimerManager {
    @Remote
    public interface R extends RTPTemperatureUpdateTimer {}
    @Local
    public interface L extends RTPTemperatureUpdateTimer {}

    void scheduleTimer();
}
