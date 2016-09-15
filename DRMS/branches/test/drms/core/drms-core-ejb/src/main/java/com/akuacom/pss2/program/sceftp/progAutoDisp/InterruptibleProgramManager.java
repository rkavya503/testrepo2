/**
 * 
 */
package com.akuacom.pss2.program.sceftp.progAutoDisp;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.program.sceftp.SCEFTPConfig;

/**
 * the interface SCEFTPManager
 * 
 */
public interface InterruptibleProgramManager {
    @Remote
    public interface R extends InterruptibleProgramManager {}
    @Local
    public interface L extends InterruptibleProgramManager {}
    public void dispatch(String fileName,String fileContext,SCEFTPConfig config);
    
    public void dispatchEvent(String dispatchType, String programName, String product, String locationType,List<String> locationNumbers,Date issueTime,Date startTime, Date endTime);
    public void createEvent(String programName, String product, String locationType,List<String> locationNumbers,Date issueTime,Date startTime, Date endTime);
    public void deleteEvent(String programName, String product, String locationType,List<String> locationNumbers,Date startTime);
    public void endEvent(String programName, String product, String locationType,List<String> locationNumbers,Date startTime,Date endTime);
}
