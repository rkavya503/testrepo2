package com.akuacom.pss2.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.contact.Contact;

/**
 * Mock Message DAO 
 */
public class MockMessageDAO extends InMemoryEAO<MessageEntity> implements MessageEAO.R, MessageEAO.L {

	@Override
	public List<MessageEntity> findByUserName(String userName) {
		return null;
	}
	
	
	@Override
	public MessageEntity create(MessageEntity entity)
			throws DuplicateKeyException {
		if(entity.getCreationTime()!=null)
			entity.setSentTime(entity.getCreationTime());
		return super.create(entity);
	}
	
	
	@Override
	public List<MessageEntity> findByDestination(String recipients) {
		List<MessageEntity> results= new ArrayList<MessageEntity>();
		for(MessageEntity entity:inmemorydb){
			if(entity.getTo().indexOf(recipients.trim())>=0)
				results.add(entity);
		}
		return results;
	}

	@Override
	public MessageEntity update(MessageEntity entity)
			throws EntityNotFoundException {
		MessageEntity entity1=super.update(entity);
		//to facilitate testing
		if(entity.getStatus()==MessageEntity.STS_FAILED ||
				entity.getStatus()==MessageEntity.STS_SENT)
			entity.setSentTime(entity.getCreationTime());
		return entity;
	}

	@Override
	public long getMessageCount(String recipient, int priority, Date from) {
		List<MessageEntity> results= new ArrayList<MessageEntity>();
		for(MessageEntity entity:inmemorydb){
			if(entity.getSentTime()!=null && entity.getSentTime().getTime()>=from.getTime()
					&& entity.getTo().indexOf(recipient.trim())>=0
					&& entity.getPriority()==priority){
					//&& entity.getStatus() == MessageEntity.STS_SENT) {
				results.add(entity);
			}
		}
		return results.size();
	}
		
	@Override
	public List<MessageEntity> findByDateRange(Date start, Date end){
		
		List<MessageEntity> results= new ArrayList<MessageEntity>();
		
		for(MessageEntity entity:inmemorydb){
			if(entity.getCreationTime().getTime()>= start.getTime() && entity.getCreationTime().getTime() <= end.getTime()) {
				results.add(entity);
			}
		}
		return results;
	}
	
	@Override
	public Object[] logMessage(MessageEntity messageEntity, Date from)
			throws DuplicateKeyException {
		
		messageEntity = create(messageEntity);
		return new Object[]{messageEntity,
				getMessageCountToContact(messageEntity.getTo(), messageEntity.getPriority(),messageEntity.getContactId(), from)};
	}

    @Override
    public void clearMessage(Date threshold) {
        //
    }
    
	@Override
	public long getMessageCountToContact(String recipient, int priority,
			String contactId, Date from) {
		List<MessageEntity> results= new ArrayList<MessageEntity>();
		
		for(MessageEntity entity:inmemorydb){
			if(entity.getSentTime()!=null && entity.getSentTime().getTime()>=from.getTime()
					&& entity.getTo().indexOf(recipient.trim())>=0
					&& entity.getPriority()==priority
					&& equals(entity.getContactId(),contactId))	{
					//&& entity.getStatus() == MessageEntity.STS_SENT) {
				results.add(entity);
			}
		}
		return results.size();
	}
	
	private boolean equals(String str1,String str2){
		if(str1==null && str2==null)
			return true;
		if(str1!=null)
			return str1.equalsIgnoreCase(str2);
		if(str2!=null)
			return str2.equalsIgnoreCase(str1);
		return false;
	}


	@Override
	public List<MessageEntity> findBySubject(String subject) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.akuacom.pss2.email.MessageEAO#findByDigest(java.util.Date, java.util.Date, java.lang.String, java.lang.String)
	 */
	@Override
	public List<MessageEntity> findByDigest(Date start, Date end, String to,
			String userName) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.akuacom.pss2.email.MessageEAO#findDigestMessage(java.util.Date, java.util.Date, java.util.List)
	 */
	@Override
	public List<MessageEntity> findDigestMessage(Date start, Date end,
			List<String> operators) {
		// TODO Auto-generated method stub
		return null;
	}
}