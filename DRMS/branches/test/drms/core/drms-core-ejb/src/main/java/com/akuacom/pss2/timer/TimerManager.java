package com.akuacom.pss2.timer;

import java.util.Collection;

import javax.ejb.Timer;

/**
 * 
 * 
 * @author Sebastian.Johnck@aawhere.com
 *
 */
public interface TimerManager {
	String getTimersInfo();
	void createTimers();
	void cancelTimers();
	void timeoutHandler(Timer timer);
}
