package com.akuacom.pss2.drw.service;

import java.util.List;

import com.akuacom.pss2.drw.model.KioskEventDataModel;

public interface KioskManager {
	/**
	 * Retrieve kiosk active events from system
	 * @return
	 */
	public List<KioskEventDataModel> getActiveEvents();
	
	/**
	 * Retrieve kiosk scheduled events  from system
	 * @param county
	 * @return
	 */
	public List<KioskEventDataModel> getScheduledEvents();
	
	
}
