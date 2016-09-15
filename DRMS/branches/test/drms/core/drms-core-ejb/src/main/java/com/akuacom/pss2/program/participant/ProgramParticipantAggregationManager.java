package com.akuacom.pss2.program.participant;

import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

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
public interface ProgramParticipantAggregationManager {

	@Remote
	public interface R extends ProgramParticipantAggregationManager {
	}

	@Local
	public interface L extends ProgramParticipantAggregationManager {
	}

	/**
	 * Add a child {@link ProgramParticipant} to the parent
	 * {@link ProgramParticipant}.
	 * 
	 * @param parent
	 *            Parent for the relationship. Cannot be NULL.
	 * @param child
	 *            Child for the relationship. Cannot be NULL.
	 */
	void addChild(ProgramParticipant parent, ProgramParticipant child);

	/**
	 * Sets the parent {@link ProgramParticipant} for the child
	 * {@link ProgramParticipant}. It removes any existing parent relationship
	 * before setting this new parent relationship. Parent could be NULL.
	 * 
	 * @param child
	 *            Child for the relationship. Cannot be NULL.
	 * @param parent
	 *            new Parent for the relationship. Could be NULL.
	 */
	void setParent(ProgramParticipant child, ProgramParticipant parent);

	/**
	 * Remove children {@link ProgramParticipant} to parent
	 * {@link ProgramParticipant} relationships, if they exists.
	 * 
	 * @param parent
	 *            Parent for the relationship. Cannot be NULL.
	 * @param child
	 *            Set of Children for the relationship. Cannot be NULL.
	 */
	void removeChildren(ProgramParticipant parent,
			Set<ProgramParticipant> children);

	/**
	 * Remove a child {@link ProgramParticipant} to parent
	 * {@link ProgramParticipant} relationship.
	 * 
	 * @param child
	 *            Child for the relationship. Cannot be NULL.
	 */
	void removeParent(ProgramParticipant child);

	/**
	 * Fetches the Descendants for this parent. The Descendants can have their
	 * own children. So you get an effective tree {@link ProgramParticipant} for
	 * the provided parent {@link ProgramParticipant}.
	 * 
	 * @param parent
	 *            - Parent for the tree
	 * @return Descendants of the parent. Set of {@link ProgramParticipant}
	 */
	Set<ProgramParticipant> getDescendants(ProgramParticipant parent);

	/**
	 * Fetches the Descendants for this parent. The Descendants can have their
	 * own children. Whole tree is flattened. So you get an effective flat tree
	 * {@link ProgramParticipant} for the provided parent
	 * {@link ProgramParticipant}.
	 * 
	 * @param parent
	 *            - Parent for the tree
	 * @return Descendants of the parent. Set of {@link ProgramParticipant}
	 */
	Set<ProgramParticipant> getFlatDescendants(ProgramParticipant parent);

	/**
	 * Fetches the ancestor for this child {@link ProgramParticipant}. The
	 * ancestor can have his own parent. So effectively you will get all the
	 * ancestors.
	 * 
	 * @param child
	 *            - child for whom ancestor is needed
	 * @return Ancestor {@link ProgramParticipant}
	 */
	ProgramParticipant getAncestor(ProgramParticipant child);

	/**
	 * Fetches the root of the tree for this child {@link ProgramParticipant}.
	 * 
	 * @param child
	 *            - child for whom ancestor is needed
	 * @return root {@link ProgramParticipant}
	 */
	ProgramParticipant getRoot(ProgramParticipant child);

	/**
	 * Fetches the ancestor for this child {@link ProgramParticipant}. The
	 * ancestor can have his own parent. Whole tree is flattened. So effectively
	 * you will get all the ancestors as a flat Set.
	 * 
	 * @param child
	 *            - child for whom ancestor is needed
	 * @return Ancestor {@link ProgramParticipant} as a flat tree
	 */
	Set<ProgramParticipant> getFlatAncestor(ProgramParticipant child);

	/**
	 * Fetches the common ancestor for the two participants
	 * {@link ProgramParticipant}.
	 * 
	 * @param pp1
	 *            - First {@link ProgramParticipant}
	 * @param pp2
	 *            - Second {@link ProgramParticipant}
	 * @return Common Ancestor {@link ProgramParticipant}
	 */
	ProgramParticipant getCommonAncestor(ProgramParticipant pp1,
			ProgramParticipant pp2);

	/**
	 * Checks if the child {@link ProgramParticipant} is in the heirarchy of the
	 * parent {@link ProgramParticipant}. Returns true if child is a descendant.
	 * 
	 * @param parent
	 *            - parent {@link ProgramParticipant}
	 * @param child
	 *            - child {@link ProgramParticipant}
	 * @return Returns true if child is a descendant.
	 */
	boolean isDescendant(ProgramParticipant parent, ProgramParticipant child);

	/**
	 * Checks if the parent {@link ProgramParticipant} is an ancestor of the
	 * child {@link ProgramParticipant}. Returns true if parent is an ancestor.
	 * 
	 * @param parent
	 *            - parent {@link ProgramParticipant}
	 * @param child
	 *            - child {@link ProgramParticipant}
	 * @return Returns true if parent is an ancestor.
	 */
	boolean isAncestor(ProgramParticipant parent, ProgramParticipant child);
	
	/**
	 * Returns only the set of descendants, who belongs to specified program
	 * 
	 * @param parent
	 *            - parent {@link ProgramParticipant}
	 * @param Program
	 *            - program {@link ProgramParticipant}
	 * @return Returns set of ProgramParticipant
	 */
	
	Set<ProgramParticipant> getDescendantsForSpecifiedProgram(ProgramParticipant parent,String programName);
}
