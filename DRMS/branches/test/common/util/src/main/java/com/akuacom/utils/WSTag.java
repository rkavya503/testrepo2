/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.WSTag.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.io.Serializable;

/**
 * The Class WSTag.
 */
public class WSTag implements Serializable
{
	
	/** The name. */
	private String name;
	
	/** The value. */
	private String value;
	
	/**
	 * Instantiates a new wS tag.
	 */
	public WSTag()
	{	
	}
	
	/**
	 * Instantiates a new wS tag.
	 * 
	 * @param theName the the name
	 * @param theValue the the value
	 */
	public WSTag(String theName, String theValue)
	{
		name = theName;
		value = theValue;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the new value
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
}
