/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ParticipantManualSignalEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.participant.Participant;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "participant_shed_entry")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class ParticipantShedEntry extends BaseEntity implements Comparable<ParticipantShedEntry> {

	private static final long serialVersionUID = 5108941925631858084L;

	/** The hour index 0-23. */
    private int hourIndex;
    
    /** The value. */
    private double value;
    
    /** The participant. */
    @ManyToOne
    @JoinColumn(name = "participant_uuid")
    private Participant participant;

    
    @Override
    public int compareTo(ParticipantShedEntry o) {
        if (o.getUUID() == null) {
            return 1;
        } else if (this.getUUID() == null) {
            return -1;
        } else {
            return this.getUUID().compareTo(o.getUUID());
        }
    }


	/**
	 * @return the hourIndex
	 */
	public int getHourIndex() {
		return hourIndex;
	}


	/**
	 * @param hourIndex the hourIndex to set
	 */
	public void setHourIndex(int hourIndex) {
		this.hourIndex = hourIndex;
	}


	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}


	/**
	 * @return the participant
	 */
	public Participant getParticipant() {
		return participant;
	}


	/**
	 * @param participant the participant to set
	 */
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

}
