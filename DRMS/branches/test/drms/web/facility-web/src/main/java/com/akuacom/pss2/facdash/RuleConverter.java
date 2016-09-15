/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.RuleConverter.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.akuacom.pss2.rule.Rule;
import java.io.Serializable;

/**
 * The Class RuleConverter.
 */
public class RuleConverter implements javax.faces.convert.Converter,Serializable
{

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value)
	{
		try
		{
			String[] values = value.split("_");
			JSFRule rule = new JSFRule();
			rule.setDelete(Boolean.parseBoolean(values[0]));
			rule.getRule().setStart(new SimpleDateFormat("HH:mm").parse(values[1]));
			rule.getRule().setEnd(new SimpleDateFormat("HH:mm").parse(values[2]));
			rule.getRule().setMode(Rule.Mode.valueOf(values[3]));
			rule.getRule().setVariable(values[4]);
			rule.setOperator(values[5]);
			rule.getRule().setValue(Double.parseDouble(values[6]));
			return rule;
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		JSFRule rule = (JSFRule)value;
		return rule.toString();
	}
	
}
