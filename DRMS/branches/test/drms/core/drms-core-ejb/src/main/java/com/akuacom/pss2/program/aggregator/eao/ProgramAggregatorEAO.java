package com.akuacom.pss2.program.aggregator.eao;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.program.aggregator.ProgramAggregator;


public interface ProgramAggregatorEAO {
	@Remote
    public interface R extends ProgramAggregatorEAO {}
    @Local
    public interface L extends ProgramAggregatorEAO {}
    
    public void create(String eventName);
   	public void delete(ProgramAggregator aggr) throws EntityNotFoundException;
   	public List<ProgramAggregator> findByEventIdAndAggregatorClientId(String eventId,String aggrClientId);
   	public List<String> findAccountsByAggIdAndEventIds(String aggrId,Set<String> eventId);
   	public List<String> findEventUUIdsByClientName(String participantName);
   	public List<String> findEventIdsByClientName(String participantName);
   	public ProgramAggregator findAccountNumberOfAggregator(String participantName,String eventName);
}
