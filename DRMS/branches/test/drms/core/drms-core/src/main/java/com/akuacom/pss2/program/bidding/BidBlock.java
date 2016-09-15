/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.BidBlockEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.bidding;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class BidBlockDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "bid_block")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class BidBlock extends VersionedEntity {

    private static final long serialVersionUID = -7411574304165342591L;

    /** The Constant REFERENCE_YEAR. */
    private static final int REFERENCE_YEAR = 2000;

    /** The Constant REFERENCE_MONTH. */
    private static final int REFERENCE_MONTH = 1;

    /** The Constant REFERENCE_DAY. */
    private static final int REFERENCE_DAY = 1;

    /** The start time h. */
    private int startTimeH;

    /** The start time m. */
    private int startTimeM;

    /** The end time h. */
    private int endTimeH;

    /** The end time m. */
    private int endTimeM;

    /** The bid config. */
    @ManyToOne
    @JoinColumn(name = "bid_config_uuid")
    private BidConfig bidConfig;

    /**
     * Gets the start time h.
     * 
     * @return the start time h
     */
    public int getStartTimeH() {
        return startTimeH;
    }

    /**
     * Sets the start time h.
     * 
     * @param startTimeH
     *            the new start time h
     */
    public void setStartTimeH(int startTimeH) {
        this.startTimeH = startTimeH;
    }

    /**
     * Gets the start time m.
     * 
     * @return the start time m
     */
    public int getStartTimeM() {
        return startTimeM;
    }

    /**
     * Sets the start time m.
     * 
     * @param startTimeM
     *            the new start time m
     */
    public void setStartTimeM(int startTimeM) {
        this.startTimeM = startTimeM;
    }

    /**
     * Gets the end time h.
     * 
     * @return the end time h
     */
    public int getEndTimeH() {
        return endTimeH;
    }

    /**
     * Sets the end time h.
     * 
     * @param endTimeH
     *            the new end time h
     */
    public void setEndTimeH(int endTimeH) {
        this.endTimeH = endTimeH;
    }

    /**
     * Gets the end time m.
     * 
     * @return the end time m
     */
    public int getEndTimeM() {
        return endTimeM;
    }

    /**
     * Sets the end time m.
     * 
     * @param endTimeM
     *            the new end time m
     */
    public void setEndTimeM(int endTimeM) {
        this.endTimeM = endTimeM;
    }

    /**
     * Gets the bid config.
     * 
     * @return the bid config
     */
    public BidConfig getBidConfig() {
        return bidConfig;
    }

    /**
     * Sets the bid config.
     * 
     * @param bidConfig
     *            the new bid config
     */
    public void setBidConfig(BidConfig bidConfig) {
        this.bidConfig = bidConfig;
    }

    /**
     * Gets the start reference time.
     * 
     * @return the start reference time
     */
    public Date getStartReferenceTime() {
        return referenceTime(startTimeH, startTimeM, 0);
    }

    /**
     * Gets the end reference time.
     * 
     * @return the end reference time
     */
    public Date getEndReferenceTime() {
        return referenceTime(endTimeH, endTimeM, 0);
    }

    private Date referenceTime(int hour, int min, int sec) {
        return new GregorianCalendar(REFERENCE_YEAR, REFERENCE_MONTH,
                REFERENCE_DAY, hour, min, 0).getTime();
    }

    public BidBlock copy(BidBlock existing, BidConfig parent) {
        BidBlock cloned = new BidBlock();
        cloned.setBidConfig(parent);
        cloned.setEndTimeH(existing.getEndTimeH());
        cloned.setEndTimeM(existing.getEndTimeM());
        cloned.setStartTimeH(existing.getStartTimeH());
        cloned.setStartTimeM(existing.getStartTimeM());
        // cloned.setModifiedTime(null);
        cloned.setModifier(null);
        return cloned;
    }
}
