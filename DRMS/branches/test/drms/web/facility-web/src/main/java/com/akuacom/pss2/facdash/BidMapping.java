/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.BidMapping.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;

/**
 * The Class BidMapping.
 */
public class BidMapping implements Serializable 
{
	private static final long serialVersionUID = 6391580916261140136L;

	/** The time block. */
	private String timeBlock;
	
	/** The normal. */
	private String normal;
	
	/** The moderate. */
	private String moderate;
	
	/** The high. */
	private String high;
	
	
	/**
	 * Instantiates a new bid mapping.
	 */
	public BidMapping()
	{
	}
	
	/**
	 * Gets the time block.
	 * 
	 * @return the time block
	 */
	public String getTimeBlock()
	{
		return timeBlock;
	}
	
	/**
	 * Sets the time block.
	 * 
	 * @param timeBlock the new time block
	 */
	public void setTimeBlock(String timeBlock)
	{
		this.timeBlock = timeBlock;
	}
	
	/**
	 * Gets the normal.
	 * 
	 * @return the normal
	 */
	public String getNormal()
	{
		return normal;
	}
	
	/**
	 * Sets the normal.
	 * 
	 * @param normal the new normal
	 */
	public void setNormal(String normal)
	{
		this.normal = normal;
	}
	
	/**
	 * Gets the moderate.
	 * 
	 * @return the moderate
	 */
	public String getModerate()
	{
		return moderate;
	}
	
	/**
	 * Sets the moderate.
	 * 
	 * @param moderate the new moderate
	 */
	public void setModerate(String moderate)
	{
		this.moderate = moderate;
	}
	
	/**
	 * Gets the high.
	 * 
	 * @return the high
	 */
	public String getHigh()
	{
		return high;
	}
	
	/**
	 * Sets the high.
	 * 
	 * @param high the new high
	 */
	public void setHigh(String high)
	{
		this.high = high;
	}
	
}
