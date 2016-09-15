/**
 * 
 */
package com.akuacom.pss2.richsite.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.system.SystemManager;

/**
 *
 */
public class ServletListener implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
	    SystemManager systemManager = EJBFactory.getBean(SystemManager.class);

        final String s = systemManager.getPss2Properties().getUtilityName();
        ServletContext context = sce.getServletContext();
        context.setAttribute("utility", s);
        final String dateFormat=systemManager.getPss2Features().getDateFormat();
        context.setAttribute("dateFormat", dateFormat);
        
        final Boolean time24hours=systemManager.getPss2Features().isTime24Hours();
    	String fullTimeFormat="HH:mm:ss";
    	String shortTimeFormat="HH:mm";
    	
    	String dateFimeFormat=dateFormat+" "+fullTimeFormat;
    	String shortDateTimeFormat=dateFormat+" "+shortTimeFormat;
    	
        if (time24hours!=null && !time24hours) {
        	fullTimeFormat="hh:mm:ss a";
        	shortTimeFormat="hh:mm a";
        	
        	dateFimeFormat=dateFormat+" "+fullTimeFormat;
        	shortDateTimeFormat=dateFormat+" "+shortTimeFormat;
        }

    	context.setAttribute("fullTimeFormat", fullTimeFormat);
    	context.setAttribute("shortTimeFormat", shortTimeFormat);
    	context.setAttribute("fullDateTimeFormat", dateFimeFormat);
    	context.setAttribute("shortDateTimeFormat", shortDateTimeFormat);

        context.setAttribute("headerDateTimeFormat", shortDateTimeFormat + " z");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
