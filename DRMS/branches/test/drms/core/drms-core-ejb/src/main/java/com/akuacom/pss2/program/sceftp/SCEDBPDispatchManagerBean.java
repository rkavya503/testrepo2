/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramEAO;
import com.akuacom.pss2.program.dbp.DBPNoBidProgramEJB;
import com.akuacom.utils.lang.TimingUtil;

/**
 *
 */
@Stateless
public class SCEDBPDispatchManagerBean extends SCEFTPTimerManagerBean implements SCEDBPDispatchManager.L, SCEDBPDispatchManager.R  {
	
    private static final Logger log = Logger.getLogger(SCEDBPDispatchManagerBean.class);

    public static final String SCEDBP_FTP_CHECK_TIMER = "SCE_DBPEvent_AutoDispatcher_Timer";
    public static final String CONFIG_NAME="SCEDBP";
    
    @EJB
	ProgramEAO.L programEAO;

	@EJB
	DBPNoBidProgramEJB.L programEJB;

	@Override
    public void processTimeout(){
    	SCEFTPConfig config=getDispatchConfig();
    	int inRange=checkTimeRange(config);
    	if (inRange>=0) {
    		boolean lastScan=inRange==1?true:false;
    		int success=programEJB.process(config, lastScan);
    		if (success==0 && !lastScan) { // success & not last time scan -> set upload = ture
				config.setUpload(true);
				dispatchConfigEAO.merge(config);
    		}
    		if (success==2) { // sent notification
				config.setConnErrorNotified(true);
				dispatchConfigEAO.merge(config);
    		}
    		if (lastScan) { // init upload & notification status for next day
				config.setUpload(false);
				config.setConnErrorNotified(false);
				dispatchConfigEAO.merge(config);
    		}
    	}
    }
    
	@Override
	public void createTimers() {
		try {
			cancelTimers();

			boolean isCreate = false;
			List<Program> programs = programEAO.getAll();
			for (Program program : programs) {
				if (program.getClassName().equals(DBPNoBidProgramEJB.class.getName())) {
					isCreate = true;
					break;
				}
			}

			if (isCreate) {
				javax.ejb.TimerService timerService = context.getTimerService();
				SCEFTPConfig config = getDispatchConfig();
				if (config != null && config.getAvailable()) {
					Integer interval = config.getScanInterval();
					if (interval == null || interval.intValue() == 0)
						return;
					
					timerService.createTimer(new Date(), interval
							* TimingUtil.MINUTE_MS, SCEDBP_FTP_CHECK_TIMER);
				}
			}
		} catch (Exception e) {
			String message = "Failed to create SCEDBP_FTP_CHECK_TIMER timer: " + e.getMessage();
			log.error(message);
		}
	}

	@Override
	public String getTimerName() {
		return SCEDBP_FTP_CHECK_TIMER;
	}

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public boolean isEnabled() {
		return systemManager.getPss2Features()
                .isSCEDBPEventAutoCreationEnabled();
	}
}
