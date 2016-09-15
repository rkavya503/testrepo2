package com.akuacom.pss2.program.dlc;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.program.dlc.signal.SignalType;
import com.akuacom.pss2.program.dlc.signal.SignalsType;

public interface DlcProgramEJB extends ProgramEJB {
    @Local
    public interface L extends DlcProgramEJB {}
    @Remote
    public interface R extends DlcProgramEJB {}

    public void updateSignals(SignalsType signalMap);

    public ProgramValidationMessage updateSignal(SignalType signal);
}
