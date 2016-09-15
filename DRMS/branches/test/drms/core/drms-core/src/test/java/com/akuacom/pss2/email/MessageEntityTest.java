package com.akuacom.pss2.email;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.test.TestUtil;

/**
 * MessageTest is the unit test class for the Message Entity.
 * 
 * @author Brian Chapman
 * 
 */
public class MessageEntityTest extends BaseEntityFixture<MessageEntity> {

	@Override
	public MessageEntity generateRandomIncompleteEntity() {
		MessageEntity message = new MessageEntity();

		String from = "support@akuacom.com";
		message.setFrom(from);
		assertEquals(from, message.getFrom());

		String to = "test_recipient@akuacom.com";
		message.setTo(to);
		assertEquals(to, message.getTo());

		String type = TestUtil.generateRandomStringOfLength(45);
		message.setType(type);
		assertEquals(type, message.getType());

		String subject = TestUtil.generateRandomString();
		message.setSubject(subject);
		assertEquals(subject, message.getSubject());

		String content = TestUtil.generateRandomString();
		message.setContent(content);
		assertEquals(content, message.getContent());

		String userName = TestUtil.generateRandomStringOfLength(64);
		message.setUserName(userName);
		assertEquals(userName, message.getUserName());

		String programName = TestUtil.generateRandomString();
		message.setProgramName(programName);
		assertEquals(programName, message.getProgramName());

		String eventName = TestUtil.generateRandomStringOfLength(64);
		message.setEventName(eventName);
		assertEquals(eventName, message.getEventName());

		String contentType = TestUtil.generateRandomStringOfLength(64);
		message.setContentType(contentType);
		assertEquals(contentType, message.getContentType());

		int status = MessageEntity.STS_NEW;
		message.setStatus(status);
		assertEquals(status, message.getStatus());

		int priority = MessageEntity.PRIORITY_NORMAL;
		message.setPriority(priority);
		assertEquals(priority, message.getPriority());

		Date sentTime = TestUtil.generateRandomDate();
		message.setSentTime(sentTime);
		assertEquals(sentTime, message.getSentTime());

		return message;
	}

}
