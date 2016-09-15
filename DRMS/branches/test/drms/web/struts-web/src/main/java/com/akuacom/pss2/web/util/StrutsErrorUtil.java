/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.util.ErrorUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.util;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.akuacom.pss2.core.ErrorUtil;

/**
 * The Class ErrorUtil.
 */
public class StrutsErrorUtil 
{    
    /**
     * Creates the action errors.
     * 
     * @param e the e
     * 
     * @return the action errors
     */
    public static ActionErrors createActionErrors(Exception e) 
    {
		final ActionErrors errors = new ActionErrors();
		for(String errorMessage: ErrorUtil.getErrorMessage(e).split("\n"))
		{
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"pss2.event.create.creationError", errorMessage));
		}
		return errors;
    }
    
    /**
     * Creates the action errors.
     * 
     * @param propertyName the property name
     * @param actionMessage the action message
     * 
     * @return the action errors
     */
    public static ActionErrors createActionErrors(String propertyName,
    	ActionMessage actionMessage)
    {
		final ActionErrors errors = new ActionErrors();
		errors.add(propertyName, actionMessage);
		return errors;
    } 
}
