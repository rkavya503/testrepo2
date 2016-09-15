package com.akuacom.pss2.usecasetest.cases;

import com.akuacom.ejb.jboss.test.JBossFixture;

import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;

/**
 *
 * @author spierson
 *
 *  low-level usecase
 *
 * Looks a program up by name
 *
 */
public class FindProgramUsecase extends AbstractUseCase {

    private String programName = null;

    public String getParticipantName() { return programName; }
    public final void setParticipantName(String programName) {this.programName = programName; }

    protected ProgramManager pman;

    public FindProgramUsecase() {
        this(null);
    }

    public FindProgramUsecase(String partName) {
        pman = JBossFixture.lookupSessionRemote(ProgramManagerBean.class);
        setParticipantName(partName);
    }

    @Override
    public Object runCase() throws Exception {

        // Now look it up and compare with the one we made
        return (Program) pman.getProgram(programName);

    }

}
