package com.akuacom.pss2.program.participant;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.ProgramManager;

/**
 * The Data Structure that aggregates {@link ProgramParticipant}.
 * 
 * 
 * Uses {@link ProgramParticipantManager} {@link ProgramManager} and
 * {@link ParticipantManager} to satisfy many of the requests.
 * 
 * @author Sunil
 * 
 */
@Stateless
public class ProgramParticipantAggregationManagerBean implements
		ProgramParticipantAggregationManager.R, ProgramParticipantAggregationManager.L {

	
    @EJB
    private ProgramParticipantEAO.L ppEAO;

	
	@Override
	public void addChild(ProgramParticipant parent, ProgramParticipant child) {
		ppEAO.addChild(parent, child);
	}

	@Override
	public ProgramParticipant getAncestor(ProgramParticipant child) {
		return ppEAO.getAncestor(child);
	}

	@Override
	public ProgramParticipant getCommonAncestor(ProgramParticipant pp1,
			ProgramParticipant pp2) {
		return ppEAO.getCommonAncestor(pp1, pp2);
	}

	@Override
	public Set<ProgramParticipant> getDescendants(ProgramParticipant parent) {
		return ppEAO.getDescendants(parent);
	}

	@Override
	public Set<ProgramParticipant> getDescendantsForSpecifiedProgram(ProgramParticipant parent,String programName) {
		return ppEAO.getDescendantsForSpecifiedProgram(parent,programName);
	}
	
	@Override
	public Set<ProgramParticipant> getFlatAncestor(ProgramParticipant child) {
		Set<ProgramParticipant> result = new HashSet<ProgramParticipant>();
		ProgramParticipant mom = child;
		while((mom = getAncestor(mom)) != null ){
			result.add(mom);
		}
		return result;
	}

	@Override
	public Set<ProgramParticipant> getFlatDescendants(ProgramParticipant parent) {
		return ppEAO.getFlatDescendants(parent);
	}

	@Override
	public boolean isAncestor(ProgramParticipant parent,
			ProgramParticipant child) {
		return ppEAO.isAncestor(parent, child);
	}

	@Override
	public boolean isDescendant(ProgramParticipant parent,
			ProgramParticipant child) {
		return ppEAO.isDescendant(parent, child);
	}

	@Override
	public void removeChildren(ProgramParticipant parent,
			Set<ProgramParticipant> children) {
		ppEAO.removeChildren(parent, children);

	}

	@Override
	public void removeParent(ProgramParticipant child) {
		ppEAO.removeParent(child);
	}

	@Override
	public void setParent(ProgramParticipant child, ProgramParticipant parent) {
		ppEAO.addChild(parent, child);
	}

	@Override
	public ProgramParticipant getRoot(ProgramParticipant child) {
		return ppEAO.getRoot(child);
	}

}
