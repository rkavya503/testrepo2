/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.about.AboutAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.about;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.akuacom.common.text.CustomFormat;
import com.akuacom.common.text.CustomFormatFactory;
import com.akuacom.common.text.DecimalFormat;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSetEAO;
import com.akuacom.pss2.data.PDataSetEAOBean;
import com.akuacom.pss2.program.scertp.entities.WeatherEAO;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.utils.BuildProperties;

/**
 * The Class AboutAction.
 */
public class AboutAction extends Action {

    static final String ABOUT_INFO_DEFAULT = "";
    protected static CustomFormatFactory factory;
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    	
        final AboutForm recForm = (AboutForm) form;
        String clientDataReportingInterval;
        PDataSetEAO pDataSetEAO = EJBFactory.getBean(PDataSetEAO.class);
        CustomFormatFactory factory = CustomFormatFactory.getInstance();
        final Locale locale = Locale.getDefault();

        SystemManager systemManager = EJB3Factory
                .getLocalBean(SystemManager.class);
        
        BuildProperties buildProperties = new BuildProperties();

		PDataSet pds = pDataSetEAO.getDataSetByName("Usage"); 
		CustomFormat formatter = factory.getFormat(Long.class);
		clientDataReportingInterval = formatter.format(pds.getPeriod()/60, locale);
		
        final PSS2Properties params = systemManager.getPss2Properties();
		// If core property version is not set or blank
		if(StringUtils.isBlank(params.getVersion())){
			recForm.setVersion(buildProperties.getVersion());
		} else {
			recForm.setVersion(params.getVersion());
		}
        recForm.setRevision(buildProperties.getRevision());
        recForm.setBuildVersion(buildProperties.getVersion());
        recForm.setBuildBranch(buildProperties.getScmBranch());
        recForm.setBuildTime(buildProperties.getBuildTimeStamp());
        recForm.setHref1(params.getContactURLLink());
        recForm.setPhone1(params.getContactPhone());
        recForm.setMailto1(params.getContactEMail());
        recForm.setCopyright1("");
        recForm.setCopyright2("");
        recForm.setHref1DisplayName(params.getContactURLDisplayName());
        recForm.setLogoUrls(params.getLogoUrls());
        recForm.setUtility(params.getUtilityDisplayName());
        recForm.setClientDataReportingInterval(clientDataReportingInterval);
        

        if (request.isUserInRole(DrasRole.Admin.toString())) {
            request.setAttribute("displayCache", true);
        }

        String aboutInfo = params.getTextValue(PropertyName.ABOUT_INFORMATION,
                ABOUT_INFO_DEFAULT);

        request.setAttribute(PropertyName.ABOUT_INFORMATION.getPropertyName(),
                aboutInfo);

        return mapping.findForward("success");
    }
}
