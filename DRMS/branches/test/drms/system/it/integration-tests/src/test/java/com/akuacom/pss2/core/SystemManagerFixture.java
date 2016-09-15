package com.akuacom.pss2.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.system.SystemManager;

public class SystemManagerFixture extends JBossFixture {
    /** The sm. */
    protected SystemManager sm;

  
    @Before
    public void setUp() throws Exception {
       
       
        sm = lookupSessionRemote(SystemManager.class);
        assertNotNull("Did not get ProgramManager in setup", sm);
    }
}
