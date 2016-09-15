package com.akuacom.pss2.email;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.jms.JMSException;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.contact.ParticipantContact;

/**
 *  <b>Notifier</b> acts as a facade to JMS Channel and notification system. 
 *   the Message going though this channel will firstly sent to message queue and then
 *   reach all message consumers. 
 *   <p>
 *   This process of message produce is transacted, and participants in the the caller's  
 *   transaction. Therefore if caller's transaction is rolled back, no message will 
 *   be pushed into the message queue, and message consumer will not get notified.
 *   
 *   <p>
 *   NOTE all methods come from {@link com.akuacom.pss2.email.MailUtil}, and MailUtil is deprecated since it
 *   is can not participate caller's transaction
 *   
 *   @since 6.4
 */

public interface Notifier {
	
	@Remote
	public interface R extends Notifier {}
	@Local
	public interface L extends Notifier {}
    
	void sendNotification(ParticipantContact p, String userName, String subject,
	        	String content, NotificationMethod method, NotificationParametersVO params,
	        	boolean internalOnly, boolean isEventNotification,
	        	boolean isCommNotification, String programName); 
	
	void sendNotification(Contact p, String userName, String subject, 
        	String content, String contentType, NotificationMethod method, NotificationParametersVO params,
        	boolean internalOnly, boolean isEventNotification, 
        	boolean isCommNotification, String programName) ;
	
	void sendNotification(List<Contact> contacts, 
		    	String userName, String subject, String content, NotificationMethod method, 
		    	NotificationParametersVO params, boolean internalOnly, String programName);
	
	void sendNotification(List<Contact> contacts, 
	    	String userName, String subject, String content, String attachFilename, String attachFileContent, 
	    	NotificationMethod method, NotificationParametersVO params, boolean internalOnly, String programName);

	
	void sendMail(String to, String from, String subject, String content, String contentType,
            String userName, String programName, String eventName, int priority, String contactUuid);
            		
	void sendMail(MessageEntity messageEntity);
	
	void sendEnvoyMessage(String userName, String subject, String xmlString, boolean isTest, String domain);

    public  void sendVaroliiNotification(List<Contact> contacts, String userName,
            String subject, NotificationMethod nm, NotificationParametersVO params,
            boolean isTest) throws JMSException;

    public  void sendVaroliiNotification2(List<Contact> contacts, String userName,
            String subject, NotificationMethod nm, NotificationParametersVO params,
            boolean isTest) throws JMSException;
}
