/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.BidMapping.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.cpp;

import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.rule.Rule;
import java.io.Serializable;

/**
 * The Class BidMapping.
 */
public class CPPShedStrategy implements Serializable {
	/**
	 * The Enum Mode.
	 */
	public enum ShedMode
	{
		DEFAULT,
		NORMAL,
		MODERATE,
        SPECIAL,
		HIGH
	}

	/** The time block. */
	private TimeBlock timeBlock;
	
	/** The normal. */
	private ShedMode mode;	
	
	/**
	 * Instantiates a new bid mapping.
	 */
	public CPPShedStrategy()
	{
	}
	
	public TimeBlock getTimeBlock()
	{
		return timeBlock;
	}
	
	public String getTimeBlockString()
	{
		return timeBlock.toString();
	}
	
	public void setTimeBlock(TimeBlock timeBlock)
	{
		this.timeBlock = timeBlock;
	}

	public ShedMode getMode()
	{
		return mode;
	}

	public void setMode(ShedMode mode)
	{
		this.mode = mode;
	}
	
	public void setRuleMode(Rule.Mode ruleMode)
	{
		switch(ruleMode)
		{
		case NORMAL:
			this.mode = ShedMode.NORMAL;
			break;
		case MODERATE:
			this.mode = ShedMode.MODERATE;
			break;
		case HIGH:
			this.mode = ShedMode.HIGH;
			break;
         case SPECIAL:
			this.mode = ShedMode.SPECIAL;
			break;
		}
	}
	
	public Rule.Mode getRuleMode()
	{
		switch(mode)
		{
		case NORMAL:
			return Rule.Mode.NORMAL;
		case MODERATE:
			return Rule.Mode.MODERATE;
		case HIGH:
			return Rule.Mode.HIGH;
		case SPECIAL:
			return Rule.Mode.SPECIAL;
		default:
			return Rule.Mode.NORMAL;
		}
	}
}
