package com.akuacom.pss2.timer;

import java.util.ArrayList;
import java.util.Collection;

public class TimerManagers {
	private static final TimerManagers instance = new TimerManagers();

	private Collection<String> timerManagers = new ArrayList<String>();

	public Collection<String> getTimerManagers() {
		return timerManagers;
	}

	private TimerManagers() {
	}

	public static synchronized TimerManagers getInstance() {
		return instance;
	}
}
