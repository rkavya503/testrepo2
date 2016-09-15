package com.akuacom.pss2.drw.cache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class DrRunTimerListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

		ServletContext servletContext = servletContextEvent.getServletContext();

		// get our timer from the Context
		ScheduledFuture<?> timeHandle = (ScheduledFuture<?>)servletContext.getAttribute ("mtimer");

		// cancel all pending tasks in the timers queue
		if (timeHandle != null)
	      timeHandle.cancel(false);
		
		// remove the timer from the servlet context
		servletContext.removeAttribute ("mtimer");
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		//***run timer
		ServletContext servletContext = servletContextEvent.getServletContext();
		try{
			// init cache
			DrEventCache.getInstance();

			// create the timer and timer task objects
//			Timer mtimer = new Timer();
//			DRMTimerTask task = new DRMTimerTask(servletContext);

			   // Get the scheduler
		    ScheduledExecutorService scheduler =
		      Executors.newSingleThreadScheduledExecutor();

		    // Get a handle, starting now, with a 10 second delay
		    final ScheduledFuture<?> timeHandle =
		      scheduler.scheduleAtFixedRate(new DRMTimerTask(servletContext), 0, 10, TimeUnit.SECONDS);    

			
			// get a calendar to initialize the start time
//			Calendar calendar = Calendar.getInstance();
//			Date startTime = calendar.getTime();

			// schedule the task to run 10 seconds
//			mtimer.scheduleAtFixedRate(task, startTime, 10000);

			// save our timer for later use
			servletContext.setAttribute("mtimer", timeHandle);
		} catch (Exception e) {
		 servletContext.log ("Problem initializing the task that was to run hourly: " + e.getMessage ());
		}
		
	}

}
