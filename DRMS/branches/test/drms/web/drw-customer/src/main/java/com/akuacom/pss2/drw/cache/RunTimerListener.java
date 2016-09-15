package com.akuacom.pss2.drw.cache;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.StatusCache;

public class RunTimerListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

		ServletContext servletContext = servletContextEvent.getServletContext();

		// get our timer from the Context
		Timer timer = (Timer)servletContext.getAttribute ("timer");

		// cancel all pending tasks in the timers queue
		if (timer != null)
		timer.cancel();

		// remove the timer from the servlet context
		servletContext.removeAttribute ("timer");
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		//***run timer
		ServletContext servletContext = servletContextEvent.getServletContext();
		try{
			// init cache
			EventCache eventCache = EventCache.getInstance();
			StatusCache statusCache = StatusCache.getInstance();

			// create the timer and timer task objects
			Timer timer = new Timer();
			DRTimerTask task = new DRTimerTask(servletContext);

			// get a calendar to initialize the start time
			Calendar calendar = Calendar.getInstance();
			Date startTime = calendar.getTime();

			// schedule the task to run 10 seconds
			timer.scheduleAtFixedRate(task, startTime, 10000);

			// save our timer for later use
			servletContext.setAttribute("timer", timer);
		} catch (Exception e) {
		 servletContext.log ("Problem initializing the task that was to run hourly: " + e.getMessage ());
		}
		
	}

}
