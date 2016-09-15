/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.dbp.CurrentBidVO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.richsite.event.dbp;

import java.util.List;

/**
 * The Class CurrentBidVO.
 */
public class CurrentBidVO {
    
    /** The account number. */
    private String accountNumber;
    
    /** The participant name. */
    private String participantName;
    
    /** The reductions. */
    private List<Double> reductions;
    
    /** The bid status. */
    private String bidStatus;

    /**
     * Gets the bid status.
     * 
     * @return the bid status
     */
    public String getBidStatus() {
        return bidStatus;
    }

    /**
     * Sets the bid status.
     * 
     * @param bidStatus the new bid status
     */
    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    /**
     * Gets the account number.
     * 
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number.
     * 
     * @param accountNumber the new account number
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
     * @param participantName the new participant name
     */
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    /**
     * Gets the reductions.
     * 
     * @return the reductions
     */
    public List<Double> getReductions() {
        return reductions;
    }

    /**
     * Sets the reductions.
     * 
     * @param reductions the new reductions
     */
    public void setReductions(List<Double> reductions) {
        this.reductions = reductions;
    }
}
