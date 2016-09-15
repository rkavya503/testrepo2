/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * <tt>MessageDispatcher</tt> should be the only gateway to dispatch a message. 
 * Messages will be blocked or sent out directly during the process of dispatching, 
 * Some changes may also happen to the message, such as the destination redirection,  
 * Priority reset. e.g. 
 * 
 * 
 * Message may come from a message queue or recycled from the database for 
 * error sending message
 * 
 * @author Daoping
 */

public interface MessageDispatcher {
    @Remote
    public interface R extends MessageDispatcher {}
    @Local
    public interface L extends MessageDispatcher {}
	
	/**
	 * dispatch the message
	 * @param message to dispatch 
	 */
	void dispatchMessage(MessageEntity message);

}
