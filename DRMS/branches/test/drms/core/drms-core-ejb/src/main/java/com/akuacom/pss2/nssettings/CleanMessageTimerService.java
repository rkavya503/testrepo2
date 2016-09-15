package com.akuacom.pss2.nssettings;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface CleanMessageTimerService extends TimerManager {
    @Remote
    public interface R extends CleanMessageTimerService {}

    @Local
    public interface L extends CleanMessageTimerService {

		}

    void scheduleTimer();
   

}
