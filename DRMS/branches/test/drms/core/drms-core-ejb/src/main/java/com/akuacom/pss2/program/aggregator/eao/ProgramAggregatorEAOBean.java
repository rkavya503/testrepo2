package com.akuacom.pss2.program.aggregator.eao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.aggregator.ProgramAggregator;

@Stateless
public class ProgramAggregatorEAOBean implements ProgramAggregatorEAO.L,ProgramAggregatorEAO.R {
	
	@PersistenceContext(unitName="core")
	private EntityManager em;
	static Logger log = Logger.getLogger(ProgramAggregatorEAOBean.class);
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(String eventname) {
		Query q = getEm().createNativeQuery("{call add_program_apex_data (?)}");
		q.setParameter(1, eventname);
		q.executeUpdate();
	}

	@Override
	public void delete(ProgramAggregator aggr) throws EntityNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ProgramAggregator> findByEventIdAndAggregatorClientId(String eventId,
			String aggrClientId) {
		Query q = getEm().createNamedQuery("ProgramAggregator.findByEventIdAndAggrClientId");
		q.setParameter("eventId", eventId);
		q.setParameter("clientId", aggrClientId);
		
		List<ProgramAggregator> results = null;
		try {
			results = (List<ProgramAggregator>)q.getResultList();
		} catch (Exception e) {
			results = null;
		}
		return results;
	}
	@Override
	public List<String> findAccountsByAggIdAndEventIds(String aggregatorName,Set<String> eventId) {
		
		Query q = getEm().createNamedQuery("ProgramAggregator.findAccountsByAggIdAndEventIds");
		q.setParameter("eventId", eventId);
		q.setParameter("aggregatorName", aggregatorName);
		List<ProgramAggregator> results = q.getResultList();
		Map<String,String> eventIdDescendentAccountNumberMap = new HashMap<String,String>();
		for(ProgramAggregator prgAgg : results){
			eventIdDescendentAccountNumberMap.put(prgAgg.getEventId(), prgAgg.getChildrenaccounts());
		}
		List<String> accounts = new ArrayList<String>();
		for(String acc: eventIdDescendentAccountNumberMap.values()){
			if(acc!=null){
				accounts.addAll(Arrays.asList(acc.split(",")));
			}
		}
		return accounts;
	}
	private EntityManager getEm(){
		return this.em;
	}
	@Override
	public List<String> findEventIdsByClientName(String participantName){
		List<String> eventIDs = new ArrayList<String>();
		Query q = getEm().createNamedQuery("ProgramAggregator.findResourcesByClientName");
		q.setParameter("aggregatorName", participantName);
		eventIDs = (List<String>)(q.getResultList());
		return eventIDs;
	}
	@Override
	public List<String> findEventUUIdsByClientName(String participantName){
		List<String> eventUUIDs = new ArrayList<String>();
		Query q = getEm().createNamedQuery("ProgramAggregator.findEventUUIDByClientName");
		q.setParameter("aggregatorName", participantName);
		eventUUIDs = (List<String>)(q.getResultList());
		return eventUUIDs;
	}
	
	@Override
	public ProgramAggregator findAccountNumberOfAggregator(String aggregatorName,String eventId){
		Query q = getEm().createNamedQuery("ProgramAggregator.findAccNumberByClientNameAndEventID");
		q.setParameter("aggregatorName", aggregatorName);
		q.setParameter("eventId", eventId);
		ProgramAggregator prgAgg = null;
		try{
			prgAgg =(ProgramAggregator)q.getSingleResult();
		}catch(NoResultException e){
			log.info("NoResultException::::::::::::::::::::::::::::::::");
		}catch(NonUniqueResultException  e){
			log.info("NonUniqueResultException::::::::::::::::::::::::::::::::");
		}catch(Exception e){
			log.info("Exception::::::::::::::::::::::::::::::::");
		}
		return prgAgg;
	}

}
