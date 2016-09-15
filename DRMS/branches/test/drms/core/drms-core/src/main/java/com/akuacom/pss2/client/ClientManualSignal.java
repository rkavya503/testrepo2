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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.participant.Participant;

/**
 * The Class ClientManualSignal.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "participant_manualsignal")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class ClientManualSignal extends BaseEntity implements Comparable<ClientManualSignal> {

	private static final long serialVersionUID = 5108941925631858084L;

	/** The name. */
    private String name;
    
    /** The value. */
    private String value;
    
    /** The participant. */
    @ManyToOne
    @JoinColumn(name = "participant_uuid")
    private Participant participant;

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

    /**
     * Gets the participant.
     * 
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * Sets the participant.
     * 
     * @param participant the new participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    @Override
    public int compareTo(ClientManualSignal o) {
        if (o.getUUID() == null) {
            return 1;
        } else if (this.getUUID() == null) {
            return -1;
        } else {
            return this.getUUID().compareTo(o.getUUID());
        }
    }
}
