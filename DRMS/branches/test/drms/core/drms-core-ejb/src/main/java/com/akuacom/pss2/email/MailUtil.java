/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.MailUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import static com.akuacom.pss2.email.NotificationParametersVO.Transport.email;
import static com.akuacom.pss2.email.NotificationParametersVO.Transport.fax;
import static com.akuacom.pss2.email.NotificationParametersVO.Transport.pager;
import static com.akuacom.pss2.email.NotificationParametersVO.Transport.phone;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.jms.ExceptionListener;
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

import com.akuacom.pss2.program.dbp.EventBidBlock;
import org.apache.log4j.Logger;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.contact.ParticipantContactPerf;
import com.akuacom.pss2.util.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;

import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * This is the gateway of notification system. All notification will be sent
 * through this class. May promote this class into a session bean later.
 * 
 * @author Dan Hennage, Dichen Mao
 * @see com.akuacom.pss2.email.EmailBean
 * @since 4.1
 * 
 * @deprecated please use {@com.akuacom.pss2.email.Notifier} instead
 */
public class MailUtil {

    /** The log. */
    private static Logger log = Logger.getLogger(MailUtil.class);

    /** The Constant fromEmailAddr. */
    private static final String fromEmailAddr = "noreply@openadr.com";

    private static CorePropertyEAO corePropertyEAO = EJB3Factory.getBean(CorePropertyEAO.class);

    /** The queue session. */
    private static QueueSession queueSession = null;

    /** The akua sender. */
    private static QueueSender akuaSender = null;

    /** The envoy sender. */
    private static QueueSender envoySender = null;

    /** The queue connection. */
    private static QueueConnection queueConnection;

    // used by client manager and system manager and 2 places from dbp bid
    // program
    /*
     * NOTE: this is a hack for sending NORMAL level emails with minimal changes
     * to the client code. TODO: make the function name explicit for sending out
     * normal level emails.
     */
    public static void sendNotification(ParticipantContactPerf p,
            String userName, String subject, String content,
            NotificationMethod method, NotificationParametersVO params,
            boolean internalOnly, boolean isEventNotification,
            boolean isCommNotification, String programName) {
        List<Contact> contacts = new ArrayList<Contact>();
        contacts.add(p.getParticipantContactAsContact());

        sendNotification(contacts, userName, subject, content, null, method,
                params, internalOnly, isEventNotification, isCommNotification,
                programName, MessageEntity.PRIORITY_NORMAL);
    }

    //
    public static void sendNotification(Contact p, String userName,
            String subject, String content, String contentType,
            NotificationMethod method, NotificationParametersVO params,
            boolean internalOnly, boolean isEventNotification,
            boolean isCommNotification, String programName) {
        List<Contact> contacts = new ArrayList<Contact>();
        contacts.add(p);

        sendNotification(contacts, userName, subject, content, contentType,
                method, params, internalOnly, isEventNotification,
                isCommNotification, programName, MessageEntity.PRIORITY_URGENT);
    }

    /**
     * Send notification.
     * 
     * @param contacts
     *            the contacts
     * @param userName
     *            the user name
     * @param subject
     *            the subject
     * @param content
     *            the content
     * @param method
     *            the method
     * @param params
     *            the params
     * @param internalOnly
     *            the internal only
     * @param programName
     *            the program name
     */
    public static void sendNotification(List<Contact> contacts,
            String userName, String subject, String content,
            NotificationMethod method, NotificationParametersVO params,
            boolean internalOnly, String programName) {
        sendNotification(contacts, userName, subject, content, method, params,
                internalOnly, true, true, programName,
                MessageEntity.PRIORITY_URGENT);
    }

    private static void sendNotification(List<Contact> origContacts,
            String userName, String subject, String content,
            NotificationMethod method, NotificationParametersVO params,
            boolean internalOnly, boolean isEventNotification,
            boolean isCommNotification, String programName, int priority) {
        sendNotification(origContacts, userName, subject, content, null,
                method, params, internalOnly, isEventNotification,
                isCommNotification, programName, priority);
    }

    /**
     * Send notification.
     * 
     * @param origContacts
     *            the orig contacts
     * @param userName
     *            the user name
     * @param subject
     *            the subject
     * @param content
     *            the content
     * @param contentType
     *            content type
     * @param method
     *            the method
     * @param params
     *            the params
     * @param internalOnly
     *            the internal only
     * @param isEventNotification
     *            the is event notification
     * @param isCommNotification
     *            the is comm notification
     * @param programName
     *            the program name
     * @param priority
     *            the priority
     */
    // base function for all public interfaces currently in use.
    private static void sendNotification(List<Contact> origContacts,
            String userName, String subject, String content,
            String contentType, NotificationMethod method,
            NotificationParametersVO params, boolean internalOnly,
            boolean isEventNotification, boolean isCommNotification,
            String programName, int priority) {
        // first get emails from participant, if none available, report error
        if (origContacts == null) {
            return;
        }

        List<Contact> contacts = filterContacts(origContacts,
                isEventNotification, isCommNotification, method);

        if (internalOnly) {
            contacts = filterExternalContacts(contacts);
        }

        if (contacts.isEmpty()) {
            String message = "participant " + userName
                    + " has no valid contacts";

            // TODO 2992
            log.debug(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_NOTIFICATION, message,
                    origContacts.toString()));
            return;
        }

        if (NotificationMethod.MEDIA_AKUA_MAIL.equals(method.getMedia())) {
            // TODO: shouldn't these be restricted to e-mail only?

            StringBuilder sb = new StringBuilder();
            for (Contact contact : contacts) {
                sb.append(contact);
                sb.append("\n");
            }
            sb.append("subject: ");
            sb.append(subject);
            sb.append("\n");
            sb.append(content);

            log.debug(LogUtils.createLogEntry(programName,
                    LogUtils.CATAGORY_NOTIFICATION,
                    "sending email notification to " + userName, sb.toString()));
            try {
                for (Contact contact : contacts) {
                    sendMail(contact.getAddress(), fromEmailAddr, subject,
                            content, contentType, userName, null, null,
                            priority, contact.getUUID());
                }
            } catch (Exception e) {
                // TODO 2992
                log.error(LogUtils.createLogEntry(programName,
                        LogUtils.CATAGORY_NOTIFICATION, "error sending email",
                        ""));
            }
        } else {
            try {
                sendEnvoyNotification(contacts, userName, subject, method,
                        params, false);
            } catch (Exception e) {
                // TODO 2992
                log.error(LogUtils.createLogEntryUser(programName, userName,
                        LogUtils.CATAGORY_NOTIFICATION,
                        "error in sending envoy message", null));
            }
        }
    }

    /**
     * Filter external contacts.
     * 
     * @param list
     *            the list
     * 
     * @return the list< contact>
     */
    private static List<Contact> filterExternalContacts(List<Contact> list) {
        List<Contact> contacts = new ArrayList<Contact>();
        for (Contact c : list) {
            final String address = c.getAddress();
            final String type = c.getType();
            if (Contact.EMAIL_ADDRESS.equals(type) && address != null
                    && address.contains("@")) {
                if (address.toLowerCase().endsWith("@akuacom.com")) {
                    contacts.add(c);
                }
            } else if ((Contact.PHONE_NUMBER.equals(type) || Contact.FAX_NUMBER
                    .equals(type))
                    && address != null
                    && (address.startsWith("415-256") || address
                            .startsWith("415256"))) {
                contacts.add(c);
            }
        }
        return contacts;
    }

    /**
     * Filter contacts.
     * 
     * @param list
     *            the list
     * @param isEventNotification
     *            the is event notification
     * @param isCommNotification
     *            the is comm notification
     * @param method
     *            the method
     * 
     * @return the list< contact>
     */
    private static List<Contact> filterContacts(List<Contact> list,
            boolean isEventNotification, boolean isCommNotification,
            NotificationMethod method) {
        List<Contact> contacts = new ArrayList<Contact>();
        for (Contact c : list) {
        	if(c.getAddress() == null || c.getAddress().length() == 0)
        	{
        		continue;
        	}
        	if(NotificationMethod.MEDIA_AKUA_MAIL.equals(method.getMedia())
        			&& !Contact.EMAIL_ADDRESS.equals(c.getType()))
        	{
        		continue;
        	}			
			
			if(isCommNotification||isEventNotification ){
				if((isCommNotification&&c.isCommNotification())||(isEventNotification&&c.eventNotificationOn()) ){
					contacts.add(c);
				}
				continue;
			}
			contacts.add(c);
        }
        return contacts;
    }

    /**
     * Send mail.
     * 
     * @param to
     *            the to
     * @param from
     *            the from
     * @param subject
     *            the subject
     * @param content
     *            the content
     * @param contentType
     *            content type
     * @param userName
     *            the user name
     * @param programName
     *            the program name
     * @param eventName
     *            the event name
     * @param priority
     *            priority
     * @param contactUuid
     *            contact uuid
     * @throws NamingException
     *             the naming exception
     * @throws JMSException
     *             the JMS exception
     */
    public static void sendMail(String to, String from, String subject,
            String content, String contentType, String userName,
            String programName, String eventName, int priority,
            String contactUuid) throws NamingException, JMSException {
        if (queueSession == null || akuaSender == null) {
            init();
        }

        MessageEntity vo = new MessageEntity();
        vo.setTo(to);
        vo.setFrom(from);
        vo.setSubject(subject);
        vo.setContent(content);
        vo.setType("email");
        vo.setCreationTime(new Date());
        vo.setUserName(userName);
        vo.setProgramName(programName);
        vo.setEventName(eventName);
        vo.setContentType(contentType);
        vo.setPriority(priority);
        vo.setContactId(contactUuid);

        ObjectMessage objectMessage = queueSession.createObjectMessage();
        objectMessage.setObject(vo);
        akuaSender.send(objectMessage);
    }

    /**
     * Send mail.
     * 
     * @param messageEntity
     *            message
     * 
     * @throws NamingException
     *             the naming exception
     * @throws JMSException
     *             the JMS exception
     * 
     * @since DRAS-6.1
     */
    public static void sendMail(MessageEntity messageEntity)
            throws NamingException, JMSException {
        if (queueSession == null || akuaSender == null) {
            init();
        }
        ObjectMessage objectMessage = queueSession.createObjectMessage();
        objectMessage.setObject(messageEntity);
        akuaSender.send(objectMessage);
    }

    /**
     * Inits the.
     * 
     * @throws NamingException
     *             the naming exception
     * @throws JMSException
     *             the JMS exception
     */
    private static void init() throws NamingException, JMSException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        final Context context = new InitialContext(env);

        final QueueConnectionFactory conFactory = (QueueConnectionFactory) context
                .lookup("ConnectionFactory");
        queueConnection = conFactory.createQueueConnection();
        queueConnection.setExceptionListener(new ExceptionListenerImpl());

        // Create a JMS queueSession object
        queueSession = queueConnection.createQueueSession(false,
                Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) context.lookup("queue/pss2email");
        akuaSender = queueSession.createSender(queue);
        Queue q2 = (Queue) context.lookup("queue/pss2envoy");
        envoySender = queueSession.createSender(q2);

        context.close();
    }

    /** The Constant xmlHeaderString. */
    private static final String xmlHeaderString = "<?xml version=\"1.0\"?>\n"
            + "<Request version=\"EXAPI 2.0\">\n"
            + "  <username>pge_autodbpadmin</username>\n"
            + "  <Password>pgeadmin</Password>\n" + "  <Domain>PGE</Domain>\n"
            + "  <OemId>pge_autodbp_oem</OemId>\n"
            + "  <OemPassword>pge$oem</OemPassword>\n"
            + "  <RequestType>commit</RequestType>\n" + "  <Job>\n";

    /**
     * Send envoy notification.
     * 
     * @param contacts
     *            the contacts
     * @param userName
     *            the user name
     * @param subject
     *            the subject
     * @param nm
     *            the nm
     * @param params
     *            the params
     * @param isTest
     *            the is test
     * 
     * @throws NamingException
     *             the naming exception
     * @throws JMSException
     *             the JMS exception
     */
    public static void sendEnvoyNotification(List<Contact> contacts,
            String userName, String subject, NotificationMethod nm,
            NotificationParametersVO params, boolean isTest)
            throws NamingException, JMSException {
        if (!isTest && (queueSession == null || envoySender == null)) {
            init();
        }

        final StringBuilder builder = new StringBuilder(xmlHeaderString);
        builder.append(getMessageXml(params, subject));

        builder.append(getContactXml(contacts));
        builder.append(getBlockXml(contacts, nm));
        builder.append("</Job>\n</Request>");

        final String xmlString = builder.toString();

        StringBuilder sb = new StringBuilder();
        for (Contact contact : contacts) {
            sb.append(contact);
            sb.append("\n");
        }
        sb.append(xmlString);
        FireLogEntry logEntry = new FireLogEntry();
        logEntry.setDescription("sending envoy notification to " + userName);
        logEntry.setLongDescr(sb.toString());
        logEntry.setCategory(LogUtils.CATAGORY_NOTIFICATION);
        // TODO 2992
        log.debug(logEntry);

        sendEnvoyMessage(userName, subject, xmlString, isTest);
    }

    /**
     * Send envoy message.
     * 
     * @param userName
     *            the user name
     * @param subject
     *            the subject
     * @param xmlString
     *            the xml string
     * @param isTest
     *            the is test
     */
    public static void sendEnvoyMessage(String userName, String subject,
            String xmlString, boolean isTest) {
        try {
            if (isTest) {
                final EnvoyBean bean = new EnvoyBean();
                bean.sendEnvoyMessage(xmlString, null);
            } else {
                final MessageEntity vo = new MessageEntity();
                vo.setSubject(subject);
                vo.setUserName(userName);
                vo.setContent(xmlString);
                vo.setCreationTime(new Date());
                final ObjectMessage om = queueSession.createObjectMessage();
                om.setObject(vo);
                envoySender.send(om);
            }
        } catch (JMSException e) {
            // TODO 2992
            log.error(LogUtils.createLogEntryUser(null, userName,
                    LogUtils.CATAGORY_NOTIFICATION,
                    "exception while sending envoy message", null));
        } catch (IOException e) {
            // TODO 2992
            log.error(LogUtils.createLogEntryUser(null, userName,
                    LogUtils.CATAGORY_NOTIFICATION,
                    "exception while sending envoy message", null));
        }
    }

    /**
     * Gets the message xml.
     * 
     * @param ep
     *            the ep
     * @param subject
     *            the subject
     * 
     * @return the message xml
     */
    private static String getMessageXml(NotificationParametersVO ep,
            String subject) {
        StringBuilder builder = new StringBuilder("");

        builder.append("<Message>\n");
        builder.append("<subject>").append(subject).append("</subject>\n");
        builder.append(getMessageArgXml("SENDER", ep.getSender()));
        builder.append(getMessageArgXml("EMAIL_ADDR", ep.getEmail()));
        builder.append(getMessageArgXml("THEME", ep.getTheme()));
        builder.append(getMessageArgXml("EventID", ep.getEventId()));
        builder.append(getMessageArgXml("IsTestEvent", ep.isTestEvent() + ""));
        builder.append(getMessageArgXml("EventCondition",
                ep.getEventCondition()));
        builder.append(getMessageArgXml("MeterName", ep.getMeterName()));
        builder.append(getMessageArgXml("ProgramName", ep.getProgramName()));
        builder.append(getMessageArgXml("ProgramType", ep.getProgramType()));
        builder.append(getMessageArgXml("SettlementType",
                ep.getSettlementType()));
        final Date startDate = ep.getEventStartDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        builder.append(getMessageArgXml("EventStartDate",
                simpleDateFormat.format(startDate)));
        final Date endDate = ep.getEventEndDate();
        builder.append(getMessageArgXml("EventEndDate", simpleDateFormat.format(endDate)));
        builder.append(getMessageArgXml("TimeZone", ep.getTimeZone()));
        builder.append(getMessageArgXml("RespondBy",
                simpleDateFormat.format(ep.getRespondBy())));
        builder.append(getMessageArgXml("URL", ep.getUrl()));
        builder.append(getMessageArgXml("EnergyUnit", ep.getEnergyUnit()));
        builder.append(getMessageArgXml("EnergyPriceUnit",
                ep.getEnergyPriceUnit()));
        final List<EventBidBlock> bidBlocks = ep.getEntries();
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        final int startHour = Integer.parseInt(hourFormat.format(startDate));
        final int endHour = Integer.parseInt(hourFormat.format(endDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        for (int i = 0, bidBlocksSize = bidBlocks.size(); i < bidBlocksSize; i++) {
            EventBidBlock bidBlock = bidBlocks.get(i);
            // note that the logic here assumes PGE DBP event is hour bound.
            if (startHour <= bidBlock.getStartTime() / 100 && endHour >= bidBlock.getEndTime() / 100 ) {
                cal.set(Calendar.HOUR_OF_DAY, bidBlock.getStartTime() / 100);
                cal.set(Calendar.MINUTE, bidBlock.getStartTime() % 100);
                builder.append(getMessageArgXml("BlockStartDate" + (i + 1), simpleDateFormat.format(cal.getTime())));
                cal.set(Calendar.HOUR_OF_DAY, bidBlock.getEndTime() / 100);
                cal.set(Calendar.MINUTE, bidBlock.getEndTime() % 100);
                builder.append(getMessageArgXml("BlockEndDate" + (i + 1), simpleDateFormat.format(cal.getTime())));
                builder.append(getMessageArgXml("BlockPrice" + (i + 1), ep.getUnitPrice()));
            }
        }
        builder.append("</Message>");

        return builder.toString();
    }

    /**
     * Gets the message arg xml.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     * 
     * @return the message arg xml
     */
    private static String getMessageArgXml(String name, String value) {
        return "<MessageArg>\n<Name>" + name + "</Name>\n<Value>" + value
                + "</Value>\n</MessageArg>\n";
    }

    /**
     * Gets the block xml.
     * 
     * @param contacts
     *            the contacts
     * @param nm
     *            the nm
     * 
     * @return the block xml
     */
    private static String getBlockXml(List<Contact> contacts,
            NotificationMethod nm) {
        final StringBuilder builder = new StringBuilder("<Block>\n");
        int i = 1;
        for (Contact c : contacts) {
            final String address = c.getAddress();
            final String type = c.getType();
            if (address != null && address.length() > 0
                    && isDeliverable(type, nm)) {
                builder.append(
                        "<DeliveryRequest>\n<MessagePath>Message[1]</MessagePath>\n<ContactMethodPath>Contact[1]/ContactMethod[")
                        .append(i)
                        .append("]</ContactMethodPath>\n</DeliveryRequest>\n");
                i++;
            }
        }
        builder.append("</Block>\n");

        return builder.toString();
    }

    /**
     * Checks if is deliverable.
     * 
     * @param type
     *            the type
     * @param nm
     *            the nm
     * 
     * @return true, if is deliverable
     */
    private static boolean isDeliverable(String type, NotificationMethod nm) {
        boolean result = false;
        if (type.equals(Contact.EMAIL_ADDRESS) && nm.isEmail()) {
            result = true;
        } else if (type.equals(Contact.FAX_NUMBER) && nm.isFax()) {
            result = true;
        } else if (type.equals(Contact.PHONE_NUMBER) && nm.isVoice()) {
            result = true;
        } else if (type.equals(Contact.PAGER) && nm.isEpage()) {
            result = true;
        }
        return result;
    }

    // NOTE: since the names are not shown in the email template, we'll fake it.
    // NOTE: names are required fields in order to send envoy notifications.
    /**
     * Gets the contact xml.
     * 
     * @param contacts
     *            the contacts
     * 
     * @return the contact xml
     */
    private static String getContactXml(List<Contact> contacts) {
        final StringBuilder builder = new StringBuilder("<Contact>\n");
        builder.append("<FirstName>dras</FirstName>\n");
        builder.append("<LastName>client</LastName>\n");
        for (Contact c : contacts) {
            builder.append(getContactMethodXml(c.getAddress(), c.getType()));
        }
        builder.append("</Contact>\n");

        return builder.toString();
    }

    /**
     * Gets the contact method xml.
     * 
     * @param contact
     *            the contact
     * @param contactType
     *            the contact type
     * 
     * @return the contact method xml
     */
    private static String getContactMethodXml(String contact, String contactType) {
        StringBuilder builder = new StringBuilder();
        if (contact != null && contact.length() > 0) {
            final String transport = getTransport(contactType);
            builder.append("<ContactMethod>\n<Transport>").append(transport)
                    .append("</Transport>\n");
            if (pager.toString().equals(transport)) {
                builder.append("<Qualifier>none</Qualifier>\n<Ordinal>0</Ordinal>\n");
            } else {
                builder.append("<Qualifier>office</Qualifier>\n<Ordinal>0</Ordinal>\n");
            }

            if (email.toString().equals(transport)) {
                builder.append("<EmailAddress>").append(contact)
                        .append("</EmailAddress>\n");
            } else {
                builder.append("<PhoneNum>").append(contact)
                        .append("</PhoneNum>\n");
            }
            builder.append("</ContactMethod>\n");
        }
        return builder.toString();
    }

    /**
     * Gets the transport.
     * 
     * @param contactType1
     *            the contact type1
     * 
     * @return the transport
     */
    private static String getTransport(String contactType1) {
        if (Contact.EMAIL_ADDRESS.equals(contactType1)) {
            return email.toString();
        } else if (Contact.PHONE_NUMBER.equals(contactType1)) {
            return phone.toString();
        } else if (Contact.FAX_NUMBER.equals(contactType1)) {
            return fax.toString();
        } else if (Contact.PAGER.equals(contactType1)) {
            return pager.toString();
        } else {
            return email.toString();
        }
    }

    private static String getFromEmailAddr() {
        String result;
        try {
            CoreProperty emailFromAddress = corePropertyEAO.getByPropertyName("emailFromAddress");
            String stringValue = emailFromAddress.getStringValue();
            InternetAddress.parse(stringValue);
            result = stringValue;
        } catch (EntityNotFoundException e) {
            result = fromEmailAddr;
        } catch (AddressException e) {
            result = fromEmailAddr;
        }

        return result;
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     * 
     * @throws NamingException
     *             the naming exception
     * @throws JMSException
     *             the JMS exception
     */
    /*
     * public static void main(String[] args) throws NamingException,
     * JMSException { final Participant p = new Participant(); final
     * ArrayList<Contact> contacts = new ArrayList<Contact>(); final Contact c1
     * = new Contact(); c1.setAddress("dichen@akuacom.com");
     * c1.setType(Contact.EMAIL_ADDRESS); contacts.add(c1); final Contact c2 =
     * new Contact(); c2.setAddress("4152562581");
     * c2.setType(Contact.PHONE_NUMBER); contacts.add(c2); final Contact c3 =
     * new Contact(); c3.setAddress("hareamao@yahoo.com");
     * c3.setType(Contact.EMAIL_ADDRESS); contacts.add(c3);
     * p.setContacts(contacts); final NotificationParametersVO ep = new
     * NotificationParametersVO(); ep.setEventId("1234");
     * ep.setEventStartDate(new Date()); ep.setEventEndDate(new Date());
     * ep.setMeterName("My Meter");
     * ep.setProgramName("Auto-DBP - Day-ahead WG2 Aggregate");
     * ep.setRespondBy(new Date()); ep.setTimeZone("PST"); final TimeBlock[]
     * timeBlocks = new TimeBlock[3]; for (int i = 0; i < timeBlocks.length;
     * i++) { final TimeBlock timeBlock = new TimeBlock();
     * timeBlock.setStartHour(12 + i); timeBlock.setEndHour(13 + i);
     * timeBlocks[i] = timeBlock; } ep.setEntries(timeBlocks); final
     * NotificationMethod method = new NotificationMethod();
     * method.setMedia(NotificationMethod.ENVOY_MESSAGE); method.setEmail(true);
     * // method.setVoice(true); BasicConfigurator.configure(); //
     * sendEnvoyNotification(p, "test", method, ep); }
     */

    /**
     * The Class ExceptionListenerImpl.
     */
    private static class ExceptionListenerImpl implements ExceptionListener {

        /*
         * (non-Javadoc)
         * 
         * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
         */
        public void onException(JMSException e) {

            for (int i = 0; i < 3; i++) {
                log.warn("Connection has problems, trying to re-create it, attempt "
                        + (i + 1) + " ...");

                try {
                    queueConnection.close(); // unregisters the
                                             // ExceptionListener
                } catch (Exception e1) {
                    // I will get an Exception anyway, since the connection to
                    // the server is
                    // broken, but close() frees up resources associated with
                    // the connection
                }

                try {
                    init();
                    return;
                } catch (JMSException e2) {
                    log.warn("Re-creating connection failed, retrying ...");
                } catch (NamingException e1) {
                    log.warn("Re-creating connection failed, retrying ...");
                }
            }

            log.error("Cannot re-establish connection, giving up ...");
            // TODO: send out alert email here.
        }
    }
}
