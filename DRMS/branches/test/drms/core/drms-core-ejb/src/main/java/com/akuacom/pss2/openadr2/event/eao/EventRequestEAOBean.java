package com.akuacom.pss2.openadr2.event.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.event.EventRequest;

@Stateless
public class EventRequestEAOBean  implements EventRequestEAO.L,EventRequestEAO.R {

	@PersistenceContext(unitName="core")
	protected EntityManager em;
	@Override
	public void create(EventRequest er) {
		getEm().persist(er);
	}
	
	@Override
	public void delete(EventRequest er) throws EntityNotFoundException {
		getEm().remove(er);
	}
	
	@Override
	public EventRequest findByUUID(String uuid) throws EntityNotFoundException {
		return getEm().find(EventRequest.class, uuid);
	}
	
	@Override
	public List<EventRequest> findByEventID(String eventId) {
		Query q = getEm().createNamedQuery("EventRequest.findByEventEntityId");
		q.setParameter("eventId", eventId);
		
		@SuppressWarnings("unchecked")
		List<EventRequest> results = q.getResultList();
		
		return results;
	}
	
	@Override
	public List<EventRequest> findByRequestAndVenID(String requestId, String venId) {
		Query q = getEm().createNamedQuery("EventRequest.findByRequestAndVen");
		q.setParameter("requestId", requestId);
		q.setParameter("venId", venId);
		
		@SuppressWarnings("unchecked")
		List<EventRequest> results = q.getResultList();
		
		return results;
	}
	
	@Override
	public List<EventRequest> findByRequestId(String requestId) {
		Query q = getEm().createNamedQuery("EventRequest.findByRequestId");
		q.setParameter("requestId", requestId);
		
		@SuppressWarnings("unchecked")
		List<EventRequest> results = q.getResultList();
		
		return results;
	}
	
	@Override
	public List<EventRequest> findByRequestAndEventAndVenID(String requestId, String eventId, String venId) {
		Query q = getEm().createNamedQuery("EventRequest.findByRequestAndEventAndVen");
		q.setParameter("requestId", requestId);
		q.setParameter("venId", venId);
		q.setParameter("eventId", eventId);
		
		@SuppressWarnings("unchecked")
		List<EventRequest> results = q.getResultList();
		
		return results;
	}
	public EventRequest findByRequestIdAndEventId(String requestId, String eventId){
		Query q = getEm().createNamedQuery("EventRequest.findByRequestIdAndEventId");
		q.setParameter("requestId", requestId);
		q.setParameter("eventId", eventId);
		
		@SuppressWarnings("unchecked")
		EventRequest result = (EventRequest)q.getSingleResult();
		return result;
	}
	private EntityManager getEm(){
		return this.em;
	}

}
