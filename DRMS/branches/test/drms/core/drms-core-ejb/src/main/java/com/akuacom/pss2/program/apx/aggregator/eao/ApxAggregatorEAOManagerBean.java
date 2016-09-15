package com.akuacom.pss2.program.apx.aggregator.eao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.program.apx.ApxAggregator;

@Stateless
public class ApxAggregatorEAOManagerBean implements ApxAggregatorEAOManager.L,ApxAggregatorEAOManager.R{
	@EJB
	private ApxAggregatorEAO.L apxAggregator;
	
	@Override
	public void createApxAggregator(ApxAggregator aggr) {
		apxAggregator.create(aggr);
	}

	@Override
	public Set<String>  getAggregatorResources(String eventId,
			String aggrClientId) {
		Set<String> resourceList = new TreeSet
				<String>();
		ApxAggregator agg = apxAggregator.findByEventIdAndAggregatorClientId(eventId, aggrClientId);
		if(null != agg){
			resourceList = agg.getAggregatorResources();
		}
		return resourceList;
	}

	/*@Override
	public Set<String> getAggregatorResources(String eventId,
			String aggrClientId, String apxEventUuid) {
		Set<String> resourceList = new TreeSet
				<String>();
		ApxAggregator agg = apxAggregator.findByEventIdEventUuidAndAggregatorClientId(eventId,aggrClientId);
		if(null != agg){
			resourceList = agg.getAggregatorResources();
		}
		return resourceList;
	}*/

}
