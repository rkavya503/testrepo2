package com.akuacom.pss2.usecasetest.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.usecasetest.cases.CreateClientUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateNewParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteClientUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.FindClientUsecase;

/**
 * Delete Client not in event Integration test
 * @author Frank
 *
 */
public class DeleteClientTest extends UsecaseTestBase{

    private static final String participantName = "p1";
    private static final String accountNum = participantName;
    private static final String password = "Test_1234";

    @Test
    public void runCase () throws Exception {
    	// 1. Create the associated Participant object
        Participant part = (Participant)runCase(new CreateNewParticipantUsecase(participantName, accountNum, password));
        // 2. A client object is persisted into the DRAS.  
        runCase (new CreateClientUsecase(part, "c1", "Test_12345"));
        // 3. Delete the newly created client object 
        runCase( new DeleteClientUsecase("p1.c1"));
        // 4.  The provided name is used to search the dropped client object from system to verify the delete
        Participant found = (Participant)runCase (new FindClientUsecase("p1.c1"));
        assertTrue(found == null);
        // 5.  Clean the test data
        runCase (new DeleteParticipantUsecase(participantName));

    }
}
