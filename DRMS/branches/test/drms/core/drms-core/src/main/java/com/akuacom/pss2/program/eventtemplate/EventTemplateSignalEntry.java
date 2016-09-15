/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.beans.EventTemplateSignalEntry.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.program.eventtemplate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class EventTemplateSignalEntry.
 */
@Entity
@Table(name = "event_template_signalentry")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class EventTemplateSignalEntry extends VersionedEntity {

	private static final long serialVersionUID = -6064166545825573922L;

	/** The signal type. */
	private String signalType;

	/** The value. */
	private String value;

	/** The relative start time. */
	private long relativeStartTime;

	/** The event template. */
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private EventTemplate eventTemplate;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the signal type.
	 * 
	 * @return the signal type
	 */
	public String getSignalType() {
		return signalType;
	}

	/**
	 * Sets the signal type.
	 * 
	 * @param signalType
	 *            the new signal type
	 */
	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the relative start time.
	 * 
	 * @return the relative start time
	 */
	public long getRelativeStartTime() {
		return relativeStartTime;
	}

	/**
	 * Sets the relative start time.
	 * 
	 * @param relativeStartTime
	 *            the new relative start time
	 */
	public void setRelativeStartTime(long relativeStartTime) {
		this.relativeStartTime = relativeStartTime;
	}

	/**
	 * Gets the event template.
	 * 
	 * @return the event template
	 */
	public EventTemplate getEventTemplate() {
		return eventTemplate;
	}

	/**
	 * Sets the event template.
	 * 
	 * @param eventTemplate
	 *            the new event template
	 */
	public void setEventTemplate(EventTemplate eventTemplate) {
		this.eventTemplate = eventTemplate;
	}
}