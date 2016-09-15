package com.akuacom.pss2.program.fastdr;

import com.akuacom.pss2.core.ProgramEJB;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface FastDRNProgramEJB extends ProgramEJB {
    @Remote
    public interface R extends FastDRNProgramEJB {  }
    @Local
    public interface L extends FastDRNProgramEJB {  }
}
