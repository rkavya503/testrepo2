package com.akuacom.pss2.usecasetest.tests;

import org.junit.Test;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.usecasetest.cases.CreateClientUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateNewParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.UpdateClientUsecase;
/**
 * Update Client Integration test
 * @author Frank
 *
 */
public class UpdateClientTest extends UsecaseTestBase{

    private static final String participantName = "p1";
    private static final String accountNum = participantName;
    private static final String password = "Test_1234";

    @Test
    public void runCase () throws Exception {
    	// 1. Create the associated Participant object
        Participant part = (Participant)runCase(new CreateNewParticipantUsecase(participantName, accountNum, password));
        // 2. A client object is persisted into the DRAS.  
        //    &  use the given name to search the stored Client object from system
        Participant client = (Participant)runCase (new CreateClientUsecase(part, "c1", "Test_12345"));
        // 3.  The found Client is updated
        client.setFirstName(client.getFirstName()+"updated");
        // 4.  Make the update operation effect and verify the update result
        runCase(new UpdateClientUsecase(client));
        // 5.  Clean the test data
        runCase (new DeleteParticipantUsecase(participantName));

    }
}
