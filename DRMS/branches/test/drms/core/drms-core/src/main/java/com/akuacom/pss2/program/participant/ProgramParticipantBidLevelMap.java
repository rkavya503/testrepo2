/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.BidLevelMappingEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.participant.Participant;

/**
 * The Class ProgramParticipantBidLevelMap.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "bid_level_mapping")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
    @NamedQuery(name = "ProgramParticipantBidLevelMap.deleteByProgramNameAndParticipantNameAndClient",
        query = "delete from ProgramParticipantBidLevelMap b where b.programName = :programName and b.participant = ( select p from Participant p where p.participantName = :participantName and p.client = :client)",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "ProgramParticipantBidLevelMap.findByProgramNameAndParticipantNameAndClient",
        query = "select b from ProgramParticipantBidLevelMap b where b.programName = :programName and b.participant.participantName = :participantName and b.participant.client = :client",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class ProgramParticipantBidLevelMap extends BaseEntity {
	
	private static final long serialVersionUID = 4328514091654080036L;

	/** The program name. */
    private String programName;
    
    @ManyToOne
    @JoinColumn(name = "participant_uuid")
    private Participant participant;
    
    /** The time block. */
    private String timeBlock;
    
    /** The normal. */
    private String normal;
    
    /** The moderate. */
    private String moderate;
    
    /** The high. */
    private String high;

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
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public String getParticipantName() {
        return participant.getParticipantName();
    }

    /**
     * Gets the time block.
     * 
     * @return the time block
     */
    public String getTimeBlock() {
        return timeBlock;
    }

    /**
     * Sets the time block.
     * 
     * @param timeBlock the new time block
     */
    public void setTimeBlock(String timeBlock) {
        this.timeBlock = timeBlock;
    }

    /**
     * Gets the normal.
     * 
     * @return the normal
     */
    public String getNormal() {
        return normal;
    }

    /**
     * Sets the normal.
     * 
     * @param normal the new normal
     */
    public void setNormal(String normal) {
        this.normal = normal;
    }

    /**
     * Gets the moderate.
     * 
     * @return the moderate
     */
    public String getModerate() {
        return moderate;
    }

    /**
     * Sets the moderate.
     * 
     * @param moderate the new moderate
     */
    public void setModerate(String moderate) {
        this.moderate = moderate;
    }

    /**
     * Gets the high.
     * 
     * @return the high
     */
    public String getHigh() {
        return high;
    }

    /**
     * Sets the high.
     * 
     * @param high the new high
     */
    public void setHigh(String high) {
        this.high = high;
    }
}
