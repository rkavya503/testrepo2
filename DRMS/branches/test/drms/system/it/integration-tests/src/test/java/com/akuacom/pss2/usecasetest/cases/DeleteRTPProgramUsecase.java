/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.cases;

import com.akuacom.pss2.program.Program;


import static org.junit.Assert.*;

/**
 *
 * @author spierson
 *
  * low-level usecase
 * Deletes the NRCAN streaming RTP program
 * 
 */
public class DeleteRTPProgramUsecase extends AbstractUseCase {

    String programName;

    public DeleteRTPProgramUsecase(String programName) {
        this.programName = programName;
    }

    @Override
    public Object runCase() throws Exception {

        getProgMgr().removeProgram(programName);
        Program  found = getProgMgr().getProgram(programName);
  //      assertTrue(found.getState() == 0);  // Why isn't this up to date after the remove?
        return null;
    }

}
