/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.EmailManagerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.util.LogUtils;

/**
 * The Class EmailManagerBean.
 */
@Stateless
public class EmailManagerBean implements EmailManager.R, EmailManager.L {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(EmailManagerBean.class);

    @EJB MessageEAO.L eao;
    
    @EJB Notifier.L notifier;
    /* (non-Javadoc)
     * @see com.akuacom.pss2.email.EmailManagerLocal#getMessages(java.lang.String)
     */
    public List<MessageEntity> getMessages(String userName) {
        return eao.findByUserName(userName);
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.email.EmailManagerLocal#getAllMessages()
     */
    public List<MessageEntity> getAllMessages() {
        return eao.getAll();
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.email.EmailManagerLocal#sendMessage(int)
     */
    public void sendMessage(String id) {
        try {
            final MessageEntity vo = eao.getById(id);
            final String type = vo.getType();
            if ("email".equals(type)) {
                try {
                	notifier.sendMail(vo);
                } catch (Exception e) {
                	log.warn(LogUtils.createLogEntry(vo.getProgramName(), LogUtils.CATAGORY_NOTIFICATION, 
                			"resend email failed", vo.toString()));
                }
            } else {
            	notifier.sendEnvoyMessage(vo.getUserName(), vo.getSubject(), vo.getContent(), false, vo.getTo());
            }
            //eao.delete(vo);
        } catch (EntityNotFoundException e) {
         // TODO 2992
            log.error(LogUtils.createLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, 
        			"exception at sending message", null));
        }
    }
    
    /* (non-Javadoc)
     * @see com.akuacom.pss2.email.EmailManagerLocal#getMessage(java.lang.String)
     */
    public MessageEntity getMessage(String uuid){
    	MessageEntity me = new MessageEntity();
    	try{
    		me = eao.getById(uuid);
    	}
    	catch (Exception e) {
			log.error("exception at get message", e);
		}
    	return me;
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.email.EmailManagerLocal#getMessages(java.lang.String, java.lang.String)
     */
    public List<MessageEntity> getMessages(Date start, Date end){
    	return eao.findByDateRange(start, end);
    }

	@Override
	public List<MessageEntity> getMessageBySubject(String subject) {
		List<MessageEntity> messages=new ArrayList<MessageEntity>();
		try {
			messages=eao.findBySubject(subject);
		}catch(Exception e){
		    // TODO 2992
			log.error(LogUtils.createExceptionLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, e));
		}
		return messages;
	}
}
