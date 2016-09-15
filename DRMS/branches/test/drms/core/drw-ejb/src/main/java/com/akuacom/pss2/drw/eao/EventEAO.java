/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.EventDetail;

/**
 * the interface EventEAO
 */
public interface EventEAO extends BaseEAO<Event>{
    @Remote
    public interface R extends EventEAO {}
    @Local
    public interface L extends EventEAO {}
    
	List<EventDetail> getActiveEvent(String programName);

	List<EventDetail> getActiveEventByProduct(String programName,
			List<String> rates);

	List<EventDetail> getHistoryEvent(String programName, Date start, Date end);
	
	List<EventDetail> getHistoryEventByEnd(String programName, Date end);
	
	List<EventDetail> getHistoryEventByStart(String programName, Date start);

	List<EventDetail> getHistoryEvent(String programName, List<String> rates,
			Date start, Date end);

	int removeEvent(List<String> eventDetailUuids);

	int updateEndTime(List<String> eventDetails, Date endTime, boolean actual);
	
	List<EventDetail> getActiveEventLimited(String programName, Integer limit);
	
	List<EventDetail> getHistoryEventByStartLimited(String programName, Date start, Integer limit);

	List<Event> getEventByEventDetail(List<String> eventDetails);

	List<Event> getActiveEventByEventDetail(List<String> eventDetails);
	
	Long getActiveEventCountsByPrograms(List<String> programNames);
	
	List<EventDetail> getHistoryEvents(String programName, boolean filterByStart, Date from, Date to, String rate, String locationNo, String locationName);
	List<EventDetail> getHistoryEvents(String programName, boolean filterByStart, Date from, Date to, String rate, String locationNo, String locationName,List<String> dispatchTypes);
	
	List<EventDetail> getHistoryEvents(String programName, boolean filterByStart, Date from, Date to, List<String> blocks);
	List<EventDetail> getHistoryEvents(String programName, boolean filterByStart, Date from, Date to, List<String> blocks,List<String> products);

	List<String> getEventsByEventName(String eventName);

	List<Event> getByEventName(String eventName);

	void removeEventByEventName(String eventName);

	List<EventDetail> getAutoDispatchEvents(String programName, String product,
			Date startTime, String dispatchType, String locationNumber,boolean actualEndTimeFlag);
}
