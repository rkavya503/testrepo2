/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import com.akuacom.pss2.util.LogUtils;

public class EmailSender implements MessageSender {
	
	private static final Logger log = Logger.getLogger(EmailSender.class);
	
	@Override
	public boolean sendMessage(MessageEntity message) {
		try {
			if ((!message.getTo().equalsIgnoreCase("noops@akuacom.com")) && (!message.getTo().equalsIgnoreCase("drastest@gmail.com")))
				//log.info("Sinking outbound message to predefined invalid email address: Message not sent to "+message.getTo());
			//else
				sendMessage(message.getProgramName(),
						message.getTo(),
					    message.getFrom(),
					    message.getSubject(),
					    message.getContent(),
					    message.getContentType(),
					    message.getAttachFilename(),
					    message.getAttachFileContent());
				
		} catch (MessagingException e) {
			log.error(e.getMessage(),e);
			log.error(LogUtils.createLogEntry(message.getProgramName(), 
                LogUtils.CATAGORY_NOTIFICATION, "Failed to send message to "+message.getTo(),
                message.toString()));
			log.error(LogUtils.createLogEntry(message.getProgramName(), 
	                LogUtils.CATAGORY_NOTIFICATION, "Exception while trying to send message to "+message.getTo(),
	                e.getMessage()));
			return false;
		}
		return true;
	}
	
	/**
     * Send message.
     * 
     * @param to the to
     * @param from the from
     * @param subject the subject
     * @param content the content
     * 
     * @throws MessagingException the messaging exception
     */
    public void sendMessage(String programName,String to, String from, String subject, 
    		String content, String contentType, String attachFilename, String attachFileContent) 
    	throws MessagingException {
        
        MimeMessage mailMsg;
    	
        try {
            mailMsg = new MimeMessage(
                    MailSessionFactory.getSmtpSession()); // get email message
        } catch (IOException e) {
            throw new MessagingException("Could not send the message");
        }
        
        InternetAddress[] ias;
        if (to != null) {
            ias = InternetAddress.parse(to, false);
            mailMsg.setRecipients(javax.mail.Message.RecipientType.TO, ias);
        } else {
            throw new MessagingException(
                    "The mail message requires a 'TO' address.");
        }
        if (from != null) {
            mailMsg.setFrom(new InternetAddress(from));
        } else {
            throw new MessagingException(
                    "The mail message requires a 'FROM' address.");
        }
        if (subject != null)
            mailMsg.setSubject(subject);
        
        MimeBodyPart messageBodyPart =  new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        String type= contentType==null?"text/plain":contentType;
        messageBodyPart.setContent(content, type);
        multipart.addBodyPart(messageBodyPart);
        
        // Part two is attachment
        if(attachFileContent!=null){
	        messageBodyPart = new MimeBodyPart();
	        DataSource source = 
	          new ByteArrayDataSource(attachFileContent.getBytes(),"text/plain");
	        messageBodyPart.setDataHandler(
	          new DataHandler(source));
	        messageBodyPart.setFileName(attachFilename);
	        multipart.addBodyPart(messageBodyPart);
        }
        
        // Put parts in message
        mailMsg.setContent(multipart);
        Transport.send(mailMsg);
    }
   
}
