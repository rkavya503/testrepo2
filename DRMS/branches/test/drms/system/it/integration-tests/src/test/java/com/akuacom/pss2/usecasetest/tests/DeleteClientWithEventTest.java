package com.akuacom.pss2.usecasetest.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.usecasetest.cases.CancelEventUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateClientUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateRTPProgramUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateSeveralParticipantsUseCase;
import com.akuacom.pss2.usecasetest.cases.DeleteClientUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteRTPProgramUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteSeveralParticipantsUseCase;
import com.akuacom.pss2.usecasetest.cases.FindClientByParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.FindClientUsecase;
import com.akuacom.pss2.usecasetest.cases.FindParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.FindProgramUsecase;
import com.akuacom.pss2.usecasetest.cases.StartEventUsecase;

/**
 * Delete Client in event Integration test
 * @author Frank
 * 
 */
@Ignore
public class DeleteClientWithEventTest  extends UsecaseTestBase {

    private static final String TEST_PROGRAM_NAME = "TEST5 MarketRTP";
    private static final String participantName = "p5";


    @SuppressWarnings("unchecked")
	@Test
    public void createParticipants () throws Exception {
    	
        ArrayList<String> participantNames = new ArrayList<String>();
        participantNames.add(participantName);
        ArrayList<String> programNames = new ArrayList<String>();
        programNames.add(TEST_PROGRAM_NAME);

        // 1. prepare step:Delete the RTP program if it already exists 
        if(runCase(new FindProgramUsecase(TEST_PROGRAM_NAME))!=null){
            //runCase(new DeleteSeveralParticipantsUseCase(participantNames));
            runCase(new DeleteRTPProgramUsecase(TEST_PROGRAM_NAME));
        }
        // 2. Create a new RTP program object
        runCase(new CreateRTPProgramUsecase(TEST_PROGRAM_NAME));
        // 3. Put the participants (and some clients for each) into the program
        runCase(new CreateSeveralParticipantsUseCase(participantNames, programNames));
        
		List<String> clientNames =  (List<String>) runCase(new FindClientByParticipantUsecase(participantName));
        if(clientNames==null||clientNames.size()==0){
        	//if there isn't any client in system ,create one
        	Participant part = (Participant) runCase(new FindParticipantUsecase(participantName));
        	runCase (new CreateClientUsecase(part, "c1", "Test_12345"));
        	clientNames =  (List<String>) runCase(new FindClientByParticipantUsecase(participantName));
        }
        // 4. Start an event
		Collection<String> eventNames = (Collection<String>) runCase(new StartEventUsecase(TEST_PROGRAM_NAME));
        // 5. The provided name is used to search the client object which will be deleted
        Participant createdClient = (Participant)runCase (new FindClientUsecase(clientNames.get(0)));
        Set<EventParticipant> eventpart= createdClient.getEventParticipants();
        // 6. Verify that the client is in an event.
        assertTrue(eventpart.size()!=0);
        // 7. Delete the newly created client with given name
        runCase( new DeleteClientUsecase(createdClient.getParticipantName()));
        // 8. The provided name is used to search the dropped client object from system to verify the delete
        Participant found = (Participant)runCase (new FindClientUsecase(createdClient.getParticipantName()));
        assertTrue(found != null);
        // 9. Clean up and delete everything
        runCase(new CancelEventUsecase(TEST_PROGRAM_NAME, eventNames));
        runCase(new DeleteSeveralParticipantsUseCase(participantNames));
        runCase(new DeleteRTPProgramUsecase(TEST_PROGRAM_NAME));

    }
}
