/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.eventtemplate.EventTemplate.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.program.eventtemplate;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.annotations.DubiousColumnDefinition;

/**
 * The Class EventTemplate.
 */
@Entity
@Table(name = "event_template")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class EventTemplate extends VersionedEntity {

	private static final long serialVersionUID = 2093676947492699749L;

	/** The program name. */
	private String programName;

	/** The singal entries. */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventTemplate")
	@OrderBy("relativeStartTime")
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<EventTemplateSignalEntry> singalEntries;

	/** The issued time. */
	@Transient //SEE DRMS-2170
	@DubiousColumnDefinition("no column for this field")
	private Date issuedTime;

	/** The start time. */
	@Transient //SEE DRMS-2170
	@DubiousColumnDefinition("no column for this field")
	private Date startTime;

	/** The end time. */
	@Transient //SEE DRMS-2170
	@DubiousColumnDefinition("no column for this field")
	private Date endTime;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the start time.
	 * 
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 * 
	 * @param startTime
	 *            the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 * 
	 * @return the end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 * 
	 * @param endTime
	 *            the new end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the issued time.
	 * 
	 * @return the issued time
	 */
	public Date getIssuedTime() {
		return issuedTime;
	}

	/**
	 * Sets the issued time.
	 * 
	 * @param issuedTime
	 *            the new issued time
	 */
	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}

	/**
	 * Gets the program name.
	 * 
	 * @return the program name
	 */
	public String getProgramName() {
		return programName;
	}

	/**
	 * Sets the program name.
	 * 
	 * @param programName
	 *            the new program name
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	/**
	 * Gets the singal entries.
	 * 
	 * @return the singal entries
	 */

	public Set<EventTemplateSignalEntry> getSingalEntries() {
		return singalEntries;
	}

	/**
	 * Sets the singal entries.
	 * 
	 * @param singalEntries
	 *            the new singal entries
	 */
	public void setSingalEntries(Set<EventTemplateSignalEntry> singalEntries) {
		this.singalEntries = singalEntries;
	}
}