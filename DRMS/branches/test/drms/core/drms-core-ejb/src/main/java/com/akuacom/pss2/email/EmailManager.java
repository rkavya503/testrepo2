/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.EmailManagerLocal.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * The Remote Interface EmailManager.
 */
/**
 * @author e218290
 *
 */
/**
 * @author e218290
 * 
 */
public interface EmailManager {
    @Remote
    public interface R extends EmailManager {}
    @Local
    public interface L extends EmailManager {}

    /**
     * Gets the messages.
     * 
     * @param userName
     *            the user name
     * 
     * @return the messages
     */
    List<MessageEntity> getMessages(String userName);

    /**
     * Gets the all messages.
     * 
     * @return the all messages
     */
    List<MessageEntity> getAllMessages();

    /**
     * Send message.
     * 
     * @param id
     *            the id
     */
    void sendMessage(String id);

    /**
     * Get message by uuid
     * 
     * @return MessageEntity
     */
    MessageEntity getMessage(String uuid);

    /**
     * Get message by send time
     * 
     * @return the messages
     */
    List<MessageEntity> getMessages(Date start, Date end);
    
    /**
     * Get message by subject
     * 
     * @return the messages
     */
    List<MessageEntity> getMessageBySubject(String subject);
    

}
