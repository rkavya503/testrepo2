/**
 * 
 */
package com.akuacom.pss2.program.cbp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.cpp.CPPProgramEJB;

/**
 * the interface CBPProgramEJB
 *
 */
public interface CBPProgramEJB extends CPPProgramEJB {
	
    @Remote
    public interface R extends CBPProgramEJB {}
    @Local
    public interface L extends CBPProgramEJB {}

}
