/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.NewsInfo.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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

public class NewsInfo implements Serializable {
	
	private String newsUrl;
	
	public NewsInfo()
	{
		SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
		PSS2Properties properties= systemManager.getPss2Properties();
		newsUrl = properties.getStringValue(PropertyName.NEWS_URL);
		
	}
	
	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	
	
}
