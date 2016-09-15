package com.akuacom.pss2.nssettings;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.log4j.Logger;

import com.akuacom.pss2.email.MessageEAO;
import com.akuacom.pss2.timer.TimerManagerBean;

/**
 * 
 * Clean message at specific time everyday according to DRMS-1456.
 * 
 * @author Li Fei Initial date 2010.08.31
 * 
 */

@Stateless
public class CleanMessageTimerServiceBean extends TimerManagerBean implements
		CleanMessageTimerService.R, CleanMessageTimerService.L {

	private static final Logger log = Logger
			.getLogger(CleanMessageTimerServiceBean.class);

	private static final String CLEAN_MESSAGE_TIMER = "Clean Message Timer";

	private static final int TIMER_DAILY_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24
																					// hours
	private static int HISTORY_MESSAGE_DAYS = 1;
	private static int CLEAN_MESSAGE_HOUR = 0;
	private static int CLEAN_MESSAGE_MINUTE = 0;

	@EJB
	MessageEAO.L messageEAO;

	@EJB
	NSSettingsManager.L nsSettingManager;

	@Resource
	private TimerService timerService;

	/**
	 * Create clean message timer The timer starts with DRAS system burn up and
	 * starts to do it's job at specific time.
	 */
	public void scheduleTimer() {

		// Stop running timer before create new timer
		cancelTimers();

		NSSettings nsSettingsEntity = nsSettingManager.getNSSettings();

		HISTORY_MESSAGE_DAYS = nsSettingsEntity.getDuration();
		CLEAN_MESSAGE_HOUR = nsSettingsEntity.getCleanMsgHour();
		CLEAN_MESSAGE_MINUTE = nsSettingsEntity.getCleanMsgMinute();

		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, CLEAN_MESSAGE_HOUR);
		calendar.set(Calendar.MINUTE, CLEAN_MESSAGE_MINUTE);
		timerService.createTimer(calendar.getTime(),
				TIMER_DAILY_REFRESH_INTERVAL_MS, CLEAN_MESSAGE_TIMER);

		log.debug(CLEAN_MESSAGE_TIMER + " [ " + calendar.getTime()
				+ " ] is start.");
	}

	/** Clean message every 24 hours */
	@Timeout
	public void timeoutHandler(Timer timer) {

		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1
				- HISTORY_MESSAGE_DAYS);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);

		messageEAO.clearMessage(calendar.getTime());

		// Calendar calendar = new GregorianCalendar();
		log.debug("Message table is clear. [ " + calendar.getTime() + " ]");
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
}
