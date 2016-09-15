/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

/**
 * Message Comparator
 * 
 * @see MessageEntity
 * 
 * @author Daoping
 */
public interface MessageComparator {
	
	/**
	 * Returns whether two messages are similar
	 * @param message1 the first message
	 * @param message2 the second message which will be compared with the first one
	 * @return  <tt>true</tt> if the two messages are consider similar 
	 */
	boolean isSimilar(MessageEntity message1,MessageEntity message2);
}
