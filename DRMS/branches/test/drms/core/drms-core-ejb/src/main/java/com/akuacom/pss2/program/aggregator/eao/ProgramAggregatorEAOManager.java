package com.akuacom.pss2.program.aggregator.eao;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface ProgramAggregatorEAOManager {
	@Remote
    public interface R extends ProgramAggregatorEAOManager {}
    @Local
    public interface L extends ProgramAggregatorEAOManager {}
    public void createProgramAggregator(String eventName);
    public List<String> getAggregatorResources(String aggrId, Set<String> eventIds);
}
