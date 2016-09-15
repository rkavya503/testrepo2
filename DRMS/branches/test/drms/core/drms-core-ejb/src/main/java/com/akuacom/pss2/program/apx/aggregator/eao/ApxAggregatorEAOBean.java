package com.akuacom.pss2.program.apx.aggregator.eao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.program.apx.ApxAggregator;

@Stateless
public class ApxAggregatorEAOBean implements ApxAggregatorEAO.L,ApxAggregatorEAO.R {
	
	@PersistenceContext(unitName="core")
	private EntityManager em;

	@Override
	public void create(ApxAggregator aggr) {
		getEm().persist(aggr);
	}

	@Override
	public void delete(ApxAggregator aggr) throws EntityNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ApxAggregator findByEventIdAndAggregatorClientId(String eventId,
			String aggrClientId) {
		Query q = getEm().createNamedQuery("ApxAggregator.findByApxEventIdAndAggrClientId");
		q.setParameter("eventId", eventId);
		q.setParameter("clientId", aggrClientId);
		
		ApxAggregator results = null;
		try {
			results = (ApxAggregator)q.getSingleResult();
		} catch (Exception e) {
			results = null;
		}
		return results;
	}
	/*@Override
	public ApxAggregator findByEventIdEventUuidAndAggregatorClientId(
			String eventId,String aggrClientId) {
		Query q = getEm().createNamedQuery("ApxAggregator.findByApxEventIdApxEventUUIDAndAggrClientId");
		q.setParameter("eventId", eventId);
		q.setParameter("clientId", aggrClientId);
		ApxAggregator results = null;
		try {
			results = (ApxAggregator)q.getSingleResult();
		} catch (Exception e) {
			results = null;
		}
		return results;
	}*/
	private EntityManager getEm(){
		return this.em;
	}

}
