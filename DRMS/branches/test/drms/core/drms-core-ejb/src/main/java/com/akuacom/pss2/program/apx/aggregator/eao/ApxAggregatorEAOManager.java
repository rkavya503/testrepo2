package com.akuacom.pss2.program.apx.aggregator.eao;

import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.apx.ApxAggregator;

public interface ApxAggregatorEAOManager {
	@Remote
    public interface R extends ApxAggregatorEAOManager {}
    @Local
    public interface L extends ApxAggregatorEAOManager {}
    
    public void createApxAggregator(ApxAggregator aggr);
    public Set<String>  getAggregatorResources(String eventId,String aggrClientId);
   // public Set<String>  getAggregatorResources(String eventId,String aggrClientId,String apxEventUuid);

}
