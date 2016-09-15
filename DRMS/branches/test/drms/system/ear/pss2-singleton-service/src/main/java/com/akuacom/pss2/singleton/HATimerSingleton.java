package com.akuacom.pss2.singleton;

import java.util.List;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.Depends;
import org.jboss.ejb3.annotation.Management;
import org.jboss.ejb3.annotation.Service;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.ha.MasterStatus;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;

/**
 * Jboss dependent implementation for a singleton to control timers in pss2
 * application. HATimerSingleton is dependent on Barrier and is not deployed to
 * development by default. Without this singleton (in development) the timers
 * are functional due to local MasterStatus pojo. HATimerSingleton depends on
 * barrier, and when barrier is deployed, so shall this singleton. In a cluster,
 * this singleton is required to be ready in order to handle failover events.
 * When a failover occurs, barrier is deployed, and this Singleton is deployed
 * and becomes master; the start method will call the createTimers on
 * SystemManager. Helper methods also available to see timers, create timers,
 * reset status, etc.
 * 
 * @author Sebastian.Johnck@aawhere.com
 * 
 */
//@Service(objectName = "jboss.ha:service=AkuaSingletonsController")
@Management(HATimerSingletonMBean.class)
//@Depends({ "jboss.ha:service=HASingletonDeployer,type=Barrier" })
public class HATimerSingleton implements HATimerSingletonMBean {

	private static final String JMX_REMOTE_PROPERTY = "com.sun.management.jmxremote";
	private static final String JBOSS_DOMAIN_NAME = "jboss";
	private static final String MBEAN_NAME = "jboss.ha:service=HASingletonDeployer";
	private static final String METHOD = "isMasterNode";
	// For this singleton go local to assure no other node besides local node
	// satisfies this request.
	private static final String SYSTEMMANAGER = EJB3Factory.EARNAME + "/"
			+ SystemManagerBean.class.getSimpleName()
			+ EJB3Factory.LOCAL_SUFFIX;

	private static MBeanServer mBeanServerInstance;

	private Logger logger = Logger.getLogger(this.getClass());

	public boolean isHASingletonMasterNode() {

		try {
			ObjectName oname = new ObjectName(MBEAN_NAME);
			return ((Boolean) getJbossMBeanServer().invoke(oname, METHOD,
					new Object[] {}, new String[] {})).booleanValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isAkuaMasterNode() {
		return MasterStatus.getInstance().getStatus();
	}

	/**
	 * HttpSingletonController declared approach to start singleton (not
	 * barrier). Resets status, and creates timers.
	 */
	public void startSingleton() {

		resetAkuaMasterStatus();
		logger.info("Master Singleton STARTING");
		createTimers();

	}

	/**
	 * Resets status and stops timers
	 */
	public void stopSingleton() {
		resetAkuaMasterStatus();
		cancelTimers();
		logger.info("Master Singleton STOPPED");
	}

	/**
	 * Get the Jboss MBean server
	 */
	private synchronized static MBeanServer getJbossMBeanServer() {
		if (mBeanServerInstance == null) {
			List<MBeanServer> mBeanServers = MBeanServerFactory
					.findMBeanServer(null);
			// if (System.getProperty(JMX_REMOTE_PROPERTY) != null) {
			// // - the mBean server we want is the first instance
			// // (and possibly the only instance) in the list if jboss was
			// // started w/ the property "-Dcom.sun.management.jmxremote"
			// mBeanServerInstance = mBeanServers.get(0);
			// } else {
			for (MBeanServer server : mBeanServers) {
				final String defaultDomain = server.getDefaultDomain();
				if (defaultDomain != null
						&& defaultDomain.equals(JBOSS_DOMAIN_NAME)) {
					mBeanServerInstance = server;
					break;
				}
			}
			// }

			if (mBeanServerInstance == null) {
				if (System.getProperty(JMX_REMOTE_PROPERTY) != null
						&& mBeanServers.size() > 0) {
					// the mBean server we want is the first
					// instance
					// (and possibly the only instance) in the list if jboss was
					// started w/ the property "-Dcom.sun.management.jmxremote"
					mBeanServerInstance = mBeanServers.get(0);
				} else {
					throw new IllegalStateException(
							"Cannot find Jboss MBeanServer.");
				}
			}
		}
		return mBeanServerInstance;
	}

	/**
	 * Tries to invoke createTimers on SystemManager. Warning on failure.
	 */
	public boolean createTimers() {
		// try to create timers via system manger (timers won't create on slave
		// nodes)
		try {

			SystemManager systemManager1 = getSystemManager();
			if (systemManager1 != null) {
				systemManager1.createTimers();
				logger.info("SystemManager lookup success");
				return true;
			} else {
				logger.warn("Tried to invoke create timers with systemManager not deployed, no problem if starting or shutting down.");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	/**
	 * Tries to invoke cancelTimers on systemManager. Warning on failure.
	 */
	public boolean cancelTimers() {
		// cancel timers via system manager
		SystemManager systemManager = getSystemManager();
		if (systemManager != null) {
			systemManager.cancelTimers();
			return true;
		} else {
			logger.warn("Tried to invoke cancel timers with systemManager not deployed, no problem if starting or shutting down.");
			return false;
		}
	}

	/**
	 * Helper to get info on all timers that are aware to the SystemManager
	 */
	public String getTimers() {
		SystemManager systemManager = getSystemManager();
		if (systemManager != null) {
			return systemManager.getTimersInfo();
		} else {
			String message = "Tried to invoke create timers with systemManager not deployed, no problem if starting or shutting down.";
			logger.error(message);
			return message;

		}

	}

	/**
	 * Resets the AkuaMaster status to the status provided by
	 * HADeploymentSingleton
	 */
	public boolean resetAkuaMasterStatus() {
		try {
			ObjectName oname = new ObjectName(MBEAN_NAME);
			// Sets local MasterStatus pojo to the same status as the
			// HADeploymentSingleton
			MasterStatus.getInstance().setStatus(
					((Boolean) getJbossMBeanServer().invoke(oname, METHOD,
							new Object[] {}, new String[] {})).booleanValue());
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private SystemManager getSystemManager() {
		SystemManager systemManager = null;
		try {
			systemManager = (SystemManager) new InitialContext()
					.lookup(SYSTEMMANAGER);
		} catch (Exception e) {
			logger.warn("Tried to invoke systemManager not deployed, no problem if starting or shutting down."
					+ e.getMessage());
			// e.printStackTrace();
		}
		return systemManager;
	}

	public void create() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("Creating me");
	}

	public void start() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("Starting");
		startSingleton();
	}

	public void stop() {
		// TODO Auto-generated method stub
		logger.debug("Stopping me");
		stopSingleton();
	}

	public void destroy() {
		// TODO Auto-generated method stub
		logger.debug("Destroying me");
	}

}
