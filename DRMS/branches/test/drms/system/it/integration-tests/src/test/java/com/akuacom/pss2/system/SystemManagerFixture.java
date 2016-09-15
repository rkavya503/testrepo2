package com.akuacom.pss2.system;

import org.junit.Before;
import org.junit.BeforeClass;

import com.akuacom.ejb.jboss.test.JBossFixture;

/**
 * Based class used for testing specific portions of the SystemManager.
 * 
 * @author roller
 * 
 */
public class SystemManagerFixture extends JBossFixture {

	protected static SystemManager systemManager;

	@BeforeClass
	public  static void setUpSystemManager() {
		if (systemManager == null) {
			systemManager = lookupSessionRemote(SystemManager.class);
		}
	}
}
