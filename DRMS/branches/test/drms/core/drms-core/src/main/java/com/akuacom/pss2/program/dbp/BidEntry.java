/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.BidEntry.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class BidEntry.
 */
public class BidEntry implements Serializable, Comparable<BidEntry> {
    
    /** The Constant TYPE_DEFAULT. */
    public static final byte TYPE_DEFAULT = 0;
    
    /** The Constant TYPE_CURRENT. */
    public static final byte TYPE_CURRENT = 1;

    //sort ints
    /** The Constant SORT_NAME. */
    public static final int SORT_NAME = 0;
    
    /** The Constant SORT_TYPE. */
    public static final int SORT_TYPE = 1;

    /** The id. */
    private String id;
    
    /** The bid type. */
    private byte bidType = TYPE_DEFAULT;

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

    //	private Participant participant;
    /** The participant name. */
    private String participantName;
    
    /** The program name. */
    private String programName;

    /** The Constant DATE_FORMAT. */
    private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss z";

    /**
     * Instantiates a new bid entry.
     */
    public BidEntry() {
    }

    /**
     * Instantiates a new bid entry.
     * 
     * @param id the id
     * @param start the start
     * @param end the end
     * @param reduceKW the reduce kw
     * @param level the level
     * @param type the type
     * @param accepted the accepted
     */
    @Deprecated
    public BidEntry(String id, Date start, Date end, double reduceKW,
                    double level, byte type, byte accepted) {
        this.id = id;
        bidType = type;
        blockStart = start;
        blockEnd = end;
        reductionKW = reduceKW;
        priceLevel = level;
        this.active = accepted == 1;
    }

    /**
     * Instantiates a new bid entry.
     * 
     * @param id the id
     * @param start the start
     * @param end the end
     * @param reduceKW the reduce kw
     * @param level the level
     * @param pname the pname
     * @param type the type
     * @param accepted the accepted
     * @param progname the progname
     */
    @Deprecated
    public BidEntry(String id, Date start, Date end, double reduceKW,
                    double level, String pname, byte type, byte accepted, String progname) {
        this.id = id;
        bidType = type;
        blockStart = start;
        blockEnd = end;
        reductionKW = reduceKW;
        priceLevel = level;
        participantName = pname;
        programName = progname;
        this.active = accepted == 1;
    }

    /**
     * Instantiates a new bid entry.
     * 
     * @param start the start
     * @param end the end
     * @param reduceKW the reduce kw
     * @param level the level
     * @param type the type
     * @param accepted the accepted
     */
    @Deprecated
    public BidEntry(Date start, Date end, double reduceKW, double level,
                    byte type, byte accepted) {
        blockStart = start;
        blockEnd = end;
        reductionKW = reduceKW;
        priceLevel = level;
        bidType = type;
        this.active = accepted == 1;
    }

    /**
     * Instantiates a new bid entry.
     * 
     * @param start the start
     * @param end the end
     * @param reduceKW the reduce kw
     * @param level the level
     * @param pname the pname
     * @param type the type
     * @param accepted the accepted
     * @param progname the progname
     */
    @Deprecated
    public BidEntry(Date start, Date end, double reduceKW, double level,
                    String pname, byte type, byte accepted, String progname) {
        blockStart = start;
        blockEnd = end;
        reductionKW = reduceKW;
        priceLevel = level;
        participantName = pname;
        bidType = type;
        programName = progname;
        this.active = accepted == 1;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the block start.
     * 
     * @param sdate the new block start
     */
    public void setBlockStart(Date sdate) {
        blockStart = sdate;
    }

    /**
     * Gets the block start.
     * 
     * @return the block start
     */
    public Date getBlockStart() {
        return blockStart;
    }

    /**
     * Sets the block end.
     * 
     * @param edate the new block end
     */
    public void setBlockEnd(Date edate) {
        blockEnd = edate;
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
     * Sets the reduction kw.
     * 
     * @param rkw the new reduction kw
     */
    public void setReductionKW(double rkw) {
        reductionKW = rkw;
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
     * Sets the price level.
     * 
     * @param lvl the new price level
     */
    public void setPriceLevel(double lvl) {
        priceLevel = lvl;
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
     * Gets the participant name.
     * 
     * @return the participant name
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * Sets the participant name.
     * 
     * @param pname the new participant name
     */
    public void setParticipantName(String pname) {
        participantName = pname;
    }

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
     * @param pgname the new program name
     */
    public void setProgramName(String pgname) {
        programName = pgname;
    }

    /**
     * Sets the bid type.
     * 
     * @param type the new bid type
     */
    @Deprecated
    public void setBidType(byte type) {
        bidType = type;
    }

    /**
     * Gets the bid type.
     * 
     * @return the bid type
     */
    @Deprecated
    public byte getBidType() {
        return bidType;
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
     * @param val the new active
     */
    public void setActive(boolean val) {
        active = val;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String rv = "		BidEntry: ";
        rv += ", id = " + id;

        if (bidType == TYPE_CURRENT)
            rv += ", bidType = CURRENT";
        else
            rv += ", bidType = DEFAULT";

        rv += ", blockStart = " +
                new SimpleDateFormat(DATE_FORMAT).format(blockStart);
        rv += ", blockEnd = " +
                new SimpleDateFormat(DATE_FORMAT).format(blockEnd);
        rv += ", kwReduction = " +
                Double.toString(reductionKW);
        rv += ", Shed Level = " +
                Double.toString(priceLevel);
        rv += ", ParticipantName = " + participantName;
        rv += ", ProgramName = " + programName;
        if (active)
            rv += ", active.";
        else
            rv += ", not active";

        rv += "\n";

        return rv;
    }

	@Override
	public int compareTo(BidEntry o)
	{
		return blockStart.compareTo(o.blockStart);
	}
}
