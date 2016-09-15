package com.akuacom.pss2.queuedhttp;

import com.akuacom.pss2.email.*;
import com.akuacom.pss2.participant.Participant;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;


import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * default implementation of {@link Notifier } 
 * 
 */
@Stateless
public class ClientPushBean implements ClientPush.R, ClientPush.L {

    /** The logger. */
    private static Logger log = Logger.getLogger(ClientPushBean.class);
    /** Connector Factory for JCA, it can support distributed transaction **/
    @Resource(mappedName = "java:/JmsXA")
    protected ConnectionFactory connFactory;
    /** email destination is used for email notification **/
    @Resource(mappedName = "queue/pss2push")
    protected Destination pushDesitination;
    private static QueueConnection queueConnection;
    /** The queue session. */
    private static QueueSession queueSession = null;
    private static QueueSender pushSender = null;

    private void init() throws JMSException, NamingException {
        Properties env = new Properties();

        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        final Context context = new InitialContext(env);
        final QueueConnectionFactory conFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
        queueConnection = conFactory.createQueueConnection();

        // Create a JMS queueSession object
        queueSession = queueConnection.createQueueSession(false,
                Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) context.lookup("queue/pss2push");
        pushSender = queueSession.createSender(queue);

        context.close();

    }

    @Override
    public void pushEventState(Participant client, String eventStateXML)
            throws JMSException, NamingException {

        if (queueSession == null || pushSender == null) {
            init();
        }

        Connection connection = null;
        Session session = null;
        javax.jms.MessageProducer producer = null;
        try {
            connection = connFactory.createConnection();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            //do send the message to jms tunnel
            producer = session.createProducer(pushDesitination);
            ObjectMessage objMsg = session.createObjectMessage();

            String clientAddr = client.getIp();
            URI address=null;
            String authName = null;
            String authPass = null;

            // The client "address" can contain just a url, or a name:password@url
            int atSymbol = clientAddr.indexOf("@");
            if (atSymbol > 0) {
                String namePass = clientAddr.substring(0,atSymbol);
                int colon = namePass.indexOf(":");
                if (colon >= 0) {
                    authName =namePass.substring(0, colon);
                    authPass = namePass.substring(colon+1);
                }
                address = new URI(clientAddr.substring(atSymbol+1));
            } else {
                address = new URI(clientAddr);
            }



            HTTPRequestDef message = new HTTPRequestDef(client.getParticipantName(),
                    address,
                    eventStateXML,
                    HTTPRequestDef.METHOD_POST,
                    authName,
                    authPass);

            objMsg.setObject(message);
            producer.send(objMsg);
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(ClientPushBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (producer != null) {
                try {
                    producer.close();
                } catch (Exception ignore) {
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (Exception ignore) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}
