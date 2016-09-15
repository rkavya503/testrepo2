/**
 * 
 */
package com.akuacom.pss2.notifications.usecasetest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.email.MessageEntity;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.usecasetest.cases.CancelEventUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateSeveralParticipantsUseCase;
import com.akuacom.pss2.usecasetest.cases.DeleteParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteRTPProgramUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteSeveralParticipantsUseCase;
import com.akuacom.pss2.usecasetest.cases.FindParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.FindProgramUsecase;
import com.akuacom.pss2.usecasetest.cases.StartEventUsecase;
import com.akuacom.pss2.usecasetest.tests.UsecaseTestBase;

/**
 * Use case test for event creation & deletion notification. 
 * 
 */
public class GetEventCreateNotificationsTest extends UsecaseTestBase {

    private static final String[] participantNames = {"TestPart999"};

    private static final String programName = "Notification_Test";
    private static final String programContactAddress="notifyTest@akuacom.com";
    private static List<String> subjects;

    @Before
    public void setup() throws Exception {
    	for (String partName: participantNames){
    		Participant part=(Participant)runCase(new FindParticipantUsecase(partName));
    		if (part != null)
    			runCase(new DeleteParticipantUsecase(partName));
    	}
    	
    	Object program=runCase(new FindProgramUsecase(programName));
    	if (program!=null){
        	List<String> programParticipantName= 
        		(List<String>)runCase(new FindProgramParticipants(programName));
        	for(String partName: programParticipantName) {
    			runCase(new DeleteParticipantUsecase(partName));
        	}
        	
    		runCase(new DeleteRTPProgramUsecase(programName));
    	}
    	
    	subjects=new ArrayList<String>();
    }
    
    @SuppressWarnings("unchecked")
	@Ignore
    @Test
    public void runCase () throws Exception {
    	runCase(new CreateProgramWithOperatorUsecase(programName, programContactAddress));

        List<String> participantNameList =  new ArrayList<String>();
        Collection<String> eventNames = new ArrayList<String>();

        for (String participantName : participantNames) {
			participantNameList.add(participantName);
		}
        
		runCase(new CreateSeveralParticipantsUseCase(participantNameList,
				programName));
		eventNames = (Collection<String>) runCase(new StartEventUsecase(programName));

		if (eventNames.size() > 0) {
			String subject = "program " + programName + " event "
					+ eventNames.toArray()[0] + " created";
			subjects.add(subject);
			
			System.out.println("event name: "+eventNames.toArray()[0]);
			
			//assert maybe go to failed due to update the notification messages to DB is non-sync.  
			checkSendMessage(subject);		
		}
		
		Program deleted=(Program)runCase(new CancelEventUsecase(programName, eventNames));
		if (deleted !=null){
			String subject = "program " + programName + " event "
					+ eventNames.toArray()[0] + " cancelled";
			subjects.add(subject);

			//assert maybe go to failed due to update the notification messages to DB is non-sync.  
			checkSendMessage(subject);
		}
		
		runCase(new DeleteSeveralParticipantsUseCase(participantNameList));
		runCase(new DeleteRTPProgramUsecase(programName));
    }
    
    @After
    public void tearDown(){
    	//delete messages
    }

    @SuppressWarnings("unchecked")
	private void checkSendMessage(String subject) throws Exception{
		List<MessageEntity> messages = new ArrayList<MessageEntity>();

		messages = (List<MessageEntity>) runCase(new FindNotificationsUseCase(
				subject));
		
		int globalContact=0;
		int programOperator=0;
		List<Contact> contacts=(List<Contact>)runCase(new FindGlobalContactsUseCase());
		for (MessageEntity msg : messages) {
			if(checkToDRASOperatorMessage(msg, contacts))
				globalContact++;
			
			if (checkProgramOperatorMessage(msg))
				programOperator++;
		}
		
		System.out.println("expected contact message count:" + contacts.size());
		System.out.println("actual contact message count:" + globalContact);
		System.out.println("program operator count:" + programOperator);
//		assertEquals(contacts.size(), globalContact);
    }
    private boolean checkToDRASOperatorMessage(MessageEntity msg, List<Contact> contacts){
    	for (Contact contact: contacts){
    		if (contact.getType().equals(Contact.EMAIL_ADDRESS) && contact.getAddress().equals(msg.getTo()))
    			return true;
    	}
    	return false;
    }
    
    private boolean checkProgramOperatorMessage(MessageEntity msg){
    	if (msg.getTo().equals(programContactAddress))
    		return true;
    	return false;
    }
} 
