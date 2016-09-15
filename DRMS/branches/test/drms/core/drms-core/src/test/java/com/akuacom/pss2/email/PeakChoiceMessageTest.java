package com.akuacom.pss2.email;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.test.TestUtil;

/**
 * PeakChoiceMessageTest is the unit test class for the PeakChoiceMessage Entity.
 * 
 * @author Brian Chapman
 * 
 *         Initial date 2010.11.01
 */
public class PeakChoiceMessageTest extends BaseEntityFixture<PeakChoiceMessageEntity> {

	@Override
	public PeakChoiceMessageEntity generateRandomIncompleteEntity() {
		PeakChoiceMessageEntity message = new PeakChoiceMessageEntity();

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

		return message;
	}

}
