/**
 * 
 */
package com.akuacom.pss2.program.testProgram;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.demo.DemoProgramEJB;

/**
 *
 */
public interface TestProgramEJB extends DemoProgramEJB {
    @Remote
    public interface R extends TestProgramEJB {}
    @Local
    public interface L extends TestProgramEJB {}

}
