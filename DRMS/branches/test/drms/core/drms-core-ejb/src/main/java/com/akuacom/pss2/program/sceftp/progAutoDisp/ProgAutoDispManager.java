/**
 * 
 */
package com.akuacom.pss2.program.sceftp.progAutoDisp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.sceftp.SCEFTPConfig;
import com.akuacom.pss2.program.sceftp.SCEFTPTimerManager;

/**
 * the interface SCEProgAutoDispManager
 */
public interface ProgAutoDispManager extends SCEFTPTimerManager {

    @Remote
    public interface R extends ProgAutoDispManager {   }
    
    @Local
    public interface L extends ProgAutoDispManager {   }

	void autoDispatch(SCEFTPConfig config);
	
	void autoDispatch(SCEFTPConfig config,String fileName,String fileContext);
}
