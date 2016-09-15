package com.akuacom.pss2.cache;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openadr.dras.eventstate.EventState;
import org.openadr.dras.eventstate.SimpleClientEventData;

public class EventStateWrapper {
	
	private Map<String,Date> nearTimeMap;

	public Map<String, Date> getNearTimeMap() {
		return nearTimeMap;
	}

	public void setNearTimeMap(Map<String, Date> nearTimeMap) {
		this.nearTimeMap = nearTimeMap;
	}

	public EventStateWrapper(List<EventState> eventState, long timeToDiscard, Map<String,Date> nearTimeMap) {
		super();
		this.eventState = eventState;
		this.nearTimeMap = nearTimeMap;
		if(timeToDiscard == 0){
			this.timeToDiscard = System.currentTimeMillis() + DIFAULT_TTL;
		}else{
			this.timeToDiscard = timeToDiscard;
		}
		
	}

	private static final long GRACE_TIME = 1;
	
	// One hour
	private static final long DIFAULT_TTL = 60 * 60 * 1000;
	
	
	private List<org.openadr.dras.eventstate.EventState> eventState;
	
	private long timeToDiscard = 0;
	
	public List<org.openadr.dras.eventstate.EventState> getEventState() {
		return eventState;
	}

	public void setEventState(
			List<org.openadr.dras.eventstate.EventState> eventState) {
		this.eventState = eventState;
	}

	public long getTimeToDiscard() {
		return timeToDiscard;
	}

	public void setTimeToDiscard(long timeToDiscard) {
		this.timeToDiscard = timeToDiscard;
	}

	
	public boolean isTimeToDiscard(){
		long currentTime = System.currentTimeMillis();
		if(currentTime > (timeToDiscard - GRACE_TIME)){
			return true;
		}
		return false;
		
	}
	
	public String getEventStateValue(){
		String result = null;
		
		if(eventState != null && !eventState.isEmpty()){
			org.openadr.dras.eventstate.EventState es = eventState.get(0);
			if(es != null){
				SimpleClientEventData  simpleClientEventData =  es.getSimpleDRModeData();
				if(simpleClientEventData != null){
					result = simpleClientEventData.getEventStatus();
				}
			}
		}
		return result;
	}

	public String getOperationModeValue(){
		String result = null;
		
		if(eventState != null && !eventState.isEmpty()){
			org.openadr.dras.eventstate.EventState es = eventState.get(0);
			if(es != null){
				SimpleClientEventData  simpleClientEventData =  es.getSimpleDRModeData();
				if(simpleClientEventData != null){
					result = simpleClientEventData.getOperationModeValue();
				}
			}
		}
		return result;
	}
	

}
