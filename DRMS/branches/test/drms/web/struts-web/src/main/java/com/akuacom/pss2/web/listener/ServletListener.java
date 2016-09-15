/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.listener.ServletListener.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.listener;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.utils.BuildProperties;


/**
 * This listener initialize the web application.
 * 
 * @since Oct 4, 2007
 */
public class ServletListener implements ServletContextListener {
    
    private static Logger log = Logger.getLogger(ServletListener.class);
    private SystemManager systemManager = EJBFactory.getBean(SystemManager.class);

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // get build time stamp and print it
    	BuildProperties buildProperties = new BuildProperties();
        log.info("--------------------------------------------------");
        log.info(" Application build Time Stamp:  " + buildProperties.getBuildTimeStamp());
        log.info(" Application build Version:     " + buildProperties.getVersion());
        log.info(" Application build SVN Revision:" + buildProperties.getRevision());
        log.info(" Application build SVN Branch:  " + buildProperties.getScmBranch());
        log.info("--------------------------------------------------");

        // initialize Program Manager
        systemManager.initialize();
        final String s = systemManager.getPss2Properties().getUtilityName();
        final ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute("utility", s);
        final String dateFormat=systemManager.getPss2Features().getDateFormat();
        context.setAttribute("dateFormat", dateFormat);
        final String millsTimeFormat=dateFormat+" HH:mm:ss.SSS";
        context.setAttribute("millsTimeFormat", millsTimeFormat);

        final Boolean time24hours = systemManager.getPss2Features().isTime24Hours();
        if (time24hours!=null && time24hours) {
        	final String fullTimeFormat="HH:mm:ss";
        	context.setAttribute("fullTimeFormat", fullTimeFormat);
        	context.setAttribute("shortTimeFormat", "HH:mm");
        	
        	final String dateFimeFormat=dateFormat+" "+fullTimeFormat;
        	context.setAttribute("dateTimeFormat", dateFimeFormat);
        } else {
        	final String fullTimeFormat="hh:mm:ss a";
        	context.setAttribute("fullTimeFormat", fullTimeFormat);
        	context.setAttribute("shortTimeFormat", "hh:mm a");
        	
        	final String dateFimeFormat=dateFormat+" "+fullTimeFormat;
        	context.setAttribute("dateTimeFormat", dateFimeFormat);
        }

        context.setAttribute("headerDateTimeFormat", context.getAttribute("dateTimeFormat") + " z");
    }

    /**
     * recycle something here?.
     * 
     * @param servletContextEvent event
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
