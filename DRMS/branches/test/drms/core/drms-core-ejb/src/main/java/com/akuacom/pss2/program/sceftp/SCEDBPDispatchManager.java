/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 */
public interface SCEDBPDispatchManager extends SCEFTPTimerManager {

    @Remote
    public interface R extends SCEDBPDispatchManager {   }
    @Local
    public interface L extends SCEDBPDispatchManager {   }
    
}
