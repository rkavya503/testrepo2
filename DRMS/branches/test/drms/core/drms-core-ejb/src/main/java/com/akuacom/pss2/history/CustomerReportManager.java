/**
 * 
 */
package com.akuacom.pss2.history;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.program.Program;

/**
 * the interface CustomerReportManager
 * 
 */
public interface CustomerReportManager {
    @Remote
    public interface R extends CustomerReportManager {}

    @Local
    public interface L extends CustomerReportManager {}

    /**
     * record all event information including event, event participants and signals to the history tables 
     * when the {@link event} completed or is canceled
     * 
     * @param event the event
     * @param cancelled the event completed normally or is canceled by the utility operator
     */
    void reportEventHistory(Event event, boolean cancelled);
    
    void reportEventHistory(Event event, boolean cancelled, Program program);
    
    /**
     * report the participants who should but not participate the {@link eventName} 
     * 
     * @param eventName the event name
     * @param partNonParticipation the participant and corresponding participation
     * @param program the program
     */
    void reportClientNonParticipation(String eventName, Map<String, 
    		ClientParticipationStatus> partNonParticipation,
    		Program program);
    
	/**
	 * 
	 * @param client_uuid
	 * @param time
	 * @param lastContact
	 * @param clientName TODO
	 */
	void reportOnlineStatus(String client_uuid, Date time,
			Date lastContact, String clientName);

	/**
	 * 
	 * @param client_uuid
	 * @param lastContact
	 * @param clientName TODO
	 */
	void reportOfflineStatus(String client_uuid, 
			Date lastContact, String clientName);
	
	public HistoryEvent getHistoryEventByEventName(String eventName);

	void reportEventParticipant(List<EventParticipant> reportList, Event event);

	List<Event> getEventListByParticipantAndProgram(List<String> participantNames, String[] porgams, Date start, Date end);
}
