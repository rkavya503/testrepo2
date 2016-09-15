package com.akuacom.pss2.ge;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface GeTimerInterfaceManager extends TimerManager {
	@Remote
    public interface R extends GeTimerInterfaceManager {}

    @Local
    public interface L extends GeTimerInterfaceManager {}
    
    void timerService(Date time, int interval);
    
}
