/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ConstraintEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class Constraint.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program_participant_constraint")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Constraint extends VersionedEntity {

	private static final long serialVersionUID = -8058201295934160258L;

	/**
	 * The Enum Action.
	 */
	public enum Action {

		/** The RESTRICT. */
		RESTRICT,

		/** The ACCEPT. */
		ACCEPT,

		/** The REJECT. */
		REJECT,

		/** The FORCE. */
		FORCE
	}

	/** The min active. */
	private Date minActive;

	/** The max active. */
	private Date maxActive;

	/** The active action. */
	@Enumerated(EnumType.STRING)
	private Action activeAction;

	/** The min notify. */
	private Date minNotify;

	/** The max notify. */
	private Date maxNotify;

	/** The notify action. */
	@Enumerated(EnumType.STRING)
	private Action notifyAction;

	/** The min duration. */
	private Date minDuration;

	/** The max duration. */
	private Date maxDuration;

	/** The duration action. */
	@Enumerated(EnumType.STRING)
	private Action durationAction;

	/** The max consecutive d. */
	private Integer maxConsecutiveD;

	/** The consecutive action. */
	@Enumerated(EnumType.STRING)
	private Action consecutiveAction;

	/** The program participant. */
	//many-to-one acting as one-to-one to achieve lazy load
	@ManyToOne
	@JoinColumn(name = "program_participant_uuid")
	//@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private ProgramParticipant programParticipant;
	
	/** The invalid dates. */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "constraint")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.REMOVE,
            org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<InvalidDate> invalidDates;
	
	/**
	 * Gets the min active.
	 * 
	 * @return the min active
	 */
	public Date getMinActive() {
		return minActive;
	}

	/**
	 * Sets the min active.
	 * 
	 * @param minActive
	 *            the new min active
	 */
	public void setMinActive(Date minActive) {
		this.minActive = minActive;
	}

	/**
	 * Gets the max active.
	 * 
	 * @return the max active
	 */
	public Date getMaxActive() {
		return maxActive;
	}

	/**
	 * Sets the max active.
	 * 
	 * @param maxActive
	 *            the new max active
	 */
	public void setMaxActive(Date maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * Gets the min notify.
	 * 
	 * @return the min notify
	 */
	public Date getMinNotify() {
		return minNotify;
	}

	/**
	 * Sets the min notify.
	 * 
	 * @param minNotify
	 *            the new min notify
	 */
	public void setMinNotify(Date minNotify) {
		this.minNotify = minNotify;
	}

	/**
	 * Gets the max notify.
	 * 
	 * @return the max notify
	 */
	public Date getMaxNotify() {
		return maxNotify;
	}

	/**
	 * Sets the max notify.
	 * 
	 * @param maxNotify
	 *            the new max notify
	 */
	public void setMaxNotify(Date maxNotify) {
		this.maxNotify = maxNotify;
	}

	/**
	 * Gets the min duration.
	 * 
	 * @return the min duration
	 */
	public Date getMinDuration() {
		return minDuration;
	}

	/**
	 * Sets the min duration.
	 * 
	 * @param minDuration
	 *            the new min duration
	 */
	public void setMinDuration(Date minDuration) {
		this.minDuration = minDuration;
	}

	/**
	 * Gets the max duration.
	 * 
	 * @return the max duration
	 */
	public Date getMaxDuration() {
		return maxDuration;
	}

	/**
	 * Sets the max duration.
	 * 
	 * @param maxDuration
	 *            the new max duration
	 */
	public void setMaxDuration(Date maxDuration) {
		this.maxDuration = maxDuration;
	}

	/**
	 * Gets the max consecutive d.
	 * 
	 * @return the max consecutive d
	 */
	public Integer getMaxConsecutiveD() {
		return maxConsecutiveD;
	}

	/**
	 * Sets the max consecutive d.
	 * 
	 * @param maxConsecutiveD
	 *            the new max consecutive d
	 */
	public void setMaxConsecutiveD(Integer maxConsecutiveD) {
		this.maxConsecutiveD = maxConsecutiveD;
	}

	/**
	 * Gets the active action.
	 * 
	 * @return the active action
	 */
	public Action getActiveAction() {
		return activeAction;
	}

	/**
	 * Sets the active action.
	 * 
	 * @param activeAction
	 *            the new active action
	 */
	public void setActiveAction(Action activeAction) {
		this.activeAction = activeAction;
	}

	/**
	 * Gets the notify action.
	 * 
	 * @return the notify action
	 */
	public Action getNotifyAction() {
		return notifyAction;
	}

	/**
	 * Sets the notify action.
	 * 
	 * @param notifyAction
	 *            the new notify action
	 */
	public void setNotifyAction(Action notifyAction) {
		this.notifyAction = notifyAction;
	}

	/**
	 * Gets the duration action.
	 * 
	 * @return the duration action
	 */
	public Action getDurationAction() {
		return durationAction;
	}

	/**
	 * Sets the duration action.
	 * 
	 * @param durationAction
	 *            the new duration action
	 */
	public void setDurationAction(Action durationAction) {
		this.durationAction = durationAction;
	}

	/**
	 * Gets the consecutive action.
	 * 
	 * @return the consecutive action
	 */
	public Action getConsecutiveAction() {
		return consecutiveAction;
	}

	/**
	 * Sets the consecutive action.
	 * 
	 * @param consecutiveAction
	 *            the new consecutive action
	 */
	public void setConsecutiveAction(Action consecutiveAction) {
		this.consecutiveAction = consecutiveAction;
	}

	/**
	 * Gets the program participant.
	 * 
	 * @return the program participant
	 */
	public ProgramParticipant getProgramParticipant() {
		return programParticipant;
	}

	/**
	 * Sets the program participant.
	 * 
	 * @param programParticipant
	 *            the new program participant
	 */
	public void setProgramParticipant(ProgramParticipant programParticipant) {
		this.programParticipant = programParticipant;
	}

	/**
	 * Gets the invalid dates.
	 * 
	 * @return the invalid dates
	 */
	public Set<InvalidDate> getInvalidDates() {
		return invalidDates;
	}

	/**
	 * Sets the invalid dates.
	 * 
	 * @param invalidDates
	 *            the new invalid dates
	 */
	public void setInvalidDates(Set<InvalidDate> invalidDates) {
		this.invalidDates = invalidDates;
	}
}
