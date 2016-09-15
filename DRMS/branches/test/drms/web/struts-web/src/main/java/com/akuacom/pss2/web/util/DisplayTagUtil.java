/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.util.DisplayTagUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * The Class DisplayTagUtil.
 */
public class DisplayTagUtil {
    
    /**
     * Checks if is export.
     * 
     * @param request the request
     * 
     * @return true, if is export
     */
    public static boolean isExport(HttpServletRequest request) {
        final Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            if (s.matches("d-.*-e")) {
                return true;
            }
        }
        return false;
    }
}
