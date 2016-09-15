/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.dbp.SCEFTPClient;

/**
 * the class SCEDBPConfigManagerBean
 *
 */
@Stateless
public class SCEDBPConfigManagerBean implements SCEDBPConfigManager.L, SCEDBPConfigManager.R {

    private static final Logger log = Logger.getLogger(SCEDBPConfigManagerBean.class);

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
		
		configEAO.updateFTPConfig(config.getHost(), config.getPort(), 
				config.getUsername(), config.getPassword(), config.getConfigName());
		
		processTimer(config);
		
		return updated;
	}

	protected void processTimer(SCEFTPConfig config){
		String className=config.getTimerManagerClass();
		if (className==null) {
            log.error("Sce ftp timer manager class not defined for " + config.getConfigName());
            return;
		}
		
        try {
            final Class<?> aClass = Class.forName(className);
            SCEFTPTimerManager manager= (SCEFTPTimerManager) EJBFactory.getBean(aClass);
            
    		if (config.isCancelTimer())
    			manager.cancelTimers();
    		else if (config.isResetTimer()) 
    			manager.createTimers();
    		
        } catch (ClassNotFoundException e) {
            log.error("Can't find sce ftp timer manager class bean for " + className, e);
        }
	}
	
	@Override
	public void testConnection(String host, Integer port, String username, String password, 
			String filenameTemplate, String backupPath) throws AppServiceException{
		
		SimpleDateFormat format=new SimpleDateFormat(filenameTemplate);
		String filename=format.format(new Date());
		
		SCEFTPClient ftpClient=new SCEFTPClient(host, port, username, 
				password, filename,	backupPath);
		ftpClient.testConnection();
	}
	
	@Override
	public void testConnection(SCEFTPConfig config) throws AppServiceException{
		SimpleDateFormat format=new SimpleDateFormat(config.getFilenameTemplate());
		String filename=format.format(new Date());
		
		SCEFTPClient ftpClient=new SCEFTPClient(config.getHost(), config.getPort(), config.getUsername(), 
				config.getPassword(), filename,
				config.getBackupPath());
		ftpClient.testConnection();
		
	}
	
	@Override
	public SCEFTPConfig getConfiguration(String configName){
		SCEFTPConfig config=null;
		try {
			config=configEAO.findByConfigName(configName);
		}catch(Exception e) {
//			log.
		}
		
		return config;
	}
	
	@Override
	public SCEFTPConfig getConfiguration(){
		SCEFTPConfig config=null;
		List<SCEFTPConfig> configList=configEAO.getAll();
		if (configList!=null && configList.size()>0) 
			config=configList.get(0);
		
		return config;
	}
}
