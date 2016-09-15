/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * the interface SCEFTPManager
 * 
 */
public interface SCEFTPManager {
    @Remote
    public interface R extends SCEFTPManager {   }
    @Local
    public interface L extends SCEFTPManager {   }
    
	int process(SCEFTPConfig config, boolean isLast);
	
	void sendExceptionNotifications(Exception e, String filename);
	
	void processParserFile(SCEParticipantFileParser parser);

	void sendNotification(String subject, String content);

	void sendNotifications(String content);
}
