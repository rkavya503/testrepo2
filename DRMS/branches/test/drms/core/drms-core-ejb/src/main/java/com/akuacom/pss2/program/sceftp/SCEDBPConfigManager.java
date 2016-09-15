/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;

/**
 * the interface SCEDBPConfigManager
 * 
 */
public interface SCEDBPConfigManager {
    @Remote
    public interface R extends SCEDBPConfigManager {   }
    @Local
    public interface L extends SCEDBPConfigManager {   }
	

	/**
	 * 
	 */
	void testConnection(SCEFTPConfig config) throws AppServiceException;

	SCEFTPConfig getConfiguration();

	void testConnection(String host, Integer port, String username,
			String password, String filenameTemplate, String backupPath)
			throws AppServiceException;

	SCEFTPConfig getConfiguration(String configName);

	SCEFTPConfig save(SCEFTPConfig config);

}
