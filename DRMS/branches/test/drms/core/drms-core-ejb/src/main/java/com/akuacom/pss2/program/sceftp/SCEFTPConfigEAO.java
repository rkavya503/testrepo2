/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 */
public interface SCEFTPConfigEAO extends SCEFTPConfigGenEAO{
	
	@Remote
	public interface R extends SCEFTPConfigEAO{}
	@Local
	public interface L extends SCEFTPConfigEAO{}

	int updateFTPConfig(String host, int port, String username, String password, String configName);

}
