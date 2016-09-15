/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.EventUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class EventUtil.
 */
public class EventUtil {
    
    /**
     * Gets the event name.
     * 
     * @return the event name
     */
    public static String getEventName() {
        final Date now = new Date();
        return getEventName(now);
    }

    private static String getEventName(Date now) {
        final SimpleDateFormat format = new SimpleDateFormat("yyMMdd-HHmmss");
        return format.format(now);
    }

    public static String getLegalName(String programName) {
		String temp = new String(programName);
		temp = temp.replace('<', '_');
		temp = temp.replace('>', '_');
		String res = temp;
		for(char c: temp.toCharArray())
		{
			if (!PSS2Util.isLegalChar(c))
				res = temp.replace(c, ' ');
		}
		
		return res;
	}
    
    public static String getUniqueEventName(String programName) {
    	return getLegalName(programName) + EventUtil.getEventName();
    }

    public static String getEventName(long now) {
        final Date date = new Date(now);
        return getEventName(date);
    }
}
