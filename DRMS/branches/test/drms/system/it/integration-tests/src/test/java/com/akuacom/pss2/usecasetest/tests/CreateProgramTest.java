/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.usecasetest.tests;

import com.akuacom.pss2.usecasetest.cases.CreateRTPProgramUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteRTPProgramUsecase;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author spierson
 */
// This test is disabled for now because it doesn't work
public class CreateProgramTest extends UsecaseTestBase {

    @Test
    public void doTest() throws Exception {

        runCase(new CreateRTPProgramUsecase("IESO MarketRTP"));
        runCase(new DeleteRTPProgramUsecase("IESO MarketRTP"));

    }
}
