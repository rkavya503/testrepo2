/**
 * 
 */
package com.akuacom.pss2.richsite.option;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.sceftp.SCEFTPConfig;
import com.akuacom.pss2.system.SystemManager;


/**
 *
 */
public class JSFParticipantSettingBean  extends JSFSettingBaseBean {

	private static final long serialVersionUID = 1524775896521554981L;
	
	public static final String DEFAULT_FIXED_FILENAME="DRAS_Customer_Info_Report";
	public static final String CONFIG_NAME="SCEPART";
	public static final String DEFAULT_FORMATTED_FILENAME="yyyyMMdd";

	private DBPEventDispatchBean.Tab subTab =  DBPEventDispatchBean.Tab.ParticipantConfigurationTab;
	SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
	public JSFParticipantSettingBean(DBPEventDispatchBean bean) {
		super(bean);
	}


	@Override
	public void init() {
		config = manager.getConfiguration(CONFIG_NAME);
		if (config == null) {
			config=new SCEFTPConfig();
	
			config.setConfigName(CONFIG_NAME);
			config.setFormattedFilename(DEFAULT_FORMATTED_FILENAME);
			config.setFixedFilename(DEFAULT_FIXED_FILENAME);
		}
		super.init();
	}

	@Override
	public String getFilenameTemplate(boolean display) {
		StringBuilder builder=new StringBuilder();
		if (config.getFormattedFilename() !=null && config.getFormattedFilename().trim().length()!=0) {
			builder.append(config.getFormattedFilename());
			builder.append("_");
		}
		if (!display)
			builder.append("'");
		builder.append(config.getFixedFilename());
		builder.append(".csv");
		if (!display)
			builder.append("'");
		
		return builder.toString();
	}
	
	private boolean participantsUploadFlag = true;

	public void setParticipantsUploadFlag(boolean participantsUploadFlag) {
		this.participantsUploadFlag = participantsUploadFlag;
	}
	public boolean isParticipantsUploadFlag() {
		participantsUploadFlag = systemManager.getPss2Features().isParticipantsUpload();
		return participantsUploadFlag;
	}
}
