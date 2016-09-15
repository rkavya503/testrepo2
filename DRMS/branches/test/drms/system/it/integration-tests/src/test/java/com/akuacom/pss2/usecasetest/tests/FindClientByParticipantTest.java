package com.akuacom.pss2.usecasetest.tests;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.usecasetest.cases.CreateClientUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateNewParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.FindClientByParticipantUsecase;

/**
 * Find Client with given participant name Integration test
 * @author Frank
 *
 */
public class FindClientByParticipantTest extends UsecaseTestBase{

    private static final String participantName = "p1";
    private static final String accountNum = participantName;
    private static final String password = "Test_1234";

    @Test
    public void runCase () throws Exception {
    	// 1. Create the associated Participant object
        Participant part = (Participant)runCase(new CreateNewParticipantUsecase(participantName, accountNum, password));
        // 2. Create some client with the same participant object.  
        String[] clientPostfix = {"c1","c2","c3","c4","c5"}; 
        for(String name :clientPostfix){
        	 runCase (new CreateClientUsecase(part, name, "Test_12345"));
        }
        // 3. Use the given client names to search the stored Client objects from system
        @SuppressWarnings("unchecked")
		List<String> clientNames =  (List<String>) runCase(new FindClientByParticipantUsecase(part.getParticipantName()));
        
        // 4.The fetched client names are compared with the original created Client names to verify whether it is the same.
        Set<String> createdNames = new HashSet<String>();
        createdNames.add("p1.c1");
        createdNames.add("p1.c2");
        createdNames.add("p1.c3");
        createdNames.add("p1.c4");
        createdNames.add("p1.c5");
        Set<String> foundNames = new HashSet<String>();
        for(String name :clientNames){
        	foundNames.add(name);
        }
        assertTrue(foundNames.equals(createdNames));
        // 5.  Clean the test data
        runCase (new DeleteParticipantUsecase(participantName));

    }
}
