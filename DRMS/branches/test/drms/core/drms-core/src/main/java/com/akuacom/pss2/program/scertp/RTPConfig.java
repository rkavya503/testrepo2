/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.scertp.RTPConfig.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.program.scertp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.annotations.DubiousColumnDefinition;

/**
 * The Class RTPConfig.
 */
@Entity
//@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program_rtp_config")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries( {
    @NamedQuery(name = "RTPConfig.getRTPConfigName",query = "select distinct p.name from RTPConfig p where p.programVersionUuid = :programUUID and p.seasonName=:seasonName and (p.startTemperature <= :temperature or p.startTemperature=0) and (p.endTemperature > :temperature or p.endTemperature=0)")
})
public class RTPConfig extends VersionedEntity 
{

	private static final long serialVersionUID = -4272183751094046905L;

	/** The start temperature. */
    private double startTemperature;

    /** The end temperature. */
    private double endTemperature;

    /** The start time. */
    @Column(columnDefinition="time")
    @DubiousColumnDefinition("why time instead of datetime?")
    private Date startTime;

    /** The end time. */
    @Column(columnDefinition="time")
    @DubiousColumnDefinition
    private Date endTime;

    /** The rate. */
    private double rate;

    /** The unit. */
    private String unit;

    /** The season name. */
    private String seasonName;

    /** The program version. */
    @Column(name = "program_uuid")
    private String programVersionUuid;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the start temperature.
     *
     * @return the start temperature
     */
    public double getStartTemperature()
    {
        return startTemperature;
    }

    /**
     * Sets the start temperature.
     *
     * @param startTemperature the new start temperature
     */
    public void setStartTemperature(double startTemperature)
    {
        this.startTemperature = startTemperature;
    }

    /**
     * Gets the end temperature.
     *
     * @return the end temperature
     */
    public double getEndTemperature()
    {
        return endTemperature;
    }

    /**
     * Sets the end temperature.
     *
     * @param endTemperature the new end temperature
     */
    public void setEndTemperature(double endTemperature)
    {
        this.endTemperature = endTemperature;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public Date getStartTime()
    {
        return startTime;
    }

    /**
     * Sets the start time.
     *
     * @param startTime the new start time
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public Date getEndTime()
    {
        return endTime;
    }

    /**
     * Sets the end time.
     *
     * @param endTime the new end time
     */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    /**
     * Gets the rate.
     *
     * @return the rate
     */
    public double getRate()
    {
        return rate;
    }

    /**
     * Sets the rate.
     *
     * @param rate the new rate
     */
    public void setRate(double rate)
    {
        this.rate = rate;
    }

    /**
     * Gets the unit.
     *
     * @return the unit
     */
    public String getUnit()
    {
        return unit;
    }

    /**
     * Sets the unit.
     *
     * @param unit the new unit
     */
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    /**
     * Gets the season name.
     *
     * @return the season name
     */
    public String getSeasonName()
    {
        return seasonName;
    }

    /**
     * Sets the season name.
     *
     * @param seasonName the new season name
     */
    public void setSeasonName(String seasonName)
    {
        this.seasonName = seasonName;
    }

	public String getProgramVersionUuid() {
		return programVersionUuid;
	}

	// See DRMS-2217
	public void setProgramVersionUuid(String programVersionUuid) {
		this.programVersionUuid = programVersionUuid;
	}
}