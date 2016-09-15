package com.akuacom.pss2.task;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;

@Stateless
public class TimerConfigManagerBean implements TimerConfigManager.R, TimerConfigManager.L {
	private static final Logger log = Logger.getLogger(TimerConfigManagerBean.class);	
	@EJB
	TimerConfigEAO.L timerConfigEAO;

	@Override
	public List<TimerConfig> getTimerConfigList() {
		return timerConfigEAO.getAll();
	}


	@Override
	public void updateTimerConfig(TimerConfig config){
		String uuid = config.getUUID();
		if(uuid!=null&&(!uuid.equalsIgnoreCase(""))){
			try {
				timerConfigEAO.update(config);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void updateTimerConfig(String uuid,int hour,int min){
		if(uuid!=null&&(!uuid.equalsIgnoreCase(""))){
			try {
				TimerConfig config = timerConfigEAO.getById(uuid);
				config.setInvokeHour(hour);
				config.setInvokeMin(hour);
				timerConfigEAO.update(config);
			}catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}
		}
		
	}
	@Override
	public TimerConfig getTimerConfig(String name){
		return timerConfigEAO.getTimerConfig(name);
	}
}