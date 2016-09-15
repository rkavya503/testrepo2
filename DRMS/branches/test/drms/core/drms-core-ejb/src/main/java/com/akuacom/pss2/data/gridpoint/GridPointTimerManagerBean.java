/**
 * 
 */
package com.akuacom.pss2.data.gridpoint;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import org.apache.log4j.Logger;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.utils.lang.TimingUtil;

/**
 * the class GridPointTimerManagerBean
 */
@Stateless
public class GridPointTimerManagerBean extends TimerManagerBean implements
		GridPointTimerManager.L, GridPointTimerManager.R {
	
    private static final Logger log = Logger.getLogger(GridPointTimerManagerBean.class);
    private static final String GRID_POINT_TIMER_NAME="GRID_POINT_TIMER";

	@Resource
	protected SessionContext context;
	
	@EJB
	GridPointConfigurationGenEAO.L configEAO;
    @EJB
    protected SystemManager.L systemManager;
    @EJB
    GridPointManager.L gridPointManager;

	public void processTimeout(){
			gridPointManager.process();
	}
    
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.timer.TimerManager#createTimers()
	 */
	@Override
	public void createTimers() {
		try {
			cancelTimers();

			javax.ejb.TimerService timerService = context.getTimerService();
			GridPointConfiguration config = getGridPointConfiguration();
			if (config != null && config.getTimeInterval()!=null
					&& config.getTimeInterval()!=0) {
				int interval=config.getTimeInterval();
				
				timerService.createTimer(new Date(), interval
						* TimingUtil.MINUTE_MS, GRID_POINT_TIMER_NAME);
			}
		} catch (Exception e) {
			String message = "Failed to create " +GRID_POINT_TIMER_NAME+": "+ e.getMessage();
			log.error(message);
		}

	}
	
	protected GridPointConfiguration getGridPointConfiguration(){
		List<GridPointConfiguration> configList = configEAO.getAll();
		if (configList!=null && configList.size()>0)
			return configList.get(0);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.timer.TimerManager#cancelTimers()
	 */
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
        if (timer.getInfo() != null 
                && GRID_POINT_TIMER_NAME.equals(timer.getInfo())) {
        	
            Boolean serviceEnabled=systemManager.getPss2Features().isEnableDataService();

            if (serviceEnabled!=null && serviceEnabled) 
            	processTimeout();
        }

	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.timer.TimerManager#getTimersInfo()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getTimersInfo() {
		Collection timersList = context.getTimerService().getTimers();
		return super.getTimersInfo(timersList);
	}

}
