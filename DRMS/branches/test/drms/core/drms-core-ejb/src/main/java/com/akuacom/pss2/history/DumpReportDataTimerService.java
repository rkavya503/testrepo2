package com.akuacom.pss2.history;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface DumpReportDataTimerService extends TimerManager {
	@Remote
	public interface R extends DumpReportDataTimerService {
	}

	@Local
	public interface L extends DumpReportDataTimerService {

	}

	void scheduleTimer();
	void invokeTimer();
}
