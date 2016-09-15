package com.akuacom.pss2.email;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface ClientOfflineNotificationManager extends TimerManager {
	
	@Remote
    public interface R extends ClientOfflineNotificationManager {}
    @Local
    public interface L extends ClientOfflineNotificationManager {}
	void invokeTimer();
	void scheduleTimer();
	public void businessLogic();
}
