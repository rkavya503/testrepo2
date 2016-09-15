/*
 * www.akuacom.com Automating Demand Response
 *
 * com.akuacom.pss2.partdata.PartData.java - Copyright 1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.drw.core;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Class PartData.
 */
@Entity
@Table(name = "location_kml")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
    @NamedQuery(name = "LocationKmlEntry.findByLocationNum",
            query = "select de from LocationKmlEntry de where de.number = :locationNum")
})

public class LocationKmlEntry extends AbstractApplicationEntity {

	private static final long serialVersionUID = 5780873407572093227L;

    /** The number. */
    private String number;
    
    /** Location type, 0: slap, 1: abank, 2:substation */
    private String locationType;
    // ALTER TABLE `drwebsite`.`location_kml`     ADD COLUMN `locationType` TINYINT(1) NULL COMMENT 'Location type, 0: slap, 1: abank, 2:substation' AFTER `creationTime`
    /** The kml. */
    @Lob
    @Column(columnDefinition="longtext")
    private String kml;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getKml() {
		return kml;
	}

	public void setKml(String kml) {
		this.kml = kml;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

}