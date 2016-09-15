package com.akuacom.pss2.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.pss2.util.LogUtils;
import org.jboss.ejb3.annotation.Depends;


/**
 * It is possible to make this class to be more generic email reader
 * for the incoming request through emails.
 *
 * @author Dichen Mao
 */
@Stateless
@Depends("jboss.messaging.destination:service=Queue,name=pss2peakchoice")
public class PeakChoiceEmailReaderBean implements PeakChoiceEmailReader.R, PeakChoiceEmailReader.L {
    private static final Logger log = Logger.getLogger(PeakChoiceEmailReaderBean.class);

//    private static final DateFormat FORMAT = new SimpleDateFormat("MM/dd/yyyy, HH:mma z");

    @EJB
    PeakChoiceMessageEAO.L eao;

    /** The queue connection. */
    QueueConnection queueConnection;
    /** The queue session. */
    QueueSession queueSession = null;
    /** The queue sender. */
    QueueSender sender;

    private boolean connected = false;

    public void init() {
        connected = false; // reset result
        try {
            Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
            env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
            final Context context = new InitialContext(env);
            final QueueConnectionFactory conFactory =
                    (QueueConnectionFactory) context.lookup("ConnectionFactory");
            queueConnection = conFactory.createQueueConnection();
            queueConnection.setExceptionListener(new ExceptionListenerImpl());
            connected = true;

            // Create a JMS queueSession object
            queueSession = queueConnection.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) context.lookup("queue/pss2peakchoice");
            sender = queueSession.createSender(queue);
        } catch (JMSException e) {
        	//Linda: DRMS-1653
        	log.error(LogUtils.createLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, 
        			"failed to get JMS connection for peak choice email reader", null));
            log.debug(LogUtils.createExceptionLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, e));
        } catch (NamingException e) {
        	//Linda: DRMS-1653
        	log.error(LogUtils.createLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, 
        			"failed to get JMS connection for peak choice email reader", null));
            log.debug(LogUtils.createExceptionLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, e));
        }
    }

    public void poll() {
        log.debug("processing peakchoice emails started");
        init();
        final Session session = MailSessionFactory.getImapSession();
        try {
            final Store store = session.getStore();
            store.connect();
            log.debug("store connected");

            final Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            log.debug("inbox opened");
            final Message[] messages = inbox.getMessages();
            log.debug("got messages");
            String eventName = null;
            String programName = null;
            for (Message message : messages) {
                if (message.isSet(Flags.Flag.SEEN)) {
                    continue;
                }
                log.debug("tested read flag");
                final String subject = message.getSubject();
                if (!subject.endsWith("Curtailment Notification")) {
                    continue;
                }
                log.debug("read subject");
                final String contentType = message.getContentType();
                if (!contentType.startsWith("text/plain;")) {
                    continue;
                }
                log.debug("tested content type");
                try {
                    final String content = (String) message.getContent();
                    log.debug("got content");
                    final BufferedReader br = new BufferedReader(new StringReader(content));
                    String line = br.readLine();
                    while (line != null) {
                        if (line.startsWith("Event ID:")) {
                            eventName = line.substring("Event ID:".length()).trim();
                        } else if (line.startsWith("Program:")) {
                            programName = line.substring("Program:".length()).trim();
                        }

                        line = br.readLine();
                    }
                    log.debug("content processed");

                    PeakChoiceMessageEntity entity = new PeakChoiceMessageEntity();
//                    entity.setUserName();
                    entity.setProgramName(programName);
                    entity.setEventName(eventName);
                    entity.setFrom(convert(message.getFrom()));
                    entity.setTo(convert(message.getRecipients(Message.RecipientType.TO)));
                    entity.setSubject(message.getSubject());
                    entity.setContent(content);
                    entity.setType("incomming");
                    entity.setCreationTime(message.getReceivedDate());  //todo: create new fields
                    entity = eao.create(entity);

                    final javax.jms.Message msg = queueSession.createObjectMessage(entity);
                    sender.send(msg);   // send to JMS and let consumer to create the event
                    // hence separate the possible cyclic reference problem.
                } catch (IOException e) {
                	//Linda: DRMS-1653
                	log.error(LogUtils.createLogEntry(programName, LogUtils.CATAGORY_NOTIFICATION, 
                			"failed in parsing email", null));
                    log.debug(LogUtils.createExceptionLogEntry(programName, 
                    		LogUtils.CATAGORY_NOTIFICATION, e));
                } catch (DuplicateKeyException e) {
                	//Linda: DRMS-1653
                	log.error(LogUtils.createLogEntry(programName, LogUtils.CATAGORY_NOTIFICATION, 
                			"failed in parsing email", null));
                    log.debug(LogUtils.createExceptionLogEntry(programName, 
                    		LogUtils.CATAGORY_NOTIFICATION, e));
                } catch (JMSException e) {
                	//Linda: DRMS-1653
                	log.error(LogUtils.createLogEntry(programName, LogUtils.CATAGORY_NOTIFICATION, 
                			"failed in parsing email", null));
                    log.debug(LogUtils.createExceptionLogEntry(programName, 
                    		LogUtils.CATAGORY_NOTIFICATION, e));
                }

                message.setFlag(Flags.Flag.SEEN, true);
                log.debug("message processed");
            }
            store.close();
        } catch (NoSuchProviderException e) {
        	//Linda: DRMS-1653
        	log.error(LogUtils.createLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, 
        			e.getMessage(), null));
            log.debug(LogUtils.createExceptionLogEntry(null, 
            		LogUtils.CATAGORY_NOTIFICATION, e));
        } catch (MessagingException e) {
        	//Linda: DRMS-1653
        	log.error(LogUtils.createLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, 
        			"unexpected error in reading email", null));
            log.debug(LogUtils.createExceptionLogEntry(null, 
            		LogUtils.CATAGORY_NOTIFICATION, e));
        }

        log.debug("processing peakchoice emails finished");
    }

    private String convert(Address[] from) {
        final StringBuilder result = new StringBuilder("");
        for (Address address : from) {
            result.append("; ").append(address.toString());
        }
        return result.substring(2);
    }

/*
    public static void main(String[] args) {
        BasicConfigurator.configure();
        final List<DBPEvent> list = new PeakChoiceEmailReaderBean().getEvents();
        System.out.println(list.size());
    }
*/
    /**
     * The Class ExceptionListenerImpl.
     */
    private class ExceptionListenerImpl implements ExceptionListener {

        /* (non-Javadoc)
         * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
         */
        public void onException(JMSException e) {

            for (int i = 0; i < 3; i++) {
                log.warn("Connection has problems, trying to re-create it, attempt " +
                        (i + 1) + " ...");

                try {
                    queueConnection.close();  // unregisters the ExceptionListener
                } catch (Exception e1) {
                    // I will get an Exception anyway, since the connection to the server is
                    // broken, but close() frees up resources associated with the connection
                }

                init();
                if (connected) {
                    return; // return if successful
                }

            }

            log.error("Cannot re-establish connection, giving up ...");
        }
    }
}
