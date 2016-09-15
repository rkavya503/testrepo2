/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.cases;


import com.akuacom.pss2.signal.SignalDef;
import org.openadr.dras.utilityprogram.ProgramInfo;
import java.util.ArrayList;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.signal.ProgramSignal;


import com.akuacom.pss2.program.Program;
import static org.junit.Assert.*;

/**
 *
 * @author spierson
 *
 * low-level usecase
 *
 * This usecase sets up the NRCAN streaming RTP program from scratch
 * 
 */
public class CreateRTPProgramUsecase extends AbstractUseCase {

    String programName;


    public CreateRTPProgramUsecase(String programName) {
        this.programName = programName;
    }

    @Override
    public Object runCase() throws Exception {

        Program pgm = new Program();
        pgm.setProgramName(programName);
        pgm.setUtilityProgramName(programName);
        pgm.setClassName("com.akuacom.pss2.program.rtp.RTP_IESOMarketRTP_ProgramEJB");
        pgm.setValidatorClass("com.akuacom.pss2.program.rtp.RTP_IESOMarketRTP_Validator");
        pgm.setAutoAccept(false);
        pgm.setUiScheduleEventString("ButtonOnlySchedulePage");
        pgm.setUiConfigureProgramString("");
        pgm.setUiConfigureEventString("");
        pgm.setValidatorConfigFile("rtp.validator.config");
        pgm.setNotificationParam1("foo");

        pgm.setMaxIssueTimeH(23);
        pgm.setMaxStartTimeH(23);
        pgm.setMaxEndTimeH(23);
        pgm.setMaxIssueTimeM(59);
        pgm.setMaxStartTimeM(59);
        pgm.setMaxEndTimeM(59);
        pgm.setMaxDurationM(1440);
        pgm.setPendingTimeDBEH(21);  // why?
        pgm.setState(1);

        ProgramSignal ps1 = new ProgramSignal();
        ArrayList<SignalDef> sigs = new ArrayList<SignalDef>();
        sigs.add(getSignalMgr().getSignal("mode"));
        sigs.add(getSignalMgr().getSignal("pending"));
        sigs.add(getSignalMgr().getSignal("price"));
// FOO TEMP FOO        pgm.setSignals(sigs);
        pgm.setPriority(getProgMgr().getNextPriority());//pgm.setPriority(92);Modified by Frank remove the hard code of priority

        ProgramInfo pi = new ProgramInfo();
        pi.setProgramName(pgm.getProgramName());

        getProgMgr().createProgram(pgm);

        Program found = getProgMgr().getProgram(programName);
        assertTrue(found!= null);

        assertTrue(pgm.getProgramName().equals(found.getProgramName()));
        
        return found;
    }

}
