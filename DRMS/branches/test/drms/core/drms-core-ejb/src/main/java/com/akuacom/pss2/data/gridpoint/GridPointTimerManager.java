/**
 * 
 */
package com.akuacom.pss2.data.gridpoint;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

/**
 * the interface GridPointTimerManager
 */
public interface GridPointTimerManager extends TimerManager {

    @Remote
    public interface R extends GridPointTimerManager {   }
    @Local
    public interface L extends GridPointTimerManager {   }

}
