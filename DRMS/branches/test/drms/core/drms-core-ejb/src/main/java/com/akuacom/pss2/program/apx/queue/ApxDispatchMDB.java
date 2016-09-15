package com.akuacom.pss2.program.apx.queue;


import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.usage.calcimpl.ImplFactory;
import com.akuacom.pss2.program.apx.APXManager;
import com.akuacom.pss2.program.apx.aggregator.processor.ApxAggregatorEventRequestProcessor;
import com.akuacom.pss2.system.SystemManager;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.Depends;


/**
 * Message-Driven Bean implementation class for: DemandLimitingDispatcher
 * 
 */
@MessageDriven(name = "ApxDispatchMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable") })
@Resources(@Resource(name = "jms/QueueFactory", type = QueueConnectionFactory.class))
@Depends("jboss.messaging.destination:service=Queue,name=apxDispatchQueue")
public class ApxDispatchMDB implements MessageListener {

	private ApxQueueMessageProcessor messageProcessor;
	@EJB
	SystemManager.L systemManager;    
	@EJB
	APXManager.L apxManager;    

	private static final Logger log = Logger.getLogger(ApxDispatchMDB.class);

	public ApxDispatchMDB() {
        
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */

	public void onMessage(Message message) {
		ObjectMessage mesg = (ObjectMessage) message;
		try {
			ApxQueueData data =(ApxQueueData)mesg.getObject();
			messageProcessor = getMsgProcessor(data.getApxQueueMsgProcessorType());
			messageProcessor.processApxQueueMessage(data.getParser());
		} catch (Exception e) {
			java.util.logging.Logger.getLogger(ApxDispatchMDB.class.getName()).log(Level.SEVERE, null, e);
			log.error("Error in event request processor");
		}
		
	}
	
	private ApxQueueMessageProcessor getMsgProcessor(String msgProcessorType) throws Exception{
		if(null == msgProcessorType || msgProcessorType.isEmpty()){
			throw new Exception("Can't create APX msg processor bean");
		}
		
		if(msgProcessorType.equalsIgnoreCase("apxAggregator")){
			return EJBFactory
				    .getBean(ApxAggregatorEventRequestProcessor.class);
			
		}
		EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
    	String utilityName = cache.getUtilityName("utilityName");
		return ImplFactory.instance().getAPXManager(utilityName); // for all other apx othan than aggregator
	}
}
