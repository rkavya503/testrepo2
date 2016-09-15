package com.akuacom.pss2.rtp.ftp;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.log4j.Logger;

import com.akuacom.pss2.email.MessageEAO;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.utils.ftp.FTPClient;
import com.akuacom.utils.ftp.SSHFTPClientImpl;
import com.akuacom.utils.ftp.exception.AuthentificationException;
import com.akuacom.utils.ftp.exception.ConnectionNotOpenException;

@Stateless
public class RTPTemperatureUpdateTimerBean extends TimerManagerBean implements RTPTemperatureUpdateTimer.L, RTPTemperatureUpdateTimer.R {

	private static final Logger log = Logger.getLogger(RTPTemperatureUpdateTimerBean.class);
	private static final String RTP_TEMPERATURE_UPDATE_TIMER = "RTP Temperature file FTP pickup and DRAS update Timer"; // Name of the timer
	private static final int A_MINUTE = 60 * 1000; // 1 minute

	@EJB
	RTPFTPConfigurationManager.L rtpFTPConfigurationManager;
	
	@EJB 
	MessageEAO.L messageEAO;

	@Resource
	private TimerService timerService;

	public void scheduleTimer() {
		
		cancelCurrentTimer();//cancel timer
		
		int interval = Integer.parseInt(rtpFTPConfigurationManager.getRTPFTPConfiguration().getInterval());
		
		timerService.createTimer(new Date(), interval * A_MINUTE, RTP_TEMPERATURE_UPDATE_TIMER);
		log.info("--- " + RTP_TEMPERATURE_UPDATE_TIMER + " is start.---");
	}

	@Timeout
	public void timeoutHandler(Timer timer) {
		rtpFTPConfigurationManager.updateTemperature();

	}
	
	private void cancelCurrentTimer() {
		// Stop running timer before create new timer
		Collection timersList = timerService.getTimers();
		for (Object timer : timersList) {
			if (timer instanceof Timer) {
				if(((Timer) timer).getInfo().equals(RTP_TEMPERATURE_UPDATE_TIMER)){
					((Timer) timer).cancel();					
				}
			}
		}
	}

	@Override
	public String getTimersInfo() {
		Collection timersList = timerService.getTimers();
		return super.getTimersInfo(timersList);
	}

	@Override
	public void createTimers() {
		this.scheduleTimer();
		
	}

	@Override
	public void cancelTimers() {
		this.cancelCurrentTimer();
		
	}

}
