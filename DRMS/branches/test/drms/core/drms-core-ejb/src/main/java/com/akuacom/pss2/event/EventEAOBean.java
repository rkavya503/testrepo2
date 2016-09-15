/**
 * 
 */
package com.akuacom.pss2.event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.CollapseAsMapConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ColumnAsObjectFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.MapConverter;
import com.akuacom.jdbc.SimpleListConverter;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.query.ClientSummary;
import com.akuacom.pss2.query.ParticipantClientListFor20Ob;
import com.akuacom.pss2.system.property.CoreProperty;

/**@see Event 
 * 
 * @author Aaron Roller
 * 
 */
@Stateless
public class EventEAOBean extends EventGenEAOBean implements EventEAO.R, EventEAO.L {

	@EJB
	protected Pss2SQLExecutor.L sqlExecutor;
	
	
	public EventEAOBean() {
		super(Event.class);
	}

	// TODO:These were copied from the generated object for the time
	// being...still problems with compile order.  AR 2010-10-29
	public List<Event> findByProgramName(String programName) {
		Query q = em.createNamedQuery("Event.findByProgramName");
		q.setParameter("programName", programName);
		return q.getResultList();
	}

	public List<Event> findByEventName(java.lang.String eventName) {
		Query q = em.createNamedQuery("Event.findByEventName");
		q.setParameter("eventName", eventName);
		List<Event> eList = q.getResultList();
		for(Event e: eList){
			e.getEventParticipants().size();
		}
		return eList;
	}
	
	public List<Participant> findClientsForEvent (List<String> participants, String programName ) {

		Query q = em.createNamedQuery("Participant.findClientsforEventParticipant");
		q.setParameter("programName", programName);
		q.setParameter("participantNames", participants);
		List<Participant> pList = q.getResultList();
		for(Participant e: pList){
			e.getEventParticipants().size();
		}
		return pList;		
	}
	
	public String getCorePropertyByName (String propertyName ) {

		Query q = em.createNamedQuery("CoreProperty.findCorePropertyByPropertyName");
		q.setParameter("propertyName", propertyName);
		List<CoreProperty> pList = q.getResultList();
		
		String propName = "";
		
		for(CoreProperty e: pList){
			propName = e.getStringValue().toString();
		}
		
		return propName;
	}	
	
	public Collection<Event> findByEventNamePerf(java.lang.String eventName) {
		Query q = em.createNamedQuery("Event.findByEventName");
		q.setParameter("eventName", eventName);
		Collection<Event> eList = q.getResultList();
		return eList;
	}

	
	/**Just a pass through making a single selection from the returned collection.
	 * 
	 * @see #findByEventNameProgramName(String, String)
	 *
	 * @param eventName
	 * @param programName
	 * @return the only event identified by the name or Null if nothing found.
	 * @throws EntityNotFoundException 
	 */
	@Override
	public Event getByEventAndProgramName(String eventName, String programName) throws EntityNotFoundException{
		Collection<Event> events = findByEventNameProgramName(eventName, programName);
		return super.getFirstFromCollection(events,eventName);
	}
	
	public Event getEventPerf(String eventName) throws EntityNotFoundException{
		Event event = getFirstFromCollection(findByEventNamePerf(eventName), eventName);
		return event;
	}
	
	
	@Override
	public Event getByEventName(String eventName) throws EntityNotFoundException{
        final Event event = getFirstFromCollection(findByEventName(eventName), eventName);
        // load bid block
        if (event instanceof DBPEvent) {
            DBPEvent e = (DBPEvent) event;
            e.getBidBlocks().size();
        }
/*
        final Set<EventParticipant> eventParticipantSet = event.getEventParticipants();
        for (EventParticipant ep : eventParticipantSet) {
            if (event instanceof DBPEvent && ep.getBidEntries() != null) {
                ep.getBidEntries().size();
            }
        }
*/
        return event;
    }
	
	public Event getByEventNameWithParticipants(String eventName) throws EntityNotFoundException{
        final Event event = super.getFirstFromCollection(findByEventName(eventName), eventName);
        event.getEventParticipants().size();
        if (event instanceof DBPEvent) {
            DBPEvent dbp = (DBPEvent) event;
            dbp.getBidBlocks().size();
        }
        return event;
	}
	
	public List<Event> findByEventNameProgramName(
			java.lang.String eventName, java.lang.String programName) {
		Query q = em.createNamedQuery("Event.findByEventNameProgramName");
		q.setParameter("eventName", eventName);
		q.setParameter("programName", programName);
		List<Event> eList = q.getResultList();
		for(Event e: eList){
			e.getEventParticipants().size();
		}
		return eList;
	}

	
	public List<Event> findAllPerf() {
		Query q = em.createNamedQuery("Event.findAll");
		List<Event> res = q.getResultList();
		return res;
	}

	
	public List<Event> findAll() {
		Query q = em.createNamedQuery("Event.findAll");
		List<Event> res = q.getResultList();
		
		for (Event e : res) {
			e.getEventParticipants().size();
			e.getEventSignals().size();
		}
		
		return res;
	}

	@Override
	public List<Event> findByDate(Date startTime, Date endTime) {
		Query q = em.createNamedQuery("Event.findByDate");
		q.setParameter("startTime", startTime);
		q.setParameter("endTime", endTime);
		
		return q.getResultList();
	}

	@Override
	public List<Event> findByProgramAndDate(Date startTime, Date endTime,
			String programName) {
		Query q = em.createNamedQuery("Event.findByProgramAndDate");
		q.setParameter("startTime", startTime);
		q.setParameter("endTime", endTime);
		q.setParameter("programName", programName);
		
		return q.getResultList();
	}

	@Override
	public List<Event> findByParticipantAndDate(Date startTime, Date endTime,
			List<String> participantNames) {
		Query q = em.createNamedQuery("Event.findByParticipantAndDate");
		q.setParameter("startTime", startTime);
		q.setParameter("endTime", endTime);
		q.setParameter("participantNames", participantNames);
		
		return q.getResultList();
	}

    @Override
    public List<Event> findAllPossibleByParticipant(String participantName) {
        Query q = em.createNamedQuery("Event.findAllPossibleByParticipant");
        q.setParameter("participantName", participantName);

        return q.getResultList();
    }
    
	@Override
	public List<Event> findByParticipantProgramAndDate(Date startTime, Date endTime,
			List<String> participantNames, List<String> programNames) {
		Query q = em.createNamedQuery("Event.findByParticipantProgramAndDate");
		q.setParameter("startTime", startTime);
		q.setParameter("endTime", endTime);
		q.setParameter("participantNames", participantNames);
		q.setParameter("programNames", programNames);
		
		return q.getResultList();
	}

	@Override
	public Map<String,Date> findAllExceptionScheduledRTPEvents(String programName, Date startDay) {
		String sql= "SELECT DISTINCT eventName,startTime FROM event evt "
				+"\n WHERE  evt.manual= FALSE AND evt.programName = ${programName}  AND evt.startTime> ${startDay}";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDay", startDay);
		params.put("programName", programName);
    	try {
    		Map<String,Date> names = sqlExecutor.doNativeQuery(sql, params, MapConverter.make(
    				ColumnAsObjectFactory.make(String.class, "eventName"),
    				ColumnAsObjectFactory.make(Date.class, "startTime")
    		));
    		return names;
		} catch (SQLException e) {
			return Collections.emptyMap();
		}
    }
	
	public List<Event> findByAggregatorProgramAndDate(Date startTime, Date endTime, List<String> participantNames, List<String> programNames) {
		Query q = em.createNamedQuery( "Event.findByAggregatorProgramAndDate" );
		q.setParameter("startTime", startTime);
		q.setParameter("endTime", endTime);
		q.setParameter("participantNames", participantNames);
		q.setParameter("programNames", programNames);
		return q.getResultList();
	}
	
	public Map<String,String>findEventIdAndProgramName(Set<String> eventNames){

		String sql= "SELECT DISTINCT eventName,programName FROM event evt "
				+"\n WHERE eventName in ${eventNames}";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventNames", eventNames);
    	try {
    		Map<String, String> names = sqlExecutor.doNativeQuery(sql, params, MapConverter.make(
    				ColumnAsObjectFactory.make(String.class, "eventName"),
    				ColumnAsObjectFactory.make(String.class, "programName")
    		));
    		return names;
		} catch (SQLException e) {
			return Collections.emptyMap();
		}
    
	}
	public Map<String,Event>findEventIdEventObjectMap(List<String> eventNames){
		Query q = em.createNamedQuery( "Event.findAllByEventIds" );
		q.setParameter("eventNames", eventNames);
	    List<Event> eventList = q.getResultList();
	    Map<String,Event> resultMap = new HashMap <String,Event>();
	    for (Event ev : eventList) {
	        resultMap.put(ev.getEventName(),ev);
    }
    return resultMap;
   }
}
