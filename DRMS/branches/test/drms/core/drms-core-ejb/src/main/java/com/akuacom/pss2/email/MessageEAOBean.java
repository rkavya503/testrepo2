/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.pss2.contact.Contact;

/**
 * EAO Bean implementation for {@link com.akuacom.pss2.email.MessageEntity} 
 * 
 * @see com.akuacom.pss2.email.MessageEntity
 */

@Stateless
public class MessageEAOBean extends MessageEntityGenEAOBean implements MessageEAO.R, MessageEAO.L {
	
    public MessageEAOBean() {
        super(MessageEntity.class);
    }
    
    @SuppressWarnings({"unchecked"})
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<MessageEntity> findByUserName(String userName) {
        final Query namedQuery = em.createNamedQuery("MessageEntity.findByUserName")
                .setParameter("userName", userName);
        return namedQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MessageEntity> findByDestination(String recipient) {
		 final Query namedQuery = em.createNamedQuery("MessageEntity.findByDestination")
         .setParameter("to", "%"+recipient.toUpperCase()+"%");
		 return namedQuery.getResultList();
	}
    
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public long getMessageCount(String recipient, int priority, Date from) {
		final Query namedQuery = em.createNamedQuery("MessageEntity.getMsgCountAtPriority")
		//count all messages despise its status
		//.setParameter("status", MessageEntity.STS_SENT)
        .setParameter("to", "%"+recipient.toUpperCase()+"%")
        .setParameter("priority", priority)
        .setParameter("sentTime", from);
		return (Long)namedQuery.getSingleResult();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public long getMessageCountToContact(String recipient, int priority,
			String contactId, Date from) {
		final Query namedQuery = em.createNamedQuery("MessageEntity.getMsgCountToContact")
		//count all messages despise its status
		//.setParameter("status", MessageEntity.STS_SENT)
        .setParameter("to", "%"+recipient.toUpperCase()+"%")
        .setParameter("priority", priority)
        .setParameter("sentTime", from)
        .setParameter("contact", contactId);
		return (Long)namedQuery.getSingleResult();
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<MessageEntity> findByDateRange(Date start, Date end){
		 final Query namedQuery = em.createNamedQuery("MessageEntity.findByDateRange")
         .setParameter("start", start)
         .setParameter("end", end);
		 return namedQuery.getResultList();	
    }
	
	@Override
	public Object[] logMessage(MessageEntity messageEntity, Date from) throws DuplicateKeyException {
		messageEntity=create(messageEntity);
		//no specific contact 
		String contact =messageEntity.getContactId();
		if(contact!=null) {
			contact=contact.toUpperCase().trim();
			return new Object[]{messageEntity, 
				getMessageCountToContact(messageEntity.getTo(),messageEntity.getPriority(),
						contact,from)};
		}
		else
			//if contactId = null, named query MessageEntity.getMsgCountToContact will always return  0
			//the correct statement is contact is null
			return new Object[]{messageEntity,
				getMessageCount(messageEntity.getTo(),messageEntity.getPriority(),from)};
	}
	
    @Override
    public void clearMessage(Date threshold) {
        final Query query = em.createNamedQuery("MessageEntity.clearMessage").setParameter("sentTime", threshold);
        query.executeUpdate();
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageEntity> findBySubject(String subject) {
		final Query namedQuery = em.createNamedQuery(
				"MessageEntity.findBySubject").setParameter("subject", subject);
		return namedQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MessageEntity> findByDigest(Date start, Date end,String to,String userName) {
		final Query namedQuery = em.createNamedQuery("MessageEntity.findByDigest")
		//count all messages despise its status
		//.setParameter("status", MessageEntity.STS_SENT)
		.setParameter("start", start)
        .setParameter("end", end)
		.setParameter("to", to)
        .setParameter("userName", userName);
		return namedQuery.getResultList();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MessageEntity> findDigestMessage(Date start, Date end,List<String> operators){
		
		
		
		final Query namedQuery = em.createNamedQuery("MessageEntity.findDigestMessage")
		//count all messages despise its status
		//.setParameter("status", MessageEntity.STS_SENT)
		.setParameter("start", start)
        .setParameter("end", end)
        .setParameter("operators", operators);
		return namedQuery.getResultList();
	}
}
