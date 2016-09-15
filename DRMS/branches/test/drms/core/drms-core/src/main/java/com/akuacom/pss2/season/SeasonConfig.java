/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.season.SeasonConfig.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.season;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class SeasonConfig.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "season_config")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries(
		{ @NamedQuery(name = "SeasonConfig.findAllByDate", query = "SELECT c FROM SeasonConfig c WHERE c.programVersionUuid = :pvUUID AND c.startDate <= :date1 and c.endDate >= :date2 ", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }) ,
		  @NamedQuery(name = "SeasonConfig.findHolidays",  query = "SELECT  DISTINCT c.startDate,c.endDate from  SeasonConfig c  where c.name like 'WEEKEND/HOLIDAY%' ", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") })
		}
)

public class SeasonConfig extends VersionedEntity {

	private static final long serialVersionUID = -6234882075403201710L;

	// TODO: these should be an enumeration
	/** The SUMME r_ season. */
	public static final String SUMMER_SEASON = "SUMMER";

	/** The WINTE r_ season. */
	public static final String WINTER_SEASON = "WINTER";

	/** The WEEKEN d_ season. */
	public static final String WEEKEND_SEASON = "WEEKEND";

	private String name;

	/** The start date. */
	@Temporal(TemporalType.DATE)
	private Date startDate;

	/** The end date. */
	@Temporal(TemporalType.DATE)
	private Date endDate;

	/** The program version. */
	// See DRMS-2217
	@Column(name = "program_uuid")
	private String programVersionUuid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the start date.
	 * 
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate
	 *            the new start date
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 * 
	 * @return the end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 * 
	 * @param endDate
	 *            the new end date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProgramVersionUuid() {
		return programVersionUuid;
	}

	public void setProgramVersionUuid(String programVersionUuid) {
		this.programVersionUuid = programVersionUuid;
	}
}