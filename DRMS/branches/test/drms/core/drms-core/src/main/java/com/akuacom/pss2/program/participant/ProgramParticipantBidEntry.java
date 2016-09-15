/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.participant.ProgramParticipantBidEntryEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class ProgramParticipantBidEntryDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program_participant_bid_entry")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class ProgramParticipantBidEntry extends VersionedEntity
{
    
	private static final long serialVersionUID = -7630988845211922068L;

	/** The block start. */
    private Date blockStart;
    
    /** The block end. */
    private Date blockEnd;
    
    /** The reduction kw. */
    private double reductionKW;
    
    /** The price level. */
    private double priceLevel;
    
    /** The active. */
    private boolean active = true;
    
    /** The program participant. */
    @ManyToOne
    @JoinColumn(name = "program_participant_uuid")
    private ProgramParticipant programParticipant;

    /**
     * Gets the block start.
     * 
     * @return the block start
     */
    public Date getBlockStart() {
        return blockStart;
    }

    /**
     * Sets the block start.
     * 
     * @param blockStart the new block start
     */
    public void setBlockStart(Date blockStart) {
        this.blockStart = blockStart;
    }

    /**
     * Gets the block end.
     * 
     * @return the block end
     */
    public Date getBlockEnd() {
        return blockEnd;
    }

    /**
     * Sets the block end.
     * 
     * @param blockEnd the new block end
     */
    public void setBlockEnd(Date blockEnd) {
        this.blockEnd = blockEnd;
    }

    /**
     * Gets the reduction kw.
     * 
     * @return the reduction kw
     */
    public double getReductionKW() {
        return reductionKW;
    }

    /**
     * Sets the reduction kw.
     * 
     * @param reductionKW the new reduction kw
     */
    public void setReductionKW(double reductionKW) {
        this.reductionKW = reductionKW;
    }

    /**
     * Gets the price level.
     * 
     * @return the price level
     */
    public double getPriceLevel() {
        return priceLevel;
    }

    /**
     * Sets the price level.
     * 
     * @param priceLevel the new price level
     */
    public void setPriceLevel(double priceLevel) {
        this.priceLevel = priceLevel;
    }

    /**
     * Checks if is active.
     * 
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     * 
     * @param active the new active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

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
     * @param programParticipant the new program participant
     */
    public void setProgramParticipant(ProgramParticipant programParticipant) {
        this.programParticipant = programParticipant;
    }
}
