package com.akuacom.pss2.program.bip;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.cpp.CPPProgramEJB;

public interface BIPProgramEJB extends CPPProgramEJB {
	
    @Remote
    public interface R extends BIPProgramEJB {  }
    @Local
    public interface L extends BIPProgramEJB {  }
    
    
}
