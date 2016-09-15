/**
 * 
 */
package com.akuacom.pss2.program.dbp;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.program.sceftp.CreationFailureException;

/**
 * the class DBPProgramManagerBean
 */
@Stateless
public class DBPProgramManagerBean implements DBPProgramManager.L, DBPProgramManager.R {


	@EJB
	DBPNoBidProgramEJB.L program;
	
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.program.dbp.DBPProgramManager#createEvent(java.lang.String, java.lang.String)
	 */
	@Override
	public void createEvent(String filename, String fileString) throws ProgramValidationException, CreationFailureException {
		
			program.createEvent(fileString, filename);
	}

}
