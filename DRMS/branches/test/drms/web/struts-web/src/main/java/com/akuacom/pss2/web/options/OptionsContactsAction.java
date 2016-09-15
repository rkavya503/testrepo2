/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.options.OptionsContactsAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.options;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * The Class OptionsContactsAction.
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class OptionsContactsAction extends DispatchAction
{
	
	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward unspecified(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) throws Exception
	{
		OptionsContactsForm form = (OptionsContactsForm) actionForm;

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
		OptionsContactsForm form = (OptionsContactsForm) actionForm;

		return unspecified(actionMapping, actionForm, httpServletRequest,
			httpServletResponse);
	}
}
