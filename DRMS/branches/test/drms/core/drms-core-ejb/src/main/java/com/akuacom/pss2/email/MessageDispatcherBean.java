/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.nssettings.NSSettingsEAO;
import com.akuacom.pss2.nssettings.NSSettings;
import com.akuacom.pss2.participant.contact.ParticipantContactEAO;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.LogUtils;

/**
 * Default implementation of <tt>MessageDiapatcher</tt>
 * @author Daoping
 */

@Stateless
public class MessageDispatcherBean implements MessageDispatcher.R, MessageDispatcher.L, MessageComparator {
	
	private static final Logger log = Logger.getLogger(MessageDispatcherBean.class);
	 
	@EJB private MessageEAO.L messageEAO;
	
	@EJB private NSSettingsEAO.L preferenceEAO;
	
	private MessageSender messageSender;
	
	@EJB private ContactManager.L contactManager;
	
	@EJB private ProgramManager.L  programManager;
	
	@EJB private ParticipantContactEAO.L participantContactEAO;
    
    @EJB private SystemManager.L systemManager;    
	
	/** The Constant MESSAGE_RETRY. */
    public static final int MESSAGE_RETRY = 1;
    
 	
	public MessageDispatcherBean(){
		this.setMessageSender(new EmailSender());
	}
	
	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}
	
	public void setMessageEAO(MessageEAO.L eao) {
		messageEAO = eao;
	}
	
	public void setPreferenceEAO(NSSettingsEAO.L eao) {
		preferenceEAO = eao;
	}
	
	public void setParticipantContactEAO(ParticipantContactEAO.L eao) {
		participantContactEAO = eao;
	}
	
	public void setContactManager(ContactManager.L manager) {
		contactManager = manager;
	}
	
	public void setProgramManager(ProgramManager.L manager) {
		programManager = manager;
	}
	
	@Override
	public boolean isSimilar(MessageEntity message1, MessageEntity message2) {
		return
			message1.getSubject().trim().equalsIgnoreCase(message2.getSubject().trim())
			&& message1.getTo().trim().equalsIgnoreCase(message2.getTo().trim());
	}
	
	/**
	 * Dispatch the message, e.g. message filtering.
	 * <p>
	 * Set transaction attribute to NOT_SUPPORTED for three reasons:
	 * <li> Message filtering is based on message counting, Message insertion should immediately be
	 * applied to message table to ensure the message counting is correct. 
	 * <li> some operations is this process are not rollbackable. e.g. It's very hard to recall a 
	 * sent message when the transaction rolling back 
	 * <li>NOT_SUPPORTED transaction will not cause a big problem in data consistency in this situation. 
	 * the best situation is that the status of some messages are not logged correctly.
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void dispatchMessage(MessageEntity message) {
		//check message
		if(message.getCreationTime()==null)
			message.setCreationTime(new Date());
		
		//go out directly for manually-resent email
		if(message.getStatus()!=MessageEntity.STS_NEW ){
			log.debug(LogUtils.createLogEntry(message.getProgramName(), 
	            	this.getClass().getSimpleName(), "Resend Email ", message.toString()));
			sendMessage(message);
			return;
		}
		
		log.debug(LogUtils.createLogEntry(message.getProgramName(), 
            	this.getClass().getSimpleName(), "Start Dispatch Message", message.toString()));
		//save it for new message
		long count = 0;
		try {
			Object objects[]=messageEAO.logMessage(message,getStartOfDay(message.getCreationTime()));
			count = (Long)objects[1];
			message = (MessageEntity)objects[0];
			log.debug(LogUtils.createLogEntry(message.getProgramName(), 
		            	this.getClass().getSimpleName(), "Log message in db successfully", message.toString()));
		} catch (Exception e) {
            // TODO 2992
			log.error(LogUtils.createLogEntry(message.getProgramName(), 
		            	this.getClass().getSimpleName(), e.getMessage(),e.getStackTrace().toString()));
		}
		
		NSSettings preference=  preferenceEAO.getNSSettings();
		doExceptionListFilter(message,preference,count);
	}
	
	/**
	 * Filter messages according to exception list. e.g. contact in exception list has no limit 
	 * @param message message to send 
	 * @param preference global notification settings 
	 */
	protected void doExceptionListFilter(MessageEntity message,NSSettings preference,long count){
		if(isInExceptionList(message)){
			log.debug(LogUtils.createLogEntry(message.getProgramName(), 
					LogUtils.CATAGORY_NOTIFICATION, "Send email to:"+message.getTo() + "," +
							" recipient is in exception list", message.toString()));
			sendMessage(message);
		}else{
			doFrequencyFilter(message,preference,count);
		}
	}
	
	protected String getContactInfo(MessageEntity message){
		String contactInfo="";
		if(message.getContactId()!=null){
			//no human-readable name for contact, so just use contact id
			contactInfo ="For Contact "+ message.getContactId();
		}
		return contactInfo;
	}
	
	/**
	 * Check the message count if it exceeds the threshold in a certain time frame
	 * If it does, the message will be suspended. 
	 * @param message the message to be sent
	 * @param preference the global filter settings 
	 */
	protected void doFrequencyFilter(MessageEntity message,NSSettings preference,long count){
		//when frequency filter status is off
		//message should be sent out directly
		if(preference.getFilterStatus() == 0){
			sendMessage(message);
			return;
		}
		
		//Check whether the contact level settings is available or not
		int limit = -1;
		String contactInfo=getContactInfo(message);
		
		if(message.getContactId()!=null){
			limit = participantContactEAO.getMsgThreshold(message.getContactId());
		}
		
		//no contact level settings, the global settings will be applied
		if(limit<0)
			 limit = preference.getMsgThreshold();
		
		if(preference.getFrequency()>0){
			String msg= message.getStatus()==MessageEntity.PRIORITY_NORMAL?"normal":"urgent";
			if(count<=limit){
				
				log.debug(LogUtils.createLogEntry(message.getProgramName(), 
						LogUtils.CATAGORY_NOTIFICATION, "Send "+msg+" to:"+message.getTo()
							+", count:"+count+"/"+limit +" "+contactInfo,message.toString()));
				sendMessage(message, count, limit);
			}
			else{
				log.debug(LogUtils.createLogEntry(message.getProgramName(), 
						LogUtils.CATAGORY_NOTIFICATION, "Block "+msg+" to:"+message.getTo()	
							+", count:"+count+"/"+limit +" "+contactInfo, message.toString()));
				suspendMessage(message);
			}
		}
	}
	
	/**
	 * Returns whether a certain contact is in exception list or not
	 * The global dras operator and program operator is on the exception list
	 *  
	 * @param MessageEntity message to be sent
	 * @return true if given address is in the exception list 
	 */
	protected boolean isInExceptionList(MessageEntity message){
		//check if it is in global contact list
		//dras operator contact 
		String address = message.getTo();
		String contactId= message.getContactId();
		
		// Global operator
		EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
		List<Contact> contacts =  cache.getEscacheforoperatorcontacts();
        if(contacts == null || contacts.isEmpty()){
        	contacts = contactManager.getOperatorContacts();
        	cache.setEscacheforoperatorcontacts("OperatorContacts", contacts);
        }
		if(contactId!=null){
			for(Contact contact:contacts){
				if(contact.getAddress().trim().equalsIgnoreCase(address.trim())){	
					if(contact.getUUID().trim().equalsIgnoreCase(contactId.trim())){
						return true;
					}
				}
			}
		}else{// if contact Id is null, then only filter based on the email address
			for(Contact contact:contacts){
				if(contact.getAddress().trim().equalsIgnoreCase(address.trim()))
					return true;
			}
		}
			
		//program operator
		if(message.getProgramName()!=null){
			if(contactId!=null){
				for(Contact contact:programManager.getOperatorContacts(message.getProgramName())){
					if(contact.getAddress().trim().equalsIgnoreCase(address.trim())){
						if(contact.getUUID().trim().equalsIgnoreCase(contactId.trim())){
							return true;
						}
					}
				}
			}else{// if contact Id is null, then only filter based on the email address
				for(Contact contact:programManager.getOperatorContacts(message.getProgramName())){
					if(contact.getAddress().trim().equalsIgnoreCase(address.trim())){
						return true;
					}
				}
			}	
		}
		return false;
	}
	
	/**
	 * Return the start time of a day 
	 * @param date
	 * @return 
	 */
	protected Date getStartOfDay(Date date){
		//Delegate to DateUtil will cause a dependency on DateUtil in other packages
		//implement it 
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        return cal.getTime();
	}
	
	/**
	 * Send the message via message sender
	 * @param message message to send 
	 * @param count count of messages already been sent
	 * @param limit the maximum messages to be be sent
	 */
	protected void sendMessage(MessageEntity message, long count, long limit){
		int i =0;
		String contactInfo=getContactInfo(message);
		boolean success= false;
		
        /*
         * Elsewhere (scattered all over the place, in fact) there is logic
         * that checks a system environment variable that squelches all but
         * internal (Akuacom) emails.  
         * That system environment variable always wins.
         * Here, we check a secondary switch "SuppressAllEmails", which is
         * settable without restarting the server.  This switch cannot enable
         * emails when the environment variable has them disabled, but it can
         * disable them when they would otherwise go out.
         */
        if (systemManager != null) {
        	EventStateCacheHelper esch = EventStateCacheHelper.getInstance();
        	Boolean suppressEmails = esch.getSupressAllEmail();
            //Boolean suppressEmails = systemManager.getPss2Properties().isSuppressAllEmails(); 
            if (suppressEmails.equals(Boolean.TRUE)) {
                // The master switch that suppresses all outgoing emails, whether
                // to Akuacom, Honeywell, or otherwise, is set.
                // NO emails get sent
                log.info("Sinking outbound message because "+
                        "of \"SuppressAllEmails\" core property  "+
                        message.getTo()+" "+message.getSubject());    
                // Not and say we did.  The message just disappears, no retries.
                message.setStatus(MessageEntity.STS_SENT);
                return;
            }
        }
        
        while (!success && i < MESSAGE_RETRY){
        	message.setSentTime(new Date());
			success = messageSender.sendMessage(message);
			i++;
		}
        if (success){
        	message.setStatus(MessageEntity.STS_SENT);
        }else{
        	String msg= message.getStatus()==MessageEntity.PRIORITY_NORMAL?"normal":"urgent";
        	
			log.debug(LogUtils.createLogEntry(message.getProgramName(), 
	            	LogUtils.CATAGORY_NOTIFICATION, "Failed to send "+msg+" to:"+ message.getTo()
	            		+", count:" +count+"/"+limit +" "+contactInfo, message.toString()));
			message.setStatus(MessageEntity.STS_FAILED);
		}
		try{
			messageEAO.update(message);
		}catch (EntityNotFoundException e) {
            // TODO 2992
			log.error(LogUtils.createLogEntry(message.getProgramName(), 
	            	this.getClass().getSimpleName(), e.getMessage(),e.getStackTrace().toString()));
		}
	}
	
	protected void sendMessage(MessageEntity message){
		// 08.12.2010 JerryM DRMS-1296:call overrided sendMessage()
		sendMessage(message, 0, 0);
	}
	
	/**
	 * suspend the message, e.g suppress the message and log the message as STS_SUSPENDED
	 * @param message
	 */
	protected void suspendMessage(MessageEntity message){
		message.setSentTime(null);
		message.setStatus(MessageEntity.STS_SUSPENDED);
		try {
			messageEAO.update(message);
		} catch (EntityNotFoundException e) {
            // TODO 2992
			log.error(LogUtils.createLogEntry(message.getProgramName(), 
	            	this.getClass().getSimpleName(), e.getMessage(),e.getStackTrace().toString()));
		}
	}
}
