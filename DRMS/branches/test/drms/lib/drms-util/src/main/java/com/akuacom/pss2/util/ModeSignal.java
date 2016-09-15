/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.ModeSignal.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

/**
 * The Class ModeSignal.
 */
public class ModeSignal
{
	// TODO: the two method calls could be done just using the enumeration
	
	/**
	 * The Enum Levels.
	 */
	public enum Levels {
	    
    	/** The normal. */
    	normal, 
 
 /** The moderate. */
 moderate, 
 
 /** The high. */
 high
	}

    /**
     * Gets the level string.
     * 
     * @param level the level
     * 
     * @return the level string
     */
    public static String getLevelString(double level) 
    {
        String levelString = "normal";
        if (level == 1.0) {
            levelString = "normal";
        } else if (level == 3.0) {
            levelString = "moderate";
        } else if (level == 5.0) {
            levelString = "high";
        }
        return levelString;
    }
    
    /**
     * Gets the level value.
     * 
     * @param levelString the level string
     * 
     * @return the level value
     */
    public static double getLevelValue(String levelString)
    {
        double level = 1.0;
        if (levelString.equals("normal")) {
            level = 1.0;
        } else if (levelString.equals("moderate")) {
            level = 3.0;
        } else if (levelString.equals("high")) {
            level = 5.0;
        }
        return level;
    }
}