/**
 * 
 */
package com.akuacom.pss2.richsite.option;

import com.akuacom.pss2.program.sceftp.SCEFTPConfig;


/**
 * 
 */
public class JSFDBPSettingBean extends JSFSettingBaseBean {

	private static final long serialVersionUID = 4249899142766972335L;
	
	public static final String CONFIG_NAME="SCEDBP";
	public static final String DEFAULT_FIXED_FILENAME="DBP_BID_REPORT";
	public static final String DEFAULT_FORMATTED_FILENAME="yyyyMMdd";

	private DBPEventDispatchBean.Tab subTab =  DBPEventDispatchBean.Tab.DBPEventConfigurationTab;

	public JSFDBPSettingBean(DBPEventDispatchBean bean) {
		super(bean);
	}

	@Override
	public void init() {
		config = manager.getConfiguration(CONFIG_NAME);
		if (config == null) {
			config = new SCEFTPConfig();

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
}
