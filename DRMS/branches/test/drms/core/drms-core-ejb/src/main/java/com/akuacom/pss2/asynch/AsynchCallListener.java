package com.akuacom.pss2.asynch;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.Depends;

@TransactionManagement
@MessageDriven(name = "AsynchCallMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
@Resources(@Resource(name = "jms/QueueFactory", type = QueueConnectionFactory.class))
@Depends("jboss.messaging.destination:service=Queue,name=asynchCallQueue")
public class AsynchCallListener implements MessageListener {
	
	 /** The Constant log. */
    private static final Logger log = Logger.getLogger(AsynchCallListener.class);
    
	//@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void onMessage(javax.jms.Message message) {
		try{
		  if (message instanceof ObjectMessage) {
	           final Object obj =  ((ObjectMessage) message).getObject();
	           if(obj instanceof AsynchRunable){
					AsynchRunable work = (AsynchRunable) obj;
				    work.run();
	           }
		  	}
		}catch(Exception e){
			 log.error("error in consuming message", e);
		}
	}
}
