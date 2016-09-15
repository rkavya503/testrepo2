/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.options.OptionsAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.nssettings;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.nssettings.CleanMessageTimerService;
import com.akuacom.pss2.nssettings.NSSettings;
import com.akuacom.pss2.nssettings.NSSettingsManager;

/**
 * The Class NSSettingsAction.
 * 
 * @author Li Fei
 * Initial date 2010.07.01
 * 
 */
public class NSSettingsAction extends Action {
	
	private static final Logger log = Logger.getLogger(NSSettingsAction.class);

	/* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
    	
    	String actiontype = request.getParameter("actiontype") + "";
    	NSSettingsForm nsForm = (NSSettingsForm)form;
    	
    	if(actiontype.equals("save")){
    		if(nsForm.getDuration() <= 0 || nsForm.getCleanMsgHour() < 0 || nsForm.getCleanMsgHour() > 23 || nsForm.getCleanMsgMinute() < 0 || nsForm.getCleanMsgMinute() >  59){
                return edit(mapping, form, request, response);    			
    		}
            return save(mapping, form, request, response);
    	}
    	else{
            return edit(mapping, form, request, response);
    	}
    }
    
    public ActionForward edit(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
    	
    	NSSettingsManager nsSettingManager = EJB3Factory.getLocalBean(NSSettingsManager.class);
    	NSSettings nsSettingsEntity = nsSettingManager.getNSSettings();

    	NSSettingsForm nsForm = (NSSettingsForm)form;
    	
    	nsForm.setUUID(nsSettingsEntity.getUUID());
    	nsForm.setFilterStatus(nsSettingsEntity.getFilterStatus());
    	nsForm.setMsgExpireTime(nsSettingsEntity.getMsgExpireTime());
    	nsForm.setMsgThreshold(nsSettingsEntity.getMsgThreshold());
    	nsForm.setFrequency(nsSettingsEntity.getFrequency());
    	nsForm.setDuration(nsSettingsEntity.getDuration());
    	nsForm.setCleanMsgHour(nsSettingsEntity.getCleanMsgHour());
    	nsForm.setCleanMsgMinute(nsSettingsEntity.getCleanMsgMinute());
    	
    	request.setAttribute("nssettings", nsForm);
    	
    	return mapping.findForward("edit");
    }
    
    public ActionForward save(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
    	
    	NSSettingsManager nsSettingManager = EJB3Factory.getLocalBean(NSSettingsManager.class);
    	NSSettingsForm nsForm = (NSSettingsForm)form;
    	    	
    	NSSettings nsEntity = new NSSettings();
    	
    	nsEntity.setUUID(nsForm.getUUID());
    	nsEntity.setFilterStatus(nsForm.getFilterStatus());
    	nsEntity.setMsgExpireTime(nsForm.getMsgExpireTime());
    	nsEntity.setMsgThreshold(nsForm.getMsgThreshold());
    	nsEntity.setFrequency(nsForm.getFrequency());
    	nsEntity.setDuration(nsForm.getDuration());
    	nsEntity.setCleanMsgHour(nsForm.getCleanMsgHour());
    	nsEntity.setCleanMsgMinute(nsForm.getCleanMsgMinute());
    	
    	nsSettingManager.saveNSSettings(nsEntity);

    	CleanMessageTimerService cts = EJB3Factory.getBean(CleanMessageTimerService.class);
    	cts.scheduleTimer();
    	
    	return edit(mapping, form, request, response);
	}
    
}
