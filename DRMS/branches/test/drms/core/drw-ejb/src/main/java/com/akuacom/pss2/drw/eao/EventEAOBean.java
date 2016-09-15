/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;


/**
 * the class ProgramEAOBean
 */
@Stateless
public class EventEAOBean extends BaseEAOBean<Event> implements EventEAO.L, EventEAO.R {

	private static final Logger log = Logger.getLogger(EventEAOBean.class);

	public EventEAOBean() {
		super(Event.class);
	}

	@Override
	protected void checkUniqueKey(Event entity) throws DuplicatedKeyException {
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EventDetail> getActiveEvent(String programName){
		Query query = em.createNamedQuery("EventDetail.getActiveEvent");
		query.setParameter("programName", programName);
		query.setParameter("endTime", new Date());
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EventDetail> getActiveEventByProduct(String programName, List<String> products){
		Query query = em.createNamedQuery("EventDetail.getActiveEventByRate");
		query.setParameter("programName", programName);
		query.setParameter("product", products);
		query.setParameter("endTime", new Date());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventDetail> getHistoryEvent(String programName, Date start, Date end){
		Query query = em.createNamedQuery("EventDetail.getHistoryEvent");
		query.setParameter("programName", programName);
		query.setParameter("start", start);
		query.setParameter("end", end);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventDetail> getHistoryEvent(String programName, List<String> products, Date start, Date end){
		Query query = em.createNamedQuery("EventDetail.getHistoryEventByRate");
		query.setParameter("programName", programName);
		query.setParameter("product", products);
		query.setParameter("start", start);
		query.setParameter("end", end);
		return query.getResultList();
	}


	@Override
	public int updateEndTime(List<String> eventDetails, Date endTime, boolean actual){
		StringBuilder builder=new StringBuilder();
		builder.append("update EventDetail ed set ");
		if (actual)
			builder.append(" ed.actualEndTime = :endTime");
		else
			builder.append(" ed.estimatedEndTime = :endTime");
		builder.append(" where ed.UUID in (:eventDetails)");
		
		Query query=em.createQuery(builder.toString());
		query.setParameter("endTime", endTime);
		query.setParameter("eventDetails", eventDetails);
		
		int i=query.executeUpdate();
		log.info("update "+i+" rows");
		return i;
	}

	@Override
	public List<Event> getByEventName(String eventName) {
		Query query = em.createNamedQuery("Event.getByEventName");
		query.setParameter("eventName", eventName);
		return query.getResultList();
	}

	@Override	
	public void removeEventByEventName(String eventName) {
		List<Event> events=getByEventName(eventName);
		if (events!=null && events.size()>0) {
			for (Event event:events) {
				em.remove(event);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int removeEvent(List<String> eventDetailUuids){
		String select="SELECT DISTINCT ed.event.UUID FROM EventDetail ed " +
				"WHERE ed.UUID in (:uuids)";
		Query query = em.createQuery(select);
		query.setParameter("uuids", eventDetailUuids);
		List<String> events=query.getResultList();
		
		String delete="delete from EventDetail ed where ed.UUID in (:uuids)";
		query=em.createQuery(delete);
		query.setParameter("uuids", eventDetailUuids);
		int result=query.executeUpdate();
		
		log.debug("remove " + result+" rows from EventDetail");
		
		if (result>0) {
			delete="DELETE FROM Event e WHERE e.UUID in (:uuids) AND NOT EXISTS (SELECT ed FROM EventDetail ed WHERE ed.event.UUID =e.UUID)";
			query=em.createQuery(delete);
			query.setParameter("uuids", events);
			int i=query.executeUpdate();
			
			log.debug("remove " + i+" rows from Event");
		}
		
		return result;
	}

	@Override
	public List<Event> getActiveEventByEventDetail(List<String> eventDetails) {
		Query query = em.createNamedQuery("EventDetail.getActiveEventByEventDetail");
		query.setParameter("eventDetails", eventDetails);
		return query.getResultList();
	}
	
	@Override
	public List<Event> getEventByEventDetail(List<String> eventDetails) {
		Query query = em.createNamedQuery("EventDetail.getEventByEventDetail");
		query.setParameter("eventDetails", eventDetails);
		return query.getResultList();
	}
	
	@Override
	public List<EventDetail> getHistoryEventByEnd(String program, Date end) {
		Query query = em.createNamedQuery("EventDetail.getHistoryEventByEnd");
		query.setParameter("programName", program);
		query.setParameter("endTime", end);
		return query.getResultList();
	}

	@Override
	public List<EventDetail> getHistoryEventByStart(String programName,
			Date start) {
		Query query = em.createNamedQuery("EventDetail.getHistoryEventByStart");
		query.setParameter("programName", programName);
		query.setParameter("startTime", start);
		return query.getResultList();
	}

	@Override
	public List<EventDetail> getActiveEventLimited(String programName,
			Integer limit) {
		Query query = em.createNamedQuery("EventDetail.getActiveEvent");
		query.setParameter("programName", programName);
		query.setParameter("endTime", new Date());
		query.setMaxResults(limit);
		
		return query.getResultList();
	}

	@Override
	public List<EventDetail> getHistoryEventByStartLimited(String programName,
			Date start, Integer limit) {
		Query query = em.createNamedQuery("EventDetail.getHistoryEventByStart");
		query.setParameter("programName", programName);
		query.setParameter("startTime", start);
		query.setMaxResults(limit);
		
		return query.getResultList();
	}

	@Override
	public Long getActiveEventCountsByPrograms(List<String> programNames) {
		Query query = em.createNamedQuery("EventDetail.getActiveEventCountsByPrograms");
		query.setParameter("programNames", programNames);
		query.setParameter("endTime", new Date());
		return (Long) query.getSingleResult();
	}
	
	@Override
	public List<EventDetail> getAutoDispatchEvents(String programName,String product, Date startTime,String dispatchType,String locationNumber,boolean actualEndTimeFlag){
		
		String[] locationNumbers = locationNumber.split("-");
		List<String> locationNumberList = new ArrayList<String>();
		for(String s:locationNumbers){
			locationNumberList.add(s.trim());
		}
		List<EventDetail> events = new ArrayList<EventDetail>();
		StringBuilder sqlTemplate = new StringBuilder();
		sqlTemplate.append(" SELECT  ed 																							");
		sqlTemplate.append(" FROM EventDetail ed  where ed.event.programName = :programName and ed.event.product = :product 		");
		sqlTemplate.append(" and ed.event.startTime = :startTime														");
		if(actualEndTimeFlag){
			sqlTemplate.append(" and ed.actualEndTime is null																		");
		}else{
			
		}
		boolean allFlag = false;
		if("all".equalsIgnoreCase(dispatchType)){
			sqlTemplate.append(" and ed.allLocationType = 'SLAP'																	");
			allFlag = true;
		}else{
			if("SLAP".equalsIgnoreCase(dispatchType)){
				dispatchType="SLAP";
			}if("ABank".equalsIgnoreCase(dispatchType)){
				dispatchType="ABank";
			}if("Substation".equalsIgnoreCase(dispatchType)){
				dispatchType="Substation";
			}
			sqlTemplate.append(" and ed.location.type = :dispatchType and ed.location.number in (:locationNumberList)						");
			
		}
		Query query = em.createQuery(sqlTemplate.toString());
		query.setParameter("programName", programName);
		query.setParameter("product", product);
		query.setParameter("startTime", startTime);
		if(!allFlag){
			query.setParameter("dispatchType", dispatchType);
			query.setParameter("locationNumberList", locationNumberList);
		}
		events=query.getResultList();
		return events;
	}
	
	@Override
	public List<EventDetail> getHistoryEvents(String programName,
			boolean filterByStart, Date from, Date to, String rate,
			String locationNo, String locationName) {
		StringBuilder sqlTemplate = new StringBuilder();
		
		sqlTemplate.append(" SELECT  ed 																		");
		sqlTemplate.append(" FROM EventDetail ed  where ed.event.programName = :programName 					");
		sqlTemplate.append(" AND ed.actualEndTime IS NOT NULL AND ed.actualEndTime <= CURRENT_TIMESTAMP			");
		if(filterByStart){
			sqlTemplate.append("and date(ed.event.startTime) >= date(:from)										");
			sqlTemplate.append("and date(ed.event.startTime) <= date(:to)										");
		}else{
			sqlTemplate.append("and date(ed.actualEndTime) >= date(:from)										");
			sqlTemplate.append("and date(ed.actualEndTime) <= date(:to)											");
		}
		if(rate != null&&!"".equals(rate))
			sqlTemplate.append(" and ed.event.product = :product												");
		if(locationNo != null&&!"".equals(locationNo))
			sqlTemplate.append(" and ed.location.number like :locationNo											");
		if(locationName != null&&!"".equals(locationName))
			sqlTemplate.append(" and ed.location.name like :locationName											");
		
		Query query = em.createQuery(sqlTemplate.toString());
		query.setParameter("programName", programName);
		query.setParameter("from", from);
		query.setParameter("to", to);
		if(rate != null&&!"".equals(rate))
			query.setParameter("product", rate);
		if(locationNo != null&&!"".equals(locationNo.trim()))
			query.setParameter("locationNo", "%"+locationNo.trim()+"%");
		if(locationName != null&&!"".equals(locationName.trim()))
			query.setParameter("locationName", "%"+locationName.trim()+"%");
		
		List<EventDetail> events=query.getResultList();
		
		return events;
	}

	@Override
	public List<EventDetail> getHistoryEvents(String programName,
			boolean filterByStart, Date from, Date to, List<String> blocks) {
		StringBuilder sqlTemplate = new StringBuilder();
		
		sqlTemplate.append(" SELECT  ed 																		");
		sqlTemplate.append(" FROM EventDetail ed  where ed.event.programName = :programName 					");
		sqlTemplate.append(" AND ed.actualEndTime IS NOT NULL AND ed.actualEndTime <= CURRENT_TIMESTAMP			");
		if(filterByStart){
			sqlTemplate.append("and date(ed.event.startTime) >= date(:from)										");
			sqlTemplate.append("and date(ed.event.startTime) <= date(:to)										");
		}else{
			sqlTemplate.append("and date(ed.actualEndTime) >= date(:from)										");
			sqlTemplate.append("and date(ed.actualEndTime) <= date(:to)											");
		}
		if(blocks != null&&!blocks.isEmpty())
			sqlTemplate.append(" and ed.blockNames in (:blocks)													");
		
		
		Query query = em.createQuery(sqlTemplate.toString());
		query.setParameter("programName", programName);
		query.setParameter("from", from);
		query.setParameter("to", to);
		if(blocks != null&&!blocks.isEmpty())
			query.setParameter("blocks", blocks);
		
		
		List<EventDetail> events=query.getResultList();
		
		return events;
	}
	@Override
	public List<EventDetail> getHistoryEvents(String programName,
			boolean filterByStart, Date from, Date to, List<String> blocks,List<String> products) {
		StringBuilder sqlTemplate = new StringBuilder();
		
		sqlTemplate.append(" SELECT  ed 																		");
		sqlTemplate.append(" FROM EventDetail ed  where ed.event.programName = :programName 					");
		sqlTemplate.append(" AND ed.actualEndTime IS NOT NULL AND ed.actualEndTime <= CURRENT_TIMESTAMP			");
		sqlTemplate.append("and date(ed.event.startTime) >= date(:from)											");
		sqlTemplate.append("and date(ed.actualEndTime) <= date(:to)												");
		if(blocks != null&&!blocks.isEmpty())
			sqlTemplate.append(" and ed.blockNames in (:blocks)													");
		if(products != null&&!products.isEmpty())
			sqlTemplate.append(" and ed.event.product in (:products)											");
		
		Query query = em.createQuery(sqlTemplate.toString());
		query.setParameter("programName", programName);
		query.setParameter("from", from);
		query.setParameter("to", to);
		if(blocks != null&&!blocks.isEmpty())
			query.setParameter("blocks", blocks);
		if(products != null&&!products.isEmpty())
			query.setParameter("products", products);
		
		List<EventDetail> events=query.getResultList();
		
		return events;
	}
	@Override
	public List<EventDetail> getHistoryEvents(String programName,
			boolean filterByStart, Date from, Date to, String rate,
			String locationNo, String locationName,List<String> dispatchTypes) {
		StringBuilder sqlTemplate = new StringBuilder();
		
		sqlTemplate.append(" SELECT  ed 																		");
		//sqlTemplate.append(" FROM EventDetail ed  where ed.event.programName = :programName 					");
		sqlTemplate.append(" FROM EventDetail ed inner join ed.location as loca where ed.event.programName = :programName                                       ");
		sqlTemplate.append(" AND ed.actualEndTime IS NOT NULL AND ed.actualEndTime <= CURRENT_TIMESTAMP			");
		sqlTemplate.append("and date(ed.event.startTime) >= date(:from)											");
		sqlTemplate.append("and date(ed.actualEndTime) <= date(:to)												");
		if(rate != null&&!"".equals(rate))
			sqlTemplate.append(" and ed.event.product = :product												");
		if(locationNo != null&&!"".equals(locationNo))
			sqlTemplate.append(" and ed.location.number like :locationNo										");
		if(locationName != null&&!"".equals(locationName))
			sqlTemplate.append(" and ed.location.name like :locationName										");
		if(dispatchTypes != null&&!dispatchTypes.isEmpty())
			sqlTemplate.append(" and ed.location.type in (:dispatchTypes)										");
		
		Query query = em.createQuery(sqlTemplate.toString());
		query.setParameter("programName", programName);
		query.setParameter("from", from);
		query.setParameter("to", to);
		if(rate != null&&!"".equals(rate))
			query.setParameter("product", rate);
		if(locationNo != null&&!"".equals(locationNo.trim()))
			query.setParameter("locationNo", "%"+locationNo.trim()+"%");
		if(locationName != null&&!"".equals(locationName.trim()))
			query.setParameter("locationName", "%"+locationName.trim()+"%");
		if(dispatchTypes != null&&!dispatchTypes.isEmpty())
			query.setParameter("dispatchTypes", dispatchTypes);
		List<EventDetail> events=query.getResultList();
		
		return events;
	}
	
	@Override
	public List<String> getEventsByEventName(String eventName) {
		Query query = em.createNamedQuery("EventDetail.getEventByEventName");
		query.setParameter("eventName", eventName);
		return query.getResultList();
	}
	
}
