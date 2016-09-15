package com.akuacom.pss2.history;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.task.RoutineStatus;
import com.akuacom.pss2.task.RoutineStatusEAO;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class DumpReportDataTimerServiceBean extends TimerManagerBean implements
DumpReportDataTimerService.R, DumpReportDataTimerService.L {
	
	private static final Logger log = Logger
	.getLogger(DumpReportDataTimerServiceBean.class);

	private static final String DUMP_REPORTDATA_TIMER = "Dump ReportData Timer";
	
	private static final int TIMER_DAILY_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24
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
		Date nextDay = getInvokeTime();
		timerService.createTimer(nextDay,
				TIMER_DAILY_REFRESH_INTERVAL_MS, DUMP_REPORTDATA_TIMER);
		
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription(DUMP_REPORTDATA_TIMER + " [ " + nextDay+ " ] is invoked.");
		log.debug(logEntry);
		
		// if the timer is executed successfully today, execute during server start-up
		RoutineStatus status = routineStatusEAO.getRoutineStatus(DUMP_REPORTDATA_TIMER, new Date());
		if(status.getStatus()==false){
		     asynchCaller.call(new EJBAsynchRunable(
		    		 DumpReportDataTimerService.class, 
     				 "invokeTimer",
     				 new Class[]{},
     				 new Object[]{}));
		}
	}
	
	
	private Date getInvokeTime() {
		Date nextDay = DateUtil.add(new Date(), Calendar.DATE, 1);
		String invokeTime = sysManager.getPss2Properties().getBaselineCalculateTime();
		DateFormat dt = new SimpleDateFormat("hh:mm:ss");
		Calendar cal2 = Calendar.getInstance();
		try {
			cal2.setTime(dt.parse(invokeTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextDay);
		
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal2.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.MINUTE), cal2.get(Calendar.SECOND));
		nextDay = cal.getTime();
		return nextDay;
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
		logEntry.setDescription(DUMP_REPORTDATA_TIMER + " [ " + new Date()+ " ] is invoked.");
		log.debug(logEntry);
		
		historyManager.timerServiceHandler();
		
		RoutineStatus status = routineStatusEAO.getRoutineStatus(DUMP_REPORTDATA_TIMER, new Date());
		status.setStatus(true);
		routineStatusEAO.insertOrUpdate(status);
	}

}
