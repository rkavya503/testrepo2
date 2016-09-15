/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.tests;

import com.akuacom.pss2.usecasetest.cases.DeleteParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateNewParticipantUsecase;
import org.junit.Test;

/**
 *
 * @author spierson
 */
public class CreateOneParticipantTest extends UsecaseTestBase {

    private static final String participantName = "singleParticipant";
    private static final String accountNum = participantName;
    private static final String password = "Test_1234";

    @Test
    public void runCase () throws Exception {

        runCase(new CreateNewParticipantUsecase(participantName, accountNum, password));
        runCase(new DeleteParticipantUsecase(participantName));

    }
}
