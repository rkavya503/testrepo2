package com.akuacom.pss2.core;

import org.junit.Before;
import static org.junit.Assert.assertNotNull;
import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.event.EventManager;

public class ProgramManagerFixture extends JBossFixture {
    /** The pm. */
    protected EventManager _em;

  
    @Before
    public void setUp() throws Exception {
       
        _em = lookupSessionRemote(EventManager.class);
        assertNotNull("Did not get ProgramManager in setup", _em);
    }
}
