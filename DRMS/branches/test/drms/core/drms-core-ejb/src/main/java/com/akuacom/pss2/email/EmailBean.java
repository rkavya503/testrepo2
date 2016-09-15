/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.EmailBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.Depends;

/**
 * The Class EmailBean.
 */
@TransactionManagement
@MessageDriven(name = "EmailMDB", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
@Resources(@Resource(name = "jms/QueueFactory", type = QueueConnectionFactory.class))
@Depends("jboss.messaging.destination:service=Queue,name=pss2email")
public class EmailBean implements MessageListener {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(EmailBean.class);
    
    @EJB
    private MessageDispatcher.L messageDispatcher;
    
    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    /**
     * all Messages should fall though this channel no matter whether the message is 
     * sent at first time or resent manually by the operator
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void onMessage(javax.jms.Message message) {
        if (message instanceof ObjectMessage) {
            final ObjectMessage obj = (ObjectMessage) message;
            try {
                final Serializable object = obj.getObject();
                if (object instanceof MessageEntity) {
                    final MessageEntity vo = (MessageEntity) object;
                    
                    //Do nothing, just forward request to dispatcher
                    messageDispatcher.dispatchMessage(vo);
                }
            } catch (JMSException e) {
                log.error("error in consuming message", e);
            }
        }
    }
    
}
