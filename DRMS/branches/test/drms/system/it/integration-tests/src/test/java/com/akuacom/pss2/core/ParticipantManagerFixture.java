package com.akuacom.pss2.core;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;

import java.util.List;

public class ParticipantManagerFixture extends JBossFixture {
    /** The pm. */
    protected static EventManager em;
    public static ParticipantManager pm;
    protected static ClientManager cm;
    public static ProgramManager programManager;
    public static ProgramParticipantManager programParticipantManager;
    protected static DataManager dm;

    @BeforeClass
    public static void setUpManagers() throws Exception {
        em = lookupSessionRemote(EventManager.class);
        assertNotNull("Did not get EventManager in setup", em);
        pm = lookupSessionRemote(ParticipantManager.class);
        assertNotNull("Did not get ParticipantManager in setup", pm);
        cm = lookupSessionRemote(ClientManager.class);
        assertNotNull("Did not get ClientManager in setup", cm);
        programManager = lookupSessionRemote(ProgramManager.class);
        assertNotNull("Did not get ProgramManager in setup", programManager);
        programParticipantManager = lookupSessionRemote(ProgramParticipantManager.class);
        assertNotNull("Did not get ProgramParticipantManager in setup", programParticipantManager);
        dm = lookupSessionRemote(DataManager.class);
        assertNotNull("Did not get DataManager in setup", dm);
    }

    // CPP as a program name is no longer guaranteed, added this to ensure tests uses correct program name.
    public static String getFirstCPPProgramName() {
        List<Program> programs = programManager.getProgramsAsPrograms();
        String name = null;
        for (Program p : programs) {
            if ("com.akuacom.pss2.program.cpp.CPPProgramEJB".equals(p.getClassName())) {
                if (p.isMustIssueBDBE()) {
                    name = p.getProgramName();
                    break;
                }
            }
        }
        return name;
    }
}
