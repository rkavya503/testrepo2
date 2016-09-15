/**
 * 
 */
package com.akuacom.pss2.program.dbp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.program.sceftp.CreationFailureException;

/**
 * the interface DBPProgramManager
 *
 */
public interface DBPProgramManager {
	
    @Remote
    public interface R extends DBPProgramManager {   }
    @Local
    public interface L extends DBPProgramManager {   }

    void createEvent(String filename, String filsString) throws ProgramValidationException, CreationFailureException;

}
