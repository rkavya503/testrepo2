package com.akuacom.pss2.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.nssettings.NSSettingsEAO;
import com.akuacom.pss2.nssettings.NSSettings;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.ejb.jboss.test.JBossFixture;

/**
 * MessageDispatcher Integration Test
 * 
 * @author Daoping Zhang
 */
public class MessageDispatcherTest extends JBossFixture {

	private final   String SUPPORT_EMAIL = "support@akuacom.com";
	private static  String OPERATOR_EMAIL;
	private static  int MSG_THRESHOLD= 3;
	
	protected MessageDispatcher dispatcher = JBossFixture.lookupSessionRemote(MessageDispatcher.class);
	
	protected MessageEAO messageEAO = JBossFixture.lookupSessionRemote(MessageEAO.class);
	
	protected ProgramManager programManager = JBossFixture.lookupSessionRemote(ProgramManager.class);
	
	protected ParticipantManager participantManager = JBossFixture.lookupSessionRemote(ParticipantManager.class);
	
	//this is a test email address
	//password is Test_1234
	String to = "drastest@gmail.com";
	String from = SUPPORT_EMAIL;
	
	// make sure the mssage subject is different for each test
	String subject = UUID.randomUUID() + ": this is a test message";
	String subjectDifferent = UUID.randomUUID()
			+ ": this is a test message with different subject";
	
	String contentType = "text/plain";
	String content = "test message send from test client";
	String userName = "daoping";
	String programName = "program";
	String eventName = "event";
	
	
	
	@BeforeClass
	public static void setupForAll() {
		NSSettingsEAO nsEAO = JBossFixture.lookupSessionRemote(NSSettingsEAO.class);
		NSSettings setting = nsEAO.getNSSettings();
		if (setting == null)
			setting = new NSSettings();
			
		// one day
		setting.setFrequency(1440);
		//open the filter
		setting.setFilterStatus(1);
		// one day
		setting.setDuration(1440);
		// max 3 similar message are allowed
		setting.setMsgThreshold(MSG_THRESHOLD);
		
		nsEAO.saveNSSettings(setting);
		
		List<Contact> list=JBossFixture.lookupSessionRemote(ContactManager.class).getOperatorContacts();
		if(list.size()>0){
			OPERATOR_EMAIL = list.get(0).getAddress();
		}
	}
	
	@Before 
	public void setup() throws EntityNotFoundException{
		//delete all messages
		for(MessageEntity entity:messageEAO.getAll()){
			messageEAO.delete(entity);
		}
		
		//setup program
		Program program = new Program();
		program.setProgramName("demo1 program");
		program.setAutoAccept(true);
	}
	
	private Matcher<Integer> anyOf(final int i, final int j) {
		return new BaseMatcher<Integer>() {
			@Override
			public boolean matches(Object value) {
				return (Integer) value == i || (Integer) value == j;
			}
			
			@Override
			public void describeTo(Description arg0) {
				
			}
		};
	}
	
	
	@Test
	public void testLimitMessagePerCountPerDayAtEachPriority() throws NamingException, JMSException {
		for (int i = 0; i < 4; i++) {
			MessageEntity message = new MessageEntity();
			message.setFrom(from);
			message.setTo(to);
			message.setStatus(MessageEntity.STS_NEW);
			message.setSubject(subject);
			message.setContent(content);
			message.setContentType(contentType);

			dispatcher.dispatchMessage(message);
		}
		
		//add a message with a different subject
		//this message should be blocked
		MessageEntity message1 = new MessageEntity();
		message1.setFrom(from);
		message1.setTo(to);
		message1.setStatus(MessageEntity.STS_NEW);
		message1.setSubject(subjectDifferent);
		message1.setContent(content);
		message1.setContentType(contentType);
		dispatcher.dispatchMessage(message1);
		
		//add a message with an URGENT priority 
		for (int i = 0; i < 4; i++) {
			//this message should go out directly
			MessageEntity message2 = new MessageEntity();
			message2.setFrom(from);
			message2.setTo(to);
			message2.setStatus(MessageEntity.STS_NEW);
			message2.setSubject(subject + "__"+ Math.random());
			message2.setContent(content);
			message2.setPriority(MessageEntity.PRIORITY_URGENT);
			message2.setContentType(contentType);
			dispatcher.dispatchMessage(message2);
		}
		
		
		List<MessageEntity> messages = messageEAO.findByDestination(to);
		assertEquals(9, messages.size());
		
		int countNormalSent = 0;
		int countUrgentSent = 0;
		
		for (int i = 0; i < 9; i++) {
			int status = messages.get(i).getStatus();
			if(status == MessageEntity.STS_SENT || status==MessageEntity.STS_FAILED) {
				if(messages.get(i).getPriority()==MessageEntity.PRIORITY_NORMAL)
					countNormalSent++;
				else
					countUrgentSent++;
			}
		}
		
		assertTrue("normal count(" +countNormalSent +") should <= threshold("+MSG_THRESHOLD+")",countNormalSent<=MSG_THRESHOLD);
		assertTrue("urgent count(" +countUrgentSent +") should <= threshold("+MSG_THRESHOLD+")",countUrgentSent<=MSG_THRESHOLD);
		
	}
	
	@Test
	public void testLimitMessageOnContactLevel() throws NamingException, JMSException {
		String contactIdA ="contact A";
		String contactIdB ="contact B";
		
		for (int i = 0; i < 4; i++) {
			MessageEntity message = new MessageEntity();
			message.setFrom(from);
			message.setTo(to);
			message.setStatus(MessageEntity.STS_NEW);
			message.setSubject(subject);
			message.setContent(content);
			message.setContentType(contentType);
			message.setContactId(contactIdA);
			dispatcher.dispatchMessage(message);
		}
		
		//add a message which sent to different contact but to same email address
		for (int i = 0; i < 4; i++) {
			//this message should go out directly
			MessageEntity message2 = new MessageEntity();
			message2.setFrom(from);
			message2.setTo(to);
			message2.setStatus(MessageEntity.STS_NEW);
			message2.setSubject(subject + "__"+ Math.random());
			message2.setContent(content);
			message2.setContentType(contentType);
			message2.setContactId(contactIdB);
			dispatcher.dispatchMessage(message2);
		}
		
		List<MessageEntity> messages = messageEAO.findByDestination(to);
		assertEquals(8, messages.size());
		
		int sentToContactA = 0;
		int sentToContactB = 0;
		
		for (int i = 0; i < 8; i++) {
			int status = messages.get(i).getStatus();
			if(status == MessageEntity.STS_SENT || status==MessageEntity.STS_FAILED) {
				if(messages.get(i).getContactId().equals(contactIdA))
					sentToContactA++;
				else if(messages.get(i).getContactId().equals(contactIdB))
					sentToContactB++;
			}
		}
		
		assertTrue("Contact A count(" +sentToContactA +") should <= threshold("+MSG_THRESHOLD+")",sentToContactA<=MSG_THRESHOLD);
		assertTrue("Contact B count(" +sentToContactB +") should <= threshold("+MSG_THRESHOLD+")",sentToContactB<=MSG_THRESHOLD);
		
	}
	
	@Ignore
	@Test
	//Open this this only for debug purpose
	public void testDeliverMessageConcurrently() throws NamingException, JMSException{
		for (int i = 0; i < 10; i++) {
			
			MessageEntity message = new MessageEntity();
			message.setFrom(from);
			message.setTo(to);
			message.setStatus(MessageEntity.STS_NEW);
			message.setSubject(subject);
			message.setContent(content);
			message.setContentType(contentType);
			
			if(i%2==0)
				message.setPriority(MessageEntity.PRIORITY_URGENT);
			else
				message.setPriority(MessageEntity.PRIORITY_NORMAL);
			
			//send message, 
			MailUtil.sendMail(message);
		}
		
		
		
		int countNormalSent = 0;
		int countUrgentSent = 0;
		
		//hold on for a while 
		System.out.println("wait for a while... ");
		long start = System.currentTimeMillis();
		for(double i = 0; i <10000000000.00;i++){
			double t = 234.1111/1.1*4.222;
		}
		System.out.println(System.currentTimeMillis()-start +" ms passed,Check the result now ");
		
		List<MessageEntity> messages = messageEAO.findByDestination(to);
		int size = messages.size();
		
		for (int i = 0; i < size; i++) {
			int status = messages.get(i).getStatus();
			
			if(status == MessageEntity.STS_SENT || status==MessageEntity.STS_FAILED) {
				if(messages.get(i).getPriority()==MessageEntity.PRIORITY_NORMAL)
					countNormalSent++;
				else 
					countUrgentSent++;
			}
		}
		
		assertTrue("The success count should <= threshold",countUrgentSent<=MSG_THRESHOLD);
		assertTrue("The success count should <= threshold",countNormalSent<=MSG_THRESHOLD);
	}
	
}
