//package com.akuacom.pss2.history;
//
//import java.util.Collection;
//import java.util.Date;
//
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.ejb.Stateless;
//import javax.ejb.Timeout;
//import javax.ejb.Timer;
//import javax.ejb.TimerService;
//
//import org.apache.log4j.Logger;
//
//import com.akuacom.pss2.system.SystemManager;
//import com.akuacom.pss2.timer.TimerManagerBean;
//import com.akuacom.pss2.util.LogUtils;
//import com.kanaeki.firelog.util.FireLogEntry;
//@Stateless
//public class InterpolateMissingDataTimerServiceBean extends TimerManagerBean implements
//InterpolateMissingDataTimerService.R, InterpolateMissingDataTimerService.L {
//
//	private static final Logger log = Logger
//	.getLogger(InterpolateMissingDataTimerServiceBean.class);
//
//	private static final String INTERPOLATE_TIMER = "Interpolate Missing data Timer";
//	
//	@Resource
//	private TimerService timerService;
//	@EJB
//	HistoryDataManager.L historyManager;
//	@EJB
//	SystemManager.L sysManager;
//
//	@Override
//	public void scheduleTimer() {
//		// Stop running timer before create new timer
//		cancelTimers();
//		Date timerStart = new Date();
//		
//		timerService.createTimer(timerStart,
//				sysManager.getPss2Properties().getInterpolateInterval(), INTERPOLATE_TIMER);
//
//		FireLogEntry logEntry = LogUtils.createLogEntry();
//		logEntry.setDescription(INTERPOLATE_TIMER + " [ " + timerStart+ " ] is invoked.");
//		log.debug(logEntry);
//		
//	}
//
//	/** Clean message every 24 hours */
//	@Timeout
//	public void timeoutHandler(Timer timer) {
//		FireLogEntry logEntry = LogUtils.createLogEntry();
//		logEntry.setDescription(INTERPOLATE_TIMER + " [ " + new Date()+ " ] is invoked.");
//		log.debug(logEntry);
//		
//		historyManager.interpolationTimerServiceHandler();
//		
//	}
//
//	@Override
//	public void createTimers() {
//		scheduleTimer();
//
//	}
//
//	@Override
//	public String getTimersInfo() {
//		Collection timersList = timerService.getTimers();
//		return super.getTimersInfo(timersList);
//	}
//	
//	@Override
//	public void cancelTimers() {
//	        Collection timersList = timerService.getTimers();
//	        super.cancelTimers(timersList);
//	}
//
//}
