/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.WSUtils.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The Class WSUtils.
 */
public class WSUtils
{
	
	/**
	 * Creates the ws time stamp.
	 * 
	 * @param date the date
	 * 
	 * @return the wS time stamp
	 */
	public static WSTimeStamp createWSTimeStamp(Date date)
	{
		if(date == null)
		{
			return new WSTimeStamp(
			  0,
			  0,
			  0,
			  0,
			  0,
			  0,
			  0);
		}
		GregorianCalendar ts = new GregorianCalendar();
		ts.setTime(date);
		return new WSTimeStamp(
		  ts.get(Calendar.YEAR),
		  ts.get(Calendar.MONTH),
		  ts.get(Calendar.DAY_OF_MONTH),
		  ts.get(Calendar.HOUR_OF_DAY),
		  ts.get(Calendar.MINUTE),
		  ts.get(Calendar.SECOND),
		  ts.get(Calendar.MILLISECOND));
	}

	/**
	 * Parses the ws time stamp.
	 * 
	 * @param timestamp the timestamp
	 * 
	 * @return the date
	 */
	public static Date parseWSTimeStamp(WSTimeStamp timestamp)
	{
		if(timestamp == null)
		{
			return null;
		}
		Date date = new GregorianCalendar(
		  timestamp.getYear(),
		  timestamp.getMonth(),
		  timestamp.getDay(),
		  timestamp.getHour(),
		  timestamp.getMinute(),
		  timestamp.getSecond()).getTime();
		return date;
	}

	/**
	 * Creates the ws tags.
	 * 
	 * @param tags the tags
	 * 
	 * @return the wS tag[]
	 */
	public static WSTag[] createWSTags(Tag[] tags)
	{
		if(tags == null)
		{
			return null;
		}
		WSTag[] wsTags = new WSTag[tags.length];
		int i = 0;
		for(Tag tag: tags)
		{
			wsTags[i++] = new WSTag(tag.getName(), tag.getValue());
		}
		return wsTags;
	}

	/**
	 * Parses the ws tags.
	 * 
	 * @param wsTags the ws tags
	 * 
	 * @return the tag[]
	 */
	public static Tag[] parseWSTags(WSTag[] wsTags)
	{
		if(wsTags == null)
		{
			return null;
		}
		Tag[] tags = new Tag[wsTags.length];
		int i = 0;
		for(WSTag wsTag: wsTags)
		{
			tags[i++] = new Tag(wsTag.getName(), wsTag.getValue());
		}
		return tags;
	}

	/**
	 * Encode string no null.
	 * 
	 * @param string the string
	 * 
	 * @return the string
	 */
	public static String encodeStringNoNull(String string)
	{
		return string == null ? "" : string;
	}
}
