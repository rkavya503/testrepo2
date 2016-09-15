/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.SignalEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.subaccount;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.participant.Participant;

/**
 * The Class SubAccount.
 */
@Entity
@Table(name = "sub_account")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
		@NamedQuery(name = "SubAccount.findAllByParticipantName", query = "select sa from SubAccount sa where sa.participant.participantName = :name and sa.participant.client = 0", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "SubAccount.findAllByParticipantNameAndSubAccountID", query = "select sa from SubAccount sa where sa.participant.participantName = :name and sa.subAccountId = :subAccountId and sa.participant.client = 0", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }) })
public class SubAccount extends VersionedEntity {
	/** The participant name. */
	@ManyToOne
	@JoinColumn(name = "participant_uuid")
	private Participant participant;

	/** The sub account id. */
	private String subAccountId;

	private String premiseNumber;

	/** The comment. */
	@Column(columnDefinition="text")
	private String comment;

	/** The start date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	/** The end date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date enrollmentDate;

	public String getParticipantName() {
		if (participant == null)
			return null;
		else
			return participant.getParticipantName();
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public String getSubAccountId() {
		return subAccountId;
	}

	public void setSubAccountId(String subAccountId) {
		this.subAccountId = subAccountId;
	}

	public String getPremiseNumber() {
		return premiseNumber;
	}

	public void setPremiseNumber(String premiseNumber) {
		this.premiseNumber = premiseNumber;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
}