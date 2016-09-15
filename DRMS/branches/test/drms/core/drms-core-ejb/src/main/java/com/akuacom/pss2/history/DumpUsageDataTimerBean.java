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

import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class DumpUsageDataTimerBean extends TimerManagerBean implements
							DumpUsageDataTimer.R, DumpUsageDataTimer.L {
	
	private static final Logger log = Logger.getLogger(DumpUsageDataTimerBean.class);

	private static final int TIMER_DAILY_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24
	@Resource
	private TimerService timerService;
	@EJB
	HistoryDataManager.L historyManager;
	
	private static final String TIMER= DumpUsageDataTimer.class.getName();
	
	private Date getStartDate(){
		return DateUtil.getEndOfDay(new Date());
	}
	
	
	/** Clean message every 24 hours */
	@Timeout
	public void timeoutHandler(Timer timer) {
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription(TIMER + " [ " + new Date()+ " ] is invoked.");
		log.debug(logEntry);
		
		historyManager.moveTodayUsageToHistory();
	}
	
	@Override
	public void createTimers() {
		cancelTimers();
		timerService.createTimer(getStartDate(), 
					TIMER_DAILY_REFRESH_INTERVAL_MS
					,TIMER);
		log.debug(TIMER +" created successfully");
		
		//ensure it is executed during server startup if server is down at mid-night
		historyManager.moveTodayUsageToHistory();
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

}
