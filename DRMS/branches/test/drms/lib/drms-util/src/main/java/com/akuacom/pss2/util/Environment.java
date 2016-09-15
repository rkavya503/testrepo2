/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.Environment.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

/**
 * Class that reads OS environment variables.
 * 
 * NOTE: This class doesn't do cache. So you don't want to
 */
public class Environment {
    
    /** The slow log threshold. */
    private static long slowLogThreshold = 1000;

    // static initializer for slow method threshold
    static {
        final String value = System.getenv("slowMethodThreshold");
        if (value != null && value.length() > 0) {
            try {
                slowLogThreshold = Long.parseLong(value);
            } catch (NumberFormatException e) {
                //
            }
        }
    }

    /**
     * Gets the slow log threshold.
     * 
     * @return the slow log threshold
     */
    public static long getSlowLogThreshold() {
        return slowLogThreshold;
    }


    /** The akuacom email only. */
    private static boolean akuacomEmailOnly = true;

    // static initializer for send email to akua only flag
    static {
        final String value = System.getenv("akuacomEmailOnly");
        if (value != null && value.length() > 0) {
            akuacomEmailOnly = Boolean.parseBoolean(value);
        }
    }

    /**
     * Checks if is akuacom email only.
     * 
     * @return true, if is akuacom email only
     */
    public static boolean isAkuacomEmailOnly() {
        return akuacomEmailOnly;
    }

}
