package com.akuacom.pss2.drw.jsf.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.drw.jsf.KioskUIBackingBean;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.KioskEventDataModel;
import com.akuacom.pss2.drw.service.impl.KioskManagerImpl;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventValue;

public class KioskScheduledEventsGroup implements Serializable {
	
	private static final long serialVersionUID = -9044643096965113443L;
	private KioskUIBackingBean parent;
	
	List<KioskEventDataModel> events = new ArrayList<KioskEventDataModel>();
	boolean updateFlag = true;
	private boolean eventEmptyFlag = true;
	public void setEventEmptyFlag(boolean eventEmptyFlag) {
		this.eventEmptyFlag = eventEmptyFlag;
	}
	public boolean isEventEmptyFlag() {
		int size = events.size();
		if(size>0){
			eventEmptyFlag = false;
		}else{
			eventEmptyFlag = true;
		}
		return eventEmptyFlag;
	}
	/**
	 * @return the updateFlag
	 */
	public boolean isUpdateFlag() {
		updateFlag=EventCache.getInstance().isKioskScheduledEventsUpdateFlag();
		return updateFlag;
	}

	/**
	 * @param updateFlag the updateFlag to set
	 */
	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}
	/**
	 * @return the events
	 */
	public List<KioskEventDataModel> getEvents() {
		if(parent!=null){
			if(parent.isInitialFlag()){
				events = KioskManagerImpl.getInstance().getScheduledEvents();
			}else{
				events=EventCache.getInstance().getKioskScheduledEvents();
			}
		}else{
			events=EventCache.getInstance().getKioskScheduledEvents();
		}
		
		List<KioskEventDataModel> result = new ArrayList<KioskEventDataModel>();
		for(KioskEventDataModel event:events){
			if(DRWUtil.filterScheduleEvent(event.getEvent())){
				result.add(event);
			}
		}
		events=result;
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<KioskEventDataModel> events) {
		this.events = events;
	}
	

	/**
	 * @return the parent
	 */
	public KioskUIBackingBean getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(KioskUIBackingBean parent) {
		this.parent = parent;
	}
}
