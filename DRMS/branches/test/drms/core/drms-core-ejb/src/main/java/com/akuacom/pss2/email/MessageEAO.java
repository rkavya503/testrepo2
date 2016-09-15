/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.email;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.pss2.contact.Contact;


public interface MessageEAO extends BaseEAO<MessageEntity> {
    @Remote
    public interface R extends MessageEAO {}
    @Local
    public interface L extends MessageEAO {}

    /**
     * Get all messages belonging to the the user
     * 
     * @param userName
     *            who owns the message
     * @return the message List
     */
    List<MessageEntity> findByUserName(String userName);

    /**
     * Get all messages to certain destination
     * 
     * @param recipient
     *            the destination of message
     */
    List<MessageEntity> findByDestination(String recipient);

    /**
     * Get count of the all messages which have been sent to same destination in
     * a time frame from a point in the past till now and priority of which
     * equals a certain value
     * 
     * @param recipient
     *            the destination of the message
     * @param priority
     *            the priority
     * @param from
     *            the time point in the past
     * @return the total message count
     */
    long getMessageCount(String recipient, int priority, Date from);

     
    /**
     * Get count of the all messages which have been sent to same destination and participant 
     * in a time frame from a point in the past till now and priority of which
     * equals a certain value
     * <ul>NOTE</ul> same destination (email address) may belong to different participant contact
     * 
     * @param recipient
     *            the destination of the message
     * @param priority
     *            the priority
     * @param contactId
     * 			  the participant contact id
     * @param from
     *            the time point in the past
     *            
     * @return the total message count
     */
    long getMessageCountToContact(String recipient, int priority, String contactId,Date from);
    
    /**
     * Get all messages which sent time are between search conditions
     * 
     * @param start
     * @param end
     * @return the message list
     */
    List<MessageEntity> findByDateRange(Date start, Date end);

    /**
     * Log the message into database, and return the logged message and the
     * count of similar messages which has same priority and destination and has
     * been logged successfully from a time in the past. The message itself is
     * counted if it is logged successfully
     * 
     * @param messageEntity
     * @param from
     *            the time point from which messages is counted
     * @return an array, Object[0] is logged MessageEntity,Object[1] count of
     *         message
     */
    Object[] logMessage(MessageEntity messageEntity, Date from)
            throws DuplicateKeyException;

    void clearMessage(Date threshold);

    /**
     * Get all messages 
     * 
     * @param start
     * @param end
     * @return the message list
     */
    List<MessageEntity> findBySubject(String subject);

	/**
	 * @param start
	 * @param end
	 * @param to
	 * @param userName
	 * @return
	 */
    List<MessageEntity> findByDigest(Date start, Date end, String to, String userName);
    List<MessageEntity> findDigestMessage(Date start, Date end,List<String> operators);
}
