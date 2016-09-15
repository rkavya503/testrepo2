/**
 * 
 */
package com.akuacom.pss2.event;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantGenEAO;

/**
 * Used to access persistent {@link Event}s.
 * 
 * @author Aaron Roller
 * 
 * @see Event
 * @see EventEAOBean
 */

public interface EventEAO extends EventGenEAO {
	@Remote
	public interface R extends EventEAO {
	}

	@Local
	public interface L extends EventEAO {
    }

	List<Event> findByProgramName(String programName);

	List<Event> findByEventName(java.lang.String eventName);

	List<Event> findByEventNameProgramName(java.lang.String eventName,
			java.lang.String programName);

	Event getByEventAndProgramName(String eventName, String programName)
			throws EntityNotFoundException;

	Event getByEventName(String eventName) throws EntityNotFoundException;
	
	Event getEventPerf(String eventName) throws EntityNotFoundException;

	List<Event> findAll();
	
	List<Event> findAllPerf();

	Event getByEventNameWithParticipants(String eventName)
			throws EntityNotFoundException;
	
	List<Event> findByDate(Date startTime, Date endTime);
	
	List<Event> findByProgramAndDate(Date startTime, Date endTime, String programName);
	
	List<Event> findByParticipantAndDate(Date startTime, Date endTime, List<String> participantNames);

    List<Event> findAllPossibleByParticipant(String participantName);
    
    List<Event> findByParticipantProgramAndDate(Date startTime, Date endTime, List<String> participantNames, List<String> programNames);
    
    Map<String,Date> findAllExceptionScheduledRTPEvents(String programName,Date startDay);
    
    List<Event> findByAggregatorProgramAndDate(Date startTime, Date endTime, List<String> participantNames, List<String> programNames);
	
    List<Participant> findClientsForEvent(List<String> participants, String programName);    
    
	String getCorePropertyByName(String string);
	Map<String,String>findEventIdAndProgramName(Set<String> eventNames);
	public Map<String,Event>findEventIdEventObjectMap(List<String> eventNames);
}
