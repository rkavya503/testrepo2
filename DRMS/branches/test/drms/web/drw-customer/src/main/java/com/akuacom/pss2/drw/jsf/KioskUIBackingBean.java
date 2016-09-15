package com.akuacom.pss2.drw.jsf;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import com.akuacom.pss2.drw.jsf.event.KioskActiveEventsGroup;
import com.akuacom.pss2.drw.jsf.event.KioskScheduledEventsGroup;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.service.impl.KioskManagerImpl;

public class KioskUIBackingBean implements Serializable {
	
	private static final long serialVersionUID = -9044643096965113443L;
	
	KioskActiveEventsGroup activeEventsGroup = new KioskActiveEventsGroup();
	KioskScheduledEventsGroup scheduledEventsGroup = new KioskScheduledEventsGroup();
	
	
	static String ID_EVENTS_PANEL_ACTIVE ="activeEventsPanelGroup";
	static String ID_EVENTS_PANEL_SCHEDULED ="scheduledEventsPanelGroup";
	private String reRenderZone;
	private boolean initialFlag= false;
	/**
	 * @return the initialFlag
	 */
	public boolean isInitialFlag() {
		return initialFlag;
	}
	/**
	 * @param initialFlag the initialFlag to set
	 */
	public void setInitialFlag(boolean initialFlag) {
		this.initialFlag = initialFlag;
	}
	public KioskUIBackingBean(){
		initialFlag = true;
		activeEventsGroup.setParent(this);
		scheduledEventsGroup.setParent(this);
		KioskManagerImpl.getInstance();//Start timer for necessary
		//DRKioskTimerTask.retrieveKioskEvents();
		
		//Any time when the backing bean initialize, retrieve data immediately
//		DRDataPool.getInstance().setKioskActiveEvents(KioskManagerImpl.getInstance().getActiveEvents());
//		DRDataPool.getInstance().setKioskScheduledEvents(KioskManagerImpl.getInstance().getScheduledEvents());
		activeEventsGroup.setEvents(EventCache.getInstance().getKioskActiveEvents());
		scheduledEventsGroup.setEvents(EventCache.getInstance().getKioskScheduledEvents());

	}	

	/**
	 * @return the activeEventsGroup
	 */
	public KioskActiveEventsGroup getActiveEventsGroup() {
		return activeEventsGroup;
	}

	/**
	 * @param activeEventsGroup the activeEventsGroup to set
	 */
	public void setActiveEventsGroup(KioskActiveEventsGroup activeEventsGroup) {
		this.activeEventsGroup = activeEventsGroup;
	}

	/**
	 * @return the scheduledEventsGroup
	 */
	public KioskScheduledEventsGroup getScheduledEventsGroup() {
		return scheduledEventsGroup;
	}

	/**
	 * @param scheduledEventsGroup the scheduledEventsGroup to set
	 */
	public void setScheduledEventsGroup(
			KioskScheduledEventsGroup scheduledEventsGroup) {
		this.scheduledEventsGroup = scheduledEventsGroup;
	}
	/**
	 * @return the reRenderZone
	 */
	public String getReRenderZone() {
		if(initialFlag){
			initialFlag = false;
			reRenderZone = ID_EVENTS_PANEL_ACTIVE+", "+ID_EVENTS_PANEL_SCHEDULED;
			return reRenderZone;
		}
		
		reRenderZone = "";
		if(activeEventsGroup.isUpdateFlag()){
			reRenderZone=ID_EVENTS_PANEL_ACTIVE;
		}
		if(scheduledEventsGroup.isUpdateFlag()){
			if(reRenderZone.equalsIgnoreCase("")){
				reRenderZone=ID_EVENTS_PANEL_SCHEDULED;
			}else{
				reRenderZone=reRenderZone+", "+ID_EVENTS_PANEL_SCHEDULED;
			}
		}
		return reRenderZone;
	}
	/**
	 * @param reRenderZone the reRenderZone to set
	 */
	public void setReRenderZone(String reRenderZone) {
		this.reRenderZone = reRenderZone;
	}
	
	
	
	//Auto refresh for kiosk solution:
	//Server site:
	//1	retrieve data from server by timer, interval set is 1 minute.
	//2	compare with the current data pool data
	//3	update the compare flag(modified or not modified)
	//4	update the current data pool data
	//	if flag is true means data has been modified, when update the data
	//	else do nothing
	//Page:
	//1	auto refresh the compare flag by timer, interval set is 1 minute.
	//2	update the view 
	//	if flag is true means data has been modified, when retrieve the data pool data and refresh the view
	//	else do nothing
	
	public void refreshEvents(ActionEvent event){
		if(activeEventsGroup!=null&&scheduledEventsGroup!=null){
			activeEventsGroup.setEvents(EventCache.getInstance().getKioskActiveEvents());
			scheduledEventsGroup.setEvents(EventCache.getInstance().getKioskScheduledEvents());
		}
	}
}
