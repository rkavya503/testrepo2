/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.usecasetest.tests;

import java.util.List;
import com.akuacom.pss2.usecasetest.cases.DeleteSeveralParticipantsUseCase;
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
public class CreateSeveralParticipantsTest  extends UsecaseTestBase {

    private static final int MAX_PARTICIPANTS = 10;

    @Test
    public void createParticipants () throws Exception {

        ArrayList<String> participantNames = new ArrayList<String>();

        BufferedReader bin = new BufferedReader(new InputStreamReader(this
        .getClass().getClassLoader().getResourceAsStream("fort500.txt")));


        String partName = null;
        int count = 0;
        while ( (partName = bin.readLine()) != null && ++count <= MAX_PARTICIPANTS) {
            partName = partName.trim();
            if (partName != null && partName.length() > 0) {
                participantNames.add(partName);
            }
        }
        System.out.println(participantNames);

        runCase(new CreateSeveralParticipantsUseCase(participantNames));
        runCase(new DeleteSeveralParticipantsUseCase(participantNames));

    }
}
