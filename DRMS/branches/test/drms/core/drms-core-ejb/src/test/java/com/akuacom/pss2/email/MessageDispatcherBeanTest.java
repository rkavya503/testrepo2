package com.akuacom.pss2.email;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.nssettings.NSSettingsEAO;
import com.akuacom.pss2.nssettings.NSSettings;
import com.akuacom.pss2.participant.contact.ParticipantContactEAO;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;


public class MessageDispatcherBeanTest {

	protected MessageSender         msgSender;
	protected MessageDispatcherBean msgDispatcher;
	protected MockMessageDAO        messagedao;
	protected NSSettingsEAO.L       preferenceEAO;
	protected ContactManager.L        contactManager;
	protected ProgramManager.L        programManager;
	protected ParticipantContactEAO.L participantContactEAO;
	protected Date                  start;
	protected Contact               contact;
	
	private static final String SUPPORT_EMAIL = "drassupport@akuacom.com";
	private String programName ="test program";
	private String drasOperator="dramOperator@gmail.com";
	private String programOperator="programOperator@gmail.com";
	private String clientOperator1 ="clientOperator1@gmail.com";
	private String clientOperator2 ="clientOperator2@gmail.com";
	
	protected List<MessageEntity> messageList = new ArrayList<MessageEntity>();

	protected MessageEntity addMessage(String from, String subject,
			String recipients, String content, Date date,String programName, String contactUuid) {
		MessageEntity msg = new MessageEntity();
		msg.setFrom(from);
		msg.setSubject(subject);
		msg.setContent(content);
		msg.setTo(recipients);
		msg.setCreationTime(date);
		msg.setStatus(MessageEntity.STS_NEW);
		msg.setProgramName(programName);
		msg.setContactId(contactUuid);
		
		messageList.add(msg);
		return msg;
	}
	
	/**
	 * @param message message
	 * @param timeElapse in second
	 * @return message
	 */
	protected MessageEntity addSimilarMessage(MessageEntity message,
			long timeElapse) {
		MessageEntity duplicate = new MessageEntity();
		duplicate.setSubject(message.getSubject());
		duplicate.setContent(message.getContent());
		duplicate.setFrom(message.getFrom());
		duplicate.setTo(message.getTo());
		duplicate.setPriority(message.getPriority());
		duplicate.setStatus(MessageEntity.STS_NEW);
		duplicate.setCreationTime(new Date(message.getCreationTime().getTime()
				+ timeElapse * 1000));
		duplicate.setProgramName(message.getProgramName());
		duplicate.setContactId(message.getContactId());
		
		messageList.add(duplicate);
		return duplicate;
	}

	protected void dispatchMessages(List<MessageEntity> messageList,
			MessageDispatcher msgDispatcher) {
		for (MessageEntity message : messageList) {
			msgDispatcher.dispatchMessage(message);
		}
	}

	@SuppressWarnings("deprecation")
	@Before
	public void setup() {
		start = new Date();
		start.setHours(12);
		start.setMinutes(0);
		start.setSeconds(0);
		
		msgDispatcher = new MessageDispatcherBean();

		msgSender = EasyMock.createMock(MessageSender.class);
		messagedao = new MockMessageDAO();
		preferenceEAO = EasyMock.createMock(NSSettingsEAO.L.class);
		contactManager =EasyMock.createMock(ContactManager.L.class);
		participantContactEAO=EasyMock.createMock(ParticipantContactEAO.L.class);
		programManager =EasyMock.createMock(ProgramManager.L.class);
		
		msgDispatcher.setMessageEAO(messagedao);
		msgDispatcher.setMessageSender(msgSender);
		msgDispatcher.setPreferenceEAO(preferenceEAO);
		msgDispatcher.setContactManager(contactManager);
		msgDispatcher.setParticipantContactEAO(participantContactEAO);
		msgDispatcher.setProgramManager(programManager);
	}

	/**
	 * Every message will go out directly if global filer off
	 */
	@Test
	public void testNoBlockForClientOperatorWhenGlobalFilterOff() {
		doTestNoBlockWhentGlobalFilterOff(clientOperator1);
	}
	
	@Test
	public void testNoBlockForDrasOperatorWhenGlobalFilterOff() {
		doTestNoBlockWhentGlobalFilterOff(drasOperator);
	}
	
	@Test
	public void testNoBlockForProgramOperatorWhenGlobalFilterOff() {
		doTestNoBlockWhentGlobalFilterOff(programOperator);
	}
	
	protected void mockup(boolean filterOn,int sentTimes){
		// expect global filter settings
		NSSettings preference = new NSSettings();
		
		// global filter off
		preference.setFilterStatus(filterOn?1:0);
		//daily base
		preference.setFrequency(24*60);
		//similar message upper limit
		preference.setMsgThreshold(3);
		//exception contact list
		
		
		//start to record behavior
		preferenceEAO.getNSSettings();
		//return preference if getNSStrings is get called
		EasyMock.expectLastCall().andReturn(preference).anyTimes();
		
		
		contact = new Contact();
		contact.setAddress(programOperator);
		
		Program program = new Program();
		EasyMock.expect(programManager.getProgram(programName)).andReturn(program).anyTimes();
		EasyMock.expect(programManager.getOperatorContacts(programName)).andReturn(Arrays.asList(contact)).anyTimes();
		
		contact = new Contact();
		contact.setAddress(drasOperator);
		
		EasyMock.expect(contactManager.getOperatorContacts()).andReturn(
					Arrays.asList(contact)).anyTimes();
		
		//6 message are expected to send out
		//the sendMessage method will expect to call 6 times
		EasyMock.expect(
				msgSender.sendMessage(EasyMock.isA(MessageEntity.class)))
				.andReturn(true).times(sentTimes);
		
		//replay
		EasyMock.replay(preferenceEAO);
		EasyMock.replay(msgSender);
		EasyMock.replay(programManager);
		EasyMock.replay(contactManager);
	}
	
	protected void verifyMock(){
		EasyMock.verify(preferenceEAO);
		EasyMock.verify(msgSender);
		EasyMock.verify(contactManager);
		EasyMock.verify(programManager);
	}
	
	protected void doTestNoBlockWhentGlobalFilterOff(String address){
		//12:00:00
		MessageEntity msg = addMessage(SUPPORT_EMAIL, "Test No Block",
				address, "xx xx xx x", start,programName,null);
		//12:00:10
		addSimilarMessage(msg, 10);
		
		//12:00:20
		addSimilarMessage(msg, 20);
		
		addSimilarMessage(msg, 30);
		addSimilarMessage(msg, 40);
		addSimilarMessage(msg, 50);
		
		//filter off
		//6 messages expected to be sent
		mockup(false,6);
		//run
		dispatchMessages(messageList, msgDispatcher);
		
		//verification
		verifyMock();
		
		assertEquals(MessageEntity.STS_SENT, messageList.get(0).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(1).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(2).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(3).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(4).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(5).getStatus());
	}
	
	@Test
	public void testNoBlockForProgramOperator(){
		doTestNoBlockForContactsInExceptionList(programOperator);
	}
	
	@Test
	public void testNoBlockForDrasOperator(){
		doTestNoBlockForContactsInExceptionList(drasOperator);
	}
	
	/**
	 * Every message will go out directly if its destination is on the exception list
	 * even if the global frequency filter is on
     * @param address address
     */
	public void doTestNoBlockForContactsInExceptionList(String address) {
		// prepare message
		//default priority is Normal
		//12:00:00
		MessageEntity msg = addMessage(SUPPORT_EMAIL, "testNoBlockMsgForContactsInExceptionList1",
				address, "xx xx xx x", start,programName,null);
		//12:00:10
		addSimilarMessage(msg, 20);
		addSimilarMessage(msg, 30);
		addSimilarMessage(msg, 40);
		addSimilarMessage(msg, 50);
		addSimilarMessage(msg, 60);
		
		//filter on
		//6 messages expected to be sent
		mockup(true,6);

		//run 
		dispatchMessages(messageList, msgDispatcher);
		
		//verification
		verifyMock();
		
		assertEquals(MessageEntity.STS_SENT, messageList.get(0).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(1).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(2).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(3).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(4).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(5).getStatus());
	}
	
	/**
 	 * Message exceeding the threshold will be blocked
 	 * for external contacts the limit will follow threshold in global settings
	 */
	@Test
	public void testLimitMessageCountForExternalContacts() {
		String subject = "testBlockSimilarMsg";
		
		//12:00:00
		MessageEntity msg = addMessage(SUPPORT_EMAIL,subject,
				clientOperator1, "xx xx xx x", start,programName,null); 
		
									//0 - send
		//12:00:10
		addSimilarMessage(msg, 10); //1- send
		addSimilarMessage(msg, 20); //2- send
		
		addSimilarMessage(msg, 30); //3- block
		addSimilarMessage(msg, 40); //4- block
		MessageEntity msg1 =addSimilarMessage(msg, 50); 
								   // 5-block
		//different subject 
		addMessage(SUPPORT_EMAIL, "different subject",
				clientOperator1, "xx xx xx x", msg1.getCreationTime(),programName,null); 
									//6-block
		//different destination
		addMessage(SUPPORT_EMAIL, subject,
				clientOperator2, "xx xx xx x", msg1.getCreationTime(),programName,null);
								   //7-send 
		
		// same destination but with high priority
		msg1=addSimilarMessage(msg, 70); 
		msg1.setPriority(MessageEntity.PRIORITY_URGENT);   
								     //8-send
		addSimilarMessage(msg1, 20); //9- send
		addSimilarMessage(msg1, 30); //10- send
		addSimilarMessage(msg1, 40); //11- block
		
		//filter on, 7 messages to be sent
		mockup(true,7);
		
		//run
		dispatchMessages(messageList, msgDispatcher);
		
		verifyMock();
		
		assertEquals(MessageEntity.STS_SENT, messageList.get(0).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(1).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(2).getStatus());
		
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(3).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(4).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(5).getStatus());
		
		//different subject 
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(6).getStatus());
		
		//different destination
		assertEquals(MessageEntity.STS_SENT, messageList.get(7).getStatus());
		
		//urgent priority
		assertEquals(MessageEntity.STS_SENT, messageList.get(8).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(9).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(10).getStatus());
		
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(11).getStatus());
	}
	
	
	@Test
	public void testLimitMessageOnCountContactLevel() {
		String subject = "testBlockSimilarMsg";
		String contactId = "1";
		
		//12:00:00
		MessageEntity msg = addMessage(SUPPORT_EMAIL,subject,
				clientOperator1, "xx xx xx x", start,programName,contactId); 
									//0 - send
		//12:00:10
		addSimilarMessage(msg, 10); //1- send
		
		addSimilarMessage(msg, 20); //2- block
		addSimilarMessage(msg, 30); //3- block
		addSimilarMessage(msg, 40); //4- block
		MessageEntity msg1 =addSimilarMessage(msg, 50); 
								   // 5-block
		//different subject 
		addMessage(SUPPORT_EMAIL, "different subject",
				clientOperator1, "xx xx xx x", msg1.getCreationTime(),programName,contactId); 
									//6-block
		//different destination
		addMessage(SUPPORT_EMAIL, subject,
				clientOperator2, "xx xx xx x", msg1.getCreationTime(),programName,contactId);
								   //7-send 
		
		//same destination but with high priority
		msg1=addSimilarMessage(msg, 70); 
		msg1.setPriority(MessageEntity.PRIORITY_URGENT);   
								     //8-send
		addSimilarMessage(msg1, 20); //9- send
		
		addSimilarMessage(msg1, 30); //10- block
		addSimilarMessage(msg1, 40); //11- block
		
		//filter on, 5 messages to be sent
		mockup(true,5);
		//Contact level threshold is max 2 messages 
		EasyMock.expect(participantContactEAO.getMsgThreshold(contactId)).andReturn(2).anyTimes();
		EasyMock.replay(participantContactEAO);
		
		//run
		dispatchMessages(messageList, msgDispatcher);
		
		verifyMock();
		EasyMock.verify(participantContactEAO);
		
		assertEquals(MessageEntity.STS_SENT, messageList.get(0).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(1).getStatus());
		
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(2).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(3).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(4).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(5).getStatus());
		
		//different subject 
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(6).getStatus());
		
		//different destination
		assertEquals(MessageEntity.STS_SENT, messageList.get(7).getStatus());
		
		//urgent priority
		assertEquals(MessageEntity.STS_SENT, messageList.get(8).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(9).getStatus());
		
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(10).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(11).getStatus());
	}
	
	
	@Test
	public void testLimitMessageSameEmailBelongingToDifferentContact() {
		String subject = "testBlockSimilarMsg";
		String contactId1 = "contactId_1";
		String contactId2 = "contactId_2";
		
		//2 low priority 
		MessageEntity msg = addMessage(SUPPORT_EMAIL,subject,
				clientOperator1, "xx xx xx x", start,programName,contactId1); 
									//0 - send
		addSimilarMessage(msg, 10); //1 - send
		
		addSimilarMessage(msg, 20); //2- block
		addSimilarMessage(msg, 30); //3- block
		
		//same destination but with high priority
		MessageEntity msg1 =addSimilarMessage(msg, 50); 
		msg1.setPriority(MessageEntity.PRIORITY_URGENT);
								     //4 - send
		addSimilarMessage(msg1, 20); //5-  send
		addSimilarMessage(msg1, 30); //6- block
		
		//same email address belonging to contact2, threshold 3
		MessageEntity msg2 = addMessage(SUPPORT_EMAIL,subject,
				clientOperator1, "xx xx xx x", start,programName,contactId2); 
									 //7 -send
		addSimilarMessage(msg2, 10); //8- send
		addSimilarMessage(msg2, 20); //9- send
		
		addSimilarMessage(msg2, 30); //10- block
		
		//filter on, 7 messages to be sent
		mockup(true,7);
		//Contact level threshold is max 2 messages 
		EasyMock.expect(participantContactEAO.getMsgThreshold(contactId1)).andReturn(2).anyTimes();
		EasyMock.expect(participantContactEAO.getMsgThreshold(contactId2)).andReturn(3).anyTimes();
		
		EasyMock.replay(participantContactEAO);
		
		//run
		dispatchMessages(messageList, msgDispatcher);
		
		verifyMock();
		EasyMock.verify(participantContactEAO);
		
		assertEquals(MessageEntity.STS_SENT, messageList.get(0).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(1).getStatus());
		
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(2).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(3).getStatus());
		
		assertEquals(MessageEntity.STS_SENT, messageList.get(4).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(5).getStatus());
		
		//different subject 
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(6).getStatus());
		
		//same address, different contact
		assertEquals(MessageEntity.STS_SENT, messageList.get(7).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(8).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(9).getStatus());
		
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(10).getStatus());
	}
	
	/**
	 * message counter counts messages in same day from zero clock.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testBlockerExpireAfter24Clock() {
		Date date = new Date();
		date.setHours(12);
		date.setMinutes(0);
		date.setSeconds(0);
		
		//12:00:00
		MessageEntity msg = addMessage(SUPPORT_EMAIL, "Test No Block",
				"daoping.zhang@honeywell.com", "xx xx xx x", date,programName,null);
		MessageEntity msg1 ;
									// send
		//12:00:10
		addSimilarMessage(msg, 10); // send
		//12:00:20
		addSimilarMessage(msg, 20); // send

		// should block the coming similar messages
		//12:00:30
		addSimilarMessage(msg, 30); // block
		//12:00:40
		addSimilarMessage(msg, 40); // block
		//13:00:50
		addSimilarMessage(msg, 50); // block
		
		//23:59:00
		msg1=addSimilarMessage(msg, 11*60*60+59*60); // block
		System.out.println(msg1.getCreationTime());
		
		//00:01:00
		msg1=addSimilarMessage(msg, 12*60*60+60);   //send, Blocker no effect after 24:00:00
		System.out.println(msg1.getCreationTime());
		
		//00:02:00
		msg1=addSimilarMessage(msg, 12*60*60+2*60); //send, Blocker no effect after 24:00:00
		
		//00:03:00
		msg1=addSimilarMessage(msg, 12*60*60+3*60); //send, Blocker no effect after 24:00:00
		
		//00:03:00
		msg1=addSimilarMessage(msg, 12*60*60+4*60); //blocked, new  blocker triggered
		
		//filter on, 6 message to be send
		mockup(true,6);
		//run 
		dispatchMessages(messageList, msgDispatcher);
		//verify 
		verifyMock();
		

		assertEquals(MessageEntity.STS_SENT, messageList.get(0).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(1).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(2).getStatus());
		
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(3).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(4).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(5).getStatus());
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(6).getStatus());
		
		assertEquals(MessageEntity.STS_SENT, messageList.get(7).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(8).getStatus());
		assertEquals(MessageEntity.STS_SENT, messageList.get(9).getStatus());
		
		assertEquals(MessageEntity.STS_SUSPENDED, messageList.get(10).getStatus());
	}
	
}
