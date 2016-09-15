/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * the interface SCEPartticipantTimerManager
 * 
 */
public interface PartticipantUploadTimer extends SCEFTPTimerManager {

    @Remote
    public interface R extends PartticipantUploadTimer {   }
    @Local
    public interface L extends PartticipantUploadTimer {   }

}
