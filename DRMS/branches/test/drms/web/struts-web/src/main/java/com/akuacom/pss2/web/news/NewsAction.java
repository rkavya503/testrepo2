/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.news.NewsAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.news;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;

/**
 * The Class NewsAction.
 */
public class NewsAction extends Action {

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	        throws IOException,ServletException {
		final NewsForm newsForm = (NewsForm)form;
		
		SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
        PSS2Properties properties= systemManager.getPss2Properties();
        String newsUrl = properties.getStringValue(PropertyName.NEWS_URL);
        newsForm.setNewsUrl(newsUrl);
        return mapping.findForward("success");
	}
}
