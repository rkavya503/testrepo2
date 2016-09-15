/**
 * 
 */
package com.akuacom.pss2.program.sceftp.progAutoDisp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.sceftp.SCEDBPConfigManager;

/**
 * the interface ProgAutoDispConfigManager
 */
public interface ProgAutoDispConfigManager extends SCEDBPConfigManager {
    @Remote
    public interface R extends ProgAutoDispConfigManager {   }
    
    @Local
    public interface L extends ProgAutoDispConfigManager {   }
    

}
