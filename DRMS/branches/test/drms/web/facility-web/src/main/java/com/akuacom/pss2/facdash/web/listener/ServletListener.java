package com.akuacom.pss2.facdash.web.listener;
/**
 * 
 */


import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;

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
        
        //***run timer
  		ServletContext servletContext = sce.getServletContext();
  		try{
  		 // create the timer and timer task objects
  		  Timer timer = new Timer();
  		  DRTimerTask task = new DRTimerTask(servletContext);
  		  // get a calendar to initialize the start time
  		  Calendar calendar = Calendar.getInstance();
  		  Date startTime = calendar.getTime();
  		  // schedule the task to run 10 minutes
		 
		  long interval = Long.valueOf(Double.valueOf(systemManager.getPss2Properties().getWeatherRefreshinterval()).intValue());
  		  timer.scheduleAtFixedRate(task, startTime, interval);
  		  // save our timer for later use
  		  servletContext.setAttribute ("timer", timer);
  		  // init cache
  		  StatusCache.getInstance();
  		} catch (Exception e) {
  		 servletContext.log ("Problem initializing the task that was to run periodically: " + e.getMessage ());
  		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		ServletContext servletContext = sce.getServletContext();
		// get our timer from the Context
		Timer timer = (Timer)servletContext.getAttribute ("timer");
		// cancel all pending tasks in the timers queue
		if (timer != null)
		timer.cancel();
		// remove the timer from the servlet context
		servletContext.removeAttribute ("timer");

	}

}
