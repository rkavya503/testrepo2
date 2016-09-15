/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.EnvoyBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;

import com.kanaeki.firelog.util.FireLogEntry;
import org.apache.log4j.Logger;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.pss2.util.LogUtils;
import org.jboss.ejb3.annotation.Depends;

/**
 * The Class EnvoyBean.
 */
@TransactionManagement
@MessageDriven(name = "EnvoyEJB", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
@Resources(@Resource(name = "jms/QueueFactory", type = QueueConnectionFactory.class))
@Depends("jboss.messaging.destination:service=Queue,name=pss2envoy")
public class EnvoyBean implements MessageListener {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger("envoy");

    /** The em. */
    @EJB MessageEAO.L eao;

    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(javax.jms.Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage obj = (ObjectMessage) message;
            try {
				FireLogEntry logEntry = new FireLogEntry();
                final Serializable serializable = obj.getObject();
                if (serializable instanceof MessageEntity) {
                    MessageEntity vo = (MessageEntity)serializable;
                    String text = vo.getContent();
                    logEntry.setDescription("enovy request");
                    logEntry.setLongDescr(text);
                    log.debug(logEntry);
                    boolean success = false;
                    for (int i = 0; !success && i < 3; i++) {
                        try {
                            success = sendEnvoyMessage(text, vo.getType());
                        } catch (IOException e) {
                            log.info(e.getMessage(), e);
                            // TODO 2992
                        	log.error(LogUtils.createLogEntry(vo.getProgramName(), LogUtils.CATAGORY_NOTIFICATION, 
                        			"failed to send envoy message", vo.toString()));
                        }
                    }
                    if (!success) {
                        try {
                            eao.create(vo);
                        } catch (DuplicateKeyException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            } catch (JMSException e) {
                // TODO 2992
            	log.error(LogUtils.createLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, 
            			"error in consuming message", null));
            }
        }
    }

    /**
     * Send envoy message.
     * 
     * @param text the text
     * @param domain this parameter defines which client of envoy/varolii is. default to PGE
     * @return true, if successful
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public boolean sendEnvoyMessage(String text, String domain) throws IOException {
        boolean result;
        final MBOemClient client = VaroliiClientFactory.getVaroliiClient();
        client.setXmlString(text);
        final String response = client.sendRequest();
        if (response.contains("<Errors>0</Errors>")) {
            FireLogEntry logEntrySuccess = new FireLogEntry();
            logEntrySuccess.setDescription("envoy response");
            logEntrySuccess.setLongDescr(response);
            log.debug(logEntrySuccess);
            result = true;
        } else {
            FireLogEntry logEntryFailure = new FireLogEntry();
            logEntryFailure.setDescription("failed to send envoy message");
            logEntryFailure.setLongDescr(response);
            log.error(logEntryFailure);
            result = false;
        }
        return result;
    }

}
