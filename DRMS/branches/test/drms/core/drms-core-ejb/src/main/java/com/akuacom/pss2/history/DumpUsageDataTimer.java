package com.akuacom.pss2.history;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface DumpUsageDataTimer extends TimerManager {
	@Remote
	public interface R extends DumpUsageDataTimer {
	}

	@Local
	public interface L extends DumpUsageDataTimer {

	}
	
}
