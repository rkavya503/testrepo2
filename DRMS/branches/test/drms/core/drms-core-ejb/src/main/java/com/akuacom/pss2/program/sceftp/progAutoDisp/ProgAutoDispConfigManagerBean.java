/**
 * 
 */
package com.akuacom.pss2.program.sceftp.progAutoDisp;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.pss2.program.dbp.SCEFTPClient;
import com.akuacom.pss2.program.sceftp.SCEDBPConfigManagerBean;
import com.akuacom.pss2.program.sceftp.SCEFTPConfig;
import com.akuacom.pss2.program.sceftp.SCEFTPConfigEAO;

/**
 * the class ProgAutoDispConfigManagerBean
 */
@Stateless
public class ProgAutoDispConfigManagerBean extends SCEDBPConfigManagerBean 
				implements ProgAutoDispConfigManager.L, ProgAutoDispConfigManager.R {

	@EJB
	SCEFTPConfigEAO.L configEAO;

	@Override
	public SCEFTPConfig save(SCEFTPConfig config) {
		SCEFTPConfig updated=null;
		if (config.getUUID()==null) {
			try {
				updated=configEAO.create(config);
			} catch (DuplicateKeyException e) {
			}
		} else {
				updated=configEAO.merge(config);
		}
		
		processTimer(config);
		
		return updated;
	}

	@Override
	public void testConnection(SCEFTPConfig config) throws AppServiceException{
		SCEFTPClient ftpClient=new SCEFTPClient(config.getHost(), config.getPort(), config.getUsername(), config.getPassword());
		
		ftpClient.testConnection();
	}

}
