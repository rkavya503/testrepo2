/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.matrix.ProgramMatrix.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.matrix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class ProgramMatrix.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program_matrix")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
		@NamedQuery(name = "ProgramMatrix.findAll", query = "select pm from ProgramMatrix pm", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "ProgramMatrix.findProgramsInConflict", query = "select pm from ProgramMatrix pm where pm.coexist = false and ((pm.program1UUID = :programUUID1 and pm.program2UUID = :programUUID2) or (pm.program1UUID = :programUUID2 and pm.program2UUID = :programUUID1))", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "ProgramMatrix.findMatrixForProgram", query = "select pm from ProgramMatrix pm where  ((pm.program1UUID = :programUUID) or (pm.program2UUID = :programUUID))", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }) })
public class ProgramMatrix extends VersionedEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 996923716354827448L;

	/** The program1 uuid. */
	private String program1UUID;

	/** The program2 uuid. */
	private String program2UUID;

	/** The coexist. */
	private Boolean coexist;

	/**
	 * Gets the coexist.
	 * 
	 * @return the coexist
	 */
	public Boolean getCoexist() {
		return coexist;
	}

	/**
	 * Sets the coexist.
	 * 
	 * @param coexist
	 *            the new coexist
	 */
	public void setCoexist(Boolean coexist) {
		this.coexist = coexist;
	}

	/**
	 * Gets the program1 uuid.
	 * 
	 * @return the program1 uuid
	 */
	public String getProgram1UUID() {
		return program1UUID;
	}

	/**
	 * Sets the program1 uuid.
	 * 
	 * @param program1UUID
	 *            the new program1 uuid
	 */
	public void setProgram1UUID(String program1UUID) {
		this.program1UUID = program1UUID;
	}

	/**
	 * Gets the program2 uuid.
	 * 
	 * @return the program2 uuid
	 */
	public String getProgram2UUID() {
		return program2UUID;
	}

	/**
	 * Sets the program2 uuid.
	 * 
	 * @param program2UUID
	 *            the new program2 uuid
	 */
	public void setProgram2UUID(String program2UUID) {
		this.program2UUID = program2UUID;
	}

}