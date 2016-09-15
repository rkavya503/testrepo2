package com.akuacom.drms.test;

import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.akuacom.pss2.event.validator.EventDataType;
import com.akuacom.pss2.event.validator.Events;
import com.akuacom.pss2.event.validator.EventType;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.OperationModeValue;

/**
 * Validates various events
 * @author Sunil
 *
 */
public class EventValidator {
	/**
	 * Base time
	 */
	private long baseTime = 0;
	
	/**
	 * Validation Events
	 */
	private Events events = null;
	
	Map<TimeRange, EventType> expectedEventMap;
	
	/**
	 * Constructor 
	 * @param baseTime Base time - can be current time or any time you want to test from. The offset and resolved against this base time
	 * @param configFile - config file for validation
	 * @throws JAXBException Failure
	 * @throws SAXException Failure
	 */
	public EventValidator(Calendar baseTime, URL configFile) throws JAXBException, SAXException{
		this.baseTime = baseTime.getTimeInMillis();
		events = EventsUnmarshaller.getInstance().unmarshal(configFile);
		expectedEventMap = new HashMap<TimeRange, EventType>();
		List<EventType> eventList = events.getEvent();
		for(EventType event: eventList){
			TimeRange timeRange = new TimeRange(getResolvedMillis(event.getStart()), getResolvedMillis(event.getStop()));
			expectedEventMap.put(timeRange, event);
		}
	}
	
	/**
	 * Is Event valid
	 * @param eventStates List of Event States
	 * @param offset current offset in millis
	 * @return true if valid
	 */
	public boolean isEventValid(List<EventState> eventStates, long offset){
		boolean result = true;
		long testTime = baseTime + offset;
		for(TimeRange range: expectedEventMap.keySet()){
			if(range.isInRange(testTime)){
				result = validate(eventStates,expectedEventMap.get(range));
			}
		}
		return result;
		
	}
	
	/**
	 * Validates the EventStates against validation events
	 * @param eventStates List of EventState
	 * @param event EventType
	 * @return true if valid
	 */
	private boolean validate(List<EventState> eventStates, EventType event ){
		boolean result = true;
		if(eventStates == null){
			result = false;
		}
		if (event != null){
			if(eventStates.size() != event.getEventData().size()){
				result = false;
			}
			
			for(EventState eventState: eventStates){
				boolean flag = false;
				for(EventDataType eventData: event.getEventData()){
					if(isEventMatching(eventState, eventData)){
						flag = true;
					}
				}
				if(!flag){
					result = false;
				}
			}
		}
		return result;
	}
	
	/**
	 * Matches the events
	 * @param eventState
	 * @param eventData
	 * @return true if match 
	 */
	private boolean isEventMatching(EventState eventState, EventDataType eventData){
		boolean result = false;
		if(eventState.getEventStatus().equals(EventStatus.valueOf(eventData.getEventStatus())) && 
				eventState.getOperationModeValue().equals(OperationModeValue.valueOf(eventData.getOperationMode()))
		){
			result = true;
			
		}
		return result;
	}
	/**
	 * Gets resolved milliseconds with base applied to the provided offset
	 * @param min offset in minutes
	 * @return resolved time in millis
	 */
	private long getResolvedMillis(int min){
		return baseTime + min*60000;
	}

}
