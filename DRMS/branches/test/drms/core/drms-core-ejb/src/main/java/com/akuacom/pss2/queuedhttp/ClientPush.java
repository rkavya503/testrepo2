package com.akuacom.pss2.queuedhttp;

import com.akuacom.pss2.email.*;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.participant.Participant;
import javax.jms.JMSException;
import javax.naming.NamingException;

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

public interface ClientPush {
	
	@Remote
	public interface R extends ClientPush {}
	@Local
	public interface L extends ClientPush {}
    
	public void pushEventState(Participant client, String eventStateXML)
            throws JMSException, NamingException   ;
	
}
