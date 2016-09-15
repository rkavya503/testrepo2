package com.akuacom.pss2.event.participant;

import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.ParticipantManager;

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
public interface EventParticipantAggregationManager {

	@Remote
	public interface R extends EventParticipantAggregationManager {
	}

	@Local
	public interface L extends EventParticipantAggregationManager {
	}

	/**
	 * Add a child {@link EventParticipant} to the parent
	 * {@link EventParticipant}.
	 * 
	 * @param parent
	 *            Parent for the relationship. Cannot be NULL.
	 * @param child
	 *            Child for the relationship. Cannot be NULL.
	 */
	void addChild(EventParticipant parent, EventParticipant child);

	/**
	 * Sets the parent {@link EventParticipant} for the child
	 * {@link EventParticipant}. It removes any existing parent relationship
	 * before setting this new parent relationship. Parent could be NULL.
	 * 
	 * @param child
	 *            Child for the relationship. Cannot be NULL.
	 * @param parent
	 *            new Parent for the relationship. Could be NULL.
	 */
	void setParent(EventParticipant child, EventParticipant parent);

	/**
	 * Remove children {@link EventParticipant} to parent
	 * {@link EventParticipant} relationships, if they exists.
	 * 
	 * @param parent
	 *            Parent for the relationship. Cannot be NULL.
	 * @param child
	 *            Set of Children for the relationship. Cannot be NULL.
	 */
	void removeChildren(EventParticipant parent, Set<EventParticipant> children);

	/**
	 * Remove a child {@link EventParticipant} to parent
	 * {@link EventParticipant} relationship.
	 * 
	 * @param child
	 *            Child for the relationship. Cannot be NULL.
	 */
	void removeParent(EventParticipant child);

	/**
	 * Fetches the Descendants for this parent. The Descendants can have their
	 * own children. So you get an effective tree {@link EventParticipant} for
	 * the provided parent {@link EventParticipant}.
	 * 
	 * @param parent
	 *            - Parent for the tree
	 * @return Descendants of the parent. Set of {@link EventParticipant}
	 */
	//Set<EventParticipant> getDescendants(EventParticipant parent);

	/**
	 * Fetches the Descendants for this parent. The Descendants can have their
	 * own children. Whole tree is flattened. So you get an effective flat tree
	 * {@link EventParticipant} for the provided parent {@link EventParticipant}
	 * .
	 * 
	 * @param parent
	 *            - Parent for the tree
	 * @return Descendants of the parent. Set of {@link EventParticipant}
	 */
	//Set<EventParticipant> getFlatDescendants(EventParticipant parent);

	/**
	 * Fetches the ancestor for this child {@link EventParticipant}. The
	 * ancestor can have his own parent. So effectively you will get all the
	 * ancestors.
	 * 
	 * @param child
	 *            - child for whom ancestor is needed
	 * @return Ancestor {@link EventParticipant}
	 */
	EventParticipant getAncestor(EventParticipant child);

	/**
	 * Fetches the ancestor for this child {@link EventParticipant}. The
	 * ancestor can have his own parent. Whole tree is flattened. So effectively
	 * you will get all the ancestors as a flat list.
	 * 
	 * @param child
	 *            - child for whom ancestor is needed
	 * @return Ancestor {@link EventParticipant} as a flat tree
	 */
	Set<EventParticipant> getFlatAncestor(EventParticipant child);

	/**
	 * Fetches the root of the tree for this child {@link EventParticipant}.
	 * 
	 * @param child
	 *            - child for whom ancestor is needed
	 * @return root {@link EventParticipant}
	 */
	EventParticipant getRoot(EventParticipant child);

	/**
	 * Fetches the common ancestor for the two participants
	 * {@link EventParticipant}.
	 * 
	 * @param pp1
	 *            - First {@link EventParticipant}
	 * @param pp2
	 *            - Second {@link EventParticipant}
	 * @return Common Ancestor {@link EventParticipant}
	 */
	EventParticipant getCommonAncestor(EventParticipant pp1,
			EventParticipant pp2);

	/**
	 * Checks if the child {@link EventParticipant} is in the heirarchy of the
	 * parent {@link EventParticipant}. Returns true if child is a descendant.
	 * 
	 * @param parent
	 *            - parent {@link EventParticipant}
	 * @param child
	 *            - child {@link EventParticipant}
	 * @return Returns true if child is a descendant.
	 */
	boolean isDescendant(EventParticipant parent, EventParticipant child);

	/**
	 * Checks if the parent {@link EventParticipant} is an ancestor of the child
	 * {@link EventParticipant}. Returns true if parent is an ancestor.
	 * 
	 * @param parent
	 *            - parent {@link EventParticipant}
	 * @param child
	 *            - child {@link EventParticipant}
	 * @return Returns true if parent is an ancestor.
	 */
	boolean isAncestor(EventParticipant parent, EventParticipant child);

}
