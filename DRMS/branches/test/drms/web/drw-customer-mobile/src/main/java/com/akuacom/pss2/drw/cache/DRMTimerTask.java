package com.akuacom.pss2.drw.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.Event;
import com.akuacom.utils.drw.CacheNotificationMessage;

//update schedule and active events
public class DRMTimerTask extends java.util.TimerTask {

	private static final Logger log = Logger.getLogger(DRMTimerTask.class);

	public DRMTimerTask(ServletContext ctx) {
		this.ctx = ctx;
	}

	@SuppressWarnings("unused")
	private ServletContext ctx;
	private List<Event> cachedEvents = null;
	
	@Override
	public void run() {
		try {
			updateStatus();
		} catch (Exception e) {
			log.error("DR Website site periodically event status monitor catch error: "
					+ e);
		}
	}

	private void updateStatus() {
		// handle events;
		cachedEvents = new ArrayList<Event>(new HashSet<Event>(DrEventCache.getInstance().getList()));

		Date current = new Date();
		Date nxtStartTime = null;
		for (Event event : cachedEvents) {
			if (event.isActive()) {
				continue;					
			} else {
				Date startTime = event.getStartDateTime();
				Date issueTime = event.getIssueTime();
				boolean scheduleToActiveFlag = false;
				boolean scheduledStill = false;
				
				if (startTime != null && current != null) {
					if (startTime.before(current)) {
						scheduleToActiveFlag = true;
					} else if (startTime.after(current)) {
						scheduledStill = true;
					}
				}

				if(nxtStartTime==null && scheduleToActiveFlag){
					nxtStartTime = startTime;
				}
				
				// schedule - > active
				if (scheduleToActiveFlag && startTime.equals(nxtStartTime)) {
					CacheNotificationMessage msg = new CacheNotificationMessage();
					msg.setProgramName(event.getProgram());
					msg.setProduct(event.getProduct());
					msg.setuUID(event.getUuid());
					msg.setActive(false);
					DrEventCache.getInstance().updateRemCache(msg);
					
					msg = new CacheNotificationMessage();
					msg.setProgramName(event.getProgram());
					msg.setProduct(event.getProduct());
					msg.setuUID(event.getUuid());
					msg.setActive(true);
					event.setActive(true);
					DrEventCache.getInstance().updateActCache(msg);
					nxtStartTime=startTime;
				}

				if (scheduledStill) {
					if(event.getIr() && issueTime != null && issueTime.after(current)){ //For IR event, not show in schedule list until issue time is reached
						CacheNotificationMessage msg = new CacheNotificationMessage();
						msg.setProgramName(event.getProgram());
						msg.setProduct(event.getProduct());
						msg.setuUID(event.getUuid());
						msg.setActive(false);
						DrEventCache.getInstance().updateRemCache(msg);
					}else{
						CacheNotificationMessage msg = new CacheNotificationMessage();
						msg.setProgramName(event.getProgram());
						msg.setProduct(event.getProduct());
						msg.setuUID(event.getUuid());
						msg.setActive(false);
						DrEventCache.getInstance().updateSCHDCache(msg);
					}
				}
			}
		}

		
//		Status
		
//		switch(run)
		
		// active - > history
		int eventStatus  = 0;
		for(Event event :cachedEvents ){
			if(!event.isActive()){
				continue;
			}else if(event.isEstimated()){
				continue;
			}
			Date endTime = event.getEndDateTime();
			if(endTime!=null&&current!=null){
				if(endTime.before(current)){
					eventStatus = 0;
				}else{
					eventStatus = 1;
				}
				switch(eventStatus){
					case 0:
						DrEventCache.getInstance().getList().remove(event); 
						break;
					case 1:
						CacheNotificationMessage msg = new CacheNotificationMessage();
						msg.setProgramName(event.getProgram());
						msg.setProduct(event.getProduct());
						msg.setuUID(event.getUuid());
						msg.setActive(true);
						DrEventCache.getInstance().updateDelCache(msg);						
						break;
				}
			}
			
		}
		DrEventCache.getInstance().setUpdateTime(current);
	}
}