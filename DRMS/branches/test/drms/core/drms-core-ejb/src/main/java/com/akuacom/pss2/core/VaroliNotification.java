package com.akuacom.pss2.core;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.event.Event;

public interface VaroliNotification {
	@Remote
	public interface R extends VaroliNotification{}
	@Local
	public interface L extends VaroliNotification{}	
	
	public void sendVaroliParticipantNotifications(Event event, String verb);

}
