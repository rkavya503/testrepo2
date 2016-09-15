package com.akuacom.pss2.core;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;

/**
 * This class test for program related stuff only.
 */
public class ProgramManagerTest extends ParticipantManagerFixture {
    private static final String DEMO = "DEMO";

    private boolean ignore;

    @Before
    public void checkProgram() {
        ignore = programManager.getProgram(DEMO) == null;
    }

    @Ignore(value = "No proper method to call for now")
    @Test
    public void testCreateProgram() {
        
    }

    @Test
    public void testAddParticipant() {
        if (ignore) return;
        
        final Participant p0 = ParticipantManagerTest.generateRandomPersistedParticipant();
        String participantName = p0.getParticipantName();
        
       	//programManager.removeParticipantFromProgram(DEMO, participantName, false);//Test checkin	
        
        
		try {
			programParticipantManager.addParticipantToProgram(DEMO, participantName, false);
			
			final List<String> stringList = pm.getParticipantsForProgram(DEMO);
			boolean found = false;
			for (String s : stringList) {
			    if (s.equals(participantName)) {
			        found = true;
			        break;
			    }
			}
			Assert.assertTrue(found);

			final List<String> stringList1 = pm.getProgramsForParticipant(participantName, false);
			Assert.assertEquals("one and only one program is expected", 1, stringList1.size());
			for (String s : stringList1) {
			    if (!s.equals(DEMO)) {
			        Assert.fail("added to the wrong program.");
			    }
			}
		} finally {
			 try{
			 pm.removeParticipant(participantName);
			 }catch(Exception e){
				 
			 }
		}
    }
    
    @Test
    @Ignore
    public void testCloneProgram() {
        if (ignore) return;

        try {
        	Program existing = programManager.getProgramWithAllConfig(DEMO);
        	Program newOne = programManager.copyProgram(existing.getUUID(), "DEMO_CLONE1");
        	assertNotNull(newOne);
        	
        	//programManager.removeProgram("DEMO_COPY");
		} finally {

		}

       
    }
}
