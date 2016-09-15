/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ProgramValidatationMessage.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.io.Serializable;

/**
 * Contains information regarding how a rule validation failed.  Normally returned
 * in as a list in a ProgramValidationException
 */
public class ProgramValidationMessage implements Serializable
{
	private static final long serialVersionUID = 7780330993274121852L;

	/** The parameter name. */
	public String parameterName;
	
	/** The description. */
	public String description;
	
	/**
	 * Instantiates a new program validation message.
	 */
	public ProgramValidationMessage()
	{
	}
	
	/**
	 * Instantiates a new program validation message.
	 * 
	 * @param parameterName the parameter name
	 * @param description the description
	 */
	public ProgramValidationMessage(String parameterName, String description)
	{
		this.parameterName = parameterName;
		this.description = description;
	}
	
	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description the new description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Gets the parameter name.
	 * 
	 * @return the parameter name
	 */
	public String getParameterName()
	{
		return parameterName;
	}
	
	/**
	 * Sets the parameter name.
	 * 
	 * @param parameterName the new parameter name
	 */
	public void setParameterName(String parameterName)
	{
		this.parameterName = parameterName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(parameterName);	
		sb.append(":");	
		sb.append(description);	
		sb.append("(");	
		sb.append("errorCode");	
		sb.append(")");	

		return sb.toString();
	}

}
