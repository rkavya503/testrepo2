/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.options.OptionsSignalAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.options;

import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.accmgr.ws.AccMgrWS;
import com.akuacom.accmgr.ws.User;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.utils.MD5Tool;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The Class OptionsSignalAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class OptionsSignalAction extends DispatchAction {
	
	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward unspecified(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) throws Exception
	{
		OptionsSignalForm form = (OptionsSignalForm) actionForm;

		return actionMapping.findForward("success");
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
	public ActionForward send(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) throws Exception
	{
		OptionsSignalForm form = (OptionsSignalForm) actionForm;

		return unspecified(actionMapping, actionForm, httpServletRequest,
			httpServletResponse);
	}}