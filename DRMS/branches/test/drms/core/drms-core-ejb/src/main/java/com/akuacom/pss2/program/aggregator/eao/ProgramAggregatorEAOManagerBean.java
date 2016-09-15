package com.akuacom.pss2.program.aggregator.eao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.program.aggregator.ProgramAggregator;

@Stateless
public class ProgramAggregatorEAOManagerBean implements ProgramAggregatorEAOManager.L,ProgramAggregatorEAOManager.R{
	@EJB
	private ProgramAggregatorEAO.L programAggregator;
	@EJB
	private EventParticipantEAO.L epEAO;
	
	@Override
	public void createProgramAggregator(String eventName) {
		programAggregator.create(eventName);
	}

	@Override
	public List<String> getAggregatorResources(String aggrId,Set<String>eventIds) {
		return programAggregator.findAccountsByAggIdAndEventIds(aggrId,eventIds);
	}
}
