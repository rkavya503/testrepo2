/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * the class PartticipantUploadTimerBean
 *
 */
@Stateless
public class PartticipantUploadTimerBean extends SCEFTPTimerManagerBean
		implements PartticipantUploadTimer.L, PartticipantUploadTimer.R {

    public static final String SCEPART_FTP_CHECK_TIMER = "SCE_Participant_Upload_Timer";
    public static final String CONFIG_NAME="SCEPART";

    @EJB
    SCEFTPManager.L manager;
    
	@Override
	public void processTimeout() {
    	SCEFTPConfig config=getDispatchConfig();
    	int inRange=checkTimeRange(config);
    	if (inRange>=0) {
    		boolean lastScan=inRange==1?true:false;
    		int success=manager.process(config, lastScan);
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
    
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.program.sceftp.SCEFTPTimerManagerBean#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return systemManager.getPss2Features()
                .isParticipantsUpload();
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.program.sceftp.SCEFTPTimerManagerBean#getTimerName()
	 */
	@Override
	public String getTimerName() {
		return SCEPART_FTP_CHECK_TIMER;
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.program.sceftp.SCEFTPTimerManagerBean#getConfigName()
	 */
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

}
