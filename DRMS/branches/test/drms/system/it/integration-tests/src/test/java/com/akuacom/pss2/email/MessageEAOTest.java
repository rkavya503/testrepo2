package com.akuacom.pss2.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.akuacom.ejb.AbstractBaseEAOTest;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.test.TestUtil;

/**
 * Message EAO test cases
 */
public class MessageEAOTest extends AbstractBaseEAOTest<MessageEAO, MessageEntity> {

	public MessageEAOTest() {
		super(MessageEAO.class);
	}

	private final String SUPPORT_EMAIL = "support@akuacom.com";

	@Before
	public void setup() throws EntityNotFoundException {
		// delete all messages
		for (MessageEntity entity : eao.getAll()) {
			eao.delete(entity);
		}
	}

	@Override
	protected void assertEntityValuesNotEquals(MessageEntity created,
			MessageEntity found) {
		assertTrue(!found.getContent().equals(created.getContent()));
	}

	@Override
	protected void mutate(MessageEntity found) {
		found.setContent(TestUtil.generateRandomString());
	}

	@Override
	protected void assertEntityValuesEquals(MessageEntity created,
			MessageEntity found) {
		assertEquals(found.getContent(), created.getContent());
	}

	@Test
	public void testFindByDestination() throws DuplicateKeyException {
		String subject = "test message for testFindByDestination "
				+ UUID.randomUUID();
		String recipients = "daoping.zhang@honeywell.com";
		for (int i = 0; i < 1; i++) {
			if (i > 5)
				subject = subject + i;
			MessageEntity msg1 = generateRandomEntity();
			msg1.setTo(recipients);
			msg1 = eao.create(msg1);
		}
		List<MessageEntity> entity = eao.findByDestination(recipients);
		assertEquals(1, entity.size());
	}

	@Test
	public void testFindMsgCountAtPriority() throws DuplicateKeyException,
			EntityNotFoundException {

		String subject = "test message for testFindMsgCountBelowUpperPriorityLimit ";
		String recipients = "daoping.zhang@honeywell.com";
		String anotherone = "anotherone@honeywell.com";
		String thirdone = "thirdone@honeywell.com";
		String content = "this is a test message for testFindSimilarMsgCountBelowUpperPriorityLimit";

		Date start = new Date();

		// message 1
		MessageEntity msg1 = createMessage(SUPPORT_EMAIL, subject + " 1",
				recipients, content, MessageEntity.STS_NEW);
		msg1.setSentTime(start);
		msg1 = eao.create(msg1);

		MessageEntity msg2 = createMessage(SUPPORT_EMAIL, subject + " 2",
				anotherone, content, MessageEntity.STS_FAILED);
		msg2.setSentTime(start);
		msg2 = eao.create(msg2);

		MessageEntity msg3 = createMessage(SUPPORT_EMAIL, subject + " 3",
				recipients + "," + anotherone, content, MessageEntity.STS_SENT);
		msg3.setSentTime(start);
		msg3 = eao.create(msg3);
		MessageEntity msg4 = createMessage(SUPPORT_EMAIL, subject + " 4",
				anotherone + "," + thirdone, content, MessageEntity.STS_SENT);
		msg4.setSentTime(start);
		msg4 = eao.create(msg4);

		MessageEntity msg5 = createMessage(SUPPORT_EMAIL, subject + " 5",
				anotherone + "," + thirdone, content, MessageEntity.STS_SENT);
		msg5.setPriority(MessageEntity.PRIORITY_URGENT);
		msg5.setSentTime(start);
		msg5 = eao.create(msg5);

		long count = eao.getMessageCount(recipients,
				MessageEntity.PRIORITY_NORMAL, start);
		assertEquals(2, count);

		count = eao.getMessageCount(anotherone, MessageEntity.PRIORITY_NORMAL,
				start);
		assertEquals(3, count);

		count = eao.getMessageCount(anotherone, MessageEntity.PRIORITY_URGENT,
				start);
		assertEquals(1, count);

		count = eao.getMessageCount(thirdone, MessageEntity.PRIORITY_NORMAL,
				start);
		assertEquals(1, count);

		count = eao.getMessageCount(thirdone, MessageEntity.PRIORITY_URGENT,
				start);
		assertEquals(1, count);
	}

	protected MessageEntity createMessage(String from, String subject,
			String recipients, String content, int status) {
		MessageEntity msg = new MessageEntity();
		msg.setFrom(from);
		msg.setSubject(subject);
		msg.setContent(content);
		msg.setTo(recipients);
		msg.setStatus(status);
		return msg;
	}

	@Test
	public void testFindByDateRange() throws DuplicateKeyException {
		String subject = "test message for testFindByDateRange "
				+ UUID.randomUUID();
		Date started = new Date();
		for (int i = 0; i < 10; i++) {
			if (i > 5)
				subject = subject + i;
			MessageEntity msg1 = generateRandomEntity();
			msg1.setCreationTime(new Date());
			msg1 = eao.create(msg1);
		}

		List<MessageEntity> entity = eao.findByDateRange(started, new Date());

		assertEquals(10, entity.size());
	}

	@Test
	public void testFindBySubject() throws DuplicateKeyException, EntityNotFoundException {
		String subject = "test message for testFindBySubject "
				+ UUID.randomUUID();
		MessageEntity msg = generateRandomEntity();
		msg.setSubject(subject);
		msg.setCreationTime(new Date());
		msg = eao.create(msg);

		List<MessageEntity> entity = eao.findBySubject(subject);

		assertEquals(1, entity.size());
		
		eao.delete(msg);
		
	}
	
	@Override
	protected MessageEntity generateRandomEntity() {
		MessageEntity message = new MessageEntityTest().generateRandomIncompleteEntity();

		assertNotNull(message);
		return message;
	}

}
