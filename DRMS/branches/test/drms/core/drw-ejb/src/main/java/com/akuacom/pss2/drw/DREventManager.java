/**
 * 
 */
package com.akuacom.pss2.drw;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.Program;

/**
 * the interface DREventManager
 * 
 */
public interface DREventManager {
    @Remote
    public interface R extends DREventManager {}
    @Local
    public interface L extends DREventManager {}

	List<Program> getAllProgram();
	
	Program getProgramByUtilityName(String utilityProgramName);
	
	List<EventDetail> getActiveEvent(String program);
	List<EventDetail> getActiveEvent(String program, List<String> rates);
	List<EventDetail> getHistoryEvent(String program, Date start, Date end);
	List<EventDetail> getHistoryEvent(String program, List<String> rates, Date start, Date end);
	
	Event createEvent(Event event);

	List<EventDetail> getHistoryEventByEnd(String program, Date end);
	
	List<EventDetail> getHistoryEventByStart(String programName, Date start);
	
	int removeEvent(List<String> eventDetailUuid);

	int updateEndTime(List<String> eventDetails, Date endTime, boolean actual);

	Event createEvent(Event event, List<Location> locations,
			Date actualEndTime, Date estimatedEndTime);
	
	List<EventDetail> getActiveEventLimited(String program, Integer limit);
	
	List<EventDetail> getHistoryEventByStartLimited(String programName, Date start, Integer limit);
	
	List<EventDetail> getHistoryEvents(String programName, boolean filterByStart, Date from, Date to, String rate, String locationNo, String locationName);
	
	List<EventDetail> getHistoryEvents(String programName, boolean filterByStart, Date from, Date to, List<String> blocks);
	
	List<EventDetail> getHistoryEvents(String programName, boolean filterByStart, Date from, Date to, List<String> blocks,List<String> products);
	List<EventDetail> getHistoryEvents(String programName, boolean filterByStart, Date from, Date to, String rate, String locationNo, String locationName,List<String> dispatchTypes);

	void cancelEvent(String eventName, Date endTime, boolean active);

	Event createEvent(String programName, String product, String eventName,
			Date startTime, Date endTime, Date issueTime, String[] slaps);

	Event createEvent(String programName, String product, String dispatchType,
			String locationNumber, String eventName, Date issueTime,
			Date startTime, Date endTime);

	List<EventDetail> getAutoDispatchEvents(String programName, String product,
			Date startTime, String dispatchType, String locationNumber,boolean actualEndTimeFlag);
	void publishLocationMessage(String programName);

	Event createEvent(String programName, String product, String dispatchType,
			String locationNumber, String eventName, Date issueTime,
			Date startTime, Date endTime, boolean isEstimated);
}
