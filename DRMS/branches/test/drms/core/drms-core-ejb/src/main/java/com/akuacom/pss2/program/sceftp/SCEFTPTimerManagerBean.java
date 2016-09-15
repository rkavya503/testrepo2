/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.utils.lang.TimingUtil;

/**
 * the class SCEFTPTimerManagerBean
 * 
 */
public abstract class SCEFTPTimerManagerBean extends TimerManagerBean implements SCEFTPTimerManager.L, SCEFTPTimerManager.R {

    private static final Logger log = Logger.getLogger(SCEFTPTimerManagerBean.class);
    
	public static final String UTILITY_NAME_SCE = "sce";

	@Resource
	protected SessionContext context;
	@EJB
	protected SCEFTPConfigGenEAO.L dispatchConfigEAO;
    @EJB
    protected SystemManager.L systemManager;

	protected SCEFTPConfig getDispatchConfig(){
		return dispatchConfigEAO.findByConfigName(getConfigName());
	}
    
    protected int checkTimeRange(SCEFTPConfig config){
		int inRange=-1;
		if (config!=null) {
	    	Date now=new Date();
			
	    	try {
	    		Date start=config.getTodayStartTime();
	    		Date end=config.getTodayEndTime();
	    		Integer interval=config.getScanInterval();
				if (start.getTime() > now.getTime() || end.getTime() < now.getTime()) {
					inRange = -1;
				} else {
					if ((now.getTime()+interval.intValue()*DateUtils.MILLIS_PER_MINUTE)>=end.getTime())
						inRange = 1;
					else 
						inRange=0;
				}
			} catch (ParseException e) {
				log.error("SCE ftp configuration error: failed to parse scan start time or end time. Config name: " + config.getConfigName());
			} catch (NullPointerException e){
				log.error("SCE ftp configuration error: " + e.getMessage() + ", Config name: "+ config.getConfigName());
			}
		}
    	
		return inRange;
    }

	@Override
	public void createTimers() {
		String timerName=getTimerName();
		try {
			cancelTimers();

			javax.ejb.TimerService timerService = context.getTimerService();
			SCEFTPConfig config = getDispatchConfig();
			if (config != null && config.getAvailable()) {
				Integer interval = config.getScanInterval();
				if (interval == null || interval.intValue() == 0)
					return;
				
				timerService.createTimer(new Date(), interval
						* TimingUtil.MINUTE_MS, timerName);
			}
		} catch (Exception e) {
			String message = "Failed to create " +timerName+": "+ e.getMessage();
			log.error(message);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getTimersInfo() {
		Collection timersList = context.getTimerService().getTimers();
		return super.getTimersInfo(timersList);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void cancelTimers() {
		 javax.ejb.TimerService timerService = context.getTimerService();
	        Collection timersList = timerService.getTimers();
	        super.cancelTimers(timersList);
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.timer.TimerManager#timeoutHandler(javax.ejb.Timer)
	 */
	@Override
	@Timeout
	public void timeoutHandler(Timer timer) {
		String timerName=getTimerName();

        if (timer.getInfo() != null 
                && timerName.equals(timer.getInfo())) {
        	
        	EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        	String utilityName=cache.getUtilityName("utilityName");
            if (isEnabled() && utilityName.equals(UTILITY_NAME_SCE))
            	processTimeout();
        }
	}
	
	public abstract boolean isEnabled(); 
	
	public abstract void processTimeout();
		
	public abstract String getTimerName();

	public abstract String getConfigName();
}
