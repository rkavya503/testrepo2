/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.report.entities.Account.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class Account.
 */
public class Account implements Serializable {
    
    /** The participant name. */
    private String participantName;

    /** The account number. */
    private String accountNumber;
    
    /** The sub account. */
    private String subAccount;
    
    /** The start date. */
    private Date startDate;
    
    /** The end data. */
    private Date endDate;
    
    /** The program names. */
    private String programNames;

    private String premiseNumber;

    private String active;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the sub account.
     * 
     * @return the sub account
     */
    public String getSubAccount() {
        return subAccount;
    }

    /**
     * Sets the sub account.
     * 
     * @param subAccount the new sub account
     */
    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    /**
     * Gets the start date.
     * 
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     * 
     * @param startDate the new start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end data.
     * 
     * @return the end data
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end data.
     * 
     * @param endDate the new end data
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the program names.
     * 
     * @return the program names
     */
    public String getProgramNames() {
        return programNames;
    }

    /**
     * Sets the program names.
     * 
     * @param programNames the new program names
     */
    public void setProgramNames(String programNames) {
        this.programNames = programNames;
    }

    public String getPremiseNumber() {
        return premiseNumber;
    }

    public void setPremiseNumber(String premiseNumber) {
        this.premiseNumber = premiseNumber;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
