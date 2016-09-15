/**
 * 
 */
package com.akuacom.pss2.ge;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 */
public interface GeConfigurationEAO extends GeConfigurationGenEAO{
	
	@Remote
	public interface R extends GeConfigurationEAO{}
	@Local
	public interface L extends GeConfigurationEAO{}

	int updateFTPConfig(String programName, String url, int shortInterval,
			int longInterval);

}
