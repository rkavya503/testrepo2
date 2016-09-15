package com.akuacom.pss2.drw.jsf.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.KioskEventDataModel;
import com.akuacom.pss2.drw.util.DRWUtil;

public class BaseEventGroupBackingBean implements Serializable{

	private static final long serialVersionUID = 5078405393784390806L;
	public static final String NO_ACTIVE_EVENT_TIP="NO EVENTS IN PROGRESS";
	public static final String NO_SCHEDULE_EVENT_TIP="NO EVENTS ARE CURRENTLY SCHEDULED FOR THIS PROGRAM CLASS";
	public static final String NO_EVENT_ICON_URL="";
	
	private String programClass;
	private String eventStatus;
	private boolean isActiveEvent =false;
	private boolean isScheduleEvent = false;
	private boolean isHistoryEvent = false;
	private boolean eventEmptyFlag = true;
	private boolean minFlag=false;
	private int eventSize=0;
	
	
	private List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
	
	public String zoomOperate() {
		minFlag = !minFlag;
		return "";
	}
	
	private int scrollerPage = 1;

    public int getScrollerPage(){
        return scrollerPage;
    }

    public void setScrollerPage(int scrollerPage){
        this.scrollerPage = scrollerPage;
    }
	
	public String getProgramClass() {
		return programClass;
	}
	public void setProgramClass(String programClass) {
		this.programClass = programClass;
	}
	public String getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public boolean isActiveEvent() {
		return isActiveEvent;
	}
	public void setActiveEvent(boolean isActiveEvent) {
		this.isActiveEvent = isActiveEvent;
	}
	public boolean isScheduleEvent() {
		return isScheduleEvent;
	}
	public void setScheduleEvent(boolean isScheduleEvent) {
		this.isScheduleEvent = isScheduleEvent;
	}
	public boolean isHistoryEvent() {
		return isHistoryEvent;
	}
	public void setHistoryEvent(boolean isHistoryEvent) {
		this.isHistoryEvent = isHistoryEvent;
	}
	public List<BaseEventDataModel> getEvents() {
		return events;
	}
	public void setEvents(List<BaseEventDataModel> events) {
		this.events = events;
	}
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
	public void setMinFlag(boolean minFlag) {
		this.minFlag = minFlag;
	}
	public boolean isMinFlag() {
		return minFlag;
	}

	public void setEventSize(int eventSize) {
		this.eventSize = eventSize;
	}

	public int getEventSize() {
		if(events!=null){
			eventSize = events.size();
		}
		return eventSize;
	}
	public List<BaseEventDataModel> getScheduledEvents() {
		List<BaseEventDataModel> result = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel event:events){
			if(DRWUtil.filterScheduleEvent(event.getEvent())){
				result.add(event);
			}
		}
		return result;
	}
	public boolean isScheduledEventEmptyFlag() {
		int size = getScheduledEvents().size();
		if(size>0){
			return false;
		}else{
			return true;
		}
	}
}
