/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.BidConfigEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.bidding;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.program.Program;

/**
 * The Class BidConfigDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "bid_config")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class BidConfig extends VersionedEntity {

    private static final long serialVersionUID = -4717269435498527211L;

    /** The min bid kw. */
    private double minBidKW;

    /** The default bid kw. */
    private double defaultBidKW;

    /** The min consectutive blocks. */
    private int minConsectutiveBlocks;

    /** The respond by time h. */
    private int respondByTimeH;

    /** The respond by time m. */
    private int respondByTimeM;

    /** The dras respond by period m. */
    private int drasRespondByPeriodM;

    /** The accept timeout period m. */
    private int acceptTimeoutPeriodM;

    /** The dras bidding. */
    private boolean drasBidding;

    /** The bid blocks. */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "bidConfig")
    @OrderBy("startTimeH, startTimeM")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.REMOVE,
            org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<BidBlock> bidBlocks;

    /** The program attribute. */
    //@OneToOne to achieve lazy load for hibernate one-to-one mapping
    @ManyToOne 
    @JoinColumn(name = "program_uuid")
    private Program program;

    /**
     * Gets the min bid kw.
     * 
     * @return the min bid kw
     */
    public double getMinBidKW() {
        return minBidKW;
    }

    /**
     * Sets the min bid kw.
     * 
     * @param minBidKW
     *            the new min bid kw
     */
    public void setMinBidKW(double minBidKW) {
        this.minBidKW = minBidKW;
    }

    /**
     * Gets the default bid kw.
     * 
     * @return the default bid kw
     */
    public double getDefaultBidKW() {
        return defaultBidKW;
    }

    /**
     * Sets the default bid kw.
     * 
     * @param defaultBidKW
     *            the new default bid kw
     */
    public void setDefaultBidKW(double defaultBidKW) {
        this.defaultBidKW = defaultBidKW;
    }

    /**
     * Gets the min consectutive blocks.
     * 
     * @return the min consectutive blocks
     */
    public int getMinConsectutiveBlocks() {
        return minConsectutiveBlocks;
    }

    /**
     * Sets the min consectutive blocks.
     * 
     * @param minConsectutiveBlocks
     *            the new min consectutive blocks
     */
    public void setMinConsectutiveBlocks(int minConsectutiveBlocks) {
        this.minConsectutiveBlocks = minConsectutiveBlocks;
    }

    /**
     * Checks if is dras bidding.
     * 
     * @return true, if is dras bidding
     */
    public boolean isDrasBidding() {
        return drasBidding;
    }

    /**
     * Sets the dras bidding.
     * 
     * @param drasBidding
     *            the new dras bidding
     */
    public void setDrasBidding(boolean drasBidding) {
        this.drasBidding = drasBidding;
    }

    /**
     * Gets the respond by time h.
     * 
     * @return the respond by time h
     */
    public int getRespondByTimeH() {
        return respondByTimeH;
    }

    /**
     * Sets the respond by time h.
     * 
     * @param respondByTimeH
     *            the new respond by time h
     */
    public void setRespondByTimeH(int respondByTimeH) {
        this.respondByTimeH = respondByTimeH;
    }

    /**
     * Gets the respond by time m.
     * 
     * @return the respond by time m
     */
    public int getRespondByTimeM() {
        return respondByTimeM;
    }

    /**
     * Sets the respond by time m.
     * 
     * @param respondByTimeM
     *            the new respond by time m
     */
    public void setRespondByTimeM(int respondByTimeM) {
        this.respondByTimeM = respondByTimeM;
    }

    /**
     * Gets the dras respond by period m.
     * 
     * @return the dras respond by period m
     */
    public int getDrasRespondByPeriodM() {
        return drasRespondByPeriodM;
    }

    /**
     * Sets the dras respond by period m.
     * 
     * @param drasRespondByPeriodM
     *            the new dras respond by period m
     */
    public void setDrasRespondByPeriodM(int drasRespondByPeriodM) {
        this.drasRespondByPeriodM = drasRespondByPeriodM;
    }

    /**
     * Gets the accept timeout period m.
     * 
     * @return the accept timeout period m
     */
    public int getAcceptTimeoutPeriodM() {
        return acceptTimeoutPeriodM;
    }

    /**
     * Sets the accept timeout period m.
     * 
     * @param acceptTimeoutPeriodM
     *            the new accept timeout period m
     */
    public void setAcceptTimeoutPeriodM(int acceptTimeoutPeriodM) {
        this.acceptTimeoutPeriodM = acceptTimeoutPeriodM;
    }

    /**
     * Gets the bid blocks.
     * 
     * @return the bid blocks
     */
    public Set<BidBlock> getBidBlocks() {
        return bidBlocks;
    }
    
    /**
     * Sets the bid blocks.
     * 
     * @param bidBlocks
     *            the new bid blocks
     */
    public void setBidBlocks(Set<BidBlock> bidBlocks) {
        this.bidBlocks = bidBlocks;
    }

    /**
     * Gets the program .
     * 
     * @return the program
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Sets the program attribute.
     * 
     * @param program
     *            the new program attribute
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    public BidConfig copy(BidConfig existing, Program parent) {
        BidConfig cloned = new BidConfig();
        cloned.setAcceptTimeoutPeriodM(existing.getAcceptTimeoutPeriodM());
        cloned.setDefaultBidKW(existing.getDefaultBidKW());
        cloned.setDrasBidding(existing.isDrasBidding());
        cloned.setDrasRespondByPeriodM(existing.getDrasRespondByPeriodM());
        cloned.setMinBidKW(existing.getMinBidKW());
        cloned.setMinConsectutiveBlocks(existing.getMinConsectutiveBlocks());
        cloned.setProgram(parent);
        cloned.setRespondByTimeH(existing.getRespondByTimeH());
        cloned.setRespondByTimeM(existing.getRespondByTimeM());
        // cloned.setModifiedTime(null);
        cloned.setModifier(null);

        if (existing.getBidBlocks() == null) {
            return cloned;
        }
        Set<BidBlock> newList = new HashSet<BidBlock>();
        Set<BidBlock> list = existing.getBidBlocks();
        for (BidBlock entry : list) {
            BidBlock newOne = entry.copy(entry, cloned);
            newList.add(newOne);
        }
        cloned.setBidBlocks(newList);
        return cloned;
    }
}
