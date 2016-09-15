/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.options.EmailsAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.options;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.email.EmailManager;
import com.akuacom.pss2.email.EmailManagerBean;
import com.akuacom.pss2.email.MessageEntity;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.akuacom.pss2.web.util.ValidateTool;

/**
 * The Class EmailsAction.
 */
public class EmailsAction extends DispatchAction {


    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        ServletContext application=httpServletRequest.getSession().getServletContext();
        String dateFormat=(String)application.getAttribute("dateFormat");
		
        // default search past month
    	EmailsForm form = (EmailsForm) actionForm;
        final Calendar cal = Calendar.getInstance();
        final Date end = cal.getTime();
        final String endDate = ValidateTool.getDateFormat(dateFormat).format(end);
        form.setEndDate(endDate);
        cal.add(Calendar.DATE, -10);
        final Date start = cal.getTime();
        final String startDate = ValidateTool.getDateFormat(dateFormat).format(start);
        form.setStartDate(startDate);

        //EmailManager em = EJBFactory.getLocalBean(EmailManager.R.class);
        //final List<MessageEntity> messageVOs = em.getAllMessages();
        //form.setEmails(messageVOs);

        return actionMapping.findForward("success");
    }

    
	/**
	 * List.
	 * 
	 * @param actionMapping the action mapping
	 * @param actionForm the action form
	 * @param httpServletRequest the http servlet request
	 * @param httpServletResponse the http servlet response
	 * 
	 * @return the action forward
	 * 
	 * @throws Exception the exception
	 */
	@SuppressWarnings({"UnusedDeclaration"})
    public ActionForward list(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

    	EmailsForm form = (EmailsForm) actionForm;

        ServletContext application=httpServletRequest.getSession().getServletContext();
        String dateFormat=(String)application.getAttribute("dateFormat");
		
        final ActionErrors actionErrors = validateInputs(form, dateFormat);
        if (actionErrors.size() > 0) {
            saveErrors(httpServletRequest, actionErrors);
            return actionMapping.findForward("success");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        final Date startTime = simpleDateFormat.parse(form.getStartDate());
		final Date endTime = simpleDateFormat.parse(form.getEndDate());
		
        EmailManager em = EJBFactory.getLocalBean(EmailManagerBean.class);
		final List<MessageEntity> list = em.getMessages(startTime, com.akuacom.utils.lang.DateUtil.endOfDay(endTime));
		form.setEmails(list);
		
        if (DisplayTagUtil.isExport(httpServletRequest)) {
            return actionMapping.findForward("export");
        } 
        else {
            return actionMapping.findForward("success");
        }
	}

    private ActionErrors validateInputs(EmailsForm form, String dateFormat) {
        ActionErrors errors = new ActionErrors();

        // validate start date
        final String startDate = form.getStartDate();
        ValidateTool.validateDateField(errors, startDate, "startDate", "Start Date", dateFormat);
        // validate end date
        final String endDate = form.getEndDate();
        ValidateTool.validateDateField(errors, endDate, "endDate", "End Date", dateFormat);

        return errors;
    }

    
    /**
     * Send.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param httpServletRequest the http servlet request
     * @param httpServletResponse the http servlet response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward send(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        EmailsForm form = (EmailsForm) actionForm;

        EmailManager em = EJBFactory.getLocalBean(EmailManagerBean.class);
        
        em.sendMessage(form.getMessageId());

        return list(actionMapping, actionForm, httpServletRequest, httpServletResponse);
    }

    /**
     * Send All.
     *
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param httpServletRequest the http servlet request
     * @param httpServletResponse the http servlet response
     *
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward sendAll(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        EmailManager em = EJBFactory.getLocalBean(EmailManagerBean.class);

        final List<MessageEntity> messages = em.getAllMessages();
        for (MessageEntity message : messages) {
            em.sendMessage(message.getUUID());
        }

        return list(actionMapping, actionForm, httpServletRequest, httpServletResponse);
    }

    public ActionForward viewEmailDetails(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
    	
        EmailsForm form = (EmailsForm) actionForm;
        
        EmailManager em = EJBFactory.getLocalBean(EmailManagerBean.class);
        form.setEmail(em.getMessage(form.getMessageId()));

        return actionMapping.findForward("viewEmailDetails");
    }
    
}
