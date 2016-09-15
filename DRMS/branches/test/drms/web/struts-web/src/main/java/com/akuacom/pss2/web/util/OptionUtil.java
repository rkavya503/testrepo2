/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.util.OptionUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.util;

import com.akuacom.utils.Tag;

import java.util.List;
import java.util.ArrayList;

/**
 * The Class OptionUtil.
 */
public class OptionUtil {
    
    /** The hours list. */
    private static List<Tag> hoursList;
    
    /** The min sec list. */
    private static List<Tag> minSecList;

    /**
     * Gets the hours list.
     * 
     * @return the hours list
     */
    public static List<Tag> getHoursList() {
        if (hoursList == null) {
            hoursList = getNumberList(24);
        }
        return hoursList;
    }

    /**
     * Gets the min sec list.
     * 
     * @return the min sec list
     */
    public static List<Tag> getMinSecList() {
        if (minSecList == null) {
            minSecList = getNumberList(60);
        }
        return minSecList;
    }

    /**
     * Gets the number list.
     * 
     * @param limit the limit
     * 
     * @return the number list
     */
    private static List<Tag> getNumberList(final int limit) {
        List<Tag> list = new ArrayList<Tag>();
        for (int i = 0; i < limit; i++) {
            list.add(new Tag(i + "", i + ""));
        }
        return list;
    }

    /** The log levels. */
    private static List<Tag> logLevels;

    /**
     * Gets the log levels.
     * 
     * @return the log levels
     */
    public static List<Tag> getLogLevels() {
        if (logLevels == null) {
            logLevels = new ArrayList<Tag>();
            logLevels.add(new Tag("ALL", "ALL"));
            logLevels.add(new Tag("TRACE", "TRACE"));
            logLevels.add(new Tag("DEBUG", "DEBUG"));
            logLevels.add(new Tag("INFO", "INFO"));
            logLevels.add(new Tag("WARN", "WARN"));
            logLevels.add(new Tag("ERROR", "ERROR"));
            logLevels.add(new Tag("FATAL", "FATAL"));
        }
        return logLevels;
    }
}
