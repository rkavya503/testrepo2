package com.akuacom.pss2.program.apx.queue;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;


@Stateless
public class ApxEventRequestQueueBean implements ApxEventRequestQueue.L,ApxEventRequestQueue.R{

	private static final Logger log = Logger.getLogger(ApxEventRequestQueueBean.class);

	
	@Resource(mappedName = "queue/apxDispatchQueue")
	private Queue apxMessageQueue;
	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory messageQueueFactory;
	
	public void apxMessageDispatch(ApxQueueData data) throws Exception {
		Connection connection = null;
		Session session = null;
		try {
			connection = messageQueueFactory.createConnection();
			session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(apxMessageQueue);
			messageProducer.send(session.createObjectMessage(data));
			log.debug("Sent an apx message");
		} catch (JMSException e) {
			log.warn(
					"Exception occurred while sending message to apx dispatcher",
					e);
			throw e;
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					log.warn("Cannot close session", e);
					throw e;
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					log.warn("Cannot close connection", e);
					throw e;
				}
			}
		}
}
}
