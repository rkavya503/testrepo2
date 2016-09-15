/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ParticipantManualSignalEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.client;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.participant.ParticipantESPerf;

/**
 * ESPerf family designed to optimize client polling for Event State
 */
@Entity
@Table(name = "participant_manualsignal")
public class ClientManualSignalESPerf extends BaseEntity implements Comparable<ClientManualSignalESPerf> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6448761469386004430L;

	/** The name. */
    private String name;
    
    /** The value. */
    private String value;
    
    /** The participant. */
    @ManyToOne
    @JoinColumn(name = "participant_uuid")
    private ParticipantESPerf participant;
    
    public ParticipantESPerf getParticipant() {
		return participant;
	}

	public void setParticipant(ParticipantESPerf participant) {
		this.participant = participant;
	}

	/**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
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
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public int compareTo(ClientManualSignalESPerf o) {
        if (o.getUUID() == null) {
            return 1;
        } else if (this.getUUID() == null) {
            return -1;
        } else {
            return this.getUUID().compareTo(o.getUUID());
        }
    }
}
