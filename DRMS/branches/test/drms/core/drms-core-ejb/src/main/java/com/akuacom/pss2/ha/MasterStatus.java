package com.akuacom.pss2.ha;

import org.apache.log4j.Logger;

/**
 * Singleton for easy access to cluster status. Clients can use this status
 * instead of referring to container specific JMX code to get status. This
 * status can be set by command line, container HA tools or similar.
 * 
 * @author Sebastian Johnck sebastian.johnck@aawhere.com
 * 
 */
public class MasterStatus {

	private static final MasterStatus instance = new MasterStatus();
		
	private static final String OVERRIDE="MasterStatus.isMaster";

	private boolean status;

	public boolean getStatus() {
		Logger.getLogger(this.getClass()).debug(
				"Request for master status returned: " + status);
		return status;
	}

	public void setStatus(boolean b) {
		Logger.getLogger(this.getClass()).debug(
				"Master Status is changing to: " + b);
		this.status = b;
	}

	private MasterStatus() {
		// Logic to determine default status: if development environment, and no
		// override exists, this JVM is the master. In clustered environment
		// default will be overridden by container.
		String providedStatus = System.getProperty(OVERRIDE);
		if (providedStatus != null) {
			// status is provided
			status = Boolean.valueOf(providedStatus);
		} else if (classExists("com.akuacom.pss2.singleton.HATimerSingletonMBean")) {
		    //compiled with clustering support. return false and let the system override.
		    status = false;
		} else {
		    //not complied with clustering support. We are the master, return true
			status = true;
		}
	}

	public static synchronized MasterStatus getInstance() {
		return instance;
	}
	
	private boolean classExists(String className) {
	    try {
	        Class.forName(className);
	        return true;
	    }
	    catch(ClassNotFoundException exception) {
	        return false;
	    }
	}
}
