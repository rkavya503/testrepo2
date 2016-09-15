/**
 * 
 */
package com.akuacom.pss2.program.participant;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.akuacom.pss2.participant.ParticipantUtil.DEFAULT_PASSWORD;
import static org.junit.Assert.assertNotNull;

/**
 * Testing the CRUD methods on the {@link com.akuacom.pss2.system.property.CorePropertyEAO} session bean.
 *
 * @author roller
 *
 */
public class ProgramParticipantEAOTest extends JBossFixture {
    private static String CPP = null;

    protected static ParticipantManager pm;
    protected static ClientManager cm;
    protected static ProgramManager programManager;
    protected static ProgramParticipantManager programParticipantManager;
    protected static ProgramParticipantEAO eao;

    @BeforeClass
    public static void setUpManagers() throws Exception {
        pm = lookupSessionRemote(ParticipantManager.class);
        assertNotNull("Did not get ParticipantManager in setup", pm);
        cm = lookupSessionRemote(ClientManager.class);
        assertNotNull("Did not get ClientManager in setup", cm);
        programManager = lookupSessionRemote(ProgramManager.class);
        assertNotNull("Did not get ProgramManager in setup", programManager);
        programParticipantManager = lookupSessionRemote(ProgramParticipantManager.class);
        assertNotNull("Did not get ProgramParticipantManager in setup", programParticipantManager);
        eao = lookupSessionRemote(ProgramParticipantEAO.class);
        assertNotNull("Did not get ProgramParticipantEAO in setup", eao);

        CPP = getFirstCPPProgramName();
    }

    private static String getFirstCPPProgramName() {
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

    @Test
    public void testAggregationMethods() {
        String p1Name = getGeneratedParticipantName();
        ProgramParticipant pp1 = programParticipantManager.getProgramParticipant(CPP, p1Name, false);
        Assert.assertNotNull(pp1);

        String p2Name = getGeneratedParticipantName();
        ProgramParticipant pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);

        String p3Name = getGeneratedParticipantName();
        ProgramParticipant pp3 = programParticipantManager.getProgramParticipant(CPP, p3Name, false);
        Assert.assertNotNull(pp3);

        String p4Name = getGeneratedParticipantName();
        ProgramParticipant pp4 = programParticipantManager.getProgramParticipant(CPP, p4Name, false);
        Assert.assertNotNull(pp4);

        String p5Name = getGeneratedParticipantName();
        ProgramParticipant pp5 = programParticipantManager.getProgramParticipant(CPP, p5Name, false);
        Assert.assertNotNull(pp5);

        try {
            eao.addChild(pp1, pp2);
            pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
            eao.addChild(pp1, pp3);
            eao.addChild(pp2, pp4);

            Set<ProgramParticipant> descendants = eao.getFlatDescendants(pp1);
            Assert.assertEquals("test", descendants.size(), 3);

            descendants = eao.getDescendants(pp1);
            Assert.assertEquals("test", descendants.size(), 2);

            descendants = eao.getDescendants(pp2);
            Assert.assertEquals("test", descendants.size(), 1);

            eao.addChild(pp2, pp3);

            descendants = eao.getDescendants(pp1);
            Assert.assertEquals("test", descendants.size(), 1);

            descendants = eao.getDescendants(pp2);
            Assert.assertEquals("test", descendants.size(), 2);

            eao.addChild(pp5, pp2);
            pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);

            descendants = eao.getDescendants(pp1);
            Assert.assertEquals("test", descendants.size(), 0);

            descendants = eao.getDescendants(pp5);
            Assert.assertEquals("test", descendants.size(), 1);

            descendants = eao.getDescendants(pp2);
            Assert.assertEquals("test", descendants.size(), 2);

            eao.removeParent(pp2);

            descendants = eao.getDescendants(pp5);
            Assert.assertEquals("test", descendants.size(), 0);

            descendants = eao.getDescendants(pp2);
            Assert.assertEquals("test", descendants.size(), 2);
        } finally {
            pm.removeParticipant(p4Name);
            pm.removeParticipant(p3Name);
            pm.removeParticipant(p2Name);
            pm.removeParticipant(p1Name);
        }

    }

    private String getGeneratedParticipantName() {
        Participant p1 = generateRandomPersistedParticipant();
        String p1Name = p1.getParticipantName();
        p1 = pm.getParticipant(p1Name);
        Assert.assertNotNull(p1);
        programParticipantManager.addParticipantToProgram(CPP, p1Name, false);
        return p1Name;
    }

    public static Participant generateRandomPersistedParticipant() {
        Participant p = ParticipantUtil.getParicipantInstance("part-" + UUID.randomUUID().toString().substring(24));
        try {
            if(pm.getParticipant(p.getParticipantName()) == null){
            	pm.createParticipant(p, DEFAULT_PASSWORD.toCharArray(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        return p;
    }
    
}