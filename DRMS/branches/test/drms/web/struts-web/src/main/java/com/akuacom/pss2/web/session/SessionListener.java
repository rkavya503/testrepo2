/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.session.SessionListener.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Manages PSS2 session lifetime cycle.
 * 
 * @author Dichen Mao
 * @since Oct 2, 2007
 */
public class SessionListener implements HttpSessionListener {

    /* Session is created. */
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent se) {
        SessionManager.init(se.getSession());
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }
}
