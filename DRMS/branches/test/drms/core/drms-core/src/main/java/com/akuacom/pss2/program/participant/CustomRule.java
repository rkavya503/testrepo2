/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.RuleEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.pss2.rule.Rule;

/**
 * The Class Rule.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "custom_rule")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class CustomRule extends Rule {

	private static final long serialVersionUID = 6077766694999604783L;

	@ManyToOne
	@JoinColumn(name = "program_participant_uuid")
	private ProgramParticipant programParticipant;

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
}
