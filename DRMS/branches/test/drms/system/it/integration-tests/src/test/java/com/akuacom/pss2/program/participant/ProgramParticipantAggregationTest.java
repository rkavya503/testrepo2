package com.akuacom.pss2.program.participant;

import static com.akuacom.pss2.participant.ParticipantUtil.DEFAULT_PASSWORD;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.akuacom.pss2.program.Program;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.pss2.program.ProgramManager;

public class ProgramParticipantAggregationTest extends JBossFixture{
//    public static final int PARTICIPANT_USER_MAX_LENGTH = 99;

    protected static ParticipantManager pm;
    protected static ClientManager cm;
    protected static ProgramManager programManager;
    protected static ProgramParticipantManager programParticipantManager;
    protected static ProgramParticipantEAO eao;
    protected static ProgramParticipantAggregationManager aggManager;
    
    private static ProgramParticipant pp1;
    private static ProgramParticipant pp2;
    private static ProgramParticipant pp3;
    private static ProgramParticipant pp4;
    
    private static String p1Name;
    private static String p2Name;
    private static String p3Name;
    private static String p4Name;

    private static String CPP = null;

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
        aggManager = lookupSessionRemote(ProgramParticipantAggregationManager.class);
        assertNotNull("Did not get ProgramParticipantAggregationManager in setup", aggManager);
        
        
        // generate p1
        Participant p1 = generateRandomPersistedParticipant();
        p1Name = p1.getParticipantName();
        p1 = pm.getParticipant(p1Name);
        Assert.assertNotNull(p1);

        // generate p2
        Participant p2 = generateRandomPersistedParticipant();
        p2Name = p2.getParticipantName();
        p2 = pm.getParticipant(p2Name);
        Assert.assertNotNull(p2);

        // generate p3
        Participant p3 = generateRandomPersistedParticipant();
        p3Name = p3.getParticipantName();
        p3 = pm.getParticipant(p3Name);
        Assert.assertNotNull(p3);

        // generate p4
        Participant p4 = generateRandomPersistedParticipant();
        p4Name = p4.getParticipantName();
        p4 = pm.getParticipant(p4Name);
        Assert.assertNotNull(p4);

        CPP = getFirstCPPProgramName();

        programParticipantManager.addParticipantToProgram(CPP, p1Name, false);
        programParticipantManager.addParticipantToProgram(CPP, p2Name, false);
        programParticipantManager.addParticipantToProgram(CPP, p3Name, false);
        programParticipantManager.addParticipantToProgram(CPP, p4Name, false);

        pp1 = programParticipantManager.getProgramParticipant(CPP, p1Name, false);
        Assert.assertNotNull(pp1);
        pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
        pp3 = programParticipantManager.getProgramParticipant(CPP, p3Name, false);
        Assert.assertNotNull(pp3);
        pp4 = programParticipantManager.getProgramParticipant(CPP, p4Name, false);
        Assert.assertNotNull(pp4);
        
        
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

    @AfterClass
    public static void cleanUp() throws Exception {
        pm.removeParticipant(p4Name);
        pm.removeParticipant(p3Name);
        pm.removeParticipant(p2Name);
        pm.removeParticipant(p1Name);
    	
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

    
    @Test
    public void testAddChild() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	aggManager.addChild(pp1, pp2);
        pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
    	ProgramParticipant pp = aggManager.getAncestor(pp2);
    	Assert.assertNotNull(pp);
    	Assert.assertEquals(pp1.getName(), pp.getName());
    	
    }
    
    
    @Test
    public void testGetAncestor() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	aggManager.addChild(pp1, pp2);
        pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
    	ProgramParticipant pp = aggManager.getAncestor(pp2);
    	Assert.assertNotNull(pp);
    	Assert.assertEquals(pp1.getName(), pp.getName());
    }
    
    
    @Test
    public void testGetCommonAncestor() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	Assert.assertNotNull(pp3);
    	aggManager.addChild(pp1, pp2);
    	aggManager.addChild(pp1, pp3);
        pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
        pp3 = programParticipantManager.getProgramParticipant(CPP, p3Name, false);
        Assert.assertNotNull(pp3);
    	ProgramParticipant pp = aggManager.getCommonAncestor(pp2, pp3);
    	Assert.assertNotNull(pp);
    	Assert.assertEquals(pp1.getName(), pp.getName());
    	Assert.assertEquals(pp1.getParticipantName(), pp.getParticipantName());
    }
    
    @Test
    public void testGetDescendants() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	Assert.assertNotNull(pp3);
    	aggManager.addChild(pp1, pp2);
    	aggManager.addChild(pp1, pp3);
    	pp1 = programParticipantManager.getProgramParticipant(CPP, p1Name, false);
        Assert.assertNotNull(pp1);
    	pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
        pp3 = programParticipantManager.getProgramParticipant(CPP, p3Name, false);
        Assert.assertNotNull(pp3);
        Set<ProgramParticipant> ppList = aggManager.getDescendants(pp1);
    	Assert.assertNotNull(ppList);
    	Assert.assertTrue(ppList.size() == 2);
    	String name = ppList.iterator().next().getName();
    	String participantName = ppList.iterator().next().getParticipantName();
    	Assert.assertNotNull(name);
    	Assert.assertTrue(pp2.getName().equals(name) || pp3.getName().equals(name));
    	Assert.assertTrue(pp2.getParticipantName().equals(participantName) || pp3.getParticipantName().equals(participantName));
    }
    


    @Test
    public void testGetFlatAncestor() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	Assert.assertNotNull(pp3);
    	aggManager.addChild(pp1, pp2);
    	aggManager.addChild(pp2, pp3);
    	pp1 = programParticipantManager.getProgramParticipant(CPP, p1Name, false);
        Assert.assertNotNull(pp1);
    	pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
        pp3 = programParticipantManager.getProgramParticipant(CPP, p3Name, false);
        Assert.assertNotNull(pp3);
        Set<ProgramParticipant> ppList = aggManager.getFlatAncestor(pp3);
    	Assert.assertNotNull(ppList);
    	Assert.assertTrue(ppList.size() == 2);
    	String name = ppList.iterator().next().getName();
    	String participantName = ppList.iterator().next().getParticipantName();
    	Assert.assertNotNull(name);
    	Assert.assertTrue(pp1.getName().equals(name) || pp2.getName().equals(name));
    	Assert.assertTrue(pp1.getParticipantName().equals(participantName) || pp2.getParticipantName().equals(participantName));
    }

    @Test
    public void testGetFlatDescendants() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	Assert.assertNotNull(pp3);
    	aggManager.addChild(pp1, pp2);
    	aggManager.addChild(pp2, pp3);
    	pp1 = programParticipantManager.getProgramParticipant(CPP, p1Name, false);
        Assert.assertNotNull(pp1);
    	pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
        pp3 = programParticipantManager.getProgramParticipant(CPP, p3Name, false);
        Assert.assertNotNull(pp3);
        Set<ProgramParticipant> ppList = aggManager.getFlatDescendants(pp1);
    	Assert.assertNotNull(ppList);
    	Assert.assertTrue(ppList.size() == 2);
    	String name = ppList.iterator().next().getName();
    	String participantName = ppList.iterator().next().getParticipantName();
    	Assert.assertNotNull(name);
    	Assert.assertTrue(pp2.getName().equals(name) || pp3.getName().equals(name));
    	Assert.assertTrue(pp2.getParticipantName().equals(participantName) || pp3.getParticipantName().equals(participantName));
    }


    
    @Test
    public void testIsAncestor() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	aggManager.addChild(pp1, pp2);
    	pp1 = programParticipantManager.getProgramParticipant(CPP, p1Name, false);
        Assert.assertNotNull(pp1);
    	pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
    	Assert.assertTrue(aggManager.isAncestor(pp1,pp2));
    }
    

    @Test
    public void testIsDescendant() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	aggManager.addChild(pp1, pp2);
    	pp1 = programParticipantManager.getProgramParticipant(CPP, p1Name, false);
        Assert.assertNotNull(pp1);
    	pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
    	Assert.assertTrue(aggManager.isDescendant(pp1,pp2));
    }

    
    @Test
    public void testRemoveChildren() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	aggManager.addChild(pp1, pp2);
        pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
    	ProgramParticipant pp = aggManager.getAncestor(pp2);
    	Assert.assertNotNull(pp);
    	Assert.assertEquals(pp1.getName(), pp.getName());
    	Set<ProgramParticipant> children = new HashSet<ProgramParticipant>();
    	children.add(pp2);
        pp1 = programParticipantManager.getProgramParticipant(CPP, p1Name, false);
        Assert.assertNotNull(pp1);
    	aggManager.removeChildren(pp1, children);
    	pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
    	pp = aggManager.getAncestor(pp2);
    	Assert.assertNull(pp);
    	
    }
    

    
    @Test
    public void testRemoveParent() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	aggManager.addChild(pp1, pp2);
        pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
    	ProgramParticipant pp = aggManager.getAncestor(pp2);
    	Assert.assertNotNull(pp);
    	Assert.assertEquals(pp1.getName(), pp.getName());
    	aggManager.removeParent(pp2);
    	pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
    	pp = aggManager.getAncestor(pp2);
    	Assert.assertNull(pp);
    	
    }
    

    @Test
    public void testSetParent() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	aggManager.setParent(pp2, pp1);
        pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
    	ProgramParticipant pp = aggManager.getAncestor(pp2);
    	Assert.assertNotNull(pp);
    	Assert.assertEquals(pp1.getName(), pp.getName());
    	
    }

    
    @Test
    public void testGetRoot() {
    	Assert.assertNotNull(pp1);
    	Assert.assertNotNull(pp2);
    	aggManager.setParent(pp2, pp1);
        pp2 = programParticipantManager.getProgramParticipant(CPP, p2Name, false);
        Assert.assertNotNull(pp2);
    	ProgramParticipant pp = aggManager.getRoot(pp2);
    	Assert.assertNotNull(pp);
    	Assert.assertEquals(pp1.getName(), pp.getName());
    	
    }
    
    
}
