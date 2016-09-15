/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFInvalidDate.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.util.Date;

import com.akuacom.pss2.program.participant.InvalidDate;

/**
 * The Class JSFInvalidDate.
 */
public class JSFInvalidDate implements Comparable<JSFInvalidDate>
{
	
	/** The invalid date. */
	private InvalidDate invalidDate;
	
	/** The delete. */
	private boolean delete;
	
	/**
	 * Instantiates a new jSF invalid date.
	 */
	public JSFInvalidDate()
	{
		this.invalidDate = new InvalidDate();
	}

	/**
	 * Instantiates a new jSF invalid date.
	 * 
	 * @param invalidDate the invalid date
	 */
	public JSFInvalidDate(InvalidDate invalidDate)
	{
		this.invalidDate = invalidDate;
	}

	/**
	 * Instantiates a new jSF invalid date.
	 * 
	 * @param date the date
	 */
	public JSFInvalidDate(Date date)
	{
		this.invalidDate = new InvalidDate();
		this.invalidDate.setInvalidDate(date);
	}

	/**
	 * Checks if is delete.
	 * 
	 * @return true, if is delete
	 */
	public boolean isDelete()
	{
		return delete;
	}
	
	/**
	 * Sets the delete.
	 * 
	 * @param delete the new delete
	 */
	public void setDelete(boolean delete)
	{
		this.delete = delete;
	}

	/**
	 * Gets the invalid date.
	 * 
	 * @return the invalid date
	 */
	public InvalidDate getInvalidDate()
	{
		return invalidDate;
	}

	/**
	 * Sets the invalid date.
	 * 
	 * @param invalidDate the new invalid date
	 */
	public void setInvalidDate(InvalidDate invalidDate)
	{
		this.invalidDate = invalidDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(JSFInvalidDate other)
	{
		return invalidDate.getInvalidDate().
			compareTo(other.invalidDate.getInvalidDate());
	}
}
