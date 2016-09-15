/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.report.entities.OfflineRecord.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class OfflineRecord.
 */
public class OfflineRecord implements Serializable {
    
    /** The participant name. */
    private String participantName;
    
    /** The account id. */
    private String accountId;
    
    /** The type. */
    private String type;
    
    /** The count. */
    private int count;
    
    /** The status. */
    private String status;
    
    /** The last contact. */
    private Date lastContact;

    /**
     * Gets the account id.
     * 
     * @return the account id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the account id.
     * 
     * @param accountId the new account id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Gets the count.
     * 
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the count.
     * 
     * @param count the new count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Gets the last contact.
     * 
     * @return the last contact
     */
    public Date getLastContact() {
        return lastContact;
    }

    /**
     * Sets the last contact.
     * 
     * @param lastContact the new last contact
     */
    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
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
     * Gets the status.
     * 
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     * 
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }
}
