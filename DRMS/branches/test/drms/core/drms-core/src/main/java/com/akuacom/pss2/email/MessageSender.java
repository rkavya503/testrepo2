/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

/**
 * MessageSender is responsible for send out {@link com.akuacom.pss2.email.MessageEntity}
 * 
 * @author Daoping
 */
public interface MessageSender {

	/**
	 * Send out the message
	 * @param message the message to send 
	 * @return whether the message is sent out successfully
	 */
	boolean sendMessage(MessageEntity message);
	
}
