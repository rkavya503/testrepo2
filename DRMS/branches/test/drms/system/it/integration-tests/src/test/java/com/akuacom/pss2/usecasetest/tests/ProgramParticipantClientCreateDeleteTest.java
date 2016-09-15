/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.usecasetest.tests;

import org.junit.Ignore;
import java.util.Collection;
import com.akuacom.pss2.usecasetest.cases.CancelEventUsecase;
import com.akuacom.pss2.usecasetest.cases.StartEventUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteRTPProgramUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteSeveralParticipantsUseCase;
import com.akuacom.pss2.usecasetest.cases.CreateRTPProgramUsecase;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import com.akuacom.pss2.usecasetest.cases.CreateSeveralParticipantsUseCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author spierson
 */
public class ProgramParticipantClientCreateDeleteTest  extends UsecaseTestBase {

    private static final int MAX_PARTICIPANTS = 5;
    private static final String TEST_PROGRAM_NAME = "IESO MarketRTP";

    @Test
    public void createParticipants () throws Exception {

        // Read a list of realistic sounding participant names from a file,
        // limited to a maximum number defined above
        ArrayList<String> participantNames = new ArrayList<String>();
        BufferedReader bin =
                new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("fort500.txt")));
        String partName = null;
        int count = 0;
        while ( (partName = bin.readLine()) != null && ++count < MAX_PARTICIPANTS) {
            partName = partName.trim();
            if (partName != null && partName.length() > 0) {
                participantNames.add(partName);
            }
        }

        ArrayList<String> programNames = new ArrayList<String>();
        programNames.add(TEST_PROGRAM_NAME);

        // Create a new RTP program
        runCase(new CreateRTPProgramUsecase(TEST_PROGRAM_NAME));
        
        // Put the participants (and some clients for each) into the program
        runCase(new CreateSeveralParticipantsUseCase(participantNames, programNames));
        // Start an event
        Collection<String> eventNames = (Collection<String>) runCase(new StartEventUsecase(TEST_PROGRAM_NAME));

        // Need a use case here to read some client event states
        // TODO

        // Now clean up and delete everything
        runCase(new CancelEventUsecase(TEST_PROGRAM_NAME, eventNames));
        runCase(new DeleteSeveralParticipantsUseCase(participantNames));
        
        runCase(new DeleteRTPProgramUsecase(TEST_PROGRAM_NAME));

    }
}
