/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.program.sdgcpp;

import com.akuacom.pss2.program.cpp.CPPProgramEJB;
import com.akuacom.pss2.timer.TimerManager;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author spierson
 */
public interface SdgCPPProgramEJB extends CPPProgramEJB, TimerManager {
    @Remote
    public interface R extends SdgCPPProgramEJB {}

    @Local
    public interface L extends SdgCPPProgramEJB {}    
    
    
}
