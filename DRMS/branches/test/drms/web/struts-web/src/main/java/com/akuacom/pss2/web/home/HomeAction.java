/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.home.HomeAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.home;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.about.AboutForm;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.utils.BuildProperties;

/**
 * The Class HomeAction.
 */
public class HomeAction extends Action {

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	        throws IOException,ServletException {
		final AboutForm recForm = (AboutForm)form;

        SystemManager systemManager = EJBFactory.getBean(SystemManager.class);

        BuildProperties buildProperties = new BuildProperties();
        
        final PSS2Properties params = systemManager.getPss2Properties();
		// If core property version is not set or blank
		if(StringUtils.isBlank(params.getVersion())){
			recForm.setVersion(params.getVersion());
		} else {
			recForm.setVersion(buildProperties.getVersion());
		}
        recForm.setRevision(buildProperties.getRevision());
        recForm.setBuildVersion(buildProperties.getVersion());
        recForm.setBuildTime(buildProperties.getBuildTimeStamp());
	    recForm.setHref1(params.getContactURLLink());
	    recForm.setPhone1(params.getContactPhone());
	    recForm.setMailto1(params.getContactEMail());
        recForm.setCopyright1("");
        recForm.setCopyright2("");
        recForm.setHref1DisplayName(params.getContactURLDisplayName());
        recForm.setLogoUrls(params.getLogoUrls());

        if (request.isUserInRole(DrasRole.Admin.toString())) {
            request.setAttribute("displayCache", true);
        }

        return mapping.findForward("success");
	}
}