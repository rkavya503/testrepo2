/**
 * 
 */
package com.akuacom.pss2.richsite.option;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.event.ValueChangeEvent;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.program.sceftp.SCEDBPConfigManager;
import com.akuacom.pss2.program.sceftp.SCEFTPConfig;
import com.akuacom.pss2.richsite.FDUtils;

/**
 *
 */
public abstract class JSFSettingBaseBean implements Serializable {

	private static final long serialVersionUID = 8155573046623746786L;

	protected SCEFTPConfig config;
	protected DBPEventDispatchBean bean;
	
	SCEDBPConfigManager manager = EJBFactory.getBean(SCEDBPConfigManager.class);
	
	public JSFSettingBaseBean(DBPEventDispatchBean bean){
		this.bean=bean;
		init();
	}
	
	public void init() {
		this.config.setAvailable(true);
	}
	
	public void testConnection(){
		try {

			manager.testConnection(this.bean.getHost(), this.bean.getPort(), this.bean.getUsername(), this.bean.getPassword(),
					this.getFilenameTemplate(false), this.config.getBackupPath());
			FDUtils.addMsgInfo("Connected to the FTP server successfully");
		} catch (AppServiceException e) {
			FDUtils.addMsgError(ErrorUtil.getErrorMessage(e));
		}
	}
	
	public void save(){
		if (!checkScanTime())
			return;
		
		config.setHost(bean.getHost());
		config.setPort(bean.getPort());
		config.setUsername(bean.getUsername());
		config.setPassword(bean.getPassword());
		
		config.setFilenameTemplate(getFilenameTemplate(false));
		config.setUpload(false);
		config.setResetTimer(true);
		config.setConnErrorNotified(false);
		
		config=manager.save(config);
		FDUtils.addMsgInfo("Configuration saved successfully");
	}
	
	private boolean checkScanTime() {
		boolean correct=true;
		SimpleDateFormat format=new SimpleDateFormat("HH:mm");
		try {
			Date startTime =null;
			if (config.getScanStartTime()!=null && config.getScanStartTime().trim().length()!=0) {
				startTime=format.parse(config.getScanStartTime());
			}
	
			Date endTime=null;
			if (config.getScanEndTime()!=null && config.getScanEndTime().trim().length()!=0) {
				endTime=format.parse(config.getScanEndTime());
			}
			
			if (startTime.getTime()>=endTime.getTime()) {
				FDUtils.addMsgError("Scan start time must be earlier than scan end time");
				correct= false;
			}
		}catch(ParseException e){
			FDUtils.addMsgError("Scan time not correct. Scan start/end time should be defined as HH:mm (HH indicates hour in day (0-23)).");
			correct=false;
		}
		return correct;
	}

	@Deprecated
	public void valueChanged(ValueChangeEvent e){
		config.setResetTimer(true);
	}

	public String getFilenameTemplate() {
		return getFilenameTemplate(true);
	}
	
	protected String getFilenameTemplate(boolean display) {
		return null;
	}

	public SCEFTPConfig getConfig() {
		return config;
	}

	public void setConfig(SCEFTPConfig config) {
		this.config = config;
	}
}
