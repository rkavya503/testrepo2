/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

/**
 * the interface SCEFTPTimerManager
 * 
 */
public interface SCEFTPTimerManager extends TimerManager {
	
    @Remote
    public interface R extends SCEFTPTimerManager {   }
    @Local
    public interface L extends SCEFTPTimerManager {   }
    
//    String getConfigName();
//    String getTimerName();
//
}
