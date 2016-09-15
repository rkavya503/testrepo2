package com.akuacom.pss2.ge;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.program.participant.ProgramParticipantEAO;

@Stateless
public class GeInterfaceManagerBean implements GeInterfaceManager.L, GeInterfaceManager.R {
	@EJB private ProgramParticipantEAO.L programParticipantEAO;
	@EJB private EventEAO.L eventEAO;
	@EJB private UsageDataManager.L usageDataManager;
	@EJB private DataManager.L dataManager;
	@EJB private UsageDataManager.L usageManager;
	@EJB private GeConfigurationEAO.L configurationEAO;
	
	@Override
	public GeConfiguration getGeConfiguration() {
		// TODO Auto-generated method stub
		return configurationEAO.findByConfigName("ge_conf");
	}

	@Override
	public int updateFTPConfig(String programName, String url,
			int shortInterval, int longInterval) {
		return configurationEAO.updateFTPConfig(programName, url, shortInterval, longInterval);
	}

	@Override
	public void saveGeConfiguration(GeConfiguration conf) {
		updateFTPConfig(conf.getProgramName(),conf.getUrl(),conf.getShortInterval(),conf.getLongInterval());
		
	}

}
