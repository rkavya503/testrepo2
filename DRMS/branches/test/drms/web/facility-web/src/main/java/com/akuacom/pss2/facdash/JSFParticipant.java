/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFParticipant.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;

/**
 * The Class JSFParticipant.
 */
public class JSFParticipant implements Serializable {

    
	/** The name. */
	private String name;

	/**
	 * Instantiates a new jSF participant.
	 *
	 * @param name the name
	 */
	public JSFParticipant(String name)
	{
		this.name = name;
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
