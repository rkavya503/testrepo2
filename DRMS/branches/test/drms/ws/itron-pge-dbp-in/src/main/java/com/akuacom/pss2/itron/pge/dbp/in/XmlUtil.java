/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.itron.pge.dbp.in.XmlUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.itron.pge.dbp.in;

/**
 * The Class XmlUtil.
 */
public class XmlUtil {

    /**
     * Removes the message envelope.
     * 
     * @param line the line
     * @param beginStr the begin str
     * @param endStr the end str
     * 
     * @return the string
     */
    public static String removeMessageEnvelope(String line, final String beginStr, final String endStr)
    {
        String hackedLine = line;
        int begIndex = line.indexOf(beginStr);
        if (begIndex != -1)
        {
            hackedLine = line.substring(begIndex);
        }

        int endIndex = hackedLine.lastIndexOf(endStr);
        if (endIndex != -1)
        {
            endIndex += endStr.length();
            hackedLine = hackedLine.substring(0, endIndex);
        }

        return hackedLine;
    }
}
