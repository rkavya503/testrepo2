package com.akuacom.pss2.usecasetest.tests;

import org.junit.Test;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.pss2.usecasetest.cases.CreateClientUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateNewParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.FindClientUsecase;

/**
 * Find Client with given client name Integration test
 * @author Frank
 *
 */
public class FindClientTest extends UsecaseTestBase{

    private static final String participantName = "p1";
    private static final String accountNum = participantName;
    private static final String password = "Test_1234";

    @Test
    public void runCase () throws Exception {
    	// 1. Create the associated Participant object
        Participant part = (Participant)runCase(new CreateNewParticipantUsecase(participantName, accountNum, password));
        // 2. A client object is persisted into the DRAS.  
        Participant client = (Participant)runCase (new CreateClientUsecase(part, "c1", "Test_12345"));
        // 3. Use the given name to search the stored Client object from system
        Participant found = (Participant)runCase (new FindClientUsecase("p1.c1"));
        // 4. The fetched client is compared with the original created Client to verify whether it is the same.
        ParticipantUtil.compareParticipants(client, found);
        // 5.  Clean the test data
        runCase (new DeleteParticipantUsecase(participantName));

    }
}
