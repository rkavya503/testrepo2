package com.akuacom.pss2.event.participant;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;

/**
 * The Data Structure that aggregates {@link EventParticipant}.
 * 
 * 
 * Uses {@link EventParticipantManager} {@link EventManager} and
 * {@link ParticipantManager} to satisfy many of the requests.
 * 
 * @author Sunil
 * 
 */
@Stateless
public class EventParticipantAggregationManagerBean implements
		EventParticipantAggregationManager.L, EventParticipantAggregationManager.R {

	
    @EJB
    EventParticipantEAO.L eventParticipantEAO;
	
	@Override
	public void addChild(EventParticipant parent, EventParticipant child) {
		eventParticipantEAO.addChild(parent, child);

	}

	@Override
	public EventParticipant getAncestor(EventParticipant child) {
		return eventParticipantEAO.getAncestor(child);
	}

	@Override
	public EventParticipant getCommonAncestor(EventParticipant pp1,
			EventParticipant pp2) {
		return eventParticipantEAO.getCommonAncestor(pp1, pp2);
	}

	/*
	@Override
	public Set<EventParticipant> getDescendants(EventParticipant parent) {
		return eventParticipantEAO.getDescendants(parent);
	}*/

	@Override
	public Set<EventParticipant> getFlatAncestor(EventParticipant child) {
		Set<EventParticipant> result = new HashSet<EventParticipant>();
		EventParticipant mom = child;
		while((mom = getAncestor(mom)) != null ){
			result.add(mom);
		}
		return result;

	
	}

	/*
	@Override
	public Set<EventParticipant> getFlatDescendants(EventParticipant parent) {
		return eventParticipantEAO.getFlatDescendants(parent);
	}*/

	@Override
	public boolean isAncestor(EventParticipant parent, EventParticipant child) {
		return eventParticipantEAO.isAncestor(parent, child);
	}

	@Override
	public boolean isDescendant(EventParticipant parent, EventParticipant child) {
		return eventParticipantEAO.isDescendant(parent, child);
	}

	@Override
	public void removeChildren(EventParticipant parent,
			Set<EventParticipant> children) {
		eventParticipantEAO.removeChildren(parent, children);

	}

	@Override
	public void removeParent(EventParticipant child) {
		eventParticipantEAO.removeParent(child);

	}

	@Override
	public void setParent(EventParticipant child, EventParticipant parent) {
		eventParticipantEAO.addChild(parent, child);

	}

	@Override
	public EventParticipant getRoot(EventParticipant child) {
		return eventParticipantEAO.getRoot(child);
	}

}
