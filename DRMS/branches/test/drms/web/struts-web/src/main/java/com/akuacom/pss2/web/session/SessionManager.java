/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.session.SessionManager.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.session;

import java.rmi.server.UID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.web.util.EJBFactory;

/**
 * This class helps session management, do dirty works instead of jsp.
 * 
 * @author Dichen Mao
 * @since Oct 2, 2007
 */
public class SessionManager {

    /**
     * Inits the.
     * 
     * @param session the session
     */
    public static void init(HttpSession session) {
    	SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
        String utilityName = systemManager.getPss2Properties().getUtilityName();
        session.setAttribute("utilityName", utilityName);

        /* The uid is used for logout to identify if the server is restarted */
        UID uid = new UID();
        session.setAttribute("uid", uid.toString());
    }

    /**
     * Inits the.
     * 
     * @param request the request
     */
    public static void init(HttpServletRequest request) {
        init(request.getSession());
    }
}
