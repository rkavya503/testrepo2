/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.BuildAndVersionProperties.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.akuacom.common.resource.ManagedResourceBundle;
import com.akuacom.common.resource.ResourceBundleMessage;

/**
 * The Class BuildProperties.
 * 
 * @author padmaja, Brian Chapman
 */
public class BuildProperties {

	private static final String propFileName = "build.properties";
	Properties properties = new Properties();

	public BuildProperties() {
		properties.put("buildTime", "");

		try {
			InputStream in = BuildProperties.class.getClassLoader()
			.getResourceAsStream(propFileName);
			properties.load(in);
		} catch (IOException e) {
			Logger.getLogger(BuildProperties.class).warn(
					"No properties file matching " + propFileName
							+ BuildProperties.class.getSimpleName());
		}
	}

	public String getBuildTimeStamp() {
		Date date = new Date(Long.parseLong(properties.getProperty("buildTime").trim()));
		String buildTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return buildTime;
	}

	public String getVersion() {
		return properties.getProperty("dras_version");
	}

	public String getRevision() {
		return properties.getProperty("scm_revision");
	}

	public String getScmBranch() {
		return properties.getProperty("scm_branch");
	}
}
