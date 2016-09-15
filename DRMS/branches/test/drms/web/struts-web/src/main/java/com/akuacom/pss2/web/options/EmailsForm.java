/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.options.EmailsForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.options;

import com.akuacom.pss2.email.MessageEntity;

import org.apache.struts.action.ActionForm;

import java.util.List;

/**
 * The Class EmailsForm.
 */
public class EmailsForm extends ActionForm {
    
    /** The message id. */
    private String messageId;
    
    /** The emails. */
    private List<MessageEntity> emails;
    
    /** The email.*/
    private MessageEntity email;
    
    
    /** The start date. */
    private String startDate;
    
    /** The end date. */
    private String endDate;

    
	/**
     * Gets the message id.
     * 
     * @return the message id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the message id.
     * 
     * @param messageId the new message id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Gets the emails.
     * 
     * @return the emails
     */
    public List<MessageEntity> getEmails() {
        return emails;
    }

    /**
     * Sets the emails.
     * 
     * @param emails the new emails
     */
    public void setEmails(List<MessageEntity> emails) {
        this.emails = emails;
    }
    
    public MessageEntity getEmail() {
		return email;
	}

	public void setEmail(MessageEntity email) {
		this.email = email;
	}
	
    /**
     * Gets the end date.
     * 
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     * 
     * @param endDate the new end date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the start date.
     * 
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     * 
     * @param startDate the new start date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }	
}
