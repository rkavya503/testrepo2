package com.akuacom.pss2.history;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.log4j.Logger;

import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.task.RoutineStatusEAO;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class BaselineAdjustTimerServiceBean extends TimerManagerBean implements
BaselineAdjustTimerService.R, BaselineAdjustTimerService.L {
	
	private static final Logger log = Logger
	.getLogger(BaselineAdjustTimerServiceBean.class);

	private static final String BASELINE_ADJUST_TIMERSERVICEBEAN = "Baseline Adjust Timer";
	
	private static final int TIMER_REFRESH_INTERVAL_MS = 5 * 60 * 1000; // 5 minute interval 
	@Resource
	private TimerService timerService;
	@EJB
	HistoryDataManager.L historyManager;
	@EJB
	SystemManager.L sysManager;
	
	@EJB
	RoutineStatusEAO.L routineStatusEAO;
	
	@EJB
	AsynchCaller.L asynchCaller;

	@Override
	public void scheduleTimer() {
		// Stop running timer before create new timer
		cancelTimers();
		
		timerService.createTimer(new Date(),
				TIMER_REFRESH_INTERVAL_MS, BASELINE_ADJUST_TIMERSERVICEBEAN);
		
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription(BASELINE_ADJUST_TIMERSERVICEBEAN + " [ " + new Date()+ " ] is invoked.");
		log.debug(logEntry);
		
	}
	
	
	/** Clean message every 24 hours */
	@Timeout
	public void timeoutHandler(Timer timer) {
		invokeTimer();
	}
	
	@Override
	public void createTimers() {
		scheduleTimer();
	}

	@Override
	public String getTimersInfo() {
		Collection timersList = timerService.getTimers();
		return super.getTimersInfo(timersList);
	}
	
	@Override
	public void cancelTimers() {
	        Collection timersList = timerService.getTimers();
	        super.cancelTimers(timersList);
	}

	@Override
	public void invokeTimer() {
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription(BASELINE_ADJUST_TIMERSERVICEBEAN + " [ " + new Date()+ " ] is invoked.");
		log.debug(logEntry);
		
		historyManager.baselineTimerServiceHandler();
	}

}
