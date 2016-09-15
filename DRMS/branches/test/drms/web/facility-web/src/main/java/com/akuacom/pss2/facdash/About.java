/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.About.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;


import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.utils.BuildProperties;
import com.akuacom.utils.DateUtil;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class About implements Serializable {

	private String utility;
	private String version;
	private String displayVersion;
	private String buildTime;
	private String contactWebsite;
	private String contactPhone;
	private String contactEMail;
	private String info;
    private String timezoneInfo;

	public About() {
		SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
		BuildProperties buildProperties = new BuildProperties();

		final PSS2Properties params = systemManager.getPss2Properties();
		utility = params.getUtilityDisplayName();
		// If core property version is not set or blank
		if(StringUtils.isBlank(params.getVersion())){
			displayVersion = buildProperties.getVersion();
		} else {
			displayVersion = params.getVersion();
		}
		version = buildProperties.getVersion();
		buildTime = buildProperties.getBuildTimeStamp();
		contactWebsite = params.getContactURLLink();
		contactPhone = params.getContactPhone();
		contactEMail = params.getContactEMail();

		info = params.getTextValue(PropertyName.ABOUT_INFORMATION, "");
        timezoneInfo = DateUtil.getServerTimeZoneInfo();
	}

	public String getUtility() {
		return utility;
	}

	public String getBuildTime() {
		return buildTime;
	}

	public String getContactWebsite() {
		return contactWebsite;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public String getContactEMail() {
		return contactEMail;
	}

	public String getInfo() {
		return info;
	}

	public String getDisplayVersion() {
		return displayVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setUtility(String utility) {
		this.utility = utility;
	}

    public String getTimezoneInfo() {
        return timezoneInfo;
    }

    public void setTimezoneInfo(String timezoneInfo) {
        this.timezoneInfo = timezoneInfo;
    }
}
