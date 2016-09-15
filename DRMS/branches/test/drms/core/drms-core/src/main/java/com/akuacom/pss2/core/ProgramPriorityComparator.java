/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ProgramPriorityComparator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;


import java.util.Comparator;

import com.akuacom.pss2.program.Program;

/**
 * Comparator for sorting ProgramPriority.
 */
public class ProgramPriorityComparator implements Comparator 
{
	
	/**
	 * Instantiates a new program priority comparator.
	 */
	public ProgramPriorityComparator() 
	{
	}
	  
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object obj1, Object obj2) 
	{
		// Get the value of the properties
		Program p1 = (Program) obj1;
		Program p2 = (Program) obj2;
		if (p1.getPriority() > p2.getPriority())
		{
			return 1;
		}
		else if (p1.getPriority() == p2.getPriority())
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
}
