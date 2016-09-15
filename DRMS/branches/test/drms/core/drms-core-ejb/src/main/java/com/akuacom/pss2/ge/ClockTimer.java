package com.akuacom.pss2.ge;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface ClockTimer extends TimerManager {
	@Remote
    public interface R extends ClockTimer {}

    @Local
    public interface L extends ClockTimer {}
    
}