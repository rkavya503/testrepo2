package com.akuacom.pss2.program.apx.aggregator.eao;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.program.apx.ApxAggregator;


public interface ApxAggregatorEAO {
	@Remote
    public interface R extends ApxAggregatorEAO {}
    @Local
    public interface L extends ApxAggregatorEAO {}
    
    public void create(ApxAggregator aggr);
   	public void delete(ApxAggregator aggr) throws EntityNotFoundException;
   	public ApxAggregator findByEventIdAndAggregatorClientId(String eventId,String aggrClientId);
   	//public ApxAggregator findByEventIdEventUuidAndAggregatorClientId(String eventId,String aggrClientId);
}
