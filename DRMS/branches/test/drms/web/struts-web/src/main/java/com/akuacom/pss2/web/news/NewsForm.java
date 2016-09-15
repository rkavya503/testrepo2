/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.news.NewsForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.news;

import org.apache.struts.action.ActionForm;


/**
 * The Class NewsForm.
 */
public class NewsForm extends ActionForm
{
	
	private String newsUrl;

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	

}