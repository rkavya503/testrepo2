package com.akuacom.drw.topic;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

import org.apache.log4j.Logger;

@Stateless
public class TopicPublisherBean implements TopicPublisher.L,TopicPublisher.R {
	
	private static Logger log = Logger.getLogger(TopicPublisherBean.class);
	
	@Resource(mappedName = "topic/pss2")
	private Topic pss2Topic;
	
	@Resource(mappedName = "java:/JmsXA")
	private TopicConnectionFactory topicConnectionFactory;
	
	@Override
	public void publish(Serializable message) {
		Connection connection = null;
		Session session = null;
		try {
			connection = topicConnectionFactory.createTopicConnection();
			session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer messageProducer = session.createProducer(pss2Topic);
			messageProducer.send(session.createObjectMessage(message));
			
		} catch (JMSException e) {
			log.error("Fail to publish topic -"+ e.getMessage(),e);
		}finally{
			if (session != null) {
				try {session.close();} catch (JMSException e) {
					log.warn("Cannot close session", e);
				}
			}
			if (connection != null) {
				try {	connection.close();		} catch (JMSException e) {
					log.warn("Cannot close connection", e);
				}
			}
		}
	}
}
