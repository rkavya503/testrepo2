package com.akuacom.pss2.history;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface BaselineAdjustTimerService extends TimerManager {
	@Remote
	public interface R extends BaselineAdjustTimerService {
	}

	@Local
	public interface L extends BaselineAdjustTimerService {

	}

	void scheduleTimer();
	void invokeTimer();
}
