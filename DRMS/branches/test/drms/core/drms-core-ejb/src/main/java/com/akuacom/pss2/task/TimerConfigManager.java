package com.akuacom.pss2.task;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface TimerConfigManager {
	
	@Remote
    public interface R extends TimerConfigManager {}
    @Local
    public interface L extends TimerConfigManager {}
//    
    List<TimerConfig> getTimerConfigList();
    void updateTimerConfig(TimerConfig config);
    void updateTimerConfig(String uuid,int hour,int min);
    TimerConfig getTimerConfig(String name);
}
