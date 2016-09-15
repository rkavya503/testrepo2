/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.RTPShedStrategy.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.akuacom.pss2.util.LogUtils;

/**
 * The Class RTPShedStrategy.
 */
public class RTPShedStrategy implements Comparable<RTPShedStrategy>,
        Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(RTPShedStrategy.class);

    /**
     * The Enum Type.
     */
    public enum Type {
        SIMPLE, ADVANCED
    };

    /** The name. */
    private String name;

    /** The type. */
    private Type type;

    /** The summer active. */
    private boolean summerActive;

    /** The winter active. */
    private boolean winterActive;

    /** The weekend active. */
    private boolean weekendActive;

    /** The simple entries. */
    private RTPShedStrategyEntry[] simpleEntries;

    /** The advanced entries. */
    private RTPShedStrategyEntry[] advancedEntries;

    /**
     * Instantiates a new rTP shed strategy.
     */
    public RTPShedStrategy() {
        try {
            type = Type.SIMPLE;
            advancedEntries = new RTPShedStrategyEntry[24];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "H:mm.ss.SSS");
            for (int i = 0; i < 24; i++) {
                advancedEntries[i] = new RTPShedStrategyEntry();
                advancedEntries[i].setStartTime(simpleDateFormat.parse(i
                        + ":00.0.000"));
                advancedEntries[i].setEndTime(simpleDateFormat.parse(i
                        + ":59.59.999"));
                advancedEntries[i].setModeratePrice("");
                advancedEntries[i].setHighPrice("");
            }
            simpleEntries = new RTPShedStrategyEntry[1];
            simpleEntries[0] = new RTPShedStrategyEntry();
            simpleEntries[0].setStartTime(simpleDateFormat.parse("0:00.0.000"));
            simpleEntries[0].setEndTime(simpleDateFormat.parse("23:59.59.999"));
            simpleEntries[0].setModeratePrice("");
            simpleEntries[0].setHighPrice("");
        } catch (ParseException e) {
            log.error(LogUtils.createLogEntry("", LogUtils.CATAGORY_EVENT,
                    "Failed to create RTP Shed Strategy", null));
            log.debug(LogUtils.createExceptionLogEntry("",
                    LogUtils.CATAGORY_EVENT, e));
        }
    }

    // TODO: for comparison purposes only
    /**
     * Instantiates a new rTP shed strategy.
     * 
     * @param name
     *            the name
     */
    public RTPShedStrategy(String name) {
        this.name = name;
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
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the new type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Checks if is summer active.
     * 
     * @return true, if is summer active
     */
    public boolean isSummerActive() {
        return summerActive;
    }

    /**
     * Sets the summer active.
     * 
     * @param summerActive
     *            the new summer active
     */
    public void setSummerActive(boolean summerActive) {
        this.summerActive = summerActive;
    }

    /**
     * Checks if is winter active.
     * 
     * @return true, if is winter active
     */
    public boolean isWinterActive() {
        return winterActive;
    }

    /**
     * Sets the winter active.
     * 
     * @param winterActive
     *            the new winter active
     */
    public void setWinterActive(boolean winterActive) {
        this.winterActive = winterActive;
    }

    /**
     * Checks if is weekend active.
     * 
     * @return true, if is weekend active
     */
    public boolean isWeekendActive() {
        return weekendActive;
    }

    /**
     * Sets the weekend active.
     * 
     * @param weekendActive
     *            the new weekend active
     */
    public void setWeekendActive(boolean weekendActive) {
        this.weekendActive = weekendActive;
    }

    public RTPShedStrategyEntry[] getSimpleEntries() {
        return simpleEntries;
    }

    public void setSimpleEntries(RTPShedStrategyEntry[] simpleEntries) {
        this.simpleEntries = simpleEntries;
    }

    public RTPShedStrategyEntry[] getAdvancedEntries() {
        return advancedEntries;
    }

    public void setAdvancedEntries(RTPShedStrategyEntry[] advancedEntries) {
        this.advancedEntries = advancedEntries;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */

    public int compareTo(RTPShedStrategy other) {
        return name.compareTo(other.name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final RTPShedStrategy other = (RTPShedStrategy) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
