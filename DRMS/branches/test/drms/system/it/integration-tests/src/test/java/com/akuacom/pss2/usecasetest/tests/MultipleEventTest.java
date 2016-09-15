/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.usecasetest.tests;

import com.akuacom.pss2.usecasetest.cases.AssociateParticipantWithProgramUsecase;
import com.akuacom.pss2.usecasetest.cases.CancelEventUsecase;
import com.akuacom.pss2.usecasetest.cases.StartEventUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteSeveralParticipantsUseCase;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import com.akuacom.pss2.usecasetest.cases.CreateSeveralParticipantsUseCase;
import java.util.Collection;
import java.util.List;
import org.junit.Ignore;

import org.junit.Test;

/**
 *
 * @author spierson
 *
 */
@Ignore
public class MultipleEventTest  extends UsecaseTestBase {

    private static final int MAX_PARTICIPANTS = 3;
    private static final int NUM_PROGRAMS = 4;

    private static final String[] programNames = { "RTP Agricultural", "RTP <2K", "RTP 2K-50K", "RTP >50K" };

    // NOTE!!  This test depends upon having the 4 SCE RTP
    // programs set up in the DRAS.  They need seasons and prices.
    // It will not run properly on a clean, empty database
    @Test
    public void MultiEventTest () throws Exception {
        // Read a list of realistic sounding participant names from a file,
        // limited to a maximum number defined above
        List<String> []participantNames =   (List<String>[]) new List<?>[NUM_PROGRAMS];
        Collection<String> []eventNames       =   (Collection<String>[]) new Collection<?>[NUM_PROGRAMS];
        int numPrograms = programNames.length;

        BufferedReader bin =
                new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("fort500.txt")));
        String partName = null;

        for (int i = 0; i < NUM_PROGRAMS; ++i) {
            participantNames[i] = new ArrayList<String>();
            int count = 0;
            while ((partName = bin.readLine()) != null && ++count < MAX_PARTICIPANTS) {
                partName = partName.trim();
                if (partName != null && partName.length() > 0) {
                    participantNames[i].add(partName);
                }
            }
        }

        // Put the participants (and some clients for each) into each program
        // and start an event in each program
        for (int i = 0; i < numPrograms; ++i) {
            // add participants and clients to a program
            runCase(new CreateSeveralParticipantsUseCase(participantNames[i], programNames[i]));
            eventNames[i] = (Collection<String>) runCase(new StartEventUsecase(programNames[i]));
        }

        // TODO Really need to simulate some polling actual clients here

        // cancel all the events at once
        for (int i = 0; i < numPrograms; ++i) {
            runCase(new CancelEventUsecase(programNames[i], eventNames[i]));
        }

        // get rid of the participants and clients we made
        for (List<String> programParticipants : participantNames) {
            runCase(new DeleteSeveralParticipantsUseCase(programParticipants));
        }

    }
}
