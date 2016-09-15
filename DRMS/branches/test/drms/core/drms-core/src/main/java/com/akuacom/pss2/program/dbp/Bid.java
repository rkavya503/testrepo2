/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.Bid.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.io.Serializable;
import java.util.List;


/**
 * The Class Bid.
 */
public class Bid implements Serializable {
    
    /** The program name. */
    private String programName;
    
    /** The event name. */
    private String eventName;
    
    /** The account name. */
    private String accountName;
    
    /** The bid entries. */
    private List<BidEntry> bidEntries;

    /**
     * Instantiates a new bid.
     */
    public Bid() {
        super();
    }

    /**
     * Instantiates a new bid.
     * 
     * @param programName the program name
     * @param eventName the event name
     * @param accountName the account name
     * @param bidEntries the bid entries
     */
    public Bid(String programName, String eventName, String accountName,
               List<BidEntry> bidEntries) {
        super();
        this.programName = programName;
        this.eventName = eventName;
        this.accountName = accountName;
        this.bidEntries = bidEntries;
    }

    /**
     * Gets the bid entries.
     * 
     * @return the bid entries
     */
    public List<BidEntry> getBidEntries() {
        return bidEntries;
    }

    /**
     * Sets the bid entries.
     * 
     * @param bidEntries the new bid entries
     */
    public void setBidEntries(List<BidEntry> bidEntries) {
        this.bidEntries = bidEntries;
    }

    /**
     * Gets the event name.
     * 
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the event name.
     * 
     * @param eventName the new event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the account name.
     * 
     * @return the account name
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Sets the account name.
     * 
     * @param accountName the new account name
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

}
