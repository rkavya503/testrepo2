package com.akuacom.pss2.singleton;

public interface HATimerSingletonMBean {
	public boolean isHASingletonMasterNode();

	public boolean isAkuaMasterNode();

	public boolean createTimers();

	public boolean cancelTimers();

	public String getTimers();

	public boolean resetAkuaMasterStatus();

	void create() throws Exception;

	void start() throws Exception;

	void stop();

	void destroy();
}
