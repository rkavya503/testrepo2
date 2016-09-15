/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.About.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.richsite;

import org.apache.commons.lang.StringUtils;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.utils.BuildProperties;
import com.akuacom.utils.DateUtil;

public class About {

	private String utility;
	private String version;
	private String buildTime;
	private String contactWebsite;
	private String contactPhone;
	private String contactEMail;
	private String info;
	private String displayVersion;
    private String timezoneInfo;

	public About() {
		SystemManager systemManager = (SystemManager) EJB3Factory.getLocalBean(SystemManager.class);
		BuildProperties buildProperties = new BuildProperties();

		final PSS2Properties params = systemManager.getPss2Properties();
		utility = params.getUtilityDisplayName();
		if(StringUtils.isBlank(params.getVersion())){
			displayVersion = params.getVersion();
		} else {
			displayVersion = buildProperties.getVersion();
		}
		version = buildProperties.getVersion();
		buildTime = buildProperties.getBuildTimeStamp();
		contactWebsite = params.getContactURLLink();
		contactPhone = params.getContactPhone();
		contactEMail = params.getContactEMail();

		info = params.getTextValue(PropertyName.ABOUT_INFORMATION, "");
        timezoneInfo = DateUtil.getServerTimeZoneInfo();
	}

	/**
	 * Gets the utility.
	 * 
	 * @return the utility
	 */
	public String getUtility() {
		return utility;
	}

	/**
	 * Gets the builds the time.
	 * 
	 * @return the builds the time
	 */
	public String getBuildTime() {
		return buildTime;
	}

	/**
	 * Gets the contact website.
	 * 
	 * @return the contact website
	 */
	public String getContactWebsite() {
		return contactWebsite;
	}

	/**
	 * Gets the contact phone.
	 * 
	 * @return the contact phone
	 */
	public String getContactPhone() {
		return contactPhone;
	}

	/**
	 * Gets the contact e mail.
	 * 
	 * @return the contact e mail
	 */
	public String getContactEMail() {
		return contactEMail;
	}

	/**
	 * Gets the info.
	 * 
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the utility.
	 * 
	 * @param utility
	 *            the new utility
	 */
	public void setUtility(String utility) {
		this.utility = utility;
	}

	public String getDisplayVersion() {
		return displayVersion;
	}

    public String getTimezoneInfo() {
        return timezoneInfo;
    }

    public void setTimezoneInfo(String timezoneInfo) {
        this.timezoneInfo = timezoneInfo;
    }
}
