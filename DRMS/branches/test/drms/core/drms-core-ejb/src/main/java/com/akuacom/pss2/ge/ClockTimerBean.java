package com.akuacom.pss2.ge;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import org.apache.log4j.Logger;

import com.akuacom.pss2.cache.GeInterfaceCache;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantEAO;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.timer.TimerManager;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.utils.lang.TimingUtil;

@Stateless
public class ClockTimerBean extends TimerManagerBean implements ClockTimer.L, ClockTimer.R {
	@EJB
	protected SystemManager.L systemManager;
	@EJB private ProgramParticipantEAO.L programParticipantEAO;
	@EJB private EventEAO.L eventEAO;
	@EJB private UsageDataManager.L usageDataManager;
	@EJB private DataManager.L dataManager;
	@EJB private UsageDataManager.L usageManager;
	@EJB private GeInterfaceManager.L geManager;
	
	private static final String TIMER_NAME="CLOCK_TIMER";
	private static final Logger log = Logger.getLogger(ClockTimerBean.class);
	@Resource
	protected SessionContext context;
	
	private GeInterfaceCache cache = GeInterfaceCache.getInstance();
	
	@Override
	public String getTimersInfo() {
		Collection timersList = context.getTimerService().getTimers();
		return super.getTimersInfo(timersList);
	}

	@Override
	public void createTimers() {
		try {
			cancelTimers();

			javax.ejb.TimerService timerService = context.getTimerService();
	
			timerService.createTimer(new Date(), 1
						* TimingUtil.MINUTE_MS, TIMER_NAME);

		} catch (Exception e) {
			String message = "Failed to create " +TIMER_NAME+": "+ e.getMessage();
			log.error(message);
		}
		
	}

	@Override
	public void cancelTimers() {
		 javax.ejb.TimerService timerService = context.getTimerService();
	     Collection timersList = timerService.getTimers();
	     super.cancelTimers(timersList);
		
	}

	@Override
	@Timeout
	public void timeoutHandler(Timer timer) {
		 if (timer.getInfo() != null 
	                && TIMER_NAME.equals(timer.getInfo())) {
	            Boolean serviceEnabled=systemManager.getPss2Properties().isGeInterfaceEnabled();
	
	            if (serviceEnabled!=null && serviceEnabled){
	            	 processTimeout();
	            }
	        }
	}

	private void processTimeout(){
		System.out.println("Clock timer running at "+new Date());
		GeConfiguration conf = geManager.getGeConfiguration();
		String programName = conf.getProgramName();
		List<ProgramParticipant> pps = programParticipantEAO.getProgramParticipantsByProgram(programName);
		if(pps==null||pps.isEmpty()){
			return;
		}
		
		List<Event> event = eventEAO.findByProgramName(programName);
		Event currentEvent = getNextEvent(event);
		
		Integer originInterval = cache.getRunningState();
		System.out.println(originInterval);
		
		Integer interval = 0;
		
		if(currentEvent!=null&&!currentEvent.getStartTime().after(new Date())){
			interval = conf.getShortInterval();
		}else{
			interval = conf.getLongInterval();
		}
		cache.putRunningInterval(Integer.valueOf(interval));
		if(interval!=originInterval){
			TimerManager geTimerManager = (TimerManager) EJBFactory.getBean(GeTimerInterfaceManager.class);
			System.out.println("Clock timer start new GE timer at "+new Date());
			geTimerManager.createTimers();
			
		}
		
	}
	
	private Event getNextEvent(List<Event> events){
		if(events==null||events.isEmpty()) return null;
		Collections.sort(events, new EventComparator());
		
		return events.get(0);
	}
	
	class EventComparator implements Comparator<Event> {
	    @Override
	    public int compare(Event a, Event b) {
	        return a.getStartTime().compareTo(b.getStartTime());
	    }
	}

}
