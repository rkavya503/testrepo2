package com.akuacom.pss2.email;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.util.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;

import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * default implementation of {@link Notifier } 
 * 
 */
@Stateless
public class NotifierBean implements Notifier.R, Notifier.L {

	/** The logger. */
	private static final Logger log = Logger.getLogger(NotifierBean.class);
    
    final static String fromEmailAddr = "noreply@openadr.com";

    private static final CorePropertyEAO corePropertyEAO = EJB3Factory.getBean(CorePropertyEAO.class);


	/** Connector Factory for JCA, it can support distributed transaction **/
	@Resource(mappedName = "java:/JmsXA")
	protected ConnectionFactory connFactory;

	/** email destination is used for email notification **/
	@Resource(mappedName = "queue/pss2email")
	protected Destination emailDesitination;
	
	@Resource(mappedName = "queue/pss2envoy")
	protected Destination envoyDesitination;
	
	protected void doSendMessage(Serializable message) throws JMSException {
		Connection connection = null;
		Session session = null;
		javax.jms.MessageProducer producer = null;
	    try {
		  connection = connFactory.createConnection();
		  session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
		  
		  //do send the message to jms tunnel
		  producer = session.createProducer(emailDesitination);
		  ObjectMessage objMsg = session.createObjectMessage();
		  objMsg.setObject( message);
		  producer.send(objMsg);
	    }catch(Exception e){
			 log.error(e.getMessage(),e);
	    } finally {
		   if (producer != null) try { producer.close(); } catch (Exception ignore) { }
		   if (session != null) try { session.close(); } catch (Exception ignore) { }
		   if (connection != null) try { connection.close(); } catch (Exception ignore) { }
		}
	}

	protected void doSendEnvoyMessage(MessageEntity message) throws JMSException {
		Connection connection = null;
		Session session = null;
		javax.jms.MessageProducer producer = null;
		try {
			connection = connFactory.createConnection();
			session = connection.createSession(true,
						Session.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(envoyDesitination);
			final ObjectMessage om = session.createObjectMessage();
			om.setObject(message);
			producer.send(om);
		} finally {
			if (producer != null) 
					try { 	producer.close(); } catch (Exception ignore) {}
			if (session != null)
					try {	session.close();} catch (Exception ignore) {}
			if (connection != null)
					try {connection.close();} catch (Exception ignore) {}
		}
	}
	
	public  void sendNotification(ParticipantContact p, String userName, String subject,
        	String content, NotificationMethod method, NotificationParametersVO params,
        	boolean internalOnly, boolean isEventNotification,
        	boolean isCommNotification, String programName)  {
	
			List<Contact> contacts = new ArrayList<Contact>();
			contacts.add(p.getParticipantContactAsContact());
			try{
				sendNotification(contacts, userName, subject, content, null, method,
						params, internalOnly, isEventNotification, isCommNotification,
						programName, MessageEntity.PRIORITY_NORMAL);
			}catch(JMSException e){
				throw new EJBException("Exceptions in JMS Channel");
			}
    }

    public  void sendNotification(Contact p, String userName, String subject, 
        	String content, String contentType, NotificationMethod method, 
        	NotificationParametersVO params,
        	boolean internalOnly, boolean isEventNotification, 
        	boolean isCommNotification, String programName) {
    	
    		
            List<Contact> contacts = new ArrayList<Contact>();
            contacts.add(p);
            
            try{
            
            sendNotification(contacts, userName, subject, content, contentType, method,
            	params, internalOnly, isEventNotification, isCommNotification,
            	programName, MessageEntity.PRIORITY_URGENT);
            }catch(JMSException e){
            	throw new EJBException("Exceptions in JMS Channel");
            }
     }

    /**
     * Send notification.
     * 
     * @param contacts the contacts
     * @param userName the user name
     * @param subject the subject
     * @param content the content
     * @param method the method
     * @param params the params
     * @param internalOnly the internal only
     * @param programName the program name
     */
    public  void sendNotification(List<Contact> contacts, 
    	String userName, String subject, String content, NotificationMethod method, 
    	NotificationParametersVO params, boolean internalOnly, 
    	String programName)   {
    	try {
    	sendNotification(contacts, userName, subject, content, method,
			params, internalOnly, true, true, programName, MessageEntity.PRIORITY_URGENT);
    	
    	}catch(JMSException e){
    		throw new EJBException("Exceptions in JMS Channel");
    	}
    }
    
    private  void sendNotification(List<Contact> origContacts,
            String userName, String subject, String content, NotificationMethod method,
            NotificationParametersVO params, boolean internalOnly,
            boolean isEventNotification, boolean isCommNotification,
            String programName, int priority)  throws JMSException{
        sendNotification(origContacts,
    	userName, subject, content, null, method,
    	params, internalOnly,
    	isEventNotification, isCommNotification,
    	programName, priority) ;
    }
    
    /**
     * Send notification.
     * 
     * @param origContacts the orig contacts
     * @param userName the user name
     * @param subject the subject
     * @param content the content
     * @param contentType content type
     * @param method the method
     * @param params the params
     * @param internalOnly the internal only
     * @param isEventNotification the is event notification
     * @param isCommNotification the is comm notification
     * @param programName the program name
     * @param priority the priority
     */
    // base function for all public interfaces currently in use.
    private  void sendNotification(List<Contact> origContacts,
            String userName, String subject, String content, String contentType, NotificationMethod method,
            NotificationParametersVO params, boolean internalOnly,
            boolean isEventNotification, boolean isCommNotification,
            String programName, int priority) throws JMSException {
    	sendNotification(origContacts, userName, subject, content, contentType, method, params, internalOnly, 
    			isEventNotification, isCommNotification, programName, priority, null, null);
    }
    
    private  void sendNotification(List<Contact> origContacts,
            String userName, String subject, String content, String contentType, NotificationMethod method,
            NotificationParametersVO params, boolean internalOnly,
            boolean isEventNotification, boolean isCommNotification,
            String programName, int priority, String attachFilename, String attachFileContent) throws JMSException {
        // first get emails from participant, if none available, report error
		if (origContacts == null){
			return;
		}
		List<Contact> contacts = filterContacts(origContacts, isEventNotification, 
			isCommNotification, method);
		
		if (internalOnly){
			contacts = filterExternalContacts(contacts);
		}
		
		if (contacts.size() == 0){
			String message = "participant " + userName +
				" has no valid contacts";
			log.debug(LogUtils.createLogEntry(programName, 
				LogUtils.CATAGORY_NOTIFICATION, message, 
				origContacts.toString()));
			return;
		}

		if (NotificationMethod.MEDIA_AKUA_MAIL.equals(method.getMedia())){
			// TODO: shouldn't these be restricted to e-mail only?
			StringBuilder sb = new StringBuilder();
			for (Contact contact : contacts){
				sb.append(contact);
				sb.append("\n");
			}
			sb.append("subject: ");
			sb.append(subject);
			sb.append("\n");
			sb.append(content);
			
			log.debug(LogUtils.createLogEntry(programName, LogUtils.CATAGORY_NOTIFICATION, 
				"sending email notification to " + userName, sb.toString()));
			try{
				for (Contact contact : contacts) {
                    sendMail(contact.getAddress(), getFromEmailAddr(), subject,
                            content, contentType, userName, null, null,
                            priority, contact.getUUID(), attachFilename, attachFileContent);
                }
			}
			catch (Exception e)
			{
				log.error(LogUtils.createLogEntry(
					programName, 
					LogUtils.CATAGORY_NOTIFICATION, 
					"error sending email", ""));
				log.debug(LogUtils.createExceptionLogEntry(programName, 
					LogUtils.CATAGORY_NOTIFICATION, e));
			}
		}
		else{
			try{
				sendEnvoyNotification(contacts, userName, subject, method,params, false);
			}
			catch (Exception e){
				log.error(LogUtils.createLogEntryUser(programName, userName, 
						LogUtils.CATAGORY_NOTIFICATION, "error in sending envoy message", null));
				log.debug(LogUtils.createExceptionLogEntry(programName, 
						LogUtils.CATAGORY_NOTIFICATION, e));
			}
		}
    }
    
    /**
     * Filter external contacts.
     * 
     * @param list the list
     * 
     * @return the list< contact>
     */
    private static List<Contact> filterExternalContacts(List<Contact> list) {
        List<Contact> contacts = new ArrayList<Contact>();
        for (Contact c : list) {
            final String address = c.getAddress();
            final String type = c.getType();
            if (Contact.EMAIL_ADDRESS.equals(type) && address != null && address.contains("@")) {
                if (address.toLowerCase().endsWith("@akuacom.com")) {
                    contacts.add(c);
                }
            } else if ((Contact.PHONE_NUMBER.equals(type) || Contact.FAX_NUMBER.equals(type) || Contact.SMS.equals(type))
                    && address != null && (address.startsWith("415-256") || address.startsWith("415256"))) {
                contacts.add(c);
            }
        }
        return contacts;
    }
    
    /**
     * Filter contacts.
     * 
     * @param list the list
     * @param isEventNotification the is event notification
     * @param isCommNotification the is comm notification
     * @param method the method
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
     * @param to the to
     * @param from the from
     * @param subject the subject
     * @param content the content
     * @param contentType content type
     * @param userName the user name
     * @param programName the program name
     * @param eventName the event name
     * @param priority priority
     * @param contactUuid contact uuid
     * @throws NamingException the naming exception
     * @throws JMSException the JMS exception
     */
    public void sendMail(String to, String from, String subject, 
    		String content, String contentType,
            String userName, String programName, String eventName, 
            int priority, String contactUuid) {
    	sendMail(to, from, subject, content, contentType, userName, programName, eventName,
    			priority, contactUuid, null, null);
    }
    
    public void sendMail(String to, String from, String subject, 
    		String content, String contentType,
            String userName, String programName, String eventName, 
            int priority, String contactUuid, String attachFilename, String attachFileContent) {
    	
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
        vo.setAttachFilename(attachFilename);
        vo.setAttachFileContent(attachFileContent);
        
        try{
        	this.doSendMessage(vo);
        }catch(JMSException e){
        	throw new EJBException("Exceptions in JMS Channel");
        }
    }

    /**
     * Send mail.
     * @param messageEntity message
     * @throws JMSException the JMS exception
     * 
     * @since DRAS-6.1
     */
    public void sendMail(MessageEntity messageEntity)  {
    	try{
    		doSendMessage(messageEntity);
    	}catch(JMSException e){
        	throw new EJBException("Exceptions in JMS Channel");
        }
    }
    
    
    /**
     * Send envoy notification.
     * 
     * @param contacts the contacts
     * @param userName the user name
     * @param subject the subject
     * @param nm the nm
     * @param params the params
     * @param isTest the is test
     * 
     * @throws JMSException the JMS exception
     */
    public  void sendEnvoyNotification(List<Contact> contacts, String userName,
		String subject, NotificationMethod nm, NotificationParametersVO params,
		boolean isTest) throws  JMSException {

		final StringBuilder builder = new StringBuilder(VaroliiUtil.getHeadXml());
        builder.append(VaroliiUtil.getMessageXml(params, subject));
        builder.append(VaroliiUtil.getContactXml(contacts));
        builder.append(VaroliiUtil.getBlockXmlWithDeliveryReqArg(contacts, nm));
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
		log.info(logEntry);
//		log.debug("XML Templete : " + sb.toString());
        sendEnvoyMessage(userName, subject, xmlString, isTest, null);
    }

    public  void sendVaroliiNotification(List<Contact> contacts, String userName,
		String subject, NotificationMethod nm, NotificationParametersVO params,
		boolean isTest) throws  JMSException {

		final StringBuilder builder = new StringBuilder(VaroliiUtil.getHeadXml());
        builder.append(VaroliiUtil.getHecoEventStartMessageXml(params, subject));
        builder.append(VaroliiUtil.getHecoContactXml(contacts, userName));
        builder.append(VaroliiUtil.getBlockXml(contacts, nm));
        builder.append("</Job>\n</Request>");

        final String xmlString = builder.toString();

        StringBuilder sb = new StringBuilder();
        for (Contact contact : contacts) {
            sb.append(contact);
            sb.append("\n");
        }
        sb.append(xmlString);
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setDescription("sending varolii notification to " + userName);
		logEntry.setLongDescr(sb.toString());
		logEntry.setCategory(LogUtils.CATAGORY_NOTIFICATION);
		log.info(logEntry);

        sendEnvoyMessage(userName, subject, xmlString, isTest, "heco");
    }

    public  void sendVaroliiNotification2(List<Contact> contacts, String userName,
		String subject, NotificationMethod nm, NotificationParametersVO params,
		boolean isTest) throws  JMSException {

		final StringBuilder builder = new StringBuilder(VaroliiUtil.getHeadXml());
        builder.append(VaroliiUtil.getHecoNotificationMessageXml(params, subject));
        builder.append(VaroliiUtil.getHecoContactXml(contacts, userName));
        builder.append(VaroliiUtil.getBlockXml(contacts, nm));
        builder.append("</Job>\n</Request>");

        final String xmlString = builder.toString();

        StringBuilder sb = new StringBuilder();
        for (Contact contact : contacts) {
            sb.append(contact);
            sb.append("\n");
        }
        sb.append(xmlString);
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setDescription("sending varolii notification to " + userName);
		logEntry.setLongDescr(sb.toString());
		logEntry.setCategory(LogUtils.CATAGORY_NOTIFICATION);
		log.info(logEntry);

        sendEnvoyMessage(userName, subject, xmlString, isTest, "heco");
    }

    /**
     * Send envoy message.
     *
     * @param userName the user name
     * @param subject the subject
     * @param xmlString the xml string
     * @param isTest the is test
     * @param domain identifies varolii client
     */
    public  void sendEnvoyMessage(String userName, String subject, String xmlString, boolean isTest, String domain) {
       try {
            if (isTest) {
                final EnvoyBean bean = new EnvoyBean();
                if ("heco".equals(domain)) {
                    bean.sendEnvoyMessage(xmlString, domain);
                } else {
                    bean.sendEnvoyMessage(xmlString, null);
                }
            } else {
                final MessageEntity vo = new MessageEntity();
                vo.setSubject(subject);
                vo.setUserName(userName);
                vo.setContent(xmlString);
                vo.setCreationTime(new Date());
                vo.setTo("Varolii");    // DRMS-4327
                vo.setType(domain);
                this.doSendEnvoyMessage(vo);
            }
        } catch (JMSException e) {
        	log.error(LogUtils.createLogEntryUser(null, userName, 
        			LogUtils.CATAGORY_NOTIFICATION, "exception while sending envoy message", null));
        	log.debug(LogUtils.createExceptionLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, e));
        } catch (IOException e) {
        	log.error(LogUtils.createLogEntryUser(null, userName, 
        			LogUtils.CATAGORY_NOTIFICATION, "exception while sending envoy message", null));
        	log.debug(LogUtils.createExceptionLogEntry(null, LogUtils.CATAGORY_NOTIFICATION, e));
        }
    }

    // NOTE: since the names are not shown in the email template, we'll fake it.
    // NOTE: names are required fields in order to send envoy notifications.

    private static String getFromEmailAddr() {
        String result;
        EventStateCacheHelper esCache  = EventStateCacheHelper.getInstance();
        result = esCache.getFromEmailAddress();
        if(result == null){
        	try {
                CoreProperty emailFromAddress = corePropertyEAO.getByPropertyName("emailFromAddress");
                String stringValue = emailFromAddress.getStringValue();
                InternetAddress.parse(stringValue);
                result = stringValue;
                esCache.setFromEmailAddress(result);
            } catch (EntityNotFoundException e) {
                result = fromEmailAddr;
                esCache.setFromEmailAddress(result);
            } catch (AddressException e) {
                result = fromEmailAddr;
                esCache.setFromEmailAddress(result);
            }
        }
        return result;
    }

	@Override
	public void sendNotification(List<Contact> contacts, String userName,
			String subject, String content, String attachFilename,
			String attachFileContent, NotificationMethod method,
			NotificationParametersVO params, boolean internalOnly,
			String programName) {
    	try {
	    	sendNotification(contacts, userName, subject, content, null, method,
				params, internalOnly, true, true, programName, MessageEntity.PRIORITY_URGENT, attachFilename, attachFileContent);
    	
    	}catch(JMSException e){
    		throw new EJBException("Exceptions in JMS Channel");
    	}

	}
    
}
