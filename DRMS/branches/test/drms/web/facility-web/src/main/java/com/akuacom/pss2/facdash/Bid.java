/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.Bid.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

/**
 * The Class Bid.
 */
public class Bid
{
	
	/** The time block. */
	private String timeBlock;
	
	/** The reduction kw. */
	private double reductionKW;
	
	/** The level. */
	private String level;
	
	/** The active. */
	private boolean active;
	
	/**
	 * Instantiates a new bid.
	 */
	public Bid()
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
	 * Gets the level.
	 * 
	 * @return the level
	 */
	public String getLevel()
	{
		return level;
	}
	
	/**
	 * Sets the level.
	 * 
	 * @param level the new level
	 */
	public void setLevel(String level)
	{
		this.level = level;
	}
	
	/**
	 * Checks if is active.
	 * 
	 * @return true, if is active
	 */
	public boolean isActive()
	{
		return active;
	}
	
	/**
	 * Sets the active.
	 * 
	 * @param active the new active
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}

	/**
	 * Gets the reduction kw.
	 * 
	 * @return the reduction kw
	 */
	public double getReductionKW()
	{
		return reductionKW;
	}

	/**
	 * Sets the reduction kw.
	 * 
	 * @param reductionKW the new reduction kw
	 */
	public void setReductionKW(double reductionKW)
	{
		this.reductionKW = reductionKW;
	}
}	
